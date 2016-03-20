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

package xyz.digitalcookies.objective.scene;

/** TODO add documentation
 * @author Bryan Charles Bettis
 */
public interface Entity extends xyz.digitalcookies.objective.graphics.Renderer
{
	/** Check if this entity utilizes a Body. An entity does not need to 
	 * currently have a body setup for this to return true. This method 
	 * only indicates if the entity could be using a body.
	 * @return true if the entity could be using a body, false if the
	 * 		entity does not support use of a body
	 */
	public boolean utilizesBody();
	/** Tell this entity to update itself. Updating includes interaction
	 * with other entities and general actions and changes made by the
	 * entity.
	 * @param event the event containing information which could be useful
	 * to an updating entity
	 */
	public void update(EntityUpdateEvent event);
	
	@Override
	public default void render(xyz.digitalcookies.objective.graphics.RenderEvent event)
	{
	}
}
