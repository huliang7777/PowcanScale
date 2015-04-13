package com.powcan.scale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.powcan.scale.bean.CurUserInfo;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.bean.http.LGNResponse;
import com.powcan.scale.bean.http.RECRequest;
import com.powcan.scale.ble.BluetoothLeService;
import com.powcan.scale.ble.SampleGattAttributes;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.net.NetRequest;
import com.powcan.scale.ui.LoginActivity;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.ui.fragment.CenterFragment;
import com.powcan.scale.ui.fragment.CenterFragment.OnViewPagerChangeListener;
import com.powcan.scale.ui.fragment.LeftFragment;
import com.powcan.scale.ui.fragment.LeftFragment.NavigationDrawerCallbacks;
import com.powcan.scale.ui.fragment.RightFragment;
import com.powcan.scale.util.SpUtil;
import com.powcan.scale.util.Utils;
import com.powcan.scale.widget.SlidingMenu;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseActivity implements NavigationDrawerCallbacks {
	
	protected static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
	private static final int SENSOR_SHAKE = 10; 
	
	private SlidingMenu mSlidingMenu;
	private LeftFragment mLeftFragment;
	private RightFragment mRightFragment;
	private CenterFragment mCenterFragment;
	
	private Vibrator vibrator;
	private SensorManager mSensorManager;
	
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothDevice> mLeDevices = new ArrayList<BluetoothDevice>();
    private boolean mScanning;
    private BluetoothLeService mBluetoothLeService;
    
    private String mDeviceName;
    private String mDeviceAddress;
    
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private boolean isDataUpdate = false;

	private UserInfo curUser;
	private UserInfoDb dbUserInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		UmengUpdateAgent.update(this);
		MobclickAgent.updateOnlineConfig(this);
		
		dbUserInfo = new UserInfoDb( this );
	}

	@Override
	public void onInit() {
		// Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
		
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public void onFindViews() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.sm);
	}

	@Override
	public void onInitViewData() {
//		int width = UiHelper.getDisplayMetrics(this).widthPixels;
//		width = width > 250 ? (width / 2 > 250 ? width / 2 : width) : width;
		int width = 280;

		View leftView = View.inflate(this, R.layout.frame_left, null);
		View rightView = View.inflate(this, R.layout.frame_right, null);
		View centerView = View.inflate(this, R.layout.frame_center, null);

		mSlidingMenu.setView(leftView, rightView, centerView, width, width);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		mLeftFragment = new LeftFragment();
		transaction.replace(R.id.fl_left, mLeftFragment);
		mRightFragment = new RightFragment();
		transaction.replace(R.id.fl_right, mRightFragment);
		mCenterFragment = new CenterFragment();
		transaction.replace(R.id.fl_center, mCenterFragment);
		transaction.commit();

	}

	@Override
	public void onBindListener() {
		mCenterFragment.setOnViewPagerChangeListener(new OnViewPagerChangeListener() {

			@Override
			public void onPageChage(int position) {
				if (mCenterFragment.isFirst()) {
					mSlidingMenu.setWhichSideCanShow(true, false);
					// } else if (mCenterFragment.isLast()) {
					// mSlidingMenu.setWhichSideCanShow(false, true);
				} else {
					mSlidingMenu.setWhichSideCanShow(false, false);
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerSensor();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
        
        CurUserInfo.getInstance( this ).reloadUserInfo();
        reloadData();
	}

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

	private void registerSensor() {
		mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(sensorEventListener);
        unregisterReceiver(mGattUpdateReceiver);
	}

	public void showLeftViewToogle() {
		mSlidingMenu.showLeftViewToogle();
	}

	public void showRightViewToogle() {
		mSlidingMenu.showRightViewToogle();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position, Object obj) 
	{
		if( obj != null )
		{
			String account = (String)obj;
			curUser = dbUserInfo.getUserInfo( account );
			SpUtil.getInstance( this ).reset();
			SpUtil.getInstance( this ).saveCurrUser( curUser );
			reloadData();
		}
	}
	
	/**
     * 动作执行
     */ 
    private Handler mHandler = new Handler() { 
 
        @Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            switch (msg.what) { 
            case SENSOR_SHAKE: 
                Toast.makeText(MainActivity.this, "检测到摇晃，执行操作！", Toast.LENGTH_SHORT).show(); 
                Log.i(TAG, "检测到摇晃，执行操作！"); 
                
                // unbindService( mServiceConnection );
                
                mLeDevices.clear();
                scanLeDevice(true);
                break; 
            } 
        } 

        private void scanLeDevice(final boolean enable) {
            if (enable) {
                // Stops scanning after a pre-defined scan period.
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }, SCAN_PERIOD);

                mScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
    }; 

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	if (device == null || TextUtils.isEmpty(device.getName())) return;
                	
                	if ("Healthcare".equals(device.getName())) {
                		mLeDevices.add(device);
                		
                        mDeviceName = device.getName();
                        mDeviceAddress = device.getAddress();

                        Intent gattServiceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
                        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                        
	                    if (mScanning) {
	                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
	                        mScanning = false;
	                    }
                	}
                }
            });
        }
    };

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                mSpUtil.checkOnlineParams(context);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA),
                		intent.getStringExtra(BluetoothLeService.EXTRA_DATA2));
            }
        }
    };

    private void displayData(byte[] data, String hex) {
    	Log.d(TAG, "display Data: " + Arrays.toString(data));
        if (hex != null) 
        {
        	Log.d(TAG, "display hex: " + hex);
        	hex = hex.replaceAll(" ", "");
        	if ( hex.length() >=14 && data[4] == 0 )
        	{
        		isDataUpdate = true;
        	}
        	else if( isDataUpdate && hex.length() >=34 && hex.startsWith("081101B1020109")  )
        	{
        		isDataUpdate = false;
        		// 换算成KG
        		String weightHexStr = hex.substring(22, 26);
        		String bodyFatRateHexStr = hex.substring(26, 30);
        		String waterContentHexStr = hex.substring(30, 34);
        		
        		final float weight = (float)Integer.parseInt(weightHexStr, 16) / 200;
        		final float bodyFatRate = (float)Integer.parseInt(bodyFatRateHexStr, 16) / 10;
        		final float waterContent = (float)Integer.parseInt(waterContentHexStr, 16) / 10;
        		
        		Log.d(TAG, "display weight-bodyFatRate-waterContent: " + weight + "-" + bodyFatRate + "-" + waterContent );
        		mCenterFragment.setWeightData( weight, bodyFatRate, waterContent );
        	}
        }
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCenterFragment.updateConnectionState(resourceId);
//                BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(UUID.fromString(SampleGattAttributes.GATT_SERVICES), properties, permissions);
//                mBluetoothLeService.writeCharacteristic(characteristic);
//                mBluetoothLeService.writeCharacteristic(null);
            }
        });
    }
    
    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            if (SampleGattAttributes.isHealthcareServices(uuid)) {
	            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
	
	            // Loops through available Characteristics.
	            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
	                uuid = gattCharacteristic.getUuid().toString();
	                if (SampleGattAttributes.isHealthcareMeasurement(uuid)) {
	                	final int charaProp = gattCharacteristic.getProperties();
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            // If there is an active notification on a characteristic, clear
                            // it first so it doesn't update the data field on the user interface.
                            if (mNotifyCharacteristic != null) {
                                mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
                                mNotifyCharacteristic = null;
                            }
                            mBluetoothLeService.readCharacteristic(gattCharacteristic);
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = gattCharacteristic;
                            mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                        }
	                }
	            }
	        	break;
	        }
        }
    }
    
    private SensorEventListener sensorEventListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// 传感器信息改变时执行该方法 
            float[] values = event.values; 
            float x = values[0]; // x轴方向的重力加速度，向右为正 
            float y = values[1]; // y轴方向的重力加速度，向前为正 
            float z = values[2]; // z轴方向的重力加速度，向上为正 
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。 
            int medumValue = 12;// 三星 i9250、魅族怎么晃都不会超过15，没办法，只设置12了 
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) { 
                Log.i(TAG, "x轴方向的重力加速度" + x +  "；y轴方向的重力加速度" + y +  "；z轴方向的重力加速度" + z); 
                vibrator.vibrate(200); 
                Message msg = new Message(); 
                msg.what = SENSOR_SHAKE; 
                mHandler.sendMessage(msg); 				
            }
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
    	if(keyCode == KeyEvent.KEYCODE_BACK)
		{
    		PowcanScaleApplication.getInstance().exit();
		}
    	
    	return super.onKeyDown(keyCode, event);
    }
    
    public void reloadData()
    {
    	mCenterFragment.reloadData();
    	mLeftFragment.reloadData();
    }
}
