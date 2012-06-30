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
 * Adapter to create a view for each number.
 * 
 * @author Santi Anousaya
 * 
 */
public class NumberAdapter extends BaseAdapter {
	private final Context mContext;
	boolean isTraditionalMode = false;
	Typeface tf;
	// references to our images
	char[] traditional_numbers = { 0x0ED1, 0x0ED2, 0x0ED3, 0x0ED4, 0x0ED5,
			0x0ED6, 0x0ED7, 0x0ED8, 0x0ED9, ' ', 0x0ED0, ' ' };
	int[] numberSounds = { R.string.one, R.string.two, R.string.three,
			R.string.four, R.string.five, R.string.six, R.string.seven,
			R.string.eight, R.string.nine, R.string.zero };

	public NumberAdapter(LaoBaseActivity c) {
		mContext = c;
		tf = c.getLaoTypeface();
	}

	public int getCount() {
		return traditional_numbers.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		// Typeface tf = Typeface.createFromAsset(mContext.getAssets(),
		// "fonts/LaoUI.ttf");
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
			textView.setTextSize(38);
			textView.setGravity(Gravity.CENTER);
			layout.addView(textView);

			textView2 = new TextView(mContext);
			textView2.setTypeface(tf);
			textView2.setTextSize(14);
			textView2.setGravity(Gravity.CENTER);
			layout.addView(textView2);

		} else {
			layout = (LinearLayout) convertView;

			textView = (TextView) layout.getChildAt(0);
			textView2 = (TextView) layout.getChildAt(1);
		}

		if (isTraditionalMode) {
			textView.setText(String.valueOf(traditional_numbers[position]));
		} else if (position < 9) {
			textView.setText(String.valueOf(position + 1));
		} else if (position == 10) {
			textView.setText(String.valueOf(0));
		} else {
			textView.setText("");
		}

		if (position < 9) {
			textView2.setText(numberSounds[position]);
		} else if (position == 10) {
			textView2.setText(numberSounds[9]);
		} else {
			textView2.setText("");
		}

		return layout;
	}

	/**
	 * Set traditiona number mode.
	 * 
	 * @param traditionalMode
	 */
	public void setTraditionalMode(boolean traditionalMode) {
		isTraditionalMode = traditionalMode;
	}

	/**
	 * Override to disable click event for some items.
	 */
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	/**
	 * Override to only enable the click event for number items.
	 */
	@Override
	public boolean isEnabled(int position) {
		return position < 9 || position == 10;
	}
}
