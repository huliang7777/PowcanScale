package com.powcan.scale.ui.settings;

import com.powcan.scale.R;
import com.powcan.scale.ui.LoginActivity;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.util.SpUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class SettingsActivity extends BaseActivity implements OnClickListener
{
	private ImageView imgBack;
	private View rlSetGoal;
	private View rlMeasureRemind;
	private View rlVersionUpdate;
	private View rlSuggest;
	private View rlAboutUs;
	private View rlHelp;
	private Button btnExit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onFindViews() 
	{
		imgBack = (ImageView) findViewById(R.id.img_back);
		rlSetGoal = findViewById(R.id.rl_set_goal);
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

	@Override
	public void onBindListener() 
	{
		imgBack.setOnClickListener( this );
		rlSetGoal.setOnClickListener( this );
		rlMeasureRemind.setOnClickListener( this );
		rlAboutUs.setOnClickListener( this );
		rlHelp.setOnClickListener( this );
		rlVersionUpdate.setOnClickListener( this );
		rlSuggest.setOnClickListener( this );
		btnExit.setOnClickListener( this );
	}

	@Override
	public void onClick(View v) 
	{
		switch ( v.getId() ) 
		{
			case R.id.img_back:
				finish();
				break;
	
			case R.id.rl_set_goal:
				Intent intent = new Intent( this, SetGoalActivity.class );
				startActivity( intent );
				break;
				
			case R.id.rl_measure_remind:
				intent = new Intent( this, MeasureRemindActivity.class );
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
				break;
				
			case R.id.rl_suggest:
				break;
				
			case R.id.btn_exit:
				SpUtil.getInstance( this ).reset();
				intent = new Intent( this, LoginActivity.class );
				startActivity( intent );
				finish();
				break;
		}
	}
}
