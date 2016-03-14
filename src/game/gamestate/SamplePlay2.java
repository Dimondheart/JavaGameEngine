package game.gamestate;

import static java.awt.event.KeyEvent.*;

import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;

import game.CtrlRenderer;
import game.Enemy;
import game.Repulsor;
import xyz.digitalcookies.objective.DevConfig;
import xyz.digitalcookies.objective.Settings;
import xyz.digitalcookies.objective.entity.Entity;
import xyz.digitalcookies.objective.entity.EntityContainer;
import xyz.digitalcookies.objective.entity.EntityUpdateEvent;
import xyz.digitalcookies.objective.gamestate.GameState;
import xyz.digitalcookies.objective.graphics.GraphicsManager;
import xyz.digitalcookies.objective.graphics.LayerSet;
import xyz.digitalcookies.objective.graphics.TextDrawer;
import xyz.digitalcookies.objective.input.InputManager;
import xyz.digitalcookies.objective.input.gui.Button;
import xyz.digitalcookies.objective.sound.SoundManager;

@SuppressWarnings("javadoc")
public class SamplePlay2 extends GameState
{
	private transient CtrlRenderer controls;
	private Button mainMenuBtn;
	public static LayerSet entityLayers;
	private boolean paused = false;
	private EntityContainer entities;
	private game.BasicTextDrawer btd;
	private String[] toDraw;
	private game.PlayerInteractor pi;
	private game.Repulsor rep1;
	private game.LayerSetTest layerSetTest;

	public SamplePlay2()
	{
		String[] cL =
			{
				"ESCAPE to pause/resume"
			};
		controls = new CtrlRenderer(cL);
		layerSetTest = new game.LayerSetTest();
		entityLayers = new LayerSet(1);
		mainMenuBtn = new Button(150,10,100,20,"To Main Menu");
		mainMenuBtn.setFont(TextDrawer.defFont.deriveFont(16f));
		mainMenuBtn.setBGColor(Color.cyan);
		mainMenuBtn.setFontColor(Color.black);
		btd = new game.BasicTextDrawer();
		pi = new game.PlayerInteractor();
		rep1 = new game.Repulsor();
		rep1.getBody().setPos(200, 200);
		entities = new EntityContainer();
	}

	@Override
	public void setupState(ConcurrentHashMap<String, Object> args)
	{
		Settings.setSetting("INVERT_SCROLL_WHEEL", true);
		GraphicsManager.getMainLayerSet().addRenderer(controls, (int)
				DevConfig.getSetting("NUM_MAIN_LAYERS") - 1);
		GraphicsManager.getMainLayerSet().addRenderer(entityLayers, 4);
//		SoundManager.playBGM("bgm/Into_the_Unknown.wav", SoundManager.BGMTransition.IMMEDIATE);
//		entities.addEntity(new Enemy(50.0,50.0,0.0,0.0,50));
//		entities.addEntity(new Enemy(50.0,0,0.0,0.0,50));
		entities.addEntity(pi);
//		entities.addEntity(rep1);
		GraphicsManager.getMainLayerSet().addRenderer(btd, (int)
				DevConfig.getSetting("NUM_MAIN_LAYERS") - 1);
		String[] td = {"Count", "Average Health", "Curr Num Dying", "AVG CPS"};
		toDraw = td;
		btd.setText(toDraw);
		entityLayers.addRenderer(entities, 0);
	}

	@Override
	public void cycleState()
	{
		System.out.println(getClock().getTimeMS());
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
			GraphicsManager.getMainLayerSet().addRenderer(mainMenuBtn, 9);
			if (mainMenuBtn.getState().equals(Button.ButtonState.CLICKED))
			{
				changeState(MainMenu.class);
				return;
			}
		}
		else
		{
			GraphicsManager.getMainLayerSet().removeRenderer(mainMenuBtn, 9);
			entities.setCycleRemoveIf(
					(Entity e)->{
						if (e instanceof Enemy && ((Enemy) e).finishedDying())
						{
							e.destroy();
							return true;
						}
						else if (e instanceof Repulsor && ((Repulsor) e).getHealth() <= 0)
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
		toDraw[0] = "Count (Approx.): " + Integer.toString((int) count);
		toDraw[1] = "---";
		btd.setText(toDraw);
	}

	@Override
	public void cleanupState()
	{
		Settings.setSetting("INVERT_SCROLL_WHEEL", false);
		SoundManager.stopBGM(SoundManager.BGMTransition.IMMEDIATE);
		controls.destroy();
		mainMenuBtn.destroy();
		entityLayers.destroy();
		entities.cleanupAll();
		btd.destroy();
		layerSetTest.cleanup();
	}
}
