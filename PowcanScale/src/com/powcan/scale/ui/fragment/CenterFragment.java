package com.powcan.scale.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.powcan.scale.MainActivity;
import com.powcan.scale.R;
import com.powcan.scale.bean.CurUserInfo;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.ui.base.BaseFragment;
import com.powcan.scale.util.SpUtil;

/**
 * 主界面管理类
 * @author Administrator
 *
 */
public class CenterFragment extends Fragment implements OnClickListener {
	private ViewPager mViewPager;
	private ImageView mLeftToogle;
	private ImageView mRightToogle;
    private TextView mConnectionState;
    private TextView tvName;

	private OnViewPagerChangeListener listener;

	private MyPagerAdapter adapter;

	private ArrayList<BaseFragment> pagerItemList = new ArrayList<BaseFragment>();
	
	/**
	 * 创建view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_center, null);
		
		findView(view);
		initView(view);
		return view;
	}
	
	/**
	 * 查找子view
	 * @param view
	 */
	private void findView(View view) {
		mViewPager = (ViewPager) view.findViewById(R.id.vp);
		mLeftToogle = (ImageView) view.findViewById(R.id.iv_center_left);
		mRightToogle = (ImageView) view.findViewById(R.id.iv_center_right);
        mConnectionState = (TextView) view.findViewById(R.id.connection_state);
        tvName = (TextView) view.findViewById(R.id.tv_name);
	}
	
	/**
	 * 初始化iew数据
	 * @param view
	 */
	private void initView(View view) {
		mLeftToogle.setOnClickListener(this);
		mRightToogle.setOnClickListener(this);

		pagerItemList.add(HomeFragment.getInstance(1));
		pagerItemList.add(ChartFragment.getInstance(2));
		pagerItemList.add(ReportFragment.getInstance(3));

		adapter = new MyPagerAdapter(getFragmentManager());

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (listener != null) {
					listener.onPageChage(position);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		mViewPager.setAdapter(adapter);
		
		
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		tvName.setText( CurUserInfo.getInstance( getActivity() ).getCurUser().getUsername() );
	}

	/**
	 * 设置点击事件处理类
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iv_center_left:
			((MainActivity) this.getActivity()).showLeftViewToogle();
			break;
		case R.id.iv_center_right:
			((MainActivity) this.getActivity()).showRightViewToogle();
			break;
		default:
			break;
		}
	}

	/**
	 * 设置界面改变监听
	 * @param listener
	 */
	public void setOnViewPagerChangeListener(OnViewPagerChangeListener listener) {
		this.listener = listener;
	}

	/**
	 * 是否为第一页
	 * @return
	 */
	public boolean isFirst() {
		return mViewPager.getCurrentItem() == 0;
	}

	/**
	 * 是否为最后页
	 * @return
	 */
	public boolean isLast() {
		return mViewPager.getCurrentItem() == pagerItemList.size() - 1;
	}

	/**
	 * 碎片page视图适配类
	 * @author Administrator
	 *
	 */
	private class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return pagerItemList.get(arg0);
		}

		@Override
		public int getCount() {
			return 3;
		}

	}

	/**
	 * Interface when the page have changed.
	 * viewpager监听接口
	 */
	public interface OnViewPagerChangeListener {
		void onPageChage(int position);
	}

	/**
	 * 更新连接状态
	 * @param resourceId
	 */
	public void updateConnectionState(int resourceId) {
		if (mConnectionState != null) {
			mConnectionState.setText(resourceId);
		}
	}

	/**
	 * 设置测量数据
	 * @param weight
	 * @param bodyFatRate
	 * @param waterContent
	 */
	public void setWeightData( float weight, float bodyFatRate, float waterContent )
	{
		HomeFragment fragment = (HomeFragment)(pagerItemList.get(0));
		fragment.setWeightData( weight, bodyFatRate, waterContent );
		
		ChartFragment chartFragment = (ChartFragment)(pagerItemList.get(1));
		chartFragment.reloadData();
	}
	
	/**
	 * 加载数据
	 */
	public void reloadData()
	{
		tvName.setText( CurUserInfo.getInstance( getActivity() ).getCurUser().getUsername() );
		int size = pagerItemList.size();
		for ( int i=0;i<size;i++ )
		{
			BaseFragment fragment = pagerItemList.get( i );
			fragment.reloadData();
		}
	}
}
