
public class Cell {
	public enum State
	{
		EMPTY, OBSTACLE, SMILEY, HOME
	}
	
	private State currentState;
	
	public Cell()
	{
		this(State.EMPTY);
	}
	
	public Cell(State state)
	{
		setState(state);
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
}
