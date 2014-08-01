package com.xml.parsing.demo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xml.parsing.demo.R;

/**
 * @author dipenp
 * 
 */
public class LauncherActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);

		((Button) findViewById(R.id.dom_parser_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(LauncherActivity.this, DomParsingActivity.class));
					}
				});

		((Button) findViewById(R.id.sax_parser_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(LauncherActivity.this, SaxParsingActivity.class));
					}
				});

		((Button) findViewById(R.id.xmlpullparser_parser_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(LauncherActivity.this, XMLPullParsingActivity.class));
					}
				});

	}

}
