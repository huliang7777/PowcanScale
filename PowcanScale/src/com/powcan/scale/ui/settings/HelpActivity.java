package com.powcan.scale.ui.settings;


import com.powcan.scale.R;
import com.powcan.scale.ui.base.BaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class HelpActivity extends BaseActivity implements OnClickListener
{
	private ImageView imgBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
	}

	@Override
	public void onInit() 
	{
	}

	@Override
	public void onFindViews() 
	{
		imgBack = (ImageView) findViewById(R.id.img_back);
	}

	@Override
	public void onInitViewData() 
	{
	}

	@Override
	public void onBindListener() 
	{
		imgBack.setOnClickListener( this );
	}

	@Override
	public void onClick(View v) 
	{
		switch ( v.getId() ) 
		{
			case R.id.img_back:
				finish();
				break;
		}
	}
}
