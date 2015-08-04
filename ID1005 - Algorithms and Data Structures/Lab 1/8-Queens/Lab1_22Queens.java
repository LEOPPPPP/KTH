import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Lab1_22Queens extends JFrame implements ActionListener {
	JPanel boardpanel = new JPanel(new GridLayout(8, 8));
	LinkedList<JLabel> board = new LinkedList<JLabel>();
	JButton button = new JButton("Visa ny l�sning");
	boolean run = false;

	public Lab1_22Queens() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(button);
		button.addActionListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(350, 383);
		this.setVisible(true);
		this.setJMenuBar(menuBar);
		//Skapar schack-br�det
		for (int i = 0; i < 64; i++) {
			//Best�mmer om det �r en rad som b�rjar med svart eller vit ruta
			//Placerar sedan ut rutor med r�tt f�rg
			int row = (i / 8) % 2;
			board.add(new JLabel());
			if (row == 0) {
				board.get(i).setBackground(i % 2 == 0 ? Color.WHITE : Color.BLACK);
			}
			else {
				board.get(i).setBackground(i % 2 == 0 ? Color.BLACK : Color.WHITE);
			}
			board.get(i).setOpaque(true);
			boardpanel.add(board.get(i));
		}
		this.add(boardpanel);
		//Anropa metoden som hittar m�jliga placeringar f�r drottningarna
		setQueen(new int[8], 8, 0);
	}
	//ActionEvent f�r att lyssna p� knappen, om den trycks s�tt run = true
	//Detta till�ter att man genererar en ny placering f�r drottningarna
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
			run = true;
		}
	}
	//S�tter drottningar p� r�tt plats i det grafiska schackbr�det
	public void showQueens(int[] placing) {
		System.out.println(Arrays.toString(placing));
		//Tar bort de gamla drottningarna
		for(int i = 0; i < 64; i++) {
			board.get(i).setIcon(null);
		}
		//Placerar ut nya drottningar p� r�tt plats
		for (int i = 0; i < 8; i++) {
			board.get(i*8+placing[i]).setIcon(new ImageIcon("queen.gif"));
		}
	}

	public boolean setQueen(int[] rows, int maxcol, int row) {
		//i, kolumner, talen i arrayen representerar kolumner
		for (int i = 0; i < maxcol; i++) {
			boolean valid = true;
			//j, rader, indexet i arrayen representerar rader
			for (int j = 0; j < row; j++) {
				//Om det finns en annan drottning p� raden, kan drottning ej placeras d�r
				if (i == rows[j]) {
					valid = false;
				}
				else {
					//Offset = rad-kolumn, identifierar vilken diagonal man befinner sig p�
					int offset = row - j;
					//Annars om det finns en drottning p� diagonalen, kan den ej heller placeras d�r
					//Om det p� n�gon rad finns en drottning vars kolumnindex �r samma som kolumnindexet man f�rs�ker placera en dam p�
					//plus eller minus diagonal-offsetet, �r drottningarna p� samma rad och det g�r allts� ej att placera en drottning d�r.
					if (rows[j] == (i + offset) || rows[j] == (i - offset)) {
						valid = false;
					}
				}
			}
			//Om drottningen �r ensam p� sin kolumn, och diagonal, placera drottningen
			rows[row] = i;
			//Om det inte finns en drottning p� varje rad, placera ut ytterligare en
			if(valid && (row + 1) < rows.length) {
				valid = setQueen(rows, maxcol, (row + 1));
			}
			//Ett resultat har hittats och alla drottningar �r utplacerade, visa grafisk representation
			if (valid) {
				showQueens(rows);
				//V�nta p� knapptryckning f�r att b�rja producera n�sta placering
				while (run == false) {
					try {
						Thread.sleep(1);
					}
					catch (InterruptedException e) {
					}
				}
				run = false;
			}
		}
		//Om inga m�jliga placeringar finns, returnera false
		return false;
	}

	public static void main(String[] arg) {
		new Lab1_22Queens();
	}
}