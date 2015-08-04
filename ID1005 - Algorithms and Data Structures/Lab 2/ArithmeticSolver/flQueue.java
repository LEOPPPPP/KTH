/*FL-k�
 *Komplexiteter:
 *takeLast: Oberoende av antalet element. Komplexiteten �r d�rmed O(1) (O = theta)
 *takeFirst: Flyttar alla element ett steg mot b�rjan d�r h�l uppst�r n�r ett element tas ut. Komplexiteten O(n)
 *putLast: (put) Oberoende av antal element. Komplexiteten O(1)
 *putFirst: Flyttar alla element ett steg bak�t i sekvensen. Komplexiteten O(n)
 *isFull, isEmpty, size: Oberoende av antal element. Komplexiteten O(1)
 */

import java.util.*;

//Last in First out - k� d�r endast tal mindre �n alla i k�n kan l�ggas till.
public class flQueue<E> {
	private E[] elements;
	private int lastIndex = -1;
	final int DEFAULT_CAPACITY = 100;
	final int ENLARGEVALUE = 50;
	private Comparator<E> comparator;

	private class Comp implements Comparator<E> {
		public int compare (E element1, E element2) {
			Comparable<E> element = (Comparable<E>) element1;
			return element.compareTo(element2);
		}
	}

	//Skapar en ny k� med DEFAULT_CAPACITY (100) som storlek
	public flQueue() {
		elements = (E[]) (new Object[DEFAULT_CAPACITY]);
		comparator = new Comp();
	}
	//Returnerar hur m�nga element som finns i k�n
	public int size() {
		return lastIndex + 1;
	}
	//Kollar om k�n �r full
	public boolean isFull() { //Beh�vs inte i en non-bounded queue.
		return false;
	}
	//Kollar om k�n �r tom.
	public boolean isEmpty() {
		return lastIndex == -1;
	}
	//L�gger till ett element sist i k�n.
	public String putLast(E element) {
		if (lastIndex == elements.length - 1) {
			this.enlarge();
		}
		elements[lastIndex+1] = element;
		lastIndex++;
		return element.toString();
	}
	//L�gger till ett element f�rst i k�n.
//	public void putFirst(E element) {
//		if (lastIndex == elements.length - 1) {
//			this.enlarge();
//		}
//		for (int i = lastIndex; i > 0; i--) {
//			elements[i+1] = elements[i];
//		}
//		elements[0] = element;
//		lastIndex++;
//	}
	//Kollar p� n�sta element i k�n, i denna implementation: sista elementet.
	public E peek() {
		return elements[lastIndex];
	}
	//Tar ut det sita elementet ur k�n.
	public E takeLast() {
		if (this.isEmpty()) {
			throw new IllegalStateException("Kan inte ta ut element - K�n �r tom");
		}
		E returnElement = elements[lastIndex];
		elements[lastIndex] = null;
		lastIndex--;
		return returnElement;
	}
	//Tar ut det f�rsta elementet ur k�n.
	public E takeFirst() {
		if (this.isEmpty()) {
			throw new IllegalStateException("Kan inte ta ut element - K�n �r tom");
		}
		E returnElement = elements[0];
		for (int i = 0; i < lastIndex; i++) {
			elements[i] = elements[i+1];
		}
		lastIndex--;
		return returnElement;
	}
	private void enlarge() {
		//Best�m storleken p� den nya arrayen som lagrar k�n
		int newLength = elements.length + (ENLARGEVALUE * elements.length) / 100;
		E[] newArray = (E[]) (new Object[newLength]);
		//Kopiera alla element fr�n den gamla arrayen till en ny, st�rre array
		for (int i = 0; i <= lastIndex; i++) {
			newArray[i] = elements[i];
		}
		//S�tt den nya arrayen att vara k�ns lagringsstruktur
		elements = newArray;
	}
	public static void main(String[] arg) {
		lifoQueue queue = new lifoQueue();
		System.out.println("K�n �r tom? " + queue.isEmpty()); //true
		System.out.println("K�n �r full? " + queue.isFull()); //false
//		queue.take(); //Testa exception
		queue.put(10);
		System.out.println("K�n �r tom? " + queue.isEmpty()); //false
		System.out.println("N�sta element " + queue.peek()); //10
		queue.put(5);
		System.out.println("N�sta element " + queue.peek()); //5
		queue.put(2);
		queue.put(3);
		System.out.println("N�sta element " + queue.peek()); //3
		System.out.println("Tar ut  " + queue.take());
		System.out.println("N�sta element " + queue.peek()); //2
		System.out.println();

	}
}