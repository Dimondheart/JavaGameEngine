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

import static core.sound.SoundManager.BGMTransition;

/** An event used to change/transition the playing background music track.
* @author Bryan Charles Bettis
*/
class BGMEvent extends BaseSoundEvent
{
	/** The background music to play. */
	private String bgm;
	/** The transition effect into this track. */
	private BGMTransition effect;
	
	/** Basic constructor.
	 * @param bgm the name of the file
	 * @param effect the effect used to transition in the new track
	 */
	public BGMEvent(String bgm, BGMTransition effect)
	{
		this.bgm = bgm;
		this.effect = effect;
	}
	
	/** Get the track to play.
	 * @return the name of the file
	 */
	public String getBGM()
	{
		return bgm;
	}
	
	/** Get the effect used to transition in this track.
	 * @return the transition effect
	 */
	public BGMTransition getEffect()
	{
		return effect;
	}
}
