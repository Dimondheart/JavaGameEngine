package game.gamestate;

import static java.awt.event.KeyEvent.*;
import static java.awt.event.MouseEvent.*;

import core.gamestate.GameState;
import core.gamestate.GameStateManager.GameStates;
import core.input.InputManager;

/** The main menu and default game state.
 * @author Bryan Bettis
 */
public class MainMenuTest extends GameState
{
	private static final long serialVersionUID = 1L;
	
	private CtrlRenderer controls;
	
	public MainMenuTest()
	{
		super(GameStates.MAIN_MENU);
	}
	
	@Override
	public void setup()
	{
		String[] cL = new String[1];
		cL[0] = "Click to start.";
		controls = new CtrlRenderer(cL);
		// Display the FPS on the highest layer
		fpsRenderer.show(core.graphics.GfxManager.NUM_MAIN_LAYERS-1);
	}

	@Override
	public void cycle()
	{
		if (InputManager.getMS().justClicked(BUTTON1))
		{
			this.changeState(GameStates.SAMPLE_PLAY);
		}
	}

	@Override
	public void cleanupState()
	{
		controls.destroy();
	}
}
