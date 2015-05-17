package com.powcan.scale.dialog;

import java.util.ArrayList;
import java.util.List;

import com.powcan.scale.R;
import com.powcan.scale.adapter.DataAdapter;
import com.powcan.scale.util.Utils;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 字符串数据选择弹出框
 * @author Administrator
 *
 */
public class SelectStrDataDialog extends Dialog 
{
	private Context mContext;
	private ListView listView;
	private List<String> list;
	private DataAdapter mAdapter;
	private TextView tvTitle;
	
	private String title;
	private int index;
	
	private ItemClickEvent mItemClickEvent;
	
	public SelectStrDataDialog( Context context, List<String> list, String title, int index, ItemClickEvent mItemClickEvent ) 
	{
		super( context, R.style.Dialog );
		
		this.mContext = context;
		this.list = list;
		this.title = title;
		this.index = index;
		if ( this.list == null )
		{
			this.list = new ArrayList<String>();
		}
		this.mItemClickEvent = mItemClickEvent;
	 	
		init();
	}
	
	private void init()
	{
		LayoutInflater inflater = LayoutInflater.from( mContext );
        View view = inflater.inflate( R.layout.dialog_select_account, null );
        setContentView(view);
        
        listView = (ListView) view.findViewById(R.id.list_account);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText( title );
        
        mAdapter = new DataAdapter( mContext, list );
        listView.setAdapter(mAdapter);
        
        listView.setSelection( index );
        
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        lp.height = Utils.dip2px(mContext, 300);
        dialogWindow.setAttributes(lp);
        
        setListener();
	}

	private void setListener()
	{
		listView.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View view, int position,
					long arg3) 
			{
				if( mItemClickEvent != null )
				{
					mItemClickEvent.onItemClick(position);
				}
			}
			
		});
	}
	
	public interface ItemClickEvent
	{
		public void onItemClick( int which );
	}
}
