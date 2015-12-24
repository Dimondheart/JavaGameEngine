package core.gamestate;

import game.gamestate.MainMenuTest;
import game.gamestate.SamplePlay;

/** Manages the game states, including handling cycling and transition
 * between game states.
* @author Bryan Bettis
*/
public class GameStateManager
{
	private GameState currGS;
	
	// TODO Replace this with class instanceof checking, etc.
	public enum GameStates
	{
		MAIN_MENU,
		SAMPLE_PLAY
	}
	
	/** Basic constructor, default start GameState is MainMenu. */
	public GameStateManager()
	{
		this(GameStates.MAIN_MENU);
	}
	
	/** Takes an argument to specify the starting GameState. */
	public GameStateManager(GameStates initState)
	{
		setNewGameState(initState);
	}
	
	/** Cycle the current game state. */
	public void cycle()
	{
		try
		{
			// Loop until the current state should be transitioned
			if (!currGS.isChangeStateIndicated())
			{
				// Call one cycle of events for this game state
				currGS.cycle();
			}
			else
			{
				// Change the game state
				setNewGameState(currGS.getNewState());
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
	
	/** Selects and sets the new game state object. */
	private void setNewGameState(GameStates newState)
	{
		// Cleanup after previous game state (if any)
		if (currGS != null)
		{
			cleanup();
		}
		// Create the new game state
		switch (newState)
		{
			// The classic space invaders mode
			case SAMPLE_PLAY:
				currGS = new SamplePlay();
				break;
			
			// Contains things like options and loading/starting a level
			case MAIN_MENU:
				// Fall through
			
			// Default to the main menu
			default:
				currGS = new MainMenuTest();
		}
		// Setup the new game state
		currGS.setup();
	}
}
