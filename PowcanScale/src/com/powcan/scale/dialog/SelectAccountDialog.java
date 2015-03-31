package com.powcan.scale.dialog;

import java.util.ArrayList;
import java.util.List;

import com.powcan.scale.R;
import com.powcan.scale.adapter.AccountAdapter;

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

public class SelectAccountDialog extends Dialog 
{
	private Context mContext;
	private ListView listAccount;
	private List<Integer> list;
	private AccountAdapter mAdapter;
	
	private ItemClickEvent mItemClickEvent;
	
	public SelectAccountDialog( Context context, List<Integer> list, ItemClickEvent mItemClickEvent ) 
	{
		super( context, R.style.Dialog );
		
		this.mContext = context;
		this.list = list;
		if ( this.list == null )
		{
			this.list = new ArrayList<Integer>();
		}
		this.mItemClickEvent = mItemClickEvent;
	 	
		init();
	}
	
	private void init()
	{
		LayoutInflater inflater = LayoutInflater.from( mContext );
        View view = inflater.inflate( R.layout.dialog_select_account, null );
        setContentView(view);
        
        listAccount = (ListView) view.findViewById(R.id.list_account);
        
        mAdapter = new AccountAdapter( mContext, list );
        listAccount.setAdapter(mAdapter);
        
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        
        dialogWindow.setAttributes(lp);
        
        setListener();
	}

	private void setListener()
	{
		listAccount.setOnItemClickListener( new OnItemClickListener() {

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
