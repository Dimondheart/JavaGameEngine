package core;

import core.gamestate.GameStateManager;
import core.graphics.GfxManager;
import core.sound.SoundManager;
import core.userinput.InputManager;

/** Starts up all subsystems for managing a game session and handles
 * the main game loop (calls the game state manager).
 * @author Bryan Bettis
 */
public class GameSession
{
	/** The graphics system. */
	private GfxManager gfx;
	/** The system that manages all user input. */
	private InputManager input;
	/** The sound system. */
	private SoundManager sound;
	/** Manages the state of the game. */
	private GameStateManager gsm;
	/** Manages timing of the main thread. */
	private ThreadClock clock;
	
	/** Normal game constructor. Also sets up the subsystems. */
	public GameSession()
	{
		// Setup graphics system
		gfx = new GfxManager();
		// Setup user input system
		input = new InputManager(GfxManager.getMainWin());
		// Setup sound system
		sound = new SoundManager();
		// Setup the game state manager
		gsm = new GameStateManager(GameStateManager.GameStates.MAIN_MENU);
		// Setup the time manager for the main game thread
		clock = new ThreadClock(10);
	}
	
	/** Start all subsystems then begins the main game loop. */
	public void start()
	{
		input.start();
		gfx.start();
		sound.start();
		// Start the program timer
		ProgramTimer.setup();
		play();
	}

	/** The main game loop. Calls the game state manager to update the current
	 * game state.
	 */
	private void play()
	{
		while(true)
		{
			clock.nextCycle();
			// Normal cycle; game is not paused, quitting, etc.
			if (InputManager.getState() == InputManager.State.NORMAL)
			{
				// Update input devices
				InputManager.poll();
				// Do one update cycle of the current game state
				gsm.cycle();
			}
			// Input manager indicated that the program should quit
			else if (InputManager.getState() == InputManager.State.QUIT)
			{
				// Clean up the current game state before quitting
				gsm.cleanup();
				break;
			}
		}
	}
}
