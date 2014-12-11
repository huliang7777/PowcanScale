package com.powcanscale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.powcanscale.adapter.SectionsPagerAdapter;
import com.powcanscale.ui.base.BaseActivity;
import com.powcanscale.ui.settings.SettingsFragment;
import com.powcanscale.widget.RibbonMenuCallback;
import com.powcanscale.widget.RibbonMenuView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseActivity implements RibbonMenuCallback, OnClickListener {

	protected static final String TAG = MainActivity.class.getSimpleName();

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private RibbonMenuView rbmView;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		UmengUpdateAgent.update(this);
		MobclickAgent.updateOnlineConfig(this);

//		if (SpUtil.getInstance(this).isFirstLaunch()) {
//			Intent mainIntent = new Intent(this, LoginActivity.class);
//			startActivity(mainIntent);
//		}

		mTitle = getTitle();
	}

	@Override
	public void onInit() {
		setTitle("保康脂肪秤");
		setUp();
	}

	@Override
	public void onFindViews() {
		// Set up the drawer.
		rbmView = (RibbonMenuView) findViewById(R.id.ribbonMenuView1);
		rbmView.setMenuClickCallback(this);
		rbmView.setMenuItems(R.menu.ribbon_menu);
	}

	@Override
	public void onInitViewData() {
		
	}

	@Override
	public void onBindListener() {
		findViewById(R.id.iv_up).setOnClickListener(this);
	}

	@Override
	public void onRibbonMenuItemClick(int itemId) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		if (itemId != R.id.btn_settings) {
			fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(itemId + 1)).commit();
		} else {
			fragmentManager.beginTransaction().replace(R.id.container, SettingsFragment.newInstance(itemId + 1)).commit();
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_up:
			rbmView.toggleMenu();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * The {@link android.support.v4.view.PagerAdapter} that will provide
		 * fragments for each of the sections. We use a
		 * {@link FragmentPagerAdapter} derivative, which will keep every loaded
		 * fragment in memory. If this becomes too memory intensive, it may be
		 * best to switch to a
		 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
		 */
		SectionsPagerAdapter mSectionsPagerAdapter;

		/**
		 * The {@link ViewPager} that will host the section contents.
		 */
		ViewPager mViewPager;

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			// TextView textView = (TextView)
			// rootView.findViewById(R.id.section_label);
			// textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));

			// Create the adapter that will return a fragment for each of the
			// three
			// primary sections of the activity.
			mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity(), getActivity().getFragmentManager());

			// Set up the ViewPager with the sections adapter.
			mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);

			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
