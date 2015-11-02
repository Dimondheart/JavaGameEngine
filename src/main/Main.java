package main;

import main.Game;
import main.input.InputManager;

/** The main entrypoint for this game. */
public class Main
{
	/** The manager of all inputs. */
	private static InputManager input;
	/** The game session object. */
	private static Game session;
	
	public static void main(String[] args)
	{
		input = new InputManager();
		input.start();
		session = new Game();
		session.start();
	}
}
