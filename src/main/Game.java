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
