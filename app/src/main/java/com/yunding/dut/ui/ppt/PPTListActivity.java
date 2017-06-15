package com.yunding.dut.ui.ppt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yunding.dut.R;
import com.yunding.dut.adapter.PPTListAdapter;
import com.yunding.dut.model.resp.ppt.PPTListResp;
import com.yunding.dut.presenter.ppt.PPTListPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.view.DUTSwipeRefreshLayout;
import com.yunding.dut.view.DUTVerticalRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PPTListActivity extends ToolBarActivity implements IPPTListView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_ppt_list)
    DUTVerticalRecyclerView rvPptList;
    @BindView(R.id.srl_ppt_list)
    DUTSwipeRefreshLayout srlPptList;

    private PPTListPresenter mPresenter;
    private PPTListAdapter mAdapter;

    private int mCourseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pptlist);
        ButterKnife.bind(this);

        mPresenter = new PPTListPresenter(this);
        mAdapter = new PPTListAdapter(new ArrayList<PPTListResp.DataBean>());
        rvPptList.setAdapter(mAdapter);
        rvPptList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PPTListActivity.this, PPTInfoActivity.class);
                intent.putExtra("ppt_info", mAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        mCourseId = getIntent().getIntExtra("course_id", 0);
        if (mCourseId > 0) {
            mPresenter.loadPPTList(mCourseId);
        } else {
            showToast("请重新打开页面");
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.loadPPTList(mCourseId);
    }

    @Override
    public void showProgress() {
        srlPptList.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        srlPptList.setRefreshing(false);
    }

    @Override
    public void showNoData() {
        mAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvPptList.getParent());
    }

    @Override
    public void showBadNetwork() {
        mAdapter.setEmptyView(R.layout.layout_bad_network, (ViewGroup) rvPptList.getParent());
    }

    @Override
    public void showListFailed() {

    }

    @Override
    public void showPPTList(PPTListResp resp) {
        List<PPTListResp.DataBean> list = resp.getData();
        mAdapter.setNewData(list);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.loadPPTList(mCourseId);
            }
        }, 3000);
    }
}
