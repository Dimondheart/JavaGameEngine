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

import java.util.LinkedList;
import java.util.function.Predicate;

import xyz.digitalcookies.objective.graphics.RenderEvent;

/** Holds multiple entities and provides various methods for rendering,
 * updating, and so on all the entities in this container.
 * @author Bryan Charles Bettis
 */
public class EntityContainer implements xyz.digitalcookies.objective.graphics.Renderer
{
	/** The list of entities in this container. */
	protected LinkedList<Entity> entities;
	/** Used to delay modification while operations are being performed
	 * on contained entities.
	 */
	protected boolean delayModification = false;
	protected Predicate<? super Entity> postUpdateRemoveIf;
	
	/** Basic constructor. */
	public EntityContainer()
	{
		entities = new LinkedList<Entity>();
	}
	
	/** Update the entities in this entity container. The specified
	 * entity event will be passed in unchanged to each entity (entities
	 * will not notice that they are in this container.) Changes made to this
	 * container during the update (for example adding or removing entities)
	 * will not take place until the update has finished.
	 * @param event the entity update event to pass in to each entity update
	 */
	public void updateEntities(EntityUpdateEvent event)
	{
//		delayModification = true;
//		entities.forEach((Entity e)->{e.update(event);});
//		delayModification = false;
		int numEntities = entities.size();
		for (int i = 0; i < numEntities; ++i)
		{
			entities.get(i).update(event);
		}
		if (postUpdateRemoveIf != null)
		{
			removeIf(postUpdateRemoveIf);
		}
	}
	
	@Override
	public void render(RenderEvent event)
	{
		int numEntities = entities.size();
		for (int i = 0; i < numEntities; ++i)
		{
			entities.get(i).render(event);
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
	 * 		(e.g. entity already exists in this container, or the change has
	 * 		been delayed while updating or rendering is in progress)
	 */
	public synchronized boolean addEntity(Entity entity)
	{
//		if (delayModification)
//		{
//			// TODO add to a queue here
//			return false;
//		}
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
	 * 		failed (e.g. entity not in this container, or the change has
	 * 		been delayed while updating or rendering is in progress)
	 */
	public synchronized boolean removeEntity(Entity entity)
	{
//		if (delayModification)
//		{
//			// TODO add to queue here
//			return false;
//		}
		return entities.remove(entity);
	}
	
	/** Remove all entities in this container for which the specified
	 * predicate filter is true.
	 * @param filter the filter to use to test each entity
	 * @return true if any elements were removed
	 */
	public synchronized boolean removeIf(Predicate<? super Entity> filter)
	{
		// TODO add ability to delay modification
		return entities.removeIf(filter);
	}
	
	public synchronized void setCycleRemoveIf(Predicate<? super Entity> filter)
	{
		this.postUpdateRemoveIf = filter;
	}
	
	/** Gets all entities contained in this container. Removing, adding, moving,
	 * etc. entities in the returned list will not affect the container itself.
	 * @return a linked list containing all entities in this container
	 */
	public synchronized Entity[] getEntities()
	{
		Entity[] es = entities.toArray(new Entity[0]);
		return es;
	}
	
	/** Cleanup this entity container. */
	public synchronized void cleanupAll()
	{
		destroy();
		entities.clear();
	}
	
	/** Get the total number of entities within this entity container.
	 * @return the number of entities that have been added to this container
	 */
	public synchronized int numEntities()
	{
		return entities.size();
	}
}
