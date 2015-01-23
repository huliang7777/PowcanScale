package com.powcan.scale;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.luckymonkey.util.UiHelper;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.ui.fragment.CenterFragment;
import com.powcan.scale.ui.fragment.CenterFragment.OnViewPagerChangeListener;
import com.powcan.scale.ui.fragment.LeftFragment;
import com.powcan.scale.ui.fragment.LeftFragment.NavigationDrawerCallbacks;
import com.powcan.scale.ui.fragment.RightFragment;
import com.powcan.scale.widget.SlidingMenu;

public class MainActivity extends BaseActivity implements NavigationDrawerCallbacks {
	
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

	}

	@Override
	public void onFindViews() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.sm);
	}

	@Override
	public void onInitViewData() {
		int width = UiHelper.getDisplayMetrics(this).widthPixels;
		width = width > 250 ? (width / 2 > 250 ? width / 2 : width) : width;

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
