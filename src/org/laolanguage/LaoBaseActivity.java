/**
 * Copyright 2012 Santi Anousaya
 * 
 * This file is part of Lao Language.
 * 
 * Lao Language is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Lao Language is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with Lao Language. If not, see <http://www.gnu.org/licenses/>.
 */
package org.laolanguage;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * Abstract Activity that contains common functions and resource for Lao
 * Language activities.
 * 
 */
public abstract class LaoBaseActivity extends Activity {
	private static final int DEFAULT_AUDIO_DELAY = 0;
	private static final boolean DEFAULT_AUDIO_OPTION = true;
	private static final int DEFAULT_AUTO_PLAY_DELAY = 5;
	private static final int DEFAULT_VOLUME = 100;
	static final String AUDIO_OPTION = "audio_option";
	static final String AUDIO_VOLUME = "audio_volume";
	static final String AUTO_PLAY_DELAY = "auto_play_delay";

	// These are shared with all instances
	static Boolean audioOption;
	static Integer audioVolume;
	static Integer autoPlayDelay;

	private GestureDetector gestureDetector;
	private Typeface tf;
	private MediaPlayer mp;
	private SharedPreferences mPrefs;
	Timer autoPlayTimer;

	/**
	 * Get audio option from the preference or the default value.
	 * 
	 * @return
	 */
	protected boolean isAudioOption() {
		if (audioOption == null) {
			audioOption = getSharedPreferences().getBoolean(AUDIO_OPTION,
					DEFAULT_AUDIO_OPTION);
		}
		return audioOption;
	}

	/**
	 * Get audio volume from the preference or the default value.
	 * 
	 * @return
	 */
	protected int getAudioVolumn() {
		if (audioVolume == null) {
			audioVolume = getSharedPreferences().getInt(AUDIO_VOLUME,
					DEFAULT_VOLUME);
		}
		return audioVolume;
	}

	/**
	 * Get auto play delay from the preference or the default value.
	 * 
	 * @return
	 */
	protected int getAutoPlayDelay() {
		if (autoPlayDelay == null) {
			autoPlayDelay = getSharedPreferences().getInt(AUTO_PLAY_DELAY,
					DEFAULT_AUTO_PLAY_DELAY);
		}
		return autoPlayDelay;
	}

	/**
	 * Get Typeface for Lao Font.
	 * 
	 * @return
	 */
	protected Typeface getLaoTypeface() {
		if (tf == null) {
			tf = Typeface.createFromAsset(getAssets(), "fonts/LaoUI.ttf");
		}
		return tf;
	}

	/**
	 * Get the SharedPreferences object.
	 * 
	 * @return
	 */
	protected SharedPreferences getSharedPreferences() {
		if (mPrefs == null) {
			mPrefs = getSharedPreferences(getString(R.string.app_name), 0);
		}
		return mPrefs;
	}

	/**
	 * Clean up audio resource.
	 */
	protected void cleanUpAudioResource() {
		if (mp != null) {
			if (mp.isPlaying()) {
				mp.stop();
			}
			mp.release();
			mp = null;
		}
	}

	/**
	 * Cancel auto play to free up resource if applicable.
	 */
	protected void cancelAutoPlay() {
		if (autoPlayTimer != null) {
			autoPlayTimer.cancel();
			autoPlayTimer.purge();
			autoPlayTimer = null;
		}
	}

	/**
	 * Play audio when user clicks on the audio button or when the page is
	 * loaded and the "Play audio automatically" preference is selected.
	 * 
	 * @param view
	 */
	public void playAudio(View view) {
		if (view != null || isAudioOption()) {

			// Cleanup audio previous resource if applicable before start a new
			// one
			cleanUpAudioResource();

			Runnable playAudio = new Runnable() {
				public void run() {
					mp = createMediaPlayer();
					if (mp != null) {
						float volume = getAudioVolumn() / 100f;
						mp.setVolume(volume, volume);
						mp.start();
					}
				}
			};

			Handler handler = new Handler();

			// No need for the delay when user click on the play audio icon
			// (view != null)
			handler.postDelayed(playAudio, (view != null ? 0 : getAudioDelay()));
		}
	}

	/**
	 * Delay (millisecond) before play the audio. The fault is 0 (no delay)
	 * 
	 * @return
	 */
	protected long getAudioDelay() {
		return DEFAULT_AUDIO_DELAY;
	}

	/**
	 * Subclass need to implement this method if audio need to be played when
	 * playAudio() method is invoked.
	 * 
	 * @return
	 */
	protected MediaPlayer createMediaPlayer() {
		return null;
	}

	/**
	 * Override to save the preferences and release audio resource.
	 */
	@Override
	protected void onPause() {
		super.onPause();

		// Save shared preferences
		Editor ed = getSharedPreferences().edit();
		saveSharedPreferences(ed);
		ed.commit();

		// Cleanup audio resource
		cleanUpAudioResource();

		// Cancel AutoPlay if applicable
		cancelAutoPlay();
	}

	/**
	 * Subclass can override this method to save additional persistence shared
	 * preferences.
	 * 
	 * @param ed
	 */
	protected void saveSharedPreferences(Editor ed) {
	}

	/**
	 * Utility method to dd gesture detector for fling motions.
	 */
	protected void addGestureDector() {
		if (gestureDetector == null) {
			SimpleOnGestureListener simpleOnGestureListener = new SimpleOnGestureListener() {

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,

				float velocityX, float velocityY) {

					float sensitvity = 50;

					if ((e1.getX() - e2.getX()) > sensitvity) {
						swipeLeft();
					} else if ((e2.getX() - e1.getX()) > sensitvity) {

						swipeRight();

					} else if ((e1.getY() - e2.getY()) > sensitvity) {

						swipeUp();

					} else if ((e2.getY() - e1.getY()) > sensitvity) {

						swipeDown();

					}

					return true;

				}

			};

			gestureDetector = new GestureDetector(this, simpleOnGestureListener);
		}
	}

	/**
	 * Override to capture gesture motions.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return (gestureDetector == null) ? false : gestureDetector
				.onTouchEvent(event);
	}

	/**
	 * Subclass need to override this method to perform task onSwipeDown.
	 */
	protected void swipeDown() {
	}

	/**
	 * Subclass need to override this method to perform task onSwipeUp.
	 */
	protected void swipeUp() {
	}

	/**
	 * Subclass need to override this method to perform task onSwipeLeft.
	 */
	protected void swipeLeft() {
	}

	/**
	 * Subclass need to override this method to perform task onSwipeRight.
	 */
	protected void swipeRight() {
	}

	/**
	 * Override to play audio and auto play onResume() if applicable.
	 */
	@Override
	protected void onResume() {
		super.onResume();

		playAudio(null);

		autoPlay(null);
	}

	/**
	 * Start auto play if applicable.
	 */
	public void autoPlay(View view) {
		TimerTask task = getAutoPlayTask();
		if (task != null) {
			autoPlayTimer = new Timer("autoPlay");
			autoPlayTimer.schedule(task, 0, getAutoPlayDelay() * 1000);

		} else {
			cancelAutoPlay();
		}
	}

	protected TimerTask getAutoPlayTask() {
		return null;
	}
}
