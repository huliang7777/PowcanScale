package com.powcan.scale;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.powcan.scale.bean.http.LGNRequest;
import com.powcan.scale.bean.http.LGNResponse;
import com.powcan.scale.bean.http.RECRequest;
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
	
	private SlidingMenu mSlidingMenu;
	private LeftFragment mLeftFragment;
	private RightFragment mRightFragment;
	private CenterFragment mCenterFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public void onInit() {
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

	public void showLeftViewToogle() {
		mSlidingMenu.showLeftViewToogle();
	}

	public void showRightViewToogle() {
		mSlidingMenu.showRightViewToogle();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position, Object obj) {
		
	}

}
