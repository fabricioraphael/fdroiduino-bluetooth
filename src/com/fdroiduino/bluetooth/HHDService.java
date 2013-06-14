package com.fdroiduino.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class HHDService {
//    // Debugging
//    private static final String TAG = "HHDService";
//    private static final boolean D = true;
//
//    boolean isRequestSend = true;
//
//    // Unique UUID for this application
//    //private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//
//    private BluetoothSocket mmSocket = null;
//    private InputStream mmInStream = null;
//    private OutputStream mmOutStream = null;
//
//    // Member fields
//    private final BluetoothAdapter mAdapter;
//    private Handler mHandler;
//    //private AcceptThread mAcceptThread;
//    private ConnectThread mConnectThread;
//    private ConnectedThread mConnectedThread;
//    private int mState;
//
//    private long waitTime = 15000;
//
//    // Constants that indicate the current connection state
//    public static final int STATE_NONE = 0; // we're doing nothing
//    public static final int STATE_LISTEN = 1; // now listening for incoming
//                                                // connections
//    public static final int STATE_CONNECTING = 2; // now initiating an outgoing
//                                                    // connection
//    public static final int STATE_CONNECTED = 3; // now connected to a remote
//                                                    // device
//
//    public static final int STATE_SLEEP = 4; // now connected to a remote
//    // device
//
//    private Context mContext;
//
//    /**
//     * Constructor. Prepares a new BluetoothChat session.
//     * 
//     * @param context
//     *            The UI Activity Context
//     * @param handler
//     *            A Handler to send messages back to the UI Activity
//     */
//    public HHDBapiBTService(Context context, Handler handler) {
//        mAdapter = BluetoothAdapter.getDefaultAdapter();
//        mState = STATE_NONE;
//        mHandler = handler;
//        mContext = context;
//    }
//
//    /**
//     * Set the current state of the chat connection
//     * 
//     * @param state
//     *            An integer defining the current connection state
//     */
//    public synchronized void setState(int state) {
//        if (D)
//            Log.d(TAG, "setState() " + mState + " -> " + state);
//        mState = state;
//
//        // Give the new state to the Handler so the UI Activity can update
//        mHandler.obtainMessage(GlobalValues.MESSAGE_STATE_CHANGE, state, -1)
//                .sendToTarget();
//    }
//
//    /**
//     * Return the current connection state.
//     */
//    public synchronized int getState() {
//        return mState;
//    }
//
//    public void setHandler(Handler handler) {
//        mHandler = handler;
//    }
//
//    /**
//     * Start the chat service. Specifically start AcceptThread to begin a
//     * session in listening (server) mode. Called by the Activity onResume()
//     */
//    public synchronized void start() {
//        if (D)
//            Log.d(TAG, "start");
//
//        // Cancel any thread attempting to make a connection
//        if (mConnectThread != null) {
//            mConnectThread.cancel(true);
//            mConnectThread = null;
//        }
//
//        // Cancel any thread currently running a connection
//        if (mConnectedThread != null) {
//            mConnectedThread.cancel();
//            mConnectedThread = null;
//        }
//
//        setState(STATE_LISTEN);
//    }
//
//    /**
//     * Start the ConnectThread to initiate a connection to a remote device.
//     * 
//     * @param device
//     *            The BluetoothDevice to connect
//     */
//    public synchronized void connect(BluetoothDevice device) {
//        if (D)
//            Log.d(TAG, "connect to: " + device);
//
//        // Cancel any thread attempting to make a connection
//        if (mState == STATE_CONNECTING) {
//            if (mConnectThread != null) {
//                mConnectThread.cancel();
//                mConnectThread = null;
//            }
//        }
//
//        // Cancel any thread currently running a connection
//        if (mConnectedThread != null) {
//            mConnectedThread.cancel();
//            mConnectedThread = null;
//        }
//
//        // Start the thread to connect with the given device
//        mConnectThread = new ConnectThread(device);
//        mConnectThread.start();
//        setState(STATE_CONNECTING);
//    }
//
//    /**
//     * Start the ConnectedThread to begin managing a Bluetooth connection
//     * 
//     * @param socket
//     *            The BluetoothSocket on which the connection was made
//     * @param device
//     *            The BluetoothDevice that has been connected
//     */
//    public synchronized void connected(BluetoothSocket socket,
//            BluetoothDevice device) {
//        if (D)
//            Log.d(TAG, "connected");
//
//        // Cancel the thread that completed the connection
//        if (mConnectThread != null) {
//            mConnectThread.cancel();
//            mConnectThread = null;
//        }
//
//        // Cancel any thread currently running a connection
//        if (mConnectedThread != null) {
//            mConnectedThread.cancel();
//            mConnectedThread = null;
//        }
//
//        // Start the thread to manage the connection and perform transmissions
//        mConnectedThread = new ConnectedThread(socket);
//        mConnectedThread.start();
//
//        setState(STATE_CONNECTED);
//    }
//
//    /**
//     * Stop all threads
//     */
//    public synchronized void stop() {
//        if (D)
//            Log.d(TAG, "stop");
//        if (mConnectThread != null) {
//            mConnectThread.cancel();
//            mConnectThread = null;
//        }
//        if (mConnectedThread != null) {
//            mConnectedThread.cancel();
//            mConnectedThread = null;
//        }
//
//        setState(STATE_NONE);
//    }
//
//    /**
//     * Write to the ConnectedThread in an unsynchronized manner
//     * 
//     * @param out
//     *            The bytes to write
//     * @see ConnectedThread#write(byte[])
//     */
//    public void write(byte[] out) {
//        if(getState() == STATE_SLEEP){
//            connectionLost();
//            return;
//        }
//        // Create temporary object
//        ConnectedThread r;
//        // Synchronize a copy of the ConnectedThread
//        synchronized (this) {
//            if (mState != STATE_CONNECTED)
//                return;
//            r = mConnectedThread;
//
//        }
//        // Perform the write unsynchronized
//        r.write(out);
//    }
//
//    public void closeConnection(){
//        ConnectedThread r;
//        // Synchronize a copy of the ConnectedThread
//        synchronized (this) {
//            if (mState != STATE_CONNECTED)
//                return;
//            r = mConnectedThread;
//        }
//        r.cancel();
//    }
//
//    /**
//     * Indicate that the connection attempt failed and notify the UI Activity.
//     */
//    private void connectionFailed() {
//        if(getState() != STATE_LISTEN){
//            setState(STATE_LISTEN);
//
//        }
//
//    }
//
//    /**
//     * Indicate that the connection was lost and notify the UI Activity.
//     */
//    private void connectionLost() {
//        //setState(STATE_LISTEN);
//
//        // Send a failure message back to the Activity
//        setState(STATE_SLEEP);
//
//    }
//
//
//    /**
//     * This thread runs while attempting to make an outgoing connection with a
//     * device. It runs straight through; the connection either succeeds or
//     * fails.
//     */
//
//    /* private class ConnectThread extends Thread {
//            private final BluetoothSocket mmSocket;
//            private final BluetoothDevice mmDevice;
//
//            public ConnectThread(BluetoothDevice device) {
//                mmDevice = device;
//                BluetoothSocket tmp = null;
//
//                // Get a BluetoothSocket for a connection with the
//                // given BluetoothDevice
//                try {
//                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
//                } catch (IOException e) {
//                    Log.e(TAG, "create() failed", e);
//                }
//                mmSocket = tmp;
//            }
//
//            public void run() {
//                Log.i(TAG, "BEGIN mConnectThread");
//                setName("ConnectThread");
//
//                // Always cancel discovery because it will slow down a connection
//                mAdapter.cancelDiscovery();
//
//                // Make a connection to the BluetoothSocket
//                try {
//                    // This is a blocking call and will only return on a
//                    // successful connection or an exception
//                    mmSocket.connect();
//                } catch (IOException e) {
//                    connectionFailed();
//                    // Close the socket
//                    try {
//                        mmSocket.close();
//                    } catch (IOException e2) {
//                        Log.e(TAG, "unable to close() socket during connection failure", e2);
//                    }
//                    // Start the service over to restart listening mode
//                    HHDService.this.start();
//                    return;
//                }
//
//                // Reset the ConnectThread because we're done
//                synchronized (HHDService.this) {
//                    mConnectThread = null;
//                }
//
//                // Start the connected thread
//                connected(mmSocket, mmDevice);
//            }
//
//            public void cancel() {
//                try {
//                    mmSocket.close();
//                } catch (IOException e) {
//                    Log.e(TAG, "close() of connect socket failed", e);
//                }
//            }
//
//    }*/
//
//    private class ConnectThread extends Thread {
//        private BluetoothSocket mmSocket;
//        private final BluetoothDevice mmDevice;
//
//        public ConnectThread(BluetoothDevice device) {
//            mmDevice = device;
//        }
//
//        @SuppressLint("NewApi")
//        protected boolean simpleComm(Integer port) {
//            // byte [] inputBytes = null;
//
//            // The documents tell us to cancel the discovery process.
//            mAdapter.cancelDiscovery();
//
//            Log.d(this.toString(), "Port = " + port);
//            try {
//                Method m = mmDevice.getClass().getMethod("createRfcommSocket",new Class[] { int.class });
//                mmSocket = (BluetoothSocket) m.invoke(mmDevice, port);
//
//                // debug check to ensure socket was set.
//                assert (mmSocket != null) : "Socket is Null";
//
//                mAdapter.cancelDiscovery();
//                // attempt to connect to device
//                mmSocket.connect();
//                try {
//                    Log.d(this.toString(), "************ CONNECTION SUCCEES! *************");
//
//                    // Reset the ConnectThread because we're done
//                    synchronized (HHDService.this) {
//                        mConnectThread = null;
//                    }
//
//                    connected(mmSocket, mmDevice);
//                    return true;
//                } finally {
//                    // close the socket and we are done.
//                    // //mmSocket.close();
//                }
//                // IOExcecption is thrown if connect fails.
//            } catch (IOException ex) {
//                Log.e(this.toString(), "IOException " + ex.getMessage());
//                if (port == 256) {
//                    connectionFailed();
//                    try {
//                        mmSocket.close();
//                        mmSocket = null;
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                }
//            } catch (NoSuchMethodException ex) {
//                Log.e(this.toString(),
//                        "NoSuchMethodException " + ex.getMessage());
//            } catch (IllegalAccessException ex) {
//                Log.e(this.toString(),
//                        "IllegalAccessException " + ex.getMessage());
//            } catch (InvocationTargetException ex) {
//                Log.e(this.toString(),
//                        "InvocationTargetException " + ex.getMessage());
//            }catch (NullPointerException ex) {
//                Log.e(this.toString(),
//                        "NullPointerException " + ex.getMessage());
//            }
//            return false;
//        }
//
//        public void run() {
//            for (Integer port = 1; port <= 256; port++) {
//                if (simpleComm(Integer.valueOf(port)))
//                    break;
//            }
//
//        }
//
//        public void cancel() {
//             if (mmSocket != null) { 
//                 try { 
//                     mmSocket.close(); mmSocket = null;
//                 } catch (IOException e) { 
//                     Log.e(TAG, "close() of connect secure socket failed", e); 
//                 } 
//             }
//        }
//
//        public void cancel(boolean val) {
//             if (mmSocket != null) { 
//                 try { 
//                     mmSocket.close(); mmSocket = null;
//                 } catch (IOException e) { 
//                     Log.e(TAG, "close() of connect secure socket failed", e); 
//                 } 
//             }
//        }
//
//    }
//
//    /**
//     * This thread runs during a connection with a remote device. It handles all
//     * incoming and outgoing transmissions.
//     */
//    private class ConnectedThread extends Thread {
//
//        private Logger logger;
//
//        public ConnectedThread(BluetoothSocket socket) {
//            Log.d(TAG, "create ConnectedThread");
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            // Get the BluetoothSocket input and output streams
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) {
//                Log.e(TAG, "temp sockets not created", e);
//            }
//
//            logger = Logger.getInstance();
//
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//
//        }
//
//        @Override
//        public void run() {
//            Log.i(TAG, "BEGIN mConnectedThread");
//            byte[] buffer = new byte[1024];
//            int bytes;
//
//            byte commandByte = 00;
//
//            //Logic to parse incoming data
//
//        }; 
//
//    }

}