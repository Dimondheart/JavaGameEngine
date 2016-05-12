/** Copyright 2016 Bryan Charles Bettis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.digitalcookies.objective;

import java.util.HashMap;

import xyz.digitalcookies.objective.gamestate.GameState;
import xyz.digitalcookies.objective.gamestate.GameStateManager;
import xyz.digitalcookies.objective.graphics.GraphicsManager;
import xyz.digitalcookies.objective.input.InputManager;
import xyz.digitalcookies.objective.resources.ResourceManager;
import xyz.digitalcookies.objective.sound.SoundManager;

/** High-level manager for all the systems in this game engine.
 * @author Bryan Charles Bettis
 */
public abstract class Game
{
	/** The title of the main game window. */
	public static final String MAIN_WIN_TITLE = "WIN_TITLE";
	/** How many layers to setup in the main layer set.
	 * <br>
	 * <br> <i>Type:</i> integer greater than 0
	 */
	public static final String NUM_LAYERS = "NUM_LAYERS";
	/** The initial width of the game window.
	 * <br>
	 * <br> <i>Type:</i> integer greater than 0
	 */
	public static final String INIT_WIN_WIDTH = "INIT_WIDTH";
	/** The initial height of the game window.
	 * <br>
	 * <br> <i>Type:</i> greater than 0
	 */
	public static final String INIT_WIN_HEIGHT = "INIT_HEIGHT";
	/** The default font to use for text drawing when a font is not specified.
	 * <br>
	 * <br> <i>Type:</i> a String corresponding to a supported font
	 */
	public static final String DEF_FONT = "DEF_FONT";
	/** The size of the default font. */
	public static final String DEF_FONT_SIZE = "DEF_FONT_SIZE";
	/** The root directory for sound resources. */
	public static final String SOUND_RES_DIR = "SOUND_RES";
	/** The root directory for graphics resources. */
	public static final String GRAPHICS_RES_DIR = "GRAPHICS_RES";
	/** If buffer-able resources should be buffered on startup. */
	public static final String INIT_BUFFER_RES = "INIT_BUFFER";
	/** The default resource pack. */
	public static final String DEF_RES_PACK = "DEF_PACK";
	/** The relative root directory of the resource pack directory. */
	public static final String RES_PACK_DIR = "PACK_DIR";
	/** The time the game was started, in nanoseconds. */
	private static long started = -1;
	
	/** The graphics system. */
	private Subsystem gfx;
	/** The system that manages all user input. */
	private Subsystem input;
	/** The sound system. */
	private Subsystem sound;
	/** Manages the state of the game. */
	private Subsystem gsm;
	/** The game state to initialize first. */
	private Class<? extends GameState> initGameState;
	/** The config data for the engine's subsystems. */
	private HashMap<String, Object> config;
	
	/** Constructs a game object with an initial game state.
	 * @param initGameState the class object for the game state to initialize
	 */
	public Game(Class<? extends GameState> initGameState)
	{
		config = new HashMap<String, Object>();
		// The name of the main game window
		config.put(
				MAIN_WIN_TITLE,
				"Built with Objective"
				);
		// The number of layers in the main layer set
		config.put(
				NUM_LAYERS,
				10
				);
		// The initial width of the main window
		config.put(
				INIT_WIN_WIDTH,
				960
				);
		// The initial height of the main window
		config.put(
				INIT_WIN_HEIGHT,
				540
				);
		config.put(
				DEF_FONT,
				"Dialog"
				);
		config.put(
				DEF_FONT_SIZE,
				12
				);
		config.put(
				SOUND_RES_DIR,
				"sounds"
				);
		config.put(
				GRAPHICS_RES_DIR,
				"graphics"
				);
		config.put(
				INIT_BUFFER_RES,
				true
				);
		config.put(
				DEF_RES_PACK,
				"Default"
				);
		config.put(
				RES_PACK_DIR,
				"resources"
				);
		this.initGameState = initGameState;
	}
	
	/** Get the time since the game was started, in seconds. */
	public static double getTimeSec()
	{
		return getTimeMilli()/1000.0;
	}

	/** Get the time since the game was started, in milliseconds. */
	public static double getTimeMilli()
	{
		return getTimeNano()/1000000.0;
	}
	
	/** Get the time since the game was started, in nanoseconds (not in
	 * double precision like getTimeMilli() and getTimeSec().) */
	public static long getTimeNano()
	{
		if (started < 0)
		{
			return 0;
		}
		else
		{
			return System.nanoTime() - started;
		}
	}
	
	/** Initialize a game.  Initialization operations include setting up
	 * DevConfig, the resource management system, and all the engine's
	 * subsystems.
	 */
	public void init()
	{
		ResourceManager.setup(config);
		// Create subsystem managers
		gfx = new GraphicsManager();
		input = new InputManager();
		sound = new SoundManager();
		gsm = new GameStateManager(initGameState);
		// Setup subsystems
		gfx.setup(config);
		input.setup(config);
		sound.setup(config);
		gsm.setup(config);
	}
	
	/** Starts all engine subsystems. */
	public void start()
	{
		input.start();
		gfx.start();
		sound.start();
		gsm.start();
		started = System.nanoTime();
		InputManager.resume();
	}
	
	/** Change an initial configuration setting of the engine.
	 * @param key the ID of the config value
	 * @param value the value to set the specified key to
	 */
	protected void setConfig(String key, Object value)
	{
		config.put(key, value);
	}
}
