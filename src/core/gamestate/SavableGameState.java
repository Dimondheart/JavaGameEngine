package core.gamestate;

import core.gamestate.GameStateManager.GameStates;

/** A simple base class for any game state that is setup to be able to
 * save and load from a save file(s).
 * @author Bryan Bettis
 */
public abstract class SavableGameState extends GameState
{
	private static final long serialVersionUID = 1L;

	/** Basic constructor.
	 * @param state the GameStates of this game state
	 */
	public SavableGameState(GameStates state)
	{
		super(state);
	}
}
