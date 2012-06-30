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

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Adapter to display a list of word for a given vowel and various alphabet
 * (consonant).
 * 
 */
public class VowelSoundAdapter extends BaseAdapter {
	private final Typeface tf;
	private final Context mContext;
	String vowel;
	String vowelSound;
	char[] alphabets = { 0x0E81, 0x0E94, 0x0E95, 0x0E99, 0x0E9A, 0x0E9B,
			0x0EA1, 0x0EAD, 0x0EA5, 0x0EA7, 0x0E9C, 0x0EAA };
	String[] alphabetSounds = { "g-", "d-", "th-", "n-", "b-", "bp-", "m-", "",
			"l-", "w-", "p-", "s-" };

	public VowelSoundAdapter(LaoBaseActivity c) {
		mContext = c;
		tf = c.getLaoTypeface();
	}

	public int getCount() {
		return alphabets.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout layout;
		TextView textView;
		TextView textView2;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes

			layout = new LinearLayout(mContext);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setBackgroundResource(R.drawable.view_border);

			textView = new TextView(mContext);
			textView.setTypeface(tf);
			textView.setTextSize(30);
			textView.setGravity(Gravity.CENTER);
			layout.addView(textView);

			textView2 = new TextView(mContext);
			textView2.setTypeface(tf);
			textView2.setTextSize(12);
			textView2.setGravity(Gravity.CENTER);
			layout.addView(textView2);

		} else {
			layout = (LinearLayout) convertView;

			textView = (TextView) layout.getChildAt(0);
			textView2 = (TextView) layout.getChildAt(1);
		}

		textView.setText(vowel.replace('x', alphabets[position]));
		textView2.setText(alphabetSounds[position] + vowelSound);
		return layout;
	}

	public void setVowel(String string) {
		vowel = string;

	}

	public void setVowelSound(String string) {
		vowelSound = string;
	}

	/**
	 * Override to disable the click event
	 */
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	/**
	 * Override to disable the click event
	 */
	@Override
	public boolean isEnabled(int position) {
		return false;
	}

}
