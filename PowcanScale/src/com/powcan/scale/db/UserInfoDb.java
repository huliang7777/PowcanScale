package com.powcan.scale.db;

import java.util.ArrayList;

import com.powcan.scale.bean.UserInfo;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

/**
 * 用户信息数据库操作类
 * @author Administrator
 *
 */
public class UserInfoDb extends BaseDb {
	private static final String TAG = UserInfoDb.class.getSimpleName();
	private static final String TABLE_NAME = "UserInfo ";
	public static final String COLUMN_ID = "ID ";
	public static final String COLUMN_ACCOUNT = "ACCOUNT ";
	public static final String COLUMN_USERNAME = "USERNAME ";
	public static final String COLUMN_IMEI = "IMEI ";
	public static final String COLUMN_GENDER = "GENDER ";
	public static final String COLUMN_BIRTHDAY = "BIRTHDAY ";
	public static final String COLUMN_HEIGHT = "HEIGHT ";
	public static final String COLUMN_PHONE = "PHONE ";
	public static final String COLUMN_QQ = "QQ ";
	public static final String COLUMN_EMAIL = "EMAIL ";
	public static final String COLUMN_WEIGHT = "GOALWEIGHT  ";
	public static final String COLUMN_PASSWORD = "PASSWORD ";

	public static final String DROP_TABLE = DROP_TABLE_PREFIX + TABLE_NAME;
	public static final String CREATE_TABLE = CREATE_TABLE_PREFIX + TABLE_NAME + BRACKET_LEFT 
			+ COLUMN_ID + COLUMN_TYPE.INTEGER + PRIMARY_KEY_AUTOINCREMENT + COMMA 
			+ COLUMN_ACCOUNT + COLUMN_TYPE.TEXT + COMMA 
			+ COLUMN_USERNAME + COLUMN_TYPE.TEXT + COMMA
			+ COLUMN_IMEI + COLUMN_TYPE.TEXT + COMMA
			+ COLUMN_GENDER + COLUMN_TYPE.TEXT + COMMA 
			+ COLUMN_BIRTHDAY + COLUMN_TYPE.TEXT + COMMA 
			+ COLUMN_HEIGHT + COLUMN_TYPE.TEXT + COMMA 
			+ COLUMN_PHONE + COLUMN_TYPE.TEXT + COMMA 
			+ COLUMN_QQ + COLUMN_TYPE.TEXT + COMMA 
			+ COLUMN_EMAIL + COLUMN_TYPE.TEXT + COMMA
			+ COLUMN_WEIGHT + COLUMN_TYPE.TEXT + COMMA
			+ COLUMN_PASSWORD + COLUMN_TYPE.TEXT
			+ BRACKET_RIGHT;
	
	public static final String TABLE_COLUMNS_WITHOUT_ID = COLUMN_ACCOUNT + COMMA 
			+ COLUMN_USERNAME + COMMA
			+ COLUMN_IMEI + COMMA 
			+ COLUMN_GENDER + COMMA 
			+ COLUMN_BIRTHDAY + COMMA 
			+ COLUMN_HEIGHT + COMMA
			+ COLUMN_PHONE + COMMA 
			+ COLUMN_QQ + COMMA 
			+ COLUMN_EMAIL + COMMA 
			+ COLUMN_WEIGHT + COMMA
			+ COLUMN_PASSWORD;
	
	public static final String TABLE_COLUMNS = COLUMN_ID + COMMA
			+ TABLE_COLUMNS_WITHOUT_ID;

	public UserInfoDb(Context context) {
		super(context);
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	
	/**
	 * 插入用户信息数据
	 * @param userInfo 用户信息
	 */
	public void insertUserInfo(UserInfo userInfo) {
		checkDb();
		String sql = "insert into " + TABLE_NAME + BRACKET_LEFT + TABLE_COLUMNS_WITHOUT_ID
				+ " ) values " + BRACKET_LEFT + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" + BRACKET_RIGHT;
		
		Log.d(TAG, "insertUserInfo : " + sql);
		try 
		{
			db.execSQL( sql,
	                new Object[]{ userInfo.getAccount(), userInfo.getUsername(), userInfo.getImei(), userInfo.getGender(), 
					userInfo.getBirthday(), userInfo.getHeight(), userInfo.getPhone(),
					userInfo.getQq(), userInfo.getEmail(), userInfo.getGoalWeight(), userInfo.getPassword() });
		} 
		catch (SQLException e) 
		{
			Log.d("err", "insert failed");
		}
		Log.d(TAG, "insert success");
	}
	
	/**
	 * 更新用户信息
	 * @param userInfo 用户信息
	 */
	public void updateUserInfo(UserInfo userInfo) 
	{
		checkDb();
		String sql = "update " + TABLE_NAME + " set " + COLUMN_USERNAME + " =?, " + COLUMN_BIRTHDAY + " =?, "
				+ COLUMN_GENDER + " =?, " + COLUMN_HEIGHT + " =?, "
				+ COLUMN_PHONE + " =?, " + COLUMN_QQ + " =?, " 
				+ COLUMN_EMAIL + " =? where " + COLUMN_ACCOUNT + " =? ";
		
		Log.d(TAG, "updateUserInfo : " + sql);
		try 
		{
			db.execSQL( sql,
	                new Object[]{ userInfo.getUsername(), userInfo.getBirthday(), userInfo.getGender(), 
					 userInfo.getHeight(), userInfo.getPhone(), userInfo.getQq(), userInfo.getEmail(),
					 userInfo.getAccount() });
		} 
		catch (SQLException e) 
		{
			Log.d("err", "update failed");
		}
		Log.d(TAG, "update success");
	}
	
	/**
	 * 更新目标体重
	 * @param account 帐号
	 * @param goalWeight 目标体重
	 */
	public void updateGoalWeight( String account, String goalWeight) 
	{
		checkDb();
		String sql = "update " + TABLE_NAME + " set " + COLUMN_WEIGHT + " =? where " + COLUMN_ACCOUNT + " =? ";
		
		Log.d(TAG, "updateGoalWeight : " + sql);
		try 
		{
			db.execSQL( sql,
	                new Object[]{ goalWeight, account });
		} 
		catch (SQLException e) 
		{
			Log.d("err", "updateGoalWeight failed");
		}
		Log.d(TAG, "updateGoalWeight success");
	}
	
	/**
	 * 根据帐号获取用户信息
	 * @param account 帐号
	 * @return 用户信息
	 */
	public UserInfo getUserInfo( String account ) 
	{
		UserInfo userInfo = null;
		checkDb();
		String sql = "select " + TABLE_COLUMNS + " from " + TABLE_NAME + " where account =? ";
		
		Log.d(TAG, "getUserInfo : " + sql);
		try 
		{
			cursor = db.rawQuery(sql, new String[]{ account });
						
			if ( cursor.moveToFirst() ) 
			{
				userInfo = new UserInfo();
				userInfo.setId( cursor.getInt( 0 ) );
				userInfo.setAccount( cursor.getString( 1 ) );
				userInfo.setPassword( cursor.getString( 11 ) );
				userInfo.setUsername( cursor.getString( 2 ) );
				userInfo.setImei( cursor.getString( 3 ) );
				userInfo.setGender( cursor.getString( 4 ) );
				userInfo.setBirthday( cursor.getString( 5 ) );
				userInfo.setHeight( cursor.getString( 6 ) );
				userInfo.setPhone( cursor.getString( 7 ) );
				userInfo.setQq( cursor.getString( 8 ) );
				userInfo.setEmail( cursor.getString( 9 ) );
				userInfo.setGoalWeight( cursor.getString( 10 ) );
			}
			cursor.close();
			cursor = null;
		} 
		catch (SQLException e) 
		{
			Log.d("err", "get failed");
		}
		
		return userInfo;
	}
	
	/**
	 * 插入或者更新用户信息
	 * @param userInfo 用户信息
	 */
	public void insertOrUpdateUserInfo( UserInfo userInfo )
	{
		UserInfo user = getUserInfo( userInfo.getAccount() );
		if( user == null )
		{
			insertUserInfo(userInfo);
		}
		else
		{
			updateUserInfo(userInfo);
		}
	}

	/**
	 * 根据帐号获得所有的用户信息
	 * @param account 帐号
	 * @return 所有用户信息
	 */
	public ArrayList<UserInfo> getUserInfoes( String account ) 
	{
		UserInfo userInfo = null;
		ArrayList<UserInfo> list = new ArrayList<UserInfo>();
		checkDb();
		String sql = "select " + TABLE_COLUMNS + " from " + TABLE_NAME + " where " + COLUMN_ACCOUNT + " !=? ";
		
		Log.d(TAG, "getUserInfoes : " + sql);
		try 
		{
			cursor = db.rawQuery(sql, new String[]{ account } );
						
			if ( cursor.moveToFirst() ) 
			{
				int count = cursor.getCount();
				for (int i = 0; i < count; i++) 
				{
					userInfo = new UserInfo();
					userInfo.setId( cursor.getInt( 0 ) );
					userInfo.setAccount( cursor.getString( 1 ) );
					userInfo.setUsername( cursor.getString( 2 ) );
					userInfo.setImei( cursor.getString( 3 ) );
					userInfo.setGender( cursor.getString( 4 ) );
					userInfo.setBirthday( cursor.getString( 5 ) );
					userInfo.setHeight( cursor.getString( 6 ) );
					userInfo.setPhone( cursor.getString( 7 ) );
					userInfo.setQq( cursor.getString( 8 ) );
					userInfo.setEmail( cursor.getString( 9 ) );
					userInfo.setGoalWeight( cursor.getString( 10 ) );
					
					list.add(userInfo);
					cursor.moveToNext();
				}
			}
			cursor.close();
			cursor = null;
		} 
		catch (SQLException e) 
		{
			Log.d("err", "get list failed");
		}
		
		return list;
	}
}
