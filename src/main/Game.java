package main;

import main.graphics.GfxManager;
import main.input.InputManager;
import main.input.Mouse;
import main.sound.SoundManager;
import main.sound.SoundManager.VolumeSetting;

import static java.awt.event.KeyEvent.*;
import static java.awt.event.MouseEvent.*;

import static main.input.InputManager.State;
import static main.sound.SoundManager.BGMTransition;

/** Represents a game session. Handles game cycling. */
public class Game
{
	/** The graphics renderer. */
	private GfxManager gfx;
	/** The manager of all input devices. */
	private InputManager input;
	/** Sound system manager. */
	private SoundManager sound;
	/** Thread manager for this object. */
	private ThreadClock clock;
	
	/** Sets up a standard game session. */
	public Game()
	{
		// Graphics manager
		gfx = new GfxManager();
		// Input device manager
		input = new InputManager(GfxManager.getMainWin());
		sound = new SoundManager();
		clock = new ThreadClock(10);
	}
	
	/** Final setup and start playing. */
	public void start()
	{
		input.start();
		gfx.start();
		sound.start();
		play();
	}

	/** Play the game. */
	private void play()
	{
		while(true)
		{
			// Normal operations
			if (InputManager.getState() == State.NORMAL)
			{
				InputManager.poll();
				// Testing inputs and sound system
				if (InputManager.getKB().isDown(VK_SPACE))
				{
					SoundManager.playSFX("testSFX");
				}
				if (InputManager.getKB().justPressed(VK_SHIFT))
				{
					SoundManager.playBGM("testBGM", BGMTransition.IMMEDIATE);
				}
				if (InputManager.getMS().justClicked(Mouse.BTN_LEFT))
				{
					SoundManager.changeVolume(VolumeSetting.MASTER, 50);
				}
			}
			// Quit
			else if (InputManager.getState() == State.QUIT)
			{
				break;
			}
			clock.nextCycle();
		}
	}
}
