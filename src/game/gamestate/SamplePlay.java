package game.gamestate;

//import static java.awt.event.MouseEvent.*;
import static java.awt.event.KeyEvent.*;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.concurrent.ConcurrentHashMap;

import core.gamestate.SavableGameState;
import core.graphics.GfxManager;
import core.graphics.LayerSet;
import core.graphics.gui.Button;
import core.sound.SoundManager;
import core.userinput.InputManager;
import game.*;

/** A sample game state with sample stuff.
 * @author Bryan Bettis
 */
public class SamplePlay extends SavableGameState
{
	private static final long serialVersionUID = 1L;
	
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
	private LayerSet entityLayers;
	
	public SamplePlay()
	{
		sr = new SampleRenderer();
		sr_2 = new SampleRenderer(40,200,1,-1);
		sr2 = new SampleRenderer2();
		sr2_2 = new SampleRenderer2(0,0,80,270,Color.cyan,3);
		spc = new SamplePlayerControlled();
		// Always show the player in front of other active entities
		entityLayers = new LayerSet(2);
		entityLayers.addRenderer(spc, 1);
		entityLayers.addRenderer(sr, 0);
		entityLayers.addRenderer(sr_2, 0);
		GfxManager.getMainLayerSet().addRenderer(entityLayers, 4);
		// Display the FPS on the highest layer
		fpsRenderer.showOnLayer(core.graphics.GfxManager.NUM_MAIN_LAYERS-1);
		mainMenuBtn = new Button(150,0,100,20,"Main Menu");
		mainMenuBtn.setText("Main Menu");
		ls = new LayerSetTester();
	}
	
	@Override
	public void setup(ConcurrentHashMap<String, Object> args)
	{
		String[] cL = new String[3];
		cL[0] = "WASD to move";
		cL[1] = "Escape + left click to return to main menu";
		cL[2] = "Hold space to move player character in front of everything";
		controls = new CtrlRenderer(cL);
		ta = new TestAnimator("testanimate", "basic");
		GfxManager.getMainLayerSet().addRenderer(ta, 9);
		SoundManager.playBGM("bgm/Into_the_Unknown.wav", SoundManager.BGMTransition.IMMEDIATE);
	}

	@Override
	public void cycle()
	{
		if (InputManager.getKB().isDown(VK_ESCAPE))
		{
			mainMenuBtn.showOnLayer(9);
			if (InputManager.getMS().isDown(MouseEvent.BUTTON1))
			{
				changeState(MainMenuTest.class);
				return;
			}
			if (mainMenuBtn.getState() == Button.ButtonState.CLICKED)
			{
				changeState(MainMenuTest.class);
				return;
			}
		}
		else
		{
			mainMenuBtn.hideOnLayer(9);
		}
		if (InputManager.getKB().justPressed(VK_ENTER))
		{
			changeState(MainMenuTest.class);
			return;
		}
		if (InputManager.getKB().isDown(VK_SPACE))
		{
			entityLayers.removeRenderer(spc, 1);
			GfxManager.getMainLayerSet().addRenderer(spc, 9);
		}
		else
		{
			GfxManager.getMainLayerSet().removeRenderer(spc, 9);
			entityLayers.addRenderer(spc, 1);
		}
		sr.update();
		sr_2.update();
		spc.update();
	}

	@Override
	public void cleanupState()
	{
		SoundManager.stopBGM(SoundManager.BGMTransition.IMMEDIATE);
		sr2.destroy();
		sr2_2.destroy();
		spc.destroy();
		controls.destroy();
		ta.destroy();
		mainMenuBtn.destroy();
		entityLayers.destroy();
		ls.layers.destroy();
	}
}
