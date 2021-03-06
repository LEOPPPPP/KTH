#include <stdlib.h>
#include <stdio.h>
#include "nod.h"

void skrivMeny() {
    printf("\n*******************  MENY *********************\n\n");
    printf("\t 1 Ladda namn fr�n fil\n");
    printf("\t 2 Ny person\n");
    printf("\t 3 S�k namn\n");
    printf("\t 4 S�k personer med bmi\n");
    printf("\t 5 Skriv ut hela listan\n");
    printf("\t 6 Ta bort person\n");
    printf("\t 0 Avsluta\n\n");
    printf("\t Vad vill du g�ra? ");
}

int main(int argc, char * argv[]) {

    Post * list = NULL;
    int menyval = 1;
    Post tmp;
    Post * tmp_pek;

    printf("Hej och v�lkommen till Viktm�starnas meny\n");
    while (menyval > 0 && menyval <= 6) {
        skrivMeny();
        scanf("%d", &menyval);
        printf("\n");

        switch (menyval) {
        case 1:
            load_names("bmi_namn.txt", & list);
            break;
        case 2:
            insert(& list);
            break;
        case 3:
            printf("Vem s�ker du? ");
            fscanf(stdin, "%s", tmp.name);
            tmp_pek = find((*compare_names), tmp, list);
            if (tmp_pek != NULL) writePost(tmp_pek);
            else printf("Hittade inte namnet '%s'\n", tmp.name);
            break;
        case 4:
			/*L�ser in vilket bmi vi vill s�ka efter. Anropar d�refter find
			som hittar f�rsta l�sningen i listan. D�refter anropas find igen
			p� resterande lista tills att hela listan s�kts igenom. N�r l�sning
			hittats skrivs den ut direkt. */
	    	printf("Personer med vilket bmi s�ker du? ");
			fscanf(stdin, "%f", &tmp.bmi);
			tmp_pek = find((*compare_bmi), tmp, list);
			if (tmp_pek != NULL) {
				writePost(tmp_pek);
				tmp_pek = tmp_pek->next;
				while (tmp_pek != NULL) {
					tmp_pek = find((*compare_bmi), tmp, tmp_pek);
					if (tmp_pek != NULL) {
						writePost(tmp_pek);
						tmp_pek = tmp_pek->next;
					}
				}
			}
            else {
				printf("Hittade ingen med bmi '%f'\n", tmp.bmi);
			}
			
            break;
        case 5:
            writeList(list);
            break;
        case 6:
            /*Letar med find efter inmatat namn. Anropar sedan remove_person som tar bort
			givet element fr�n listan.*/
			printf("Vem vill du ta bort? ");
			fscanf(stdin, "%s", tmp.name);
            tmp_pek = find((*compare_names), tmp, list);
            if (tmp_pek != NULL) {
				printf("Tar bort %s fr�n listan.\n", tmp_pek->name);
				remove_person(&list, tmp_pek);
			}
            else {
				printf("Hittade inte namnet '%s'\n", tmp.name);
			}
            break;
        }
        
    }
	//Avallokerar hela listan n�r programmet st�ngs ner.
	Post * temp;
	while(list != NULL) {
		temp = list;
		list = list->next;
		free(temp);
	}

    printf("\n\nHej d�!\n");
    return 0;
}


