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

package core.gamestate;

import java.util.concurrent.ConcurrentHashMap;

import core.DeveloperSettings;

/** Manages the game states, including handling cycling and transition
 * between game states.
 * @author Bryan Charles Bettis
*/
public class GameStateManager
{
	/** The current game state object. */
	protected static GameState currGS;
	/** A clock for timing different things in a game state. */
	protected static core.Clock clock;
	
	/** Used by the cycle method to limit debug output when a game state
	 * is not currently set.
	 */
	private boolean noGSSetIndicated = false;
	
	/** Basic constructor. */
	public GameStateManager()
	{
	}
	
	/** Setup the game state manager, and the first game state. The initial
	 * game state will be the game state specified by the developer setting
	 * "INIT_GAME_STATE".
	 */
	@SuppressWarnings("unchecked")
	public void setup()
	{
		Class<? extends GameState> initState;
		initState = (Class<? extends GameState>) DeveloperSettings.getSetting("INIT_GAME_STATE");
		setup(initState, new ConcurrentHashMap<String, Object>());
	}
	
	/** Setup the game state manager, and the first game state.
	 * @param initialState the class of the game state to start with
	 */
	public void setup(Class<? extends GameState> initialState)
	{
		setup(initialState, new ConcurrentHashMap<String, Object>());
	}
	
	/** Setup the game state manager, and the first game state.
	 * @param initialState the class of the game state to start with
	 * @param args a hash map of arguments the game state can use to setup
	 */
	public void setup(Class<? extends GameState> initialState, ConcurrentHashMap<String, Object> args)
	{
		setNewGameState(initialState, args);
	}
	
	/** Cycle the current game state. */
	public void cycle()
	{
		try
		{
			// Loop until the current state should be transitioned
			if (currGS.isChangeStateIndicated())
			{
				// Change the game state
				setNewGameState(currGS.getNewState(), currGS.getNewStateArgs());
				// Call one cycle of events for this game state
				currGS.cycle();
			}
			else
			{
				// Call one cycle of events for this game state
				currGS.cycle();
			}
		}
		catch (NullPointerException e)
		{
			// The current game state is not set
			if (!noGSSetIndicated)
			{
				noGSSetIndicated = true;
				System.out.println("No game state set.");
			}
		}
	}
	
	/** Gets the game state's clock.
	 * @return the core.Clock object for the current game state
	 */
	public static synchronized core.Clock getClock()
	{
		return clock;
	}
	
	/** Cleans up the current game state. */
	public void cleanup()
	{
		currGS.cleanup();
		core.userinput.InputManager.clear();
		currGS = null;
		noGSSetIndicated = false;
	}
	
	/** Cleans up any previous game state and creates then sets up the
	 * new game state.
	 * @param newState the class of the new state
	 * @param setupArgs the hash map of arguments to setup the new game state
	 */
	private void setNewGameState(Class<? extends GameState> newState, ConcurrentHashMap<String, Object> setupArgs)
	{
		clock = new core.Clock();
		// Cleanup after previous game state (if any)
		if (currGS != null)
		{
			cleanup();
		}
		// Create the new game state
		try
		{
			currGS = newState.newInstance();
		}
		// Class cannot be instantiated
		catch (InstantiationException e)
		{
			e.printStackTrace();
			System.out.println(
					"ERROR: GameState \'" +
							newState.getName() +
							"\' cannot be instantiated"
							);
			return;
		}
		// Unable to access the class
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			System.out.println(
					"ERROR: GameState \'" +
							newState.getName() +
							"\' is not accessable from " +
							this.getClass().getPackage().getName()
							);
			return;
		}
		// Setup the new game state
		currGS.setup(setupArgs);
		// Start the game state clock
		clock.start();
	}
}
