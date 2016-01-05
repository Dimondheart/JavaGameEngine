package core.gamestate;

import java.util.concurrent.ConcurrentHashMap;

/** Manages the game states, including handling cycling and transition
 * between game states.
* @author Bryan Bettis
*/
public class GameStateManager
{
	/** The current game state object. */
	private GameState currGS;
	
	public GameStateManager()
	{
		// TODO make a way to specify this in the game or a game sub-package
		this(game.gamestate.MainMenuTest.class);
	}
	
	public GameStateManager(Class<? extends GameState> initialState)
	{
		this(initialState, new ConcurrentHashMap<String, Object>());
	}
	
	public GameStateManager(Class<? extends GameState> initialState, ConcurrentHashMap<String, Object> args)
	{
		setNewGameState(initialState, args);
	}
	
	/** Cycle the current game state. */
	public void cycle()
	{
		try
		{
			// Loop until the current state should be transitioned
			if (currGS.isChangeStateIndicated())
			{
				// Change the game state
				setNewGameState(currGS.getNewState(), currGS.getNewStateArgs());
				// Call one cycle of events for this game state
				currGS.cycle();
			}
			else
			{
				// Call one cycle of events for this game state
				currGS.cycle();
			}
		}
		catch (NullPointerException e)
		{
			// The current game state is not set
			System.out.println("No game state set.");
		}
	}
	
	/** Cleans up the current game state. */
	public void cleanup()
	{
		currGS.cleanup();
		core.userinput.InputManager.clear();
		currGS = null;
	}
	
	/** Save the current game state, if it is a save-able state. */
	public void saveState()
	{
		// TODO implement
		if (currGS instanceof SavableGameState)
		{
			System.out.println("Current State IS saveable");
		}
		else
		{
			System.out.println("Current State is NOT saveable");
		}
	}
	
	/** Cleans up any previous game state and creates then sets up the
	 * new game state.
	 * @param newState the class of the new state
	 * @param setupArgs the hash map of arguments to setup the new game state
	 */
	private void setNewGameState(Class<? extends GameState> newState, ConcurrentHashMap<String, Object> setupArgs)
	{
		// Cleanup after previous game state (if any)
		if (currGS != null)
		{
			cleanup();
		}
		// Create the new game state
		try
		{
			currGS = newState.newInstance();
		}
		// Class cannot be instantiated
		catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(
					"ERROR: GameState \'" +
							newState.getName() +
							"\' cannot be instantiated"
							);
			return;
		}
		// Unable to access the class
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			System.out.println(
					"ERROR: GameState \'" +
							newState.getName() +
							"\' is not accessable from " +
							this.getClass().getPackage().getName()
							);
			return;
		}
		// Setup the new game state
		currGS.setup(setupArgs);
	}
}
