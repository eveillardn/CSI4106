import java.util.ArrayList;
import java.util.List;

public class Board {	
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
	
	public Cell getCell(int x, int y)
	{
		return cells[y][x];
	}
	
	public Cell getHome()
	{
		return home;
	}
	
	public boolean isHome(Cell cell)
	{
		return cell.getX() == home.getX() && cell.getY() == home.getY();
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public List<Cell> getSmileys()
	{
		return smileys;
	}
	
	private void setCells(String input)
	{
		if (input.isEmpty())
		{
			throw new IllegalArgumentException("Empty board is not supported.");
		}
		
		String[] lines = input.split("\\r?\\n");
		width = lines.length;
		height = lines[0].length();
		cells = new Cell[width][height];
		home = null;
		smileys = new ArrayList<Cell>();
		
		Cell.State state;
		char c;
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				try
				{
					c = lines[i].charAt(j);

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
					
					cells[i][j] = new Cell(j, i, state);
				} catch (IndexOutOfBoundsException e) {
					throw new IllegalArgumentException("Every line of your board must be the same length.");
				}
				
				if (state == Cell.State.HOME)
				{
					if (home == null)
					{
						home = cells[i][j];
					} else {
						throw new IllegalArgumentException("Only one HOME cell is allowed.");
					}
				} else if (state == Cell.State.SMILEY)
				{
					smileys.add(cells[i][j]);
				}
			}
		}
		
		if (home == null)
		{
			throw new IllegalArgumentException("No HOME cell provided!");
		}
	}
	
	public String toString()
	{
		String s = "";
		
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				s += cells[i][j].toString();
			}
			s += "\n";
		}
		
		return s;
	}

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
