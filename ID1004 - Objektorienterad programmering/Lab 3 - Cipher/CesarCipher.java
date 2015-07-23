/**
 *CesarCipher.java
 *Laboration 3, 10 November
 *Mattias Cederlund
 *mcede@kth.se
 */

public class CesarCipher extends Cipher {
	//Konstruktor, �rvs fr�n superklassen.
	public CesarCipher(long key) {
		super(key);
	}
	//Metod f�r att kryptera ett tecken, returnerar det krypterade tecknet.
	public char encryptChar(char plainChar) {
		return (char) ('A' + (((plainChar - 'A') + (char) key) % 26));
	}
	//Metod f�r att dekryptera ett tecken, retunerar det okrypterade tecknet.
	public char decryptChar(char cryptoChar) {
		return (char) ('A' + (((cryptoChar - 'A') + (char) 26 - key) % 26));
	}
}