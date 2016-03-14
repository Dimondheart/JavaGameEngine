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

package xyz.digitalcookies.objective.entity;

/** Entity update events are passed in to entities when calling the entities'
 * update methods. These events provide contextual information that the entity
 * could use, like other entities it could interact with.
 * @author Bryan Charles Bettis
 */
public class EntityUpdateEvent extends EntityEvent
{
	/** Contains entities that could be interacted with by entities
	 * this event will be given to.
	 */
	private EntityContainer entities;
	
	public EntityUpdateEvent()
	{
		this(new EntityContainer());
	}
	
	public EntityUpdateEvent(EntityContainer entities)
	{
		if (entities == null)
		{
			this.entities = new EntityContainer();
		}
		else
		{
			this.entities = entities;
		}
	}
	
	public EntityContainer getEntities()
	{
		return entities;
	}
	
	public void setEntities(EntityContainer entities)
	{
		this.entities = entities;
	}
}