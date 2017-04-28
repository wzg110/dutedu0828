package com.yunding.dut.presenter.discuss;

import android.text.TextUtils;
import android.util.Log;

import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.model.resp.discuss.DiscussMsgListResp;
import com.yunding.dut.model.resp.discuss.DiscussSubjectResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.discuss.IDiscussView;
import com.yunding.dut.util.api.ApisDiscuss;
import com.yunding.dut.util.file.FileUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

/**
 * 类 名 称：DiscussPresenter
 * <P/>描    述：讨论组消息页面逻辑
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/22 14:54
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/22 14:54
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DiscussPresenter extends BasePresenter {

    public static final int STATE_DISCUSS_NOT_START = 0;
    public static final int STATE_DISCUSSING = 1;
    public static final int STATE_DISCUSS_FINISHED = 2;

    public static final int MSG_TYPE_TEXT = 0;
    public static final int MSG_TYPE_VOICE = 1;
    public static final int MSG_TYPE_IMAGE = 2;

    private IDiscussView mView;

    public DiscussPresenter(IDiscussView mView) {
        this.mView = mView;
    }

    public void startDiscussion(long subjectId, long groupId) {
        String url = ApisDiscuss.startDiscussion(subjectId, groupId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.showMsg("开启成功");
                        mView.showDiscussing();
                    } else {
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.showMsg(null);
                }
            }

            @Override
            public void onError(Exception e) {
                mView.showMsg(e.toString());
            }
        });
    }

    public void refreshMsg(long subjectId, long groupId) {
        String url = ApisDiscuss.discussGroupMsgListUrl(subjectId, groupId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                DiscussMsgListResp resp = parseJson(response, DiscussMsgListResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.showMsgList(resp);
                        if (resp.getData().getTotal() > 0) {
                            int state = resp.getData().getDatas().get(0).getState();
                            switch (state) {
                                case STATE_DISCUSS_NOT_START:
                                    mView.showDiscussNotStart();
                                    break;
                                case STATE_DISCUSSING:
                                    mView.showDiscussing();
                                    break;
                                case STATE_DISCUSS_FINISHED:
                                    mView.showDiscussFinished();
                                    break;
                            }
                        }
                    } else {
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.showMsg(null);
                }
            }

            @Override
            public void onError(Exception e) {
                mView.showMsg(e.toString());
            }
        }, this);
    }

    public void stopRefreshMsg() {
        OkHttpUtils.getInstance().cancelTag(this);
    }

    public void sendMsg(File file, long subjectId, long groupId, final int msgType, int length, String content) {
        if (msgType == MSG_TYPE_TEXT && TextUtils.isEmpty(content)) {
            mView.showMsg("请输入消息内容");
            return;
        }
        if (msgType == MSG_TYPE_VOICE && file == null) {
            mView.showMsg("录音时间太短");
            return;
        }
        String url = ApisDiscuss.discussSendMsg(subjectId, groupId, msgType, length, content);
        long userId = DUTApplication.getUserInfo().getUserId();

        PostFormBuilder builder = OkHttpUtils
                .post()
                .url(url)
                .addParams("themeid", String.valueOf(subjectId))
                .addParams("groupid", String.valueOf(groupId))
                .addParams("studentid", String.valueOf(userId))
                .addParams("messagetype", String.valueOf(msgType))
                .addParams("messagelength", String.valueOf(length))
                .addParams("content", content);
        if (file != null) {
            builder.addFile("file", "record.voice", file);
        } else {
            file = new File(FileUtil.getRecordVoiceDir() + System.currentTimeMillis() + FileUtil.getRecordSuffix());
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.d(getClass().toString(), "sendMsg: " + e.toString());
            }
            builder.addFile("file", "record.voice", file);
        }
        builder.build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mView.showMsg(e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        if (msgType == MSG_TYPE_TEXT)
                            mView.sendTextMsgSuccess();
                    } else {
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.showMsg(null);
                }
            }
        });
    }

    public void loadSubjectInfo(long subjectId) {
        String url = ApisDiscuss.getSubjectInfo(subjectId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                DiscussSubjectResp resp = parseJson(response, DiscussSubjectResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.showSubjectInfo(resp);
                    } else {
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.showMsg(null);
                }

            }

            @Override
            public void onError(Exception e) {
                mView.showMsg(e.toString());
            }
        });
    }
}
