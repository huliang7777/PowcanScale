package com.powcan.scale.adapter;

import java.util.HashMap;
import java.util.List;

import com.powcan.scale.R;
import com.powcan.scale.bean.MeasureResult;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.db.MeasureResultDb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserListAdapter extends BaseAdapter {

	private Context mContext;
	private List<UserInfo> items;
	private LayoutInflater mInflater;
	private HashMap<String, MeasureResult> measureMap;
	private MeasureResultDb dbMeasureResult;

	public UserListAdapter(Context context, List<UserInfo> list) 
	{
		this.mInflater = LayoutInflater.from(context);
		this.items = list;
		
		initData();
	}
	
	private void initData()
	{
		measureMap = new HashMap<String, MeasureResult>();
		dbMeasureResult = new MeasureResultDb(mContext);
		MeasureResult result = null;
		String account = null;
		for ( int i=0;i<items.size();i++ )
		{
			account = items.get( i ).getAccount();
			result = dbMeasureResult.getLastMeasureResult( account );
			if( result != null )
			{
				measureMap.put( account, result );
			}
		}
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.item_user, null);

		TextView tvName = (TextView) convertView
				.findViewById(R.id.tv_name);
		TextView tvData = (TextView) convertView
				.findViewById(R.id.tv_data);
		TextView tvWeight = (TextView) convertView
				.findViewById(R.id.tv_weight);
		ImageView ivImage = (ImageView) convertView
				.findViewById(R.id.iv_image);

		UserInfo user = items.get(position);
		String username = user.getUsername();
		tvName.setText( username );
		
		if( user.getGender().equalsIgnoreCase("f") )
		{
			ivImage.setImageResource(R.drawable.icon_female);
		}
		
		MeasureResult result = measureMap.get( user.getAccount() );
		if ( result != null )
		{
			tvData.setText( "上次体检：" + result.getDate() + "\n距减重目标还有" + Math.abs( (int)result.getWeight() - Integer.valueOf( user.getGoalWeight() ) ) + "KG" );
			tvWeight.setText( String.format( "体重%dKG" , (int)result.getWeight() ) );
			tvWeight.setVisibility(View.VISIBLE);
		}
		else
		{
			tvWeight.setVisibility(View.GONE);
			tvData.setText("亲，还没有测量体重，赶紧测量体重哦~");
		}
		return convertView;
	}

}
