package game.gamestate;

import java.awt.Font;
import java.util.concurrent.ConcurrentHashMap;

import core.gamestate.GameState;
import core.graphics.GfxManager;
import core.userinput.InputManager;
import core.userinput.inputdevice.gui.Button;
import utility.FPSRenderer;

//import static java.awt.event.KeyEvent.*;
//import static java.awt.event.MouseEvent.*;

/** The main menu and default game state.
 * @author Bryan Charles Bettis
 */
@SuppressWarnings("javadoc")
public class MainMenu extends GameState
{
	private FPSRenderer fpsRenderer;
	private Button startTestBtn;
	private Button startSampleBtn;
	private Button quitBtn;
	
	public MainMenu()
	{
		fpsRenderer = new FPSRenderer();
		Font buttonFont = new Font("Serif", Font.ITALIC, 16);
		startTestBtn = new Button(150, 100, 105, 30, "Old Test Mode", buttonFont);
		startSampleBtn = new Button(150, 150, 105, 30, "New Play Mode", buttonFont);
		quitBtn = new Button(150, 200, 105, 30);
		// Alternate way to set text & font
		quitBtn.setText("Quit");
		quitBtn.setFont(buttonFont);
	}
	
	@Override
	public void setupState(ConcurrentHashMap<String, Object> args)
	{
		GfxManager.getMainLayerSet().addRenderer(fpsRenderer, GfxManager.TOP_LAYER_INDEX);
		GfxManager.getMainLayerSet().addRenderer(startTestBtn, 9);
		GfxManager.getMainLayerSet().addRenderer(startSampleBtn, 9);
		GfxManager.getMainLayerSet().addRenderer(quitBtn, 9);
	}

	@Override
	public void cycleState()
	{
		if (startTestBtn.getState().equals(Button.ButtonState.CLICKED))
		{
			changeState(SamplePlay.class);
			return;
		}
		else if (startSampleBtn.getState().equals(Button.ButtonState.CLICKED))
		{
			changeState(SamplePlay2.class);
			return;
		}
		else if (quitBtn.getState().equals(Button.ButtonState.CLICKED))
		{
			InputManager.quit();
			return;
		}
	}

	@Override
	public void cleanupState()
	{
		fpsRenderer.destroy();
		startTestBtn.destroy();
		startSampleBtn.destroy();
		quitBtn.destroy();
	}
}
