package com.powcan.scale.ui.settings;

import com.powcan.scale.R;
import com.powcan.scale.ui.LoginActivity;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.util.SpUtil;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 应用设置界面
 * @author Administrator
 *
 */
public class SettingsActivity extends BaseActivity implements OnClickListener
{
	private ImageView imgBack;
//	private View rlSetGoal;
	private View rlMeasureRemind;
	private View rlVersionUpdate;
	private View rlSuggest;
	private View rlAboutUs;
	private View rlHelp;
	private Button btnExit;
	
	private FeedbackAgent mFeedbackAgent;
	
	/**
	 * 创建界面方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void onInit() 
	{
        mFeedbackAgent = new FeedbackAgent(this);
        mFeedbackAgent.sync();
	}

	/**
	 * 查找子view
	 */
	@Override
	public void onFindViews() 
	{
		imgBack = (ImageView) findViewById(R.id.img_back);
//		rlSetGoal = findViewById(R.id.rl_set_goal);
		rlMeasureRemind = findViewById(R.id.rl_measure_remind);
		rlAboutUs = findViewById(R.id.rl_about_us);
		rlHelp = findViewById(R.id.rl_help);
		rlVersionUpdate = findViewById(R.id.rl_version_update);
		rlSuggest = findViewById(R.id.rl_suggest);
		btnExit = (Button) findViewById(R.id.btn_exit);
	}

	@Override
	public void onInitViewData() 
	{
		
	}

	/**
	 * 绑定事件监听
	 */
	@Override
	public void onBindListener() 
	{
		imgBack.setOnClickListener( this );
//		rlSetGoal.setOnClickListener( this );
		rlMeasureRemind.setOnClickListener( this );
		rlAboutUs.setOnClickListener( this );
		rlHelp.setOnClickListener( this );
		rlVersionUpdate.setOnClickListener( this );
		rlSuggest.setOnClickListener( this );
		btnExit.setOnClickListener( this );
	}

	/**
	 * 点击事件处理方法
	 */
	@Override
	public void onClick(View v) 
	{
		switch ( v.getId() ) 
		{
			case R.id.img_back:
				finish();
				break;
	
//			case R.id.rl_set_goal:
//				Intent intent = new Intent( this, SetGoalActivity.class );
//				startActivity( intent );
//				break;
				
			case R.id.rl_measure_remind:
				Intent intent = new Intent( this, MeasureRemindActivity.class );
				startActivity( intent );
				break;
				
			case R.id.rl_about_us:
				intent = new Intent( this, AboutActivity.class );
				startActivity( intent );
				break;
				
			case R.id.rl_help:
				intent = new Intent( this, HelpActivity.class );
				startActivity( intent );
				break;
				
			case R.id.rl_version_update:
				toCheckUpdateVersion();
				break;
				
			case R.id.rl_suggest:
				mFeedbackAgent.startFeedbackActivity();
				break;
				
			case R.id.btn_exit:
				SpUtil.getInstance( this ).reset();
				intent = new Intent( this, LoginActivity.class );
				intent.putExtra( "from", "exit" );
				startActivity( intent );
				finish();
				break;
		}
	}
	
	/**
	 * 检查更新
	 */
	private void toCheckUpdateVersion() {
    	// 检查更新
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
		    @Override
		    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
		        switch (updateStatus) {
		        case UpdateStatus.Yes: // has update
		            UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
		            break;
		        case UpdateStatus.No: // has no update
		            Toast.makeText(getActivity(), "没有更新", Toast.LENGTH_SHORT).show();
		            break;
		        case UpdateStatus.NoneWifi: // none wifi
		            Toast.makeText(getActivity(), "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
		            break;
		        case UpdateStatus.Timeout: // time out
		            Toast.makeText(getActivity(), "超时", Toast.LENGTH_SHORT).show();
		            break;
		        }
		    }
		});
		UmengUpdateAgent.update(this);
//    	UmengUpdateAgent.forceUpdate(this);
    }
}
