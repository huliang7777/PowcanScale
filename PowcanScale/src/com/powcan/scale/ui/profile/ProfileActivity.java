package com.powcan.scale.ui.profile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.powcan.scale.MainActivity;
import com.powcan.scale.R;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.bean.http.BaseResponse;
import com.powcan.scale.bean.http.UTURequest;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.dialog.LoadingDialog;
import com.powcan.scale.dialog.SelectGenderDialog;
import com.powcan.scale.dialog.SelectGenderDialog.GenderSelectEvent;
import com.powcan.scale.net.NetRequest;
import com.powcan.scale.ui.RegisterActivity;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.util.SpUtil;

/**
 * 动画参考：http://cyrilmottier.com/2014/05/20/custom-animations-with-fragments/
 * @author Administrator
 */
public class ProfileActivity extends BaseActivity implements OnClickListener, GenderSelectEvent
{
	private static final String TAG = RegisterActivity.class.getSimpleName();

	private Button btnSave;
	private EditText etUsername;
	private EditText etGender;
	private EditText etBirthday;
	private EditText etHeight;
	
	private LoadingDialog loadingDialog;
	private SelectGenderDialog genderDialog;
	private UserInfoDb dbUserInfo;
	private UserInfo userInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
	}

	@Override
	public void onInit() 
	{
		userInfo = SpUtil.getInstance( this ).getCurrUser();
		dbUserInfo = new UserInfoDb( this ); 
	}

	@Override
	public void onFindViews() 
	{
		btnSave = (Button) findViewById(R.id.btn_save);
		etUsername = (EditText) findViewById(R.id.et_username);
		etGender = (EditText) findViewById(R.id.et_gender);
		etBirthday = (EditText) findViewById(R.id.et_birthday);
		etHeight = (EditText) findViewById(R.id.et_height);
	}

	@Override
	public void onInitViewData() {
		
	}

	@Override
	public void onBindListener() {
		btnSave.setOnClickListener( this );
		etGender.setOnClickListener( this );
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:
			Log.d( TAG, "btn_commit" );
			reqSaveUserInfo();			
			break;
		case R.id.et_gender:
			genderDialog = new SelectGenderDialog( this, this );
			genderDialog.show();
			break;
		}
	}

	private void reqSaveUserInfo() 
	{
		userInfo.setUsername( etUsername.getText().toString() );
		userInfo.setGender( etGender.getText().toString() );
		userInfo.setBirthday( etBirthday.getText().toString() );
		userInfo.setHeight( etHeight.getText().toString() );
		
		
		loadingDialog = new LoadingDialog( this, "保存中..." );
		loadingDialog.show();
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... arg0) 
			{
				UTURequest utu = new UTURequest();
				utu.account = userInfo.getAccount();
				utu.nickname = userInfo.getUsername();
				utu.gender = userInfo.getGender();
				utu.birthday = userInfo.getBirthday();
				utu.height = userInfo.getHeight();
//				utu.phone = "";
//				utu.qq = "";
//				utu.email = "";
				
				BaseResponse regResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(utu, BaseResponse.class);
				if (regResponse != null && ( regResponse.RES >= 401 || regResponse.RES <= 404 ) ) 
				{
					return true;
				}
				
				return false;
			}
			
			@Override
			protected void onPostExecute(Boolean result)
			{
				super.onPostExecute(result);
				if ( result ) 
				{
					dbUserInfo.updateUserInfo( userInfo );
					SpUtil.getInstance( ProfileActivity.this ).saveCurrUser(userInfo);
					
					showToast("资料保存成功");
					
					gotoMain();
				}
				else
				{
					String msg = "资料保存失败,请重试！";
					showToastShort( msg );
				}
				loadingDialog.dismiss();
			}
			
		}.execute();
	}

	protected void gotoMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
		finish();
	}

	@Override
	public void onGenderSelect(int which) 
	{
		if ( which == 1 )
		{
			etGender.setText("M");
		}
		else if ( which == 2 )
		{
			etGender.setText("F");
		}
		genderDialog.dismiss();
	}
}
