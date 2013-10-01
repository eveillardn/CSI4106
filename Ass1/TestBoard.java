import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestBoard
{

	
	public static void main(String[] args)
	{
		String s = "";
		try {
			s = readFile("board.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Board b = new Board(s);
		System.out.println(b);
		System.out.println("Home: (" + b.getHome().getX() + "," + b.getHome().getY() + ")");
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
