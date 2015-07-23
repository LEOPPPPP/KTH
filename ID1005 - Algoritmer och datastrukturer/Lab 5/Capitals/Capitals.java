public class Capitals {
	public static void main(String[] arg) {
		WeightedDirectedGraph g = new WeightedDirectedGraph();
		//L�gg till st�der
		String[]    citys = {"Stockholm", "G�teborg", "Malm�", "V�ster�s", "Lule�"};
        for (int i = 0; i < citys.length; i++) {
            g.addCorner (citys[i]);
        }
        //L�gg till v�gar mellan st�der
        g.addRoute("Stockholm", "Malm�", 100);
        g.addRoute("Stockholm", "V�ster�s", 40);
        g.addRoute("G�teborg", "V�ster�s", 70);
        g.addRoute("G�teborg", "Malm�", 20);
        g.addRoute("G�teborg", "Stockholm", 60);
        g.addRoute("Stockholm", "G�teborg", 65);
//       	g.addRoute("Malm�", "Lule�", 200);
//       	g.addRoute("V�ster�s", "Lule�", 150);
       	System.out.println(g);
       	System.out.println("Breadth first search:");
       	System.out.println(g.hasRouteBreadth("Stockholm", "Malm�"));
       	System.out.println(g.hasRouteBreadth("Stockholm", "V�ster�s"));
       	System.out.println(g.hasRouteBreadth("Stockholm", "G�teborg"));
       	System.out.println(g.hasRouteBreadth("V�ster�s", "Stockholm"));
       	System.out.println("\nDepth first search:");
       	System.out.println(g.hasRouteDepth("Stockholm", "Malm�"));
       	System.out.println(g.hasRouteDepth("Stockholm", "V�ster�s"));
       	System.out.println(g.hasRouteDepth("Stockholm", "G�teborg"));
       	System.out.println(g.hasRouteDepth("V�ster�s", "Stockholm"));
	}
}