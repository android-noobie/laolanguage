package org.laolanguage;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

/**
 * Activity to display introduction information and the EULA.
 * 
 * @author Santi Anousaya
 * 
 */
public class IntroductionActivity extends LaoBaseActivity {
	private static final boolean DEFAULT_SKIP_INTRODUCTION = false;
	private static final String SKIP_INTRODUCTION = "skip_introduction";
	CheckBox skipIntroductionCb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introduction);

		skipIntroductionCb = (CheckBox) findViewById(R.id.skipIntroductionCb);

		// Get saved or the default value of skip introduction option
		boolean skipIntroduction = getSharedPreferences().getBoolean(
				SKIP_INTRODUCTION, DEFAULT_SKIP_INTRODUCTION);
		skipIntroductionCb.setChecked(skipIntroduction);
	}

	private void checkEulaAndSkipOption(boolean skipIntroduction) {
		// This is true if and only if user already accepted the term of the
		// EULA and select the "Don't show the again" checkbox
		if (skipIntroduction) {
			continueToTabs(null);
		} else {
			Eula.show(this);
		}
	}

	/**
	 * Create background audio.
	 */
	@Override
	protected MediaPlayer createMediaPlayer() {
		MediaPlayer mp = MediaPlayer.create(IntroductionActivity.this,
				R.raw.champalao);
		mp.setLooping(true);
		return mp;
	}

	/**
	 * Save the current traditional number mode.
	 */
	@Override
	protected void saveSharedPreferences(Editor ed) {
		super.saveSharedPreferences(ed);

		// Save current number mode
		ed.putBoolean(SKIP_INTRODUCTION, skipIntroductionCb.isChecked());
	}

	/**
	 * Override to always play the background music if skip option is not
	 * checked.
	 * 
	 * @return
	 */
	@Override
	protected boolean isAudioOption() {
		return !skipIntroductionCb.isChecked();
	}

	public void continueToTabs(View view) {
		Intent tabWidget = new Intent().setClass(this, LanguageTabWidget.class);
		startActivity(tabWidget);
		// Remove the page from stack if the checkbox is checked.
		if (skipIntroductionCb.isChecked()) {
			finish();
		}
	}

	/**
	 * Override to to check the option to skip this page.
	 */
	@Override
	protected void onResume() {
		super.onResume();

		// Check to see if EULA need to be displayed or this page should skipped
		checkEulaAndSkipOption(skipIntroductionCb.isChecked());

	}
}
