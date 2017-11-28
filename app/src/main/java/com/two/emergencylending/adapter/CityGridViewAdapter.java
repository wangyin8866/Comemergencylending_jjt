package com.two.emergencylending.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.two.emergencylending.bean.CityBean;
import com.zyjr.emergencylending.R;

import java.util.List;

/**
 * 头部热门城市的适配器
 */

public class CityGridViewAdapter extends CityBaseAdapter<CityBean, GridView> {
    private LayoutInflater inflater;
    private List<CityBean> list;

    public CityGridViewAdapter(Context ct, List<CityBean> list) {
        super(ct, list);
        inflater = LayoutInflater.from(ct);
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_hot_city, null);
            holder.cityname = (TextView) convertView.findViewById(R.id.cityname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CityBean info = list.get(position);
        if (position % 3 == 0) {
            holder.cityname.setGravity(Gravity.LEFT);
        }
        if (position % 3 == 1) {
            holder.cityname.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        if (position % 3 == 2) {
            holder.cityname.setGravity(Gravity.RIGHT);
        }
        holder.cityname.setText(info.getName());
        return convertView;
    }

    class ViewHolder {
        TextView cityname;
    }
}