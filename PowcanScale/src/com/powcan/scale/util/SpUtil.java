package com.powcan.scale.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class SpUtil {
	private static final String NAME = "preferences";

	public static SpUtil instance = null;

	private Context context;

	private SpUtil(Context context) {
		this.context = context;
	}

	public static SpUtil getInstance(Context context) {
		Context applicationContext = context.getApplicationContext();
		if (null == instance || instance.context != applicationContext) {
			instance = new SpUtil(context);
		}
		return instance;
	}

	private SharedPreferences sp;

	public SharedPreferences getSp() {
		if (sp == null)
			sp = context.getSharedPreferences(getSpFileName(),
					Context.MODE_PRIVATE);
		return sp;
	}

	public Editor getEdit() {
		return getSp().edit();
	}

	private String getSpFileName() {
		return NAME;
	}

	public void logout(){
		
	}

	public boolean isFirstLaunch() {
		return getSp().getBoolean("isFirstLaunch", true);
	}
	
	public void setFirstLaunched() {
		getEdit().putBoolean("isFirstLaunch", false).commit();
	}

	public boolean isLogin() {
		return getSp().getBoolean("isLogin", false);
	}

	public void setLogin(boolean isLogin) {
		getEdit().putBoolean("isLogin", isLogin).commit();
	}
	
	public int getAccount() {
		return getSp().getInt("account", 0);
	}

	public void setAccount(int account) {
		getEdit().putInt("account", account).commit();
	}
	
	public int getHeight() {
		return getSp().getInt("height", 0);
	}

	public void setHeight(int height) {
		getEdit().putInt("height", height).commit();
	}
	
	public String getPhone() {
		return getSp().getString("phone", "");
	}

	public void setPhone(String phone) {
		getEdit().putString("phone", phone).commit();
	}
	
	public String getQQ() {
		return getSp().getString("qq", "");
	}

	public void setQQ(String qq) {
		getEdit().putString("qq", qq).commit();
	}
	
	public String getEmail() {
		return getSp().getString("email", "");
	}

	public void setEmail(String email) {
		getEdit().putString("email", email).commit();
	}
	
	public String getBirthday() {
		return getSp().getString("birthday", "");
	}

	public void setBirthday(String birthday) {
		getEdit().putString("birthday", birthday).commit();
	}
	
	public String getGender() {
		return getSp().getString("gender", "");
	}

	public void setGender(String gender) {
		getEdit().putString("gender", gender).commit();
	}
}
