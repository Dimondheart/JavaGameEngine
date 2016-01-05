package core;

import java.net.URISyntaxException;

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
	/** The setup of the program files (jar, file, etc.). */
	private static String URI_SCHEME;
	
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
	
	// Static variables setup
	static
	{
		// Determine the URI scheme to see if we are running in a Jar, etc.
		try
		{
			URI_SCHEME = core.GameSession.class.getResource(
					"/core"
					).toURI().getScheme();
		}
		// Unable to determine the URI scheme
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			System.out.println(
					"ERROR: Unable to determine the layout of the game files."
					);
			System.exit(0);
		}
	}
	
	/** Normal game constructor. Sets up the subsystems (graphics,
	 * sound, etc).
	 */
	public GameSession()
	{
		// Setup graphics system
		gfx = new GfxManager();
		// Setup user input system
		input = new InputManager(GfxManager.getMainWin());
		// Setup sound system
		sound = new SoundManager();
		// Setup the game state manager
		gsm = new GameStateManager();
		// Setup the time manager for the main game thread
		clock = new ThreadClock(10);
	}
	
	/** Gets the URI setup of this program; "jar" indicates we are running
	 * inside a jar file, "file" means we are running from a basic file
	 * system directory structure, etc.
	 * @return the URI scheme
	 */
	public static String getURIScheme()
	{
		// Return a copy of this string to prevent modification
		return URI_SCHEME.substring(0);
	}
	
	/** Start all subsystems then begins the main game loop. */
	public void start()
	{
		// Start the subsystems
		input.start();
		gfx.start();
		sound.start();
		// Start the program timer
		ProgramTimer.setup();
		// Go to the main loop
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
