package com.powcan.scale.db;

import java.util.ArrayList;
import java.util.HashMap;

import com.powcan.scale.bean.MeasureResult;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

/**
 * 正则替换
 * COLUMN_([^, & ^$]*);
 * COLUMN_$1 = "$1 ";
 * @author Administrator
 *
 */
public class MeasureResultDb extends BaseDb 
{
	private static final String TAG = MeasureResultDb.class.getSimpleName();
	private static final String TABLE_NAME = "MeasureResult ";
	private static final String COLUMN_ID = "ID ";
	private static final String COLUMN_ACCOUNT = "ACCOUNT ";
	private static final String COLUMN_WEIGHT = "WEIGHT "; 
	private static final String COLUMN_BMI = "BMI "; 
	private static final String COLUMN_BODYFATRATE = "BODYFATRATE "; // 体脂率
	private static final String COLUMN_MUSCLEPROPORTION = "MUSCLEPROPORTION "; // 肌肉比例
	private static final String COLUMN_PHYSICALAGE = "PHYSICALAGE "; // 身体年龄
	private static final String COLUMN_SUBCUTANEOUSFAT = "SUBCUTANEOUSFAT "; // 皮下脂肪
	private static final String COLUMN_VISCERALFAT = "VISCERALFAT "; // 内脏脂肪
	private static final String COLUMN_SUBBASALMETABOLISM = "SUBBASALMETABOLISM "; // 基础代谢(亚)
	private static final String COLUMN_EUROPEBASALMETABOLISM = "EUROPEBASALMETABOLISM "; // 基础代谢(欧)
	private static final String COLUMN_BONEMASS = "BONEMASS "; // 骨量
	private static final String COLUMN_WATERCONTENT = "WATERCONTENT "; // 水含量
	private static final String COLUMN_DATE = "DATE ";

	public static final String DROP_TABLE = DROP_TABLE_PREFIX + TABLE_NAME;
	public static final String CREATE_TABLE = CREATE_TABLE_PREFIX + TABLE_NAME + BRACKET_LEFT
			+ COLUMN_ID + COLUMN_TYPE.INTEGER + PRIMARY_KEY_AUTOINCREMENT + COMMA
			+ COLUMN_ACCOUNT + COLUMN_TYPE.TEXT + COMMA 
			+ COLUMN_WEIGHT + COLUMN_TYPE.FLOAT + COMMA
			+ COLUMN_BMI + COLUMN_TYPE.FLOAT + COMMA 
			+ COLUMN_BODYFATRATE + COLUMN_TYPE.FLOAT + COMMA 
			+ COLUMN_MUSCLEPROPORTION + COLUMN_TYPE.FLOAT + COMMA 
			+ COLUMN_PHYSICALAGE + COLUMN_TYPE.FLOAT + COMMA
			+ COLUMN_SUBCUTANEOUSFAT + COLUMN_TYPE.FLOAT + COMMA 
			+ COLUMN_VISCERALFAT + COLUMN_TYPE.FLOAT + COMMA
			+ COLUMN_SUBBASALMETABOLISM + COLUMN_TYPE.FLOAT + COMMA 
			+ COLUMN_EUROPEBASALMETABOLISM + COLUMN_TYPE.FLOAT + COMMA
			+ COLUMN_BONEMASS + COLUMN_TYPE.FLOAT + COMMA 
			+ COLUMN_WATERCONTENT + COLUMN_TYPE.FLOAT + COMMA
			+ COLUMN_DATE + COLUMN_TYPE.TEXT
			+ BRACKET_RIGHT;
	
	public static final String TABLE_COLUMNS_WITHOUT_ID = COLUMN_ACCOUNT + COMMA 
			+ COLUMN_WEIGHT + COMMA 
			+ COLUMN_BMI + COMMA 
			+ COLUMN_BODYFATRATE + COMMA 
			+ COLUMN_MUSCLEPROPORTION + COMMA 
			+ COLUMN_PHYSICALAGE + COMMA
			+ COLUMN_SUBCUTANEOUSFAT + COMMA 
			+ COLUMN_VISCERALFAT + COMMA 
			+ COLUMN_SUBBASALMETABOLISM + COMMA 
			+ COLUMN_EUROPEBASALMETABOLISM + COMMA 
			+ COLUMN_BONEMASS + COMMA 
			+ COLUMN_WATERCONTENT + COMMA 
			+ COLUMN_DATE;
	
	public static final String TABLE_COLUMNS = COLUMN_ID + COMMA + TABLE_COLUMNS_WITHOUT_ID;
	
	

	public MeasureResultDb(Context context) {
		super(context);
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	public void insertMeasureResult(MeasureResult measureResult) {
		checkDb();
		String sql = "insert into " + TABLE_NAME + BRACKET_LEFT + TABLE_COLUMNS_WITHOUT_ID
				+ " ) values " + BRACKET_LEFT + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" + BRACKET_RIGHT;
		
		Log.d(TAG, "insertMeasureResult : " + sql);
		try 
		{
			db.execSQL( sql,
	                new Object[]{ measureResult.getAccount(), measureResult.getWeight(), measureResult.getBmi(),
					measureResult.getBodyFatRate(), measureResult.getMuscleProportion(), measureResult.getPhysicalAge(),
					measureResult.getSubcutaneousFat(), measureResult.getVisceralFat(), measureResult.getSubBasalMetabolism(),
					measureResult.getEuropeBasalMetabolism(), measureResult.getBoneMass(), measureResult.getWaterContent(),
					measureResult.getDate() });
		} 
		catch (SQLException e) 
		{
			Log.d("err", "insert failed");
		}
		Log.d(TAG, "insert success");
	}
	
	public void updateMeasureResult(MeasureResult measureResult) 
	{
		checkDb();
		String sql = "update " + TABLE_NAME + " set " 
				+ COLUMN_WEIGHT + " =?, " 
				+ COLUMN_BMI + " =?, "
				+ COLUMN_BODYFATRATE + " =?, "
				+ COLUMN_MUSCLEPROPORTION + " =?, "
				+ COLUMN_PHYSICALAGE + " =?, "
				+ COLUMN_SUBCUTANEOUSFAT + " =?, "
				+ COLUMN_VISCERALFAT + " =?, "
				+ COLUMN_SUBBASALMETABOLISM + " =?, "
				+ COLUMN_EUROPEBASALMETABOLISM + " =?, "
				+ COLUMN_BONEMASS + " =?, "
				+ COLUMN_WATERCONTENT + " =? "
				+ " where 1=1 "
				+ " and " + COLUMN_ACCOUNT + " =? "
				+ " and " + COLUMN_DATE + " =? ";
		
		Log.d(TAG, "updateUserInfo : " + sql);
		try 
		{
			db.execSQL( sql,
	                new Object[]{ measureResult.getWeight(), measureResult.getBmi(),
					measureResult.getBodyFatRate(), measureResult.getMuscleProportion(), measureResult.getPhysicalAge(),
					measureResult.getSubcutaneousFat(), measureResult.getVisceralFat(), measureResult.getSubBasalMetabolism(),
					measureResult.getEuropeBasalMetabolism(), measureResult.getBoneMass(), measureResult.getWaterContent(),
					measureResult.getAccount(), measureResult.getDate() });
		} 
		catch (SQLException e) 
		{
			Log.d("err", "update failed");
		}
		Log.d(TAG, "update success");
	}
	
	public MeasureResult getMeasureResult( String account, String date ) 
	{
		MeasureResult measureResult = null;
		checkDb();
		String sql = "select " + TABLE_COLUMNS + " from " + TABLE_NAME + " where 1=1 "
				+ " and " + COLUMN_ACCOUNT + " =? "
				+ " and " + COLUMN_DATE + " =? ";
		
		Log.d(TAG, "getMeasureResult : " + sql);
		try 
		{
			cursor = db.rawQuery(sql, new String[]{ account, date });
			if ( cursor.moveToFirst() ) {
				
				measureResult = new MeasureResult();
				measureResult.setId( cursor.getInt( 0 ) );
				measureResult.setAccount( cursor.getString( 1 ) );
				measureResult.setWeight( cursor.getFloat( 2 ) );
				measureResult.setBmi( cursor.getFloat( 3 ) );
				measureResult.setBodyFatRate( cursor.getFloat( 4 ) );
				measureResult.setMuscleProportion( cursor.getFloat( 5 ) );
				measureResult.setPhysicalAge( cursor.getFloat( 6 ) );
				measureResult.setSubcutaneousFat( cursor.getFloat( 7 ) );
				measureResult.setVisceralFat( cursor.getFloat( 8 ) );
				measureResult.setSubBasalMetabolism( cursor.getFloat( 9 ) );
				measureResult.setEuropeBasalMetabolism( cursor.getFloat( 10 ) );
				measureResult.setBoneMass( cursor.getFloat( 11 ) );
				measureResult.setWaterContent( cursor.getFloat( 12 ) );
				measureResult.setDate( cursor.getString( 13 ) );
			}
			cursor.close();
			cursor = null;
		} 
		catch (SQLException e) 
		{
			Log.d("err", "get failed");
		}
		
		return measureResult;
	}
	
	public MeasureResult getLastMeasureResult( String account ) 
	{
		MeasureResult measureResult = null;
		checkDb();
		String sql = "select " + TABLE_COLUMNS + " from " + TABLE_NAME + " where 1=1 "
				+ " and " + COLUMN_ACCOUNT + " =?";
		
		Log.d(TAG, "getLastMeasureResult : " + sql);
		try 
		{
			cursor = db.rawQuery(sql, new String[]{ account });
			if ( cursor.moveToFirst() ) {
				
				measureResult = new MeasureResult();
				measureResult.setId( cursor.getInt( 0 ) );
				measureResult.setAccount( cursor.getString( 1 ) );
				measureResult.setWeight( cursor.getFloat( 2 ) );
				measureResult.setBmi( cursor.getFloat( 3 ) );
				measureResult.setBodyFatRate( cursor.getFloat( 4 ) );
				measureResult.setMuscleProportion( cursor.getFloat( 5 ) );
				measureResult.setPhysicalAge( cursor.getFloat( 6 ) );
				measureResult.setSubcutaneousFat( cursor.getFloat( 7 ) );
				measureResult.setVisceralFat( cursor.getFloat( 8 ) );
				measureResult.setSubBasalMetabolism( cursor.getFloat( 9 ) );
				measureResult.setEuropeBasalMetabolism( cursor.getFloat( 10 ) );
				measureResult.setBoneMass( cursor.getFloat( 11 ) );
				measureResult.setWaterContent( cursor.getFloat( 12 ) );
				measureResult.setDate( cursor.getString( 13 ) );
			}
			cursor.close();
			cursor = null;
		} 
		catch (SQLException e) 
		{
			Log.d("err", "get failed");
		}
		
		return measureResult;
	}
	
	public ArrayList<MeasureResult> getMeasureResults( String account ) 
	{
		MeasureResult measureResult = null;
		ArrayList<MeasureResult> list = new ArrayList<MeasureResult>();
		checkDb();
		String sql = "select " + TABLE_COLUMNS + " from " + TABLE_NAME + " where 1=1 "
				+ " and " + COLUMN_ACCOUNT + " =? order by " + COLUMN_ID + " desc ";
		
		Log.d(TAG, "getMeasureResults : " + sql);
		try 
		{
			cursor = db.rawQuery(sql, new String[]{ account } );
						
			if ( cursor.moveToFirst() ) 
			{
				int count = cursor.getCount();
				for (int i = 0; i < count; i++) 
				{
					measureResult = new MeasureResult();
					measureResult.setId( cursor.getInt( 0 ) );
					measureResult.setAccount( cursor.getString( 1 ) );
					measureResult.setWeight( cursor.getFloat( 2 ) );
					measureResult.setBmi( cursor.getFloat( 3 ) );
					measureResult.setBodyFatRate( cursor.getFloat( 4 ) );
					measureResult.setMuscleProportion( cursor.getFloat( 5 ) );
					measureResult.setPhysicalAge( cursor.getFloat( 6 ) );
					measureResult.setSubcutaneousFat( cursor.getFloat( 7 ) );
					measureResult.setVisceralFat( cursor.getFloat( 8 ) );
					measureResult.setSubBasalMetabolism( cursor.getFloat( 9 ) );
					measureResult.setEuropeBasalMetabolism( cursor.getFloat( 10 ) );
					measureResult.setBoneMass( cursor.getFloat( 11 ) );
					measureResult.setWaterContent( cursor.getFloat( 12 ) );
					measureResult.setDate( cursor.getString( 13 ) );
					
					list.add( measureResult );
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
	
	public HashMap<String, String> getMeasureResults( String account, String startDate, String endDate ) 
	{
		HashMap<String, String> map = new HashMap<String, String>();
		checkDb();
		String sql = "select " + COLUMN_WEIGHT + ", " + COLUMN_DATE +  " from " + TABLE_NAME + " where 1=1 "
				+ " and " + COLUMN_ACCOUNT + " =? and "
				+ COLUMN_DATE + " >=? and " + COLUMN_DATE + " <= ? ";
		
		Log.d(TAG, "getMeasureResults : " + sql);
		try 
		{
			cursor = db.rawQuery(sql, new String[]{ account, startDate, endDate } );
						
			if ( cursor.moveToFirst() ) 
			{
				int count = cursor.getCount();
				for (int i = 0; i < count; i++) 
				{
					String weight = cursor.getString( 0 );
					String date = cursor.getString( 1 );
					
					map.put(date, weight);
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
		
		return map;
	}
}
