// UseWeightedUndirectedGraph.java

/**********************************************************************

Klassen UseWeightedUndirectedGraph anv�nder en graf av typen
WeightedUndirectedGraph p� olika s�tt.

**********************************************************************/

// package fjava.graph;

import java.util.*;  // Arrays
import static java.lang.System.out;


class UseWeightedUndirectedGraph
{
    public static void main (String[] args)
    {
        out.println ("WEIGHTED UNDIRECTED GRAPH");
        out.println ();

		// en viktad, oriktad graf
        WeightedDirectedGraph    graph = new WeightedDirectedGraph();
        // graph = new WeightedUndirectedGraph (2);
        // String[]    vertices0 = {"X", "Y", "Z"};
        // graph = new WeightedUndirectedGraph (vertices0);

        out.println ("empty graph?");
        out.println (graph);
        out.println ("empty: " + graph.isEmpty ());
        out.println ("size: " + graph.size ());
        out.println ();
        out.println ();


        // l�gg till n�gra h�rn till grafen
        String[]    vertices = {"A", "B", "C", "D", "E"};
        for (int i = 0; i < vertices.length; i++)
            graph.addCorner (vertices[i]);
        out.println ("vertices added");
        out.println (graph);
        out.println ("empty: " + graph.isEmpty ());
        out.println ("size: " + graph.size ());
        out.println ();

        // l�gg till n�gra kanter till grafen
//      graph.addRoute ("A", "B", 30);
        graph.addRoute ("A", "C", 20);
        graph.addRoute ("A", "D", 40);
        graph.addRoute ("B", "A", 30);
        graph.addRoute ("B", "D", 20);
//        graph.addRoute ("C", "B", 20); //
        graph.addRoute ("B", "E", 50);
        graph.addRoute ("D", "B", 70);
        graph.addRoute ("D", "C", 10);
        graph.addRoute ("E", "C", 90);
        // graph.addRoute ("X", "Z", 80);
        out.println ("edges added");
        out.println (graph);
        out.println (graph.hasRoute("A", "B"));
        out.println (graph.hasRoute("A", "C"));
        out.println (graph.hasRoute("A", "D"));
        out.println (graph.hasRoute("B", "A"));
        out.println (graph.hasRoute("B", "D"));
        out.println (graph.hasRoute("B", "E"));
        out.println (graph.hasRoute("D", "B"));
        out.println (graph.hasRoute("D", "C"));
        out.println (graph.hasRoute("E", "C"));
        out.println ("\nMinumum spanning tree - kruskals algoritm");
		out.println(graph.MST());
		out.println("\nDijkstra - r�kna ut kortaste v�gen fr�n ett h�rn till de andra h�rnen");
		out.println(graph.Dijkstra("A"));


        // grafens h�rn
        out.println ("\ninspect vertices");
        out.println ("has vertex A: " + graph.hasCorner ("A"));
        String[]    allVertices = graph.getCorners ();
        out.print ("all vertices: ");
        out.println (Arrays.toString (allVertices));
        String[]    neighbours = graph.getNeighbours ("C");
        out.print ("neighbours to C: ");
        out.println (Arrays.toString (neighbours));
        out.println ();

        // grafens kanter
        out.println ("inspect edges");
         out.println ("has edge AB: " + graph.hasRoute ("A", "B"));
        out.println ("has edge ED: " + graph.hasRoute ("E", "D"));
        out.println (
			"weight of edge AB: " + graph.routeWeight ("A", "B"));
        out.println (
			"weight of edge ED: " + graph.routeWeight ("E", "D"));
		out.println ();
		out.println ();

		//finns route?
		out.println("Finns route?");
		out.println(graph.hasRouteBreadth("A", "B"));
		out.println(graph.hasRouteBreadth("E", "A"));
		out.println(graph.hasRouteBreadth("A", "A"));

        // ta bort kanter
        graph.removeRoute ("B", "E");
        out.println (graph);
        graph.removeRoutes ("D");
        out.println ("edges removed");
        out.println (graph);
        out.println ();

        // ta bort h�rn
        graph.removeCorner ("B");
        // graph.removeVertex("Y");
        out.println ("vertices removed");
        out.println (graph);
        out.println ();

        // ta bort alla h�rn och alla kanter
        graph.clear ();
        out.println ("graph cleared");
        out.println (graph);
        out.println ();
	}
}
