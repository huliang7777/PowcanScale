package com.powcan.scale;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.powcan.scale.bean.http.LGNRequest;
import com.powcan.scale.bean.http.LGNResponse;
import com.powcan.scale.net.NetRequest;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.ui.fragment.CenterFragment;
import com.powcan.scale.ui.fragment.CenterFragment.OnViewPagerChangeListener;
import com.powcan.scale.ui.fragment.LeftFragment;
import com.powcan.scale.ui.fragment.LeftFragment.NavigationDrawerCallbacks;
import com.powcan.scale.ui.fragment.RightFragment;
import com.powcan.scale.util.Md5Utils;
import com.powcan.scale.widget.SlidingMenu;

public class MainActivity extends BaseActivity implements NavigationDrawerCallbacks {
	
	protected static final String TAG = MainActivity.class.getSimpleName();
	
	private static final int SENSOR_SHAKE = 10; 
	
    private BluetoothAdapter mBluetoothAdapter;
	
	private SlidingMenu mSlidingMenu;
	private LeftFragment mLeftFragment;
	private RightFragment mRightFragment;
	private CenterFragment mCenterFragment;
	
	private Vibrator vibrator;
	private SensorManager mSensorManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
						mSpUtil.setAccount(response.ACT);
						mSpUtil.setGender(response.GDR);
						mSpUtil.setBirthday(response.GDR);
						mSpUtil.setHeight(response.HET);
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
		int width = 250;

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
	}

	private void registerSensor() {
		mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(sensorEventListener);
	}

	public void showLeftViewToogle() {
		mSlidingMenu.showLeftViewToogle();
	}

	public void showRightViewToogle() {
		mSlidingMenu.showRightViewToogle();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position, Object obj) {
		
	}
	
	/**
     * 动作执行
     */ 
    private Handler handler = new Handler() { 
 
        @Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            switch (msg.what) { 
            case SENSOR_SHAKE: 
                Toast.makeText(MainActivity.this, "检测到摇晃，执行操作！", Toast.LENGTH_SHORT).show(); 
                Log.i(TAG, "检测到摇晃，执行操作！"); 
                break; 
            } 
        } 
 
    }; 
    
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
                handler.sendMessage(msg); 				
            }
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

}
