package com.powcan.scale;

import java.util.ArrayList;
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
import android.view.View;
import android.widget.Toast;

import com.powcan.scale.bean.http.LGNRequest;
import com.powcan.scale.bean.http.LGNResponse;
import com.powcan.scale.ble.BluetoothLeService;
import com.powcan.scale.ble.SampleGattAttributes;
import com.powcan.scale.net.NetRequest;
import com.powcan.scale.ui.LoginActivity;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.ui.fragment.CenterFragment;
import com.powcan.scale.ui.fragment.CenterFragment.OnViewPagerChangeListener;
import com.powcan.scale.ui.fragment.LeftFragment;
import com.powcan.scale.ui.fragment.LeftFragment.NavigationDrawerCallbacks;
import com.powcan.scale.ui.fragment.RightFragment;
import com.powcan.scale.ui.profile.ProfileActivity;
import com.powcan.scale.ui.settings.SettingsActivity;
import com.powcan.scale.util.Md5Utils;
import com.powcan.scale.widget.SlidingMenu;

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
    private ArrayList<BluetoothDevice> mLeDevices;
    private boolean mScanning;
    private BluetoothLeService mBluetoothLeService;
    
    private String mDeviceName;
    private String mDeviceAddress;
    
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public void onInit() {
		// Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
//        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
//            finish();
//        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
//        final BluetoothManager bluetoothManager =
//                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
//        if (mBluetoothAdapter == null) {
//            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
		
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		new Thread(){
			public void run() {
				if (!mSpUtil.isLogin()) {
//					GATRequest gat = new GATRequest(6);
//					GATResponse response = (GATResponse) NetRequest.getInstance(getActivity()).send(gat, GATResponse.class);
//					if (response != null) {
//						List<Integer> noList = response.BKH;
//						if (noList != null && noList.size() > 0) {
//							Integer account = noList.get(1);
//							int height = 178;
//							
//							REGRequest reg = new REGRequest();
//							reg.account = account + "";
//							reg.pswd = Md5Utils.encryptMD5("123456");
//							reg.imei = Utils.getDeviceId(getActivity());
//							reg.gender = "M";
//							reg.birthday = "19880722";
//							reg.height = height + "";
//							reg.phone = "13424269212";
//							reg.qq = "442666876";
//							reg.email = "442666876@qq.com";
//							
//							BaseResponse regResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(reg, BaseResponse.class);
//							if (regResponse != null && regResponse.RES == 201) {
//								mSpUtil.setLogin(true);
//							}
//						}
//					}
//				} else {
					LGNRequest request = new LGNRequest();
					request.number = "2001978";
					request.pswd = Md5Utils.encryptMD5("123456");
					
					LGNResponse response = NetRequest.getInstance(getActivity()).send(request, LGNResponse.class);
					if (response != null && response.RES == 301) {
						mSpUtil.setLogin(true);
						//mSpUtil.setAccount(response.ACT);
						mSpUtil.setGender(response.GDR);
						mSpUtil.setBirthday(response.GDR);
						//mSpUtil.setHeight(response.HET);
						mSpUtil.setPhone(response.PHN);
						mSpUtil.setQQ(response.QQN);
						mSpUtil.setEmail(response.EML);
					}
				} else {
//					int account = mSpUtil.getAccount();
//					int height = 180;
//					
//					UTURequest utu = new UTURequest();
//					utu.account = account + "";
//					utu.pswd = Md5Utils.encryptMD5("123456");
//					utu.imei = Utils.getDeviceId(getActivity());
//					utu.gender = "M";
//					utu.birthday = "19880722";
//					utu.height = height + "";
//					utu.phone = "13424269212";
//					utu.qq = "442666876";
//					utu.email = "442666876@qq.com";
//					
//					BaseResponse regResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(utu, BaseResponse.class);
//					if (regResponse != null && regResponse.RES == 401) {
//						showToast("更新资料成功");
//					}
					
//					GTNRequest gtn = new GTNRequest();
////					gtn.number = "13424269212";
////					gtn.type = "phone";
//					gtn.number = "442666876";
//					gtn.type = "qq";
////					gtn.number = "442666876@qq.com";
////					gtn.type = "email";
//					
//					GTNResponse gtnResponse = (GTNResponse) NetRequest.getInstance(getActivity()).send(gtn, GTNResponse.class);
//					if (gtnResponse != null && gtnResponse.RES == 501) {
//						showToast("获取成功");
//					}
					
//					int account = mSpUtil.getAccount();
//					
//					FBPRequest fbp = new FBPRequest();
//					fbp.account = account + "";
//					fbp.number = "442666876";
//					fbp.type = "qq";
//					
//					BaseResponse fbpResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(fbp, BaseResponse.class);
//					if (fbpResponse != null && fbpResponse.RES == 601) {
////						showToast("验证成功，允许修改密码");
//					}
					
//					int account = mSpUtil.getAccount();
//					
//					UPPRequest upp = new UPPRequest();
//					upp.account = account + "";
//					upp.password = Md5Utils.encryptMD5("654321");
//					
//					BaseResponse uppResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(upp, BaseResponse.class);
					
//					int account = mSpUtil.getAccount();
//					
//					LGNRequest request = new LGNRequest();
//					request.number = account + "";
//					request.pswd = Md5Utils.encryptMD5("654321");
//					
//					LGNResponse response = NetRequest.getInstance(getActivity()).send(request, LGNResponse.class);
					
//					int account = mSpUtil.getAccount();
//					
//					CHPRequest chp = new CHPRequest();
//					chp.account = account + "";
//					chp.oldpsw = Md5Utils.encryptMD5("654321");
//					chp.newpsw = Md5Utils.encryptMD5("123456");
//					
//					BaseResponse chpResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(chp, BaseResponse.class);
					
//					int account = mSpUtil.getAccount();
//					
//					LGNRequest request = new LGNRequest();
//					request.number = account + "";
//					request.pswd = Md5Utils.encryptMD5("123456");
//					
//					LGNResponse response = NetRequest.getInstance(getActivity()).send(request, LGNResponse.class);
					
//					int account = mSpUtil.getAccount();
//					
//					RECRequest request = new RECRequest();
//					request.account = account + "";
//					request.weight = "11.1";
//					request.fat = "11.123456";
//					request.water = "11.1";
//					request.muscle = "11.1";
//					request.bone = "11.1";
//					request.bmr = "11.1";
//					request.sfat = "11.1";
//					request.infat = "11.1";
//					request.bodyage = "11.1";
//					request.amr = "11.1";
//					
//					LGNResponse response = NetRequest.getInstance(getActivity()).send(request, LGNResponse.class);
				}
			};
		}.start();
	}

	@Override
	public void onFindViews() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.sm);
	}

	@Override
	public void onInitViewData() {
//		int width = UiHelper.getDisplayMetrics(this).widthPixels;
//		width = width > 250 ? (width / 2 > 250 ? width / 2 : width) : width;
		int width = 350;

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
//        if (!mBluetoothAdapter.isEnabled()) {
//            if (!mBluetoothAdapter.isEnabled()) {
//                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//            }
//        }
        
//        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
//        if (mBluetoothLeService != null) {
//            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
//            Log.d(TAG, "Connect request result=" + result);
//        }
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
//        unregisterReceiver(mGattUpdateReceiver);
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
		Intent intent = new Intent( this, LoginActivity.class );
        intent.putExtra( "account", (String)obj );
        startActivity(intent);
        finish();
	}
	
	/**
     * 动作执行
     */ 
//    private Handler mHandler = new Handler() { 
// 
//        @Override 
//        public void handleMessage(Message msg) { 
//            super.handleMessage(msg); 
//            switch (msg.what) { 
//            case SENSOR_SHAKE: 
//                Toast.makeText(MainActivity.this, "检测到摇晃，执行操作！", Toast.LENGTH_SHORT).show(); 
//                Log.i(TAG, "检测到摇晃，执行操作！"); 
//
//                mLeDevices.clear();
//                scanLeDevice(true);
//                break; 
//            } 
//        } 
//
//        private void scanLeDevice(final boolean enable) {
//            if (enable) {
//                // Stops scanning after a pre-defined scan period.
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mScanning = false;
//                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                    }
//                }, SCAN_PERIOD);
//
//                mScanning = true;
//                mBluetoothAdapter.startLeScan(mLeScanCallback);
//            } else {
//                mScanning = false;
//                mBluetoothAdapter.stopLeScan(mLeScanCallback);
//            }
//        }
//    }; 

//    // Device scan callback.
//    private BluetoothAdapter.LeScanCallback mLeScanCallback =
//            new BluetoothAdapter.LeScanCallback() {
//
//        @Override
//        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                	if (device == null || TextUtils.isEmpty(device.getName())) return;
//                	
//                	if ("Healthcare".equals(device.getName())) {
//                		mLeDevices.add(device);
//                		
//                        mDeviceName = device.getName();
//                        mDeviceAddress = device.getAddress();
//
//                        Intent gattServiceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
//                        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
//                        
//	                    if (mScanning) {
//	                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
//	                        mScanning = false;
//	                    }
//                	}
//                }
//            });
//        }
//    };

    // Code to manage Service lifecycle.
//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
//            if (!mBluetoothLeService.initialize()) {
//                Log.e(TAG, "Unable to initialize Bluetooth");
//                finish();
//            }
//            // Automatically connects to the device upon successful start-up initialization.
//            mBluetoothLeService.connect(mDeviceAddress);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            mBluetoothLeService = null;
//        }
//    };

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
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private void displayData(String data) {
        if (data != null) {
        	Log.d(TAG, "displayData: " + data);
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
//                mHandler.sendMessage(msg); 				
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

}
