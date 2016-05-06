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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import xyz.digitalcookies.objective.graphics.RenderEvent;
import xyz.digitalcookies.objective.graphics.Renderer;

/** Holds multiple entities and provides various methods for rendering,
 * updating, and so on all the entities in this container.
 * @author Bryan Charles Bettis
 * @param <T> the type of entity this container will be storing
 */
public class EntityContainer<T extends Entity> implements Renderer
{
	/** The list of entities in this container. */
	private ArrayList<T> entities;
	/** The predicate to use to remove entities at the end of each
	 * updateEntites(event).
	 */
	private Predicate<T> postUpdateRemoveIf;
	
	/** Standard constructor. */
	public EntityContainer()
	{
		entities = new ArrayList<T>(1);
	}
	
	/** Setup this entity container and add the specified entities.
	 * @param entities the entities to add after the container
	 * 		has been setup
	 */
	public EntityContainer(T... entities)
	{
		this.entities = new ArrayList<T>(entities.length);
		addEntities(entities);
	}
	
	/** setup this entity container and add the entities in the specified
	 * list.
	 * @param entities a list of entities that should all be added to this
	 * 		container
	 */
	public EntityContainer(List<T> entities)
	{
		this.entities = new ArrayList<T>(entities.size());
		addEntities(entities);
		Integer i = 1;
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
		// Update entities
		synchronized (entities)
		{
			entities.forEach(
					(T entity)->
					{
						synchronized (entity)
						{
							entity.update(event);
						}
					}
					);
		}
		// Run the post-update entity removal predicate
		if (postUpdateRemoveIf != null)
		{
			synchronized (postUpdateRemoveIf)
			{
				removeIf(postUpdateRemoveIf);
			}
		}
	}
	
	@Override
	public void render(RenderEvent event)
	{
		// Render all entities
		synchronized (entities)
		{
			entities.forEach(
					(T entity)->
					{
						RenderEvent event2 = event.clone();
						synchronized (entity)
						{
							entity.render(event2);
						}
						event2.getGC().dispose();
					}
					);
		}
	}
	
	/** Check if the specified entity is contained within this entity
	 * container.
	 * @param entity the entity to check for
	 * @return true if the entity is in this container, false otherwise
	 */
	public boolean contains(T entity)
	{
		if (entity == null)
		{
			return false;
		}
		boolean result = false;
		synchronized (entities)
		{
			result = entities.contains(entity);
		}
		return result;
	}
	
	/** Add the specified entity to this container, only if it is not
	 * already in this container.
	 * @param entity the entity to add
	 * @return true if the entity was added, false if the addition failed
	 * 		(e.g. entity already exists in this container, or the change has
	 * 		been delayed while updating or rendering is in progress)
	 */
	public boolean addEntity(T entity)
	{
		// No entity specified
		if (entity == null)
		{
			Thread.dumpStack();
			System.out.println("WARNING: Null specified for argument when "
					+ "attempting to add an entity to an entity container."
					);
			return false;
		}
		boolean result = false;
		// Already contains entity
		if (contains(entity))
		{
			result = false;
		}
		else
		{
			synchronized (entities)
			{
				result = entities.add(entity);
			}
		}
		return result;
	}
	
	/** Remove all specified entities from this container.
	 * @param entities the list of entities to remove
	 * @return if the container was changed as a result
	 */
	public boolean addEntities(List<T> entities)
	{
		boolean changed = false;
		// Lock the passed in list to prevent concurrent access
		synchronized (entities)
		{
			for (T entity : entities)
			{
				if (addEntity(entity))
				{
					changed = true;
				}
			}
		}
		return changed;
	}
	
	/** Add all of the specified entities to this container.
	 * @param entities all entities that should be added
	 * @return true if this container was changed (entities were added),
	 * 		false otherwise (no entities added, such as when all are
	 * 		already in this container)
	 */
	public boolean addEntities(T... entities)
	{
		boolean changed = false;
		for (T entity : entities)
		{
			boolean result = addEntity(entity);
			if (result)
			{
				changed = true;
			}
		}
		return changed;
	}
	
	/** Removes the specified entity from this container.
	 * @param entity the entity to remove
	 * @return true if the entity was removed, false if removal
	 * 		failed (such as when the specified entity was not in this
	 * 		container)
	 */
	public boolean removeEntity(T entity)
	{
		boolean changed = false;
		synchronized (entities)
		{
			changed = entities.remove(entity);
		}
		return changed;
	}
	
	/** Remove all specified entities from this container.
	 * @param entities the list of entities to remove
	 * @return if the container was changed as a result
	 */
	public boolean removeEntities(Collection<T> entities)
	{
		boolean changed = false;
		synchronized (this.entities)
		{
			changed = this.entities.removeAll(entities);
		}
		return changed;
	}
	
	/** Remove all specified entities from this container.
	 * @param entities the entities to remove
	 * @return if the container was changed as a result
	 */
	public boolean removeEntities(T... entities)
	{
		boolean changed = false;
		for (T entity : entities)
		{
			boolean result = removeEntity(entity);
			if (result)
			{
				changed = true;
			}
		}
		return changed;
	}
	
	/** Remove all entities in this container for which the specified
	 * predicate filter is true.
	 * @param filter the filter to use to test each entity
	 * @return true if any elements were removed
	 */
	public boolean removeIf(Predicate<T> filter)
	{
		boolean changed = false;
		synchronized (entities)
		{
			changed = entities.removeIf(filter);
		}
		return changed;
	}
	
	/** Add a predicate to run after each call to updateEntities(event)
	 * that will remove any entities that evaluate true for the given
	 * predicate.
	 * @param filter the predicate to evaluate entities with
	 */
	public void setCycleRemoveIf(Predicate<T> filter)
	{
		synchronized (postUpdateRemoveIf)
		{
			postUpdateRemoveIf = filter;
		}
	}
	
	/** Gets all entities contained in this container. Removing, adding,
	 * etc. entities in the returned list will not affect the container itself.
	 * @return a linked list containing all entities in this container
	 */
	@SuppressWarnings("unchecked")
	public List<T> getEntities()
	{
		List<T> es;
		synchronized (entities)
		{
			es = (List<T>) entities.clone();
		}
		return es;
	}
	
	/** Get all entities in this container that evaluate true for the
	 * specified predicate. Specifying null for the predicate is
	 * equivalent to calling getEntities(), which returns all entities
	 * in this container.
	 * @param filter the filter to evaluate each entity with (entities that
	 * 		evaluate true for this will be returned)
	 * @return all entities in this container matching the specified
	 * 		predicate
	 */
	public List<T> getEntities(Predicate<T> filter)
	{
		// No filter specified; return same value as getEntities()
		if (filter == null)
		{
			System.out.println("INFO: No filter specified when calling the "
					+ "filter version of entityContainer.getEntities(filter). "
					+ "Returning an array containing all entities in the "
					+ "container."
					);
			return getEntities();
		}
		List<T> es = new ArrayList<T>();
		synchronized (entities)
		{
			for (T entity : entities)
			{
				if (filter.test(entity))
				{
					es.add(entity);
				}
			}
		}
		return es;
	}
	
	/** Get a new EntityContainer with the same entities currently contained
	 * in this entity container.
	 * @return a new EntityContainer containing the same entities as this
	 * 		container (the new container will not be synchronized with this
	 * 		container; the returned container is its own independent
	 * 		container)
	 */
	public EntityContainer<T> copyContainer()
	{
		return new EntityContainer<T>(getEntities());
	}
	
	/** Get all entities in this container that evaluate true for the
	 * specified predicate. Specifying null for the predicate is
	 * equivalent to calling getContainer(), which returns a new
	 * EntityContainer containing the same entities as this container.
	 * @param filter the filter to evaluate each entity with (entities that
	 * 		evaluate true for this will be returned)
	 * @return all entities that evaluated true for the specified filter,
	 * 		contained within a new EntityContainer
	 */
	public EntityContainer<T> getSubContainer(Predicate<T> filter)
	{
		// No filter specified; return same value as getEntities()
		if (filter == null)
		{
			System.out.println("INFO: No filter specified when calling the "
					+ "filter version of entityContainer.getEntities(filter). "
					+ "Returning an array containing all entities in the "
					+ "container."
					);
			return copyContainer();
		}
		List<T> es = new ArrayList<T>();
		synchronized (entities)
		{
			for (T entity : entities)
			{
				if (filter.test(entity))
				{
					es.add(entity);
				}
			}
		}
		return new EntityContainer<T>(es);
	}
	
	/** Removes all entities from this entity container. */
	public void clear()
	{
		synchronized (entities)
		{
			entities.clear();
		}
	}
	
	@Override
	public void destroy()
	{
		Renderer.super.destroy();
		clear();
	}
	
	/** Get the total number of entities within this entity container.
	 * @return the number of entities that have been added to this container
	 */
	public int numEntities()
	{
		int size = 0;
		synchronized (entities)
		{
			size = entities.size();
		}
		return size;
	}
}
