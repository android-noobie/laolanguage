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

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TabHost;

/**
 * Display TabView of "Alphabet", "Vowel", "Number", and "Preference" tabs.
 * NOTE: For backward compatibility, TabActivity is used instead of Fragment.
 * 
 */
public class LanguageTabWidget extends TabActivity {
	private static final String LAST_ACCESSED_TAB = "last_accessed_tab";
	SharedPreferences mPrefs;
	TabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources(); // Resource object to get Drawables
		tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Alphabet
		intent = new Intent().setClass(this, AlphabetActivity.class);
		spec = tabHost
				.newTabSpec("alphabet")
				.setIndicator(getString(R.string.tab_alphabet),
						res.getDrawable(R.drawable.ic_tab_alphabet))
				.setContent(intent);
		tabHost.addTab(spec);

		// Vowel tab
		intent = new Intent().setClass(this, VowelActivity.class);
		spec = tabHost
				.newTabSpec("vowel")
				.setIndicator(getString(R.string.tab_vowel),
						res.getDrawable(R.drawable.ic_tab_vowel))
				.setContent(intent);
		tabHost.addTab(spec);

		// Number tab
		intent = new Intent().setClass(this, NumberActivity.class);
		spec = tabHost
				.newTabSpec("number")
				.setIndicator(getString(R.string.tab_number),
						res.getDrawable(R.drawable.ic_tab_number))
				.setContent(intent);
		tabHost.addTab(spec);

		// preference tab
		intent = new Intent().setClass(this, PreferenceActivity.class);
		spec = tabHost
				.newTabSpec("preference")
				.setIndicator(getString(R.string.tab_preference),
						res.getDrawable(R.drawable.ic_tab_preference))
				.setContent(intent);
		tabHost.addTab(spec);
		mPrefs = getSharedPreferences(getString(R.string.app_name), 0);

		tabHost.setCurrentTab(mPrefs.getInt(LAST_ACCESSED_TAB, 0));
	}

	/**
	 * Capture MotionEvent and delegate it to current tab.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return getCurrentActivity().onTouchEvent(event);
	}

	/**
	 * Save current tab.
	 */
	@Override
	protected void onPause() {
		super.onPause();

		// Save current position (progress)
		SharedPreferences.Editor ed = mPrefs.edit();
		ed.putInt(LAST_ACCESSED_TAB, tabHost.getCurrentTab());
		ed.commit();
	}

}
