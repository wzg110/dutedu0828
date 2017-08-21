package com.yunding.dut.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yunding.dut.R;
import com.yunding.dut.adapter.HomePagerAdapter;
import com.yunding.dut.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.vp_home)
    ViewPager vpHome;
//    @BindView(R.id.bnv_home)
//    BottomNavigationView bnvHome;
    @BindView(R.id.home_radio_course)
    RadioButton homeRadioCurse;
    @BindView(R.id.home_radio_reading)
    RadioButton homeRadioReading;
    @BindView(R.id.home_radio_discuss)
    RadioButton homeRadioDiscuss;
    @BindView(R.id.bnv_home)
    RadioGroup bnvHome;

    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;


    private static final int PERMISSON_REQUESTCODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initBottomNavigationView();
        HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager());
        vpHome.setAdapter(adapter);
        vpHome.setOffscreenPageLimit(2);
        vpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
//                        bnvHome.setSelectedItemId(R.id.action_course);
                        bnvHome.check(R.id.home_radio_course);
                        break;
                    case 1:
//                        bnvHome.setSelectedItemId(R.id.action_reading);
                        bnvHome.check(R.id.home_radio_reading);
                        break;
                    case 2:
//                        bnvHome.setSelectedItemId(R.id.action_discuss);
                        bnvHome.check(R.id.home_radio_discuss);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bnvHome.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.home_radio_course:
                        vpHome.setCurrentItem(0,false);
                        break;
                    case R.id.home_radio_reading:
                        vpHome.setCurrentItem(1,false);
                        break;
                    case R.id.home_radio_discuss:
                        vpHome.setCurrentItem(2,false);
                        break;

                }

            }
        });


    }

    private void initBottomNavigationView() {
//        int[][] states = new int[][]{
//                new int[]{-android.R.attr.state_checked},
//                new int[]{android.R.attr.state_checked}
//        };
//
//        int[] colors = new int[]{
//                getResources().getColor(R.color.bottom_navigation_normal),
//                getResources().getColor(R.color.bottom_navigation_selected)
//        };
//        ColorStateList csl = new ColorStateList(states, colors);
//        bnvHome.setItemTextColor(csl);
//        bnvHome.setItemIconTintList(csl);
//        bnvHome.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_course:
//                vpHome.setCurrentItem(0, false);
//                return true;
//            case R.id.action_reading:
//                vpHome.setCurrentItem(1, false);
//                return true;
//            case R.id.action_discuss:
//                vpHome.setCurrentItem(2, false);
//                return true;
//        }
        return false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(isNeedCheck){
            checkPermissions(needPermissions);
        }
    }
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
//                showMissingPermissionDialog();
                isNeedCheck = false;
                finish();
            }
        }
    }
//    private void showMissingPermissionDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(R.string.notifyTitle);
//        builder.setMessage(R.string.notifyMsg);
//
//        // 拒绝, 退出应用
//        builder.setNegativeButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                });
//
//        builder.setPositiveButton(R.string.setting,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startAppSettings();
//                    }
//                });
//
//        builder.setCancelable(false);
//
//        builder.show();
//    }
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
