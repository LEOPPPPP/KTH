import java.io.*;
import java.util.*;

public class MorseTranslator {
	HashTable<String, String> table = new HashTable<String, String>(100);
	//Konstruktor, anropar loadTranslation som laddar in
	public MorseTranslator() throws IOException {
		loadTranslation();
	}
	//loadTranslation - H�mtar morse-alfabetet fr�n en fil och l�gger till i en hashtabell.
	public void loadTranslation() throws IOException {
		Scanner sc = new Scanner(new File("morse.txt"));
		//S� l�nge det finns mer i filen.
		while (sc.hasNext()) {
			String letter = sc.next();
			String morse = sc.next();
			//L�gg till en �vers�ttning i hashtabellen. Filen �r formaterad s� att p� varje rad
			//st�r f�rst bokstaven och sedan morse�vers�ttningen. L�gg �ven till �vers�ttning �t andra h�llet.
			table.put(letter, morse);
			table.put(morse, letter);
		}
//		System.out.println(table.toString());
	}
	//toMorse - returnerar en str�ng �versatt till morsekod.
	public String toMorse(String in) {
		//S�tter str�ngen till UPPERCASE
		in = in.toUpperCase();
		String out = "";
		//G�r igenom alla tecken i str�ngen.
		for (int i = 0; i < in.length(); i++) {
			//Om tecknet �r ett mellanslag, l�gg till mellanslag.
			if (in.charAt(i) == ' ') {
				out += " ";
			}
			//Annars leta upp �vers�ttningen i hashtabellen med bokstaven som key.
			else {
				//L�gg till �vers�ttningen till str�ngen som ska returneras.
				out += table.get(Character.toString(in.charAt(i))) + " ";
			}
		}
		return out;
	}
	//toABC - returnerar en str�ng �versatt fr�n morsekod till bokst�ver, ABC.
	public String toABC(String in) {
		Scanner sc = new Scanner(in);
		sc.useDelimiter("[^-.]+");
		String out = "";
		//S� l�nge det finns fler morse-tecken i str�ngen
		while(sc.hasNext()) {
			//L�gg till �vers�ttningen till out.
			out += table.get(sc.next());
		}
		return out;
	}
	public static void main(String[] arg) throws IOException {
		MorseTranslator m = new MorseTranslator();
		System.out.println(m.toMorse("ABCDEFGHIJKLMNOPQSTUVWXYZ123456789"));
		System.out.println(m.toABC(".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --- .--. --.- ... - ..- ...- .-- -..- -.-- --.. .---- ..--- ...-- ....- ..... -.... --... ---.. ----."));
		System.out.println(m.toMorse("hej tihi"));
		System.out.println(m.toABC(m.toMorse("hej tihi")));
	}
}