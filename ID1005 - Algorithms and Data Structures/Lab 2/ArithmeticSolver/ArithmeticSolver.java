public class ArithmeticSolver {
	flQueue<Character> operatorStack = new flQueue<Character>();
	flQueue<Double> operandStack = new flQueue<Double>();
	String sentence;

	//Konstruktor, s�tter argumentet till en klassvariabel f�r synlighet f�r resten av klassen.
	//Samt anropar solvning-metoden och skriver ut resultatet.
	public ArithmeticSolver(String in) {
		sentence = in;
		System.out.println("Svar:" + solve());
	}
	//Metoden solve, ber�knar ett aritmetiskt uttryck.
	public double solve() {
		int index = 0;
		double lastValue = 0;
		//S� l�nge inte hela uttrycket g�tts igenom.
		while (index < sentence.length()) {
			//S�tt c till att vara tecknet p� aktuell position
			char c = sentence.charAt(index);
			//Om tecknet �r en siffra
			if (Character.isDigit(c) == true) {
				double value = 0;
				int i;
				//Kolla om efterf�ljande tecken �r siffor
				for (i = index+1 ; i < sentence.length(); i++) {
					if (!Character.isDigit(sentence.charAt(i))) {
						break;
					}
				}
				//N�r f�rsta tecknet som inte �r en siffra p�tr�ffas l�gg till talet till operandstacken.
				value = Double.parseDouble(sentence.substring(index, i));
				System.out.println("Digit:" + value + " Index:" + index);
				//�ka indexpekaren till att peka p� n�sta tecken som inte ingick i talet.
				index = i;
				//L�gg operanden p� stacken.
				operandStack.putLast(value);
			}
			//Om ett + eller, l�gg det p� operatorstacken, �ka indexpekaren.
			else if (c == '+') {
				System.out.println("+:" + c + " Index:" + index);
				operatorStack.putLast(c);
				index++;
			}
			//Om ett - p�tr�ffas, l�gg det efterf�ljande negtiva operanden p� operandstacken, samt ett + p� operatorstacken.
			else if (c == '-') {
				double value = 0;
				boolean doubleminus = false;
				int i;
				//Kolla om efterf�ljande tecken �r siffor.
				for (i = index+1 ; i < sentence.length(); i++) {
					//Om n�stf�ljande tecken ocks� �r ett minustecken, hoppa fram startindexet ett steg f�r att undvika att b�da
					//minustecknen tas med n�r man f�rs�ker hitta operanden. (2 minustecken tar ut varandra och kan lika g�rna hoppas �ver.)
					if (sentence.charAt(i) == '-') {
						//Index s�tts till += 2 f�r att "hoppa �ver" b�da minustecknen.
						index += 2;
						//doubleminus-flaggan s�tts till true vilket signalerar senare att man inte ska f�rs�ka l�gga till varken operand eller operator
						doubleminus = true;
						break;
					}
					else if (!Character.isDigit(sentence.charAt(i))) {
						break;
					}
				}
				//Operator och operand ska endast l�ggas till k�erna ifall att man inte p�tr�ffat dubbla minustecken.
				if (doubleminus == false) {
					//N�r f�rsta tecknet som inte �r en siffra p�tr�ffas l�gg till talet till operandstacken.
					value = Double.parseDouble(sentence.substring(index, i));
					System.out.println("Digit:" + value + " Index:" + index);
					//�ka indexpekaren till att peka p� n�sta tecken som inte ingick i talet.
					index = i;
					//L�gg operanden p� stacken.
					operandStack.putLast(value);
					//L�gg till ett + p� operatorstacken. Att addera ett negativt tal blir samma sak som att subtrahera samma positiva tal.
				}
				operatorStack.putLast('+');
			}
			//Om et * eller / p�tr�ffas, l�gg det p� operatorstacken.
			else if (c == '*' || c == '/') {
				System.out.println("*//:" + c + " Index:" + index);
				operatorStack.putLast(c);
				double value = 0;
				int i = index+1;
				//Om efterf�ljande tecken �r ett minustecken, g� fram en position f�r att hitta n�sta operand
				if (sentence.charAt(i) == '-') {
					i++;
				}
				//Leta sedan fram efterf�ljande operand
				for (; i < sentence.length(); i++) {
					if (!Character.isDigit(sentence.charAt(i))) {
						break;
					}
				}
				value += Double.parseDouble(sentence.substring(index+1, i));
				System.out.println("Digit:" + value + " Index:" + (index+1));
				index = i;
				//Utf�r operation mellan tidigare funnen operand och nyligen funnen operand.
				operandStack.putLast(arithmeticOperation(operatorStack.takeLast(), (double) operandStack.takeLast(), value));
			}
			//Om n�got ogiltigt tecken p�tr�ffas, avsluta.
			else {
				System.out.println("Felaktiga tecken!");
				System.exit(0);
			}
		}
		//N�r hela serien g�tts igenom och alla multiplikationer och divisioner utf�rs, addera och subtrahera �terst�ende tal.
		//Om det finns fler eller lika m�nga operatorer som operander, s� �r den f�rsta operatorn ett tecken till f�rsta operanden.
//		if (operatorStack.size() >= operandStack.size()) {
//			System.out.println("Lika m�nga eller fler operatorer �n operander");
//			lastValue = arithmeticOperation(operatorStack.takeFirst(), 0, operandStack.takeFirst());
//		}
//		else {
			lastValue = operandStack.takeFirst();
//		}
		//lastValue - Senaste framr�knade v�rdet s�tts till det f�rsta v�rdet i k�n.
		//S� l�nge det finns fler operatorer, utf�r operationer p� operanderna fr�n v�nster med lastValue och n�sta operand.
		while (operatorStack.size() > 0 && operandStack.size() > 0) {
			lastValue = (arithmeticOperation(operatorStack.takeFirst(), lastValue, operandStack.takeFirst()));
		}
		return lastValue;
	}
	//Utf�r aritmetik operation, +-*/ mellan tv� tal beroende av vilken operand som skickas som argument.
	public double arithmeticOperation(char operator, double firstOperand, double secondOperand) {
		if (operator == '+') {
			return firstOperand + secondOperand;
		}
		else if (operator == '-') {
			return firstOperand - secondOperand;
		}
		else if (operator == '*') {
			return firstOperand * secondOperand;
		}
		else if (operator == '/') {
			return firstOperand / secondOperand;
		}
		return Integer.MAX_VALUE;
	}
	public static void main(String [] arg) {
		//Skapa ny solver med argumentet: vad som ska ber�knas.
		ArithmeticSolver solver = new ArithmeticSolver("-12*-8+1/5--2*3*4");
	}
}