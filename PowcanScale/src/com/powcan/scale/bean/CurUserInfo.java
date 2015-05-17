package com.powcan.scale.bean;

import android.content.Context;

import com.powcan.scale.util.SpUtil;

/**
 * 当前用户信息
 * @author Administrator
 *
 */
public class CurUserInfo 
{
	private static CurUserInfo instance;
	private UserInfo curUser;
	private Context mContext;
	
	private CurUserInfo( Context context )
	{
		mContext = context;
	}
	
	public static CurUserInfo getInstance( Context context )
	{
		Context applicationContext = context.getApplicationContext();
		if (null == instance || instance.mContext != applicationContext ) 
		{
			instance = new CurUserInfo( context );
		}
		return instance;
	}
	
	public UserInfo getCurUser()
	{
		if ( curUser == null )
		{
			reloadUserInfo();
		}
		return curUser;
	}
	
	public void setUserInfo( UserInfo curUser )
	{
		this.curUser = curUser;
	}
	
	public void reloadUserInfo()
	{
		curUser = SpUtil.getInstance( mContext ).getCurrUser();
	}
}
