package com.yunding.dut.ui.reading;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yunding.dut.R;
import com.yunding.dut.adapter.ReadingListAdapter;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.presenter.reading.ReadingListPresenter;
import com.yunding.dut.ui.base.ToolBarFragment;
import com.yunding.dut.ui.me.MeActivity;
import com.yunding.dut.view.DUTSwipeRefreshLayout;
import com.yunding.dut.view.DUTVerticalRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类 名 称：ReadingFragment
 * <P/>描    述：阅读
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/19 19:21
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/19 19:21
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class ReadingListFragment extends ToolBarFragment implements IReadingListView ,SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.rv_reading_list)
    DUTVerticalRecyclerView rvReadingList;
    @BindView(R.id.srl_reading_list)
    DUTSwipeRefreshLayout srlReadingList;
    Unbinder unbinder;
    private ReadingListAdapter mAdapter;
    private List<ReadingListResp.DataBean> mData;

    private ReadingListPresenter mPresenter;

    public ReadingListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reading_list;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        setTitleInCenter("阅读");
        setShowNavigation(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_reading,menu);//这种方法设置menu不显示
        getmToolbar().getMenu().clear();
        getmToolbar().inflateMenu(R.menu.menu_reading);
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
    public void showProgress() {
        if (srlReadingList != null)
            srlReadingList.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        if (srlReadingList != null)
            srlReadingList.setRefreshing(false);
    }

    @Override
    public void showReadingList(ReadingListResp resp) {
        mData = new ArrayList<>();
        if (mAdapter == null) {
            mAdapter = new ReadingListAdapter(mData);
            rvReadingList.setAdapter(mAdapter);
        } else {
            mData.clear();
        }
        mData.addAll(resp.getData());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            showToast(getString(R.string.server_error));
        } else {
            showToast(msg);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        mPresenter = new ReadingListPresenter(this);

        srlReadingList.setOnRefreshListener(this);
        rvReadingList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ReadingListResp.DataBean data = mData.get(position);
                Intent intent = new Intent(getHoldingActivity(), ReadingActivity.class);
                intent.putExtra(ReadingActivity.READING_INFO, data);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getReadingList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        mPresenter.getReadingList();
    }
}
