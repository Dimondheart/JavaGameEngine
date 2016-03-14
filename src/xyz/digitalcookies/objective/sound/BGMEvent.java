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

import static xyz.digitalcookies.objective.sound.SoundManager.BGMTransition;

/** An event for the sound system queue, used to change the background music.
* @author Bryan Charles Bettis
*/
public class BGMEvent extends BaseSoundEvent
{
	/** The background music to play. */
	private String bgm;
	/** The transition effect into this track. */
	private BGMTransition effect;
	
	/** Basic constructor. Sets the track and other settings to default or
	 * placeholder values.
	 */
	public BGMEvent()
	{
		this("", BGMTransition.IMMEDIATE);
	}
	
	/** Basic constructor.
	 * @param bgmTrack the name of the file
	 * @param effect the effect used to transition in the new track
	 */
	public BGMEvent(String bgmTrack, BGMTransition effect)
	{
		setTrack(bgmTrack);
		setEffect(effect);
	}
	
	/** Get the background music track to play.
	 * @return the name of the file
	 */
	public String getTrack()
	{
		return bgm;
	}
	
	/** Get the transition effect used to transition in this track.
	 * @return the transition effect
	 */
	public BGMTransition getEffect()
	{
		return effect;
	}
	
	/** Change the background music track.
	 * @param newTrack the name/path to the new track
	 * @see game.resources
	 */
	public void setTrack(String newTrack)
	{
		this.bgm = newTrack;
	}
	
	/** Get the effect used to transition in this track.
	 * @param effect the effect to use to transition in the new track
	 */
	public void setEffect(BGMTransition effect)
	{
		this.effect = effect;
	}
}
