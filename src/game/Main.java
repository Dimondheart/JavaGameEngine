package game;

import core.GameSession;

/** The main entry point for this program. */
public class Main
{
	/** The main function, creates and starts a new game session.
	 * @param args the system arguments
	 */
	public static void main(String[] args)
	{
		// Setup and start the game session
		GameSession session = new GameSession();
		session.start();
		// When the game has stopped, stop the program
		System.out.println("Quitting...");
		System.exit(0);
	}
}
