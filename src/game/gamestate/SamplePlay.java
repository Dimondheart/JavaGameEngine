package game.gamestate;

//import static java.awt.event.MouseEvent.*;
import static java.awt.event.KeyEvent.*;

import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;

import core.DynamicSettings;
import core.gamestate.GameState;
import core.graphics.GfxManager;
import core.graphics.LayerSet;
import core.graphics.TextDrawer;
import core.sound.SoundManager;
import core.userinput.InputManager;
import core.userinput.inputdevice.gui.Button;
import game.*;
import utility.FPSRenderer;

/** A sample game state with sample stuff.
 * @author Bryan Charles Bettis
 */
@SuppressWarnings("javadoc")
public class SamplePlay extends GameState
{
	private FPSRenderer fpsRenderer;
	// Sample dynamic renderers
	private SampleRenderer sr;
	private SampleRenderer sr_2;
	// Sample static renderers
	private SampleRenderer2 sr2;
	private SampleRenderer2 sr2_2;
	// Sample player controlled and renderer
	private SamplePlayerControlled spc;
	private transient CtrlRenderer controls;
	// Animation testing
	private transient TestAnimator ta;
	private Button mainMenuBtn;
	private LayerSetTester ls;
	public static LayerSet entityLayers;
	private boolean paused = false;
	private MouseMotionTester ct;
	
	public SamplePlay()
	{
		fpsRenderer = new FPSRenderer();
		sr = new SampleRenderer();
		sr_2 = new SampleRenderer(40,200,1,-1);
		sr2 = new SampleRenderer2();
		sr2_2 = new SampleRenderer2(150,120,80,80,Color.cyan);
		spc = new SamplePlayerControlled();
		entityLayers = new LayerSet(2);
		mainMenuBtn = new Button(150,0,100,20,"Main Menu");
		mainMenuBtn.setFont(TextDrawer.defFont.deriveFont(16f));
		ls = new LayerSetTester();
		ct = new MouseMotionTester();
	}
	
	@Override
	public void setupState(ConcurrentHashMap<String, Object> args)
	{
		DynamicSettings.setSetting("INVERT_SCROLL_WHEEL", true);
		entityLayers.addRenderer(ct, 0);
		entityLayers.addRenderer(spc, 1);
		entityLayers.addRenderer(sr, 0);
		entityLayers.addRenderer(sr_2, 0);
		GfxManager.getMainLayerSet().addRenderer(sr2, 5);
		GfxManager.getMainLayerSet().addRenderer(sr2_2, 3);
		GfxManager.getMainLayerSet().addRenderer(fpsRenderer, GfxManager.TOP_LAYER_INDEX);
		GfxManager.getMainLayerSet().addRenderer(spc, GfxManager.TOP_LAYER_INDEX);
		String[] cL = new String[4];
		cL[0] = "WASD to move";
		cL[1] = "Escape to pause/resume, + click main menu button to go to the main menu";
		cL[2] = "Scroll to change the player unit's speed";
		cL[3] = "Select the blue circle (left mouse button) & move it by moving the cursor";
		controls = new CtrlRenderer(cL);
		GfxManager.getMainLayerSet().addRenderer(controls, GfxManager.TOP_LAYER_INDEX);
		GfxManager.getMainLayerSet().addRenderer(ct, GfxManager.TOP_LAYER_INDEX);
		GfxManager.getMainLayerSet().addRenderer(entityLayers, 4);
		ta = new TestAnimator("testanimate", "basic");
		GfxManager.getMainLayerSet().addRenderer(ta, 9);
		SoundManager.playBGM("bgm/Into_the_Unknown.wav", SoundManager.BGMTransition.IMMEDIATE);
	}

	@Override
	public void cycleState()
	{
		ct.update();
		if (InputManager.getKB().isDown(VK_SPACE))
		{
			SoundManager.stopAllSFX();
		}
		if (!InputManager.getWin().isActive())
		{
			paused = true;
		}
		else if (InputManager.getKB().justPressed(VK_ESCAPE))
		{
			paused = !paused;
			if (paused)
			{
				getClock().pause();
			}
			else
			{
				getClock().resume();
			}
		}
		if (paused)
		{
			GfxManager.getMainLayerSet().addRenderer(mainMenuBtn, 9);
			if (mainMenuBtn.getState().equals(Button.ButtonState.CLICKED))
			{
				changeState(MainMenu.class);
				return;
			}
		}
		else
		{
			GfxManager.getMainLayerSet().removeRenderer(mainMenuBtn, 9);
			sr.update();
			sr_2.update();
			spc.update();
		}
	}

	@Override
	public void cleanupState()
	{
		DynamicSettings.setSetting("INVERT_SCROLL_WHEEL", false);
		SoundManager.stopBGM(SoundManager.BGMTransition.IMMEDIATE);
		sr2.destroy();
		sr2_2.destroy();
		spc.destroy();
		controls.destroy();
		ta.destroy();
		mainMenuBtn.destroy();
		entityLayers.destroy();
		ls.layers.destroy();
		ct.destroy();
	}
}
