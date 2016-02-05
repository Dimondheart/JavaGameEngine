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

package core.sound;

/** Event used to change one of the volume settings.
 * @author Bryan Charles Bettis
 */
class VolumeEvent extends BaseSoundEvent
{
	/** What volume setting is being changed. */
	private SoundManager.VolumeSetting setting;
	/** The new value for the volume level. */
	private int newVolume = 100;
	
	/** Basic constructor.
	 * @param setting the setting to change
	 * @param newVolume the new value of that setting
	 */
	public VolumeEvent(SoundManager.VolumeSetting setting, int newVolume)
	{
		this.setting = setting;
		this.newVolume = newVolume;
	}
	
	/** Gets the setting to change.
	 * @return the Volume.VolumeSetting to change
	 */
	public SoundManager.VolumeSetting getSetting()
	{
		return setting;
	}
	
	/** Gets the new volume level.
	 * @return the new volume value
	 */
	public int getNewVolume()
	{
		return newVolume;
	}
}
