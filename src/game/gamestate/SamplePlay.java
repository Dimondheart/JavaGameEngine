package game.gamestate;

//import static java.awt.event.MouseEvent.*;
import static java.awt.event.KeyEvent.*;

import java.awt.Color;
import java.awt.event.MouseEvent;

import core.gamestate.SavableGameState;
import core.gamestate.GameStateManager.GameStates;
import core.sound.SoundManager;
import core.userinput.InputManager;
import game.CtrlRenderer;
import game.SamplePlayerControlled;
import game.SampleRenderer;
import game.SampleRenderer2;
import game.TestAnimator;
import core.userinput.gui.Button;

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
	
	public SamplePlay()
	{
		super(GameStates.SAMPLE_PLAY);
		sr = new SampleRenderer();
		sr_2 = new SampleRenderer(40,200,1,-1,Color.green);
		sr2 = new SampleRenderer2();
		sr2_2 = new SampleRenderer2(0,0,80,270,Color.cyan,3);
		spc = new SamplePlayerControlled();
		// Display the FPS on the highest layer
		fpsRenderer.showOnLayer(core.graphics.GfxManager.NUM_MAIN_LAYERS-1);
		mainMenuBtn = new Button(150,0,100,20,"Main Menu");
		mainMenuBtn.setText("Main Menu");
	}
	
	@Override
	public void setup()
	{
		String[] cL = new String[3];
		cL[0] = "WASD to move";
		cL[1] = "Escape + left click to return to main menu";
		cL[2] = "Hold space to move player character in front of everything";
		controls = new CtrlRenderer(cL);
		ta = new TestAnimator("testanimate", "basic");
		ta.showOnLayer(9);
		SoundManager.playBGM("Into_the_Unknown", SoundManager.BGMTransition.IMMEDIATE);
	}

	@Override
	public void cycle()
	{
		if (InputManager.getKB().isDown(VK_ESCAPE))
		{
			mainMenuBtn.showOnLayer(9);
			if (InputManager.getMS().isDown(MouseEvent.BUTTON1))
			{
				changeState(GameStates.MAIN_MENU);
				return;
			}
			if (mainMenuBtn.getState() == Button.ButtonState.CLICKED)
			{
				changeState(GameStates.MAIN_MENU);
				return;
			}
		}
		else
		{
			mainMenuBtn.hideOnLayer(9);
		}
		if (InputManager.getKB().justPressed(VK_ENTER))
		{
			changeState(GameStates.MAIN_MENU);
			return;
		}
		if (InputManager.getKB().isDown(VK_SPACE))
		{
			spc.showOnlyOnLayer(9);
		}
		else
		{
			spc.showOnlyOnLayer(4);
		}
		sr.update();
		sr_2.update();
		spc.update();
	}

	@Override
	public void cleanupState()
	{
		SoundManager.stopBGM(SoundManager.BGMTransition.IMMEDIATE);
		sr.destroy();
		sr_2.destroy();
		sr2.destroy();
		sr2_2.destroy();
		spc.destroy();
		controls.destroy();
		ta.destroy();
		mainMenuBtn.destroy();
	}
}
