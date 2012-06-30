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

import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * Display audio option and volume preferences.
 * 
 */
public class PreferenceActivity extends LaoBaseActivity {
	private CheckBox audioOptionCb;
	private SeekBar audioVolumeSb;
	private TextView volumeTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference);

		audioOptionCb = (CheckBox) findViewById(R.id.audioOption);
		volumeTv = (TextView) findViewById(R.id.volValue);
		audioVolumeSb = (SeekBar) findViewById(R.id.volSeekBar);

		audioOptionCb.setChecked(isAudioOption());
		audioVolumeSb.setProgress(getAudioVolumn());
		setVolumeValue(getAudioVolumn());

		audioVolumeSb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				setVolumeValue(progress);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				playAudio(null);
			}
		});
	}

	private void setVolumeValue(int progress) {
		volumeTv.setText(" " + progress);

		// Update static variables
		LaoBaseActivity.audioOption = audioOptionCb.isChecked();
		LaoBaseActivity.audioVolume = progress;
	}

	/**
	 * Save audio option and volume preferences and update static variables.
	 */
	@Override
	protected void saveSharedPreferences(Editor ed) {
		// Save current preferences
		ed.putBoolean(AUDIO_OPTION, audioOptionCb.isChecked());
		ed.putInt(AUDIO_VOLUME, audioVolumeSb.getProgress());

		// Update static variables
		LaoBaseActivity.audioOption = audioOptionCb.isChecked();
	}

	/**
	 * Create background audio.
	 */
	@Override
	protected MediaPlayer createMediaPlayer() {
		MediaPlayer mp = MediaPlayer.create(PreferenceActivity.this,
				R.raw.champalao);
		mp.setLooping(true);
		return mp;
	}
}
