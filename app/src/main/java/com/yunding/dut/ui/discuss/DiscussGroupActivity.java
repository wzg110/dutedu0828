package com.yunding.dut.ui.discuss;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.widget.TextView;

import com.yunding.dut.R;
import com.yunding.dut.adapter.DiscussGroupMemberAdapter;
import com.yunding.dut.model.resp.discuss.DiscussGroupInfoResp;
import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.presenter.discuss.DiscussGroupMemberPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.view.DUTGridRecycleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscussGroupActivity extends ToolBarActivity implements IDiscussGroupView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_group_list)
    DUTGridRecycleView rvGroupList;
    @BindView(R.id.tv_group_name)
    TextView tvGroupName;
    @BindView(R.id.tv_group_grade)
    TextView tvGroupGrade;
    @BindView(R.id.tv_group_class)
    TextView tvGroupClass;
    @BindView(R.id.srl_group_info)
    SwipeRefreshLayout srlGroupInfo;
    private DiscussListResp.DataBean mDiscussInfo;
    public static final String GROUP_INFO = "group_info";

    private DiscussGroupMemberAdapter mAdapter;
    private DiscussGroupMemberPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss_group);
        ButterKnife.bind(this);

        srlGroupInfo.setOnRefreshListener(this);
        mDiscussInfo = (DiscussListResp.DataBean) getIntent().getSerializableExtra(GROUP_INFO);
        mPresenter = new DiscussGroupMemberPresenter(this);
        if (mDiscussInfo != null) {
            mPresenter.loadDiscussGroupMembers(mDiscussInfo.getGroupId());
            setTitle(mDiscussInfo.getGroupName());
        }
    }

    @Override
    public void showGroupInfo(DiscussGroupInfoResp resp) {
        if (mAdapter == null) {
            mAdapter = new DiscussGroupMemberAdapter(resp.getData().getStudents());
            rvGroupList.setSpanCount(this, 4);
            rvGroupList.setAdapter(mAdapter);
        } else {
            mAdapter.setNewData(resp.getData().getStudents());
        }

        setTitle(mDiscussInfo.getGroupName() + "（" + resp.getData().getStudents().size() + "人）");
        tvGroupName.setText(mDiscussInfo.getGroupName());
        tvGroupGrade.setText(resp.getData().getGroup().getGradeName());
        tvGroupClass.setText(resp.getData().getGroup().getClassName());
    }

    @Override
    public void showGroupFailed(String msg) {
        if (TextUtils.isEmpty(msg)) {
            showToast(R.string.server_error);
        } else {
            showToast(msg);
        }
    }

    @Override
    public void showProgress() {
        srlGroupInfo.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        srlGroupInfo.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadDiscussGroupMembers(mDiscussInfo.getGroupId());
    }
}
