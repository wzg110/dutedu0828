package com.yunding.dut.ui.ppt;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.adapter.PPTQuestionListAdapter;
import com.yunding.dut.model.resp.ppt.PPTListResp;
import com.yunding.dut.model.resp.ppt.QuestionInfoResp;
import com.yunding.dut.presenter.ppt.PPTInfoPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.view.DUTVerticalRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * 类 名 称：PPTInfoActivity
 * <P/>描    述：废弃
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 16:58
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 16:58
 * <P/>修改备注：
 * <P/>版    本：
 */
public class PPTInfoActivity extends ToolBarActivity implements IPPTInfoView {

    @BindView(R.id.tv_page_no)
    TextView tvPageNo;
    @BindView(R.id.img_ppt)
    SimpleDraweeView imgPpt;
    @BindView(R.id.rv_question_list)
    DUTVerticalRecyclerView rvQuestionList;

    private PPTInfoPresenter mPresenter;
    private PPTQuestionListAdapter mAdapter;
    private PPTListResp.DataBean mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pptinfo);
        ButterKnife.bind(this);

        mInfo = (PPTListResp.DataBean) getIntent().getSerializableExtra("ppt_info");
        if (mInfo != null) {
            showPPTInfo(mInfo);
            mPresenter = new PPTInfoPresenter(this);
            mPresenter.addLog(mInfo.getSubjectId());
            if (!"0".equals(mInfo.getSubjectId())) {
                mPresenter.loadPPTInfo(mInfo.getSubjectId());
                mAdapter = new PPTQuestionListAdapter(new ArrayList<QuestionInfoResp.DataBean>());
                rvQuestionList.setAdapter(mAdapter);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ppt_questions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_commit:
                if (mAdapter != null
                        && mAdapter.getData() != null
                        && mAdapter.getData().size() != 0) {
                    int id = mAdapter.getData().get(0).getId();
                    String content = mAdapter.getData().get(0).getLocalAnswer();
                    if (!TextUtils.isEmpty(content)) {
                        mPresenter.commitAnswer(id, content);
                    } else {
                        showToast("请输入答案");
                    }
                } else {
                    showToast("没有小题");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public void showPPTInfo(PPTListResp.DataBean item) {
        tvPageNo.setText("第几页");
        imgPpt.setImageURI(Uri.parse(Apis.TEST_URL + item.getPPTImgUrl()));
    }

    @Override
    public void showQuestionList(QuestionInfoResp resp) {
        List<QuestionInfoResp.DataBean> list = resp.getData();
        mAdapter.setNewData(list);
    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);
    }

    @Override
    public void commitSuccess() {
        showMsg("提交成功");
        finish();
    }
}
