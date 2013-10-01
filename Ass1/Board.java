
public class Board {	
	private Cell[][] cells;
	private Cell home;
	
	public Board(String input)
	{
		setCells(input);
	}
	
	public Cell getHome()
	{
		return home;
	}
	
	private void setCells(String input)
	{
		if (!input.isEmpty())
		{
			String[] lines = input.split("\\r?\\n");
			int width = lines.length;
			int height = lines[0].length();
			cells = new Cell[width][height];
			home = null;
			
			Cell.State state;
			char c;
			for (int i = 0; i < width; i++)
			{
				for (int j = 0; j < height; j++)
				{
					try {
						c = lines[i].charAt(j);
					} catch (IndexOutOfBoundsException e) {
						throw new IllegalArgumentException("Every line of your board must be the same length.");
					}
					
					switch(c)
					{
						case 'S':
							state = Cell.State.EMPTY;
							break;
							
						case 'H':
							if (home == null) {
								state = Cell.State.HOME;
							} else {
								throw new IllegalArgumentException("Only one HOME cell is allowed.");
							}
							break;
							
						case 'O':
							state = Cell.State.OBSTACLE;
							break;
							
						default:
							state = Cell.State.EMPTY;
						break;
					}
					
					cells[i][j] = new Cell(state);
					if (state == Cell.State.HOME)
					{
						home = cells[i][j];
					}
				}
			}
		}
	}
	
}
