package main;

import main.graphics.GfxManager;
import main.input.InputManager;
import main.sound.SoundManager;
import main.sound.Volume;

import static java.awt.event.KeyEvent.*;
//import static java.awt.event.MouseEvent.*;

import static main.input.InputManager.State;

import java.awt.Color;

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
	@SuppressWarnings("unused")
	private void play()
	{
		// Sample renderer
		SampleRenderer sr = new SampleRenderer();
		SampleRenderer sr_2 = new SampleRenderer(40,200,1,-1,Color.green);
		SampleRenderer2 sr2 = new SampleRenderer2();
		SampleRenderer2 sr2_2 = new SampleRenderer2(0,0,80,270,Color.cyan,3);
		SamplePlayerControlled spc = new SamplePlayerControlled();
		while(true)
		{
			// Normal operations
			if (InputManager.getState() == State.NORMAL)
			{
				InputManager.poll();
				if (InputManager.getKB().isDown(VK_SHIFT))
				{
					SoundManager.changeVolume(Volume.Setting.MASTER, 0);
				}
				else
				{
					SoundManager.changeVolume(Volume.Setting.MASTER, 100);
				}
				if (InputManager.getKB().justPressed(VK_SPACE))
				{
					SoundManager.playBGM("Into_the_Unknown", SoundManager.BGMTransition.IMMEDIATE);
				}
				sr.update();
				sr_2.update();
				spc.update();
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
