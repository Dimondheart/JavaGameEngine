package core.gamestate;

import java.io.Serializable;

import core.gamestate.GameStateManager.GameStates;

/** Base Class for a game state.
 * @author Bryan Bettis
 */
public abstract class GameState implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** Renders the FPS to the screen. */
	protected FPSRenderer fpsRenderer;
	
	private GameStates state;
	private GameStates newState;
	
	/** Basic constructor.
	 * @param state the GameStates for the subclass
	 */
	public GameState(GameStates state)
	{
		fpsRenderer = new FPSRenderer();
		this.state = state;
		this.newState = state;
	}
	
	/** Performs setup operations for a game state. */
	public abstract void setup();
	/** State-specific processing operations. */
	public abstract void cycle();
	/** Cleanup operations specific to a game state. */
	protected abstract void cleanupState();
	
	/** Do any important cleanup-related operations before stopping a game
	 * state, like auto-saving, etc.
	 */
	public void cleanup()
	{
		fpsRenderer.destroy();
		cleanupState();
	}
	
	/** Determines if this game state should be transitioned.
	 * @return true if the state needs to be  changed
	 */
	public boolean isChangeStateIndicated()
	{
		return (state != newState);
	}
	
	/** Get the new game state to transition to.
	 * @return the GameStates for the new state
	 */
	public GameStates getNewState()
	{
		return newState;
	}
	
	/** Indicate changing to a new game state and set the new state.
	 * @param newState what GameStates to change to next
	 */
	protected void changeState(GameStates newState)
	{
		this.newState = newState;
	}
}
