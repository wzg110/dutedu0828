package com.yunding.dut.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.collect.CollectAllWordsResp;
import com.yunding.dut.model.resp.translate.JSTranslateBean;
import com.yunding.dut.model.resp.translate.YDTranslateBean;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.util.FontsUtils;
import com.yunding.dut.util.api.ApisTranslate;
import com.yunding.dut.util.third.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 类 名 称：MeWordsAdapter
 * <P/>描    述：我的生词列表Adapter
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:00
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:00
 * <P/>修改备注：
 * <P/>版    本：
 */
public class MeWordsAdapter extends BaseAdapter {
    private MediaPlayer mMediaPlayer;
    private Context mContext;
    private CollectAllWordsResp mCollectAllWordsResp;
    private boolean[] showControl;
    private String readingUrl;
    private Map<String, String> readUrlMmap = new HashMap<>();

    public MeWordsAdapter(Context context) {
        this.mContext = context;
        mMediaPlayer = new MediaPlayer();
    }


    public void setCollectAllWordsResp(CollectAllWordsResp collectAllWordsResp) {
        mCollectAllWordsResp = collectAllWordsResp;
        showControl = new boolean[mCollectAllWordsResp == null ? 0 : mCollectAllWordsResp.getData().size()]; // 构造器中初始化布尔数组
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCollectAllWordsResp == null ? 0 : mCollectAllWordsResp.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return mCollectAllWordsResp.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MeHolder meHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_me_words, parent, false);
            meHolder = new MeHolder(convertView);
            convertView.setTag(meHolder);
        } else {
            meHolder = (MeHolder) convertView.getTag();
        }


        meHolder.ivSpeak.setBackgroundResource(R.drawable.laba_anim);
        meHolder.tvMeWords.setTypeface(DUTApplication.getHsbwTypeFace());
        meHolder.tvMeWords.setText(mCollectAllWordsResp.getData().get(position).getEnglish());
        meHolder.tvExplain.setTag(position);
        meHolder.ivSpeak.setTag(position);
        if (showControl[position]) {
            meHolder.rlhideArea.setVisibility(View.VISIBLE);
        } else {
            meHolder.rlhideArea.setVisibility(View.GONE);
        }

        final MeHolder finalMeHolder = meHolder;
        meHolder.tvExplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 根据点击位置改变控制数组中对应位置的布尔值
                int tag = (Integer) view.getTag();
                // 如果已经是true则改为false，反过来同理（即点击展开，再次点击收起）
                if (showControl[tag]) {
                    showControl[tag] = false;
                } else {
                    showControl[tag] = true;
                }
                readingUrl = "";
                getTranslation(mCollectAllWordsResp.getData().get(tag).getEnglish(), finalMeHolder);
                //通知adapter数据改变需要重新加载
                //必须要有一步
                notifyDataSetChanged();
            }
        });

        return convertView;
    }


    private class MeHolder {
        private TextView tvMeWords;
        private TextView tvExplain;

        private TextView tvPhonogram;
        private ImageView ivSpeak;
        private TextView tvTranslate;
        private RelativeLayout rlShowArea;
        private LinearLayout rlhideArea;

        public MeHolder(View convertView) {
            tvMeWords = (TextView) convertView.findViewById(R.id.tv_me_word);
            tvExplain = (TextView) convertView.findViewById(R.id.tv_explain);


            tvPhonogram = (TextView) convertView.findViewById(R.id.tv_phonogram);
            ivSpeak = (ImageView) convertView.findViewById(R.id.iv_speak);
            tvTranslate = (TextView) convertView.findViewById(R.id.tv_translate);
            rlShowArea = (RelativeLayout) convertView.findViewById(R.id.rl_show_area);
            rlhideArea = (LinearLayout) convertView.findViewById(R.id.rl_hide_area);


        }
    }

    public void request(@NotNull final String url, final BasePresenter.DUTResp resp) {
        OkHttpUtils.get().tag(this).url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (resp != null)
                    resp.onError(e);
            }

            @Override
            public void onResponse(String response, int id) {
                if (resp != null)
                    resp.onResp(response);
            }
        });
    }

    protected <T> T parseJson(@Nullable String json, @NotNull Class<T> classOfT) {
        try {
            return new Gson().fromJson(json, classOfT);
        } catch (Exception e) {
            return null;
        }
    }

    public void getTranslation(final String content, final MeHolder holder) {

        if (content.contains(" ")) {
            //使用有道翻译
            request(ApisTranslate.getYDTRANSLATE(content), new BasePresenter.DUTResp() {
                @Override
                public void onResp(String response) {
                    YDTranslateBean ydTranslateBean = parseJson(response, YDTranslateBean.class);
                    if (ydTranslateBean != null) {
                        switch (ydTranslateBean.getErrorCode()) {
                            case 0:
                                holder.tvPhonogram.setVisibility(View.GONE);
                                holder.ivSpeak.setVisibility(View.GONE);
                                holder.tvTranslate.setText(ydTranslateBean.getTranslation().get(0));
                                break;
                            default:
                                holder.tvPhonogram.setVisibility(View.GONE);
                                holder.ivSpeak.setVisibility(View.GONE);
                                holder.tvTranslate.setText("翻译无结果");

                        }
                    } else {
                        holder.tvPhonogram.setVisibility(View.GONE);
                        holder.ivSpeak.setVisibility(View.GONE);
                        holder.tvTranslate.setText("翻译失败");
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
        } else {
            String contents = content.toLowerCase();
            //使用金山翻译
            request(ApisTranslate.getJSTRANSLATE(contents), new BasePresenter.DUTResp() {
                @Override
                public void onResp(String response) {
                    if (!response.isEmpty()) {
                        JSTranslateBean jsTranslateBean = parseJson(response, JSTranslateBean.class);

                        if (jsTranslateBean != null) {
                            JSTranslateBean.SymbolsBean partsBean = jsTranslateBean.getSymbols().get(0);
                            if (!TextUtils.isEmpty(partsBean.getPh_en_mp3())){
                                readingUrl=partsBean.getPh_en_mp3();
                            }else if (!TextUtils.isEmpty(partsBean.getPh_am_mp3())){
                                readingUrl=partsBean.getPh_am_mp3();
                            }else if (!TextUtils.isEmpty(partsBean.getPh_tts_mp3())){
                                readingUrl=partsBean.getPh_tts_mp3();
                            }else{
                                holder.tvPhonogram.setVisibility(View.GONE);
                                holder.ivSpeak.setVisibility(View.GONE);
                                holder.tvTranslate.setText("翻译失败");
                                return;
                            }
                            readUrlMmap.put(content, readingUrl);
                            holder.ivSpeak.setVisibility(View.VISIBLE);
                            holder.tvPhonogram.setText("[" + partsBean.getPh_en() + "]");
                            StringBuffer strTranslate = new StringBuffer("");
                            for (int i = 0; i < partsBean.getParts().size(); i++) {
                                strTranslate.append(partsBean.getParts().get(i).getPart() + "   " + partsBean.getParts().get(i).getMeans() + "\n");
                            }
                            String str = strTranslate.toString();
                            if (!str.isEmpty()) {
                                if (FontsUtils.isContainChinese(str)
                                        || TextUtils.isEmpty(str)) {

                                } else {
                                    holder.tvTranslate.setTypeface(DUTApplication.getHsbwTypeFace());
                                }
                                holder.tvTranslate.setText(strTranslate);
                                final AnimationDrawable ad = (AnimationDrawable) holder.ivSpeak.getBackground();

                                holder.ivSpeak.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ad.start();
                                        int tag = (int) v.getTag();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!readUrlMmap.get(content).isEmpty()) {
                                                    mMediaPlayer.reset();
                                                    try {
                                                        mMediaPlayer.setDataSource(readUrlMmap.get(content));
                                                        //准备
                                                        mMediaPlayer.prepare();
                                                        //播放
                                                        mMediaPlayer.start();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();

                                                    }
                                                } else {
                                                    ToastUtils.showShortToast("该单词无发音");
                                                }

                                            }
                                        }).start();

                                    }
                                });
                                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        ad.stop();
                                    }
                                });

                            } else {
                                holder.tvPhonogram.setVisibility(View.GONE);
                                holder.ivSpeak.setVisibility(View.GONE);
                                holder.tvTranslate.setText("翻译失败");
                            }

                        } else {
                            holder.tvPhonogram.setVisibility(View.GONE);
                            holder.ivSpeak.setVisibility(View.GONE);
                            holder.tvTranslate.setText("翻译失败");
                        }
                    } else {
                        holder.tvPhonogram.setVisibility(View.GONE);
                        holder.ivSpeak.setVisibility(View.GONE);
                        holder.tvTranslate.setText("翻译失败");
                    }
                }

                @Override
                public void onError(Exception e) {
                }
            });

        }


    }

}
