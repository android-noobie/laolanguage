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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * 
 * Activity for displaying Lao Alphabet.
 * 
 */
public class AlphabetActivity extends LaoBaseActivity implements ViewFactory {
	private static final int DEFAULT_POSITION = 0;
	private static final String ALPHABET_POSITION = "alphabet_position";
	TextView alphabetTv;
	TextView alphabetSoundTv;
	TextView wordTv;
	ImageSwitcher wordIs;
	int curPosition = 0;
	Button previousBt;
	Button nextBt;

	// Unicode for each alphabets
	char[] alphabet = { 0x0E81, 0x0E82, 0x0E84, 0x0E87, 0x0E88, 0x0EAA, 0x0E8A,
			0x0E8D, 0x0E94, 0x0E95, 0x0E96, 0x0E97, 0x0E99, 0x0E9A, 0x0E9B,
			0x0E9C, 0x0E9D, 0x0E9E, 0x0E9F, 0x0EA1, 0x0EA2, 0x0EA5, 0x0EA7,
			0x0EAB, 0x0EAD, 0x0EAE };
	int[] alphabetSound = { R.string.gaw, R.string.khaw, R.string.khoaw,
			R.string.ghaw, R.string.jaw, R.string.saw, R.string.zaw,
			R.string.yaw, R.string.daw, R.string.thaw, R.string.toaw,
			R.string.taw, R.string.naw, R.string.baw, R.string.bpaw,
			R.string.phaw, R.string.faw, R.string.poaw, R.string.foaw,
			R.string.maw, R.string.yoaw, R.string.law, R.string.waw,
			R.string.haw, R.string.aw, R.string.hoaw };
	int[] alphabetImg = { R.drawable.chicken, R.drawable.egg,
			R.drawable.buffalo, R.drawable.cow, R.drawable.plate,
			R.drawable.tiger, R.drawable.elephant, R.drawable.mosquito,
			R.drawable.kid, R.drawable.eye, R.drawable.bag, R.drawable.flag,
			R.drawable.bird, R.drawable.goat, R.drawable.fish, R.drawable.bee,
			R.drawable.rain, R.drawable.mountain, R.drawable.fire,
			R.drawable.cat, R.drawable.medicine, R.drawable.monkey,
			R.drawable.fan, R.drawable.goose, R.drawable.bowl, R.drawable.house };
	int[] word = { R.string.chicken, R.string.egg, R.string.buffalo,
			R.string.cow, R.string.plate, R.string.tiger, R.string.elephant,
			R.string.mosquito, R.string.kid, R.string.eye, R.string.bag,
			R.string.flag, R.string.bird, R.string.goat, R.string.fish,
			R.string.bee, R.string.rain, R.string.mountain, R.string.fire,
			R.string.cat, R.string.medicine, R.string.monkey, R.string.fan,
			R.string.goose, R.string.bowl, R.string.house };

	int[] sound = { R.raw.chicken, R.raw.egg, R.raw.buffalo, R.raw.cow,
			R.raw.plate, R.raw.tiger, R.raw.elephant, R.raw.mosquito,
			R.raw.kid, R.raw.eye, R.raw.bag, R.raw.flag, R.raw.bird,
			R.raw.goat, R.raw.fish, R.raw.bee, R.raw.rain, R.raw.mountain,
			R.raw.fire, R.raw.cat, R.raw.medicine, R.raw.monkey, R.raw.fan,
			R.raw.goose, R.raw.bowl, R.raw.house };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get saved or default position.
		curPosition = getSharedPreferences().getInt(ALPHABET_POSITION,
				DEFAULT_POSITION);

		setContentView(R.layout.alphabet);

		alphabetTv = (TextView) findViewById(R.id.alphabetTv);
		alphabetTv.setTypeface(getLaoTypeface());

		alphabetSoundTv = (TextView) findViewById(R.id.alphabetSoundTv);

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

		previousBt = (Button) findViewById(R.id.previousBt);
		nextBt = (Button) findViewById(R.id.nextBt);

		// Add gesture detector for fling motions.
		addGestureDector();

		displayAlphabet(curPosition);

	}

	/**
	 * Display previous alphabet.
	 * 
	 * @param view
	 */
	public void previousAlphabet(View view) {
		if (curPosition > 0) {
			displayAlphabet(--curPosition);
			playAudio(null);
		}
	}

	/**
	 * Create audio for corresponding alphabet.
	 */
	@Override
	protected MediaPlayer createMediaPlayer() {
		MediaPlayer mp = null;
		if (curPosition < sound.length) {
			mp = MediaPlayer.create(AlphabetActivity.this, sound[curPosition]);
		}
		return mp;
	}

	/**
	 * Display next alphabet.
	 * 
	 * @param view
	 */
	public void nextAlphabet(View view) {
		if (curPosition < alphabet.length - 1) {
			displayAlphabet(++curPosition);
			playAudio(null);
		}
	}

	/**
	 * Display alphabet at specified position.
	 * 
	 * @param position
	 */
	private void displayAlphabet(int position) {

		// Enable/Disable buttons
		previousBt.setClickable(position > 0);
		nextBt.setClickable(position < alphabet.length - 1);

		alphabetTv.setText(String.valueOf(alphabet[position]));
		alphabetSoundTv.setText(alphabetSound[position]);
		wordIs.setImageResource(alphabetImg[position]);
		wordTv.setText(word[position]);
	}

	/**
	 * Save the position of current alphabet in preference.
	 */
	@Override
	protected void saveSharedPreferences(Editor ed) {
		super.saveSharedPreferences(ed);

		// Save current position (progress)
		ed.putInt(ALPHABET_POSITION, curPosition);
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
	 * Show next alphabet on left swipe.
	 */
	@Override
	protected void swipeLeft() {
		nextAlphabet(null);
	}

	/**
	 * Show previous alphabet on right swipe.
	 */
	@Override
	protected void swipeRight() {
		previousAlphabet(null);
	}

}