package com.powcan.scale.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.powcan.scale.R;
import com.powcan.scale.adapter.UserListAdapter;
import com.powcan.scale.bean.CurUserInfo;
import com.powcan.scale.bean.MeasureResult;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.db.MeasureResultDb;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.ui.LoginActivity;
import com.powcan.scale.ui.base.BaseFragment;
import com.powcan.scale.ui.profile.ProfileActivity;
import com.powcan.scale.ui.profile.UserInfoDetailActivity;
import com.powcan.scale.ui.settings.SetGoalActivity;
import com.powcan.scale.ui.settings.SettingsActivity;
import com.powcan.scale.util.SpUtil;
import com.powcan.scale.util.Utils;

/**
 * 左边用户列表界面
 * @author Administrator
 *
 */
public class LeftFragment extends BaseFragment implements OnClickListener {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    
    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;
    
    private ListView mDrawerListView;

    private int mCurrentSelectedPosition = 0;
    private boolean mUserLearnedDrawer;

	private View mBtnSettings;
	private View imgAdd;
	private View llUser;
	private TextView tvName;
	private TextView tvData;
	private TextView tvWeight;
	private ImageView ivImage;

	private UserInfoDb dbUserInfo;
	private ArrayList<UserInfo> users;
	private UserListAdapter mAdapter;
	private MeasureResultDb dbMeasureResult;
	
	/**
	 * 创建view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }
		return inflater.inflate(R.layout.fragment_navigation_drawer, null);
	}

	/**
	 * 初始化
	 */
	@Override
	public void onInit() {
		// Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        // Select either the default item (0) or the last selected item.
//        selectItem(mCurrentSelectedPosition);
        
		dbUserInfo = new UserInfoDb( mContext ); 
		dbMeasureResult = new MeasureResultDb( mContext );
	}

	/**
	 * 查找子view
	 */
	@Override
	public void onFindViews() {
		View mDrawer = getView();
    	
        mDrawerListView = (ListView) mDrawer.findViewById(R.id.listView);        
        mBtnSettings = mDrawer.findViewById(R.id.btn_settings);
        tvName = (TextView) mDrawer.findViewById(R.id.tv_name);
		tvData = (TextView) mDrawer.findViewById(R.id.tv_data);
		tvWeight = (TextView) mDrawer.findViewById(R.id.tv_weight);
		ivImage = (ImageView) mDrawer.findViewById(R.id.iv_image);
        imgAdd = mDrawer.findViewById(R.id.img_add);
        llUser = mDrawer.findViewById(R.id.ll_curUser);
	}

	/**
	 * 初始化view数据
	 */
	@Override
	public void onInitViewData() {
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
	}
	
	/**
	 * 绑定事件监听
	 */
	@Override
	public void onBindListener() {
		mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        mBtnSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
		        if (mCallbacks != null) {
		            mCallbacks.onNavigationDrawerItemSelected(R.id.btn_settings, null);
		    		Intent intent = new Intent(getActivity(), SettingsActivity.class);
		    		startActivity(intent);
		        }
			}
		});
        
        llUser.setOnClickListener( this );
        imgAdd.setOnClickListener( this );
	}

	/**
	 * attach到window
	 */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    /**
	 * 从window中detach
	 */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
	 * 保存实例状态
	 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    /**
	 * 选中列表，进行用户切换
	 */
    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        
        Object obj = null;
        if (mDrawerListView != null) {
        	obj = users.get( position ).getAccount();
            mDrawerListView.setItemChecked(position, true);
        }
        closeDrawer();
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position, obj);
        }
    }
    
    /**
     * 关闭drawer
     */
    private void closeDrawer() {
        if (!mUserLearnedDrawer) {
            // The user manually opened the drawer; store this flag to prevent auto-showing
            // the navigation drawer automatically in the future.
            mUserLearnedDrawer = true;
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
        }
	}

	/**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position, Object obj);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	reloadData();
    }

    /**
	 * 点击事件处理方法
	 */
	@Override
	public void onClick(View view) 
	{
		switch ( view.getId() ) 
		{
			case R.id.ll_curUser:
//				Intent intent = new Intent(mContext, UserInfoDetailActivity.class);
//				startActivity(intent);
//				Intent intent = new Intent( mContext, ProfileActivity.class );
//				intent.putExtra( "from", "UserInfoDetail" );
//				startActivity(intent);
				Intent intent = new Intent( mContext, SetGoalActivity.class );
				startActivity( intent );
				break;
			case R.id.img_add:
				intent = new Intent(mContext, LoginActivity.class);
				intent.putExtra( "from", "LeftFragment" );
				startActivity(intent);
				break;
		}
	}
	
	/**
	 * 重新加载数据
	 */
	@Override
	public void reloadData() 
	{
		super.reloadData();
		if ( mContext == null )
		{
			return;
		}
		users = dbUserInfo.getUserInfoes( CurUserInfo.getInstance( mContext ).getCurUser().getAccount() );
		mAdapter = new UserListAdapter(mContext, users);
		mDrawerListView.setAdapter( mAdapter );
		
		UserInfo user = CurUserInfo.getInstance( mContext ).getCurUser();//dbUserInfo.getUserInfo( CurUserInfo.getInstance( mContext ).getCurUser().getAccount() );
        String username = user.getUsername();
		tvName.setText( username );
		
		if( user.getGender().equalsIgnoreCase("f") )
		{
			ivImage.setImageResource(R.drawable.icon_female);
		}
		else
		{
			ivImage.setImageResource(R.drawable.icon_male);
		}
		
		MeasureResult result = dbMeasureResult.getLastMeasureResult( user.getAccount() );
		if ( result != null )
		{
			tvData.setText( "上次体检：" + result.getDate() + "\n距减重目标还有" + Utils.formatTwoFractionDigits( Math.abs( result.getWeight() - Float.valueOf( user.getGoalWeight() ) ) ) + "KG" );
			tvWeight.setText( String.format( "体重%sKG" , Utils.formatTwoFractionDigits( result.getWeight() ) ) );
			tvWeight.setVisibility(View.VISIBLE);
		}
		else
		{
			tvWeight.setVisibility(View.GONE);
			tvData.setText("亲，还没有测量体重，赶紧测量体重哦~");
		}
	}
}
