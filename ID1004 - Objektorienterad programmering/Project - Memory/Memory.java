import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class Memory extends JFrame implements ActionListener {
	JPanel panel = new JPanel(new GridLayout(9, 8));
	static LinkedList<Card> cards = new LinkedList<Card>();
	LinkedList<ImageIcon> bilder = new LinkedList<ImageIcon>();
	static LinkedList<Player> players = new LinkedList<Player>();
	LinkedList<JLabel> scorelabels = new LinkedList<JLabel>();
	LinkedList<JLabel> playerlabels = new LinkedList<JLabel>();
	javax.swing.Timer timer = new javax.swing.Timer(200, this);
	int save = -1;
	int savei = -1;
	int savei2 = -1;
	int cardsturned = 0;
	ImageIcon bk = new ImageIcon("Memorypics/baksida.jpg");
	static AI ai = new AI("hej",1);
	AIHandeler AIh = new AIHandeler();
	public Memory(LinkedList<Player> players) {
		super("Memory -");
		this.players = players;
		makeBoard();
		this.add(panel);
		this.setVisible(true);
		this.setSize(640,720);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Ger den f�rsta spelaren turen
		players.get(0).myTurn = true;
		playerlabels.get(0).setForeground(Color.GREEN);
		//Startar endast tr�den f�r AI-hantering om det finns n�got AI med i spelet
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getClass() == ai.getClass()) {
				AIh.start();
				break;
			}
		}
	}
	//Metoden makeBoard - Skapar de grafiska komponenterna f�r spelplanen
	public void makeBoard() {
		int k = 0;
		//Laddar in bilderna f�r kortens baksida till programmet
		for (int j = 1; j < 33; j++) {
			for (int i = 0; i < 2; i++) {
				bilder.add(new ImageIcon("Memorypics/" + j + ".gif"));
			}
		}
		//Skapar korten med bilder och id, blandar sedan korten
		for (int i = 0; i < 64; i++) {
			cards.add(new Card(bilder.get(i), k));
			if (i % 2 == 1) {
				k++;
			}
			Collections.shuffle(cards);
		}
		//L�gger ut korten p� spelplanen
		for (int i = 0; i < 64; i++) {
			panel.add(cards.get(i));
			cards.get(i).addActionListener(this);
			cards.get(i).setBorder(new LineBorder(Color.WHITE, 1));
			cards.get(i).pos = i;
			if (i % 8 == 0) {
				System.out.print("\n");
			}
			System.out.print(cards.get(i).id + " ");
		}
		System.out.print("\n");
		//L�gger till labels f�r nick och po�ngst�llning
		for (int i = 0; i < players.size(); i++) {
			playerlabels.add(new JLabel(players.get(i).nick + ": "));
			scorelabels.add(new JLabel(players.get(i).score + ""));
			panel.add(playerlabels.get(i));
			panel.add(scorelabels.get(i));
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Metoden updateLabels - Uppdaterar labels med vems tur det �r samt po�ngst�llningen
	public void updateLabels(boolean in) {
		for (int i = 0; i < players.size(); i++) {
			//Om spelaren vars tur det �r v�ljer r�tt kort ges en po�ng, och spelaren f�r f�rs�ka en g�ng till
			if (players.get(i).myTurn && in) {
				System.out.println("R�tt kort valt, score uppdateras, spelaren forts�tter\n########################################################################\n"
						+ players.get(i).nick + "'s tur.");
				players.get(i).score++;
				scorelabels.get(i).setText(players.get(i).score + "");
				//Startar upp AIHandlern f�r att eventuellt AI ska g�ra sitt drag, om det finns kort kvar
				if (isCards()) {
					this.dispose();
					new Scorelist(players);
				}
				else {
					AIh.interrupt();
				}
				break;
			}
			//Om spelaren vars tur det �r v�ljer fel kort blir det n�sta spelares tur
			else if (players.get(i).myTurn && !in) {
				System.out.println("Fel kort valt, n�sta spelares tur\n########################################################################\n"
						+ players.get((i+1) % players.size()).nick + "'s tur.");
				players.get(i).myTurn = false;
				players.get((i+1) % players.size()).myTurn = true;
				playerlabels.get(i).setForeground(Color.BLACK);
				playerlabels.get((i+1) % players.size()).setForeground(Color.GREEN);
				//Startar upp AIHandlern f�r att eventuellt AI ska g�ra sitt drag
				AIh.interrupt();
				break;
			}
		}
	}
	//Metoden isCards - Kollar om det finns kort kvar, genom att kolla spelarnas sammanlagda po�ng
	public boolean isCards() {
		int sum = 0;
		for (int i = 0; i < players.size(); i++) {
			sum += players.get(i).score;
		}
		if (sum < 32) {
			return false;
		}
		else {
			return true;
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Actionevent - vad som h�nder om man klickar p� ett kort
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < cards.size(); i++) {
			if (e.getSource() == cards.get(i)) {
				for (int k = 0; k < players.size(); k++) {
					if (players.get(k).getClass() == ai.getClass()) {
						//Kort ska inte l�ggas till minnet om det redan finns i minnet
						if (players.get(k).cardmemory.indexOf((Card) e.getSource()) == -1) {
							players.get(k).cardmemory.add((Card) e.getSource());
						}
					}
					while (players.get(k).cardmemory.size() > players.get(k).mem) {
						players.get(k).cardmemory.removeFirst();
					}
				}
				//Om det �r det f�rsta kortet spelaren v�ljer
				if (save == -1 && cardsturned == 0) {
					System.out.println("Knapp " + i + " tryckt. Card id: " + cards.get(i).id);
					cards.get(i).setIcon(cards.get(i).back);
					cards.get(i).setBorder(new LineBorder(Color.RED, 3));
					save = cards.get(i).id;
					savei = i;
					cardsturned = 1;
				}
				//Om det �r det andra kortet spelaren v�ljer, och det �r likadant som det f�rsta
				else if (save == cards.get(i).id && savei != i && cardsturned == 1) {
					System.out.println("Knapp " + i + " tryckt. Card id: " + cards.get(i).id + " R�tt kort.");
					cards.get(i).setIcon(cards.get(i).back);
					cards.get(i).setBorder(new LineBorder(Color.GREEN, 3));
					cards.get(savei).setBorder(new LineBorder(Color.GREEN, 3));
					save = -1;
					savei2 = i;
					cardsturned = 3;
					cards.get(i).taken = true;
					cards.get(savei).taken = true;
					timer.start();
					//Om en spelare har gissat r�tt tar AIn bort korten fr�n minnet
					for (int k = 0; k < cards.size(); k++) {
						if (cards.get(k).taken == true) {
							for (int j = 0; j < players.size(); j++) {
								players.get(j).cardmemory.remove(cards.get(k));
							}
						}
					}
				}
				//Om det �r det andra kortet spelaren v�ljer, och det inte �r likadant som det f�rsta
				else if (save != cards.get(i).id && savei != i && cardsturned == 1) {
					System.out.println("Fel kort");
					cards.get(i).setBorder(new LineBorder(Color.RED, 3));
					cards.get(i).setIcon(cards.get(i).back);
					save = -1;
					savei2 = i;
					cardsturned = 2;
					timer.start();
				}
				//Om spelaren v�ljer samma kort igen
				else if (savei == i) {
					System.out.println("Du kan inte v�lja samma kort 2 g�nger under en runda. Valt kort: " + i);
				}
			}
			//N�r tv� kort �r valda som inte �r likadana startas en timer som best�mmer n�r korten ska v�ndas tillbaka.
			if (e.getSource() == timer) {
				timer.stop();
				//Om spelaren gissat fel v�nds korten tillbaka
				if (cardsturned == 2) {
					System.out.println("Timer k�rs - V�nder tillbaka korten");
					updateLabels(false);
				}
				//Om spelaren gissat r�tt tas korten bort.
				else if (cardsturned == 3) {
					System.out.println("Timer k�rs - Tar bort korten");
					updateLabels(true);
				}
				turnCards();
				save = -1;
				savei = -1;
				savei2 = -1;
				cardsturned = 0;
			}
		}
	}
	//Metoden turnCards - Plockar bort alla kort som �r tagna och v�nder tillbaka alla kort som inte �r tagna
	public void turnCards(){
		for (int i = 0; i < cards.size(); i++) {
			cards.get(i).setIcon(bk);
			cards.get(i).setBorder(new LineBorder(Color.WHITE, 1));
			if (cards.get(i).taken == true) {
				cards.get(i).setVisible(false);
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN
	public static void main(String[] arg) {
		new Menu();
	}
}
//Klassen Card, kortobjekten som ligger p� spelplanen, �rvs fr�n JButton
class Card extends JButton {
	public int id, pos;
	boolean taken = false;
	ImageIcon back;
	public Card(ImageIcon img, int id) {
		super(new ImageIcon("Memorypics/baksida.jpg"));
		this.id = id;
		back = img;
	}
}
//Klassen Player, spelarobjektet
class Player {
	int score, mem;
	String nick;
	boolean myTurn;
	LinkedList<Card> cardmemory = new LinkedList<Card>();
	public Player(String nick) {
		this.nick = nick;
	}
	public void play() {
		System.out.println("Spelaren �r inte ett AI");
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Klassen AI, datorstyrd spelare, �rver fr�n Player
class AI extends Player {
	public AI(String nick, int mem) {
		super(nick);
		this.mem = mem;
	}
	//Strategin AIt anv�nder f�r att g�ra sitt drag
	public void play() {
		boolean twoInMem = false;
		boolean guessSameAsMem = false;
		int firstguess, secondguess;
		System.out.println("Startar botens spelfunktion");
		//Kollar om den k�nner till tv� likadana kort
		for (int i = 0; i < cardmemory.size(); i++) {
			System.out.print(cardmemory.get(i).id + " ");
		}
		System.out.print("\n");
		for (int i = 0; i < cardmemory.size(); i++) {
			for (int j = i+1; j < cardmemory.size(); j++) {
				if (cardmemory.get(i).id == cardmemory.get(j).id && cardmemory.get(i).pos != cardmemory.get(j).pos) {
					System.out.println("Boten har tv� likadana kort");
					Memory.cards.get(cardmemory.get(i).pos).doClick();
					Memory.cards.get(cardmemory.get(j).pos).doClick();
					twoInMem = true;
					break;
				}
			}
			if (twoInMem == true) {
				break;
			}
		}
		//Om den inte har tv� likadana kort - gissa ett kort
		if (twoInMem == false) {
			System.out.println("Boten gissar p� f�rsta kortet");
			firstguess = guess();
			Memory.cards.get(firstguess).doClick();
			//Kollar om ett likadant kort som det framgissade finns i minnet, om sant, v�lj kortet
			for (int i = 0; i < cardmemory.size(); i++) {
				if (cardmemory.get(i).id == Memory.cards.get(firstguess).id && cardmemory.get(i).pos != Memory.cards.get(firstguess).pos) {
					System.out.println("Boten har ett likadant kort som gissningen");
					Memory.cards.get(cardmemory.get(i).pos).doClick();
					twoInMem = true;
					break;
				}
			}
			//Om ett likadant kort som gissningen inte finns i minnet, gissa ett nytt kort
			if (twoInMem == false) {
				System.out.println("Boten gissar p� andra kortet");
				while (true) {
					secondguess = guess();
					if (firstguess != secondguess) {
						break;
					}
				}
				Memory.cards.get(secondguess).doClick();
			}
		}
		twoInMem = false;
		guessSameAsMem = false;
	}
	public int guess() {
		int guess;
		boolean guessSameAsMem = false;
		while (true) {
			//G�r en gissning
			guess = (int) (Math.random() * 64);
			//Kolla s� att kortet man gissat inte finns i minnet
			for (int i = 0; i < cardmemory.size(); i++) {
				if (guess == cardmemory.get(i).pos) {
					System.out.println("Gissningen fanns i minnet, g�r en ny gissning");
					guessSameAsMem = true;
					break;
				}
			}
			//Om det framgissade kortet inte finns i minnet, v�lj kortet
			if (Memory.cards.get(guess).taken == false && guessSameAsMem == false) {
				System.out.println("Gissningen fanns inte i minnet, anv�nder gissningen");
				System.out.println("Botens gissning �r: " + guess);
				return guess;
			}
			//Annars, nollst�ll v�rden och g�r en ny gissning
			guessSameAsMem = false;
		}
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Klassen Menu, skapar en meny med spelinst�llningar
class Menu extends JFrame implements ActionListener {
	JPanel panel;
	String[] labeltext = {"Spelare", "Nick", "AI", "Intellegens", "Spelare 1", "Spelare 2", "Spelare 3", "Spelare 4"};
	LinkedList<JTextField> textfields = new LinkedList<JTextField>();
	LinkedList<JTextField> intfields = new LinkedList<JTextField>();
	LinkedList<JCheckBox> boxes = new LinkedList<JCheckBox>();
	LinkedList<Player> players = new LinkedList<Player>();
	JButton go = new JButton("Go!");
	public Menu() {
		super("Meny");
		panel = new JPanel(new GridLayout(7,4));
		createLabels();
		this.add(panel);
		this.setVisible(true);
		this.setSize(300,200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	//Skapar de grafiska komponenterna f�r menyn
	public void createLabels() {
		for (int i = 0; i < 4; i++) {
			textfields.add(new JTextField(20));
			intfields.add(new JTextField(20));
		}
		for (int i = 0; i < 4; i++) {
			boxes.add(new JCheckBox());
			boxes.get(i).addActionListener(this);
		}
		for (int i = 0; i < 4; i++) {
			panel.add(new JLabel(labeltext[i]));
		}
		for (int i = 4; i < 8; i++) {
			panel.add(new JLabel(labeltext[i]));
			panel.add(textfields.get(i-4));
			panel.add(boxes.get(i-4));
			panel.add(intfields.get(i-4));
		}
		for (int i = 0; i < 5; i++) {
			panel.add(new JLabel(" "));
		}
		panel.add(go);
		go.addActionListener(this);
	}
	//Action - vad som h�nder om man klickar p� de olika knapparna
	public void actionPerformed(ActionEvent e) {
		//Om man trycker p� startknappen
		if (e.getSource() == go) {
			for (int i = 0; i < 4; i++) {
				//Skapar spelare och AI, beroende p� om textf�lten och checkboxarna �r ifyllda
				if (textfields.get(i).getText().length() > 0) {
					if (boxes.get(i).isSelected()) {
						try {
							players.add(new AI(textfields.get(i).getText(), Integer.parseInt(intfields.get(i).getText())));
						}
						catch (NumberFormatException ex) {
							players.add(new AI(textfields.get(i).getText(), 20));
						}
					}
					else {
						players.add(new Player(textfields.get(i).getText()));
					}
				}
			}
			//St�nger ner menyn och startar spelet, skickar med spelarinfon
			this.dispose();
			new Memory(players);
		}
		for (int i = 0; i < boxes.size(); i++) {
			//Om checkboxarna �r ikryssade l�gger den till ett eget namn f�r AIn som ska skapas
			if (boxes.get(i).isSelected()) {
				textfields.get(i).setText("AI " + (i+1));
				textfields.get(i).setEnabled(false);
				intfields.get(i).setText(""+20);
			}
			//Om checkboxarna blir urklickade igen, nollst�ll textf�lten
			else if (!boxes.get(i).isSelected()) {
				if (textfields.get(i).getText().indexOf("AI") != -1) {
					textfields.get(i).setText("");
				}
				textfields.get(i).setEnabled(true);
			}
		}
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Klassen Scorelist, skapar en meny med spelinst�llningar
class Scorelist extends JFrame implements ActionListener {
	JPanel panel;
	String[] labeltext = {"Placering", "Nick", "Score", "1", "2", "3", "4"};
	LinkedList<Player> players = new LinkedList<Player>();
	JButton exit = new JButton("Avsluta");
	public Scorelist(LinkedList<Player> players) {
		super("Scores");
		this.players = players;
		panel = new JPanel(new GridLayout((3 + players.size()),3));
		createLabels();
		this.add(panel);
		this.setVisible(true);
		this.setSize(300, 30 * (3 + players.size()));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	//Metod som sorterar spelarlistan
	public LinkedList<Player> sortPlayers(LinkedList<Player> tosort) {
		boolean isSorted = false;
		Player replace;
		while (isSorted == false) {
			for (int i = 1; i < players.size(); i++) {
				if (tosort.get(i-1).score < tosort.get(i).score) {
					replace = tosort.get(i);
					tosort.remove(i);
					tosort.add(i-1, replace);
				}
			}
			for (int i = 1; i < players.size(); i++) {
				if (tosort.get(i-1).score < tosort.get(i).score) {
					isSorted = false;
					break;
				}
				else {
					isSorted = true;
				}
			}
		}
		return tosort;
	}
	//Skapar de grafiska komponenterna f�r scorelistan
	public void createLabels() {
		if (players.size() > 1) {
			players = sortPlayers(players);
		}
		for (int i = 0; i < 3; i++) {
			panel.add(new JLabel(labeltext[i]));
		}
		for (int i = 3; i < 7; i++) {
			if (players.size() > i-3) {
				panel.add(new JLabel(labeltext[i]));
				panel.add(new JLabel(players.get(i-3).nick));
				panel.add(new JLabel(players.get(i-3).score + ""));
			}
		}
		for (int i = 0; i < 4; i++) {
			panel.add(new JLabel(" "));
		}
		panel.add(exit);
		exit.addActionListener(this);
	}
	//Action - vad som h�nder om man klickar p� knappen
	public void actionPerformed(ActionEvent e) {
		//Om man trycker p� Avslutaknappen
		if (e.getSource() == exit) {
			//St�nger ner scorelistan och avslutar spelet
			System.exit(0);
		}
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Egen tr�d f�r att styra AIn
class AIHandeler extends Thread {
	public void run() {
		while (true) {
			System.out.println("Multithread/AIController starting");
			for (int i = 0; i < Memory.players.size(); i++) {
				System.out.println("Multithread/AIController searching for player with myTurn tag");
				//Kontrollerar s� att spelaren vars tur �r ett AI
				if (Memory.players.get(i).myTurn == true) {
					//Om spelaren �r ett AI, tillkalla spelmetoden
					System.out.println("Multithread/AIController found player with myTurn, called playfunction");
					Memory.players.get(i).play();
					break;
				}
			}
			//N�r AIt gjort sitt drag, l�gg tr�den i sleep
			while (true) {
				System.out.println("Multithread/AIController going to sleep");
				try {
					this.sleep(1000000);
				}
				//Om tr�den interruptas, breakas sleepen och b�rjar om fr�n b�rjan
				catch (InterruptedException e) {
					System.out.println("Thread is interrupted");
					break;
				}
			}
		}
	}
}