package game.gamestate;

import java.awt.Font;
import java.util.concurrent.ConcurrentHashMap;

import core.gamestate.GameState;
import core.graphics.GfxManager;
import core.userinput.InputManager;
import core.userinput.inputdevice.gui.*;
import game.GUIPanelTester;
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
	private GUIPanel buttonPanel;
	private GUIPanelTester gpt;
	
	public MainMenu()
	{
		fpsRenderer = new FPSRenderer();
		Font buttonFont = new Font("Serif", Font.ITALIC, 16);
		startTestBtn = new Button(150, 100, 105, 30, "Old Test Mode", buttonFont);
		startSampleBtn = new Button(150, 150, 105, 50, "New Play Mode", buttonFont);
		quitBtn = new Button(150, 200, 105, 30);
		// Alternate way to set text & font
		quitBtn.setText("Quit");
		quitBtn.setFont(buttonFont);
		buttonPanel = new GUIPanel(150, 100, 105, 200);
		buttonPanel.addGUIObject(startTestBtn, "playMode1Btn");
		buttonPanel.addGUIObject(startSampleBtn, "playMode2Btn", GUIPanel.RelativePosition.RIGHT_OF, "playMode1Btn");
		buttonPanel.addGUIObject(quitBtn, "quitBtn", GUIPanel.RelativePosition.BELOW, "playMode1Btn");
		buttonPanel.addGUIObject(
				new Button(0, 0, 80, 30, "Test Button"),
				"testBtn",
				GUIPanel.RelativePosition.RIGHT_OF,
				"quitBtn"
				);
		buttonPanel.addGUIObject(
				new Button(0, 0, 95, 30, "Click to Remove"),
				"testBtn2",
				GUIPanel.RelativePosition.LEFT_OF,
				"quitBtn"
				);
		buttonPanel.addGUIObject(
				new Button(0, 0, 80, 30, "Test Button 3"),
				"testBtn3",
				GUIPanel.RelativePosition.ABOVE,
				startTestBtn
				);
//		buttonPanel.addGUIObj(
//				new Placeholder(0,0,100,25),
//				"testPH",
//				GUIPanel.RelativePosition.BELOW,
//				startTestBtn
//				);
		gpt= new GUIPanelTester(buttonPanel);
	}
	
	@Override
	public void setupState(ConcurrentHashMap<String, Object> args)
	{
		// Testing frame animator image dimensioning setup
//		GfxManager.getMainLayerSet().addRenderer(new game.FrameAnimatorUT1(), 0);
		GfxManager.getMainLayerSet().addRenderer(fpsRenderer, GfxManager.TOP_LAYER_INDEX);
		GfxManager.getMainLayerSet().addRenderer(buttonPanel, 9);
		GfxManager.getMainLayerSet().addRenderer(gpt, 9);
	}

	@Override
	public void cycleState()
	{
		if (((Button) buttonPanel.getGUIObject("playMode1Btn")).justReleased())
		{
			changeState(SamplePlay.class);
			return;
		}
		else if (startSampleBtn.justReleased())
		{
			changeState(SamplePlay2.class);
			return;
		}
		else if (quitBtn.justReleased())
		{
			InputManager.quit();
			return;
		}
		if (buttonPanel.containsGUIObject("testBtn2"))
		{
			Button testBtn2 = (Button) buttonPanel.getGUIObject("testBtn2");
			if (testBtn2.justReleased())
			{
				buttonPanel.removeGUIObject("testBtn2");
			}
		}
		gpt.update();
	}

	@Override
	public void cleanupState()
	{
		fpsRenderer.destroy();
		buttonPanel.destroy();
		gpt.destroy();
	}
}
