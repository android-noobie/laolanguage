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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;

/**
 * Activity to display Lao numbers.
 * 
 */
public class NumberActivity extends LaoBaseActivity {
	private static final boolean DEFAULT_TRADITIONAL_MODE = false;
	private static final String TRADITIONAL_MODE = "traditionalMode";
	GridView gridview;
	NumberAdapter adapter;
	CheckBox traditionalCb;

	int[] numberSound = { R.raw.one, R.raw.two, R.raw.three, R.raw.four,
			R.raw.five, R.raw.six, R.raw.seven, R.raw.eight, R.raw.nine,
			R.raw.zero };
	Integer selectedPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.number);

		gridview = (GridView) findViewById(R.id.numberview);

		// Get saved or the default value of traditional number mode
		boolean traditionalMode = getSharedPreferences().getBoolean(
				TRADITIONAL_MODE, DEFAULT_TRADITIONAL_MODE);
		traditionalCb = (CheckBox) findViewById(R.id.traditionalCb);
		traditionalCb.setChecked(traditionalMode);

		adapter = new NumberAdapter(this);
		adapter.setTraditionalMode(traditionalMode);
		gridview.setAdapter(adapter);

		// Add on click listener to play audio for selected number.
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				selectedPosition = position;
				playAudio(v);
			}
		});

	}

	/**
	 * Switch number mode between standar number and traditional number.
	 * 
	 * @param view
	 */
	public void switchNumberMode(View view) {
		adapter.setTraditionalMode(traditionalCb.isChecked());
		gridview.invalidateViews();

	}

	/**
	 * Create audio for selected number.
	 */
	@Override
	protected MediaPlayer createMediaPlayer() {
		MediaPlayer mp = null;
		if (selectedPosition != null) {
			if (selectedPosition < numberSound.length - 1) {
				mp = MediaPlayer.create(NumberActivity.this,
						numberSound[selectedPosition]);
			} else if (selectedPosition == 10) {
				mp = MediaPlayer.create(NumberActivity.this, numberSound[9]);
			}

			// Reset it after the media have been created.
			selectedPosition = null;
		}
		return mp;
	}

	/**
	 * Save the current traditional number mode.
	 */
	@Override
	protected void saveSharedPreferences(Editor ed) {
		super.saveSharedPreferences(ed);

		// Save current number mode
		ed.putBoolean(TRADITIONAL_MODE, traditionalCb.isChecked());
	}
}
