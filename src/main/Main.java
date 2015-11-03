package main;

import main.Game;
import main.input.InputManager;

/** The main entrypoint for this game. */
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
		System.exit(0);
	}
}
