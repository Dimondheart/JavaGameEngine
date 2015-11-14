package main.gamestate;

import main.input.InputManager;
import main.gamestate.GameStateManager.GameStates;

import static java.awt.event.KeyEvent.*;
import static java.awt.event.MouseEvent.*;

/** The main menu and default game state. */
public class MainMenu extends GameState
{
	private CtrlRenderer controls;
	
	public MainMenu()
	{
		super(GameStates.MAIN_MENU);
	}
	
	@Override
	public void setup()
	{
		controls = new CtrlRenderer(
				"Click to start."
				);
	}

	@Override
	public void cycle()
	{
		if (InputManager.getMS().justClicked(BUTTON1))
		{
			this.changeState(GameStates.SAMPLE_PLAY);
		}
	}

	@Override
	public void cleanup()
	{
		controls.destroy();
	}
}
