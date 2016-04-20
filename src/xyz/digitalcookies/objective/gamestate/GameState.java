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

package xyz.digitalcookies.objective.gamestate;

import java.util.concurrent.ConcurrentHashMap;

import xyz.digitalcookies.objective.input.InputManager;

/** Base Class for a game state.
 * @author Bryan Charles Bettis
 */
public abstract class GameState
{
	/** A clock for timing different things in a game state. */
	protected static xyz.digitalcookies.objective.utility.Stopwatch clock;
	
	/** The collection of startup arguments for the next game state. */
	protected ConcurrentHashMap<String, Object> newStateArgs;
	
	/** The Class of the next game state to transition to. */
	private Class<? extends GameState> newState;
	
	/** Basic constructor. */
	public GameState()
	{
		clock = new xyz.digitalcookies.objective.utility.Stopwatch();
		newStateArgs = new ConcurrentHashMap<String, Object>();
		newState = this.getClass();
	}
	
	/** Performs setup operations specific to each game state.
	 * @param args a hash map of arguments the state can use to set up
	 */
	protected abstract void setupState(ConcurrentHashMap<String, Object> args);
	/** State-specific cycle operations. */
	protected abstract void cycleState();
	/** Cleanup operations specific to a game state. */
	protected abstract void cleanupState();
	
	/** Performs setup operations for a game state.
	 * @param args a hash map of arguments the state can use to set up
	 */
	public void setup(ConcurrentHashMap<String, Object> args)
	{
		// Game-state-specific setup
		setupState(args);
		clock.start();
	}
	
	/** Does one cycle of the game state. */
	public void cycle()
	{
		// Game-state-specific cycling
		cycleState();
	}
	
	/** Do any important cleanup-related operations before stopping a game
	 * state, like auto-saving, etc.
	 */
	public void cleanup()
	{
		// Game-state-specific cleanup
		cleanupState();
	}
	
	/** Gets the game state's clock.
	 * @return the core.Clock object for the current game state
	 */
	public static synchronized xyz.digitalcookies.objective.utility.Stopwatch getClock()
	{
		return clock;
	}
	
	/** Determines if this game state should be transitioned.
	 * @return true if the state needs to be changed
	 */
	public boolean isChangeStateIndicated()
	{
		return (!newState.equals(this.getClass()));
	}
	
	/** Get the new game state to transition to.
	 * @return the GameState class for the new state
	 */
	public Class<? extends GameState> getNewState()
	{
		return newState;
	}
	
	/** Gets the hash map of arguments to use for setting up the next
	 * game state.
	 * @return the hash map of arguments the next state can use for setup
	 */
	public ConcurrentHashMap<String, Object> getNewStateArgs()
	{
		return newStateArgs;
	}
	
	/** Indicate changing to a new game state and set the new state.
	 * @param newState what GameState class to change to next
	 */
	protected void changeState(Class<? extends GameState> newState)
	{
		this.newState = newState;
		if (newState == null)
		{
			InputManager.quit();
		}
	}
}
