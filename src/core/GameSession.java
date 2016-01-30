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
	/** The last engine threading setting, used to detect when it has
	 * been changed so it can be applied.
	 */
	private DynamicSettings.ThreadingSetting lastThreadingSetting;
	
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
		lastThreadingSetting = 
				(DynamicSettings.ThreadingSetting)
				DynamicSettings.getSetting("ENGINE_THREADING");
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
	
	/** Start all subsystems then begin playing. */
	public void start()
	{
		while (true)
		{
			// Quit the game
			if (InputManager.getState().equals(InputManager.State.QUIT))
			{
				break;
			}
			// Determine how many threads we should run
			int numThreads = 1;
			int numCores = Runtime.getRuntime().availableProcessors();
			DynamicSettings.ThreadingSetting threadSetting =
					(DynamicSettings.ThreadingSetting)
					DynamicSettings.getSetting("ENGINE_THREADING");
			// Determine number of threads
			switch (threadSetting)
			{
				// Run all subsystems in their own thread
				case FULL:
					numThreads = 4;
					break;
				// Optimize threading based on available resources
				case OPTIMIZE:
					if (numCores >= 4)
					{
						numThreads = 4;
					}
					else if (numCores > 0)
					{
						numThreads = numCores;
					}
					break;
				// Run all subsystems in the main loop
				case SINGLE:
					// Fall Through
				default:
					numThreads = 1;
					break;
			}
			// All subsystems are threaded
			if (numThreads >= 4)
			{
				// Start the subsystems
				input.startThreaded();
				gfx.startThreaded();
				sound.startThreaded();
			}
			// Don't thread the input system
			else if (numThreads == 3)
			{
				input.startUnthreaded();
				gfx.startThreaded();
				sound.startThreaded();
			}
			// Don't thread the input and sound systems
			else if (numThreads == 2)
			{
				input.startUnthreaded();
				gfx.startThreaded();
				sound.startUnthreaded();
			}
			// Don't thread any subsystems
			else
			{
				input.startUnthreaded();
				gfx.startUnthreaded();
				sound.startUnthreaded();
			}
			// Start the program timer
			ProgramClock.setup();
			// Go to the main loop
			play(numThreads);
		}
	}

	/** Starts playing the game. */
	private void play(int numThreads)
	{
		// Select how to run based on how subsystems are setup
		switch (numThreads)
		{
			case 4:
				play4Threads();
				break;
			case 3:
				play3Threads();
				break;
			case 2:
				play2Threads();
				break;
			case 1:
				// Fall Through
			default:
				play1Thread();
				break;
		}
		// Stop all threaded subsystems before returning
		input.stopThread();
		sound.stopThread();
		gfx.stopThread();
		lastThreadingSetting = (DynamicSettings.ThreadingSetting)
				DynamicSettings.getSetting("ENGINE_THREADING");
	}
	
	/** Play with 3 subsystems running in separate threads. */
	private void play4Threads()
	{
		while(true)
		{
			clock.nextCycle();
			if (!mainLoop())
			{
				break;
			}
		}
	}
	
	/** Play with 2 subsystems running in separate threads. */
	private void play3Threads()
	{
		while(true)
		{
			clock.nextCycle();
			input.runCycle();
			if (!mainLoop())
			{
				break;
			}
		}
	}
	
	/** Play with 1 subsystem running in a separate thread. */
	private void play2Threads()
	{
		while(true)
		{
			clock.nextCycle();
			input.runCycle();
			sound.runCycle();
			if (!mainLoop())
			{
				break;
			}
		}
	}
	
	/** Play with all three subsystems running with the main loop. */
	private void play1Thread()
	{
		while(true)
		{
			clock.nextCycle();
			input.runCycle();
			sound.runCycle();
			gfx.runCycle();
			if (!mainLoop())
			{
				break;
			}
		}
	}
	
	/** The main loop of the game.  Updates the game state manager.
	 * @return true if the main loop should keep running, false otherwise
	 */
	private boolean mainLoop()
	{
		if (!lastThreadingSetting.equals(DynamicSettings.getSetting("ENGINE_THREADING")))
		{
			return false;
		}
		// Normal cycle; game is not paused, quitting, etc.
		else if (InputManager.getState() == InputManager.State.NORMAL)
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
			return false;
		}
		return true;
	}
}
