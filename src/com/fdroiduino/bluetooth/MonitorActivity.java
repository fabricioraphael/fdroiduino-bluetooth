package com.fdroiduino.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import com.fdroiduino.bluetooth.util.Application;
import com.fdroiduino.bluetooth.util.SamplesUtils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class MonitorActivity extends MyActivity {
	//TODO apagar depois
	private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	private static final int REQUEST_DISCOVERY = 0x1;;
	private final String TAG = "MonitorActivity";
	private Handler _handler = new Handler();
	private final int maxlength = 2048;
	private BluetoothDevice device = null;
	private BluetoothSocket socket = null;
	private EditText sEditText;
	private TextView sTextView;
	private OutputStream outputStream;
	private InputStream inputStream;
	CheckBox cbxHexSend;
	CheckBox cbxHexView;
	CheckBox cbxNewLine;
	private Object obj1 = new Object();
	private Object obj2 = new Object();
	public static boolean canRead = true;

	public static StringBuffer hexString = new StringBuffer();
	ScrollView mScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.monitor);
		
		cbxHexSend = (CheckBox) findViewById(R.id.cbxHexSend);
		cbxHexView = (CheckBox) findViewById(R.id.cbxHexView);
		cbxNewLine = (CheckBox) findViewById(R.id.cbxNewLine);
		sTextView = (TextView) findViewById(R.id.sTextView);
		sEditText = (EditText) findViewById(R.id.sEditText);
		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
		
		cbxHexView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					sTextView.setText(hexString.toString());
				} else {
					String str = bufferStrToHex(hexString.toString(), false);
					if (!str.startsWith("-->") && !str.startsWith("<--")
							&& str.length() > 0) {
						str = str.substring(2);
					}
					sTextView.setText(str);
				}
			}
		});

		cbxHexSend.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					cbxNewLine.setChecked(false);
					cbxNewLine.setEnabled(false);
				} else {
					cbxNewLine.setChecked(true);
					cbxNewLine.setEnabled(true);
				}
			}
		});

		BluetoothDevice finalDevice = this.getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		
		SocketApplication app = (SocketApplication) getApplicationContext();
		device = app.getDevice();
		Log.i(Application.TAG, "Device: " + device);
		
		if (finalDevice == null) {
			Log.i(Application.TAG, "finalDevice == NULL!!! =/ ");
			if (device == null) {
				Log.i(Application.TAG, "device == null fudeu");
				Intent intent = new Intent(this, SearchDeviceActivity.class);
				startActivity(intent);
				finish();
				return;
			}else
				Log.i(Application.TAG, "device != null =S");
		} else if (finalDevice != null) {
			Log.i(Application.TAG, "finalDevice != null ");
			app.setDevice(finalDevice);
			device = app.getDevice();
		}
		
		new Thread() {
			public void run() {
				Log.i(Application.TAG, "To na thread... ");
				connect(device);
			};
		}.start();
	}

	public void onButtonClicksend(View view) {
		String editText = sEditText.getText().toString();
		String tempHex = "";
		byte bytes[] = editText.getBytes();
		
		if (cbxHexSend.isChecked()) {
			try {
				bytes = SamplesUtils.hexToByte(editText);
			} catch (Exception e) {
				Log.e(Application.TAG, "Cagou... " + e.getMessage());
				e.printStackTrace();
				Toast.makeText(getBaseContext(),getResources().getString(R.string.number),Toast.LENGTH_SHORT).show();
				return;
			}
		}
		try {
			if (outputStream != null) {
				synchronized (obj2) {
					Log.i(Application.TAG, "Žeeee... " + outputStream);
					outputStream.write(bytes);
				}
			} else {
				Log.i(Application.TAG, "aff... " + outputStream);
				Toast.makeText(getBaseContext(),getResources().getString(R.string.wait),Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			Log.e(TAG, ">>", e);
			e.printStackTrace();
		}
		
		new Thread(){
			public void run(){
				String editText = sEditText.getText().toString();
				String tempHex = "";
				if (cbxHexSend.isChecked()) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < editText.length(); i = i + 2) {
						sb.append(editText.substring(i, i + 2)).append(" ");
					}
					tempHex = sb.toString();
				} else {
					tempHex = SamplesUtils.stringToHex(editText);
					if (cbxNewLine.isChecked()) {
						tempHex += " 0d 0a ";
					}
				}
				String hex = hexString.toString();
				if (hex == "") {
					hexString.append("-->");
				} else {
					if (hex.lastIndexOf("-->") < hex.lastIndexOf("<--")) {
						hexString.append("\n-->");
					}
				}
				hexString.append(tempHex);
				hex = hexString.toString();
				if (hex.length() > maxlength) {
					try {
						hex = hex.substring(hex.length() - maxlength, hex.length());
						hex = hex.substring(hex.indexOf(" "));
						hex = "-->" + hex;
						hexString = new StringBuffer();
						hexString.append(hex);
					} catch (Exception e) {
						e.printStackTrace();
						Log.e(TAG, "e", e);
					}
				}
				final String showStr;
				if (cbxHexView.isChecked()) {
					showStr=hexString.toString();
				} else {
					showStr=bufferStrToHex(hexString.toString(),false).trim();
				}
				_handler.post(new Runnable(){
					public void run() {
						sTextView.setText(showStr);
						Log.d(TAG, "ScrollY: " + mScrollView.getScrollY());   
			            int off = sTextView.getMeasuredHeight() - mScrollView.getHeight();   
			            if (off > 0) {   
			                mScrollView.scrollTo(0, off);   
			            }                          
					}
				});
			}
		}.start();
	}

	public void onButtonClickclear(View view) throws IOException {
		hexString = new StringBuffer();
		sTextView.setText(hexString.toString());
	}

	/* after select, connect to device */
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
				socket.close();
			}
		} catch (IOException e) {
			Log.e(TAG, ">>", e);
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
					
					Log.i(Application.TAG, "Vamos rodar: " + read);
					
					if (read > 0) {
						Log.i(Application.TAG, "Dentro: " + read);
						final int count = read;
						String str = SamplesUtils.byteToHex(bytes, count);
						String hex = hexString.toString();
						if (hex == "") {
							hexString.append("<--");
						} else {
							if (hex.lastIndexOf("<--") < hex.lastIndexOf("-->")) {
								hexString.append("\n<--");
							}
						}
						hexString.append(str);
						hex = hexString.toString();
						if (hex.length() > maxlength) {
							try {
								hex = hex.substring(hex.length() - maxlength,hex.length());
								hex = hex.substring(hex.indexOf(" "));
								hex = "<--" + hex;
								hexString = new StringBuffer();
								hexString.append(hex);
							} catch (Exception e) {
								e.printStackTrace();
								Log.e(Application.TAG, "fudeu: ", e);
							}
						}
						_handler.post(new Runnable() {
							public void run() {
								if (cbxHexView.isChecked()) {
									sTextView.setText(hexString.toString());
									Log.i(Application.TAG, "setText 1: " + hexString.toString());
								} else {
									Log.i(Application.TAG, "setText 2: " + bufferStrToHex(hexString.toString(), false).trim());
									sTextView.setText(bufferStrToHex(hexString.toString(), false).trim());
								}
								Log.d(Application.TAG, "ScrollY: " + mScrollView.getScrollY());   
					            int off = sTextView.getMeasuredHeight() - mScrollView.getHeight();   
					            if (off > 0) {
					                mScrollView.scrollTo(0, off);   
					            }    
							}
						});
					}
				}
			}

		} catch (Exception e) {
			Log.e(Application.TAG, "N‹o conectou essa bosta Error: ", e);
			Toast.makeText(getBaseContext(),getResources().getString(R.string.ioexception),Toast.LENGTH_SHORT).show();
			return;
		} finally {
			if (socket != null) {
				try {
					Log.d(Application.TAG, ">>Client Socket Close");
					socket.close();
					socket = null;
					this.finish();
					return;
				} catch (IOException e) {
					Log.e(Application.TAG, "N‹o fechou tb esse coco. Error: ", e);
				}
			}
		}
	}

	public String bufferStrToHex(String buffer, boolean flag) {
		String all = buffer;
		StringBuffer sb = new StringBuffer();
		String[] ones = all.split("<--");
		for (int i = 0; i < ones.length; i++) {
			if (ones[i] != "") {
				String[] twos = ones[i].split("-->");
				for (int j = 0; j < twos.length; j++) {
					if (twos[j] != "") {
						if (flag) {
							sb.append(SamplesUtils.stringToHex(twos[j]));
						} else {
							sb.append(SamplesUtils.hexToString(twos[j]));
						}
						if (j != twos.length - 1) {
							if (sb.toString() != "") {
								sb.append("\n");
							}
							sb.append("-->");
						}
					}
				}
				if (i != ones.length - 1) {
					if (sb.toString() != "") {
						sb.append("\n");
					}
					sb.append("<--");
				}
			}
		}
		return sb.toString();
	}

}