package core;

import java.awt.event.KeyEvent;

import core.gamestate.GameStateManager;
import core.graphics.GfxManager;
import core.input.InputManager;
import core.sound.SoundManager;

//import static java.awt.event.KeyEvent.*;
//import static java.awt.event.MouseEvent.*;

/** Represents a game session. Handles game cycling.
 * @author Bryan Bettis
 */
public class Game
{
	/** The graphics renderer. */
	private GfxManager gfx;
	/** The manager of all input devices. */
	private InputManager input;
	/** Sound system manager. */
	private SoundManager sound;
	/** The game state manager. */
	private GameStateManager gsm;
	/** Thread manager for this object. */
	private ThreadClock clock;
	
	/** Sets up a standard game session. */
	public Game()
	{
		// Graphics manager
		gfx = new GfxManager();
		// Input device manager
		input = new InputManager(GfxManager.getMainWin());
		// Sound manager
		sound = new SoundManager();
		// Game state manager
		gsm = new GameStateManager(GameStateManager.GameStates.MAIN_MENU);
		clock = new ThreadClock(10);
	}
	
	/** Final setup and start playing. */
	public void start()
	{
		input.start();
		gfx.start();
		sound.start();
		// Start the program timer
		ProgramTimer.setup();
		play();
	}

	/** Play the game. */
	private void play()
	{
		while(true)
		{
			clock.nextCycle();
			// Normal operations
			if (InputManager.getState() == InputManager.State.NORMAL)
			{
				InputManager.poll();
				gsm.cycle();
				// Test of saving setup
				if (InputManager.getKB().justPressed(KeyEvent.VK_ENTER))
				{
					gsm.saveState();
				}
			}
			// Quit
			else if (InputManager.getState() == InputManager.State.QUIT)
			{
				// Allow the game state to safely stop
				gsm.cleanup();
				// Stop cycling
				break;
			}
		}
	}
}
