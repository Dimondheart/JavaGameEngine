package game.gamestate;

import static java.awt.event.MouseEvent.*;
import static java.awt.event.KeyEvent.*;

import java.awt.Color;

import core.gamestate.SavableGameState;
import core.gamestate.GameStateManager.GameStates;
import core.sound.SoundManager;
import core.userinput.InputManager;
import game.SamplePlayerControlled;
import game.SampleRenderer;
import game.SampleRenderer2;
import game.TestAnimator;

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
	}
	
	@Override
	public void setup()
	{
		String[] cL = new String[3];
		cL[0] = "WASD to move";
		cL[1] = "Escape to return to main menu";
		cL[2] = "Hold space to move player character in front of everything";
		controls = new CtrlRenderer(cL);
		ta = new TestAnimator(9,"test");
		SoundManager.playBGM("Into_the_Unknown", SoundManager.BGMTransition.IMMEDIATE);
	}

	@Override
	public void cycle()
	{
		if (InputManager.getKB().justPressed(VK_ESCAPE))
		{
			changeState(GameStates.MAIN_MENU);
			return;
		}
		if (InputManager.getKB().justPressed(VK_ENTER))
		{
			// TODO save/serialize this object here
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
	}
}
