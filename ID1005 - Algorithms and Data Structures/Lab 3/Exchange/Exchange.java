public class Exchange {
	BinaryTreeMultiset<Integer> values;
	int total;
	//Konstruktor, skapar en "v�xelautomat"
	public Exchange(Set<Integer> set) {
		values = new BinaryTreeMultiset<Integer>(set);
	}
	//Returnerar en multim�ngd med minimalt antal val�rer.
	public BinaryTreeMultiset<Integer> exchange() {
		//Adderar ihop alla val�rer i given m�ngd och anropar giveChange-rutinen.
		for (int element : values) {
			total += element;
//			values.remove();
		}
		return giveChange(total);
	}
	//giveChange - returnerar en multim�ngd med minimalt antal val�rer.
	public BinaryTreeMultiset<Integer> giveChange(int in) {
		BinaryTreeMultiset<Integer> result = new BinaryTreeMultiset<Integer>();
		//Array med alla m�jliga val�rer i sjunkande ordning.
		int[] values = {1000, 500, 100, 50, 20, 10, 5, 1};
		//K�rs s� l�nge man inte g�tt igenom alla typer av val�rer, och det finns saldo p� "kontot"
		//som ska v�xlas ut.
		for (int i = 0; i < values.length && in > 0; i++) {
			//S� l�nge man kan ge fler sedlar/mynt av samma val�r. N�r det inte l�ngre g�r
			//Byts val�ren ut mot en l�gre och man f�rs�ker igen.
			while (in >= values[i]) {
				//Dra bort val�ren fr�n saldot och l�gg till val�ren i multim�ngden.
				in = in - values[i];
				result.add(values[i]);
			}
		}
		return result;
	}
	public static void main(String[] arg) {
		//Skapa tv� arrayer med val�rer
		int[] values1 = {5,10,20,50,1,100,500,5,5,20};
		int[] values2 = {5,100,20,100,500,1000,20,1,1,1,5};
		//Skapa tv� multim�ngder och l�gg val�rerna i m�ngderna
		BinaryTreeMultiset<Integer> kassa1 = new BinaryTreeMultiset<Integer>();
		BinaryTreeMultiset<Integer> kassa2 = new BinaryTreeMultiset<Integer>();
		for(int i : values1) {
			kassa1.add(i);
		}
		for(int i : values2) {
			kassa2.add(i);
		}
		System.out.println("Ursprungliga kassa1: " + kassa1);
		System.out.println("Ursprungliga kassa2: " + kassa2);
		//Sl� ihop kassorna och cleara den kassan som nu �r tom.
		kassa1 = kassa1.union(kassa2);
		kassa2.clear();
		System.out.println("Kassan efter ihopslagningen: " + kassa1);
		//Skapa en ny bank och l�gg in kassan i banken.
		Exchange bank = new Exchange(kassa1);
		//Anropa exhange-rutinen, som ger dig minsta m�jliga multim�ngd val�rer tillbaka.
		kassa1 = bank.exchange();
		System.out.println("Kassan efter v�xlingen: " + kassa1);
	}
}