package com.yunding.dut.ui.ppt;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yunding.dut.R;
import com.yunding.dut.adapter.CourseListAdapter;
import com.yunding.dut.model.resp.ppt.CourseListResp;
import com.yunding.dut.presenter.ppt.CourseListPresenter;
import com.yunding.dut.ui.base.ToolBarFragment;
import com.yunding.dut.view.DUTSwipeRefreshLayout;
import com.yunding.dut.view.DUTVerticalRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类 名 称：CourseListFragment
 * <P/>描    述：首页->课堂
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 13:59
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 13:59
 * <P/>修改备注：
 * <P/>版    本：
 */
public class CourseListFragment extends ToolBarFragment implements SwipeRefreshLayout.OnRefreshListener, ICourseListView {

    @BindView(R.id.rv_course_list)
    DUTVerticalRecyclerView rvCourseList;
    @BindView(R.id.srl_course_list)
    DUTSwipeRefreshLayout srlCourseList;
    Unbinder unbinder;

    private CourseListPresenter mPresenter;
    private CourseListAdapter mAdapter;

    public CourseListFragment() {
        mPresenter = new CourseListPresenter(this);
        mAdapter = new CourseListAdapter(new ArrayList<CourseListResp.DataBean.DatasBean>());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        rvCourseList.setAdapter(mAdapter);
        rvCourseList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getHoldingActivity(), PPTListActivity.class);
                intent.putExtra("course_id", mAdapter.getData().get(position).getId());
                startActivity(intent);
            }
        });
        srlCourseList.setOnRefreshListener(this);

        mPresenter.loadCourseList();
        return rootView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_course_list;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        setTitleInCenter("课堂");
        setShowNavigation(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        mPresenter.loadCourseList();
    }

    @Override
    public void showProgress() {
        srlCourseList.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        srlCourseList.setRefreshing(false);
    }

    @Override
    public void showCourseList(CourseListResp resp) {
        List<CourseListResp.DataBean.DatasBean> list = resp.getData().getDatas();
        mAdapter.setNewData(list);
    }

    @Override
    public void showNoData() {
        mAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvCourseList.getParent());
    }

    @Override
    public void showListFailed() {
        mAdapter.setEmptyView(R.layout.layout_bad_network, (ViewGroup) rvCourseList.getParent());
    }
}
