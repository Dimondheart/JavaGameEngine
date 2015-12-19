package main.gamestate;

import static java.awt.event.MouseEvent.*;
import static java.awt.event.KeyEvent.*;

import java.awt.Color;

import main.SamplePlayerControlled;
import main.SampleRenderer;
import main.SampleRenderer2;
import main.gamestate.GameStateManager.GameStates;
import main.input.InputManager;
import main.sound.SoundManager;
import main.graphics.TestAnimator;

/** A sample game state with sample stuff. */
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
			spc.showOnly(9);
		}
		else
		{
			spc.showOnly(4);
		}
		sr.update();
		sr_2.update();
		spc.update();
	}

	@Override
	public void cleanup()
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
