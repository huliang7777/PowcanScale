package com.powcan.scale.dialog;

import com.powcan.scale.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
/**
 * 选中性别弹出框
 * @author Administrator
 *
 */
public class SelectGenderDialog extends Dialog implements android.view.View.OnClickListener
{
	private Context mContext;
	private GenderSelectEvent mGenderSelectEvent;
	
	private String gender;
	private ImageView ivMale;
	private ImageView ivFemale;
	
	public SelectGenderDialog( Context context, String gender, GenderSelectEvent mGenderSelectEvent ) 
	{
		super( context, R.style.LoadDialogStyle );
		
		this.mContext = context;
		this.gender = gender;
		this.mGenderSelectEvent = mGenderSelectEvent;

		init();
	}
	
	/**
	 * 初始化选择性别数据
	 */
	private void init()
	{
		LayoutInflater inflater = LayoutInflater.from( mContext );
        View view = inflater.inflate( R.layout.dialog_select_gender, null );
        setContentView(view);
        
        ivMale = (ImageView) view.findViewById(R.id.iv_male);
        ivFemale = (ImageView) view.findViewById(R.id.iv_female);
        
        ivMale.setOnClickListener( this );
        ivFemale.setOnClickListener( this );
        
        if ( gender.equals("M") )
        {
        	ivMale.setImageResource(R.drawable.icon_frame);
        }
        else if ( gender.equals("F") )
        {
        	ivFemale.setImageResource(R.drawable.icon_frame);
        }
	}
	
	/**
	 * 性别选中按钮回调接口
	 * @author Administrator
	 *
	 */
	public interface GenderSelectEvent
	{
		public void onGenderSelect( int which );
	}

	/**
	 * 响应按钮点击事件
	 */
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
