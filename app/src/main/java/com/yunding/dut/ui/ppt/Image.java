package com.yunding.dut.ui.ppt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;
import com.yunding.dut.R;
import com.yunding.dut.adapter.ViewPagerAdapter;
import com.yunding.dut.model.resp.ppt.AutoAnswerSingleResp;
import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.presenter.ppt.PPTAnswerPresenter;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.TimeUtils;
import com.yunding.dut.view.DUTViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.yunding.dut.ui.ppt.PPTActivity.PPTINFO;

/**
 * 类 名 称：Image
 * <P/>描    述：显示ppt大图
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 16:53
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 16:53
 * <P/>修改备注：
 * <P/>版    本：
 */
public class Image extends BaseFragmentInReading implements IPPTContentView{

    Unbinder unbinder;
    @BindView(R.id.view_pager)
    DUTViewPager viewPager;
    private List<String> imageList;
    private int position1;
    private ViewPagerAdapter adapter;
    private List<View> viewList = new ArrayList<>();
    private String title;
    private List<PPTResp.DataBean> mPPTInfo;
    private PPTAnswerPresenter mPresenter;
    private boolean isLeft=true;
    private int currentPosition;
    private  float positionX=-1;

    public Image(){

    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_pager;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {

        getHoldingActivity().getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position1==currentPosition){
                    removeFragment();
                }else{
                    removeFragment();
                    addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.get(currentPosition), currentPosition));
                }
            }
        });
        mPPTInfo = (List<PPTResp.DataBean>) getArguments().getSerializable(PPTINFO);
        title = getHoldingActivity().getmToolbarTitle().getText().toString();
        imageList = getArguments().getStringArrayList("pptImage");
        position1 = getArguments().getInt("position");
        currentPosition=position1;
        mPPTInfo.get(currentPosition).setPptStartTime
                (TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss"));
        mPresenter=new PPTAnswerPresenter(this);
        initUI();

    }
    private void initUI() {
        getHoldingActivity().getmToolbarTitle().setText(mPPTInfo.get(position1).getPageIndex() + "/" + imageList.size());
        for (int i = 0; i < imageList.size(); i++) {
            View view;
            LayoutInflater inflater = getHoldingActivity().getLayoutInflater();
            view = inflater.inflate(R.layout.image_pager_adapter, null);
             PhotoView ivPhotoview = (PhotoView) view.findViewById(R.id.iv_photoview_pager);
            final int finalI = i;
            Picasso.with(getHoldingActivity()).load(Apis.SERVER_URL + imageList.get(i))
                    .placeholder(R.drawable.ic_zhanwei).into(ivPhotoview);

            ivPhotoview.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
//                    Log.e("点击事件",finalI+"");
                    try {
                        if (finalI == position1) {
                            removeFragment();
                        } else {
                            removeFragment();
                            addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.get(finalI), finalI));
                        }
                    }catch (Exception e){
                        Log.e("exception" ,e.getMessage());
                    }
                }
            });

            viewList.add(view);
        }

        if (mPPTInfo.get(position1).getQuestionsCompleted()==1){
            isLeft=true;
        }else{
            isLeft=false;
        }
        adapter = new ViewPagerAdapter(viewList, getHoldingActivity());
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(position1);
        viewPager.setScroll(isLeft);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                getHoldingActivity().getmToolbarTitle().setText(mPPTInfo.get(position).getPageIndex()
                        + "/" + imageList.size());
                currentPosition=position;

                if (mPPTInfo.get(position).getQuestionsCompleted()==1){
                    isLeft=true;
                }else{
                    isLeft=false;
                }
                viewPager.setScroll(isLeft);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setsCallback(new DUTViewPager.SlideCallback() {
            @Override
            public void changeView(boolean left) {

                if (!isLeft&&left){

                    new MaterialDialog.Builder(getHoldingActivity())
                            .title("提示")
                            .content("当前PPT尚未完成是否确认退出？退出则视为提交空答案。")
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    mPresenter.savePPTAnswerAuto(mPPTInfo.get(currentPosition).getSlideId(), mPPTInfo.get(currentPosition).getTeachingId());
                                }
                            })
                            .show();
                }
            }

        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        getHoldingActivity().getmToolbarTitle().setText(title);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public void commitSuccess() {

    }

    @Override
    public void getAnswerShow(String state) {

    }

    @Override
    public void saveAutoResp(PPTResp bean) {
        isLeft=true;
        viewPager.setScroll(isLeft);
        mPPTInfo.get(currentPosition).setQuestionsCompleted(1);
        mPPTInfo.get(currentPosition).setSlideQuestions(bean.getData().get(0).getSlideQuestions());
        String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
        mPresenter.savePPTBrowerStayTime(mPPTInfo.get(currentPosition).getTeachingSlideId(), "", mPPTInfo.get(currentPosition).getSlideId(), mPPTInfo.get(currentPosition).getClassId()
                , mPPTInfo.get(currentPosition).getTeachingId(), mPPTInfo.get(currentPosition).getPptStartTime(), endTime);

        addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.get(currentPosition), currentPosition));
    }

    @Override
    public void getNewPPTimage(String url) {

    }

    @Override
    public void getPollingPPTQuestion(List<? extends PPTResp.DataBean.slideQuestionsBean> dataList) {

    }

    @Override
    public void getAutoAnswerSingle(AutoAnswerSingleResp.DataBean dataBean) {

    }

    @Override
    public void getAnalysisFlag(String flag) {

    }

}
