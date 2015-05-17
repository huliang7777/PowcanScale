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
import android.os.AsyncTask;
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
import com.powcan.scale.bean.MeasureResult;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.bean.http.DNRRequest;
import com.powcan.scale.bean.http.DNRResponse;
import com.powcan.scale.bean.http.GNTRequest;
import com.powcan.scale.bean.http.GNTResponse;
import com.powcan.scale.bean.http.LGNResponse;
import com.powcan.scale.bean.http.RECRequest;
import com.powcan.scale.ble.BluetoothLeService;
import com.powcan.scale.ble.SampleGattAttributes;
import com.powcan.scale.db.MeasureResultDb;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.dialog.LoadingDialog;
import com.powcan.scale.net.NetRequest;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.ui.fragment.CenterFragment;
import com.powcan.scale.ui.fragment.CenterFragment.OnViewPagerChangeListener;
import com.powcan.scale.ui.fragment.LeftFragment;
import com.powcan.scale.ui.fragment.LeftFragment.NavigationDrawerCallbacks;
import com.powcan.scale.ui.fragment.RightFragment;
import com.powcan.scale.util.SpUtil;
import com.powcan.scale.util.Utils;
import com.third.library.widget.SlidingMenu;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements NavigationDrawerCallbacks {
	
	protected static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
	private static final int SENSOR_SHAKE = 10;
	private static final int GET_LAST_DATAS = 0x0001; 
	private static final int UPLOAD_LOCAL_DATAS = 0x0002; 
	
	private SlidingMenu mSlidingMenu;
	// 左边用户界面
	private LeftFragment mLeftFragment;
	// 右边界面
	private RightFragment mRightFragment;
	// 中间界面
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
	private MeasureResultDb dbMeasureResult;
	private boolean isDeal = false;
	private LoadingDialog mDialog;

	/**
	 * 界面创建回调方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		UmengUpdateAgent.update(this);
		MobclickAgent.updateOnlineConfig(this);
		
		dbUserInfo = new UserInfoDb( this );
		dbMeasureResult = new MeasureResultDb( this );
		mDialog = new LoadingDialog( this , "数据同步中..." );
		// 同步数据
		sychData();
	}

	/**
	 * 初始化方法
	 */
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
	
	/**
	 * 同步数据
	 */
	private void sychData()
	{
		mDialog.show();
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... arg0) 
			{
				String account = mSpUtil.getAccount();
				GNTRequest request = new GNTRequest();
				request.account = account;

				GNTResponse response = NetRequest.getInstance( MainActivity.this )
						.send(request, GNTResponse.class);
				if ( response != null && response.RES == 1201 )
				{
					String serverTime = response.SST;
					MeasureResult measure = dbMeasureResult.getLastMeasureResult( account );
					if ( measure != null && serverTime.compareTo( measure.getDateTime() ) < 0 )
					{
						return UPLOAD_LOCAL_DATAS + "," + serverTime;
					}
					else if ( measure != null && serverTime.compareTo( measure.getDateTime() ) > 0 )
					{
						return GET_LAST_DATAS + "," + measure.getDateTime();
					}
					else if ( measure == null )
					{
						return GET_LAST_DATAS + "," + "0000-00-00 00:00:00";
					}
				}
				
				return "";
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute( result );
				if ( !TextUtils.isEmpty( result ) )
				{
					String []array = result.split( "," );
					int flag = Integer.valueOf( array[ 0 ] );
					if ( flag == GET_LAST_DATAS ) 
					{
						Message msg = new Message();
						msg.what = GET_LAST_DATAS;
						msg.obj = array[ 1 ];
						mHandler.sendMessage( msg );
					}
					else if ( flag == UPLOAD_LOCAL_DATAS ) 
					{
						Message msg = new Message();
						msg.what = UPLOAD_LOCAL_DATAS;
						msg.obj = array[ 1 ];
						mHandler.sendMessage( msg );
					}
					else 
					{
						String msg = "数据同步失败，请重试！";
						showToastShort(msg);
						mDialog.hide();
						reloadData();
					}
				}
				else
				{
					mDialog.hide();
					reloadData();
				}
			}

		}.execute();
	}
	
	/**
	 * 查找控件
	 */
	@Override
	public void onFindViews() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.sm);
	}

	/**
	 * 初始化主界面数据
	 */
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

	/**
	 * 绑定事件监听
	 */
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
	
	/**
	 * 界面显示回调方法
	 */
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

	/**
	 * 蓝牙连接广播接收filter
	 * @return
	 */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    /**
     * 注册震动事件
     */
	private void registerSensor() {
		mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	/**
	 * 界面暂停回调方法
	 * 反注册各种事件监听
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(sensorEventListener);
        unregisterReceiver(mGattUpdateReceiver);
	}
	
	/**
	 * 显示左边view
	 */
	public void showLeftViewToogle() {
		mSlidingMenu.showLeftViewToogle();
	}

	/**
	 * 显示右边view
	 */
	public void showRightViewToogle() {
		mSlidingMenu.showRightViewToogle();
	}
	
	/**
	 * 界面选中回调处理
	 */
	@Override
	public void onNavigationDrawerItemSelected(int position, Object obj) 
	{
		if( obj != null )
		{
			String account = (String)obj;
			curUser = dbUserInfo.getUserInfo( account );
			SpUtil.getInstance( this ).reset();
			SpUtil.getInstance( this ).saveCurrUser( curUser );
			sychData();
		}
	}
	
	/**
     * 动作执行，进行蓝牙连接
     */ 
    private Handler mHandler = new Handler() { 
 
        @Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            switch (msg.what) { 
            case SENSOR_SHAKE: 
                Log.i(TAG, "检测到摇晃，执行操作！"); 
                
                if ( !isDeal )
                {
                	Toast.makeText(MainActivity.this, "检测到摇晃，执行操作！", Toast.LENGTH_SHORT).show(); 
                	isDeal = true;
                	if ( mConnected && mBluetoothLeService != null && mServiceConnection != null )
            		{
                		unbindService( mServiceConnection );
            			mBluetoothLeService.disconnect();
            			
            		}
                	mLeDevices.clear();
					scanLeDevice(true);
                }
                break; 
            case GET_LAST_DATAS:
            	String datetime = (String) msg.obj;
            	getLastDatas( datetime );
            	break;
            case UPLOAD_LOCAL_DATAS:
            	datetime = (String) msg.obj;
            	uploadLocalDatas( datetime );
            	break;
            } 
        } 

        /**
         * 扫描设备
         * @param enable 是否扫描
         */
        private void scanLeDevice(final boolean enable) {
            if (enable) {
                // Stops scanning after a pre-defined scan period.
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScanning = false;
                        isDeal = false;
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
    
    /**
     * 从服务器获取最小数据
     * @param datetime 时间
     */
    private void getLastDatas( final String datetime )
    {
    	new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... arg0) 
			{
				String account = mSpUtil.getAccount();
				DNRRequest request = new DNRRequest();
				request.account = account;
				request.from_time = datetime;
				request.to_time = Utils.getCurDateTime();

				DNRResponse response = NetRequest.getInstance(getActivity())
						.send(request, DNRResponse.class);
				if ( response != null && response.RES == 1001 ) 
				{
					Log.d(TAG, "记录下载成功");
					int num = response.NUM;
					String height = SpUtil.getInstance( MainActivity.this ).getHeight();
					MeasureResult measure = null;
					for ( int i=0;i<num;i++ )
					{
						measure = new MeasureResult();
						float weight = Float.valueOf( response.WET.get( i ) );
						float bmi = weight / ( (Float.valueOf( height ) / 100) * (Float.valueOf( height ) / 100) );
						bmi = (float)Math.round( bmi * 100 ) / 100;
						measure.setAccount( account );
						measure.setWeight( weight );
						measure.setBmi( bmi );
						measure.setWaterContent( Float.valueOf( response.WAT.get( i ) ) );
						measure.setBodyFatRate( Float.valueOf( response.FAT.get( i ) ) );
						measure.setDate( response.TIM.get( i ) );
						measure.setUpload( 1 );
						dbMeasureResult.insertMeasureResult( measure );
					}
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute( result );
				if ( result ) 
				{
					String msg = "数据同步成功！";
					showToastShort(msg);
				}
				else 
				{
					String msg = "数据同步失败，请重试！";
					showToastShort(msg);
				}
				mDialog.hide();
				reloadData();
			}

		}.execute();
    }
    
    /**
     * 更新本地数据
     * @param datetime
     */
    private void uploadLocalDatas( final String datetime )
    {
    	new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... arg0) 
			{
				String account = mSpUtil.getAccount();
				ArrayList<MeasureResult>  measureResults = dbMeasureResult.getMeasureResults( account, datetime, Utils.getCurDateTime(), "0" ); 
				int size = measureResults.size();
				for ( int i=0; i<size; i++ )
				{
					final MeasureResult result = measureResults.get(i);

					RECRequest request = new RECRequest();
					request.account = account;
					request.weight = "" + result.getWeight();
					request.fat = "" + result.getBodyFatRate();
					request.water = "" + result.getWaterContent();
					request.muscle = "0.0";
					request.bone = "0.0";
					request.bmr = "0.0";
					request.sfat = "0.0";
					request.infat = "0.0";
					request.bodyage = "0.0";
					request.amr = "0.0";
					request.measure_time = result.getDateTime();

					LGNResponse response = NetRequest
							.getInstance(getActivity()).send(request,
									LGNResponse.class);
					if ( response != null && response.RES == 901 ) 
					{
						Log.d( TAG, "数据上传成功");
						dbMeasureResult.updateMeasureResult(result.getId(), 1);
					}
				}
				
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute( result );
				if ( result ) 
				{
					String msg = "数据同步成功！";
					showToastShort(msg);
				}
				else 
				{
					String msg = "数据同步失败，请重试！";
					showToastShort(msg);
				}
				mDialog.hide();
				reloadData();
			}

		}.execute();
    }

    /**
     * 蓝牙连接回调方法处理接口类
     */
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

    /**
     * 蓝牙连接服务类
     */
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
    /**
     * 蓝牙状态广播监听类
     */
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
                isDeal = false;
            }
        }
    };
    
    /**
     * 获取脂肪称数据，显示到界面
     * @param data byte数组数据
     * @param hex 16进制数据
     */
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
        		reloadData();
        	}
        }
    }

    /**
     * 更新连接状态：断开，连接等
     * @param resourceId 字体资源id
     */
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
    /**
     * 读取蓝牙的数据
     * @param gattServices gat服务
     */
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
    
    /**
     * 手机摇晃监听事件处理类
     */
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
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) 
            { 
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

	/**
	 * 界面回调处理方法
	 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    /**
     * 监听返回键处理，连续按2次返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
    	if(keyCode == KeyEvent.KEYCODE_BACK)
		{
    		if(System.currentTimeMillis() - firstClickTime < 2000)
			{
    			PowcanScaleApplication.getInstance().exit();
				finish();
				return super.onKeyDown(keyCode, event);
			}
			else
			{
				showToast("再按一次退出程序...");
				firstClickTime = System.currentTimeMillis();
				return true;
			}
    		
		}
    	
    	return super.onKeyDown(keyCode, event);
    }
    
    /**
     * 加载数据
     */
    public void reloadData()
    {
    	mCenterFragment.reloadData();
    	mLeftFragment.reloadData();
    }
	
	private long firstClickTime = 0;

    /**
     * 界面注销回调方法
     */
    @Override
    protected void onDestroy() 
    {
    	super.onDestroy();
    	if ( mDialog != null )
    	{
    		mDialog.dismiss();
    		mDialog = null;
    	}
    }
}
