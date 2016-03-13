package game.gamestate;

//import static java.awt.event.MouseEvent.*;
import static java.awt.event.KeyEvent.*;

import java.util.concurrent.ConcurrentHashMap;

import core.DynamicSettings;
import core.entity.EntityContainer;
import core.entity.EntityUpdateEvent;
import core.gamestate.GameState;
import core.graphics.GfxManager;
import core.sound.SoundManager;
import core.userinput.InputManager;
import core.userinput.inputdevice.gui.Button;
import game.AstroBody;
import game.AstroScene;
import game.Planet;

/** A sample game state with sample stuff.
 * @author Bryan Charles Bettis
 */
@SuppressWarnings("javadoc")
public class SamplePlay extends GameState
{
	protected EntityContainer astroObjects;
	protected AstroScene realisticScene;
	protected AstroScene testScene;
	
	public SamplePlay()
	{
		astroObjects = new EntityContainer();
		realisticScene = new AstroScene();
		testScene = new AstroScene();
	}
	
	@Override
	public void setupState(ConcurrentHashMap<String, Object> args)
	{
		DynamicSettings.setSetting(DynamicSettings.INVERT_SCROLL_WHEEL, true);
		realisticScene.setup(0);
		testScene.setup(1);
		GfxManager.getMainLayerSet().addRenderer(realisticScene, 2);
		GfxManager.getMainLayerSet().addRenderer(testScene, 2);
		realisticScene.setActive(false);
		testScene.setActive(true);
//		AstroBody b1 = new AstroBody();
//		b1.setMass(5.972e24);
//		b1.setRadius(6371.3929);
//		b1.setPos(0, 0);
//		astroObjects.addEntity(new Planet(b1, realisticScene));
//		AstroBody b2 = new AstroBody();
//		b2.setMass(7.34767309e22);
//		b2.setRadius(1736.4822);
//		b2.setPos(384472.282, 0);
//		b2.setVector(0, 1.022);
//		b2.setMass(7.34767309e22);
//		b2.setRadius(1736.4822);
//		b2.setPos(9000, 0);
//		b2.setVector(0, 10);
//		astroObjects.addEntity(new Planet(b2, realisticScene));
//		GfxManager.getMainLayerSet().addRenderer(astroObjects, 1);
//		GfxManager.getMainLayerSet().addRenderer(realisticScene, 2);
	}

	@Override
	public void cycleState()
	{
//		EntityUpdateEvent event = new EntityUpdateEvent();
		int cdx = 0;
		int cdy = 0;
		double zoomFactor = 1;
		if (InputManager.getKB().isDown(VK_A))
		{
			cdx += 2;
		}
		if (InputManager.getKB().isDown(VK_D))
		{
			cdx += -2;
		}
		if (InputManager.getKB().isDown(VK_W))
		{
			cdy += 2;
		}
		if (InputManager.getKB().isDown(VK_S))
		{
			cdy += -2;
		}
		if (InputManager.getKB().isDown(VK_SHIFT))
		{
			cdx *= 2;
			cdy *= 2;
			zoomFactor *= 3;
		}
		if (realisticScene.isActive())
		{
			if (InputManager.getMS().getWheelChange() > 0)
			{
				realisticScene.zoomIn(zoomFactor);
			}
			else if (InputManager.getMS().getWheelChange() < 0)
			{
				realisticScene.zoomOut(zoomFactor);
			}
			realisticScene.updateOffset(cdx,cdy);
			realisticScene.updateScene(null);
		}
		if (testScene.isActive())
		{
			if (InputManager.getMS().getWheelChange() > 0)
			{
				testScene.zoomIn(zoomFactor);
			}
			else if (InputManager.getMS().getWheelChange() < 0)
			{
				testScene.zoomOut(zoomFactor);
			}
			testScene.updateOffset(cdx,cdy);
			testScene.updateScene(null);
		}
	}

	@Override
	public void cleanupState()
	{
	}
}
