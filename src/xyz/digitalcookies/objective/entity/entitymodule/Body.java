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

package xyz.digitalcookies.objective.entity.entitymodule;

import xyz.digitalcookies.objective.entity.Scene;

/** A body represents the physical aspects of an entity that exist within
 * a specific scene. This class may be integrated with Entity in the future,
 * or with a physics system.
 * @author Bryan Charles Bettis
 */
public abstract class Body extends EntityModule
{
	/** Set the scene that this body is part of.
	 * @param scene the scene containing various properties that this
	 * 		body can use
	 * @return true if the scene was set successfully, false otherwise
	 * 		(for example if the scene is not of a supported type)
	 */
	public abstract boolean setScene(Scene scene);
}
