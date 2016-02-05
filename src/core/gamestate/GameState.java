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

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/** Base Class for a game state.
 * @author Bryan Charles Bettis
 */
public abstract class GameState implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** Renders the FPS to the screen. */
	protected FPSRenderer fpsRenderer;
	
	/** The Class of the next game state to transition to. */
	private Class<? extends GameState> newState;
	/** The list of startup arguments for the next game state.
	 * TODO wrap this with a custom object.
	 */
	protected ConcurrentHashMap<String, Object> newStateArgs;
	
	/** Basic constructor.
	 * @param state the GameStates for the subclass
	 */
	public GameState()
	{
		fpsRenderer = new FPSRenderer();
		newState = this.getClass();
		newStateArgs = new ConcurrentHashMap<String, Object>();
	}
	
	/** Performs setup operations specific to each game state. */
	protected abstract void setupState(ConcurrentHashMap<String, Object> args);
	/** State-specific cycle operations. */
	protected abstract void cycleState();
	/** Cleanup operations specific to a game state. */
	protected abstract void cleanupState();
	
	/** Performs setup operations for a game state. */
	public void setup(ConcurrentHashMap<String, Object> args)
	{
		// Game-state-specific setup
		setupState(args);
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
		fpsRenderer.destroy();
		// Game-state-specific cleanup
		cleanupState();
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
	 * @return a ConcurrentHashMap<String, Object> of arguments
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
	}
}
