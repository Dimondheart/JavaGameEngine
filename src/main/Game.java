package main;

import main.input.InputManager;

import static main.input.InputManager.State;

/** Represents a game session. Handles game cycling. */
public class Game
{
	/** The graphics renderer. */
	private main.graphics.Gfx gfx;
	/** The manager of all input devices. */
	private InputManager input;
	/** Thread manager for this object. */
	private ThreadClock clock;
	
	/** Sets up a standard game session. */
	public Game()
	{
		gfx = new main.graphics.Gfx();
		// Input device manager
		input = new InputManager(main.graphics.Gfx.getMainWin());
		clock = new ThreadClock(10);
	}
	
	/** Final setup and start playing. */
	public void start()
	{
		input.start();
		gfx.start();
		play();
	}

	/** Play the game. */
	private void play()
	{
		while(true)
		{
			// Normal operations
			if (InputManager.getState() == State.NORMAL)
			{
				main.input.InputManager.poll();
			}
			// Quit
			else if (InputManager.getState() == State.QUIT)
			{
				break;
			}
			clock.nextCycle();
		}
	}
}
