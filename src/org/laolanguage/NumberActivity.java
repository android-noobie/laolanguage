package org.laolanguage;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class NumberActivity extends Activity {
	MediaPlayer mp;
	GridView gridview;
	NumberAdapter adapter;

	int[] numberSound = { R.raw.one, R.raw.two, R.raw.three, R.raw.four,
			R.raw.five, R.raw.six, R.raw.seven, R.raw.eight, R.raw.nine,
			R.raw.zero };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.number);

		gridview = (GridView) findViewById(R.id.numberview);

		adapter = new NumberAdapter(this);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				// Cleanup audio previous resource if applicable before start a
				// new one
				cleanUpAudioResource();

				if (position < numberSound.length-1) {
					mp = MediaPlayer.create(NumberActivity.this,
							numberSound[position]);
					mp.setLooping(false);
					mp.start();
				} else if (position == 10) {
					mp = MediaPlayer
							.create(NumberActivity.this, numberSound[9]);
					mp.setLooping(false);
					mp.start();
				}
			}
		});

	}

	public void switchNumberMode(View view) {
		adapter.toggleMode();
		gridview.invalidateViews();

	}

	private void cleanUpAudioResource() {
		if (mp != null) {
			if (mp.isPlaying()) {
				mp.stop();
			}
			mp.release();
			mp = null;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Cleanup audio resource
		cleanUpAudioResource();
	}
}
