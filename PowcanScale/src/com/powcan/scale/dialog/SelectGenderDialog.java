package com.powcan.scale.dialog;

import com.powcan.scale.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class SelectGenderDialog extends Dialog implements android.view.View.OnClickListener
{
	private Context mContext;
	private GenderSelectEvent mGenderSelectEvent;
	
	private ImageView ivMale;
	private ImageView ivFemale;
	
	public SelectGenderDialog( Context context, GenderSelectEvent mGenderSelectEvent ) 
	{
		super( context, R.style.LoadDialogStyle );
		
		this.mContext = context;
		this.mGenderSelectEvent = mGenderSelectEvent;

		init();
	}
	
	private void init()
	{
		LayoutInflater inflater = LayoutInflater.from( mContext );
        View view = inflater.inflate( R.layout.dialog_select_gender, null );
        setContentView(view);
        
        ivMale = (ImageView) view.findViewById(R.id.iv_male);
        ivFemale = (ImageView) view.findViewById(R.id.iv_female);
        
        ivMale.setOnClickListener( this );
        ivFemale.setOnClickListener( this );
	}
	
	public interface GenderSelectEvent
	{
		public void onGenderSelect( int which );
	}

	@Override
	public void onClick(View v) 
	{
		switch ( v.getId() ) 
		{
			case R.id.iv_male:
				if( mGenderSelectEvent != null )
				{
					mGenderSelectEvent.onGenderSelect( 1 );
				}
				break;
	
			case R.id.iv_female:
				if( mGenderSelectEvent != null )
				{
					mGenderSelectEvent.onGenderSelect( 2 );
				}
				break;
		}
	}
}
