/*
 *
 * NAME:
 *   smallshell - a simple shell
 * 
 * SYNTAX:
 *   smallshell
 *
 * DESCRIPTION:
 *   Running smallshell will open a simple shell that handles two
 *   internal commands, cd and exit. You can also run external
 *   commands such as ls. To run a command as a background process
 *   you specify & as the last parameter.
 *
 * NOTES:
 * 	 Ctrl-c will cause smallshell to exit if executed when a foreground
 * 	 process is not running. If a foreground process is running,
 * 	 a interupt signal will be sent to the foreground process causing
 * 	 it to terminate.
 *
 */

#include <sys/types.h> /*definierar typen pid_t*/
#include <sys/wait.h> /*definierar bland annat WIFEXITED*/
#include <errno.h> /*definierar errno*/
#include <stdio.h> /*definierar bland annat stderr*/
#include <stdlib.h> /*definierar bland annat rand() och RAND_MAX*/
#include <string.h> /*definierar stringfunktioner som bl.a. strcmp och strlen*/
#include <sys/time.h> /*definierar funktioner f�r att h�mta tid*/
#include <signal.h> /*deinierar signalnamn med mera*/
#include <unistd.h> /*definierar bl.a. fork*/

int childpid;

/*register_signalhandler
 * 
 * register_signalhandler will register a signal handler for specified
 * signal and function to run on signal
 * 
 * @ signal_code - the signalcode to run handler on
 * @ *handler - the hanlder function to run on signal
 * 
 * */
void register_signalhandler(int signal_code, void (*handler)(int sig)) {
	int returnvalue;
	struct sigaction signal_parameters;
	signal_parameters.sa_handler = handler;
	sigemptyset(&signal_parameters.sa_mask);
	signal_parameters.sa_flags = 0;

	returnvalue = sigaction(signal_code, &signal_parameters, (void *) 0);
	if (returnvalue == -1) {
		perror("sigaction() failed");
	}
}

/*wait_for_child
 * 
 * wait_for_child will wait for a specified child process to terminate
 * and will print error messages if it terminated with an error.
 * 
 * @pid - the pid to run wait with.
 * 
 * */

void wait_for_child(pid_t pid) {
	int status; /*f�r returv�rden fr�n child-processer*/
	childpid = waitpid(pid, &status, 0);
	if (childpid == -1) {
		perror("wait() failed unexpectedly"); 
	}

	/*Child-processen har k�rt klart*/
	if (WIFEXITED(status)) { 
		int child_status = WEXITSTATUS(status);
		/*Om child-processen hade problem*/
		if (child_status != 0) { 
			fprintf(stderr, "Child (pid %ld) failed with exit code %d: %s\n",
					(long int) childpid, child_status, strerror(child_status));
		}
	}
	else {
		/*Om child-processen avbr�ts av signal*/
		if (WIFSIGNALED(status)) { 
			int child_signal = WTERMSIG(status);
			fprintf(stderr, "Child (pid %ld) was terminated by signal no. %d\n",
					(long int) childpid, child_signal);
		}
	}
}

/*poll_background_child
 * 
 * poll_background_child performs a non blocking waitpid for any process
 * and will print error messages if something terminated with an error.
 *  
 * */

void poll_background_child() {
	int status; /*f�r returv�den fr�n child-processer*/
	
	/*S� l�nge det finns childprocesser som �ndrat status*/
	while ((childpid = waitpid(-1, &status, WNOHANG)) > 0) { 
		if (childpid > 0) {
			/*Child-processen har k�rt klart*/
			if (WIFEXITED(status)) { 
				int child_status = WEXITSTATUS(status);
				/*Om child-processen hade problem*/
				if (child_status != 0) { 
					fprintf(stderr, "Child (pid %ld) failed with exit code %d: %s\n",
							(long int) childpid, child_status, strerror(child_status));
				}
				else {
					printf("Backgroundprocess %d terminated\n", childpid);
				}
			}
			else {
				/*Om child-processen avbr�ts av signal*/
				if (WIFSIGNALED(status)) { 
					int child_signal = WTERMSIG(status);
					fprintf(stderr, "Child (pid %ld) was terminated by signal no. %d\n",
							(long int) childpid, child_signal);
				}
			}
		}
	}
}

/*kill_child
 * 
 * kill_child sends the signal SIGTERM to the current child foreground process
 * 
 * */

void kill_child() {
	kill(childpid, SIGKILL);
}

int main() {
	char buffer[80]; /*Buffer till fgets*/
	char * input; /*F�r att spara input fr�n fgets*/
	int returnvalue; /*Variabel f�r att spara returv�rden fr�n systemanrop*/
	struct timeval tv; /*Anv�nds f�r att h�mta ut timestamps*/
	struct timezone tz; /*Anv�nds f�r att h�mta ut timestamps med gettimeofday*/
	long starttime; /*F�r att spara tidpunkten d� en f�rgrundsprocess startat*/
	long endtime; /*F�r att spara tidpunkten d� en f�rgrundsprocess avslutat*/
	long diff; /*F�r att spara ner tidsskillnaden mellan start och sluttid*/
	int runinbackground = 0; /*F�r att spara om process ska k�ra i bakgrunden eller inte*/

	sigset_t set; /*Set av signaler som vi ska blockera*/
	sigemptyset(&set);
	sigaddset(&set, SIGCHLD); /*L�gg till SIGCHLD till signalerna som ska blockeras*/

	/*Om vi ska anv�nda signaldetection, registrera signalhanterare*/
#ifdef SIGNALDETECTION
	register_signalhandler(SIGCHLD, poll_background_child);
	printf("USING SIGNAL DETECTION\n");
#endif

	register_signalhandler(SIGINT, kill_child); /*Starta signalhanterare f�r att ta hand om ctrl-c*/

	while (1) {
		printf("smallshell> ");

		/*Om vi anv�nder signaldetection, blockera signaler medan inl�sning av indata*/
#ifdef SIGNALDETECTION
		returnvalue = sigprocmask(SIG_BLOCK, &set, NULL);
		if (returnvalue == -1) {
			perror("Could not block signals");
		}
#endif
		input = fgets(&buffer[0], 70, stdin); /*L�s in en rad*/
		while(input[0] == ' ') input++; /* Ta bort inledande space */
		int i = (int) strlen(input) - 1; /*Hitta radens sista index*/
		/*Om inmatningen är annat än tom*/
		if (i > 0) {
			input[i] = '\0'; /*Radens sista tecken, \n ska bytas ut mot \0*/
			char * args[6];
			args[0] = strtok(input, " "); /*Starta uppdelning av parametrarna till args*/
			int k = 1;
			/*L�gger till resterande del av parametrarna till args*/
			while ((args[k] = strtok(NULL, " ")) != NULL) { 
				if(strcmp(args[k], "&") == 0) {
					runinbackground = 1;
					args[k] = (char *) 0;
				}
				k++;
			}
			args[k] = (char *) 0; /*Nollst�ll biten efter sista parametern*/

#ifdef SIGNALDETECTION
			returnvalue = sigprocmask(SIG_UNBLOCK, &set, NULL);
			if (returnvalue == -1) {
				perror("Could not block signals");
			}
#endif

			/*Om internt kommando exit, avsluta smallshell*/
			if (strcmp(args[0], "exit") == 0) {
				printf("smallshell exiting\n");
				exit(0);
			}
			/*Om internt kommando cd, byt directory till inl�st dir*/
			else if (strcmp(args[0], "cd") == 0) {
				returnvalue = chdir(args[1]);
				if (returnvalue == -1) { /*Om det misslyckas, byt till HOME, som l�ses fr�n environmentvariablerna*/
					char* home = getenv("HOME");
					returnvalue = chdir(home);
					if (returnvalue == -1) {
						printf("Could not change working directory");
					}
				}
			}
			/*Annars externt kommando, skapa childprocess*/
			else {
				childpid = fork(); /*Forka ny child-process*/
				if (childpid == 0) {
					execvp(args[0], args); /*Exekvera inmatat kommando*/
					perror("Cannot execute the given command");
					exit(1); 
				} 
				else if (childpid == -1) { /*Om fork misslyckades, meddela fel*/
					perror("Cannot fork()"); 
				}
				else { 
					/*Annars, om parentprocess och child �r f�rgrundsprocess, v�nta p� child*/
					if (runinbackground == 0) {
						/*L�s ut starttid fr�n gettimeofday*/
						returnvalue = gettimeofday(&tv, &tz); 
						if (returnvalue == -1) {
							perror("Could not get starttime");
						}
						starttime = (long) tv.tv_usec;
						printf("Spawned foreground process pid: %d\n", childpid);
						wait_for_child(childpid); /*V�nta p� att childprocessen ska returnera*/
						printf("Foreground process %d terminated\n", childpid);
						/*L�s ut sluttid fr�n gettimeofday*/
						returnvalue = gettimeofday(&tv, &tz);
						if (returnvalue == -1) {
							perror("Could not get starttime");
						}
						endtime = (long) tv.tv_usec;
						/*R�kna ut skillnaden menllan start och sluttid och skriv ut resultatet*/
						diff = endtime - starttime;
						printf("Wallckock time: %li microseconds\n", diff); 
					}
					else {
						/*Om bakgrundsprocess, skriv ut info och nollst�ll bakgrundsprocess-flaggan*/
						printf("Spawned background process pid: %d\n", childpid);
						runinbackground = 0;
					}
				}
			}
		}
		/*Om signaldetection inte anv�nds, polla efter �ndrade statusar fr�n bakgrundsprocesser*/
#ifndef SIGNALDETECTION
		poll_background_child();
#endif
	}
	return 0;
}
