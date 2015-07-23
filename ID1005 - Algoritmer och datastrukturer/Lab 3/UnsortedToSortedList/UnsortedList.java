import java.util.*;

public class UnsortedList<E> {
	//Nodeelementet som listan �r uppbyggd av.
	protected class Node {
		//Nodens element, samt pekare till n�sta nod.
		public E element;
		public Node nextNode;

		public Node(E element) {
			this.element = element;
			this.nextNode = null;
		}
	}
	//Defaultcomparator - f�r att kunna j�mf�ra element.
	protected class DefaultComparator implements Comparator<E> {
        public int compare (E element1, E element2) {
            int    compareValue = 0;
			try {
				Comparable<? super E> element = (Comparable<? super E>) element1;
				compareValue = element.compareTo(element2);
			}
			catch (ClassCastException e){
				throw new ClassCastException();
			}
			return compareValue;
		}
    };
	//Listans/tr�dets f�rsta element.
	protected Node firstNode;
	protected int numberOfElements;
	protected Comparator comparator = new DefaultComparator();

	//Konstruktor, skapar en tom lista.
	public UnsortedList() {
		firstNode = null;
	}
	//Size - returnerar antalet element i listan.
	public int size() {
		return numberOfElements;
	}
	//Metoden contains - Unders�ker om ett element finns i listan.
	public boolean contains(E element) {
		//CurrentNode b�rjar i f�rsta noden.
		Node currentNode = firstNode;
		int compValue;
		//K�rs s� l�nge currentNode inte �r null. Annars returna false.
		while (currentNode != null) {
			compValue = comparator.compare(element, currentNode.element);
			//Om elementet �r likadant som ett i listan, returnera true,
			if (compValue == 0) {
				return true;
			}
			//Annars om det inte �r likadant, unders�k n�sta element.
			else {
				currentNode = currentNode.nextNode;
			}
		}
		return false;
	}
	//Add - l�gger till ett element i listan.
	public void add(E element) {
		//Skapa en ny nod inneh�llande det nya elementet.
		Node newNode = new Node(element);
		//newNodes nextNode s�tts att peka p� firstNode
		newNode.nextNode = firstNode;
		//firstNode s�tts sedan att peka p� den nya noden, newNode.
		firstNode = newNode;
		//�kar antalet element med 1.
		numberOfElements++;
	}
	//remove - Tar bort ett element ur listan.
	public void remove(E element) {
		Node currentNode = firstNode;
		Node parentNode = null;
		int compValue;
		//K�rs s� l�nge currentNode inte �r null. Annars returna false.
		while (currentNode != null) {
			compValue = comparator.compare(element, currentNode.element);
			//Om elementet �r likadant som ett i listan, returnera true,
			if (compValue == 0) {
				break;
			}
			//Annars om det inte �r likadant, unders�k n�sta element.
			else {
				parentNode = currentNode;
				currentNode = currentNode.nextNode;
			}
		}
		//Om den hittat ett element, allts� om while-loopen inte g�tt s� att nextNode blir null.
		if (currentNode != null) {
			//ParentNode till noden som ska tas bort s�tts till att peka p�
			//den nod som tas borts nextNode.
			parentNode.nextNode = currentNode.nextNode;
		}
		numberOfElements--;
	}
	//Tostring - returnerar en stringrepresentation av listan.
	public String toString() {
		String out = "";
		Node currentNode = firstNode;
		//S� l�nge det finns fler element, l�gg till dem till stringen out.
		while (currentNode != null) {
			out += currentNode.element + " ";
			currentNode = currentNode.nextNode;
		}
		return out;
	}

	//Representerar en iterator till listan. M�jligg�r iterering.
	private class JIterator implements Iterator<E> {
		//Referens till n�sta nod att returneras
		private Node currentNode;
		//Referens till senaste elmentet som returnerats
		private Node lastReturnedNode;

		//Skapar en iterator f�r att iterera genom listan en g�ng
		public JIterator() {
			//S�tter nextNode till first node och lastReturnedIndex, dvs parent till null.
			currentNode = firstNode;
			lastReturnedNode = null;
		}
		//Metoden hasnext, returnerar true om det finns ett n�sta element.
		public boolean hasNext() {
			return currentNode != null;
		}
		//Metoden next - returnerar n�sta element
		public E next() {
			//Om det inte finns ett n�sta element, kasta exception
			if (!this.hasNext()) {
				throw new NullPointerException("End of iteration");
			}
			//H�mtar n�sta element ur m�ngden.
			E element = currentNode.element;
			//S�tter lastReturnedIndex att peka p� index till det element som ska returneras.
			lastReturnedNode = currentNode;
			//S�tt nextNode att peka p� n�stkommane nod.
			currentNode = currentNode.nextNode;
			return element;
		}
		//Metoden remove - tar bort senast bes�kta element.
		public void remove() {
			if (lastReturnedNode == null) {
				throw new IllegalStateException("Cannot remove - yet no element to remove");
			}
			UnsortedList.this.remove(lastReturnedNode.element);
			lastReturnedNode = null;
		}
	}
	//Returnerar en iterator f�r att iterera �ver m�ngden.
	public Iterator<E> iterator ()
	{
		return new JIterator ();
	}
	public static void main(String[] arg) {
//		UnsortedList<Integer> l = new UnsortedList<Integer>();
		SortedList<Integer> l = new SortedList<Integer>();
		System.out.println("Listans storlek: " + l.size());
		l.add(5);
		l.add(2);
		l.add(6);
		l.add(4);
		l.add(7);
		l.add(4);
		l.add(6);
		System.out.println("Listans storlek: " + l.size());
		System.out.println("Listans inneh�ll: " + l);
		l.remove(4);
		l.remove(5);
		System.out.println("Tar bort 4 och 5, listans inneh�ll efter�t: " + l);
		Iterator<Integer> iterator = l.iterator();
		System.out.print("Iteration genom listan: ");
		while (iterator.hasNext()) {
		    System.out.print(iterator.next() + " ");
		}
	}
}