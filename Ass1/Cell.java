
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
		currentState = state;
	}
	
	public State getState()
	{
		return currentState;
	}
}
