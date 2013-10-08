/**
 * This class stores a node of our solution tree.
 */
public class Node
{
	/**
	 * The cost of each traveled cell in g(x).
	 */
	private static final int G_COST_PER_CELL = 1;
	
	private int heuristic; // h'(x)
	public Node parent;
	public Cell current;
	
	public Node left;
	public Node right;
	public Node up;
	public Node down;
	
	public Node(Cell current, Node parent, int heuristic)
	{
		this.parent = parent;
		this.current = current;
		this.heuristic = heuristic;
	}
	
	/**
	 * Calculates recursively the weight of a cell by adding its heuristic value
	 * and the distance from the starting point (where there is no parent).
	 */
	public int getWeight()
	{
		int weight = heuristic;
		Node p = parent;
		
		while (p != null)
		{
			weight += G_COST_PER_CELL;
			p = p.parent;
		}
		
		return weight;
	}
}
