package org.laolanguage;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * Display audio option and volume preferences.
 * 
 * @author Santi Anousaya
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
				// TODO Auto-generated method stub
			}
		});
	}

	private void setVolumeValue(int progress) {
		volumeTv.setText(" " + progress);
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
		LaoBaseActivity.audioVolume = audioVolumeSb.getProgress();
	}
}
