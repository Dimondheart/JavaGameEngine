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

import xyz.digitalcookies.objective.gamestate.GameState;
import xyz.digitalcookies.objective.gamestate.GameStateManager;
import xyz.digitalcookies.objective.graphics.GraphicsManager;
import xyz.digitalcookies.objective.input.InputManager;
import xyz.digitalcookies.objective.sound.SoundManager;

/** Starts up all subsystems for managing a game session and handles
 * the main game loop (calls the game state manager). Only one instance
 * of the GameSession class should be created and started at one time.
 * @author Bryan Charles Bettis
 */
public class GameSession
{
	/** The graphics system. */
	private static Subsystem gfx;
	/** The system that manages all user input. */
	private static Subsystem input;
	/** The sound system. */
	private static Subsystem sound;
	/** Manages the state of the game. */
	private static GameStateManager gsm;
	/** Manages timing of the main thread. */
	private static ThreadManager clock;
	/** The last engine threading setting, used to detect when it has
	 * been changed so it can be applied.
	 */
	private Settings.ThreadingSetting lastThreadingSetting;
	
	/** Sets up all game engine subsystems, and sets the first game state
	 * to be an instance of the specified game state class.
	 * @param initGameState the game state to setup and run when this
	 * 		game session has been started.
	 */
	public GameSession(Class<? extends GameState> initGameState)
	{
		// Create subsystem managers
		gfx = new GraphicsManager();
		input = new InputManager();
		sound = new SoundManager();
		gsm = new GameStateManager(initGameState);
		// Setup threading-related stuff
		clock = new ThreadManager(10);
		lastThreadingSetting = 
				(Settings.ThreadingSetting)
				Settings.getSetting("ENGINE_THREADING");
	}
	
	/** Gets the average number of cycles per second of the main loop.
	 * @return the average CPS of the main loop
	 */
	public double getAverageCPS()
	{
		return clock.getAvgCPS();
	}
	
	/** Start all subsystems then begin playing. */
	public void start()
	{
		// Setup subsystems
		gfx.setup();
		input.setup();
		sound.setup();
		gsm.setup();
		// Reset the program timer
		ProgramTime.reset();
		while (true)
		{
			// Quit the game
			if (InputManager.isQuitting())
			{
				gfx.stop();
				input.stop();
				sound.stop();
				break;
			}
			// Determine how many threads we should run
			int numThreads = 1;
			int numCores = Runtime.getRuntime().availableProcessors();
			Settings.ThreadingSetting threadSetting =
					(Settings.ThreadingSetting)
					Settings.getSetting("ENGINE_THREADING");
			// Determine number of threads
			switch (threadSetting)
			{
				// Run all subsystems in their own thread
				case FULL:
					numThreads = 4;
					break;
				// Optimize threading based on available resources
				case OPTIMIZE:
					if (numCores >= 4)
					{
						numThreads = 4;
					}
					else if (numCores > 0)
					{
						numThreads = numCores;
					}
					break;
				// Run all subsystems in the main loop
				case SINGLE:
					// Fall Through
				default:
					numThreads = 1;
					break;
			}
			// All subsystems are threaded
			if (numThreads >= 4)
			{
				// Start the subsystems
				input.startThreaded();
				gfx.startThreaded();
				sound.startThreaded();
			}
			// Don't thread the input system
			else if (numThreads == 3)
			{
				input.startUnthreaded();
				gfx.startThreaded();
				sound.startThreaded();
			}
			// Don't thread the input and sound systems
			else if (numThreads == 2)
			{
				input.startUnthreaded();
				gfx.startThreaded();
				sound.startUnthreaded();
			}
			// Don't thread any subsystems
			else
			{
				input.startUnthreaded();
				gfx.startUnthreaded();
				sound.startUnthreaded();
			}
			// Start the input manager
			InputManager.resume();
			// Go to the main loop
			play(numThreads);
		}
	}

	/** Starts playing the game.
	 * @param numThreads the number of threads the game has been setup to use
	 */
	private void play(int numThreads)
	{
		// Select how to run based on how subsystems are setup
		switch (numThreads)
		{
			case 4:
				play4Threads();
				break;
			case 3:
				play3Threads();
				break;
			case 2:
				play2Threads();
				break;
			case 1:
				// Fall Through
			default:
				play1Thread();
				break;
		}
		// Stop all threaded subsystems before returning
		input.stopThread();
		sound.stopThread();
		gfx.stopThread();
		lastThreadingSetting = (Settings.ThreadingSetting)
				Settings.getSetting("ENGINE_THREADING");
	}
	
	/** Play with 3 subsystems running in separate threads. */
	private void play4Threads()
	{
		while(true)
		{
			clock.nextCycle();
			if (!mainLoop())
			{
				break;
			}
		}
	}
	
	/** Play with 2 subsystems running in separate threads. */
	private void play3Threads()
	{
		while(true)
		{
			clock.nextCycle();
			input.run();
			if (!mainLoop())
			{
				break;
			}
		}
	}
	
	/** Play with 1 subsystem running in a separate thread. */
	private void play2Threads()
	{
		while(true)
		{
			clock.nextCycle();
			input.run();
			sound.run();
			if (!mainLoop())
			{
				break;
			}
		}
	}
	
	/** Play with all three subsystems running with the main loop. */
	private void play1Thread()
	{
		while(true)
		{
			clock.nextCycle();
			input.run();
			sound.run();
			gfx.run();
			if (!mainLoop())
			{
				break;
			}
		}
	}
	
	/** The main loop of the game.  Updates the game state manager.
	 * @return true if the main loop should keep running, false otherwise
	 */
	private boolean mainLoop()
	{
		if (!lastThreadingSetting.equals(Settings.getSetting("ENGINE_THREADING")))
		{
			return false;
		}
		// Normal cycle; game is not paused, quitting, etc.
		else if (InputManager.isRunning())
		{
			// Update input devices
			InputManager.poll();
			// Do one update cycle of the current game state
			gsm.cycle();
		}
		// Input manager indicated that the program should quit
		else if (InputManager.isQuitting())
		{
			// Clean up the current game state before quitting
			gsm.cleanup();
			return false;
		}
		return true;
	}
}
