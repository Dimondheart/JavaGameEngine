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

package xyz.digitalcookies.objective.sound;

import xyz.digitalcookies.objective.sound.SoundManager.BGMTransition;

/** A simple event used to stop or fade out the current background music
 * track.
 * @author Bryan Charles Bettis
 */
class StopBGMEvent extends BaseSoundEvent
{
	/** The effect used to fade out the track. */
	private BGMTransition effect;
	
	/** Basic constructor.
	 * @param effect the effect used to fade out the track
	 */
	public StopBGMEvent(BGMTransition effect)
	{
		this.effect = effect;
	}
	
	/** Get the fade out effect.
	 * @return the transition effect for fading out the track
	 */
	public BGMTransition getEffect()
	{
		return effect;
	}
}
