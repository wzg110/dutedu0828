package com.yunding.dut.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunding.dut.R;
import com.yunding.dut.model.resp.collect.CollectAllWordsResp;

/**
 * Created by Administrator on 2017/5/23.
 */

public class MeWordsAdapter extends BaseAdapter {

    private Context mContext;
    private CollectAllWordsResp mCollectAllWordsResp;

    public MeWordsAdapter(Context context) {
        mContext = context;
    }

    public void setCollectAllWordsResp(CollectAllWordsResp collectAllWordsResp) {
        mCollectAllWordsResp = collectAllWordsResp;
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
        meHolder.tvMeWords.setText(mCollectAllWordsResp.getData().get(position).getEnglish());


        return convertView;
    }

    private class MeHolder {
        private TextView tvMeWords;

        public MeHolder(View convertView) {
            tvMeWords = (TextView) convertView.findViewById(R.id.tv_me_word);


        }
    }

}
