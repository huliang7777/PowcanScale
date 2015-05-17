package com.powcan.scale.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库帮助类
 * @author Administrator
 *
 */
public class DbHelper extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "powcanscale";

	private static final int DATABASE_VERSION = 4;

	private static DbHelper mDbHelper;
	
	/**
	 * 返回当前对象实例
	 * @param context
	 * @return
	 */
	public static DbHelper getInstance(Context context) {
		if (mDbHelper == null) {
			mDbHelper = new DbHelper(context);
		}
		return mDbHelper;
	}
	
	private DbHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * 创建数据库表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(getTag(), "onCreate");
		createTable(db);
	}

	/**
	 * 更新数据库表
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(getTag(), "onUpgrade");
		db.execSQL( UserInfoDb.DROP_TABLE );
		db.execSQL( MeasureResultDb.DROP_TABLE );
		createTable(db);
	}
	
	/**
	 * 创建表
	 * @param db
	 */
	private void createTable(SQLiteDatabase db) {
		Log.i(getTag(), "createTable : " + UserInfoDb.CREATE_TABLE);
		Log.i(getTag(), "createTable : " + MeasureResultDb.CREATE_TABLE);
		db.execSQL( UserInfoDb.CREATE_TABLE );
		db.execSQL( MeasureResultDb.CREATE_TABLE );
	}

	private String getTag() {
		return this.getClass().toString();
	}

}