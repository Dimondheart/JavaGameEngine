package main.gamestate;

/** A simple base class for any game state that is setup to be able to
 * save and load from a save file(s).
 */
public abstract class SavableGameState extends GameState
{
	private static final long serialVersionUID = 1L;

	/** Basic constructor. */
	public SavableGameState(main.gamestate.GameStateManager.GameStates state)
	{
		super(state);
	}
}
