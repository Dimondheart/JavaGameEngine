package game.gamestate;

//import static java.awt.event.KeyEvent.*;
import static java.awt.event.MouseEvent.*;

import java.util.concurrent.ConcurrentHashMap;

import core.gamestate.GameState;
import core.graphics.gui.Button;
import core.userinput.InputManager;
import game.CtrlRenderer;

/** The main menu and default game state.
 * @author Bryan Bettis
 */
public class MainMenuTest extends GameState
{
	private static final long serialVersionUID = 1L;
	
	private CtrlRenderer controls;
	private Button startBtn;
	
	@Override
	public void setup(ConcurrentHashMap<String, Object> args)
	{
		startBtn = new Button(150, 100, 100, 100);
		String[] cL = new String[1];
		cL[0] = "Click to start.";
		controls = new CtrlRenderer(cL);
		// Display the FPS on the highest layer
		fpsRenderer.showOnLayer(core.graphics.GfxManager.NUM_MAIN_LAYERS-1);
		startBtn.showOnLayer(9);
		startBtn.setText("Click to Start");
	}

	@Override
	public void cycle()
	{
		if (InputManager.getMS().justClicked(BUTTON1))
		{
			this.changeState(SamplePlay.class);
		}
	}

	@Override
	public void cleanupState()
	{
		controls.destroy();
		startBtn.destroy();
	}
}
