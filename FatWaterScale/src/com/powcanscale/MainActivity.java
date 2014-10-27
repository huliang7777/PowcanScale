package com.powcanscale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.powcanscale.adapter.SectionsPagerAdapter;
import com.powcanscale.ui.LoginActivity;
import com.powcanscale.ui.settings.SettingsFragment;
import com.powcanscale.util.SpUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private UMSocialService mController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		UmengUpdateAgent.update(this);
		MobclickAgent.updateOnlineConfig(this);

		// 首先在您的Activity中添加如下成员变量
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
		mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		// 设置分享图片, 参数2为图片的url地址
		mController.setShareMedia(new UMImage(this, "http://www.umeng.com/images/pic/banner_module_social.png"));
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appID = "wx967daebe835fbeac";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, appID);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appID);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// 设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "100424468", "c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK();
		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "100424468", "c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();

		if (SpUtil.getInstance(this).isFirstLaunch()) {
			Intent mainIntent = new Intent(this, LoginActivity.class);
			startActivity(mainIntent);
		}

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

		SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(getApplicationContext(), "holder", new SpeechSynthesizerListener() {

			@Override
			public void onBufferProgressChanged(SpeechSynthesizer arg0, int arg1) {

			}

			@Override
			public void onCancel(SpeechSynthesizer arg0) {

			}

			@Override
			public void onError(SpeechSynthesizer arg0, SpeechError arg1) {

			}

			@Override
			public void onNewDataArrive(SpeechSynthesizer arg0, byte[] arg1, int arg2) {

			}

			@Override
			public void onSpeechFinish(SpeechSynthesizer arg0) {

			}

			@Override
			public void onSpeechPause(SpeechSynthesizer arg0) {

			}

			@Override
			public void onSpeechProgressChanged(SpeechSynthesizer arg0, int arg1) {

			}

			@Override
			public void onSpeechResume(SpeechSynthesizer arg0) {

			}

			@Override
			public void onSpeechStart(SpeechSynthesizer arg0) {

			}

			@Override
			public void onStartWorking(SpeechSynthesizer arg0) {

			}
		});
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, SpeechSynthesizer.SPEAKER_FEMALE);
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE, SpeechSynthesizer.AUDIO_ENCODE_AMR);
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE, SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85);
		speechSynthesizer.setApiKey("your-apiKey", "your-secretKey");
		speechSynthesizer.speak("欢迎使用保康脂肪秤");
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		if (position != R.id.btn_settings) {
			fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
		} else {
			fragmentManager.beginTransaction().replace(R.id.container, SettingsFragment.newInstance(position + 1)).commit();
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

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			// 是否只有已登录用户才能打开分享选择页
			mController.openShare(this, false);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity(), getActivity().getSupportFragmentManager());

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
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
