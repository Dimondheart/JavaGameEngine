package main.gamestate;

import static java.awt.event.MouseEvent.BUTTON1;

import java.awt.Color;

import main.SamplePlayerControlled;
import main.SampleRenderer;
import main.SampleRenderer2;
import main.gamestate.GameStateManager.GameStates;
import main.input.InputManager;
import main.graphics.GfxManager;

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
		controls = new CtrlRenderer(
				"Press WASD to move, click to return to main menu."
				);
	}

	@Override
	public void cycle()
	{
		if (InputManager.getMS().justClicked(BUTTON1))
		{
			this.changeState(GameStates.MAIN_MENU);
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
