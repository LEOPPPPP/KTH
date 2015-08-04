/*Komplexitet:
 *Size: G�r igenom fr�n nod till nod tills det p�tr�ffas right/leftNode som �r null. O(n)
 *Put/take: Worst case, d�r alla element l�ggs till i sorterad ordning ger att loopen f�r att
 *hitta r�tt position k�rs n g�nger. WorstCase har allts� komplexiteten O(n)
 *�r tr�det balanserat, �r komplexiteten O(logn)
 *peek: F�ljer left eller rightNode tills den kommer till en node utan childs. Komplexitet samma som
 *put och take.
 *
 *Vinsten j�mf�rt med att anv�nda en sekvensiell struktur �r att komplexiteten f�r en s�dan
 *k� �r O(n) d� man beh�ver g� igenom hela k�n f�r att hitta det st�rsta respektive minsta elementet.
 *
 *Heapen ser bara till att det �versta elementet �r st�rst. TakeHighest hade hamnat p� en motsvarande
 *komplexitet, men f�r att hitta minsta elementet �r det bin�ra s�ktr�det effektivare eftersom
 *det i heapen kan ligga var som helst, men som ett l�v utan childs.
 *
 *
 */

import java.util.*;

public class lhQueue<E extends Comparable<? super E>> {
	//Nodeelementet som k�n/tr�det �r uppbyggd av.
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
	//K�n/tr�dets root.
	private Node root;
	private int numberOfElements;

	public lhQueue() {
		root = null;
	}
	//Metoden isEmpty - om k�n/tr�det har en root, �r k�n inte tom.
	public boolean isEmpty() {
		return root == null;
	}
	//Metoden size - kallar p� den rekursiva algoritmen som best�mmer storleken utifr�n rooten.
	public int size() {
//		return size(root);
		return numberOfElements;
	}
	//Ber�knar k�ns storlek. G�r rekursivt fr�n nod till nod tills den st�ter p� ett l�v.
//	private int size(Node node) {
//		int count = 0;
//		if (node != null) {
//			count = 1 + size(node.leftNode) + size(node.rightNode);
//		}
//		return count;
//	}
	//Metoden put - L�gger till ett element i k�n/tr�det.
	public void put(E element) {
		//newNode �r det element som ska placeras i k�n/tr�det.
		Node newNode = new Node(element);
		//Om k�n �r tom, placera elementet i rooten.
		if (root == null) {
			root = newNode;
		}
		//Annars, leta upp r�tt plats i k�n/tr�det.
		else {
			Node currentNode = root;
			//K�rs tills loopen bryts inifr�n, n�r ett element har placerats.
			while (true) {
				int compare = element.compareTo(currentNode.element);
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
	//Metoden peekHighest - returnerar det st�rsta elementet i k�n.
	public E peekHighest() {
		//currentNode s�tts till k�ns root.
		Node currentNode = root;
		//Om rooten inte �r null.
		if (root != null) {
			//S� l�nge det finns en rightNode, s�tt currentNode till rightNode.
			//N�r det inte l�ngre finns en rightNode, har det st�rsta elementet hittats.
			while (currentNode.rightNode != null) {
				currentNode = currentNode.rightNode;
			}
		}
		return currentNode.element;
	}
	//Metoden peekLowest - returnerar det minsta elementet i k�n. Som peekHighest, fast g�r �t v�nster.
	public E peekLowest() {
		Node currentNode = root;
		if (root != null) {
			//S� l�nge det finns en leftNode, s�tt currentNode till rightNode.
			//N�r det inte l�ngre finns en leftNode, har det minsta elementet hittats.
			while (currentNode.leftNode != null) {
				currentNode = currentNode.leftNode;
			}
		}
		return currentNode.element;
	}
	//Metoden takeHighest - returnerar det h�gsta elementet i k�n/tr�det. Tar �ven bort elementet.
	public E takeHighest() {
		Node currentNode = root;
		Node parentNode = null;
		//Om rooten inte �r null, allts� om k�n inte �r tom.
		if (root != null) {
			//S� l�nge det finns en rightNode, s�tt currentNode till rightNode.
			//N�r det inte l�ngre finns en rightNode, har det st�rsta elementet hittats.
			while (currentNode.rightNode != null) {
				//parentNode s�tts till currentNode och currentNode �ndras att peka p� n�sta node.
				parentNode = currentNode;
				currentNode = currentNode.rightNode;
			}
		}
		//Elementet som ska returneras �r currentNodes element n�r man hittat st�rsta elementet.
		E returnElement = currentNode.element;
		//Om elementet som ska returneras samt tas bort har en leftNode ska parentNode s�ttas att
		//peka p� currentNodes leftNode. St�rsta talet �r alltid en rightNode, d�rf�r beh�ver
		//vi ej kolla om currentNode har en rightNode.
		//Om currentNode har en leftNode.
		if (currentNode.leftNode != null) {
			//Om currentNode �r root till k�n, s�tt currentNodes leftNode till ny root.
			if (currentNode == root) {
				root = currentNode.leftNode;
			}
			//Annars s�tt parentNodes rightNode som f�rrut pekade till currentNode,
			//till att peka p� currentNodes leftNode.
			else {
				parentNode.rightNode = currentNode.leftNode;
			}
		}
		//Om rooten inte har n�gon leftNode eller rightNode, �r elementet som ska returnas
		//rooten sj�lv. D�rav s�tts rooten till null, och k�n �r nu tom.
		else if (root.leftNode == null && root.rightNode == null) {
			root = null;
		}
		//Annars, om det inte fanns n�gon leftNode till currentNode, kopplas bara currentNode av.
		else {
			parentNode.rightNode = null;
		}
		numberOfElements--;
		return returnElement;
	}

	//Metoden takeLowest - returnerar det l�gsta elementet i k�n/tr�det. Tar �ven bort elementet.
	public E takeLowest() {
		Node currentNode = root;
		Node parentNode = null;
		//Om rooten inte �r null, allts� om k�n inte �r tom.
		if (root != null) {
			//S� l�nge det finns en leftNode, s�tt currentNode till leftNode.
			//N�r det inte l�ngre finns en leftNode, har det minsta elementet hittats.
			while (currentNode.leftNode != null) {
				//parentNode s�tts till currentNode och currentNode �ndras att peka p� n�sta node.
				parentNode = currentNode;
				currentNode = currentNode.leftNode;
			}
		}
		//Elementet som ska returneras �r currentNodes element n�r man hittat minsta elementet.
		E returnElement = currentNode.element;
		//Om elementet som ska returneras samt tas bort har en rightNode ska parentNode s�ttas att
		//peka p� currentNodes rightNode. Minsta talet �r alltid en leftNode, d�rf�r beh�ver
		//vi ej kolla om currentNode har en leftNode.
		//Om currentNode har en rightNode.
		if (currentNode.rightNode != null) {
			//Om currentNode �r root till k�n, s�tt currentNodes rightNode till ny root.
			if (currentNode == root) {
				root = currentNode.rightNode;
			}
			//Annars s�tt parentNodes leftNode som f�rrut pekade till currentNode,
			//till att peka p� currentNodes rightNode.
			else {
				parentNode.leftNode = currentNode.rightNode;
			}
		}
		//Om rooten inte har n�gon leftNode eller rightNode, �r elementet som ska returnas
		//rooten sj�lv. D�rav s�tts rooten till null, och k�n �r nu tom.
		else if (root.leftNode == null && root.rightNode == null) {
			root = null;
		}
		//Annars, om det inte fanns n�gon rightNode till currentNode, kopplas bara currentNode av.
		else {
			parentNode.leftNode = null;
		}
		numberOfElements--;
		return returnElement;
	}
//	public String toString ()
//	{
//		StringBuilder    string = new StringBuilder ("");
//		toString (root, string);
//
//		return string.toString ();
//	}
//
//
//	// toString l�gger till de element som ligger i ett givet tr�d
//	// till en given str�ng. Tr�dets element l�ggs till enligt
//	// "inorder" ordningen.
//	private void toString (Node tree, StringBuilder string)
//	{
//		if (tree != null)
//		{
//            toString (tree.leftNode, string);
//			string.append (tree.element + " ");
//            toString (tree.rightNode, string);
//	    }
//	}

	public static void main(String[] arg) {
		lhQueue<Integer> q = new lhQueue<Integer>();
		Random rand = new Random();
		//Skapar en array med random tal.
		for (int i = 0; i < 20; i++) {
			q.put(Math.abs(rand.nextInt()) % 100);
		}
//		q.put(5);
//		q.put(3);
//		q.put(7);
//		System.out.println(q.peekHighest()); //7
//		System.out.println(q.peekLowest()); //3
//		q.put(2);
//		System.out.println(q.peekHighest()); // 7
//		System.out.println(q.peekLowest()); // 2
//		System.out.println(q.takeHighest()); //7
//		System.out.println(q.takeHighest()); //5
//		System.out.println(q.takeHighest()); //3
//		q.put(4);
//		q.put(1);
//		System.out.println(q.takeHighest()); //4
//		System.out.println(q.peekHighest()); //2
//		System.out.println(q.takeLowest()); //1
//		System.out.println(q.takeLowest()); //2
	}
}