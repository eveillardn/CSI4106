import java.util.ArrayList;
import java.util.List;

/**
 * This class calls the A* algorithm on a given board.
 */
public class Solver
{
	private boolean debug;
	private Board board;
	private Cell current;
	private Node[][] nodes;
	private List<Node> open;
	private List<Cell> smileys;
	private List<SolverListener> listeners = new ArrayList<SolverListener>();
	
	public Solver(Board b)
	{
		this(b, false);
	}
	
	public Solver(Board b, boolean d)
	{
		board = b;
		debug = d;
		smileys = new ArrayList<Cell>(b.getSmileys());
	}
	
	/**
	 * Finds the best path to home from the given cell.
	 */
	public List<Cell> findPath(Cell start)
	{
		current = start;
		nodes = new Node[board.getWidth()][board.getHeight()];
		open = new ArrayList<Node>();
		smileys.remove(start);
		
		Node n = new Node(start, null, 0);
		nodes[start.getX()][start.getY()] = n;
		
		return findPath(n);
	}
	
	/**
	 * Recursive call.
	 */
	private List<Cell> findPath(Node n)
	{
		// Base case: we're home, there is nothing more to visit.
		if (n.current == board.getHome())
		{
			if (smileys.isEmpty())
			{
				notifyListeners();
			}
			
			return generatePath(n);
		}
		
		open.remove(n);
		
		// We look around to discover each adjacent cell.
		n.left = checkCell(n, Board.Neighbour.LEFT);
		n.right = checkCell(n, Board.Neighbour.RIGHT);
		n.up = checkCell(n, Board.Neighbour.UP);
		n.down = checkCell(n, Board.Neighbour.DOWN);
		
		// At this point we notify our listener(s) that the board was updated.
		notifyListeners();
		
		// We get the lowest weighted node of the open list to determine which cell is next to visit.
		Node next = getLowestWeightedOpenNode();
		if (next == null)
		{
			// If no cell is left to be visited but we're not home, no solution could be found.
			return null;
		}
		
		// We update the position of the smiley on the board, if applicable
		if (current != null && !board.isHome(current) && !smileys.contains(current))
		{
			current.setState(Cell.State.EMPTY);
		}
		current = next.current;
		if (!board.isHome(current))
		{
			current.setState(Cell.State.SMILEY);
		}
		
		return findPath(next);
	}
	
	/**
	 * This generates the path to the home cell.
	 */
	private List<Cell> generatePath(Node n)
	{
		List<Cell> path = new ArrayList<Cell>();
		
		while (n != null)
		{
			path.add(0, n.current);
			n = n.parent;
		}
		
		return path;
	}

	/**
	 * We discover the cell next to the given current cell in the specified direction.
	 * If it is not valid, return null.
	 */
	private Node checkCell(Node node, Board.Neighbour direction)
	{
		Cell neighbour = board.getNeighbour(node.current, direction);
		
		// If the neighbour is not a valid cell we return null.
		if (neighbour == null)
		{
			return null;
		}
		
		// If the cell is already a node of our tree, we update its parent if it would optimize the weight
		if (nodes[neighbour.getX()][neighbour.getY()] != null)
		{
			if (open.contains(nodes[neighbour.getX()][neighbour.getY()]))
			{
				int previousWeight = nodes[neighbour.getX()][neighbour.getY()].getWeight();
				Node previousParent = nodes[neighbour.getX()][neighbour.getY()].parent;
				
				nodes[neighbour.getX()][neighbour.getY()].parent = node;
				int newWeight = nodes[neighbour.getX()][neighbour.getY()].getWeight();
				
				if (previousWeight <= newWeight)
				{
					nodes[neighbour.getX()][neighbour.getY()].parent = previousParent;
				}
			}
		// If it is a new walkable cell, we add it to our open list.
		} else if (!neighbour.isObstacle())
		{
			nodes[neighbour.getX()][neighbour.getY()] = new Node(neighbour, node, getHeuristic(neighbour));
			open.add(nodes[neighbour.getX()][neighbour.getY()]);
		}	
		
		return nodes[neighbour.getX()][neighbour.getY()];
	}
	
	/**
	 * Calculates the weight of each node in the open list
	 * and returns the one with the lowest value, or null if empty.
	 */
	private Node getLowestWeightedOpenNode()
	{
		int lowestWeight = Integer.MAX_VALUE;
		Node lowestWeightedNode = null;
		
		for (Node n : open)
		{
			if (n.getWeight() < lowestWeight)
			{
				lowestWeight = n.getWeight();
				lowestWeightedNode = n;
			}
		}
		
		return lowestWeightedNode;
	}
	
	/**
	 * Calculates the heuristic value for the specified cell.
	 */
	public int getHeuristic(Cell c)
	{
		if (c.isObstacle()) {
			throw new IllegalArgumentException("Cannot calculate heuristic on an obstacle.");
		}

		Cell h = board.getHome();

		int width = h.getX() - c.getX();
		int height = h.getY() - c.getY();
		int obstaclesX = 0;
		int obstaclesY = 0;
		
		// We iterate vertically to the home cell while counting obstacles.
		if (height != 0) {
			for (int i = 1; i <= Math.abs(height); i++) {
				if (height > 0) {
					if (board.getCell(c.getX(), c.getY() + i).isObstacle()) {
						obstaclesY++;
					}
				}
				else {
					if (board.getCell(c.getX(), c.getY() - i).isObstacle()) {
						obstaclesY++;
					}
				}
			}
		}
		
		// We iterate horizontally to the home cell while counting obstacles.
		if (width != 0) {
			for (int i = 1; i <= Math.abs(width); i++) {
				if (width > 0) {
					if (board.getCell(c.getX() + i, h.getY()).isObstacle()) {
						obstaclesX++;
					}
				}
				else {
					if (board.getCell(c.getX() - i, h.getY()).isObstacle()) {
						obstaclesX++;
					}
				}
			}
		}
		
		int weight = Math.abs(width) + Math.abs(height) + obstaclesX + obstaclesY;
		
		// If we're in debug mode, we print the values of the heuristic function.
		if (debug) {
			System.out.println("Cell " + c.getCoordinates() + ": " + Math.abs(height)
				+ " vertical squares travelled with " + obstaclesY + " obstacles and " + Math.abs(width)
				+ " horizontal squares travalled with " + obstaclesX + " obstacles. Final heuristic = " + weight);
		}
		
		return weight;
	}
	
	/**
	 * Adds a new listener to this class.
	 */
	public void addListener(SolverListener l)
	{
		listeners.add(l);
	}
	
	/**
	 * Loops through the listeners and notifies them that the board is updated.
	 */
	private void notifyListeners()
	{
		for (SolverListener l : listeners)
		{
			l.boardUpdated();
		}
	}
}
