package com.two.emergencylending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.two.emergencylending.bean.NoticeBean;
import com.zyjr.emergencylending.R;

import java.util.List;

public class NoticeAdapter extends BaseAdapter {
    private Context mContext;
    private List<NoticeBean> noticeList;
    private LayoutInflater inflater;

    public NoticeAdapter(Context context, List<NoticeBean> noticeList) {
        this.mContext = context;
        this.noticeList = noticeList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return noticeList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_notice, parent, false);
            viewHolder = new NoticeAdapter.ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NoticeBean itembean = noticeList.get(position);
        viewHolder.title.setText(itembean.getTitle());
        viewHolder.content.setText(itembean.getContent());
        viewHolder.time.setText(itembean.getDate());
        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView content;
        TextView time;
    }
}
