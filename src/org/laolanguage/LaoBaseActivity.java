package org.laolanguage;

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
 * @author Santi Anousaya
 * 
 */
public abstract class LaoBaseActivity extends Activity {
	private static final int DEFAULT_AUDIO_DELAY = 0;
	private static final boolean DEFAULT_AUDIO_OPTION = true;
	private static final int DEFAULT_VOLUME = 100;
	static final String AUDIO_OPTION = "audio_option";
	static final String AUDIO_VOLUME = "audio_volume";

	// These are shared with all instances
	static Boolean audioOption;
	static Integer audioVolume;

	private GestureDetector gestureDetector;
	private Typeface tf;
	private MediaPlayer mp;
	private SharedPreferences mPrefs;

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
						mp.setLooping(false);
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
	 * Override to play audio onResume() if applicable.
	 */
	@Override
	protected void onResume() {
		super.onResume();

		playAudio(null);
	}
}
