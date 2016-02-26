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
		rep1.setCoords(200, 200);
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
		addEnemy(new Enemy(50.0,50.0,0.2,1.2,100));
		addEnemy(new Enemy(50.0,0,-1.5,3.0,100));
		GfxManager.getMainLayerSet().addRenderer(btd, GfxManager.TOP_LAYER_INDEX);
		String[] td = {"Count", "Average Health", "Curr Num Dying"};
		toDraw = td;
		btd.setText(toDraw);
		GfxManager.getMainLayerSet().addRenderer(pi, 5);
		GfxManager.getMainLayerSet().addRenderer(rep1, 5);
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
			pi.update(eue);
			for (Enemy e : enemies)
			{
				e.update(new EntityUpdateEvent());
			}
//			if (pi.isRepulsing())
//			{
//				for (Enemy e : enemies)
//				{
//					if (pi.isWithinAOE((int) e.getBody().getX(), (int) e.getBody().getY()))
//					{
//						double dx = 0.1;
//						double dy = 0.1;
//						if (pi.getX() > e.getBody().getX())
//						{
//							dx *= -1.0;
//						}
//						if(pi.getY() > e.getBody().getY())
//						{
//							dy *= -1.0;
//						}
//						e.adjVector(dx, dy);
//					}
//				}
//			}
			if (rep1.isRepulsing())
			{
				for (Enemy e : enemies)
				{
					if (rep1.isWithinAOE((int) e.getBody().getX(), (int) e.getBody().getY()))
					{
						double dx = 0.1;
						double dy = 0.1;
						if (rep1.getX() > e.getBody().getX())
						{
							dx *= -1.0;
						}
						if(rep1.getY() > e.getBody().getY())
						{
							dy *= -1.0;
						}
						e.adjVector(dx, dy);
					}
				}
			}
			for (Enemy e : enemies)
			{
				if (e.isDead())
				{
					e.die();
					LinkedList<Enemy> nearEnemies = getNearEnemies(e, 32);
					for (Enemy ne : nearEnemies)
					{
						ne.doDamage(1);
					}
				}
				else
				{
					LinkedList<Enemy> nearEnemies = getNearEnemies(e, 8);
					for (Enemy ne : nearEnemies)
					{
						ne.doDamage(1);
					}
				}
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
		double count = enemies.size();
		double sumHealth = 0;
		for (Enemy e : enemies)
		{
			sumHealth += e.getHealth();
		}
		toDraw[0] = "Count: " + Integer.toString((int) count);
		toDraw[1] = "Average Health: " + String.format("%.2f", sumHealth/count);
		btd.setText(toDraw);
		rep1.nextCycle();
		pi.nextCycle();
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
		entities.destroy();
		btd.destroy();
		pi.destroy();
	}
	
	private void addEnemy(Enemy e)
	{
		enemies.add(e);
		entityLayers.addRenderer(e, 0);
	}
	
	private LinkedList<Enemy> getNearEnemies(Enemy e, int distance)
	{
		LinkedList<Enemy> nearby = new LinkedList<Enemy>();
		for (Enemy other : enemies)
		{
			if (other == e)
			{
				continue;
			}
			if (e.distTo(other) <= distance)
			{
				nearby.add(other);
			}
		}
		return nearby;
	}
}
