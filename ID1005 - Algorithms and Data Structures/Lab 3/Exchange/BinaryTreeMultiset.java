import java.util.*;

public class BinaryTreeMultiset<E> implements Set<E>  {
	//Nodeelementet som m�ngden/tr�det �r uppbyggd av.
	private class Node {
		//Nodens element, samt pekare till left- och rightNode
		public E element;
		public Node leftNode;
		public Node rightNode;

		public Node(E element) {
			this.element = element;
			this.leftNode = null;
			this.rightNode = null;
		}
	}
	//Defaultcomparator - f�r att kunna j�mf�ra element.
	 private class DefaultComparator implements Comparator<E> {
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


	//M�ngdens/tr�dets root.
	private Node root;
	private int numberOfElements;
	private Comparator comparator = new DefaultComparator();

	//Konstruktor f�r en tom m�ngd.
	public BinaryTreeMultiset() {
		root = null;
	}
	//Konstruktor f�r en ny m�ngd som inneh�ller samma element som given m�ngd.
	public BinaryTreeMultiset(Set<E> set) {
		root = null;
		for(E element : set) {
			add(element);
		}
	}
	//Konstruktor f�r en m�ngd som inneh�ller alla element som specificerats i en array.
//	public BinaryTreeMultiset(E[] elements) {
//		root = null;
//		for(E element : elements) {
//			add(element);
//		}
//	}
	//Metoden isEmpty - om m�ngden/tr�det har en root, �r m�ngden inte tom.
	public boolean isEmpty() {
		return root == null;
	}
	//Metoden size - returnerar antalet element i m�ngden.
	public int size() {
		return numberOfElements;
	}
	//Metoden add - l�gger till ett element till m�ngden.
	public void add(E element) {
		//newNode �r det element som ska placeras i tr�det.
		Node newNode = new Node(element);
		//Om m�ngden inte redan inneh�ller elementet man vill l�gga till.
		//Om m�ngden �r tom, placera elementet i rooten.
		if (root == null) {
			root = newNode;
		}
		//Annars, leta upp r�tt plats i tr�det.
		else {
			Node currentNode = root;
			//K�rs tills loopen bryts inifr�n, n�r ett element har placerats.
			while (true) {
				int compare = comparator.compare(element, currentNode.element);
				//Om elementet �r mindre �n currentNodes element.
				if (compare <= 0) {
					//Om currentNode inte har en leftNode, s�tt currentNodes leftNode att peka p� newNode.
					if (currentNode.leftNode == null) {
						currentNode.leftNode = newNode;
						break;
					}
					//Annars flytta currentpekaren till currentNodes leftNode.
					else {
						currentNode = currentNode.leftNode;
					}
				}
				//Om elementet �r st�rre �n currentNodes element.
				else {
					//Om currentNode inte har en rightNode.
					if (currentNode.rightNode == null) {
						currentNode.rightNode = newNode;
						break;
					}
					//Annars flytta currentpekaren till rootens rightNode.
					else {
						currentNode = currentNode.rightNode;
					}
				}
			}
		}
		numberOfElements++;
	}
	//Metoden contains - Unders�ker om ett element finns i m�ngden.
	public boolean contains(E element) {
		//CurrentNode b�rjar i rooten.
		Node currentNode = root;
		int compValue;
		//K�rs s� l�nge currentNode inte �r null. Annars returna false.
		while (currentNode != null) {
			compValue = comparator.compare(element, currentNode.element);
			//Om elementet �r likadant som ett i m�ngden, returnera true,
			if (compValue == 0) {
				return true;
			}
			//Annars om det s�kta elementet �r st�rre, g� till currentNodes rightNode.
			else if (compValue == 1) {
				currentNode = currentNode.rightNode;
			}
			//Annars om det s�kta elementet �r mindre, g� till currentNodes leftNode.
			else {
				currentNode = currentNode.leftNode;
			}
		}
		return false;
	}
	//Metoden remove - tar bort ett element ur m�ngden.
	public void remove(E element) {
		Node currentNode = root;
		Node parent = null;
		int compValue;
		boolean elementFound = false;
		//K�rs s� l�nge currentNode inte �r null.
		while (currentNode != null) {
			compValue = comparator.compare(element, currentNode.element);
			//Om elementet �r likadant som ett i m�ngden, break.
			if (compValue == 0) {
				elementFound = true;
				break;
			}
			//Annars om det s�kta elementet �r st�rre, g� till currentNodes rightNode.
			else if (compValue == 1) {
				parent = currentNode;
				currentNode = currentNode.rightNode;
			}
			//Annars om det s�kta elementet �r mindre, g� till currentNodes leftNode.
			else {
				parent = currentNode;
				currentNode = currentNode.leftNode;
			}
		}
		//Om man har hittat ett likadant element.
		if (elementFound) {
			//Om elementet ligger i rooten
			if (currentNode == root) {
				//Om rooten inte har n�gon leftNode, s�tts rooten till rootens rightNode.
				if (root.leftNode == null) {
					root = root.rightNode;
				}
				//Annars hitta r�tt Node att s�tta i rooten
				else {
					Node tempNode = root.leftNode;
					Node tempParent = root;
					//Hitta det st�rsta elementet som �r mindre �n rooten.
					while (tempNode.rightNode != null) {
						tempParent = tempNode;
						tempNode = tempNode.rightNode;
					}
					//Om elementets parent �r rooten, s�tt elementet till ny root,
					//och elementets rightNode s�tts till den gamla rootens rightNode.
					if (tempParent == root) {
						tempNode.rightNode = root.rightNode;
						root = tempNode;
					}
					//Annars byt ut rootens element mot elementet, och s�tt
					//parents rightNode att peka p� nodens leftNode.
					else {
						root.element = tempNode.element;
						tempParent.rightNode = tempNode.leftNode;
					}
				}
			}
			//Om noden bara har ett barn, och det �r �t h�ger
			else if (currentNode.leftNode == null) {
				//Om parents leftNode �r currentNode, ska parents leftNode s�ttas till currentNodes rightNode.
				if (parent.leftNode == currentNode) {
					parent.leftNode = currentNode.rightNode;
				}
				//Annars ska parrents rightNode s�ttas till currentNodes rightNode.
				else {
					parent.rightNode = currentNode.rightNode;
				}
			}
			//Om noden bara har ett barn, och det �r �t v�nster
			else if (currentNode.rightNode == null) {
				//Om parents leftNode �r currentNode, ska parents leftNode s�ttas till currentNodes leftNode.
				if (parent.leftNode == currentNode) {
					parent.leftNode = currentNode.leftNode;
				}
				//Annars ska parrents rightNode s�ttas till currentNodes leftNode.
				else {
					parent.rightNode = currentNode.leftNode;
				}
			}
			//Om noden har tv� barn
			else {
				Node tempNode = currentNode.leftNode;
				Node tempParent = currentNode;
				//Hitta det st�rsta elementet som �r mindre �n rooten.
				while (tempNode.rightNode != null) {
					tempParent = tempNode;
					tempNode = tempNode.rightNode;
				}
				//Flytta den funna nodens element till noden som ska tas bort.
				currentNode.element = tempNode.element;
				//Om parent �r samma som current, s�tt parents leftNode till den funna leftNode
				if (tempParent == currentNode) {
					tempParent.leftNode = tempNode.leftNode;
				}
				//Annars s�tt parents rightNode till den funna leftNode
				else {
					tempParent.rightNode = tempNode.leftNode;
				}
			}
			numberOfElements--;
		}
	}
	//Metoden clear - kopplar bort tr�det
	public void clear() {
		root = null;
	}
	//isSubsetOf - returnerar true om m�ngden �r en delm�ngd av given m�ngd.
	public boolean isSubsetOf (Set<E> set) {
		for (E element : this) {
			//G�r igenom alla element och kollar om de finns i den andra m�ngden.
			//Om n�got element inte finns med, returnera false.
			if (!set.contains(element)) {
				return false;
			}
		}
		return true;
	}
	//union - returnerar en union av tv� m�ngder.
	public BinaryTreeMultiset<E> union (Set<E> set) {
		BinaryTreeMultiset<E> setout = new BinaryTreeMultiset<E>(this);
		//G�r igenom alla element och l�gger till i den nya m�ngden som redan inneh�ller ena m�ngden.
		for (E element : set) {
				setout.add(element);
		}
		return setout;
	}
	//intersection - returnerar snittet av tv� m�ngder
	public BinaryTreeMultiset<E> intersection(Set<E> set) {
		BinaryTreeMultiset<E> setout = new BinaryTreeMultiset<E>();
		//G�r genom hela m�ngden och kollar om elementet finns i den andra m�ngden.
		//Is�fall, l�gg till elementet till en ny m�ngd som returneras.
		for (E element : this) {
			if (set.contains(element)) {
				setout.add(element);
			}
		}
		return setout;
	}
	public BinaryTreeMultiset<E> difference (Set<E> set) {
		BinaryTreeMultiset<E> setout = new BinaryTreeMultiset<E>();
		//G�r genom hela m�ngden och kollar om elementet finns i den andra m�ngden.
		//Om det inte finns, l�gg till elementet till en ny m�ngd som returneras.
		for (E element : this) {
			if (!set.contains(element)) {
				setout.add(element);
			}
		}
		return setout;
	}
	//toString - returnerar en stringrepresentation av m�ngden
	public String toString() {
		StringBuilder string = new StringBuilder("");
		toString(root, string);
		return string.toString();
	}
	//Rekursiv metod som bygger upp stringrepresentationen.
	private void toString(Node node, StringBuilder string) {
		if (node != null) {
			toString(node.leftNode, string);
			string.append(node.element + " ");
			toString(node.rightNode, string);
		}
	}
	int pos = 0;
	//toArray - returnerar en array-representation av m�ngden i stigande ordning.
	public E[] toArray() {
		E[] array = (E[]) new Object[numberOfElements];
		toArray(root, array);
		pos = 0;
		return array;
	}
	//Hj�lpmetod, l�gger rekursivt till alla element p� r�tt plats i arrayen.
	private void toArray(Node node, E[] array) {
		if (node != null) {
			//L�gger till rootens v�nstra barn.
			toArray(node.leftNode, array);
			array[pos] = node.element;
			pos++;
			//L�gger till rootens h�gra barn.
			toArray(node.rightNode, array);
		}
	}
	//Representerar en iterator till m�ngden. M�jligg�r iterering.
	private class JIterator implements Iterator<E> {
		private E[] elements = null;
		//Index f�r n�sta element
		private int nextIndex = -1;
		//Index f�r det senaste elmentet som returnerats
		private int lastReturnedIndex;

		//Skapar en iterator f�r att iterera genom m�ngden en g�ng
		public JIterator() {
			elements = BinaryTreeMultiset.this.toArray();
			//S�tter nextIndex till 0 om det finns element, annars till -1.
			nextIndex = (elements.length != 0) ? 0 : -1;
			lastReturnedIndex = -1;
		}
		//Metoden hasnext, returnerar true om det finns ett n�sta element.
		public boolean hasNext() {
			return nextIndex != -1;
		}
		//Metoden next - returnerar n�sta element
		public E next() {
			//Om det inte finns ett n�sta element, kasta exception
			if (!this.hasNext()) {
				throw new NullPointerException("End of iteration");
			}
			//H�mtar n�sta element ur m�ngden.
			E element = elements[nextIndex];
			//S�tter lastReturnedIndex att peka p� index till det element som ska returneras.
			lastReturnedIndex = nextIndex;
			//Om det finns ett n�sta element, s�tt nextIndex att peka p� n�stkommane index.
			if (nextIndex < elements.length - 1) {
				nextIndex++;
			}
			//Annars s�tt nextIndex till -1 f�r att markera att det inte finns fler element att iterera �ver.
			else {
				nextIndex = -1;
			}
			return element;
		}
		//Metoden remove - tar bort senast bes�kta element.
		public void remove() {
			if (lastReturnedIndex == -1) {
				throw new IllegalStateException("Cannot remove - yet no element to remove");
			}
			BinaryTreeMultiset.this.remove(elements[lastReturnedIndex]);
			lastReturnedIndex = -1;
		}
	}
	//Returnerar en iterator f�r att iterera �ver m�ngden.
	public Iterator<E> iterator ()
	{
		return new JIterator ();
	}

	public static void main(String[] arg) {
		BinaryTreeMultiset<Integer> s = new BinaryTreeMultiset<Integer>();
		s.add(5);
		s.add(2);
		s.add(6);
		System.out.println("Adderat 5, 2, 6 till m�ngden S: " + s);
		System.out.println("Arrayrepresenation av S: " + Arrays.toString(s.toArray()));
		System.out.println("Size av S: " + s.size());
		s.add(2);
		System.out.println("Lagt till 2 till S, size av S: " + s.size());
		System.out.println("S: " + s);
		s.remove(2);
		System.out.println("Tagit bort 2 fr�n S, size av S: " + s.size());
		System.out.println("S inneh�ller 2: " + s.contains(2));
		System.out.println("S inneh�ller 6: " + s.contains(6));
		Iterator<Integer> iterator = s.iterator();
		System.out.print("Iteration genom m�ngden: ");
		while (iterator.hasNext()) {
		    System.out.print(iterator.next () + " ");
		}
		BinaryTreeMultiset<Integer> d = new BinaryTreeMultiset<Integer>(s);
		d.add(7);
		System.out.println("\nSkapat en kopia av S och lagt till 7 i den");
		System.out.println("S: " + s);
		System.out.println("D: " + d);
		System.out.println("D subset av S: " + d.isSubsetOf(s));
		System.out.println("S subset av D: " + s.isSubsetOf(d));
		System.out.println("D union av S:" + d.union(s));
		System.out.println("D intersection av S: " + d.intersection(s));
		System.out.println("D difference av D: " + d.difference(s));
	}
}