import java.awt.Container;
import java.awt.Font;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class AStarAss1 extends JFrame implements SolverListener
{
	private static final boolean GUI = true;
	private static final boolean DEBUG = false;
	private static final int DELAY = 500;
	
	private JTextArea textArea;
	private Board board;
	
	public AStarAss1(Board board)
	{
		super("Assignment 1");
		
		this.board = board;
		Container content = getContentPane();
		textArea = new JTextArea();
		textArea.setFont(new Font("Courier New", 0, 15));
		content.add(textArea);
		textArea.setText(board.toString());
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(200, 200);
		setVisible(true);
	}
	
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
		Solver solver = new Solver(board, DEBUG);
		
		if (GUI)
		{
			SolverListener l = new AStarAss1(board);
			solver.addListener(l);
		}
		
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

	@Override
	public void boardUpdated()
	{
		textArea.setText(board.toString());
		
		try {
			Thread.sleep(DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
