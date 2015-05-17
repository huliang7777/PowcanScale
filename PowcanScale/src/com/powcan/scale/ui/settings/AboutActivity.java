package com.powcan.scale.ui.settings;


import com.powcan.scale.R;
import com.powcan.scale.ui.base.BaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 关于界面
 * @author Administrator
 *
 */
public class AboutActivity extends BaseActivity implements OnClickListener
{
	private ImageView imgBack;

	/**
	 * 创建界面方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	@Override
	public void onInit() 
	{
	}

	/**
	 * 查找子view
	 */
	@Override
	public void onFindViews() 
	{
		imgBack = (ImageView) findViewById(R.id.img_back);
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
		}
	}
}
