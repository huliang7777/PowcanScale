package com.powcan.scale.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "powcanscale";

	private static final int DATABASE_VERSION = 2;

	private static DbHelper mDbHelper;

	public static DbHelper getInstance(Context context) {
		if (mDbHelper == null) {
			mDbHelper = new DbHelper(context);
		}
		return mDbHelper;
	}

	private DbHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(getTag(), "onCreate");
		createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(getTag(), "onUpgrade");
		db.execSQL( UserInfoDb.DROP_TABLE );
		db.execSQL( MeasureResultDb.DROP_TABLE );
//		db.execSQL("DROP TABLE IF EXISTS " + SaleEvaluateDb.TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + QueryCacheDB.TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + RegionInfoDB.TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + SearChRecordDB.TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + AgentDb.TABLE_NAME);
		createTable(db);
	}

	private void createTable(SQLiteDatabase db) {
		Log.i(getTag(), "createTable : " + UserInfoDb.CREATE_TABLE);
		Log.i(getTag(), "createTable : " + MeasureResultDb.CREATE_TABLE);
		db.execSQL( UserInfoDb.CREATE_TABLE );
		db.execSQL( MeasureResultDb.CREATE_TABLE );
//		db.execSQL(QueryCacheDB.CREATE_TABLE);
//		db.execSQL(RegionInfoDB.CREATE_TABLE);
//		db.execSQL(SearChRecordDB.CREATE_TABLE);
//		db.execSQL(AgentDb.CREATE_TABLE);
	}

	private String getTag() {
		return this.getClass().toString();
	}

}