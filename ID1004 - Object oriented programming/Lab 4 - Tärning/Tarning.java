/**
 *Tarning.java
 *Laboration 4, 25 November
 *Mattias Cederlund
 *mcede@kth.se
 */

public class Tarning {
	protected final int sidor; //Final f�r att l�sa v�rdet n�r t�rningen v�l skapats
    public Tarning(int sidor) {
    	this.sidor = sidor;
    }
    public int kasta() { //Metod f�r att kasta t�rningen, returnerar ett tal mellan 1 och antal sidor t�rningen har
    	return (int) (Math.random()*sidor) + 1;
    }
    public String toString() { //Metod f�r att "skriva ut" t�rningen, beskriva t�rningen i en str�ng
    	return "T�rningen har " + sidor + " sidor.";
    }
}