package com.fdroiduino.bluetooth.views;

import android.app.Activity;

public class SettingActivity extends Activity {
//	Spinner spLanguage;
//	Button btnReturn;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.setting);
//
//		spLanguage = (Spinner) findViewById(R.id.spLanguage);
//		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//				this, R.array.language, android.R.layout.simple_spinner_item);
//		adapter
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spLanguage.setAdapter(adapter);
//		Locale locale = Locale.getDefault();
//		if (locale.equals(Locale.CHINA)) {
//			spLanguage.setSelection(1);
//		} else {
//			spLanguage.setSelection(0);
//		}
//		spLanguage
//				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
//
//					public void onItemSelected(AdapterView<?> parent, View v,
//							int position, long id) {
//						Locale locale2;
//						Configuration config = new Configuration();
//						if (position == 0) {
//							locale2 = Locale.ENGLISH;
//							Locale.setDefault(locale2);
//							config.locale = locale2;
//						} else if (position == 1) {
//							locale2 = Locale.CHINA;
//							Locale.setDefault(locale2);
//							config.locale = locale2;
//
//						}
//						getBaseContext().getResources().updateConfiguration(
//								config,
//								getBaseContext().getResources()
//										.getDisplayMetrics());
//						SharedPreferences settings = getSharedPreferences(
//								"locale", 0);
//						settings.edit().putInt("Locale", position).commit();
//					}
//
//					public void onNothingSelected(AdapterView<?> arg0) {
//
//					}
//				});
//
//		btnReturn = (Button) findViewById(R.id.btnReturn);
//		btnReturn.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				finish();
//			}
//		});
//	}
}