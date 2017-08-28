package com.yunding.dut.ui.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.yunding.dut.R;
import com.yunding.dut.adapter.MeWordsAdapter;
import com.yunding.dut.model.resp.collect.CollectAllWordsResp;
import com.yunding.dut.presenter.me.MeWordsPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yunding.dut.util.third.SizeUtils.dp2px;

/**
 * Created by Administrator on 2017/5/17.
 */

public class MeWordsActivity extends ToolBarActivity implements IMeWordsView {



    @BindView(R.id.lila_no_data)
    LinearLayout lilaNoData;
    @BindView(R.id.list_me_word)
    SwipeMenuListView listMeWord;
    private MeWordsAdapter mMeWordsAdapter;
    private MeWordsPresenter mMeWordsPresenter;
    private CollectAllWordsResp mCollectAllWordsResp;
    public static final String MEWORD = "meWord";
    private static final String TAG = "MeWordsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_word);
        ButterKnife.bind(this);
        setTitleInCenter(getString(R.string.my_word));
        mMeWordsPresenter = new MeWordsPresenter(this);
        mMeWordsPresenter.getCollectWordsList();
        mMeWordsAdapter = new MeWordsAdapter(this);

//        setItemClick();

        SwipeMenuCreator creator = new SwipeMenuCreator() {


            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(R.color.orange);
                // set item width
                openItem.setWidth(dp2px(70));
                // set item title
                openItem.setTitle("取消收藏");
                // set item title fontsize
                openItem.setTitleSize(12);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);



            }
        };
        listMeWord.setMenuCreator(creator);
        listMeWord.setAdapter(mMeWordsAdapter);
        listMeWord.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        final String collectionId = mCollectAllWordsResp.getData().get(position).getCollectionId();
                        mMeWordsPresenter.delCollectWords(collectionId);
                        mCollectAllWordsResp.getData().remove(position);
                        mMeWordsAdapter.setCollectAllWordsResp(mCollectAllWordsResp);
                        if (mCollectAllWordsResp.getData().size() == 0) {
                            lilaNoData.setVisibility(View.VISIBLE);
                        }
                        break;




                }
                return false;
            }
        });
        listMeWord.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

        // Left
        listMeWord.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

    }

    private void setItemClick() {
        listMeWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toMeWordsTranslate = new Intent(MeWordsActivity.this, MeWordTranslateActivity.class);
                toMeWordsTranslate.putExtra(MEWORD, mCollectAllWordsResp.getData().get(position).getEnglish());
                startActivity(toMeWordsTranslate);
            }
        });
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showWordsList(CollectAllWordsResp collectAllWordsResp) {
        if (collectAllWordsResp.getData().size() == 0) {
            lilaNoData.setVisibility(View.VISIBLE);
        }
        mCollectAllWordsResp = collectAllWordsResp;
        mMeWordsAdapter.setCollectAllWordsResp(collectAllWordsResp);
    }

    @Override
    public void showMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            showToast(R.string.server_error);
        } else {
            showToast(msg);
        }
    }

}
