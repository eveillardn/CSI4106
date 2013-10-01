import java.util.HashMap;


public class AStarAss1 {

	public static void main(String[] args) {
		HashMap<Integer,Integer> stack  = new HashMap<>();
		
		Cell[][] cells; //poulate board with obsticals and specified starting positions for smilies
		cells[0][0] = new Cell
		;
	}
	
	
	public HashMap<Cell,int[][]> solver(Cell[][] board){
		HashMap<Cell,int[][]> solution = new HashMap<>();
		for (Cell cell: board.getCells()){
			if (cell.getState() == Cell.State.SMILEY){
				int[][] tempPath = findPath(cell, board);
			}
		}
		
		for 
	}


	private int[][] findPath(Cell firstCell, Cell[][] board) {
		int[][] path = null;
		sometree = new sometype;//TODO: Find proper tree to use
		Cell homeCell = board.getHome();
		Cell lastCell = firstCell;
		
		while (board.compare(lastCell, homeCell))
		
		
		return path;
	}

}
