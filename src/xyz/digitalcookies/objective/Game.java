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

import java.net.URL;

import xyz.digitalcookies.objective.gamestate.GameState;
import xyz.digitalcookies.objective.gamestate.GameStateManager;
import xyz.digitalcookies.objective.graphics.GraphicsManager;
import xyz.digitalcookies.objective.input.InputManager;
import xyz.digitalcookies.objective.resources.ResourcePackManager;
import xyz.digitalcookies.objective.sound.SoundManager;

/** High-level manager for all the systems in this game engine.
 * @author Bryan Charles Bettis
 */
public abstract class Game
{
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
	
	/** Constructs a game object with an initial game state.
	 * @param source the source location of the project using this game engine;
	 * 		this should be the value returned by calling
	 * 		"[class].class.getProtectionDomain().getCodeSource().getLocation()"
	 * 		where [class] is a class in your project
	 * @param initGameState the class object for the game state to initialize
	 */
	public Game(URL source, Class<? extends GameState> initGameState)
	{
		EngineSetupData.setCodeSource(source);
		this.initGameState = initGameState;
	}
	
	/** Initialize a game.  Initialization operations include setting up
	 * DevConfig, the resource management system, and all the engine's
	 * subsystems.
	 */
	public void init()
	{
		DevConfig.setup();
		ResourcePackManager.setup();
		// Create subsystem managers
		gfx = new GraphicsManager();
		input = new InputManager();
		sound = new SoundManager();
		gsm = new GameStateManager(initGameState);
		// Setup subsystems
		gfx.setup();
		input.setup();
		sound.setup();
		gsm.setup();
	}
	
	/** Starts all engine subsystems. */
	public void start()
	{
		// Reset the program timer
		GameTime.reset();
		input.start();
		gfx.start();
		sound.start();
		gsm.start();
		// Start the input manager
		InputManager.resume();
	}
}
