import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class AStarAss1
{
	public static void main(String[] args)
	{
		String s = "";
		try {
			s = readFile("board.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Board board = new Board(s);
		List<Cell> smileys = board.getSmileys();
		Solver solver = new Solver(board);
		
		for (Cell smiley : smileys)
		{
			System.out.print("Path from smiley " + smiley.getCoordinates() + ": ");
			List<Cell> l = solver.findPath(smiley);
			for (Cell c : l)
			{
				System.out.print(c.getCoordinates() + " ");
			}
			System.out.println();
		}		
	}
	
	private static String readFile(String path) throws IOException 
	{
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not read from file", e);
		}
	}
	
}
