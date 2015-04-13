package com.powcan.scale.dialog;

import com.powcan.scale.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class PerfectDataRemindDialog extends Dialog 
{
	private Context mContext;
	private CheckBox cbNoRemind;
	private Button btnOk;
	private Button btnCancel;
	private PerfectDataEvent event;
	
	public PerfectDataRemindDialog( Context context, PerfectDataEvent event ) 
	{
		super( context, R.style.DialogStyle );
		
		this.mContext = context;
		this.event = event;
		init();
	}
	
	private void init()
	{
		LayoutInflater inflater = LayoutInflater.from( mContext );
        View view = inflater.inflate( R.layout.dialog_perfectdata_remind, null );
        setContentView(view);
        
        cbNoRemind = (CheckBox) view.findViewById( R.id.cb_no_remind );
        btnOk = (Button) view.findViewById( R.id.btn_ok );
        btnCancel = (Button) view.findViewById( R.id.btn_cancel );
        
        setListener();
	}
	
	private void setListener()
	{
		btnOk.setOnClickListener( new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if ( event != null )
				{
					event.onButtonSelect( 1,  cbNoRemind.isChecked() );
				}
			}
		});
		btnCancel.setOnClickListener( new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				event.onButtonSelect( 2,  cbNoRemind.isChecked() );
			}
		});
	}
	
	public interface PerfectDataEvent
	{
		public void onButtonSelect( int which, boolean remind );
	}
}
