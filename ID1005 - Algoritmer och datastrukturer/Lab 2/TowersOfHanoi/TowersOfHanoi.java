import javax.swing.*;
import java.awt.*;
import java.io.*;

public class TowersOfHanoi extends JFrame{
	//Skapa tre k�er som symboliserar tornen.
	static minOnlyLifoQueue<Integer> pole1 = new minOnlyLifoQueue<Integer>();
	static minOnlyLifoQueue<Integer> pole2 = new minOnlyLifoQueue<Integer>();
	static minOnlyLifoQueue<Integer> pole3 = new minOnlyLifoQueue<Integer>();
	int diskCount;

	public TowersOfHanoi(int disks) {
		diskCount = disks;
		//L�gg till diskar p� start-tornet.
		for (int i = diskCount; i > 0; i--) {
			pole1.put(i);
			System.out.println(pole1.peek());
		}
		//Inst�llningar f�r rutan grafiken visas i.
		this.setVisible(true);
		this.setSize(1150, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		//G�r f�rflyttning fr�n torn1, till torn3, via torn 2.
		move(disks, 1, 3, 2);
	}
	//Move-metoden. Flyttar en disk.
	public void move(int n, int from, int to, int via) {
		int element;

 		if (n == 1) {
 			try {
			//Skapa f�rdr�jning mellan varje f�rflyttning.
 			Thread.sleep(500);
	 		}
	 		catch (Exception e) {
	 		}
	 		//Rita om grafiken med den nya placeringen av diskarna.
	 		repaint();
 			//Best�mmer vilken k�/torn diskarna ska placeras i. Placerar disken p� tornet.
 			if (from == 1) {
 				element = pole1.take();
 			}
 			else if (from == 2) {
 				element = pole2.take();
 			}
 			else {
 				element = pole3.take();
 			}
 			if (to == 1) {
 				pole1.put(element);
 			}
 			else if (to == 2) {
 				pole2.put(element);
 			}
 			else {
 				pole3.put(element);
 			}
   			System.out.println("Move disk sized " + element + " from pole " + from + " to pole " + to);
  		}
  		//Rekursiva l�sningsmetoden f�r Towers of Hanoi-problemet.
  		else {
    		move(n - 1, from, via, to);
    		move(1, from, to, via);
    		move(n - 1, via, to, from);
  		}
	}
	//Metoden paint, ritar ut grafisk representation �ver tornen och diskarna.
	public void paint(Graphics g) {
		lifoQueue<Integer> temp = new lifoQueue<Integer>();
		g.setColor(Color.WHITE);
		g.fillRect(0 , 0, 1200, 1000);
		g.setColor(Color.BLACK);
		//Ritar tornen p� r�tt plats beroende av antalet diskar
		g.fillRect(90+(diskCount)*20, 50, 20, 200);
		g.fillRect(390+(diskCount)*20, 50, 20, 200);
		g.fillRect(690+(diskCount)*20, 50, 20, 200);
		g.fillRect(0, 250, 1200 , 10);
		g.setColor(Color.RED);
		//Tempor�r size-variabel. Eftersom vi tar ut objekt ur k�n under utritning kommer inte listans storlek vara konstant.
		int size = pole1.size();
		//Ritar diskarna p� det f�rsta tornet p� r�tt plats.
		//Positionen best�ms av x = (grundposition - �versta diskens storlek * grundstorlek),
		//y = (grundposition - (antal diskar - nuvarande �versta disk) * grundstorlek + (antal diskar - antal diskar p� tornet) * grundstorlek
		//Storleken best�ms av diskens storlek som lagras i k�n, g�nger en grundstorlek.
		for (int i = 0; i < size; i++) {
			//H�mtar �versta disken fr�n tower1's k� och l�gger den i en tempor�r lifok�. Ritar ut disken.
			temp.put(pole1.take());
			g.fillRect(100+(diskCount-temp.peek())*20, 250-(diskCount-i)*20+(diskCount-size)*20, temp.peek()*40, 20);
		}
		//L�gger tillbaka diskarna p� tornet i motsatt ordning man tog ut dem. Allts� hamnar alla diskar p� samma plats igen.
		for (int i = 0; i < size; i++) {
			pole1.put(temp.take());
		}
		size = pole2.size();
		//Samma som f�r f�rsta tornet.
		for (int i = 0; i < size; i++) {
			temp.put(pole2.take());
			g.fillRect(400+(diskCount-temp.peek())*20, 250-(diskCount-i)*20+(diskCount-size)*20, temp.peek()*40, 20);
		}
		for (int i = 0; i < size; i++) {
			pole2.put(temp.take());
		}
		size = pole3.size();
		//Samma som f�r f�rsta tornet.
		for (int i = 0; i < size; i++) {
			temp.put(pole3.take());
			g.fillRect(700+(diskCount-temp.peek())*20, 250-(diskCount-i)*20+(diskCount-size)*20, temp.peek()*40, 20);
		}
		for (int i = 0; i < size; i++) {
			pole3.put(temp.take());
		}
	}

	public static void main(String[] args) {
		//Skapa ny instans av Towers of Hanoi-problemet.
		TowersOfHanoi towers = new TowersOfHanoi(3);
		System.out.println(towers.pole1.size());
		System.out.println(towers.pole2.size());
		System.out.println(towers.pole3.size());
	}
}
