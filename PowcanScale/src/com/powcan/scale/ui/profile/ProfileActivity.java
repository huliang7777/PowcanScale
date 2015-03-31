package com.powcan.scale.ui.profile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.powcan.scale.R;
import com.powcan.scale.ui.base.BaseActivity;

/**
 * 动画参考：http://cyrilmottier.com/2014/05/20/custom-animations-with-fragments/
 * @author Administrator
 */
public class ProfileActivity extends BaseActivity 
{
	private Button btnSave;
	private EditText etUsername;
	private EditText etGender;
	private EditText etBirthday;
	private EditText et;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onFindViews() {
		
	}

	@Override
	public void onInitViewData() {
		
	}

	@Override
	public void onBindListener() {
		
	}

}
