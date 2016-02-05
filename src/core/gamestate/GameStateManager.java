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
		// TODO make a way to specify this in the game or a game sub-package
		this(game.gamestate.MainMenuTest.class);
	}
	
	/** TODO add javadoc. */
	public GameStateManager(Class<? extends GameState> initialState)
	{
		this(initialState, new ConcurrentHashMap<String, Object>());
	}
	
	/** TODO add javadoc. */
	public GameStateManager(Class<? extends GameState> initialState, ConcurrentHashMap<String, Object> args)
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
	
	/** Save the current game state, if it is a save-able state. */
	public void saveState()
	{
		// TODO implement
		if (currGS instanceof SavableGameState)
		{
			System.out.println("Current State IS saveable");
		}
		else
		{
			System.out.println("Current State is NOT saveable");
		}
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
	private synchronized void setNewGameState(Class<? extends GameState> newState, ConcurrentHashMap<String, Object> setupArgs)
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
			// TODO Auto-generated catch block
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
