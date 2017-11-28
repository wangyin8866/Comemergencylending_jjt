package com.two.emergencylending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.two.emergencylending.bean.PresentStore;
import com.zyjr.emergencylending.R;

import java.util.List;

/**
 * Created by User on 2016/8/17.
 */
public class PresentStoreAdapter extends BaseAdapter {
    private Context mContext;
    private List<PresentStore> presentStore;

    public PresentStoreAdapter(Context context, List<PresentStore> presentStore) {
        this.mContext = context;
        this.presentStore = presentStore;
    }


    @Override
    public int getCount() {
        return presentStore.size();
    }

    @Override
    public Object getItem(int position) {
        return presentStore.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.present_store_item, null);
            holder.presentStoreCompay = (TextView) convertView.findViewById(R.id.present_store_company);
            holder.presentStoreItem = (TextView) convertView.findViewById(R.id.present_store_item);
            holder.getPresentStoreAddress = (TextView) convertView.findViewById(R.id.present_store_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.presentStoreCompay.setText(presentStore.get(position).getStoreName());
        holder.presentStoreItem.setText("");
        holder.getPresentStoreAddress.setText(presentStore.get(position).getStoreAddr());
        return convertView;
    }

    class ViewHolder {
        TextView presentStoreCompay;
        TextView presentStoreItem;
        TextView getPresentStoreAddress;
    }
}