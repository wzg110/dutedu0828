package com.yunding.dut.ui.ppt;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.appupdate.versionUpdateResp;
import com.yunding.dut.model.resp.ppt.CourseListResp;
import com.yunding.dut.model.resp.ppt.pptSelfListResp;
import com.yunding.dut.presenter.ppt.CourseListPresenter;
import com.yunding.dut.ui.base.ToolBarFragment;
import com.yunding.dut.ui.me.MeActivity;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.AppUtils;
import com.yunding.dut.view.DUTSwipeRefreshLayout;

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

import static com.yunding.dut.util.third.SizeUtils.dp2px;

/**
 * 类 名 称：CourseListForTeacherFragment
 * <P/>描    述： 教师预览课堂列表
 * <P/>创 建 人：Administrator
 * <P/>创建时间：2017/8/14 11:34
 * <P/>修 改 人：Administrator
 * <P/>修改时间：2017/8/14 11:34
 * <P/>修改备注：
 * <P/>版    本：
 */

public class CourseListForTeacherFragment extends ToolBarFragment implements SwipeRefreshLayout.OnRefreshListener,
        ICourseListView {
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
    @BindView(R.id.rv_course_list)
    SwipeMenuListView rvCourseList;
    @BindView(R.id.srl_course_list)
    DUTSwipeRefreshLayout srlCourseList;
    Unbinder unbinder;
    @BindView(R.id.lila_no_data)
    LinearLayout lilaNoData;
    private CourseListPresenter mPresenter;
    private CourseListForTeacherAdapter mAdapter;
    private List<CourseListResp.DataBean> mDataList = new ArrayList<>();
    private List<CourseListResp.DataBean> mDataListOnCourse = new ArrayList<>();
    private List<CourseListResp.DataBean> mDataListDIY = new ArrayList<>();
    private String whichChoose = "0";// 默认课堂，0  自学1
    private List<PackageInfo> arr=new ArrayList<>();
    private int marketTips=-1;
    private  Dialog dialog;
    private String versionName;
    private String versionContent;
    private int textSize;
    private View view;
    public CourseListForTeacherFragment() {
        mPresenter = new CourseListPresenter(this);
        mAdapter = new CourseListForTeacherAdapter(new ArrayList<CourseListResp.DataBean>());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getmToolbar().getMenu().clear();
        getmToolbar().inflateMenu(R.menu.menu_reading_list);

    }

    //菜单跳转到我的信息
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
    public void onResume() {
        super.onResume();
        mPresenter.loadCourseTeacherList();
        mPresenter.versionUpdate();
    }

    @Override
    public void onRefresh() {
        mPresenter.loadCourseTeacherList();
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
            if (mDataList.get(i).getStudyMode() == 3) {
                mDataListOnCourse.add(mDataList.get(i));

            } else {
                mDataListDIY.add(mDataList.get(i));

            }
        }
        if (whichChoose.equals("0")) {
            if (mDataListOnCourse.size() == 0) {
                lilaNoData.setVisibility(View.VISIBLE);
            } else {
                lilaNoData.setVisibility(View.GONE);
                mAdapter.setNewData(mDataListOnCourse);
                rvCourseList.setAdapter(mAdapter);
            }


        } else {
            if (mDataListDIY.size() == 0) {
                lilaNoData.setVisibility(View.VISIBLE);
            } else {
                lilaNoData.setVisibility(View.GONE);
                mAdapter.setNewData(mDataListDIY);
                rvCourseList.setAdapter(mAdapter);
            }

        }


    }

    @Override
    public void showNoData() {
        lilaNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void showListFailed() {
        lilaNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMsg() {

    }

    @Override
    public void showPPTList(pptSelfListResp resp) {

    }

    @Override
    public void showDiyProgress() {

    }

    @Override
    public void hideDiyProgress() {

    }

    @Override
    public void deleteSuccess() {
        showToast("删除成功");

    }

    @Override
    public void versionUpdate(versionUpdateResp.DataBean resp) {
        versionContent = resp.getContent();
        versionName = resp.getVersion();
        textSize = resp.getTextSize();
        Log.e("更新",resp.getUpdatable()+"");
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
                if (marketTips==-1){
                    Uri uri = Uri.parse("http://android.myapp.com/myapp/detail.htm?apkName=com.yunding.dut");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getHoldingActivity().finish();
                }else{
                    AppUtils.goToMarket(getHoldingActivity().getApplicationContext(),
                            "com.yunding.dut",marketTips);
                    getHoldingActivity().finish();
                }

            }
        });
        btn_ok_force.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marketTips==-1){
                    Uri uri = Uri.parse("http://android.myapp.com/myapp/detail.htm?apkName=com.yunding.dut");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getHoldingActivity().finish();
                }else{
                    AppUtils.goToMarket(getHoldingActivity().getApplicationContext(),
                            "com.yunding.dut",marketTips);
                    getHoldingActivity().finish();
                }
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


    @Override
    protected int getLayoutId() {
        return R.layout.fragement_course_for_teacher;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {

        setTitleInCenter("预览");

        setShowNavigation(false);

        arr= AppUtils.getAllApps(getHoldingActivity().getApplicationContext());
        for (int i=0;i<arr.size();i++){
            if ("com.tencent.android.qqdownloader".equals(arr.get(i).packageName)){
                marketTips=0;
                return;
            }else if ("com.wandoujia.phoenix2".equals(arr.get(i).packageName)){
                marketTips=1;
                return;
            }else if ("com.baidu.appsearch".equals(arr.get(i).packageName)){
                marketTips=2;
                return;
            }

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getHoldingActivity());
                // set item background
                openItem.setBackground(R.color.orange);
                // set item width
                openItem.setWidth(dp2px(70));
                // set item title
                openItem.setTitle("删除");
                // set item title fontsize
                openItem.setTitleSize(12);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);


            }
        };
        rvCourseList.setMenuCreator(creator);

        rvCourseList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //删除
                        List<CourseListResp.DataBean> data = mAdapter.getData();
                        mPresenter.deleteCourseListItem(String.valueOf(data.get(position).getTeachingId()));
                        data.remove(position);
                        mAdapter.setNewData(data);
                        if (data.size() == 0) {
                            lilaNoData.setVisibility(View.VISIBLE);
                        }
                        break;


                }
                return false;
            }
        });
        srlCourseList.setOnRefreshListener(this);
        rvCourseList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        rvCourseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (whichChoose.equals("0")) {
                    Intent intent = new Intent(getHoldingActivity(), PPTListForTeacherActivity.class);
                    intent.putExtra("teachingId", mDataListOnCourse.get(position).getTeachingId() + "");
                    startActivity(intent);
                    //进二级页面
                } else {
                    mPresenter.loadPPTList(mDataListDIY.get(position).getTeachingId()+""
                            , String.valueOf(0), String.valueOf(0), mDataListDIY.get(position).getClassId(), mDataListDIY.get(position).getStudyMode());
                }
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_doing, R.id.rl_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_doing:
                whichChoose = "0";

                if (mAdapter == null) {
                    mPresenter.loadCourseTeacherList();
                    tvOnCourse.setTextColor(getResources().getColor(R.color.orange));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvDone.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.bg_white));

                } else {

                    if (mDataListOnCourse.size() == 0) {
                        lilaNoData.setVisibility(View.VISIBLE);
                    } else {
                        lilaNoData.setVisibility(View.GONE);
                        mAdapter.setNewData(mDataListOnCourse);
                        rvCourseList.setAdapter(mAdapter);
                    }
                    tvOnCourse.setTextColor(getResources().getColor(R.color.orange));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvDone.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.bg_white));

                }

                break;
            case R.id.rl_done:
                whichChoose = "1";
                if (mAdapter == null) {
                    mPresenter.loadCourseTeacherList();
                    tvDone.setTextColor(getResources().getColor(R.color.orange));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvOnCourse.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.bg_white));

                } else {


                    if (mDataListDIY.size() == 0) {
                        lilaNoData.setVisibility(View.VISIBLE);
                    } else {
                        lilaNoData.setVisibility(View.GONE);
                        mAdapter.setNewData(mDataListDIY);
                        rvCourseList.setAdapter(mAdapter);
                    }

                    tvDone.setTextColor(getResources().getColor(R.color.orange));
                    lineDone.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvOnCourse.setTextColor(getResources().getColor(R.color.tab_text));
                    lineDoing.setBackgroundColor(getResources().getColor(R.color.bg_white));

                }

                break;

        }
    }

    private class CourseListForTeacherAdapter extends BaseAdapter {
        private List<CourseListResp.DataBean> dataListAdapter;

        public CourseListForTeacherAdapter(List<CourseListResp.DataBean> list) {
            this.dataListAdapter = list;

        }

        public void setNewData(List<CourseListResp.DataBean> list) {

            this.dataListAdapter = list;
            notifyDataSetChanged();
        }

        public List<CourseListResp.DataBean> getData() {
            return dataListAdapter;
        }

        @Override
        public int getCount() {
            return dataListAdapter.size();
        }

        @Override
        public Object getItem(int position) {
            return dataListAdapter.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderTeacher holderTeacher;
            if (convertView == null) {
                convertView = View.inflate(getHoldingActivity(), R.layout.teacher_course, null);
                holderTeacher = new HolderTeacher();
                holderTeacher.tv_teacher_name = (TextView) convertView.findViewById(R.id.tv_teacher_name);
                holderTeacher.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                holderTeacher.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holderTeacher.img_ppt_narrow = (SimpleDraweeView) convertView.findViewById(R.id.img_ppt_narrow);
                convertView.setTag(holderTeacher);

            } else {
                holderTeacher = (HolderTeacher) convertView.getTag();
            }
            holderTeacher.tv_content.setText(dataListAdapter.get(position).getFileName());
            holderTeacher.tv_time.setText(dataListAdapter.get(position).getStartDate());
            holderTeacher.tv_teacher_name.setText(dataListAdapter.get(position).getTeacherName());
            holderTeacher.img_ppt_narrow.setImageURI(Uri.parse(Apis.TEST_URL2 + dataListAdapter.get(position).getCover()));
            return convertView;
        }
    }

    class HolderTeacher {

        TextView tv_content;
        TextView tv_time;
        TextView tv_teacher_name;
        SimpleDraweeView img_ppt_narrow;


    }
}
