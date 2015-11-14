package main.gamestate;

import main.gamestate.GameStateManager.GameStates;

/** Base Class for a game state. */
public abstract class GameState
{
	private GameStates state;
	private GameStates newState;
	
	public GameState(GameStates state)
	{
		this.state = state;
		this.newState = state;
	}
	
	/** Performs setup operations for a game state. */
	public abstract void setup();
	/** State-specific processing operations. */
	public abstract void cycle();
	/** Do any important cleanup-related operations before stopping a game
	 * state, like saving, etc.
	 */
	public abstract void cleanup();
	
	/** Determines if this game state should be transitioned. */
	public boolean isChangeStateIndicated()
	{
		return (state != newState);
	}
	
	/** Get the new game state to transition to. */
	public GameStates getNewState()
	{
		return newState;
	}
	
	/** Indicate changing to a new game state and set the new state. */
	protected void changeState(GameStates newState)
	{
		this.newState = newState;
	}
}
