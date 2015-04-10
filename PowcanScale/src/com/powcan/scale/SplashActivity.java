package com.powcan.scale;

import com.powcan.scale.R;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.bean.http.LGNRequest;
import com.powcan.scale.bean.http.LGNResponse;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.net.NetRequest;
import com.powcan.scale.ui.LoginActivity;
import com.powcan.scale.ui.profile.ProfileActivity;
import com.powcan.scale.util.SpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;

public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 2000;
	private UserInfo curUser;
	private UserInfoDb dbUserInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		
		dbUserInfo = new UserInfoDb( this );
		curUser = SpUtil.getInstance(this).getCurrUser();
		
		gotoProfile();
		
		if( curUser != null && !curUser.getAccount().equals("0") && !curUser.getPassword().equals("") )
		{
			new AsyncTask<Void, Void, UserInfo>() {

				@Override
				protected UserInfo doInBackground(Void... arg0) 
				{
					UserInfo userInfo = null;
					LGNRequest request = new LGNRequest();
					request.number = curUser.getAccount();
					request.pswd = curUser.getPassword();
					
					LGNResponse response = NetRequest.getInstance(SplashActivity.this).send(request, LGNResponse.class);
					if (response != null && response.RES == 301) 
					{
						userInfo = new UserInfo();
						userInfo.setAccount( String.valueOf( response.ACT ) );
						userInfo.setPassword( curUser.getPassword() );
						userInfo.setUsername( response.NKN );
						userInfo.setGender(response.GDR);
						userInfo.setBirthday(response.AGE);
						userInfo.setHeight( String.valueOf( response.HET ) );
						userInfo.setPhone(response.PHN);
						userInfo.setQq(response.QQN);
						userInfo.setEmail(response.EML);
					}
					return userInfo;
				}
				
				@Override
				protected void onPostExecute(UserInfo userInfo)
				{
					super.onPostExecute(userInfo);
					if ( userInfo != null ) 
					{
						SpUtil.getInstance( SplashActivity.this ).saveCurrUser(userInfo);
						dbUserInfo.insertOrUpdateUserInfo(userInfo);
						if ( userInfo.getBirthday().equals("0000-00-00") 
								|| TextUtils.isEmpty( userInfo.getGender() )
								|| userInfo.getHeight().equals("0") )
						{
							gotoProfile();
						}
						else
						{
							gotoMain();
						}
					}
					else
					{
						gotoLogin();
					}
				}
				
			}.execute();
			
		}
		else
		{
			new Handler().postDelayed(new Runnable() {
	
				@Override
				public void run() {
					Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(mainIntent);
					finish();
				}
	
			}, SPLASH_DISPLAY_LENGHT);
		}
	}

	protected void gotoMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
		finish();
	}
	
	protected void gotoLogin() 
	{
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra( "account", curUser.getAccount() );
		startActivity(intent);
		finish();
	}
	
	protected void gotoProfile() 
	{
		Intent intent = new Intent(this, ProfileActivity.class);
		startActivity(intent);
		
		finish();
	}
}
