package main.gamestate;

public class GameStateManager
{
	private GameState currGS;
	
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
	
	/** Cleans up the current game state and destroys it. */
	public void cleanup()
	{
		currGS.cleanup();
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
		// Cleanup a previous game state
		if (currGS != null)
		{
			currGS.cleanup();
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
				currGS = new MainMenu();
		}
		// Setup the new game state
		currGS.setup();
	}
}
