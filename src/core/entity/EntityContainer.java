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

package core.entity;

import java.util.LinkedList;
import java.util.function.Predicate;

import core.graphics.RenderEvent;

/** 
 * @author Bryan Charles Bettis
 */
public class EntityContainer implements core.graphics.Renderer
{
	/** The list of entities in this container. */
	protected LinkedList<Entity> entities;
	
	public EntityContainer()
	{
		entities = new LinkedList<Entity>();
	}
	
	/** Update the entities in this entity container. The specified
	 * entity event will be passed in unchanged to each entity (entities
	 * will not notice that they are in this container.)
	 * @param event the entity update event to pass in to each entity update
	 */
	public synchronized void updateEntities(EntityUpdateEvent event)
	{
		for (Entity entity : entities)
		{
			entity.update(event);
		}
	}
	
	@Override
	public synchronized void render(RenderEvent event)
	{
		for (Entity entity : entities)
		{
			entity.render(event);
		}
	}
	
	/** Check if the specified entity is contained within this entity
	 * container.
	 * @param entity the entity to check for
	 * @return true if the entity is in this container, false otherwise
	 */
	public synchronized boolean containsEntity(Entity entity)
	{
		return entities.contains(entity);
	}
	
	/** Add the specified entity to this container, only if it is not
	 * already in this container.
	 * @param entity the entity to add
	 * @return true if the entity was added, false if the addition failed
	 * 		(e.g. entity already exists in this container)
	 */
	public synchronized boolean addEntity(Entity entity)
	{
//		return false;
		if (containsEntity(entity))
		{
			return false;
		}
		else
		{
			entities.add(entity);
			return true;
		}
	}
	
	/** Removes the specified entity from this container.
	 * @param entity the entity to remove
	 * @return true if the entity was removed, false if removal
	 * 		failed (e.g. entity not in this container)
	 */
	public synchronized boolean removeEntity(Entity entity)
	{
		return entities.remove(entity);
	}
	
	public synchronized boolean removeIf(Predicate<? super Entity> filter)
	{
		return entities.removeIf(filter);
	}
	
	/** Gets all entities contained in this container. Removing, adding, moving,
	 * etc. entities in the returned list will not affect the container itself.
	 * @return a linked list containing all entities in this container
	 */
	@SuppressWarnings("unchecked")
	public synchronized LinkedList<Entity> getEntities()
	{
		return (LinkedList<Entity>) entities.clone();
	}
}
