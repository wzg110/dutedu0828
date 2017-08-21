package com.yunding.dut.ui.ppt;

import android.os.Bundle;

import com.yunding.dut.ui.base.BaseFragmentInReading;

/**
 * 类 名 称：BackHandledFragment
 * <P/>描    述：捕获fragment返回键事件基类
 * <P/>创 建 人：Administrator
 * <P/>创建时间：2017/8/14 11:33
 * <P/>修 改 人：Administrator
 * <P/>修改时间：2017/8/14 11:33
 * <P/>修改备注：
 * <P/>版    本：
 */
public abstract class BackHandledFragment extends BaseFragmentInReading {

	protected BackHandledInterface mBackHandledInterface;

	protected abstract boolean onBackPressed();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!(getActivity() instanceof BackHandledInterface)){
			throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
		}else{
			this.mBackHandledInterface = (BackHandledInterface)getActivity();
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();

		mBackHandledInterface.setSelectedFragment(this);
	}
	
}
