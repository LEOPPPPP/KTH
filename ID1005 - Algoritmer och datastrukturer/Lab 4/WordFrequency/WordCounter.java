import java.io.*;
import java.util.*;

public class WordCounter {
	static HashTable<String, Integer> frequency = new HashTable<String, Integer>(10000);

//	public static void wordCounter(String input) {
//		Scanner sc = new Scanner(input);
//        sc.useDelimiter("[^A-Za-z������]+");
//		while (sc.hasNext()) {
//			String key = sc.next();
//			Integer num = frequency.get(key);
//			if (num != null) {
//				frequency.put(key, (int) num+1);
//			}
//			else {
//				frequency.put(key, 1);
//			}
//		}
//		System.out.println(frequency);
//	}
	//wordCounter - l�ser in alla ord i en fil och l�gger till dem i en hashtabell.
	//Finns ordet i hashtabellen uppdateras f�rekomstsr�knaren med 1 och det nya v�rdet l�ggs till i tabellen.
	public static void wordCounter(File inputFile) throws IOException {
		Scanner sc = new Scanner(inputFile);
        sc.useDelimiter("[^A-Za-z������]+");
        //S� l�nge det finns fler ord i filen.
		while (sc.hasNext()) {
			String key = sc.next();
			//H�mta v�rdet med ordet som nyckel.
			Integer num = frequency.get(key);
			//Om man f�r ett v�rde, har ordet redan f�rekommit, och man l�gger till +1 till v�rdet.
			if (num != null) {
				frequency.put(key, (int) num+1);
			}
			//Annars l�gger man till ett nytt element i hashtabellen med v�rdet 1.
			else {
				frequency.put(key, 1);
			}
		}
//		System.out.println("TEST");
		//Skriv de 100 mest frekventa till fil.
		frequency.mostFrequentToFile();
//		System.out.println("TEST");
//		System.out.println(Arrays.toString(frequency.sortedArray()));
//		System.out.println(frequency);
	}

	public static void main(String[] arg) throws IOException {
//		String input = "Ett program l�ser en bok, och best�mmer de hundra mest frekventa orden i boken. Programmet skriver sedan ut dessa ord och deras frekvenser till en fil. Orden ordnas i filen enligt deras frekvenser.";
		wordCounter(new File("inputText.txt"));
//		frequency.toFile();
//		System.out.println(frequency.get("ett"));
//		frequency.remove("ett");
//		System.out.println(frequency.get("ett"));
//		frequency.put("ett", 1);
//		System.out.println(frequency.get("ett"));
	}
}