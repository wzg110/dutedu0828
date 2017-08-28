package com.yunding.dut.ui.ppt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;
import com.yunding.dut.R;
import com.yunding.dut.adapter.ViewPagerAdapter;
import com.yunding.dut.model.resp.ppt.pptSelfListResp;
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

import static com.yunding.dut.ui.ppt.PPTSelfActivity.PPTINFO;

/**
 * 类 名 称：ImageSelf
 * <P/>描    述： 自学PPT显示大图
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 16:55
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 16:55
 * <P/>修改备注：
 * <P/>版    本：
 */

public class ImageSelf extends BaseFragmentInReading {

    Unbinder unbinder;
    @BindView(R.id.view_pager)
    DUTViewPager viewPager;
    private List<String> imageList;
    private int position1;
    private ViewPagerAdapter adapter;
    private List<View> viewList = new ArrayList<>();
    private String title;
    private pptSelfListResp.DataBean mPPTInfo;
    private int currentPosition;
    private boolean isLeft = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_pager;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        getHoldingActivity().getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position1 == currentPosition) {
                    removeFragment();
                } else {
                    removeFragment();
                    addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.getSlides().get(currentPosition),
                            String.valueOf(mPPTInfo.getSlides().get(currentPosition).getStudyMode())));
                }


            }
        });
        mPPTInfo = (pptSelfListResp.DataBean) getArguments().getSerializable(PPTINFO);
        title = getHoldingActivity().getmToolbarTitle().getText().toString();
        imageList = getArguments().getStringArrayList("pptImage");
        position1 = getArguments().getInt("position");
        currentPosition = position1;
        mPPTInfo.getSlides().get(currentPosition).setPptStartTime
                (TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss"));
        initUI();
    }


    private void initUI() {
        getHoldingActivity().getmToolbarTitle().setText((mPPTInfo.getSlides().get(position1).getPageIndex()) + "/" + imageList.size());
        for (int i = 0; i < imageList.size(); i++) {
            View view;
            LayoutInflater inflater = getHoldingActivity().getLayoutInflater();
            view = inflater.inflate(R.layout.image_pager_adapter, null);
            final PhotoView ivPhotoview = (PhotoView) view.findViewById(R.id.iv_photoview_pager);
            final int finalI = i;
            PhotoViewAttacher attacher = new PhotoViewAttacher(ivPhotoview);
            attacher.canZoom();
            Picasso.with(getHoldingActivity()).load(Apis.SERVER_URL + imageList.get(i)).placeholder(R.drawable.ic_zhanwei).into(ivPhotoview);
            attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (finalI == position1) {
                        removeFragment();
                    } else {
                        removeFragment();
                        addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.getSlides().get(finalI), mPPTInfo.getSlides().get(finalI).getStudyMode() + ""));

                    }
                }
            });

            viewList.add(view);
        }

        if (mPPTInfo.getSlides().get(position1).getSlideQuestions().size() == 0) {
            mPPTInfo.getSlides().get(position1).setQuestionsCompleted(1);
        }

        if (mPPTInfo.getSlides().get(position1).getQuestionsCompleted() == 1) {
            isLeft = true;

        } else {

            isLeft = false;

        }

        adapter = new ViewPagerAdapter(viewList, getHoldingActivity(),1);
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
                getHoldingActivity().getmToolbarTitle().setText(mPPTInfo.getSlides().get(position).getPageIndex() + "/" + imageList.size());
                currentPosition = position;
                if (mPPTInfo.getSlides().get(position).getSlideQuestions().size() == 0) {
                    mPPTInfo.getSlides().get(position).setQuestionsCompleted(1);
                }
                if (mPPTInfo.getSlides().get(position).getQuestionsCompleted() == 1) {
                    isLeft = true;
                } else {
                    isLeft = false;
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
                if (!isLeft && left) {
                    new MaterialDialog.Builder(getHoldingActivity())
                            .title("提示")
                            .content("当前PPT尚未完成无法进入下一页。")
                            .positiveText("确定")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    isLeft = true;
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
}
