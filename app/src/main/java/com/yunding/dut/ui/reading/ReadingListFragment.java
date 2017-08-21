package com.yunding.dut.ui.reading;


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
import com.yunding.dut.adapter.ReadingListAdapter;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.presenter.reading.ReadingListPresenter;
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
 * 类 名 称：ReadingFragment
 * <P/>描    述：阅读
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/19 19:21
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/19 19:21
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class ReadingListFragment extends ToolBarFragment implements IReadingListView, SwipeRefreshLayout.OnRefreshListener {
    //  阅读内容列表
    @BindView(R.id.rv_reading_list)
    DUTVerticalRecyclerView rvReadingList;
    //  搭载页面的swipe
    @BindView(R.id.srl_reading_list)
    DUTSwipeRefreshLayout srlReadingList;
    //    butterknife自动生成绑定
    Unbinder unbinder;
    @BindView(R.id.tv_doing)
    TextView tvDoing;
    @BindView(R.id.line_doing)
    View lineDoing;
    @BindView(R.id.rl_doing)
    RelativeLayout rlDoing;
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
    //    阅读listAdapter
    private ReadingListAdapter mAdapter;
    //    接口返回列表数据
    private List<ReadingListResp.DataBean> mData;
    //操作presenter
    private ReadingListPresenter mPresenter;
    private String whichStatus="0";//0进行中，1已完成，2已过期
    private List<ReadingListResp.DataBean> mDataDoing=new ArrayList<>();
    private List<ReadingListResp.DataBean> mDataDone=new ArrayList<>();
    private List<ReadingListResp.DataBean> mDataOutDate=new ArrayList<>();
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
        setTitleInCenter("阅读课");
        setShowNavigation(false);

    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
////        inflater.inflate(R.menu.menu_reading,menu);//这种方法设置menu不显示
//        getmToolbar().getMenu().clear();
//        getmToolbar().inflateMenu(R.menu.menu_reading_list);
//
//    }
//
//    //菜单跳转到我的信息
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_me:
//                startActivity(new Intent(getHoldingActivity(), MeActivity.class));
//                break;
//        }
//        return true;
//    }

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

    //view的实现
    @Override
    public void showReadingList(ReadingListResp resp) {
        if (mAdapter == null) {

            mData = resp.getData();
            for (int i=0;i<mData.size();i++){
                if (mData.get(i).getCompleted()== ReadingActivity.STATE_FINISHED){
                    mDataDone.add(mData.get(i));
                }else if (mData.get(i).getCompleted()== ReadingActivity.STATE_UNDER_WAY){
                    mDataDoing.add(mData.get(i));

                }else{
                    mDataOutDate.add(mData.get(i));
                }
            }
            if ("0".equals(whichStatus)){

                mAdapter = new ReadingListAdapter(mDataDoing);
            }else if ("1".equals(whichStatus)){

                mAdapter = new ReadingListAdapter(mDataDone);
            }else{
                mAdapter = new ReadingListAdapter(mDataOutDate);
            }
            mAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvReadingList.getParent());
            rvReadingList.setAdapter(mAdapter);
        } else {
//            有数据
            mData.clear();
            mDataDoing.clear();
            mDataDone.clear();
            mDataOutDate.clear();
            mData.addAll(resp.getData());
//            现在只有两种完成，进行中
            for (int i=0;i<mData.size();i++){
                if (mData.get(i).getCompleted()== ReadingActivity.STATE_FINISHED){
                    mDataDone.add(mData.get(i));
                }else if (mData.get(i).getCompleted()== ReadingActivity.STATE_UNDER_WAY){
                    mDataDoing.add(mData.get(i));

                }else{
                    mDataOutDate.add(mData.get(i));
                }
            }


        }
        if ("0".equals(whichStatus)){
            mAdapter.setNewData(mDataDoing);
        }else if ("1".equals(whichStatus)){
            mAdapter.setNewData(mDataDone);
        }else{
            mAdapter.setNewData(mDataOutDate);
        }
//        set数据并且刷新

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
                if ("0".equals(whichStatus)){
                    ReadingListResp.DataBean data = mDataDoing.get(position);
                    Intent intent = new Intent(getHoldingActivity(), ReadingActivity.class);
                    intent.putExtra(ReadingActivity.READING_INFO, data);
                    startActivity(intent);

                }else if ("1".equals(whichStatus)){
                    ReadingListResp.DataBean data = mDataDone.get(position);
                    Intent intent = new Intent(getHoldingActivity(), ReadingActivity.class);
                    intent.putExtra(ReadingActivity.READING_INFO, data);
                    startActivity(intent);

                }else{
                    ReadingListResp.DataBean data = mDataOutDate.get(position);
                    Intent intent = new Intent(getHoldingActivity(), ReadingActivity.class);
                    intent.putExtra(ReadingActivity.READING_INFO, data);
                    startActivity(intent);

                }

            }
        });
        return rootView;
    }

    // 刷新页面
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getReadingList();
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

    //销毁解绑，貌似现在新的butterknife不需要解绑了吧
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //swipe刷新事件
    @Override
    public void onRefresh() {
        mPresenter.getReadingList();
    }

    @OnClick({R.id.rl_doing, R.id.rl_done, R.id.rl_outof_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_doing:
                whichStatus="0";
                if (mAdapter == null) {
                    mPresenter.getReadingList();
                    tvDoing.setTextColor(getResources().getColor(R.color.orange));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvDone.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvOutDate.setTextColor(getResources().getColor(R.color.tab_text));
                    lineOutDate.setBackgroundColor(getResources().getColor(R.color.bg_white));

                }else{

//                mPresenter.getReadingList();
                    mAdapter.setNewData(mDataDoing);
                    tvDoing.setTextColor(getResources().getColor(R.color.orange));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvDone.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvOutDate.setTextColor(getResources().getColor(R.color.tab_text));
                    lineOutDate.setBackgroundColor(getResources().getColor(R.color.bg_white));
                }

                break;
            case R.id.rl_done:
                whichStatus="1";
//                mPresenter.getReadingList();
                if (mAdapter == null) {
                    mPresenter.getReadingList();
                    tvDone.setTextColor(getResources().getColor(R.color.orange));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvDoing.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvOutDate.setTextColor(getResources().getColor(R.color.tab_text));
                    lineOutDate.setBackgroundColor(getResources().getColor(R.color.bg_white));

                }else {
                    mAdapter.setNewData(mDataDone);
                    tvDone.setTextColor(getResources().getColor(R.color.orange));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvDoing.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvOutDate.setTextColor(getResources().getColor(R.color.tab_text));
                    lineOutDate.setBackgroundColor(getResources().getColor(R.color.bg_white));
                }
                break;
            case R.id.rl_outof_date:
                whichStatus="2";
//                mPresenter.getReadingList();
                if (mAdapter == null) {
                    mPresenter.getReadingList();
                    tvOutDate.setTextColor(getResources().getColor(R.color.orange));
                    lineOutDate.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvDone.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvDoing.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.bg_white));

                }else {
                    mAdapter.setNewData(mDataOutDate);
                    tvOutDate.setTextColor(getResources().getColor(R.color.orange));
                    lineOutDate.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvDone.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.bg_white));
                    tvDoing.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.bg_white));
                }
                break;
        }
    }
}
