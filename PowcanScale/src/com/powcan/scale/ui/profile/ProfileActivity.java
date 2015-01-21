package com.powcan.scale.ui.profile;

import android.animation.TimeInterpolator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.powcan.scale.R;
import com.powcan.scale.ui.base.BaseActivity;

/**
 * 动画参考：http://cyrilmottier.com/2014/05/20/custom-animations-with-fragments/
 * @author Administrator
 */
public class ProfileActivity extends BaseActivity {

	private static final int ANIMATION_DURATION = 500;
	private static final TimeInterpolator ANIMATION_INTERPOLATOR = new AccelerateDecelerateInterpolator();
	
	private static FrameLayout mMainContainer;
	private static FrameLayout mNormalModeContainer;
	private static FrameLayout mEditModeContainer;
	private int mHalfHeight;
	
	private static FragmentManager mFragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		mMainContainer = (FrameLayout) findViewById(R.id.main_container);
		mNormalModeContainer = (FrameLayout) findViewById(R.id.normal_mode_container);
		mEditModeContainer = (FrameLayout) findViewById(R.id.edit_mode_container);
//		mHalfHeight = mEditModeContainer.getMeasuredHeight() / 2;
		mHalfHeight = 540;
		
		mFragmentManager = getFragmentManager();

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.form_container, new FormFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class FormFragment extends Fragment {
		
		private LinearLayout mMainContainer;

		public FormFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
			
			mMainContainer = (LinearLayout) rootView;
			
			final View height = rootView.findViewById(R.id.ll_height);
			height.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					focusOn(height, rootView, true);
					showEditModelPane();
				}
				
			});
			return rootView;
		}

		private final Rect mTmpRect = new Rect();

		private void focusOn(View v, View movableView, boolean animated) {

			v.getDrawingRect(mTmpRect);
			mMainContainer.offsetDescendantRectToMyCoords(v, mTmpRect);

			movableView.animate().translationY(-mTmpRect.top).setDuration(animated ? ANIMATION_DURATION : 0).setInterpolator(ANIMATION_INTERPOLATOR).start();
		}

	}
	
	public static void showEditModelPane() {
//		FragmentManager fragmentManager = getFragmentManager();
//		fragmentManager.beginTransaction().add(R.id.edit_mode_container, new HeightFragment()).commit();
		mFragmentManager.beginTransaction().add(R.id.form_container, new HeightFragment()).commit();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class HeightFragment extends Fragment {

		public HeightFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_height, container, false);
//			slideInToTop(rootView, true);
			return rootView;
		}

		private void slideInToTop(View v, boolean animated) {
			v.animate().translationY(0).alpha(1).setDuration(animated ? ANIMATION_DURATION : 0).setInterpolator(ANIMATION_INTERPOLATOR).start();
		}

	}

	private final Rect mTmpRect = new Rect();

	private void focusOn(View v, View movableView, boolean animated) {

		v.getDrawingRect(mTmpRect);
		mMainContainer.offsetDescendantRectToMyCoords(v, mTmpRect);

		movableView.animate().translationY(-mTmpRect.top).setDuration(animated ? ANIMATION_DURATION : 0).setInterpolator(ANIMATION_INTERPOLATOR).start();
	}

	private void fadeOutToBottom(View v, boolean animated) {
		v.animate().translationYBy(mHalfHeight).alpha(0).setDuration(animated ? ANIMATION_DURATION : 0).setInterpolator(ANIMATION_INTERPOLATOR).start();
	}

	private void slideInToTop(View v, boolean animated) {
		v.animate().translationY(0).alpha(1).setDuration(animated ? ANIMATION_DURATION : 0).setInterpolator(ANIMATION_INTERPOLATOR).start();
	}

	private void stickTo(View v, View viewToStickTo, boolean animated) {

		v.getDrawingRect(mTmpRect);
		mMainContainer.offsetDescendantRectToMyCoords(v, mTmpRect);

		v.animate().translationY(viewToStickTo.getHeight() - mTmpRect.top).setDuration(animated ? ANIMATION_DURATION : 0).setInterpolator(ANIMATION_INTERPOLATOR).start();
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFindViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitViewData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBindListener() {
		// TODO Auto-generated method stub
		
	}

}
