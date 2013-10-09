import java.util.ArrayList;
import java.util.List;

/**
 * This class stores the board of a maze.
 * The cell (0,0) is the top-left most cell.
 */
public class Board
{
	public enum Neighbour
	{
		LEFT, RIGHT, UP, DOWN
	}
	
	private Cell[][] cells;
	private Cell home;
	private List<Cell> smileys;
	private int width;
	private int height;
	
	public Board(String input)
	{
		setCells(input);
	}
	
	/**
	 * Returns cell (x,y) of the board.
	 */
	public Cell getCell(int x, int y)
	{
		return cells[x][y];
	}
	
	/**
	 * Returns the home cell of the board.
	 */
	public Cell getHome()
	{
		return home;
	}
	
	/**
	 * Checks if specified cell is the home cell of this board.
	 */
	public boolean isHome(Cell cell)
	{
		return cell.getX() == home.getX() && cell.getY() == home.getY();
	}
	
	/**
	 * Returns the width of the board.
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Returns the height of the board.
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Returns a list of all smileys on the board.
	 */
	public List<Cell> getSmileys()
	{
		return smileys;
	}
	
	/**
	 * Reads a string representation of the board and generates
	 * an array of cells representation of that board.
	 */
	private void setCells(String input)
	{
		if (input.isEmpty())
		{
			throw new IllegalArgumentException("Empty board is not supported.");
		}
		
		// Rows are newline separated.
		String[] lines = input.split("\\r?\\n");
		height = lines.length;
		width = lines[0].length();
		cells = new Cell[width][height];
		home = null;
		smileys = new ArrayList<Cell>();
		
		// Loop through the string and convert individual characters to Cell objects.
		Cell.State state;
		char c;
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				try
				{
					c = lines[y].charAt(x);

					switch(c)
					{
						case 'S':
							state = Cell.State.SMILEY;
							break;
							
						case 'H':
							state = Cell.State.HOME;
							break;
							
						case 'O':
							state = Cell.State.OBSTACLE;
							break;
							
						default:
							state = Cell.State.EMPTY;
						break;
					}
					
					cells[x][y] = new Cell(x, y, state);
				} catch (IndexOutOfBoundsException e) {
					throw new IllegalArgumentException("Every line of your board must be the same length.");
				}
				
				if (state == Cell.State.HOME)
				{
					if (home == null)
					{
						home = cells[x][y];
					} else {
						throw new IllegalArgumentException("Only one HOME cell is allowed.");
					}
				} else if (state == Cell.State.SMILEY)
				{
					smileys.add(cells[x][y]);
				}
			}
		}
		
		if (home == null)
		{
			throw new IllegalArgumentException("No HOME cell provided!");
		}
	}
	
	/**
	 * Converts current board into a string using
	 * the character equivalent of Cell objects.
	 */
	public String toString()
	{
		String s = "";
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				s += cells[x][y].toString();
			}
			s += "\n";
		}
		
		return s;
	}

	/**
	 * Returns cell next to the given cell in the specified direction,
	 * or null if it doesn't exist.
	 */
	public Cell getNeighbour(Cell current, Neighbour direction)
	{
		if (direction == Neighbour.DOWN && current.getY() < height - 1)
		{
			return getCell(current.getX(), current.getY() + 1);
		} else if (direction == Neighbour.LEFT && current.getX() > 0)
		{
			return getCell(current.getX() - 1, current.getY());
		} else if (direction == Neighbour.RIGHT && current.getX() < width - 1)
		{
			return getCell(current.getX() + 1, current.getY());
		} else if (direction == Neighbour.UP && current.getY() > 0)
		{
			return getCell(current.getX(), current.getY() - 1);
		}
		
		return null;
	}
	
}
