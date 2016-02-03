package game.gamestate;

//import static java.awt.event.KeyEvent.*;
import static java.awt.event.MouseEvent.*;

import java.util.concurrent.ConcurrentHashMap;

import core.gamestate.GameState;
import core.graphics.gui.Button;
import core.userinput.InputManager;

/** The main menu and default game state.
 * @author Bryan Bettis
 */
public class MainMenuTest extends GameState
{
	private static final long serialVersionUID = 1L;
	
//	private game.CtrlRenderer controls;
	private Button startBtn;
	
	@Override
	public void setupState(ConcurrentHashMap<String, Object> args)
	{
		startBtn = new Button(150, 100, 100, 100);
//		String[] cL = new String[1];
//		cL[0] = "Click to start.";
//		controls = new game.CtrlRenderer(cL);
		// Display the FPS on the highest layer
		int layer =
				(int) core.DeveloperSettings.getSetting("NUM_MAIN_LAYERS")
				- 1
				;
		fpsRenderer.showOnLayer(layer);
		startBtn.showOnLayer(9);
		startBtn.setText("Click to Start");
	}

	@Override
	public void cycleState()
	{
		if (InputManager.getMS().justClicked(BUTTON1))
		{
			this.changeState(SamplePlay.class);
		}
	}

	@Override
	public void cleanupState()
	{
//		controls.destroy();
		startBtn.destroy();
	}
}
