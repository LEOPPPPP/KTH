import java.util.*;

public class WeightedDirectedGraph {
	//Node -  representerar en nod i en sekvens med grannh�rn
    private static class Node implements Comparable<Node> {
		//Index av ett grannh�rn i vektorn med h�rn
		public int fromIndex;
		public int toIndex;
		public int edgeWeight;
		//N�sta nod i sekvensen med grannh�rn
		public Node    nextNode;
		public Node (int toIndex, int edgeWeight) {
			this.toIndex = toIndex;
			this.edgeWeight = edgeWeight;
			this.nextNode = null;
		}
		public int compareTo(Node compareNode) {
			if (edgeWeight < compareNode.edgeWeight) {
				return -1;
			}
			else if (edgeWeight == compareNode.edgeWeight) {
				return 0;
			}
			else {
				return 1;
			}
		}
		public String toString() {
			return "" + edgeWeight;
		}
	}
	public static final int    DEFAULTCAPACITY = 100;
	//Hur mycket kapaciteten �ndras med vid enlarge. H�r 25%.
    public static final int    ENLARGEVALUE = 25;
	//Grafens h�rn
    private String[]    corners;
    //Sekvenser med v�gar mellan h�rn
    private Node[]    routes;
    private int    lastIndex = -1;

   	//Konstruktor - skapar en graf med default kapacitet.
    public WeightedDirectedGraph() {
        corners = new String[DEFAULTCAPACITY];
        routes = new Node[DEFAULTCAPACITY];
	}
    //Konstruktor - skapar en graf med given kapacitet.
    public WeightedDirectedGraph(int initialCapacity) {
        corners = new String[initialCapacity];
        routes = new Node[initialCapacity];
	}
	//Size - returnerar antalet h�rn i grafen.
	public int size() {
		return lastIndex + 1;
	}
	//isEmpty - returnerar true om grafen �r tom, annars false.
	public boolean isEmpty() {
		return lastIndex == -1;
	}
	//Enlarge - ut�kar grafens kapacitet.
	protected void enlarge() {
		//Ut�ka storleken med 25% och skapa nya arrayer med den nya storleken.
		int newLength = 1 + corners.length + ENLARGEVALUE * corners.length/100;
		String[] newCorners = new String[newLength];
		Node[] newRoutes = new Node[newLength];
		//Kopiera corners och routes till de nya arrayerna.
		for (int i = 0; i <= lastIndex; i++) {
			newCorners[i] = corners[i];
			newRoutes[i] = routes[i];
			corners[i] = null;
			routes[i] = null;
		}
		corners = newCorners;
		routes = newRoutes;
	}
	//indexOf - returnerar index av det h�rn som �r likadant som givet h�rn.
	protected int indexOf(String corner) {
		//G� igenom arrayen med h�rn och j�mf�r med givet h�rn. Om lika, returnera index.
		for (int i = 0; i <= lastIndex; i++) {
			if (corner.equals(corners[i])) {
				return i;
			}
		}
		//Annars returnera -1.
		return -1;
	}
	//hasCorner - returnerar true om det finns ett h�rn som �r likadant som givet h�rn.
	public boolean hasCorner(String corner) {
		return indexOf(corner) != -1;
	}
	//L�gger till ett h�rn om det inte redan finns ett likadant h�rn.
	public void addCorner(String corner) {
		if (this.hasCorner(corner) == false) {
			if (lastIndex == corners.length - 1) {
				enlarge();
			}
			lastIndex++;
			corners[lastIndex] = corner;
		}
	}
	//getCorners - returnerar en array med alla h�rn.
	public String[] getCorners() {
		String[] allCorners = new String[lastIndex + 1];
		for (int i = 0; i < allCorners.length; i++) {
			allCorners[i] = corners[i];
		}
		return allCorners;
	}
	//getNeighbours - returnerar en array med h�rnets grannh�rn.
	public String[] getNeighbours(String corner) throws IllegalArgumentException {
		//Plats i h�rnarrayen d�r h�rnet finns.
		int index = indexOf(corner);
		//Om h�rnet inte finns, kasta exception
		if (index < 0) {
			throw new IllegalArgumentException("Fel med h�rnet: " + corner);
		}
		//S�tt currentNode att peka p� f�rsta noden i nodsekvensen av routes.
		Node currentNode = routes[index];
		int count = 0;
		//S� l�nge det finns fler routes, r�kna hur m�nga det finns.
		while (currentNode != null) {
			count++;
			currentNode = currentNode.nextNode;
		}
		//Skapa en array med r�tt antal platser.
		String[] out = new String[count];
		currentNode = routes[index];
		int neighbourIndex = 0;
		//G� igenom nodsekvensen av routes igen och l�gg till corners som
		//routsen leder till i arrayen med grannh�rn som ska returneras.
		while(currentNode != null) {
			out[neighbourIndex++] = corners[currentNode.toIndex];
			currentNode = currentNode.nextNode;
		}
		return out;
	}
	//hasEdge - returnerar true om det finns en route mellan tv� givna h�rn.
	//Observera att eftersom grafen �r riktad r�knas bara routes som g�r fr�n
	//givet corner1 till corner2.
	public boolean hasRoute(String corner1, String corner2) throws IllegalArgumentException {
		//H�mtar h�rnens index i arrayen med h�rn.
		int index1 = indexOf(corner1);
		int index2 = indexOf(corner2);
		//Om de inte finns, kasta exception.
		if (index1 < 0) {
			throw new IllegalArgumentException("Fel med h�rnet: " + corner1);
		}
		if (index2 < 0) {
			throw new IllegalArgumentException("Fel med h�rnet: " + corner2);
		}
		//S�tt currentNode att peka p� f�rsta routen corner1 har.
		Node currentNode = routes[index1];
		//G� l�ngs nodsekvensen och kolla om det finns en route till corner2's index.
		//Is�fall returnera true, annars l�ps loopen vidare och false returneras om
		//Ingen route mellan givna h�rn hittats.
		while (currentNode != null) {
			if (currentNode.toIndex == index2) {
				return true;
			}
			else {
				currentNode = currentNode.nextNode;
			}
		}
		return false;
	}
	public int routeWeight(String corner1, String corner2) {
		//H�mtar h�rnens index i arrayen med h�rn.
		int index1 = indexOf(corner1);
		int index2 = indexOf(corner2);
		//Om de inte finns, kasta exception.
		if (index1 < 0) {
			throw new IllegalArgumentException("Fel med h�rnet: " + corner1);
		}
		if (index2 < 0) {
			throw new IllegalArgumentException("Fel med h�rnet: " + corner2);
		}
		//S�tt currentNode att peka p� f�rsta routen corner1 har.
		Node currentNode = routes[index1];
		//G� l�ngs nodsekvensen och kolla om det finns en route till corner2's index.
		//Is�fall returnera vikten, annars l�ps loopen vidare och -1 returneras om
		//Ingen route mellan givna h�rn hittats.
		while (currentNode != null) {
			if (currentNode.toIndex == index2) {
				return currentNode.edgeWeight;
			}
			else {
				currentNode = currentNode.nextNode;
			}
		}
		return -1;
	}
	//L�gger till en nod till den nodsekvens given av indexet.
	//Noden placeras s� att sekvensen f�rblir sorterad efter vikter.
	protected void addNode(Node node, int index) {
		//S�tt currentNode att peka p� f�rsta noden i sekvensen som utg�r fr�n givet h�rnindex.
		Node currentNode = routes[index];
		//Om det inte finns n�gra routes fr�n h�rnet, l�gg till noden/routen.
		if (currentNode == null) {
			routes[index] = node;
		}
		else {
			Node previousNode = null;
			//Annars, s� l�nge det finns fler noder i sekvensen och nuvarande nod
			//har l�gre vikt �n den som ska l�ggas till, g� l�ngs nodsekvensen och
			//uppdatera currentNode och previousNode.
			while (currentNode != null && currentNode.edgeWeight < node.edgeWeight) {
				previousNode = currentNode;
				currentNode = currentNode.nextNode;
			}
			//Om previousNode �r null, allts� om noden ska ligga f�rst i sekvensen,
			//s�tt routes[index] att peka p� den nya noden.
			if (previousNode == null) {
				routes[index] = node;
			}
			//Annars om det inte �r den f�rsta noden i sekvensen, s�tt f�reg�ende nods
			//nextNode att peka p� noden som ska l�ggas till.
			else {
				previousNode.nextNode = node;
			}
			//S�tt sedan den nya noden att peka p� currentNode.
			node.nextNode = currentNode;
		}
	}
	//removeNode - tar bort den nod som symboliserar routen mellan tv� h�rn.
	protected void removeNode(int from, int to) {
		//S�tt currentNode att peka p� den f�rsta noden i given nodsekvens.
		Node currentNode = routes[from];
		Node previousNode = null;
		//S� l�nge det finns fler noder i sekvensen och r�tt route inte �r funnen.
		while (currentNode != null  &&  currentNode.toIndex != to) {
			previousNode = currentNode;
			currentNode = currentNode.nextNode;
		}
		//Om det finns n�gon route.
		if (currentNode != null) {
			//Om Noden som ska tas bort inte �r den f�rsta, s�tt previousNode
			//att peka p� currentNodes nextNode.
			if (previousNode != null) {
				previousNode.nextNode = currentNode.nextNode;
			}
			//Annars s�tt route-arrayens pekare p� given positon att peka p�
			//currentNodes nextNode.
			else {
				routes[from] = currentNode.nextNode;
			}
		}
	}
	//L�gger till en route mellan tv� h�rn med given vikt.
	public void addRoute(String fromCorner, String toCorner, int weight) throws IllegalArgumentException {
		//H�mtar h�rnens index i arrayen med h�rn.
		int index1 = indexOf(fromCorner);
		int index2 = indexOf(toCorner);
		//Om de inte finns, kasta exception.
		if (index1 < 0) {
			throw new IllegalArgumentException("Fel med h�rnet: " + fromCorner);
		}
		if (index2 < 0) {
			throw new IllegalArgumentException("Fel med h�rnet: " + toCorner);
		}
		//Om det finns en route mellan givna h�rn, ta bort den.
		if (hasRoute(fromCorner, toCorner)) {
			removeNode(index1, index2);
		}
		//Skapa ny nod med given riktning och vikt
		Node newNode = new Node(index2, weight);
		//L�gg till noden till given sekvens.
		addNode(newNode, index1);
	}
	//Tar bort routen mellan givna h�rn.
	public void removeRoute(String fromCorner, String toCorner) throws IllegalArgumentException {
		int index1 = indexOf(fromCorner);
		int index2 = indexOf(toCorner);
		//Om de inte finns, kasta exception.
		if (index1 < 0) {
			throw new IllegalArgumentException("Fel med h�rnet: " + fromCorner);
		}
		if (index2 < 0) {
			throw new IllegalArgumentException("Fel med h�rnet: " + toCorner);
		}
		//Kalla p� removeNode som tar bort eventuella routes mellan givna h�rn.
		removeNode(index1, index2);
	}
	//Tar bort alla routes fr�n givet h�rn.
	public void removeRoutes(String corner) throws IllegalArgumentException {
		int index = indexOf(corner);
		//Om h�rnet inte finns, kasta exception.
		if (index < 0) {
			throw new IllegalArgumentException("Fel med h�rnet: " + corner);
		}
		//Ta bort alla routes fr�n givet h�rn.
		routes[index] = null;
		//G� igenom alla routes och hitta routes som g�r till givet h�rn.
		for(int i = 0; i <= lastIndex; i++) {
			Node currentNode = routes[i];
			while (currentNode != null) {
				//Om route g�r till givet h�rn, ta bort, annars g� vidare i nodsekvensen.
				if (currentNode.toIndex == index) {
					removeRoute(corners[i], corner);
				}
				currentNode = currentNode.nextNode;
			}
		}
	}
	//removeCorner - tar bort ett h�rn i grafen som �r likadant som givet h�rn.
	public void removeCorner(String corner) {
		//H�mta h�rnets plats i arrayen med h�rn.
		int index = indexOf(corner);
		//Om h�rnet finns
		if (index != -1) {
			//Ta bort alla routes till och fr�n h�rnet.
			removeRoutes(corner);
			//Ta bort h�rnet och flytta inneh�llet i arrayerna.
			for (int i = index + 1; i <= lastIndex; i++) {
				corners[i - 1] = corners[i];
				routes[i - 1] = routes[i];
			}
			corners[lastIndex] = null;
			routes[lastIndex] = null;
			lastIndex--;
			//Uppdatera routes med nya toIndex.
			for (int i = 0; i <= lastIndex; i++) {
				Node currentNode = routes[i];
				//G� genom nodsekvenserna.
				while (currentNode != null) {
					//Om toIndex �r mindre �n index av h�rnet som togs bort har den flyttats.
					//Minska is�fall toIndex med 1 eftersom den flyttats en position l�ngre fram i arrayen.
					if (currentNode.toIndex > index) {
						currentNode.toIndex--;
					}
					currentNode = currentNode.nextNode;
				}
			}
		}
	}
	//Clear - tar bort alla h�rn och routes ifr�n grafen.
	public void clear() {
		corners = new String[corners.length];
		routes = new Node[routes.length];
		lastIndex = -1;
	}
	//hasRouteBreadth - returnerar true om det finns en route mellan tv� h�rn.
	public boolean hasRouteBreadth(String start, String stop) {
		ArrayDeque<String> q = new ArrayDeque<String>();
		String[] visited = new String[lastIndex+1];
		boolean found = false;
		String current = start;
		int count = 0;
		//L�gger till start-h�rnet i k�n.
		q.addLast(current);
		//K�rs s� l�nge man inte hittat en v�g till r�tt h�rn.
		while (found == false) {
//			System.out.println(q);
			//Om k�n �r tom har man g�tt igenom alla h�rn utan att hitta, avbryt och returnera false.
			if (q.isEmpty()) {
				break;
			}
			//L�gg k�ns f�rsta h�rn till current, och l�gg current till listan av bes�kta h�rn.
			current = q.removeFirst();
			visited[count++] = current;
			//Om man befinner sig i r�tt h�rn, returnera true.
			if (current == stop) {
				found = true;
			}
			//Annars, g� igenom alla routes fr�n current-h�rnet
			else {
				Node currentNode = routes[indexOf(current)];
				//K�rs s� l�nge det finns fler routes fr�n current h�rnet.
				while (currentNode != null) {
					//Om routes fr�n current-h�rnet g�r till ett annat h�rn som ej �r bes�kt,
					//l�gg till det i k�n. Unders�k sedan n�sta route i sekvensen.
					if (Arrays.asList(visited).contains(corners[currentNode.toIndex]) == false) {
						q.addLast(corners[currentNode.toIndex]);
					}
					currentNode = currentNode.nextNode;
				}
			}
		}
		return found;
	}
	//hasRouteDepth - returnerar true om det finns en route mellan tv� h�rn.
	public boolean hasRouteDepth(String start, String stop) {
		ArrayDeque<String> q = new ArrayDeque<String>();
		String[] visited = new String[lastIndex+1];
		boolean found = false;
		String current = start;
		int count = 0;
		//L�gger till start-h�rnet i k�n.
		q.addLast(current);
		//K�rs s� l�nge man inte hittat en v�g till r�tt h�rn.
		while (found == false) {
//			System.out.println(q);
			//Om k�n �r tom har man g�tt igenom alla h�rn utan att hitta, avbryt och returnera false.
			if (q.isEmpty()) {
				break;
			}
			//L�gg k�ns f�rsta h�rn till current, och l�gg current till listan av bes�kta h�rn.
			current = q.removeFirst();
			visited[count++] = current;
			//Om man befinner sig i r�tt h�rn, returnera true.
			if (current == stop) {
				found = true;
			}
			//Annars, g� igenom alla routes fr�n current-h�rnet
			else {
				Node currentNode = routes[indexOf(current)];
				//K�rs s� l�nge det finns fler routes fr�n current h�rnet.
				while (currentNode != null) {
					//Om routes fr�n current-h�rnet g�r till ett annat h�rn som ej �r bes�kt,
					//l�gg till det i k�n. Unders�k sedan n�sta route i sekvensen.
					if (Arrays.asList(visited).contains(corners[currentNode.toIndex]) == false) {
						q.addLast(corners[currentNode.toIndex]);
					}
					currentNode = currentNode.nextNode;
				}
			}
		}
		return found;
	}
	//MST - returnerar ett minimalt spanning-tree till grafen utifr�n Kruskals algoritm.
	public WeightedDirectedGraph MST() {
		WeightedDirectedGraph res = new WeightedDirectedGraph();
		//L�gg till alla h�rn till den nya resultatgrafen.
		for (int i = 0; i < lastIndex+1; i++) {
			res.addCorner(corners[i]);
		}
		//Skapa en array som symboliserar vilka m�ngder h�rnen tillh�r.
		int[] sets = new int[lastIndex+1];
		int included = 0;
		//L�gg till alla h�rn i olika sets.
		for (int i = 0; i < lastIndex+1; i++) {
			sets[i] = i;
		}
		LinkedList<Node> routeList = new LinkedList<Node>();
		//L�gg alla routes till en l�nkad lista.
		for (int i = 0; i <= lastIndex; i++) {
			Node currentNode = routes[i];
			while (currentNode != null) {
				//Utvidgar Noden genom att l�gga till �ven start-h�rn.
				currentNode.fromIndex = i;
				routeList.addLast(currentNode);
				currentNode = currentNode.nextNode;
			}
		}
//		System.out.println(routeList);
		//Sorterar listan s� att routesen ligger i ordning d�r l�gst weight ligger f�rst.
		Collections.sort(routeList);
//		System.out.println(routeList);
		//K�rs s� l�nge antalet routes i resultatm�ngden �r mindre �n lastIndex, dvs antal corners -1
		while (included < lastIndex) {
			//S�tt node att peka p� f�rsta routen,som �ven tas bort ur listan.
			Node node = routeList.removeFirst();
			//Om routen g�r mellan tv� olika m�ngder, l�gg till routen till den nya grafen.
			if (sets[node.fromIndex] != sets[node.toIndex]) {
				res.addRoute(corners[node.fromIndex], corners[node.toIndex], node.edgeWeight);
				//�ndra i arrayen som representerar vilka m�ngder h�rnen tillh�r s�
				//de tv� h�rnen numera tillh�r samma set.
				sets[node.fromIndex] = sets[node.toIndex];
				included++;
			}
		}
		return res;
	}
	//Dijkstras algoritm - best�mmer kortaste v�garna fr�n ett h�rn till alla andra h�rn.
	//Returnerar sedan en graf inneh�llande endast de v�gar som ger kortast v�g samt total weight.
	public WeightedDirectedGraph Dijkstra(String source) {
		WeightedDirectedGraph g = new WeightedDirectedGraph();
		int[] distance = new int[lastIndex+1];
		boolean[] visited = new boolean[lastIndex+1];
		int currentMax = Integer.MAX_VALUE;
		int currentCorner = 0;
		//L�gg till alla h�rn till den nya grafen.
		for (int m = 0; m <= lastIndex; m++) {
			g.addCorner(corners[m]);
		}
		//L�gg till h�gsta m�jliga avst�nd till h�rnen.
		for (int i = 0; i < distance.length; i++) {
			distance[i] = Integer.MAX_VALUE;
		}
		//S�tt avst�ndet fr�n source-h�rnet att vara 0.
		distance[indexOf(source)] = 0;
		for (int i = 0; i < distance.length; i++) {
			//Hitta den route med kortast avst�nd, detta h�rn ska bes�kas.
			for (int k = 0; k < distance.length; k++) {
				if (visited[k] == false && distance[k] < currentMax) {
					currentCorner = k;
					currentMax = distance[k];
				}
			}
//			System.out.println("NEXT: " + currentCorner + Arrays.toString(distance));
			//Markera nuvarande h�rn som bes�kt.
			visited[currentCorner] = true;
			//H�mta nuvarande h�rns grannar.
			String[] neighbours = getNeighbours(corners[currentCorner]);
			//G� igenom nuvarande h�rnets grannar.
			for (int j = 0; j < neighbours.length; j++) {
				String currentNeighbour = neighbours[j];
				//Ber�kna ny distans till nuvarande h�rnets grannar via nuvarande h�rnet.
				int newDistance = distance[currentCorner] + routeWeight(corners[currentCorner], currentNeighbour);
				//Om distansen via nuvarande h�rnet �r kortare �n redan funnen v�gs distans.
				if (distance[indexOf(currentNeighbour)] > newDistance) {
					//L�gg till routen som anv�nds till den nya grafen.
					g.addRoute(corners[currentCorner], currentNeighbour, routeWeight(corners[currentCorner], currentNeighbour));
					//L�gg till den nya distansen till arrayen med n�rmaste routes.
					distance[indexOf(currentNeighbour)] = newDistance;
				}
			}
			//Nollst�ll next och currentMax till sina startv�rden f�r att hitta n�sta kortaste v�g.
			currentCorner = 0;
			currentMax = Integer.MAX_VALUE;
		}
		System.out.println(Arrays.toString(distance));
		System.out.println(Arrays.toString(visited));
		return g;
	}
	// toString returnerar grafens str�ngrepresentation
    public String toString ()
    {
		// str�ng med h�rn
		String    verticesString = "{";
		for (int index = 0; index < lastIndex; index++)
		{
		    verticesString += corners[index] + ", ";
		    if ((index + 1) % 10 == 0)
		        verticesString += "\n   ";
		}
		if (lastIndex >= 0)
		    verticesString += corners[lastIndex];
		verticesString += "}";

        // str�ng med kanter
		String    edgesString = "{";
		int    counter = 0;
		for (int index = 0; index <= lastIndex; index++)
		{
			Node    node = routes[index];
			while (node != null)
			{
//                if (corners[index].compareTo (
//					corners[node.toIndex]) <= 0)
//                {
			        if (counter > 0  &&  counter % 5 == 0)
			            edgesString += "\n   ";
			        edgesString += "{" + corners[index] + ", "
			                    + corners[node.toIndex] + ", "
			                    + node.edgeWeight + "}, ";
			        counter++;
//				}

			    node = node.nextNode;
		    }
		}
		if (edgesString.length () > 1)
		    edgesString = edgesString.substring (0,
		                  edgesString.length () - 2);
		edgesString += "}";

        // str�ng med b�de h�rn och kanter
		String    string = "{ " + verticesString + ",\n  "
		                       + edgesString + " }";

		return string;
	}

}