package org.laolanguage;

import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * Activity to display Lao basic vowels.
 * 
 * @author Santi Anousaya
 * 
 */
public class VowelActivity extends LaoBaseActivity implements ViewFactory {
	private static final int AUDIO_DELAY = 1000;
	private static final int DEFAULT_POSITION = 0;
	private static final String VOWEL_POSITION = "vowel_position";
	TextView vowelTv;
	TextView vowelSoundTv;
	TextView wordTv;
	ImageSwitcher wordIs;
	GridView gridview;
	VowelSoundAdapter adapter;

	int curPosition = 0;
	Button previousBt;
	Button nextBt;

	// each vowels
	int[] vowel = { R.string.ah_short, R.string.ah_long, R.string.i_short,
			R.string.ee_long, R.string.ue_short, R.string.ue_long,
			R.string.oo_short, R.string.oo_long, R.string.oh_short,
			R.string.oh_long, R.string.ay_short, R.string.ay_long,
			R.string.ae_short, R.string.ae_long, R.string.aw_short,
			R.string.aw_long, R.string.ia_short, R.string.ia_long,
			R.string.ua_short, R.string.ua_long, R.string.eua_short,
			R.string.eua_long, R.string.euh_short, R.string.euh_long,
			R.string.ai_short, R.string.ai_long, R.string.ao, R.string.um };
	int[] vowelSound = { R.string.gaw, R.string.khaw, R.string.khoaw,
			R.string.ghaw, R.string.jaw, R.string.saw, R.string.zaw,
			R.string.yaw, R.string.daw, R.string.thaw, R.string.toaw,
			R.string.taw, R.string.naw, R.string.baw, R.string.bpaw,
			R.string.phaw, R.string.faw, R.string.poaw, R.string.foaw,
			R.string.maw, R.string.yoaw, R.string.law, R.string.waw,
			R.string.haw, R.string.aw, R.string.hoaw };
	int[] vowelImg = { R.drawable.blackboard, R.drawable.fish,
			R.drawable.shrimppaste, R.drawable.bananaflower,
			R.drawable.pumpkin, R.drawable.hand, R.drawable.bucket,
			R.drawable.crab, R.drawable.table, R.drawable.watermelon,
			R.drawable.kick, R.drawable.hammock, R.drawable.lamb,
			R.drawable.trumpet, R.drawable.island, R.drawable.laoviolin,
			R.drawable.notavailable, R.drawable.bat, R.drawable.nolies,
			R.drawable.cow, R.drawable.notavailable, R.drawable.boat,
			R.drawable.dirty, R.drawable.pho, R.drawable.leaves,
			R.drawable.fire, R.drawable.pole, R.drawable.blackant };
	int[] word = { R.string.blackboard, R.string.fish, R.string.shrimppaste,
			R.string.bananaflower, R.string.pumpkin, R.string.hand,
			R.string.bucket, R.string.crab, R.string.table,
			R.string.watermelon, R.string.kick, R.string.hammock,
			R.string.lamb, R.string.trumpet, R.string.island,
			R.string.laoviolin, R.string.slap, R.string.bat, R.string.nolie,
			R.string.cow, R.string.stucco, R.string.boat, R.string.dirty,
			R.string.pho, R.string.leave, R.string.fire, R.string.pole,
			R.string.blackant };

	int[] sound = { R.raw.blackboard, R.raw.fish_vowel, R.raw.shrimppaste,
			R.raw.bananaflower, R.raw.pumpkin, R.raw.hand, R.raw.bucket,
			R.raw.crab, R.raw.table, R.raw.watermelon, R.raw.kick,
			R.raw.hammock, R.raw.lamb, R.raw.trumpet, R.raw.island,
			R.raw.laoviolin, R.raw.slap, R.raw.bat, R.raw.nolie,
			R.raw.cow_vowel, R.raw.stucco, R.raw.boat, R.raw.dirty, R.raw.pho,
			R.raw.leave, R.raw.fire_vowel, R.raw.pole, R.raw.blackant };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get saved or default position.
		curPosition = getSharedPreferences().getInt(VOWEL_POSITION,
				DEFAULT_POSITION);

		setContentView(R.layout.vowel);

		vowelTv = (TextView) findViewById(R.id.vowelTv);
		vowelTv.setTypeface(getLaoTypeface());

		vowelSoundTv = (TextView) findViewById(R.id.vowelSoundTv);

		wordIs = (ImageSwitcher) findViewById(R.id.wordIs);
		wordIs.setFactory(this);
		Animation in = AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in);
		Animation out = AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out);
		wordIs.setInAnimation(in);
		wordIs.setOutAnimation(out);

		wordTv = (TextView) findViewById(R.id.wordTv);
		wordTv.setTypeface(getLaoTypeface());

		gridview = (GridView) findViewById(R.id.vowelsoundview);

		adapter = new VowelSoundAdapter(this);
		gridview.setAdapter(adapter);

		previousBt = (Button) findViewById(R.id.previousBt);
		nextBt = (Button) findViewById(R.id.nextBt);

		// Add gesture detector for fling motions.
		addGestureDector();

		displayVowel(curPosition);

	}

	/**
	 * Display previous vowel.
	 * 
	 * @param view
	 */
	public void previousVowel(View view) {
		if (curPosition > 0) {
			displayVowel(--curPosition);
			playAudio(null);
		}
	}

	/**
	 * Delay 1s to allow the rendering to complete before playing the audio.
	 * TODO: Need to figure out way to detect when the the rendering is
	 * completed.
	 */
	@Override
	protected long getAudioDelay() {
		return AUDIO_DELAY;
	}

	/**
	 * Display next vowel.
	 * 
	 * @param view
	 */
	public void nextVowel(View view) {
		if (curPosition < vowel.length - 1) {
			displayVowel(++curPosition);
			playAudio(null);
		}
	}

	/**
	 * Display a vowel at the specified position.
	 * 
	 * @param position
	 */
	private void displayVowel(int position) {

		// Enable/Disable buttons
		previousBt.setClickable(position > 0);
		nextBt.setClickable(position < vowel.length - 1);

		// Split comma delimited of the vowel and its sounding
		String[] values = getString(vowel[position]).split(",");
		vowelTv.setText(values[0]);
		vowelSoundTv.setText(values[1]);
		wordIs.setImageResource(vowelImg[position]);
		wordTv.setText(word[position]);
		adapter.setVowel(values[0]);
		adapter.setVowelSound(values[1].replaceFirst(" \\(.*\\)", ""));
		gridview.invalidateViews();
	}

	/**
	 * ViewFactory method to create ImageView for ImageSwitcher.
	 */
	public View makeView() {
		ImageView iv = new ImageView(this);
		iv.setScaleType(ScaleType.CENTER);
		return iv;
	}

	/**
	 * Save the position of current vowel in preference.
	 */
	@Override
	protected void saveSharedPreferences(Editor ed) {
		super.saveSharedPreferences(ed);

		// Save current position (progress)
		ed.putInt(VOWEL_POSITION, curPosition);
	}

	/**
	 * Create audio for corresponding vowel.
	 */
	@Override
	protected MediaPlayer createMediaPlayer() {
		MediaPlayer mp = null;
		if (curPosition < sound.length) {
			mp = MediaPlayer.create(VowelActivity.this, sound[curPosition]);
		}
		return mp;
	}

	/**
	 * Display next vowel on left swipe.
	 */
	@Override
	protected void swipeLeft() {
		nextVowel(null);
	}

	/**
	 * Display previous vowel on right swipe.
	 */
	@Override
	protected void swipeRight() {
		previousVowel(null);
	}
}