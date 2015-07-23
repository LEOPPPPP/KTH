import java.util.*;

public class SortedList<E> extends UnsortedList<E> {
	public SortedList() {
		super();
	}
	//Skapar en sorterad lista med samma element som given osorterad lista.
	public SortedList(UnsortedList<E> unsorted) {
		//Iterarar genom m�ngden och l�gger till elementen.
		Iterator<E> iterator = unsorted.iterator();
		while (iterator.hasNext()) {
			this.add(iterator.next());
		}
	}

	public void add(E element) {
		//CurrentNode b�rjar i f�rsta noden.
		Node newNode = new Node(element);
		Node currentNode = firstNode;
		Node parentNode = null;
		int compValue;
		//K�rs s� l�nge currentNode inte �r null, eller till villkor f�r stopp.
		while (currentNode != null) {
			compValue = comparator.compare(element, currentNode.element);
			//Om elementet �r mindre eller lika som currentNodens element har du hittat r�tt plats
			if (compValue <= 0) {
				break;
			}
			//Annars, unders�k n�sta position i listan.
			else {
				parentNode = currentNode;
				currentNode = currentNode.nextNode;
			}
		}
		//Om parentNode �r null, dvs om elementet ska ligga f�rst i listan.
		if (parentNode == null) {
			//S�tt firstNode att peka p� den nya noden.
			firstNode = newNode;
			//newNodes nextNode s�tts att peka p� currentNode, f�rra f�rstapositionen i listan.
			newNode.nextNode = currentNode;
		}
		//Annars placera elementet p� r�tt plats i listan genom att s�tta newNodes nextnode att
		//peka p� currentNode, och att parentNodes nextNode att peka p� den nya noden.
		else {
			newNode.nextNode = currentNode;
			parentNode.nextNode = newNode;
		}
		numberOfElements++;
	}
}