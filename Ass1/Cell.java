
public class Cell {
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
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public State getState()
	{
		return currentState;
	}
	
	public void setState(State state)
	{
		currentState = state;
	}
	
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

	public boolean isObstacle()
	{
		return currentState == State.OBSTACLE;
	}
}
