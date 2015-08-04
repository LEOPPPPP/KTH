#include <stdlib.h>
#include <stdio.h>
#include "nod.h"
#include "nod.h" //Testar include guards!

void writePost(Post * p) {
	printf("Namn: %s\nBMI: %.2f\n",p->name, p->bmi);
}
/*Loopar igenom hela listan och kallar p� writePost f�r att skriva ut de enskilda elementen*/
void writeList(Post * p) {
	if (p == NULL) {
		printf("Listan �r tom.");
	}
	while(p != NULL) {
		writePost(p);
		p = p->next;
	}
}
/*Loopar igenom listan och j�mf�r givet element med listans element.
Om lika element p�tr�ffas returneras toppen av listan, allts� elementet*/
Post * find(int (* compare) (Post *  a, Post * b), Post sought, Post * list) {
	if (list == NULL) {
		printf("Listan �r tom.");
		return NULL;
	}
	Post *temp = (Post *) malloc(sizeof(Post));
	*temp = sought;
	while (list != NULL) {
		if ((* compare)(temp, list) == 0) {
			break;
		}
		list = list->next;
	}
	free(temp);
	return list;
}
/*Anv�nder inbyggd funktion f�r att j�mf�ra strings*/
int compare_names(Post * a, Post * b) {
	return strcmp(a->name, b->name);
}
/*Om lika returnera 0, a st�rre 1, b st�rre -1*/
int compare_bmi(Post * a, Post * b) {
	if (a->bmi == b->bmi) {
		return 0;
	}
	else if (a->bmi > b->bmi) {
		return 1;
	}
	else {
		return -1;
	}
}
/*L�ser in data och allokerar nytt element- Datan l�ggs till elementet och elementet
l�ggs till i b�rjan av listan. Det nya elementets next-pekare pekar p� det tidigare
f�rsta elementet och toppen av listan pekar nu p� det nya elementet, precis som i load_names*/
void insert(Post ** list) {
	Post * temp = (Post *) malloc(sizeof(Post));
	char name[SIZE];
	float length;
	float weight;
	printf("\nVad heter personen? ");
	scanf("%s", &name);
	printf("\nHur l�ng �r personen? (m) ");
	scanf("%f", &length);
	printf("\nHur mycket v�ger personen? (kg) ");
	scanf("%f", &weight);
	strcpy(temp->name, name);
	temp->bmi = weight / (length * length);

	temp->next = *list;
	*list = temp;
}
/*Tar bort givet element(j�mf�r dock endast namn) fr�n listan. 
De olika fallen beskrivs nedan.*/
void remove_person(Post ** list, Post * toRemove) {
	Post * curr = *list;
	Post * next = curr->next;
	
	if (curr == NULL) {
		printf("Listan �r tom.");
		return;
	}
	//Om elementet som ska tas bort ligger f�rst i listan och det inte finns n�gra fler.
	if (next == NULL) {
		if (compare_names(curr, toRemove) == 0) {
			Post *temp = curr;
			*list = NULL;
			free(temp);
			return;
		}
	}
	//Om elementet ligger f�rst i listan och det finns fler element.
	if (compare_names(curr, toRemove) == 0) {
		Post *temp = curr;
		*list = next;
		free(temp);
		return;
	}
	//Om elementet inte ligger f�rst i listan, loopa genom listan och kolla next-elementet.
	while(next != NULL) {
		if (compare_names(next, toRemove) == 0) {
			Post *temp = next;
			curr->next = next->next;
			free(temp);
			return;
		}
		curr = next;
		next = next->next;
	}
}

void load_names(char * filename, Post ** list) {
    char name[SIZE]; /* <- Ful h�rdkodning */
    float bmi;
    FILE *fil = fopen(filename, "r");
    Post * p;
    if (fil == NULL) {
	printf("Filen inte funnen.\n");
    } else {
        while (fscanf(fil, "%s %f", name, &bmi) == 2) {
            p = (Post *) malloc(sizeof(Post));
            strcpy(p->name, name);
            p->bmi = bmi;

            p->next = *list;
            *list = p;
        }
    }
	fclose(fil);
}


