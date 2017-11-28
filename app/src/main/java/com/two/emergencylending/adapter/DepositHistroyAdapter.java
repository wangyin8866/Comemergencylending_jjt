package com.two.emergencylending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.two.emergencylending.base.DepositHistroy;
import com.zyjr.emergencylending.R;

import java.util.List;

/**
 * Created by User on 2016/8/10.
 */
public class DepositHistroyAdapter extends BaseAdapter {
    private Context mContext;
    private List<DepositHistroy> depositHistroys;
    private LayoutInflater inflater;

    public DepositHistroyAdapter(Context context, List<DepositHistroy> depositHistroys) {
        this.mContext = context;
        this.depositHistroys = depositHistroys;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return depositHistroys.size();
    }

    @Override
    public Object getItem(int position) {
        return depositHistroys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dposit_history_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.depositAMount = (TextView) convertView.findViewById(R.id.deposit_history_amout);
            viewHolder.getDepositData = (TextView) convertView.findViewById(R.id.deposit_history_data);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DepositHistroy itembean = depositHistroys.get(position);
        viewHolder.depositAMount.setText(itembean.getCommission() + " 元");
        viewHolder.getDepositData.setText(itembean.getCreate_date());
        return convertView;
    }

    private static class ViewHolder {
        TextView depositAMount;
        TextView getDepositData;
    }
}
