package com.yunding.dut.ui.ppt;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.squareup.picasso.Picasso;
import com.yunding.dut.R;
import com.yunding.dut.adapter.PPTGridAdapter;
import com.yunding.dut.adapter.PPTListSlideAdapter;
import com.yunding.dut.adapter.ViewPagerAdapter;
import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.presenter.ppt.PPTListPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.SizeUtils;
import com.yunding.dut.view.DUTImageAutoLoadScrollGridRecyclerView;
import com.yunding.dut.view.DUTImageAutoLoadScrollRecyclerView;
import com.yunding.dut.view.DUTVerticalViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PPTListNewActivity extends ToolBarActivity implements IPPTListView {
    @BindView(R.id.layout)
    FrameLayout layout;
    @BindView(R.id.fl_list)
    FrameLayout flList;
    @BindView(R.id.iv_list_choice)
    ImageView ivListChoice;
    @BindView(R.id.iv_grid_choice)
    ImageView ivGridChoice;
    @BindView(R.id.ll_ppt_tips)
    LinearLayout llPptTips;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;

    @BindView(R.id.vvp_pptlist)
    DUTVerticalViewPager vvpPptlist;
    @BindView(R.id.lv_cover)
    DUTImageAutoLoadScrollRecyclerView lvCover;
    @BindView(R.id.rl_pptlist_slide)
    LinearLayout rlPptlistSlide;
    @BindView(R.id.rv_ppt_grid)
    DUTImageAutoLoadScrollGridRecyclerView rvPptGrid;
    @BindView(R.id.ll_no_data)
    RelativeLayout llNoData;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_page)
    TextView tvPage;
    @BindView(R.id.iv_has_question_tips)
    Button ivHasQuestionTips;
    //    @BindView(R.id.iv_has_question_tips)
//    ImageView ivHasQuestionTips11;

    @BindView(R.id.btn_grid)
    Button btnGrid;
    @BindView(R.id.ll_choose_btn)
    LinearLayout llChooseBtn;
    @BindView(R.id.btn_list)
    Button btnList;
    @BindView(R.id.ll_ppt_grid)
    LinearLayout llPptGrid;
    private boolean ListOrGrid = false; //false 列表  true 网格
    private boolean btnOpenOrExit = false; //true 打开菜单 false关闭菜单
    private ViewPagerAdapter adapter;
    private PPTListPresenter mPresenter;
    //    private PPTListAdapter mAdapter;
    private PPTListSlideAdapter mAdapter;
//    private MyRecyclerAdapter mAdapter;
    private PPTGridAdapter mGridAdapter;
    public List<String> pptImageList = new ArrayList<>();
    private String mTeachingId;
    private List<PPTResp.DataBean> dataList = new ArrayList<>();
    private double jingd, weid;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    public AMapLocationClientOption mLocationOption = null;
    private List<View> viewList = new ArrayList<>();

    private boolean isRefreshing = true;
    private ImageView ivTips;
    private int count = 0;
    //    @BindView(R.id.btn_menu)
//    FloatingActionButton btnMenu;
//    @BindView(R.id.btn_lsit)
//    FloatingActionButton btnLsit;
//    @BindView(R.id.btn_grid)
//    FloatingActionButton btnGrid;
    private Map<Integer, PhotoView> imageMap = new HashMap<>();
    private Map<Integer, View> imageTipsMap = new HashMap<>();

    private int currentPosition = -1;
    private String courseName;
    private boolean isShowSlide = false;// true 显示抽屉 false不显示
private  TranslateAnimation mShowAction,mHiddenAction;
    private TextView tvFeedBack;
    private  Dialog dialog;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pptlist_new);
        ButterKnife.bind(this);
        initUi();
         mShowAction = new TranslateAnimation(
                 Animation.RELATIVE_TO_SELF,
                 -1.0f,
                 Animation.RELATIVE_TO_SELF,
                 0.0f,
                 Animation.RELATIVE_TO_SELF,
                0.0f,
                 Animation.RELATIVE_TO_SELF,
                 0.0f);
        mShowAction.setDuration(400);
        mHiddenAction = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                -1.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f);
        mHiddenAction.setDuration(400);

    }

    private void initUi() {
        tvFeedBack=getFeedBack();
        tvFeedBack.setVisibility(View.VISIBLE);
        tvFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
showFeedBackDialog();
            }
        });
//        initFloatingActionsMenu(layout);
        mTeachingId = getIntent().getStringExtra("teachingId");
        courseName = getIntent().getStringExtra("courseName");
        setTitleInCenter(courseName);
        mPresenter = new PPTListPresenter(this);
        rvPptGrid.setSpanCount(this, 2);
        mAdapter = new PPTListSlideAdapter(new ArrayList<PPTResp.DataBean>());
//        mAdapter = new MyRecyclerAdapter(getApplicationContext(),new ArrayList<PPTResp.DataBean>());
        mGridAdapter = new PPTGridAdapter(new ArrayList<PPTResp.DataBean>());
        lvCover.setAdapter(mAdapter);
        rvPptGrid.setAdapter(mGridAdapter);
        rvPptGrid.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(PPTListNewActivity.this, PPTActivity.class);
//                intent.putExtra("pptImage", (Serializable) pptImageList);
//                intent.putExtra("PPT_INFO", (Serializable) dataList);//所有的PPT信息
//                intent.putExtra("PPTInfo_Item", dataList.get(position));//点击的ppt信息
//                intent.putExtra("position", position);
//                startActivity(intent);
                mAdapter.setShowTips(position);
                vvpPptlist.setCurrentItem(position,false);
                flList.setVisibility(View.VISIBLE);

                rvPptGrid.setVisibility(View.GONE);
                llPptGrid.setVisibility(View.GONE);
            }
        });
        lvCover.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

//                mPresenter.signIn(String.valueOf(mTeachingId), dataList.get(position).getSpecialityId()
//                        , dataList.get(position).getClassId(), String.valueOf(weid), String.valueOf(jingd));
//                Intent intent = new Intent(PPTListActivity.this, PPTActivity.class);
//                intent.putExtra("pptImage", (Serializable) pptImageList);
//                intent.putExtra("PPT_INFO", (Serializable) dataList);//所有的PPT信息
//                intent.putExtra("PPTInfo_Item", dataList.get(position));//点击的ppt信息
//                intent.putExtra("position", position);
//                startActivity(intent);
//                Intent intent = new Intent(PPTListNewActivity.this, PPTActivity.class);
//                startActivity(intent);
                mAdapter.setShowTips(position);
                vvpPptlist.setCurrentItem(position, false);



            }
        });
        vvpPptlist.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                currentPosition = position;
                mAdapter.setShowTips(currentPosition);
//                Log.e("viewpager滑动", position + "");
                lvCover.smoothScrollToPosition(position);
                imageMap.get(position).setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
//                        Intent intent = new Intent(PPTListNewActivity.this, PPTActivity.class);
//                        intent.putExtra("pptImage", (Serializable) pptImageList);
//                        intent.putExtra("PPT_INFO", (Serializable) dataList);//所有的PPT信息
//                        intent.putExtra("PPTInfo_Item", dataList.get(position));//点击的ppt信息
//                        intent.putExtra("position", position);
//                        startActivity(intent);
                        if (!isShowSlide) {
                            isShowSlide = true;
                            rlPptlistSlide.startAnimation(mShowAction);
                            rlPptlistSlide.setVisibility(View.VISIBLE);
                        } else {
                            isShowSlide = false;
                            rlPptlistSlide.startAnimation(mHiddenAction);
                            rlPptlistSlide.setVisibility(View.GONE);
                        }
                    }
                });
                imageTipsMap.get(position).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isShowSlide) {
                            isShowSlide = true;
                            rlPptlistSlide.startAnimation(mShowAction);
                            rlPptlistSlide.setVisibility(View.VISIBLE);
                        } else {
                            isShowSlide = false;
                            rlPptlistSlide.startAnimation(mHiddenAction);
                            rlPptlistSlide.setVisibility(View.GONE);
                        }
                    }
                });
                icon.setVisibility(View.VISIBLE);
                tvPage.setText(dataList.get(position).getPageIndex() + "/" + dataList.size());
                tvTime.setText(dataList.get(position).getPlatformTime());
                if (dataList.get(position).getSlideQuestions() == null || dataList.get(position).getSlideQuestions().size() == 0) {
                    ivHasQuestionTips.setVisibility(View.INVISIBLE);
                } else {
                    ivHasQuestionTips.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mLocationClient = new AMapLocationClient(this);
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。

                        weid = aMapLocation.getLatitude();//获取纬度
                        jingd = aMapLocation.getLongitude();//获取经度
//                        showToast("经度"+jingd+"---维度"+weid);
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }

            }
        };
//设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(100000);
        mLocationOption.setWifiActiveScan(false);
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showNoData() {
        if (isRefreshing) {
            mPresenter.loadPPTList(mTeachingId);
        }

        llNoData.setVisibility(View.VISIBLE);
        vvpPptlist.setVisibility(View.GONE);
//        mAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) lvCover.getParent());
        mGridAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvPptGrid.getParent());

    }

    @Override
    public void showBadNetwork() {
        if (isRefreshing) {
            mPresenter.loadPPTList(mTeachingId);
        }
        llNoData.setVisibility(View.VISIBLE);
        vvpPptlist.setVisibility(View.GONE);
//        mAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) lvCover.getParent());
        mGridAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvPptGrid.getParent());

    }

    @Override
    public void showListFailed() {
        showToast("暂无数据");
        if (isRefreshing) {
            mPresenter.loadPPTList(mTeachingId);
        }
//        llNoData.setVisibility(View.VISIBLE);
//        vvpPptlist.setVisibility(View.GONE);

    }

    @Override
    public void showPPTList(PPTResp resp) {
        if (dataList.size() == resp.getData().size()) {

            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).getSlideImage().equals(resp.getData().get(i).getSlideImage())) {

                } else {
                    Log.e("IRL",Apis.SERVER_URL + resp.getData().get(i).getSlideImage());
                    Picasso.with(this).load(Apis.SERVER_URL + resp.getData().get(i).getSlideImage())
                            .resize(SizeUtils.dp2px(220),SizeUtils.dp2px(165)).placeholder(R.drawable.ic_zhanwei).into(imageMap.get(i));
                    mAdapter.getData().set(i,resp.getData().get(i));
                    mAdapter.notifyItemChanged(i);
                }
            }
            dataList = resp.getData();
            if (dataList.get(currentPosition).getSlideQuestions() == null || dataList.get(currentPosition).getSlideQuestions().size() == 0) {
                ivHasQuestionTips.setVisibility(View.GONE);
            } else {
                ivHasQuestionTips.setVisibility(View.VISIBLE);
            }

        } else {
            try {
            dataList.clear();
            dataList = resp.getData();
            pptImageList.clear();
            llNoData.setVisibility(View.GONE);
            vvpPptlist.setVisibility(View.VISIBLE);

            for (int i = 0; i < dataList.size(); i++) {
                pptImageList.add(dataList.get(i).getSlideImage());
            }
            initPager();
            for (int i = 0; i < dataList.size(); i++) {
                dataList.get(i).setPptImageList(pptImageList);
            }
            icon.setVisibility(View.VISIBLE);
            tvPage.setText(dataList.get(currentPosition).getPageIndex() + "/" + dataList.size());
            tvTime.setText(dataList.get(currentPosition).getPlatformTime());
            if (dataList.get(currentPosition).getSlideQuestions() == null || dataList.get(currentPosition).getSlideQuestions().size() == 0) {
                ivHasQuestionTips.setVisibility(View.GONE);
            } else {
                ivHasQuestionTips.setVisibility(View.VISIBLE);
            }

    mGridAdapter.setNewData(dataList);
    mAdapter.setNewData(dataList);
    mGridAdapter.setShowTips(0);
    mAdapter.setShowTips(0);
    lvCover.smoothScrollToPosition(0);
}catch ( Exception e){
    e.getStackTrace();
}
        }

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
    public void showMsg(String msg) {
        showToast(msg);
    }

    private void initPager() {
        viewList.clear();
        for (int i = 0; i < pptImageList.size(); i++) {
            View view;
            LayoutInflater inflater = this.getLayoutInflater();
            view = inflater.inflate(R.layout.image_pager_adapter, null);
            PhotoView ivPhotoview = (PhotoView) view.findViewById(R.id.iv_photoview_pager);
            final int finalI = i;
            Picasso.with(this).load(Apis.SERVER_URL + pptImageList.get(i)).resize(SizeUtils.dp2px(220),SizeUtils.dp2px(165))
                    .placeholder(R.drawable.ic_zhanwei).into(ivPhotoview);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isShowSlide) {
                        isShowSlide = true;
                        rlPptlistSlide.startAnimation(mShowAction);
                        rlPptlistSlide.setVisibility(View.VISIBLE);
                    } else {
                        isShowSlide = false;
                        rlPptlistSlide.startAnimation(mHiddenAction);
                        rlPptlistSlide.setVisibility(View.GONE);
                    }
                }
            });

            ivPhotoview.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (!isShowSlide) {
                        isShowSlide = true;
                        rlPptlistSlide.startAnimation(mShowAction);
                        rlPptlistSlide.setVisibility(View.VISIBLE);
                    } else {
                        isShowSlide = false;
                        rlPptlistSlide.startAnimation(mHiddenAction);
                        rlPptlistSlide.setVisibility(View.GONE);
                    }



                }
            });
            imageMap.put(i, ivPhotoview);
            imageTipsMap.put(i,view);
            viewList.add(view);
        }
        adapter = new ViewPagerAdapter(viewList, this, 0);
        vvpPptlist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        currentPosition = 0;
        vvpPptlist.setCurrentItem(0);
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
        isRefreshing = false;
        count = 0;
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        tvFeedBack.setVisibility(View.GONE);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        isRefreshing = true;
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

    @OnClick({R.id.iv_list_choice, R.id.iv_grid_choice, R.id.iv_menu, R.id.btn_list, R.id.btn_grid, R.id.iv_has_question_tips})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_list_choice:
                ivListChoice.setImageResource(R.drawable.ic_ppt_list_selceted);
                ivGridChoice.setImageResource(R.drawable.ic_ppt_gird_normal);
                flList.setVisibility(View.VISIBLE);
                rvPptGrid.setVisibility(View.GONE);
                llPptGrid.setVisibility(View.GONE);


                break;
            case R.id.iv_grid_choice:

                ivListChoice.setImageResource(R.drawable.ic_ppt_list_normal);
                ivGridChoice.setImageResource(R.drawable.ic_ppt_gird_selected);
                flList.setVisibility(View.GONE);
                rvPptGrid.setVisibility(View.VISIBLE);
                llPptGrid.setVisibility(View.VISIBLE);

                break;
            case R.id.iv_menu:

                if (!btnOpenOrExit) {
                    btnOpenOrExit = true;
                    ivMenu.setImageResource(R.drawable.ic_ppt_list_guide_exit);
                    llPptTips.setVisibility(View.VISIBLE);
                    if (!dataList.isEmpty()) {
                        rlPptlistSlide.setVisibility(View.VISIBLE);
                    }

                } else {
                    btnOpenOrExit = false;
                    ivMenu.setImageResource(R.drawable.ic_ppt_list_guide);
                    llPptTips.setVisibility(View.GONE);
                    rlPptlistSlide.setVisibility(View.GONE);

                }


                break;
            case R.id.btn_list:

                rlPptlistSlide.setVisibility(View.GONE);
                isShowSlide=false;
                flList.setVisibility(View.GONE);
                rvPptGrid.setVisibility(View.VISIBLE);
                llPptGrid.setVisibility(View.VISIBLE);
                mGridAdapter.setShowTips(currentPosition);
                mGridAdapter.setNewData(dataList);
                break;
            case R.id.btn_grid:

                rvPptGrid.setVisibility(View.GONE);
                llPptGrid.setVisibility(View.GONE);
                flList.setVisibility(View.VISIBLE);


                break;
            case R.id.iv_has_question_tips:
                mPresenter.signIn(String.valueOf(mTeachingId), dataList.get(currentPosition).getSpecialityId()
                        , dataList.get(currentPosition).getClassId(), String.valueOf(weid), String.valueOf(jingd));
                Intent intent = new Intent(PPTListNewActivity.this, PPTActivity.class);
                intent.putExtra("pptImage", (Serializable) pptImageList);
                intent.putExtra("PPT_INFO", (Serializable) dataList);//所有的PPT信息
                intent.putExtra("PPTInfo_Item", dataList.get(currentPosition));//点击的ppt信息
                intent.putExtra("position", currentPosition);
                startActivity(intent);
                break;
        }
    }

    private void showFeedBackDialog() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        view = LayoutInflater.from(this).inflate(R.layout.layout_add_notes_activity, null);
        //初始化控件
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_send = (Button) view.findViewById(R.id.btn_send);
        final EditText et_notes = (EditText) view.findViewById(R.id.et_notes);
        TextView tv_note = (TextView) view.findViewById(R.id.tv_notes_sentence);
        tv_note.setVisibility(View.GONE);
        btn_send.setText("提交");
        et_notes.setHint("您对于该页PPT的问题反馈");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_notes.getText().toString().trim())) {
                    showToast("请输入您的问题描述");

                } else {
                    mPresenter.sendFeedBack(
                            dataList.get(currentPosition).getTeachingId(),
                            dataList.get(currentPosition).getSlideId(),
                            "",
                            dataList.get(currentPosition).getClassId(),
                            "",
                            et_notes.getText().toString().trim());
                    dialog.dismiss();
                }
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 1); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

//
//        lp.y = 200;//设置Dialog距离底部的距离
//       将属性设置给窗体
//        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }
}
