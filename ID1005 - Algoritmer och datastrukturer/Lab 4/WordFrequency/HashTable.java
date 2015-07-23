import java.io.*;
import java.util.*;
import java.lang.reflect.Array;

//Nodelement - lagrar nyckel och value, samt l�nk till n�stf�ljande nod.
class Node<E,T>  {
	E key;
	T value;
	Node nextNode;
	public Node(E keyin, T valuein) {
		key = keyin;
		value = valuein;
		nextNode = null;
	}
	public String toString(){
		return (String) key + ":"+ value;
	}
}
public class HashTable<E, T> {
	//Defaultcomparator - f�r att kunna j�mf�ra noder med varandra.
	//Nodens v�rde j�mf�rs.
	private class DefaultComparator implements Comparator<Node> {
        public int compare (Node element1, Node element2) {
            int    compareValue = 0;
			try {
				Comparable element = (Comparable) element1.value;
				compareValue = element.compareTo(element2.value);
			}
			catch (ClassCastException e){
				throw new ClassCastException();
			}
			return compareValue;
		}
    };

    Comparator comp;
	Node[] table;
	int size;
	int count;
	//Konstruktor - skapar tabell med r�tt storlek.
	public HashTable(int size) {
		this.size = size;
		comp = new DefaultComparator();
		//Skapar en array med generiska element.
		table = (Node[]) Array.newInstance(Node.class, size);
	}
	//contains - returnerar true om ett element med given nyckel finns.
	public boolean contains(E key) {
		int hash = Math.abs(key.hashCode() % size);
		Node currentNode = table[hash];
		//G�r igenom nodsekvensen p� framr�knad plats.
		while (currentNode != null) {
			//Om element med r�tt nyckel p�tr�ffas, returnera true.
			if (table[hash].key.equals(key)) {
				return true;
			}
			//Annars flytta currentpekaren till n�sta nod.
			else {
				currentNode = currentNode.nextNode;
			}
		}
		//Om inget element med nyckeln finns, returnera false;
		return false;
	}
	//Get - retrunerar value f�r nod med s�kt nyckel.
	public T get(E key) {
		T returnValue = null;
		//R�knar ut ett hashv�rde, index i arrayen som noderna lagras i.
		int hash = Math.abs(key.hashCode() % size);
		Node currentNode = table[hash];
		//G�r igenom nodsekvensen.
		while (currentNode != null) {
			//Om element med r�tt nyckel p�tr�ffas, s�tt returnValue till v�rdet och bryt loopen.
			if (currentNode.key.equals(key)) {
				returnValue = (T) currentNode.value;
				break;
			}
			//Annars flytta currentpekaren till n�sta nod.
			else {
				currentNode = currentNode.nextNode;
			}
		}
		//Returnera returnValue.
		return returnValue;
	}
	//Put - l�gger till ett element till hashtabellen
	public void put(E key, T value) {
		//R�knar ut ett hashv�rde, index i arrayen som noderna lagras i.
		int hash = Math.abs(key.hashCode() % size);
		Node currentNode = table[hash];
		Node parentNode = null;
		boolean nodeFound = false;
		//Om det inte finns n�got element p� platsen i arrayen, skapa och l�gg till.
		if (currentNode == null) {
			table[hash] = new Node(key, value);
			count++;
		}
		//Annars g� igenom alla noder p� platsen i arrayen.
		else {
			while (currentNode != null) {
				//Om nyckeln �r samma som elementets nyckel.
				if ((currentNode.key).equals(key)) {
					//Byt ut det gamla v�rdet mot det nya.
					currentNode.value = value;
					return;
				}
				//Om fel nyckel, g� till n�sta nod.
				else {
					parentNode = currentNode;
					currentNode = currentNode.nextNode;
				}
			}
			//Om det inte finns en nod med r�tt nyckel, skapa en ny nod och l�gg den f�rst i nodsekvensen.
				Node newNode = new Node(key, value);
				newNode.nextNode = table[hash];
				table[hash] = newNode;
				count++;
		}
	}
	//remove - tar bort ett element med given nyckel ur hashtabellen.
	public void remove(E key) {
		int hash = Math.abs(key.hashCode() % size);
		Node currentNode = table[hash];
		Node parentNode = null;
		//S� l�nge det finns fler noder i tabellen p� platsen.
		while (currentNode != null) {
			//Om nyckeln �r samma som elementets nyckel.
			if (currentNode.key.equals(key)) {
				if (parentNode == null) {
					table[hash] = currentNode.nextNode;
				}
				else {
					parentNode.nextNode = currentNode.nextNode;
				}
				currentNode = null;
				count--;
				break;
			}
			//Om fel nyckel, g� till n�sta nod.
			else {
				parentNode = currentNode;
				currentNode = currentNode.nextNode;
			}
		}
	}
	//toString - returnerar en stringrepresentation av hashtabellen
	public String toString() {
		String out = "";
		Node currentNode = null;
		//G�r genom hashtabellens alla index.
		for (int i = 0; i < table.length; i++) {
			currentNode = table[i];
			//currentNode b�rjar peka p� f�rsta elementet. Finns det fler element flyttas den till n�sta element.
			//Skriver sedan informationen till en String.
			while (currentNode != null) {
				out += i + ": " + (String) currentNode.key + ": " + currentNode.value + "\n";
				currentNode = currentNode.nextNode;
			}
		}
		return out;
	}
	//toFile - skriver hashtabellens inneh�ll till en fil.
	public void toFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("frequency.txt"));
		Node currentNode = null;
		//G�r genom hashtabellens alla index.
		for (int i = 0; i < table.length; i++) {
			currentNode = table[i];
			//currentNode b�rjar peka p� f�rsta elementet. Finns det fler element flyttas den till n�sta element.
			//Skriver sedan informationen i varje nod till en egen rad i en fil.
			while (currentNode != null) {
				writer.write((String) currentNode.key + ": " + currentNode.value);
				writer.newLine();
				currentNode = currentNode.nextNode;
			}
		}
		writer.close();
	}
	//toFileSorted - skriver hashtabellens inneh�ll till en fil.
	public void toFileSorted() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("frequency.txt"));
		Node[] nodes = this.toArray();
		Arrays.sort(nodes, comp);
		//G�r genom hashtabellens alla index.
		for (int i = 0; i < nodes.length; i++) {
			//currentNode b�rjar peka p� f�rsta elementet. Finns det fler element flyttas den till n�sta element.
			//Skriver sedan informationen i varje nod till en egen rad i en fil.
				writer.write((String) nodes[i].key + ": " + nodes[i].value);
				writer.newLine();
		}
		writer.close();
	}
	//mostFrequentToFile - skriver de 100 mest f�rekommande orden till en fil.
	public void mostFrequentToFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("frequency.txt"));
		Node[] nodes = sortedArray();
		//Fr�n den sorterade arrayen, ta elementen bakifr�n och skriv till filen.
		for (int i = nodes.length-1; i > nodes.length - 101; i--) {
			writer.write((String) nodes[i].key+ ": " + nodes[i].value);
			writer.newLine();
		}
		writer.close();
	}
	//toArray - returnerar en array-representation med hashtabellens element.
	public Node[] toArray() {
		Node[] nodes = (Node[]) Array.newInstance(Node.class, count);
		Node currentNode = null;
		int arrayIndex = 0;
		//G�r genom hashtabellens alla index.
		for (int i = 0; i < table.length; i++) {
			currentNode = table[i];
			//currentNode b�rjar peka p� f�rsta elementet. Finns det fler element flyttas den till n�sta element.
			//Skriver sedan noden till arrayen av noder.
			while (currentNode != null) {
				nodes[arrayIndex] = currentNode;
				currentNode = currentNode.nextNode;
				arrayIndex++;
			}
		}
		return nodes;
	}
	//sortedArray - returnerar en sorterad array-representation med hashtabellens element.
	public Node[] sortedArray() {
		Node[] nodes = this.toArray();
		Arrays.sort(nodes, comp);
		return nodes;
	}
}