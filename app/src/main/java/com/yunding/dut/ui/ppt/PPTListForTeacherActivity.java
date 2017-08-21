package com.yunding.dut.ui.ppt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yunding.dut.R;
import com.yunding.dut.adapter.PPTGridAdapter;
import com.yunding.dut.adapter.PPTListAdapter;
import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.presenter.ppt.PPTListPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.view.DUTGridRecycleView;
import com.yunding.dut.view.DUTVerticalRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 类 名 称：PPTListForTeacherActivity
 * <P/>描    述：课堂老师预览列表
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 17:00
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 17:00
 * <P/>修改备注：
 * <P/>版    本：
 */

public class PPTListForTeacherActivity extends ToolBarActivity implements IPPTListView {
    @BindView(R.id.rv_ppt_list)
    DUTVerticalRecyclerView rvPptList;
    @BindView(R.id.rv_ppt_grid)
    DUTGridRecycleView rvPptGrid;
    private PPTListPresenter mPresenter;
    private PPTListAdapter mAdapter;
    public List<String> pptImageList = new ArrayList<>();
    private String mTeachingId;
    private List<PPTResp.DataBean> dataList = new ArrayList<>();
    private boolean isRefreshing = true;
    private ImageView ivTips;
    private int count = 0;
    private PPTGridAdapter mGridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pptlist);
        ButterKnife.bind(this);
        mTeachingId = getIntent().getStringExtra("teachingId");
        setTitleInCenter("预览");
        ivTips = getIvTips();
        ivTips.setVisibility(View.VISIBLE);

        ivTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if ((count % 2) == 1) {
                    ivTips.setImageResource(R.drawable.ic_blue_list);
                    rvPptList.setVisibility(View.GONE);
                    rvPptGrid.setVisibility(View.VISIBLE);
                } else {

                    ivTips.setImageResource(R.drawable.ic_ppt_grid);
                    rvPptList.setVisibility(View.VISIBLE);
                    rvPptGrid.setVisibility(View.GONE);
                }


            }
        });
        mPresenter = new PPTListPresenter(this);
        rvPptGrid.setSpanCount(this,2);
        mAdapter = new PPTListAdapter(new ArrayList<PPTResp.DataBean>());
        mGridAdapter=new PPTGridAdapter(new ArrayList<PPTResp.DataBean>());
        rvPptList.setAdapter(mAdapter);
        rvPptGrid.setAdapter(mGridAdapter);
        rvPptList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PPTListForTeacherActivity.this, PPTTeacherActivity.class);
                intent.putExtra("pptImage", (Serializable) pptImageList);
                intent.putExtra("PPT_INFO", (Serializable) dataList);//所有的PPT信息
                intent.putExtra("PPTInfo_Item", dataList.get(position));//点击的ppt信息
                intent.putExtra("position", position);
                startActivity(intent);

            }
        });
        rvPptGrid.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent(PPTListForTeacherActivity.this, PPTTeacherActivity.class);
                intent.putExtra("pptImage", (Serializable) pptImageList);
                intent.putExtra("PPT_INFO", (Serializable) dataList);//所有的PPT信息
                intent.putExtra("PPTInfo_Item", dataList.get(position));//点击的ppt信息
                intent.putExtra("position", position);
                startActivity(intent);


            }
        });

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showNoData() {
        mPresenter.loadPPTList(mTeachingId);
        mAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvPptList.getParent());
        mGridAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvPptList.getParent());
    }

    @Override
    public void showBadNetwork() {
        mPresenter.loadPPTList(mTeachingId);
        mAdapter.setEmptyView(R.layout.layout_bad_network, (ViewGroup) rvPptGrid.getParent());
        mGridAdapter.setEmptyView(R.layout.layout_bad_network, (ViewGroup) rvPptGrid.getParent());
    }

    @Override
    public void showListFailed() {
        showToast("暂无数据");
    }

    @Override
    public void showPPTList(PPTResp resp) {
        dataList.clear();
        dataList = resp.getData();
        pptImageList.clear();


        for (int i = 0; i < dataList.size(); i++) {
            pptImageList.add(dataList.get(i).getSlideImage());
        }
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setPptImageList(pptImageList);
        }

        mGridAdapter.setNewData(dataList);
        mAdapter.setNewData(dataList);
        Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        if (isRefreshing)
                            mPresenter.loadPPTList(mTeachingId);
                    }
                });

    }

    @Override
    protected void onDestroy() {

        isRefreshing = false;
        count = 0;
        ivTips.setVisibility(View.GONE);
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        isRefreshing=true;
        if (mTeachingId != null) {
            mPresenter.loadPPTList(mTeachingId);
        } else {
            showToast("请重新打开页面");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        count = 0;
        isRefreshing = false;
    }
}
