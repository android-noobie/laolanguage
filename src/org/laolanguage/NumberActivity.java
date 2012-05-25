package org.laolanguage;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NumberActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView textview = new TextView(this);
        textview.setText("Coming soon! Stay tune...");
        setContentView(textview);
	}
}
