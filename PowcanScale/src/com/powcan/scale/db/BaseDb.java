package com.powcan.scale.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 基础数据库类
 * @author Administrator
 *
 */
public abstract class BaseDb {
	
	public static final String CREATE_TABLE_PREFIX = "CREATE TABLE IF NOT EXISTS ";
	public static final String DROP_TABLE_PREFIX = "DROP TABLE IF EXISTS ";
	
	public static interface COLUMN_TYPE {
		public static final String INTEGER = " INTEGER ";
		public static final String LONG = " LONG ";
		public static final String TEXT = " TEXT ";
		public static final String FLOAT = " FLOAT ";
	}
	
	public static final String PRIMARY_KEY = " PRIMARY KEY ";
	public static final String PRIMARY_KEY_AUTOINCREMENT = " PRIMARY KEY AUTOINCREMENT ";
	
	public static final String BRACKET_LEFT = " ( ";
	public static final String BRACKET_RIGHT = " );";
	public static final String COMMA = ",";
	
	protected Cursor cursor = null;

	protected DbHelper helper = null;

	protected SQLiteDatabase db = null;

	/**
	 * 构造函数
	 * @param context
	 */
	public BaseDb(Context context) {
		helper = DbHelper.getInstance(context);
		db = helper.getWritableDatabase();
	}
	
	/**
	 * 检查数据库是否连接
	 */
	protected void checkDb() {
		if (db == null || !db.isOpen()) {
			db = helper.getWritableDatabase();
		}
	}
	
	/**
	 * 关闭游标
	 */
	public void closeDbAndCursor() {
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}

	/**
	 * 获得表名虚方法
	 * @return
	 */
    public abstract String getTableName();
}
