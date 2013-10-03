import java.util.HashMap;


public class AStarAss1 {

	private static final boolean DEBUG = true;

	public static void main(String[] args) {
		Board board = new Board("S  O  S\n O  O  \n O  O  \n OOHO  \n  O    \n  OOO  \n     S"); //poulate board with obsticals and specified starting positions for smilies
		solver(board);
		;
	}
	
	
	public static HashMap<Cell,int[][]> solver(Board board){
		HashMap<Cell,int[][]> solution = new HashMap<Cell,int[][]>();

		for (int i = 0; i<board.getWidth(); i++){
			for (int j = 0; j<board.getHeight(); j++){
				Cell cell = board.getCell(i,j);
				if (cell.getState() == Cell.State.SMILEY){
					int[][] path = findPath(cell, board);
					solution.put(cell,path);
				}
			}
		}
		return solution;
	}

	
	private static int[][] findPath(Cell firstCell, Board board) {
		int[][] path = null;
		//sometree = new sometype;	//TODO: Find proper tree to use
		Cell lastCell = firstCell;
		
		
		
		
		return path;
	}
	
	public static int getHeuristic(Cell c, Board b){
		if (c.isObstacle()){
			throw new IllegalArgumentException("Cannot calculate heuristic on an obstacle.");
		}

		Cell h = b.getHome();

		int width = h.getX() - c.getX();
		int height = h.getY() - c.getY();
		int obstaclesX = 0;
		int obstaclesY = 0;
		
		if (Math.abs(height) > 0) {
			for (int i = 1; i <= Math.abs(height); i++) {
				if (height > 0) {
					if (b.getCell(c.getX(), c.getY() + i).isObstacle()) {
						obstaclesY++;
					}
				}
				else {
					if (b.getCell(c.getX(), c.getY() - i).isObstacle()) {
						obstaclesY++;
					}
				}
			}
		}
		if (Math.abs(width) > 0) {
			for (int i = 1; i <= Math.abs(width); i++){
				if(width > 0) {
					if (b.getCell(c.getX() + i, h.getY()).isObstacle()) {
						obstaclesX++;
					}
				}
				else {
					if (b.getCell(c.getX() - i, h.getY()).isObstacle()) {
						obstaclesX++;
					}
				}
			}
		}
		
		int weight = Math.abs(width) + Math.abs(height) + obstaclesX + obstaclesY;
		
		if (DEBUG) {
		System.out.println("Cell (" + c.getX() + "," + c.getY() + "): " + Math.abs(height)
				+ " vertical squares travelled with " + obstaclesY + " obstacles and " + Math.abs(width)
				+ " horizontal squares travalled with " + obstaclesX + " obstacles. Final weight = " + weight);
		}
		
		return weight;
	}

}
