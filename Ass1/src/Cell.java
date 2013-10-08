/**
 * This object stores a single cell of the board.
 */
public class Cell
{
	public enum State
	{
		EMPTY, OBSTACLE, SMILEY, HOME
	}
	
	private int x;
	private int y;
	private State currentState;
	
	public Cell(int x, int y, State state)
	{
		this.x = x;
		this.y = y;
		setState(state);
	}
	
	/**
	 * Returns the X coordinate of the cell.
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * Returns the Y coordinate of the cell.
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * Returns the coordinates of the cell as a string.
	 */
	public String getCoordinates()
	{
		return "(" + x + "," + y + ")";
	}
	
	/**
	 * Return in which state the cell currently is.
	 */
	public State getState()
	{
		return currentState;
	}
	
	/**
	 * Sets the state of the cell to the specified state.
	 */
	public void setState(State state)
	{
		currentState = state;
	}
	
	/**
	 * Converts the cell to a character matching its state.
	 */
	public String toString()
	{
		switch(currentState)
		{			
		case OBSTACLE:
			return "O";
			
		case SMILEY:
			return "S";
			
		case HOME:
			return "H";
			
		default:
			return " ";
		}
	}

	/**
	 * Returns true if the cell is an obstacle.
	 */
	public boolean isObstacle()
	{
		return currentState == State.OBSTACLE;
	}
}
