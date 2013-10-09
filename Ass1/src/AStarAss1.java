import java.awt.Container;
import java.awt.Font;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * Main class that reads a board from a text file and 
 * calls the A* algorithm to find a path for all smileys.
 */
@SuppressWarnings("serial")
public class AStarAss1 extends JFrame implements SolverListener
{
	/**
	 * Defines if we want a GUI representation of the solutions.
	 * If set to false, the program will simply return the solutions.
	 */
	private static final boolean GUI = true;
	
	/**
	 * Set to true if you want to see the value of
	 * every calls of the heuristic function
	 */
	private static final boolean DEBUG = false;
	
	/**
	 * Centers the coordinates around home.
	 * That would mean the home has coordinates of (0,0)
	 * and the top-left cell has coordinates of (-width/2, height/2).
	 */
	private static final boolean CENTER_COORDINATES_ON_HOME = true;
	
	/**
	 * Specifies the delay between each move for the GUI representation
	 * of the solutions, in milliseconds.
	 */
	private static final int DELAY = 500;
	
	private JTextArea textArea;
	private Board board;
	
	/**
	 * When instantiated, this class creates a frame
	 * that holds the GUI representation of the solutions.
	 */
	public AStarAss1(Board board)
	{
		super("Assignment 1");
		
		this.board = board;
		Container content = getContentPane();
		textArea = new JTextArea();
		textArea.setFont(new Font("Courier New", 0, 15));
		textArea.setEditable(false);
		content.add(textArea);
		textArea.setText(board.toString());
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(200, 200);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		// We read the board from a text file
		String s = readFile("board.txt");
		
		Board board = new Board(s);
		List<Cell> smileys = board.getSmileys();
		Solver solver = new Solver(board, DEBUG);
		
		// If we want a GUI we instantiate this class as a listener to the solving class
		if (GUI)
		{
			solver.addListener(new AStarAss1(board));
		}
		
		// We loop through the smileys and print their solutions.
		List<Integer> winners = new ArrayList<Integer>();
		int x, y, steps, minSteps = Integer.MAX_VALUE;
		Cell smiley;
		List<Cell> path;
		for (int i = 0; i < smileys.size(); i++)
		{
			smiley = smileys.get(i);
			path = solver.findPath(smiley);
			steps = path.size();
			
			if (steps < minSteps)
			{
				minSteps = steps;
				winners = new ArrayList<Integer>();
				winners.add(new Integer(i));
			} else if (steps == minSteps)
			{
				winners.add(new Integer(i));
			}
			
			System.out.print("Smiley #" + (i+1) + ": ");
			for (Cell c : path)
			{
				x = c.getX();
				y = c.getY();
				
				if (CENTER_COORDINATES_ON_HOME)
				{
					x -= board.getHome().getX();
					y = -1 * y + board.getHome().getY();
				}
				
				System.out.print("(" + x + " " + y + ") ");
			}
			System.out.println(steps + " steps");
		}
		
		// We announce the winner(s).
		if (winners.size() > 1)
		{
			System.out.print("The winners of the race are ");
			for (int i = 0; i < winners.size(); i++)
			{
				if (i != 0)
				{
					if (winners.size() > 2)
					{
						System.out.print(", ");
					} else {
						System.out.print(" and ");
					}
				}
				System.out.print("Smiley #" + (winners.get(i).intValue()+1));
			}
		} else {
			System.out.print("The winner of the race is Smiley #" + (winners.get(0).intValue()+1));
		}
		System.out.println(" with " + minSteps + " steps.");
	}
	
	/**
	 * This method simply reads a file from the root of your project (outside the src folder)
	 * and returns a string with the content of the file, or throws an exception if not found.
	 */
	private static String readFile(String path)
	{
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not read from file", e);
		}
	}

	/**
	 * This method refreshes the GUI display whenever
	 * a change is made to the board.
	 */
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
