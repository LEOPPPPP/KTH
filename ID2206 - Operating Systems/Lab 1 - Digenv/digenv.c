/*
 *
 * NAME:
 *   digenv - a program for easily showing environment variables
 * 
 * SYNTAX:
 *   digenv [grep arguments]
 *
 * DESCRIPTION:
 *	Digenv can be run with or without arguments. All arguments
 *	will be passed on to grep. Passing bad arguments to digenv
 *	will cause grep to terminate with an error and by that way
 *	terminate digenv with an error.
 *
 * 	If run without arguments, digenv will execute the following:
 *	printenv | sort | less
 *	
 *	If run with arguments, digenv will execute the following:
 *	printenv | grep [arguments] | sort | less
 *
 * EXAMPLES:
 *  digenv
 *	digenv PATH
 *
 * SEE ALSO:
 *   printenv(1), grep(1), sort(1), less(1)
 *
 */

#include <sys/types.h> /*definierar typen pid_t*/
#include <sys/wait.h> /*definierar bland annat WIFEXITED*/
#include <errno.h> /*definierar errno*/
#include <stdio.h> /*definierar bland annat stderr*/
#include <stdlib.h> /*definierar bland annat rand() och RAND_MAX*/
#include <unistd.h> /*definierar bland annat pipe() och STDIN_FILENO*/

#define PIPE_READ_SIDE (0)
#define PIPE_WRITE_SIDE (1)
#define DEBUG (0)

pid_t childpid; /*f�r child-processens PID vid fork()*/
pid_t grep_pid; /*F�r att spara greps pid, f�r att skriva vettigt errormsg om fel d�r*/

/*close_pipes
 *
 *close_pipes closes both ends of a given pipe
 *
 *int * pipe - the filedescriptor for the pipe to close
 *
 */
void close_pipes(int * pipe) {
	int return_value;

	/*St�ng pipens l�s-sida och kolla output fr�n anropet*/
	return_value = close(pipe[ PIPE_READ_SIDE ]);
	if (return_value == -1) {
		perror("Cannot close read end"); 
		exit(1); 
	}

	/*St�ng pipens skriv-sida och kolla output fr�n anropet*/
	return_value = close(pipe[PIPE_WRITE_SIDE]);
	if (return_value == -1) {
		perror("Cannot close write end"); 
		exit(1); 
	}
}

/*wait_for_child
 *
 *close_pipes waits for a child process to terminate
 *If an error occurs wait_for_child will print information to STDERR
 *
 */
void wait_for_child() {
	int status; /*f�r returv�rden fr�n child-processer*/

	childpid = wait(&status);
	if (DEBUG) {
		fprintf(stderr, "Process (pid %ld) exited\n", (long int) childpid);
	}
	if (childpid == -1) {
		perror("wait() failed unexpectedly"); 
		exit(1); 
	}

	/*Child-processen har k�rt klart*/
	if (WIFEXITED(status)) { 
		int child_status = WEXITSTATUS(status);
		/*Om child-processen hade problem*/
		if (child_status != 0) { 
			fprintf(stderr, "Child (pid %ld) failed with exit code %d\n",
					(long int) childpid, child_status);
			if (childpid == grep_pid) {
				fprintf(stderr, "Grep could not find anything with that input!\n");
			}
			exit(0);
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

int main(int argc, char **argv, char **envp) {
	int pipe_getenv_grep[2]; /*f�r fildeskriptorer fr�n pipe(2)*/
	int pipe_getenv_sort[2]; 
	int pipe_grep_sort[2];
	int pipe_sort_less[2];
	int return_value; /*f�r returv�rden fr�n systemanrop*/
	int use_grep = 0; /*Flagga som s�tts till 1 om vi ska anv�nda grep*/
	char* grep_args[argc+1]; /*Beh�llare f�r argumenten till grep*/
	char* pager; /*Beh�llare f�r pager*/

	/*L�s pager fr�n environment-variablerna. Om Pager inte finns, anv�nd less*/
	pager = getenv("PAGER");
	if (pager == NULL) {
		pager = "less";
	}

	/*Om det finns parametrar till grep*/
	if (argc > 1) {
		use_grep = 1;					/*S�tt flaggan f�r att anv�nda grep*/
		int i;
		grep_args[0] = "grep"; 			/*F�rsta argumentet �r namnet p� programmet som ska k�ras*/
		for (i = 1; i < argc; i++) {
			grep_args[i] = argv[i]; 	/*Kopiera �ver argumenten*/
		}
		grep_args[argc] = (char *) 0; 	/*Avsluta argument-arrayen med nollst�lld byte*/
	}

	/*Skapa pipe beroende p� om grep ska k�ras*/
	if (use_grep) {
		return_value = pipe(pipe_getenv_grep); /*Skapa en pipe*/
	}
	else {
		return_value = pipe(pipe_getenv_sort); 
	}

	if (return_value == -1) { /*Om pipe() misslyckades*/
		perror("Cannot create pipe"); 
		exit(1); 
	}

	childpid = fork(); 	/*Skapa ny process med fork*/
	if (DEBUG) {
		fprintf(stderr, "PRINTENV forked (pid %ld)\n", (long int) childpid);
	}
	/*Kod som k�rs av child-processen*/
	if (0 == childpid) { 
		if (use_grep) {
			/*Dupicera fildeskriptor s� att processen skriver till pipe_getenv_grep ist�llet f�r stdout, kolla returv�rde*/
			return_value = dup2(pipe_getenv_grep[ PIPE_WRITE_SIDE ], STDOUT_FILENO); 
			if (return_value == -1) { 
				perror("Cannot dup printenv_grep write"); 
				exit(1); 
			}
			close_pipes(pipe_getenv_grep); 	/*St�ng pipens �ndar*/
		}
		else {
			/*Om vi inte ska anv�nda grep - duplicera annan pipe, kolla returv�rde*/
			return_value = dup2(pipe_getenv_sort[ PIPE_WRITE_SIDE ], STDOUT_FILENO);
			if (return_value == -1) { 
				perror("Cannot dup printenv_sort write"); 
				exit(1); 
			}

			close_pipes(pipe_getenv_sort);
		}
		(void) execlp("printenv", "printenv", (char *) 0); 	/*Exekvera printenv*/
		perror("Cannot exec printenv"); 
		exit(1);

	}
	/*Om fork misslyckades*/
	if (childpid == -1) {
		perror("Cannot fork()"); 
		exit(1); 
	}

	wait_for_child(); 	/*V�nta p� att printenv blir klar*/

	/*K�rs endast om vi har angett parametrar till grep*/
	if (use_grep) {
		return_value = pipe(pipe_grep_sort); 

		if (return_value == -1) { 
			perror("Cannot create pipe"); 
			exit(1); 
		}

		childpid = fork(); 
		if (DEBUG) {
			fprintf(stderr, "GREP forked (pid %ld)\n", (long int) childpid);
		}
		if (0 == childpid) {
			/*Dupicera fildeskriptor s� att processen l�ser fr�n pipe_getenv_grep ist�llet f�r stdin*/
			return_value = dup2(pipe_getenv_grep[ PIPE_READ_SIDE ], STDIN_FILENO);
			if (return_value == -1) { 
				perror("Cannot dup getenv_grep read"); 
				exit(1); 
			}

			close_pipes(pipe_getenv_grep);
			/*Dupicera fildeskriptor s� att processen skriver till pipe_grep_sort ist�llet f�r stdout*/
			return_value = dup2(pipe_grep_sort[ PIPE_WRITE_SIDE ], STDOUT_FILENO); 
			if (return_value == -1) { 
				perror("Cannot dup grep_sort write"); 
				exit(1); 
			}

			close_pipes(pipe_grep_sort);
			(void) execvp("grep", grep_args); 	/*Exekvera grep med argumenten som l�stes in som argument*/

			perror("Cannot exec grep"); 
			exit(1);
		}
		else {
			grep_pid = childpid;
		}

		if (childpid == -1) {
			perror("Cannot fork()"); 
			exit(1); 
		}
		close_pipes(pipe_getenv_grep);
	}

	/*V�nta p� grep endast om vi anv�nde grep*/
	if (use_grep) {
		wait_for_child();
	}

	return_value = pipe(pipe_sort_less);  /*Skapa pipe f�r att anv�nda mellan sort och less*/

	if (return_value == -1) {
		perror("Cannot create pipe"); 
		exit(1); 
	}

	childpid = fork(); 
	if (DEBUG) {
		fprintf(stderr, "SORT forked (pid %ld)\n", (long int) childpid);
	}
	if (0 == childpid) {
		/*Anv�nd olika pipes f�r l�sning beroende p� om vi anv�nder grep eller inte*/
		if (use_grep) { 
			return_value = dup2(pipe_grep_sort[ PIPE_READ_SIDE ], STDIN_FILENO); 
			if (return_value == -1) { 
				perror("Cannot dup grep_sort read"); 
				exit(1); 
			}

			close_pipes(pipe_grep_sort);
		}
		else {
			return_value = dup2(pipe_getenv_sort[ PIPE_READ_SIDE ], STDIN_FILENO); 
			if (return_value == -1) { 
				perror("Cannot dup getenv_sort read"); 
				exit(1); 
			}

			close_pipes(pipe_getenv_sort);
		}

		return_value = dup2(pipe_sort_less[ PIPE_WRITE_SIDE ], STDOUT_FILENO); 
		if (return_value == -1) { 
			perror("Cannot dup sort_less write"); 
			exit(1); 
		}

		close_pipes(pipe_sort_less);

		(void) execlp("sort", "sort", (char *) 0);

		perror("Cannot exec sort"); 
		exit(1);

	}

	if (childpid == -1) {
		perror("Cannot fork()"); 
		exit(1); 
	}

	/*St�ng de pipes vi �r klara med, beroende om vi anv�nt grep eller inte*/
	if (use_grep) {
		close_pipes(pipe_grep_sort);
	}
	else {
		close_pipes(pipe_getenv_sort);
	}

	wait_for_child(); /*V�nta p� sort*/

	childpid = fork();
	if (DEBUG) {
		fprintf(stderr, "LESS forked (pid %ld)\n", (long int) childpid);
	}
	if (0 == childpid) {
		return_value = dup2(pipe_sort_less[ PIPE_READ_SIDE ], STDIN_FILENO); 
		if (return_value == -1) { 
			perror("Cannot dup sort_less read"); 
			exit(1); 
		}

		close_pipes(pipe_sort_less);

		(void) execlp(pager, pager, (char *) 0);

		perror("Cannot exec pager, will retry with more"); 

		(void) execlp("more", "more", (char *) 0);	/*Om vald pager misslyckades - exekvera more ist�llet*/

		perror("Cannot exec more"); 
		exit(1);

	}

	if (childpid == -1) {
		perror("Cannot fork()"); 
		exit(1); 
	}

	close_pipes(pipe_sort_less); /*St�ng pipen mellan sort och less*/

	wait_for_child(); /*V�nta p� less*/

	exit(0);  /*Avsluta parent-processen p� normalt s�tt*/
}
