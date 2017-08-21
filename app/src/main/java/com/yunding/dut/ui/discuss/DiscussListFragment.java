package com.yunding.dut.ui.discuss;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.adapter.DiscussGroupListAdapter;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.presenter.discuss.DiscussGroupListPresenter;
import com.yunding.dut.ui.base.ToolBarFragment;
import com.yunding.dut.ui.me.MeActivity;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.FastBlurUtil;
import com.yunding.dut.view.DUTSwipeRefreshLayout;
import com.yunding.dut.view.DUTVerticalRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
public class DiscussListFragment extends ToolBarFragment implements IDiscussListView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.rv_discuss_list)
    DUTVerticalRecyclerView rvDiscussList;
    @BindView(R.id.srl_discuss_list)
    DUTSwipeRefreshLayout srlDiscussList;
    Unbinder unbinder;
    @BindView(R.id.tv_doing)
    TextView tvDoing;
    @BindView(R.id.line_doing)
    View lineDoing;
    @BindView(R.id.rl_doing)
    RelativeLayout rlDoing;
    @BindView(R.id.tv_not_begin)
    TextView tvNotBegin;
    @BindView(R.id.line_not_begin)
    View lineNotBegin;
    @BindView(R.id.rl_not_begin)
    RelativeLayout rlNotBegin;
    @BindView(R.id.tv_done)
    TextView tvDone;
    @BindView(R.id.line_done)
    View lineDone;
    @BindView(R.id.rl_done)
    RelativeLayout rlDone;
    @BindView(R.id.tv_out_date)
    TextView tvOutDate;
    @BindView(R.id.line_out_date)
    View lineOutDate;
    @BindView(R.id.rl_outof_date)
    RelativeLayout rlOutofDate;
    private DiscussGroupListAdapter mAdapter;
    private DiscussGroupListPresenter mPresenter;
    private List<DiscussListResp.DataBean> mData;
    private List<DiscussListResp.DataBean> mDataDoing=new ArrayList<>();
    private List<DiscussListResp.DataBean> mDataNotBegin=new ArrayList<>();
    private List<DiscussListResp.DataBean> mDataDone=new ArrayList<>();
    private List<DiscussListResp.DataBean> mDataOutOfDate=new ArrayList<>();
    private  String  whichStatus="0";//选择位置  0 进行中 1未开始 2已结束 3已过期 默认进行中
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
        setTitleInCenter("讨论组");
        setShowNavigation(false);
        mPresenter = new DiscussGroupListPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        srlDiscussList.setOnRefreshListener(this);
        rvDiscussList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getHoldingActivity(), DiscussActivity.class);
                DiscussListResp.DataBean bean = (DiscussListResp.DataBean) adapter.getData().get(position);
                intent.putExtra(DiscussActivity.DISCUSS_SUBJECT_INFO, bean);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadDiscussGroupList();
        SimpleDraweeView ss= getmToolbarIvMe();
        ss.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(DUTApplication.getUserInfo().getUSER_HEAD_BG())){
            if (TextUtils.isEmpty(DUTApplication.getUserInfo().getUserAvatar())){
                ss.setImageResource(R.drawable.ic_userhead);
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String blurBitmap2 = FastBlurUtil.GetUrlBitmap
                                (Apis.SERVER_URL + DUTApplication.getUserInfo().getUserAvatar(), 10,getHoldingActivity());
                    }
                }).start();
                ss.setImageURI(Uri.parse(Apis.SERVER_URL + DUTApplication.getUserInfo().getUserAvatar()));
            }

        }else{
            ss.setImageURI(Uri.fromFile(new File(DUTApplication.getUserInfo().getUSER_HEAD_BG())));
        }
        ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getHoldingActivity(), MeActivity.class));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        mPresenter.loadDiscussGroupList();
    }

    @Override
    public void showProgress() {
        srlDiscussList.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        if (srlDiscussList != null)
            srlDiscussList.setRefreshing(false);
    }
    @Override
    public void showNoData() {
        if (mAdapter != null)
            mAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvDiscussList.getParent());
    }
    @Override
    public void showBadNetwork() {
        if (mAdapter != null)
            mAdapter.setEmptyView(R.layout.layout_bad_network, (ViewGroup) rvDiscussList.getParent());
    }
    @Override
    public void showDiscussList(DiscussListResp resp) {
        if (mAdapter == null) {
            mData=resp.getData();

                for (int i=0;i<mData.size();i++){
                    if (mData.get(i).getState()==0){
                        mDataNotBegin.add(mData.get(i));
                    }else if (mData.get(i).getState()==1){
                        mDataDoing.add(mData.get(i));
                    }else if (mData.get(i).getState()==2){
                        mDataDone.add(mData.get(i));
                    }else if(mData.get(i).getState()==3){
                        mDataOutOfDate.add(mData.get(i));
                    }

                }
                if ("0".equals(whichStatus)){
                    mAdapter = new DiscussGroupListAdapter(mDataDoing);

                }else if ("1".equals(whichStatus)){
                    mAdapter = new DiscussGroupListAdapter(mDataNotBegin);

                }else if ("2".equals(whichStatus)){
                    mAdapter = new DiscussGroupListAdapter(mDataDone);

                }else if ("3".equals(whichStatus)){
                    mAdapter = new DiscussGroupListAdapter(mDataOutOfDate);

                }
            mAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvDiscussList.getParent());

            rvDiscussList.setAdapter(mAdapter);

        } else {
            mData.clear();
            mDataDoing.clear();
            mDataDone.clear();
            mDataNotBegin.clear();
            mDataOutOfDate.clear();
            mData.addAll(resp.getData());
            for (int i=0;i<mData.size();i++){
                if (mData.get(i).getState()==0){
                    mDataNotBegin.add(mData.get(i));
                }else if (mData.get(i).getState()==1){
                    mDataDoing.add(mData.get(i));
                }else if (mData.get(i).getState()==2){
                    mDataDone.add(mData.get(i));
                }else if(mData.get(i).getState()==3){
                    mDataOutOfDate.add(mData.get(i));
                }

            }
            if ("0".equals(whichStatus)){
                mAdapter.setNewData(mDataDoing);

            }else if ("1".equals(whichStatus)){
                mAdapter.setNewData(mDataNotBegin);

            }else if ("2".equals(whichStatus)){
                mAdapter.setNewData(mDataDone);

            }else if ("3".equals(whichStatus)){
                mAdapter.setNewData(mDataOutOfDate);
            }
        }
    }

    @Override
    public void showListFailed() {
        getHoldingActivity().showToast(R.string.server_error);
    }
    @OnClick({R.id.rl_doing, R.id.rl_not_begin, R.id.rl_done, R.id.rl_outof_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_doing:
                whichStatus="0";
                if (mAdapter == null) {
                    mPresenter.loadDiscussGroupList();
                }else {
                    mAdapter.setNewData(mDataDoing);
                    tvDoing.setTextColor(getResources().getColor(R.color.orange));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvDone.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvOutDate.setTextColor(getResources().getColor(R.color.tab_text));
                    lineOutDate.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvNotBegin.setTextColor(getResources().getColor(R.color.tab_text));
                    lineNotBegin.setBackgroundColor(getResources().getColor(R.color.bg_white));
                }
                break;
            case R.id.rl_not_begin:
                whichStatus="1";
                if (mAdapter == null) {
                    mPresenter.loadDiscussGroupList();

                }else {
                    mAdapter.setNewData(mDataNotBegin);
                    tvNotBegin.setTextColor(getResources().getColor(R.color.orange));
                    lineNotBegin.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvDone.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvOutDate.setTextColor(getResources().getColor(R.color.tab_text));
                    lineOutDate.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvDoing.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.bg_white));
                }
                break;
            case R.id.rl_done:
                whichStatus="2";
                if (mAdapter == null) {
                    mPresenter.loadDiscussGroupList();

                }else {
                    mAdapter.setNewData(mDataDone);
                    tvDoing.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvDone.setTextColor(getResources().getColor(R.color.orange));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvOutDate.setTextColor(getResources().getColor(R.color.tab_text));
                    lineOutDate.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvNotBegin.setTextColor(getResources().getColor(R.color.tab_text));
                    lineNotBegin.setBackgroundColor(getResources().getColor(R.color.bg_white));
                }
                break;
            case R.id.rl_outof_date:
                whichStatus="3";
                if (mAdapter == null) {
                    mPresenter.loadDiscussGroupList();
                }else {
                    mAdapter.setNewData(mDataOutOfDate);
                    tvDoing.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvDone.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvOutDate.setTextColor(getResources().getColor(R.color.orange));
                    lineOutDate.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvNotBegin.setTextColor(getResources().getColor(R.color.tab_text));
                    lineNotBegin.setBackgroundColor(getResources().getColor(R.color.bg_white));
                }
                break;
        }
    }
}
