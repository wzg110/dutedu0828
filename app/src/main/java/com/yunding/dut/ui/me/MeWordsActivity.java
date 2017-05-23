package com.yunding.dut.ui.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.yunding.dut.R;
import com.yunding.dut.adapter.MeWordsAdapter;
import com.yunding.dut.model.resp.collect.CollectAllWordsResp;
import com.yunding.dut.presenter.discuss.DiscussPresenter;
import com.yunding.dut.presenter.me.MeWordsPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yunding.dut.util.third.ConvertUtils.dp2px;

/**
 * Created by Administrator on 2017/5/17.
 */

public class MeWordsActivity extends ToolBarActivity implements IMeWordsView{


    @BindView(R.id.list_me_word)
    ListView mListMeWord;
    private MeWordsAdapter mMeWordsAdapter;
    private MeWordsPresenter mMeWordsPresenter;
    private CollectAllWordsResp mCollectAllWordsResp;
    public static final String MEWORD = "meWord";
    private static final String TAG = "MeWordsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        setContentView(R.layout.activity_me_word);
        ButterKnife.bind(this);
        setTitle(getString(R.string.my_word));
        mMeWordsPresenter = new MeWordsPresenter(this);
        mMeWordsPresenter.getCollectWordsList();
        mMeWordsAdapter = new MeWordsAdapter(this);
        mListMeWord.setAdapter(mMeWordsAdapter);
        setItemClick();
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//
//            @Override
//            public void create(SwipeMenu menu) {
//                    // create "open" item
//                    SwipeMenuItem openItem = new SwipeMenuItem(
//                            getApplicationContext());
//                    // set item background
//                    openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                            0xCE)));
//                    // set item width
//                    openItem.setWidth(dp2px(90));
//                    // set item title
//                    openItem.setTitle("Open");
//                    // set item title fontsize
//                    openItem.setTitleSize(18);
//                    // set item title font color
//                    openItem.setTitleColor(Color.WHITE);
//                    // add to menu
//                    menu.addMenuItem(openItem);
//
//                    // create "delete" item
//                    SwipeMenuItem deleteItem = new SwipeMenuItem(
//                            getApplicationContext());
//                    // set item background
//                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                            0x3F, 0x25)));
//                    // set item width
//                    deleteItem.setWidth(dp2px(90));
//                    // set a icon
//
//                    // add to menu
//                    menu.addMenuItem(deleteItem);
//
//
//            }
//        };
    }

    private void setItemClick() {
        mListMeWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toMeWordsTranslate = new Intent(MeWordsActivity.this, MeWordTranslateActivity.class);
                toMeWordsTranslate.putExtra(MEWORD, mCollectAllWordsResp.getData().get(position).getEnglish());
                startActivity(toMeWordsTranslate);
            }
        });
        mListMeWord.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final int collectionId = mCollectAllWordsResp.getData().get(position).getCollectionId();
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MeWordsActivity.this);
                builder.setMessage("确定删除？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //开启讨论
                        mMeWordsPresenter.delCollectWords(collectionId);
                        mCollectAllWordsResp.getData().remove(position);
                        mMeWordsAdapter.setCollectAllWordsResp(mCollectAllWordsResp);
                        dialog.dismiss();
                    }
                });
                builder.show();


                return false;
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
        Log.e(TAG, "showWordsList: " );
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
