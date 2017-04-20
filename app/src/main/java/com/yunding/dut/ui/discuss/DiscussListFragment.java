package com.yunding.dut.ui.discuss;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yunding.dut.R;
import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.ui.base.ToolBarFragment;
import com.yunding.dut.ui.me.MeActivity;
import com.yunding.dut.view.DUTSwipeRefreshLayout;
import com.yunding.dut.view.DUTVerticalRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类 名 称：DiscussFragment
 * <P/>描    述：讨论
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/19 19:20
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/19 19:20
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class DiscussListFragment extends ToolBarFragment implements IDiscussListView,SwipeRefreshLayout.OnRefreshListener{


    @BindView(R.id.rv_discuss_list)
    DUTVerticalRecyclerView rvDiscussList;
    @BindView(R.id.srl_discuss_list)
    DUTSwipeRefreshLayout srlDiscussList;
    Unbinder unbinder;

    public DiscussListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discuss;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        setTitleInCenter("讨论");
        setShowNavigation(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_discuss, menu);//这种方法设置menu不显示
        getmToolbar().getMenu().clear();
        getmToolbar().inflateMenu(R.menu.menu_discuss);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_me:
                startActivity(new Intent(getHoldingActivity(), MeActivity.class));
                break;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void showProgress() {
        srlDiscussList.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        srlDiscussList.setRefreshing(false);
    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showBadNetwork() {

    }

    @Override
    public void showDiscussList(DiscussListResp resp) {

    }
}
