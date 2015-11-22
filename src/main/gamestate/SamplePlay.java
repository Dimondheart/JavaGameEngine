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
import main.graphics.GfxManager;
import main.graphics.TestAnimator;

/** A sample game state with sample stuff. */
public class SamplePlay extends GameState
{
	// Sample dynamic renderers
	SampleRenderer sr;
	SampleRenderer sr_2;
	// Sample static renderers
	SampleRenderer2 sr2;
	SampleRenderer2 sr2_2;
	// Sample player controlled and renderer
	SamplePlayerControlled spc;
	CtrlRenderer controls;
	// Animation testing
	TestAnimator ta;
	
	public SamplePlay()
	{
		super(GameStates.SAMPLE_PLAY);
		sr = new SampleRenderer();
		sr_2 = new SampleRenderer(40,200,1,-1,Color.green);
		sr2 = new SampleRenderer2();
		sr2_2 = new SampleRenderer2(0,0,80,270,Color.cyan,3);
		spc = new SamplePlayerControlled();
		ta = new TestAnimator(9,"test");
	}
	
	@Override
	public void setup()
	{
		String[] cL = new String[3];
		cL[0] = "WASD to move";
		cL[1] = "click to return to main menu";
		cL[2] = "space to reset BGM track";
		controls = new CtrlRenderer(cL);
	}

	@Override
	public void cycle()
	{
		if (InputManager.getMS().justClicked(BUTTON1))
		{
			this.changeState(GameStates.MAIN_MENU);
		}
		if (InputManager.getKB().justPressed(VK_SPACE))
		{
			SoundManager.playBGM("Into_the_Unknown", SoundManager.BGMTransition.IMMEDIATE);
		}
		sr.update();
		sr_2.update();
		spc.update();
	}

	@Override
	public void cleanup()
	{
		GfxManager.clearAll();
	}
}
