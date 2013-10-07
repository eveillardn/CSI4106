import java.util.ArrayList;
import java.util.List;


public class Solver
{
	private boolean debug;
	private Board board;
	private Cell current;
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
	
	public List<Cell> findPath(Cell start)
	{
		this.current = start;
		smileys.remove(start);
		
		List<Cell> l = new ArrayList<Cell>();
		l.add(start);
		Node n = new Node(null, start, l, 0);
		
		return findPath(n);
	}
	
	public List<Cell> findPath(Node n)
	{
		if (n.current == board.getHome())
		{
			if (smileys.isEmpty())
			{
				notifyListeners();
			}
			
			return n.visited;
		}
		
		n.left = checkCell(n, Board.Neighbour.LEFT);
		n.right = checkCell(n, Board.Neighbour.RIGHT);
		n.up = checkCell(n, Board.Neighbour.UP);
		n.down = checkCell(n, Board.Neighbour.DOWN);
		
		notifyListeners();
		
		Node next = getLowestWeightedChildren(n);
		if (next == null)
		{
			next = n.parent;
			next.visited.add(n.current);
		}
		
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
	
	private Node checkCell(Node node, Board.Neighbour direction)
	{
		Cell neighbour = board.getNeighbour(node.current, direction);
		if (neighbour != null && !neighbour.isObstacle() && !node.visited.contains(neighbour))
		{
			List<Cell> visited = new ArrayList<Cell>(node.visited);
			visited.add(neighbour);
			return new Node(node, neighbour, visited, getHeuristic(neighbour));
		} else {
			return null;
		}
	}
	
	private Node getLowestWeightedChildren(Node n)
	{
		int lowestWeight = Integer.MAX_VALUE;
		Node lowestWeightedChildren = null;
		
		if (n.left != null) {
			lowestWeight = n.left.weight;
			lowestWeightedChildren = n.left;
		}
		
		if (n.right != null && n.right.weight < lowestWeight) {
			lowestWeight = n.right.weight;
			lowestWeightedChildren = n.right;
		}
		
		if (n.up != null && n.up.weight < lowestWeight) {
			lowestWeight = n.up.weight;
			lowestWeightedChildren = n.up;
		}
		
		if (n.down != null && n.down.weight < lowestWeight) {
			lowestWeight = n.down.weight;
			lowestWeightedChildren = n.down;
		}
		
		return lowestWeightedChildren;
	}
	
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
		
		if (debug) {
			System.out.println("Cell " + c.getCoordinates() + ": " + Math.abs(height)
				+ " vertical squares travelled with " + obstaclesY + " obstacles and " + Math.abs(width)
				+ " horizontal squares travalled with " + obstaclesX + " obstacles. Final weight = " + weight);
		}
		
		return weight;
	}
	
	public void addListener(SolverListener l)
	{
		listeners.add(l);
	}
	
	private void notifyListeners()
	{
		for (SolverListener l : listeners)
		{
			l.boardUpdated();
		}
	}
}
