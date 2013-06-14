package com.fdroiduino.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.fdroiduino.bluetooth.views.AboutActivity;
import com.fdroiduino.bluetooth.views.MonitorActivity;
import com.fdroiduino.bluetooth.views.SearchDeviceActivity;

public class MainActivity extends Activity{
	private ImageView logo;
	private Button btnSearchDevice;
	private Button btnMonitor;
	private Button btnAbout;
	private Button btnExit;

	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		_bluetooth.enable();
		
		logo = (ImageView) findViewById(R.id.logo);
		logo.setClickable(true);
		logo.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					Uri uri = Uri.parse(getResources().getString(R.string.uri));
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
			}
		});

		btnSearchDevice = (Button) findViewById(R.id.btnSearchDevice);
		btnSearchDevice.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SearchDeviceActivity.class);
				startActivity(intent);
			}
		});

		btnMonitor = (Button) findViewById(R.id.btnMonitor);
		btnMonitor.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				_bluetooth.enable();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MonitorActivity.class);
				startActivity(intent);
			}
		});

		btnAbout = (Button) findViewById(R.id.btnAbout);
		btnAbout.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AboutActivity.class);
				startActivity(intent);
			}
		});

		btnExit = (Button) findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
			return false;
		}
		return false;
	}

	protected void dialog() {
		AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
		build.setTitle(R.string.message);
		build.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				if (_bluetooth.isEnabled()) {
					_bluetooth.disable();
				}
				
				SocketApplication app = (SocketApplication) getApplicationContext();
				app.setDevice(null);
				MainActivity.this.finish();
			}
		});
		build.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		build.create().show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}
}
