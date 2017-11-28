package com.two.emergencylending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.two.emergencylending.bean.AnnouncementBean;
import com.two.emergencylending.utils.DateUtil;
import com.zyjr.emergencylending.R;

import java.util.List;

public class AnnouncementAdapter extends BaseAdapter {
    private Context mContext;
    private List<AnnouncementBean> announcementList;
    private LayoutInflater inflater;

    public AnnouncementAdapter(Context context, List<AnnouncementBean> announcementList) {
        this.mContext = context;
        this.announcementList = announcementList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return announcementList.size();
    }

    @Override
    public Object getItem(int position) {
        return announcementList.get(position);
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
            viewHolder = new AnnouncementAdapter.ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AnnouncementBean itembean = announcementList.get(position);
        viewHolder.title.setText(itembean.getAct_name());
        viewHolder.content.setText(itembean.getAct_content());
        viewHolder.time.setText(DateUtil.stringToDate(itembean.getAct_date()));
        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView content;
        TextView time;
    }
}
