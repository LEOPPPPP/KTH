/*GissaTal.java
 *Laboration 2, 3 November 2011
 *Mattias Cederlund
 *mcede@kth.se
 */

import java.util.*;

public class GissaTal {
	public static void main(String[] arg) {
		Scanner sc = new Scanner(System.in);
		int tal = (int) (Math.random() *  101); //Det r�tta talet, slumpas mellan 0 och 100
		int gissning = 0;
		LinkedList<Integer> g = new LinkedList<Integer>();
		int i;
		System.out.println("Gissa ett tal mellan 0-100");
		//Spelet k�rs s� l�nge man inte gissat r�tt och max 10 g�nger
		for (i = 0; tal != gissning && i < 10; i++) {
			//L�s in n�sta tal fr�n tangentbordet
			gissning = sc.nextInt();
			//Spara gissningen till listan g
			g.add(gissning);
			//Spelaren gissar r�tt
			if (gissning == tal) {
				System.out.println("Du gissade r�tt! Det r�tta talet var: " + tal);
			}
			//Spelaren gissar f�r l�gt
			else if (gissning < tal) {
				System.out.println("Du gissade f�r l�gt. Talet �r h�gre �n: " + gissning + "\nDu har " + (9-i) + " gissningar kvar.");
			}
			//Spelaren gissar f�r h�gt
			else if (gissning > tal) {
				System.out.println("Du gissade f�r h�gt. Talet �r l�gre �n: " + gissning + "\nDu har " + (9-i) + " gissningar kvar.");
			}
		}
		//Om spelaren gissat 10 g�nger utan att gissa r�tt, ge det r�tta talet
		if (i >= 9) {
			System.out.println("Du har gjort 10 gissningar utan att gissa r�tt. Det r�tta talet �r: " + tal);
		}
		//Skriva ut spelarens gissningar i samma f�ljd som de gjordes
		System.out.print("Dina gissningar: ");
		for (int j = 0; j < g.size()-1; j++) {
			System.out.print(g.get(j) + ", ");
		}
		System.out.print(g.get(g.size()-1)); //Sista gissningen utanf�r for-satsen f�r att slippa ett "," i slutet ;)
	}
}