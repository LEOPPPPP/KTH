import java.util.*;
public class Factors {
	HashTable<Integer, int[]> table = new HashTable<Integer, int[]>(10000);
	//Konstruktor - kallar p� funktionen som l�gger till tal och faktorisering i hashtabellen.
	public Factors() {
		createTable();
	}
	//L�gger till alla tal mellan 0 och 10000 till tabellen, med deras faktoriseringar.
	public void createTable() {
		for (int i = 0; i < 10000; i++) {
			table.put(i, getFactors(i));
		}
	}
	//Metoden shorten - returnerar ett f�rkortat br�k
	public String shorten(int dividend, int divisor) {
		//H�mta t�ljare och n�mnares faktoriseringar ur hashtabellen.
		int[] dividendFactors = table.get(dividend).clone();
		int[] divisorFactors = table.get(divisor).clone();
//		System.out.println(Arrays.toString(dividendFactors));
//		System.out.println(Arrays.toString(divisorFactors));
		//G� igenom faktoriseringarna. P�tr�ffas samma fxaktor i b�de t�ljaren och n�mnaren "stryks" de, f�rkorats till en etta.
		for (int i = 0; i < dividendFactors.length; i++) {
			for (int j = 0; j < divisorFactors.length; j++) {
				//Om lika faktorer p�tr�ffas, �ndra b�da till 1 och avbryt inre loopen.
				if (dividendFactors[i] == divisorFactors[j]) {
					dividendFactors[i] = 1;
					divisorFactors[j] = 1;
					break;
				}
			}
		}
//		System.out.println(Arrays.toString(dividendFactors));
//		System.out.println(Arrays.toString(divisorFactors));
		int dividendTotal = 1;
		int divisorTotal = 1;
		//Multiplicera alla faktorer i t�ljaren.
		for (int i = 0; i < dividendFactors.length; i++) {
			dividendTotal = dividendTotal *  dividendFactors[i];
		}
		//Multiplicera alla faktorer i n�mnaren.
		for (int i = 0; i < divisorFactors.length; i++) {
			divisorTotal = divisorTotal * divisorFactors[i];
		}
		//Returnera representation av br�ket.
		return dividendTotal + " / " + divisorTotal;
	}
	//getPrimes - returnerar en array med alla primtal inom ett intervall.
	public int[] getPrimes(int start, int stop) {
		//Tempor�r array f�r att lagra primtalen, fel antal platser d� det �nnu �r ok�nt.
		int[] temp = new int[stop-start];
		int count = 0;
		//S� l�nge man inte �verstiger h�gsta v�rdet, s�k upp talet i hashtabellen
		//och unders�k om det �r ett primtal, om arrayen som lagras bara har ett element.
		for (int i = start; i < stop; i++) {
			//�r det ett primtal, l�gg till talet till den tempo�r�ra arrayen.
			if (table.get(i).length == 1) {
				temp[count] = table.get(i)[0];
				count++;
			}
		}
		//N�r alla primtal hittats, skapa en array med r�tt antal platser.
		int[] out = new int[count];
		//�verf�r v�rdena i den tempo�r�ra arrayen till arrayen som ska returneras.
		for (int i = 0; i < count; i++) {
			out[i] = temp[i];
		}
		return out;
	}
	//getFactors - returnerar en int-array med alla faktorer till ett tal.
	public int[] getFactors(int in) {
		int[] factors = new int[100];
		int[] out;
		int divisions = 0;
		int i;
		//S� l�nge det minst g�r att rela in p� 2.
		for (i = 2; i < in; i++) {
			//Om talet �r delbart p� n�got tal.
			if (in % i == 0) {
				//L�gg till det till arrayen och peka ut n�sta plats.
				factors[divisions] = i;
				divisions++;
				//Dela begynnelsev�rdet med faktorn.
				in = in / i;
				//S�tt i till 1 f�r att i n�sta varv i loopen forts�tta p� minsta m�jliga faktor.
				i = 1;
			}
		}
		//Skapa array med r�tt antal platser f�r att returnera.
		out = new int[divisions+1];
		//Om det gjorts divisioner, allts� om talet inte �r ett primtal.
		if (divisions != 0) {
			//L�gg till sista faktorn
			factors[divisions] = i;
			//Skriv faktorerna fr�n den tempor�ra arrayen till den som ska returneras, med r�tt antal platser.
			for (i = 0; i < factors.length && factors[i] != 0; i++) {
				out[i] = factors[i];
			}
		}
		//Om primtal, skriv till f�rsta platsen i arrayen som kommer ha 1 plats.
		else {
			out[0] = in;
		}
		return out;
	}
	public static void main(String[] arg) {
		Factors f = new Factors();
		System.out.println(Arrays.toString(f.getPrimes(100,200)));
		System.out.println(Arrays.toString(f.getPrimes(1009,1200)));
		System.out.println(f.shorten(12, 120));
		System.out.println(f.shorten(12, 60));
	}
}