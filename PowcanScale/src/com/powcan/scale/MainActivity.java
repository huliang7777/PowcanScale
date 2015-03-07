package com.powcan.scale;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.powcan.scale.bean.http.BaseResponse;
import com.powcan.scale.bean.http.GATRequest;
import com.powcan.scale.bean.http.GATResponse;
import com.powcan.scale.bean.http.LGNRequest;
import com.powcan.scale.bean.http.LGNResponse;
import com.powcan.scale.bean.http.REGRequest;
import com.powcan.scale.net.NetRequest;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.ui.fragment.CenterFragment;
import com.powcan.scale.ui.fragment.CenterFragment.OnViewPagerChangeListener;
import com.powcan.scale.ui.fragment.LeftFragment;
import com.powcan.scale.ui.fragment.LeftFragment.NavigationDrawerCallbacks;
import com.powcan.scale.ui.fragment.RightFragment;
import com.powcan.scale.util.Md5Utils;
import com.powcan.scale.util.Utils;
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
					GATRequest gat = new GATRequest(6);
					GATResponse response = (GATResponse) NetRequest.getInstance(getActivity()).send(gat, GATResponse.class);
					if (response != null) {
						List<Integer> noList = response.BKH;
						if (noList != null && noList.size() > 0) {
							Integer account = noList.get(1);
							int height = 178;
							
							REGRequest reg = new REGRequest();
							reg.account = account + "";
							reg.pswd = Md5Utils.encryptMD5("123456");
							reg.imei = Utils.getDeviceId(getActivity());
							reg.gender = "M";
							reg.birthday = "19880722";
							reg.height = height + "";
							reg.phone = "13424269212";
							reg.qq = "442666876";
							reg.email = "442666876@qq.com";
							
							BaseResponse regResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(reg, BaseResponse.class);
							if (regResponse != null && regResponse.RES == 201) {
								mSpUtil.setLogin(true);
							}
						}
					}
				} else {
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
