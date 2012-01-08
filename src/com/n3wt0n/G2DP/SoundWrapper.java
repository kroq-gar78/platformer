package com.n3wt0n.G2DP;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundWrapper {
	private boolean soundOn = true;
	protected Sound sfx; // Sounds that are played just once without repeat.
	protected Sound music; // The background music/audio. This stuff loops.

	/**
	 * Create an empty SoundWrapper.
	 */
	public SoundWrapper() {

	}

	/**
	 * Create a SoundWrapper and initialise the sound effect.
	 * 
	 * @param sfx
	 *            The .wav or .ogg to initialise.
	 */
	public SoundWrapper(Sound sfx) {
		this.sfx = sfx;
	}

	/**
	 * Create a SoundWrapper and initialise with sound effect and music
	 * 
	 * @param sfx
	 *            The sound effect.
	 * @param music
	 *            The music to be looped.
	 */
	public SoundWrapper(Sound sfx, Sound music) {
		this.sfx = sfx;
		this.music = music;
	}

	/**
	 * Is the sound turned on?
	 * 
	 * @return True if the sound is not muted.
	 */
	public boolean soundOn() {
		return soundOn;
	}

	/**
	 * Turn the sound on or off.
	 * 
	 * @param boo
	 *            True or False for sound or none.
	 */
	public void setSoundOn(boolean boo) {
		soundOn = boo;
	}

	/**
	 * Play a sound effect.
	 * 
	 * @param sound
	 *            The location of the sound effect to play.
	 * @throws SlickException
	 */
	public void playSound(String sound) throws SlickException {
		if (!soundOn)
			return;

		sfx = new Sound(sound);
		sfx.play();
	}

	/**
	 * Loop a sound.
	 * 
	 * @param sound
	 *            The location of the sound to loop.
	 * @throws SlickException
	 */
	public void loopSound(String sound) throws SlickException {
		if (!soundOn)
			return;

		music = new Sound(sound);
		music.loop();
	}

	/**
	 * Turn off the sound. Calls Sound.stop() on the sfx and music.
	 */
	public void muteSound() {
		// Yes, stop the sound entirely, not just mute it.
		sfx.stop();
		music.stop();
	}
}
