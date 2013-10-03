import java.util.HashMap;


public class AStarAss1 {

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
	
	private int getHeuristic(Cell c, Board b){
		if (c.toString() =="O"){
			return 999;
		}

		Cell h = b.getHome();
		int hx= h.getX();
		int hy = h.getY();



		int cx = c.getX();
		int cy = c.getY();


		int width = hx-cx;
		int height = hy - cy;
		int obstacles = 0;
		//check for negative
		if (height==0){
			for(int i = 0; i< Math.abs(width); i++){
				if (b.getCell(cx+i, cy).toString() =="0"){
					obstacles++;
				}
			}return obstacles;
		}
		if (width==0){
			for(int i = 0; i< Math.abs(height); i++){
				if (b.getCell(cx, cy+1).toString() =="0"){
					obstacles++;
				}
			}return obstacles;
		}	
		boolean positivex;
		boolean positivey;
		if (height >0){
			positivey = true;
		}
		else{
			positivey = false;
		}

		if (width >0){
			positivex = true;
		}
		else{
			positivex = false;
		}

		if (Math.abs(height) >0 && Math.abs(width) >0){
			for (int i = 0; i<Math.abs(height); i++){

				if(positivey){
					if (b.getCell(cx, cy+i).toString() =="0"){
						obstacles++;
					}
				}
				else{
					if (b.getCell(cx, cy-i).toString() =="0"){
						obstacles++;
					}
				}
			}
			for (int i = 1; i<Math.abs(height); i++){
				if(positivex){

					if (b.getCell(cx+1, cy+height).toString() =="0"){
						obstacles++;
					}
				}
				else{
					if (b.getCell(cx-i,cy+height).toString() =="0"){
						obstacles++;
					}

				}
			}
		}
		return width+height+obstacles;
	}

}
