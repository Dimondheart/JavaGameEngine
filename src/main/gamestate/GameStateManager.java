package main.gamestate;

public class GameStateManager
{
	private GameState currGS;
	public enum GameStates
	{
		MAIN_MENU,
		SAMPLE_PLAY
	}
	
	public GameStateManager()
	{
		this(GameStates.MAIN_MENU);
	}
	
	public GameStateManager(GameStates initState)
	{
		setNewGameState(initState);
	}
	
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
