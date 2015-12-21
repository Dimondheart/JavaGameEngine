package game;

import main.Game;

/** The main entry point for this program. */
public class Main
{
	/** The game session object. */
	private static Game session;
	
	public static void main(String[] args)
	{
		// Setup and start the game session
		session = new Game();
		session.start();
		// When the game has stopped, stop the program
		System.out.println("Quitting...");
		System.exit(0);
	}
}
