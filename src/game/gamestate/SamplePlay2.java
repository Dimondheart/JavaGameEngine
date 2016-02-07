package game.gamestate;

import static java.awt.event.KeyEvent.*;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import core.DynamicSettings;
import core.gamestate.GameState;
import core.graphics.GfxManager;
import core.graphics.LayerSet;
import core.graphics.TextDrawer;
import core.sound.SoundManager;
import core.userinput.InputManager;
import core.userinput.inputdevice.gui.Button;
import game.CtrlRenderer;
import game.Enemy;
import utility.FPSRenderer;

@SuppressWarnings("javadoc")
public class SamplePlay2 extends GameState
{
	private FPSRenderer fpsRenderer;
	private transient CtrlRenderer controls;
	private Button mainMenuBtn;
	public static LayerSet entityLayers;
	private boolean paused = false;
	private LinkedList<Enemy> enemies;

	public SamplePlay2()
	{
		fpsRenderer = new FPSRenderer();
		String[] cL =
			{
				"ESCAPE to pause/resume"
			};
		controls = new CtrlRenderer(cL);
		enemies = new LinkedList<Enemy>();
		entityLayers = new LayerSet(2);
		mainMenuBtn = new Button(150,10,100,20,"To Main Menu");
		mainMenuBtn.setFont(TextDrawer.defFont.deriveFont(16f));
	}

	@Override
	public void setupState(ConcurrentHashMap<String, Object> args)
	{
		DynamicSettings.setSetting("INVERT_SCROLL_WHEEL", true);
		GfxManager.getMainLayerSet().addRenderer(fpsRenderer, GfxManager.TOP_LAYER_INDEX);
		GfxManager.getMainLayerSet().addRenderer(controls, GfxManager.TOP_LAYER_INDEX);
		GfxManager.getMainLayerSet().addRenderer(entityLayers, 4);
		SoundManager.playBGM("bgm/Into_the_Unknown.wav", SoundManager.BGMTransition.IMMEDIATE);
		addEnemy(new Enemy(50.0,50.0,0.2,1.2,100));
		addEnemy(new Enemy(50.0,50.0,-1.5,3.0,100));
	}

	@Override
	public void cycleState()
	{
		if (!InputManager.getWin().isActive())
		{
			paused = true;
		}
		else if (InputManager.getKB().justPressed(VK_ESCAPE))
		{
			if (paused)
			{
				paused = false;
				getClock().resume();
			}
			else
			{
				paused = true;
				getClock().pause();
			}
		}
		if (paused)
		{
			GfxManager.getMainLayerSet().addRenderer(mainMenuBtn, 9);
			if (mainMenuBtn.getState().equals(Button.ButtonState.CLICKED))
			{
				changeState(MainMenu.class);
				return;
			}
		}
		else
		{
			GfxManager.getMainLayerSet().removeRenderer(mainMenuBtn, 9);
			for (Enemy e : enemies)
			{
				e.update();
			}
			int numEnemies = enemies.size();
			for (int i = 0; i < numEnemies; ++i)
			{
				Enemy e = enemies.get(i);
				if (e.shouldSplit())
				{
					Enemy newEnemy = e.split();
					addEnemy(newEnemy);
				}
			}
		}
	}

	@Override
	public void cleanupState()
	{
		DynamicSettings.setSetting("INVERT_SCROLL_WHEEL", false);
		SoundManager.stopBGM(SoundManager.BGMTransition.IMMEDIATE);
		controls.destroy();
		mainMenuBtn.destroy();
		entityLayers.destroy();
		for (Enemy e : enemies)
		{
			e.destroy();
		}
	}
	
	private void addEnemy(Enemy e)
	{
		enemies.add(e);
		entityLayers.addRenderer(e, 0);
		System.out.println(enemies.size());
	}
}
