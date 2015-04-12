package com.powcan.scale.dialog;

import java.util.ArrayList;
import java.util.List;

import com.powcan.scale.R;
import com.powcan.scale.adapter.AccountAdapter;
import com.powcan.scale.util.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SelectHeightDialog extends Dialog
{
	private Context mContext;
	private ListView listHeight;
	private List<Integer> list;
	private AccountAdapter mAdapter;
	private int curHeight;
	private TextView tvHeight;
	private Button btnOk;
	
	private int genderRes;
	
	private static final int MAX_HEIGHT = 230;
	
	private ItemClickEvent mItemClickEvent;
	
	public SelectHeightDialog( Context context, String height, String gender, ItemClickEvent mItemClickEvent ) 
	{
		super( context, R.style.Dialog );
		
		this.mContext = context;
		this.mItemClickEvent = mItemClickEvent;
		if( TextUtils.isEmpty( height ) || height.equals( "0" ) )
		{
			curHeight = 170;
		}
		else
		{
			curHeight = Integer.valueOf( height );
		}
		if ( gender.equals("") || gender.equalsIgnoreCase( "O" ) || gender.equalsIgnoreCase( "M" ) )
		{
			this.genderRes = R.drawable.icon_male;
		}
		else
		{
			this.genderRes = R.drawable.icon_female;
		}
	 	
		init();
	}
	
	private void init()
	{
		LayoutInflater inflater = LayoutInflater.from( mContext );
        View view = inflater.inflate( R.layout.dialog_select_height, null );
        setContentView(view);
        
        this.list = new ArrayList<Integer>();
        int length = MAX_HEIGHT + 3;
        for( int i=29; i<=length; i++ )
        {
        	if ( i == 29 || i > MAX_HEIGHT  )
        	{
        		list.add( 0 );
        	}	
        	else
        	{	
        		list.add( i );
        	}
        }
        
        listHeight = (ListView) view.findViewById(R.id.list_height);
        tvHeight =  (TextView) view.findViewById(R.id.tv_height);
        btnOk = (Button) view.findViewById(R.id.btn_ok);
        
        mAdapter = new AccountAdapter( mContext, list );
        listHeight.setAdapter(mAdapter);
        
        listHeight.setSelection( curHeight - 30 );
        
        tvHeight.setText( curHeight + "CM" );
        
        tvHeight.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, this.genderRes );
        
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        
        dialogWindow.setAttributes(lp);
        
        setListener();
	}

	private void setListener()
	{
		this.setOnCancelListener( new OnCancelListener() {
			
			@Override
			public void onCancel( DialogInterface dialog ) 
			{
				if ( mItemClickEvent != null )
				{
					mItemClickEvent.onItemClick( curHeight );
				}
			}
		});
		
		btnOk.setOnClickListener( new  android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( mItemClickEvent != null )
				{
					mItemClickEvent.onItemClick( curHeight );
				}				
			}
		} );
			
		listHeight.setOnScrollListener( new OnScrollListener() 
		{
			@Override  
            public void onScrollStateChanged(AbsListView view, int scrollState) 
			{  
				
            }  
              
            @Override  
            public void onScroll(AbsListView view, int firstVisibleItem,  
                    int visibleItemCount, int totalItemCount) 
            { 
            	View c = listHeight.getChildAt(0);
            	if( c == null )
            	{
            		return;
            	}
            	int top = c.getTop();
            	
            	int h = -top + firstVisibleItem * Utils.dip2px(mContext, 60);
            	Log.d("SelectHeightDialog", "height : " + h );
//            	curHeight = firstVisibleItem + 30;
            	curHeight = ( h + Utils.dip2px(mContext, 15) ) / Utils.dip2px(mContext, 60)  + 30;
            	if ( curHeight > MAX_HEIGHT )
            	{
            		curHeight = MAX_HEIGHT;
            	}
            	tvHeight.setText( curHeight + "CM" );
            }
		});
	}
	
	public interface ItemClickEvent
	{
		public void onItemClick( int height );
	}
}
