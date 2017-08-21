package com.yunding.dut.ui.ppt;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.adapter.CourseListAdapter;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.appupdate.versionUpdateResp;
import com.yunding.dut.model.resp.ppt.CourseListResp;
import com.yunding.dut.model.resp.ppt.pptSelfListResp;
import com.yunding.dut.presenter.ppt.CourseListPresenter;
import com.yunding.dut.ui.base.ToolBarFragment;
import com.yunding.dut.ui.me.MeActivity;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.FastBlurUtil;
import com.yunding.dut.view.DUTSwipeRefreshLayout;
import com.yunding.dut.view.DUTVerticalRecyclerView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
public class CourseListFragment extends ToolBarFragment implements SwipeRefreshLayout.OnRefreshListener,
        ICourseListView {

    @BindView(R.id.rv_course_list)
    DUTVerticalRecyclerView rvCourseList;
    @BindView(R.id.srl_course_list)
    DUTSwipeRefreshLayout srlCourseList;
    Unbinder unbinder;
    @BindView(R.id.tv_on_course)
    TextView tvOnCourse;
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
    private double jingd, weid;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    public AMapLocationClientOption mLocationOption = null;
    private CourseListPresenter mPresenter;
    private CourseListAdapter mAdapter;
    private List<CourseListResp.DataBean> mDataList = new ArrayList<>();
    private List<CourseListResp.DataBean> mDataListOnCourse = new ArrayList<>();
    private List<CourseListResp.DataBean> mDataListDIY = new ArrayList<>();
    private String whichChoose = "0";// 默认课堂，0  自学1
    private int positionDIY;
    private List<String> pptImageList = new ArrayList<>();
    private String versionName;
    private String versionContent;
    private int textSize;
    private Dialog dialog;
    private View view;

    public CourseListFragment() {
        mPresenter = new CourseListPresenter(this);
        mAdapter = new CourseListAdapter(new ArrayList<CourseListResp.DataBean>());
    }

    @Override
    public void onResume() {
        super.onResume();
        if ("游客".equals(DUTApplication.getUserInfo().getSTUDENT_TYPE())){
            mPresenter.loadCourseList(DUTApplication.getUserInfo().getUSER_TEACHING());
        }else{
            mPresenter.loadCourseList("");
        }

// TODO: 2017/8/7 三期
        mPresenter.versionUpdate();

        SimpleDraweeView ss = getmToolbarIvMe();
        ss.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(DUTApplication.getUserInfo().getUSER_HEAD_BG())) {
            if (TextUtils.isEmpty(DUTApplication.getUserInfo().getUserAvatar())) {
                ss.setImageResource(R.drawable.ic_userhead);
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String blurBitmap2 = FastBlurUtil.GetUrlBitmap
                                (Apis.SERVER_URL + DUTApplication.getUserInfo().getUserAvatar(), 10, getHoldingActivity());

                    }
                }).start();
                ss.setImageURI(Uri.parse(Apis.SERVER_URL + DUTApplication.getUserInfo().getUserAvatar()));
            }

        } else {
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        rvCourseList.setAdapter(mAdapter);
//            课上
        rvCourseList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (whichChoose.equals("0")) {
                    Intent intent = new Intent(getHoldingActivity(), PPTListActivity.class);
                    intent.putExtra("teachingId", mDataListOnCourse.get(position).getTeachingId() + "");
                    startActivity(intent);
                    //进二级页面
                } else {
                    mPresenter.loadPPTList(mDataListDIY.get(position).getTeachingId()
                            , String.valueOf(jingd), String.valueOf(weid), mDataListDIY.get(position).getClassId(), mDataListDIY.get(position).getStudyMode());
                    positionDIY = position;
                }

            }
        });

        srlCourseList.setOnRefreshListener(this);
        mLocationClient = new AMapLocationClient(getHoldingActivity());
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                        weid = aMapLocation.getLatitude();//获取纬度
                        jingd = aMapLocation.getLongitude();//获取经度
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
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        if ("游客".equals(DUTApplication.getUserInfo().getSTUDENT_TYPE())){
            mPresenter.loadCourseList(DUTApplication.getUserInfo().getUSER_TEACHING());
        }else{
            mPresenter.loadCourseList("");
        }
    }

    @Override
    public void showProgress() {
        if (srlCourseList != null) {
            srlCourseList.setRefreshing(true);
        }

    }

    @Override
    public void hideProgress() {
        if (srlCourseList != null) {
            srlCourseList.setRefreshing(false);
        }
    }

    @Override
    public void showCourseList(CourseListResp resp) {
        mDataList.clear();
        mDataListDIY.clear();
        mDataListOnCourse.clear();
        mDataList = resp.getData();
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).getStudyMode() == 0) {
                mDataListOnCourse.add(mDataList.get(i));

            } else {
                mDataListDIY.add(mDataList.get(i));

            }
        }
        mAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvCourseList.getParent());
        if (whichChoose.equals("0")) {
            mAdapter.setNewData(mDataListOnCourse);
        } else {
            mAdapter.setNewData(mDataListDIY);
        }
    }

    @Override
    public void showNoData() {
        mAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvCourseList.getParent());
    }


    @Override
    public void showListFailed() {
        mAdapter.setEmptyView(R.layout.layout_bad_network, (ViewGroup) rvCourseList.getParent());
    }

    @Override
    public void showMsg() {
        showToast("该课程下没有PPT");

    }

    @Override
    public void showPPTList(pptSelfListResp resp) {
        pptImageList.clear();
        for (int i = 0; i < resp.getData().getSlides().size(); i++) {
            pptImageList.add(resp.getData().getSlides().get(i).getSlideImage());
        }
        for (int i = 0; i < resp.getData().getSlides().size(); i++) {
            resp.getData().setPptImageList(pptImageList);
        }
        Intent intent = new Intent(getHoldingActivity(), PPTSelfActivity.class);
        intent.putExtra("PPT_INFO", (Serializable) resp.getData());//所有的PPT信息
        intent.putExtra("STUDY_MODE", String.valueOf(mDataListDIY.get(positionDIY).getStudyMode()));
        intent.putExtra("pptImage", (Serializable) pptImageList);
        startActivity(intent);


    }

    @Override
    public void showDiyProgress() {
        showProgressDialog();

    }

    @Override
    public void hideDiyProgress() {
        hideProgressDialog();
    }

    @Override
    public void deleteSuccess() {

    }
// TODO: 2017/8/2 三期

    /**
     * 更新轮询
     *
     * @param resp
     */

    @Override
    public void versionUpdate(versionUpdateResp.DataBean resp) {
        versionContent = resp.getContent();
        versionName = resp.getVersion();
        textSize = resp.getTextSize();
        if (resp.getUpdatable() == 0) {
//            没有更新继续轮训
            Observable.timer(10, TimeUnit.SECONDS)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(@NonNull Long aLong) throws Exception {
                            mPresenter.versionUpdate();

                        }
                    });


        } else {
            if (resp.getUpdatemode() == 0) {
                //非强制
                if (DUTApplication.getInstance().getIsShowUpdateDialog() == 0) {
                    DUTApplication.getInstance().setIsShowUpdateDialog(-1);
                    //提示
                    showUpdateDialog(resp.getUpdatemode());
                } else {
                    //不提示每次进来提示
                }


            } else if (resp.getUpdatemode() == 1) {
                //强制
                showUpdateDialog(resp.getUpdatemode());
            } else {

            }

        }

    }

    /**
     * 更新提示框
     *
     * @param updatemode 强制非强制
     */
    private void showUpdateDialog(int updatemode) {
        dialog = new Dialog(getHoldingActivity(), R.style.ActionSheetDialogStyle);
        view = LayoutInflater.from(getHoldingActivity()).inflate(R.layout.update_dialog, null);
        LinearLayout ll_normal_update = (LinearLayout) view.findViewById(R.id.ll_normal_update);
        RelativeLayout rl_force_update = (RelativeLayout) view.findViewById(R.id.rl_force_update);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_ok_force = (Button) view.findViewById(R.id.btn_ok_force);
        Button btn_version = (Button) view.findViewById(R.id.btn_version);
        TextView tv_content = (TextView) view.findViewById(R.id.text_update_notice);
        if (updatemode == 0) {
            ll_normal_update.setVisibility(View.VISIBLE);
            rl_force_update.setVisibility(View.GONE);
        } else {
            ll_normal_update.setVisibility(View.GONE);
            rl_force_update.setVisibility(View.VISIBLE);
        }
        btn_version.setText(versionName);
        tv_content.setText(versionContent);
        // TODO: 2017/8/7
        switch (textSize) {
            case 0:
                tv_content.setTextSize(15);
                break;
            case 1:
                tv_content.setTextSize(13);
                break;
            case 2:
                tv_content.setTextSize(11);
                break;
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://android.myapp.com/myapp/detail.htm?apkName=com.yunding.dut");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                getHoldingActivity().finish();
            }
        });
        btn_ok_force.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://android.myapp.com/myapp/detail.htm?apkName=com.yunding.dut");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                getHoldingActivity().finish();
            }
        });
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if (updatemode == 0) {
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
        } else {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.show();//显示对话框
    }


    @OnClick({R.id.rl_doing, R.id.rl_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_doing:
                whichChoose = "0";

                if (mAdapter == null) {
                    if ("游客".equals(DUTApplication.getUserInfo().getSTUDENT_TYPE())){
                        mPresenter.loadCourseList(DUTApplication.getUserInfo().getUSER_TEACHING());
                    }else{
                        mPresenter.loadCourseList("");
                    }
                    tvOnCourse.setTextColor(getResources().getColor(R.color.orange));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvDone.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.bg_white));

                } else {
                    mAdapter.setNewData(mDataListOnCourse);
                    tvOnCourse.setTextColor(getResources().getColor(R.color.orange));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvDone.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.bg_white));

                }

                break;
            case R.id.rl_done:
                whichChoose = "1";
                if (mAdapter == null) {
                    if ("游客".equals(DUTApplication.getUserInfo().getSTUDENT_TYPE())){
                        mPresenter.loadCourseList(DUTApplication.getUserInfo().getUSER_TEACHING());
                    }else{
                        mPresenter.loadCourseList("");
                    }
                    tvDone.setTextColor(getResources().getColor(R.color.orange));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvOnCourse.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.bg_white));

                } else {

                    mAdapter.setNewData(mDataListDIY);

                    tvDone.setTextColor(getResources().getColor(R.color.orange));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvOnCourse.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.bg_white));

                }

                break;

        }
    }


}
