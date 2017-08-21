package com.yunding.dut.ui.ppt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yunding.dut.R;
import com.yunding.dut.ui.base.BaseFragmentInReading;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 类 名 称：ImageShowFragment
 * <P/>描    述：预览显示大图
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 16:56
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 16:56
 * <P/>修改备注：
 * <P/>版    本：
 */

public class ImageShowFragment extends BaseFragmentInReading {
    @BindView(R.id.iv_photoview)
    PhotoView ivPhotoview;
    Unbinder unbinder;
    private String imagePath;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_show_image;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        getHoldingActivity().getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment();
            }
        });

        imagePath = getArguments().getString("imagePath");
        PhotoViewAttacher attacher = new PhotoViewAttacher(ivPhotoview);
        attacher.canZoom();
        Picasso.with(getHoldingActivity()).load(imagePath).into(ivPhotoview);
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
        super.onDestroyView();
        unbinder.unbind();
    }
}
