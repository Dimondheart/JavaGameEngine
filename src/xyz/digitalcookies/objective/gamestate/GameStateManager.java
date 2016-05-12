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

import java.util.HashMap;
import xyz.digitalcookies.objective.Subsystem;
import xyz.digitalcookies.objective.input.InputManager;

/** Manages the game states, including handling cycling and transition
 * between game states.
 * @author Bryan Charles Bettis
*/
public class GameStateManager extends Subsystem
{
	/** The current game state object. */
	protected GameState currGS;
	/** The initial game state; the first one this game state manager setup. */
	private Class<? extends GameState> initGameState;
	
	/** Basic constructor.
	 * @param initGameState the game state to initially setup
	 */
	public GameStateManager(Class<? extends GameState> initGameState)
	{
		super(10, "GameState Thread");
		this.initGameState = initGameState;
	}
	
	@Override
	protected void setupSystem(HashMap<String, Object> config)
	{
		setNewGameState(initGameState);
		currGS.setup(new HashMap<String, Object>());
	}
	
	@Override
	protected void startSystem()
	{
		System.out.println("Starting game state...");
	}
	
	@Override
	protected void stopSystem()
	{
		cleanup();
	}
	
	@Override
	protected boolean runCycle()
	{
		if (InputManager.isQuitting())
		{
			cleanup();
			return false;
		}
		else if (!InputManager.isRunning() || currGS == null)
		{
			return true;
		}
		// Update input devices
		InputManager.pollImmediately();
		// Loop until the current state should be transitioned
		if (currGS.isChangeStateIndicated())
		{
			HashMap<String, Object> setupArgs = currGS.getNewStateArgs();
			// Change the game state
			setNewGameState(currGS.getNewState());
			// Setup the new game state
			currGS.setup(setupArgs);
		}
		// Call one cycle of events for this game state
		currGS.cycle();
		return true;
	}
	
	/** Cleans up the current game state. */
	private void cleanup()
	{
		if (currGS != null)
		{
			currGS.cleanup();
		}
		InputManager.clear();
		currGS = null;
	}
	
	/** Cleans up any previous game state and creates then sets up the
	 * new game state.
	 * @param newState the class of the new state
	 */
	private void setNewGameState(Class<? extends GameState> newState)
	{
		cleanup();
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
	}
}
