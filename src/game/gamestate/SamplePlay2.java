package game.gamestate;

import static java.awt.event.KeyEvent.*;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import core.DynamicSettings;
import core.entity.Entity;
import core.entity.EntityContainer;
import core.entity.EntityUpdateEvent;
import core.gamestate.GameState;
import core.graphics.GfxManager;
import core.graphics.LayerSet;
import core.graphics.TextDrawer;
import core.sound.SoundManager;
import core.userinput.InputManager;
import core.userinput.inputdevice.gui.Button;
import game.CtrlRenderer;
import game.Enemy;
import game.SimpleMap;
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
	private EntityContainer entities;
	private game.BasicTextDrawer btd;
	private String[] toDraw;
	private game.PlayerInteractor pi;
	private game.Repulsor rep1;
	private SimpleMap map;

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
		btd = new game.BasicTextDrawer();
		pi = new game.PlayerInteractor();
		rep1 = new game.Repulsor();
		rep1.getBody().setPos(200, 200);
		entities = new EntityContainer();
	}

	@Override
	public void setupState(ConcurrentHashMap<String, Object> args)
	{
		DynamicSettings.setSetting("INVERT_SCROLL_WHEEL", true);
		GfxManager.getMainLayerSet().addRenderer(fpsRenderer, GfxManager.TOP_LAYER_INDEX);
		GfxManager.getMainLayerSet().addRenderer(controls, GfxManager.TOP_LAYER_INDEX);
		GfxManager.getMainLayerSet().addRenderer(entityLayers, 4);
//		SoundManager.playBGM("bgm/Into_the_Unknown.wav", SoundManager.BGMTransition.IMMEDIATE);
		entities.addEntity(new Enemy(50.0,50.0,0.0,0.0,100));
		entities.addEntity(new Enemy(50.0,0,0.0,0.0,100));
		entities.addEntity(pi);
		entities.addEntity(rep1);
		GfxManager.getMainLayerSet().addRenderer(btd, GfxManager.TOP_LAYER_INDEX);
		String[] td = {"Count", "Average Health", "Curr Num Dying", "AVG CPS"};
		toDraw = td;
		btd.setText(toDraw);
		map = new SimpleMap(100,100);
		entityLayers.addRenderer(entities, 0);
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
			enemies.removeIf((Enemy e)->{if (e.finishedDying()) {e.destroy(); return true;} return false;});
			entities.removeIf(
					(Entity e)->{
						if (e instanceof Enemy && ((Enemy) e).finishedDying())
						{
							e.destroy();
							return true;
						}
						return false;
					}
					);
			EntityUpdateEvent eue = new EntityUpdateEvent();
			eue.setEntities(entities);
			entities.updateEntities(eue);
		}
		double count = entities.numEntities();
		double sumHealth = 0;
		for (Enemy e : enemies)
		{
			sumHealth += e.getHealth();
		}
		toDraw[0] = "Count (Approx.): " + Integer.toString((int) count);
		toDraw[1] = "Average Health: " + String.format("%.2f", sumHealth/count);
		toDraw[3] = "Average CPS: " + String.format("%.3f", core.GameSession.getAverageCPS());
		btd.setText(toDraw);
	}

	@Override
	public void cleanupState()
	{
		DynamicSettings.setSetting("INVERT_SCROLL_WHEEL", false);
		SoundManager.stopBGM(SoundManager.BGMTransition.IMMEDIATE);
		controls.destroy();
		mainMenuBtn.destroy();
		entityLayers.destroy();
		entities.cleanupAll();
		btd.destroy();
	}
}
