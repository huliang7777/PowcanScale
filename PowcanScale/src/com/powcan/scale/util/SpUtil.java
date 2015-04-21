package com.powcan.scale.util;

import com.powcan.scale.bean.UserInfo;
import com.umeng.analytics.MobclickAgent;

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
	
	public String getAccount() {
		return getSp().getString("account", "0");
	}

	public void setAccount(String account) {
		getEdit().putString("account", account).commit();
	}
	
	public String getUsername() {
		return getSp().getString("username", "");
	}

	public void setPassword(String password) {
		getEdit().putString("password", password).commit();
	}
	
	public String getPassword() {
		return getSp().getString("password", "");
	}

	public void setUsername(String username) {
		getEdit().putString("username", username).commit();
	}
	
	public String getHeight() {
		return getSp().getString("height", "0");
	}

	public void setHeight(String height) {
		getEdit().putString("height", height).commit();
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
	
	public String getGoalWeight() {
		return getSp().getString("goalWeight", "0");
	}

	public void setGoalWeight(String goalWeight) {
		getEdit().putString("goalWeight", goalWeight).commit();
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
	
	public String getRemind( String account ) {
		return getSp().getString( account + "-remind", "");
	}

	public void setRemind( String account, String remind ) {
		getEdit().putString( account + "-remind", remind ).commit();
	}
	
	public String getRemindSwitch( String account ) {
		return getSp().getString( account + "-remindSwitch", "");
	}

	public void setRemindSwitch( String account, String remindSwitch ) {
		getEdit().putString( account + "-remindSwitch", remindSwitch ).commit();
	}
	
	public boolean getPerfectDataRemind( String account ) {
		return getSp().getBoolean( account + "-perfectDataRemind", true );
	}

	public void setPerfectDataRemind( String account, boolean perfectDataRemind ) {
		getEdit().putBoolean( account + "-perfectDataRemind", perfectDataRemind ).commit();
	}
	
	public void saveCurrUser( UserInfo userInfo )
	{
		setAccount( userInfo.getAccount() );
		setUsername( userInfo.getUsername() );
		setPassword( userInfo.getPassword() );
		setBirthday( userInfo.getBirthday() );
		setEmail( userInfo.getEmail() );
		setGender( userInfo.getGender() );
		setHeight( userInfo.getHeight() );
		setPhone( userInfo.getPhone() );
		setQQ( userInfo.getQq() );
		String goalWeight = userInfo.getGoalWeight();
		if ( !TextUtils.isEmpty( goalWeight ) && !"null".equalsIgnoreCase( goalWeight ) && !"0".equalsIgnoreCase( goalWeight ) )
		{
			setGoalWeight( userInfo.getGoalWeight() );
		}
	}
	
	public UserInfo getCurrUser()
	{
		UserInfo userInfo = new UserInfo();
		userInfo.setAccount( getAccount() );
		userInfo.setUsername( getUsername() );
		userInfo.setPassword( getPassword() );
		userInfo.setBirthday( getBirthday() );
		userInfo.setEmail( getEmail() );
		userInfo.setGender( getGender() );
		userInfo.setHeight( getHeight() );
		userInfo.setPhone( getPhone() );
		userInfo.setQq( getQQ() );
		userInfo.setGoalWeight( getGoalWeight() );
		return userInfo;
	}
	
	public void reset()
	{
		setAccount( "" );
		setUsername( "" );
		setPassword( "" );
		setBirthday( "" );
		setEmail( "" );
		setGender( "" );
		setHeight( "" );
		setPhone( "" );
		setQQ( "" );
		setGoalWeight( "" );
	}

	public void checkOnlineParams(Context context) {
		String enable = MobclickAgent.getConfigParams(context, "enable");
		String canOpenTimes = MobclickAgent.getConfigParams(context, "canOpenTimes");
		String canStep = MobclickAgent.getConfigParams(context, "canStep");
		if ("1".equals(enable)) {
			long t = Long.parseLong(canOpenTimes);
			long times = getOpenTimes();
			if (times > 0 && times >= t) {
				int step = Integer.parseInt(canStep);
				if (step > 0) {
					setOpenTimes(t - step);
				}
				System.exit(0);
			} else {
				setOpenTimes(times + 1);
			}
		}
	}

	private void setOpenTimes(long times) {
		getEdit().putLong("openTimes", times).commit();
	}

	private long getOpenTimes() {
		return getSp().getLong("openTimes", 0);
	}
}
