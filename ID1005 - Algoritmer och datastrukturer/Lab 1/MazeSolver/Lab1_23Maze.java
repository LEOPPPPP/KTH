import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Lab1_23Maze extends JFrame {
	private final int xbound;
	private final int ybound;
	private int[][] maze;

	//Konstruktor, s�tter bounds, size, osv.
	public Lab1_23Maze(int xin, int yin) {
		xbound = xin;
		ybound = yin;
		maze = new int[xbound][ybound];
		createMaze(0, 0);
		solve(0 , 0, xbound-1, ybound-1, null);
		this.setVisible(true);
		this.setSize(48*xbound, 50*ybound);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
	}

	//Metod som skapar en labyrint. Anv�nder Recursive Backtracking f�r att skapa labyrinten.
	//G�r i random riktning, om den �r inom bounds och �r obes�kt. Kallar sedan p� sig sj�lv fr�n den nya positionen.
	public void createMaze(int inx, int iny) {
		//Slumpar fram olika directions och g�r i tur och ordning till dem, om det g�r.
		DIR[] dirs = DIR.values();
		Collections.shuffle(Arrays.asList(dirs));
		for (DIR dir : dirs) {
			//N�sta ruta �r nuvarande ruta + random direction
			int newx = inx + dir.x;
			int newy = iny + dir.y;
			if ((newx >= 0) && (newx < xbound) && (newy >= 0) && (newy < ybound) && (maze[newx][newy] == 0)) {
				//L�gg till vilket h�ll det saknas en v�gg i nuvarande yta, l�gger till bitrepresentation
				maze[inx][iny] += dir.id;
				//L�gg till vilket h�ll det saknas en v�gg i n�sta ruta, rutan man kommer till.
				maze[newx][newy] += dir.opposite.id;
				createMaze(newx, newy);
			}
		}
	}
	//Metod som l�ser en labyrint. Anv�nder Recursive Backtracking som metod f�r att hitta en v�g genom labyrinten.
	public boolean solve(int startx, int starty, int endx, int endy, DIR indir) {
		//Om positionen man �r p� �r utanf�r labyrinten, returna false
		if (!(startx >= 0) && (startx < xbound) && (starty >= 0) && (starty < ybound)) {
			return false;
		}
		//Om man har g�tt igenom hela labyrinten, returna true
		if (startx == endx && starty == endy) {
			maze[startx][starty] |= 1024;
			return true;
		}
		DIR newdir;
		DIR[] dirs = DIR.values();
		for (DIR dir : dirs) {
			//Om det g�r att g� i riktningen och den valda riktningen inte �r samma som man kom ifr�n, g� dit�t.
			if ((maze[startx][starty] & dir.id) == dir.id && indir != dir.opposite) {
				//Markera rutan som l�sning till labyrinten
				maze[startx][starty] |= 1024;
				newdir = dir;
				int newx = startx + dir.x;
				int newy = starty + dir.y;
				//Ta ett till steg i labyrinten utifr�n den nya positionen
				if (solve(newx, newy, endx, endy, newdir) == true) {
					return true;
				}
			}
		}
		//Avmarkera rutan som l�sning till labyrinten om v�gen inte ledde till en l�sning.
		maze[startx][starty] &= ~1024;
		return false;
	}

	//Enum f�r att h�lla koll p� riktningar samt id f�r hur g�ngarna g�r.
	private enum DIR {
		N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);
		private final int id;
		private final int x;
		private final int y;
		private DIR opposite;

		static {
			N.opposite = S;
			S.opposite = N;
			E.opposite = W;
			W.opposite = E;
		}

		private DIR(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
	};

	//Skapar en grafisk representation av labyrinten
	public void paint(Graphics g) {
		int col, row, cellsize = 22;
		g.setColor(Color.BLACK);
		g.fillRect(0 , 0, 10000, 10000);
		for (int rowid = 0; rowid < ybound; rowid++) {
			for (int colid = 0; colid < xbound; colid++) {
				row = rowid*2*cellsize + 51;
				col = 2*colid*cellsize + 30;
				//Om aktuell cell tillh�r l�sningen p� labyrinten, markera cellen bl�.
				if ((maze[colid][rowid] & 1024) == 1024) {
					g.setColor(Color.BLUE);
					g.fillRect(col, row, cellsize, cellsize);
					//Om det finns en cell �t �ster och den ocks� �r en l�sning,
					//markera g�ngen mellan aktuell cell och cellen �sterut bl�.
					if ((maze[colid][rowid] & 4) == 4 && (maze[colid+1][rowid] & 1024) == 1024) {
						g.fillRect(col+cellsize, row, cellsize, cellsize);
					}
					else if ((maze[colid][rowid] & 4) == 4) {
						g.setColor(Color.WHITE);
						g.fillRect(col+cellsize, row, cellsize, cellsize);
					}
					//Om det finns en cell �t s�der och den ocks� �r en l�sning,
					//markera g�ngen mellan aktuell cell och cellen s�derut bl�.
					if ((maze[colid][rowid] & 2) == 2 && (maze[colid][rowid+1] & 1024) == 1024) {
						g.setColor(Color.BLUE);
						g.fillRect(col, row+cellsize, cellsize, cellsize);
					}
					else if ((maze[colid][rowid] & 2) == 2) {
						g.setColor(Color.WHITE);
						g.fillRect(col, row+cellsize, cellsize, cellsize);
					}
				}
				//Om aktuell cell inte tillh�r l�sningen, markera den vit.
				else  {
					g.setColor(Color.WHITE);
					g.fillRect(col, row, cellsize, cellsize);
					//Om det finns en cell �sterut, markera �ven denna vit.
					if ((maze[colid][rowid] & 4) == 4) {
						g.fillRect(col+cellsize, row, cellsize, cellsize);
					}
					//Om det finns en cell s�derut, markera �ven denna vit.
					if ((maze[colid][rowid] & 2) == 2) {
						g.fillRect(col, row+cellsize, cellsize, cellsize);
					}
				}
			}
		}
	}

	public static void main(String[] arg) {
		Lab1_23Maze maze = new Lab1_23Maze(30, 15);
	}
}
