package com.fdroiduino.bluetooth.views;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fdroiduino.bluetooth.R;
import com.fdroiduino.bluetooth.SocketApplication;
import com.fdroiduino.bluetooth.util.Application;
import com.fdroiduino.bluetooth.util.SamplesUtils;


public class MonitorActivity extends Activity {
	//TODO checar esse ID
	private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	private static final int REQUEST_DISCOVERY = 0x1;;
	private final String TAG = "MonitorActivity";
	private final int maxlength = 2048;
	private BluetoothDevice device = null;
	private BluetoothSocket socket = null;
	private EditText sEditText;
	private OutputStream outputStream;
	private InputStream inputStream;

	private Object obj1 = new Object();
	private Object obj2 = new Object();
	public static boolean canRead = true;
	
	private ImageButton mButtonTop;
	private ImageButton mButtonBot;
	private ImageButton mButtonLeft;
	private ImageButton mButtonRight;
	
	private int mTop=0, mBot=0, mLeft=0, mRight=0; 

	public static StringBuffer hexString = new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.monitor);
		
		sEditText = (EditText) findViewById(R.id.sEditText);
		mButtonTop = (ImageButton) findViewById(R.id.btn_go_top);
		mButtonBot = (ImageButton) findViewById(R.id.btn_go_bot);
		mButtonLeft = (ImageButton) findViewById(R.id.btn_go_left);
		mButtonRight = (ImageButton) findViewById(R.id.btn_go_right);
		
		BluetoothDevice finalDevice = this.getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		
		SocketApplication app = (SocketApplication) getApplicationContext();
		device = app.getDevice();
		
		Log.i(Application.TAG, "Device: " + device);
		
		if (finalDevice == null) {
			if (device == null) {
				Log.w(Application.TAG, "Device is null. Going to SearchDeviceActivity.");
				Intent intent = new Intent(this, SearchDeviceActivity.class);
				startActivity(intent);
				finish();
				return;
			}else{
				Log.v(Application.TAG, "Device isn't null.");
			}
				
		} else if (finalDevice != null) {
			Log.v(Application.TAG, "Recovering the device...");
			app.setDevice(finalDevice);
			device = app.getDevice();
		}
		
		new Thread() {
			public void run() {
				Log.i(Application.TAG, "Beginning to try to connect.... ");
				connect(device);
			};
		}.start();
	}

	public void onButtonClicksend(View view) {
		String editText = sEditText.getText().toString();
		byte bytes[] = editText.getBytes();

		try {
			if (outputStream != null) {
				synchronized (obj2) {
					Log.i(Application.TAG, "Sending data to " + outputStream + " ...");
					outputStream.write(bytes);
				}
			} else {
				Log.i(Application.TAG, "Bluetooth is null! " + outputStream);
				Toast.makeText(getBaseContext(),getResources().getString(R.string.wait),Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			Log.e(TAG, "Error while sending data. error: " + e.getMessage());
		}
	}
	
	public void onButtonClickTop(View view) {
		byte bytes[] = "1".getBytes();
		
		if (mTop < 2){
			mTop++;
			mBot = 0;
			mButtonBot.setBackgroundResource(R.drawable.bot0);
		}
		
		switch (mTop) {
		case 1:
			mButtonTop.setBackgroundResource(R.drawable.top1);
			break;
		case 2:
			mButtonTop.setBackgroundResource(R.drawable.top2);
			break;
		}

		try {
			if (outputStream != null) {
				synchronized (obj2) {
					outputStream.write(bytes);
				}
			}
		} catch (IOException e) {}
	}
	
	public void onButtonClickBot(View view) {
		byte bytes[] = "2".getBytes();
		
		if (mBot < 2){
			mBot++;
			mTop = 0;
			mButtonTop.setBackgroundResource(R.drawable.top0);
		}
		
		switch (mBot) {
		case 1:
			mButtonBot.setBackgroundResource(R.drawable.bot1);
			break;
		case 2:
			mButtonBot.setBackgroundResource(R.drawable.bot2);
			break;
		}

		try {
			if (outputStream != null) {
				synchronized (obj2) {
					outputStream.write(bytes);
				}
			}
		} catch (IOException e) {}
	}
	
	public void onButtonClickLeft(View view) {
		byte bytes[] = "0".getBytes();
		
		if (mLeft < 2){
			mLeft++;
			mRight = 0;
			mButtonRight.setBackgroundResource(R.drawable.right0);
		}
		
		switch (mLeft) {
		case 1:
			mButtonLeft.setBackgroundResource(R.drawable.left1);
			break;
		case 2:
			mButtonLeft.setBackgroundResource(R.drawable.left2);
			break;
		}

		try {
			if (outputStream != null) {
				synchronized (obj2) {
					outputStream.write(bytes);
				}
			}
		} catch (IOException e) {}
	}
	
	public void onButtonClickRight(View view) {
		byte bytes[] = "0".getBytes();
		
		if (mRight < 2){
			mRight++;
			mLeft = 0;
			mButtonLeft.setBackgroundResource(R.drawable.left0);
		}
		
		switch (mRight) {
		case 1:
			mButtonRight.setBackgroundResource(R.drawable.right1);
			break;
		case 2:
			mButtonRight.setBackgroundResource(R.drawable.right2);
			break;
		}

		try {
			if (outputStream != null) {
				synchronized (obj2) {
					outputStream.write(bytes);
				}
			}
		} catch (IOException e) {}
	}
	
	public void onButtonClickStop(View view) {
		byte bytes[] = "2".getBytes();
		
		mTop = 0;
		mBot = 0;
		mLeft = 0;
		mRight = 0;
		
		mButtonTop.setBackgroundResource(R.drawable.top0);
		mButtonBot.setBackgroundResource(R.drawable.bot0);
		mButtonLeft.setBackgroundResource(R.drawable.left0);
		mButtonRight.setBackgroundResource(R.drawable.right0);

		try {
			if (outputStream != null) {
				synchronized (obj2) {
					outputStream.write(bytes);
				}
			}
		} catch (IOException e) {}
	}
	
	

	public void onButtonClickclear(View view) throws IOException {
		sEditText.setText("");
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != REQUEST_DISCOVERY) {
			finish();
			return;
		}
		if (resultCode != RESULT_OK) {
			finish();
			return;
		}
		
		final BluetoothDevice device = data.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		
		new Thread() {
			public void run() {
				connect(device);
			};
		}.start();
	}

	protected void onDestroy() {
		super.onDestroy();
		
		try {
			if (socket != null) {
				Log.w(Application.TAG, "onDestroy: closing socket.");
				socket.close();
			}
		} catch (IOException e) {
			Log.e(Application.TAG, "onDestroy error: " + e.getMessage());
		}
	}

	protected void connect(BluetoothDevice device) {
		try {
			Log.i(Application.TAG, "Connecting... ");
			
			BluetoothSocket tmp = null;
			try {
			    tmp = device.createRfcommSocketToServiceRecord(SPP_UUID);

			    // for others devices
			    // Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});

			    // for fucker galaxy tab 2 |m|_
			    Method m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[] {int.class});
			    
			    tmp = (BluetoothSocket) m.invoke(device, 1);
			} catch (IOException e) {
			    Log.e(Application.TAG, "create() failed" + e.getMessage());
			}
			socket = tmp;
			
			socket.connect();
			
			Log.i(Application.TAG, "Client Connected!!!");
			
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			
			int read = -1;
			final byte[] bytes = new byte[2048];
			
			while (true) {
				synchronized (obj1) {
					read = inputStream.read(bytes);
					
					if (read > 0) {
						Log.i(Application.TAG, "BEGGINING ==> reading data: " + read);

						final int count = read;
						
						String str = SamplesUtils.byteToHex(bytes, count);
						
						String hex = hexString.toString();
						
						hexString.append(str);
						
						hex = hexString.toString();
						if (hex.length() > maxlength) {
							try {
								hex = hex.substring(hex.length() - maxlength, hex.length());
								hex = hex.substring(hex.indexOf(" "));
								
								hexString = new StringBuffer();
								hexString.append(hex);
								
								Log.i(Application.TAG, "DATA RECEIVED FROM BLUETOOTH: " + hexString);
							} catch (Exception e) {
								Log.e(Application.TAG, "Error in cutting a data received. error: " + e.getMessage());
							}
						}
					}
				}
			}

		} catch (Exception e) {
			Log.e(Application.TAG, "Connection error: " + e.getMessage());
			Toast.makeText(getBaseContext(),getResources().getString(R.string.ioexception),Toast.LENGTH_SHORT).show();
			return;
		} finally {
			if (socket != null) {
				try {
					socket.close();
					socket = null;
					Log.w(Application.TAG, "Closing Socket...");
					this.finish();
					return;
				} catch (IOException e) {
					Log.e(Application.TAG, "Connection close error: " + e.getMessage());
				}
			}
		}
	}
}