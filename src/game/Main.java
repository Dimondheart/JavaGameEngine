package game;

import core.GameSession;

/** The main entry point for this program. */
public class Main
{
	/** The game session object. */
	private static GameSession session;
	
	public static void main(String[] args)
	{
		// Setup and start the game session
		session = new GameSession();
		session.start();
		// When the game has stopped, stop the program
		System.out.println("Quitting...");
		System.exit(0);
	}
}
