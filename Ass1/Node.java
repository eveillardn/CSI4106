import java.util.List;

/**
 * This class stores a node of our solution tree.
 */
public class Node
{
	public int weight;
	public Node parent;
	public Cell current;
	public List<Cell> visited;
	
	public Node left;
	public Node right;
	public Node up;
	public Node down;
	
	public Node(Node parent, Cell current, List<Cell> visited, int weight)
	{
		this.parent = parent;
		this.current = current;
		this.visited = visited;
		this.weight = weight;
	}
}
