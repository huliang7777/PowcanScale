package com.powcanscale.ui.profile;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.powcanscale.R;

/**
 * 动画参考：http://cyrilmottier.com/2014/05/20/custom-animations-with-fragments/
 * @author Administrator
 *
 */
public class ProfileActivity extends Activity {

	private static final int ANIMATION_DURATION = 500;
	private static final TimeInterpolator ANIMATION_INTERPOLATOR = new AccelerateDecelerateInterpolator();
	
	private FrameLayout mMainContainer;
	private FrameLayout mEditModeContainer;
	private int mHalfHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		mMainContainer = (FrameLayout) findViewById(R.id.main_container);
		mEditModeContainer = (FrameLayout) findViewById(R.id.edit_mode_container);
		mHalfHeight = mEditModeContainer.getMeasuredHeight() / 2;

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

		public FormFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
			return rootView;
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
		v.animate().translationY(0).alpha(1).setDuration(animated ? ANIMATION_DURATION : 0).setInterpolator(ANIMATION_INTERPOLATOR);
	}

	private void stickTo(View v, View viewToStickTo, boolean animated) {

		v.getDrawingRect(mTmpRect);
		mMainContainer.offsetDescendantRectToMyCoords(v, mTmpRect);

		v.animate().translationY(viewToStickTo.getHeight() - mTmpRect.top).setDuration(animated ? ANIMATION_DURATION : 0).setInterpolator(ANIMATION_INTERPOLATOR).start();
	}

}
