package com.powcan.scale.dialog;

import com.powcan.scale.R;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 加载弹出框
 * @author Administrator
 *
 */
public class LoadingDialog extends Dialog 
{
	private Context mContext;
	private String content;
	
	public LoadingDialog( Context context, String content ) 
	{
		super( context, R.style.LoadDialogStyle );
		
		this.mContext = context;
		this.content = content;

		init();
	}
	
	/**
	 * 初始化加载界面
	 */
	private void init()
	{
		LayoutInflater inflater = LayoutInflater.from( mContext );
        View view = inflater.inflate( R.layout.dialog_loading, null );
        setContentView(view);
        
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        tvContent.setText( content );
	}
}
