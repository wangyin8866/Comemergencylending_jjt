package com.two.emergencylending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.two.emergencylending.bean.MyBonus;
import com.two.emergencylending.utils.StringUtil;
import com.zyjr.emergencylending.R;

import java.util.List;

public class MyBonusAdapter extends BaseAdapter {
    private Context mContext;
    private List<MyBonus> myBonus;
    private LayoutInflater inflater;

    public MyBonusAdapter(Context context, List<MyBonus> myBonus) {
        this.mContext = context;
        this.myBonus = myBonus;
        inflater = LayoutInflater.from(context);
    }

    public String starPhoneNum(String phone) {
        String newPhone = "";
        String star = "****";
        if (phone.length() == 11) {
            newPhone = phone.substring(0, 3) + star + phone.substring(phone.length() - 4, phone.length());
            return newPhone;
        }
        return phone;
    }

    @Override
    public int getCount() {
        return myBonus.size();
    }

    @Override
    public Object getItem(int position) {
        return myBonus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.my_bonus_item, parent, false);
            viewHolder = new MyBonusAdapter.ViewHolder();
            viewHolder.my_friend = (TextView) convertView.findViewById(R.id.my_friend);
            viewHolder.my_bonus = (TextView) convertView.findViewById(R.id.my_bonus);
            viewHolder.my_bonus_data = (TextView) convertView.findViewById(R.id.my_bonus_data);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MyBonus itembean = myBonus.get(position);
        if(StringUtil.isNullOrEmpty(itembean.getPhone())){
            viewHolder.my_friend.setText("");
        }else{
            viewHolder.my_friend.setText(starPhoneNum(itembean.getPhone()));
        }
        viewHolder.my_bonus.setText(itembean.getCommission() + " 元");
        viewHolder.my_bonus_data.setText(itembean.getCreate_date());
        return convertView;
    }

    private static class ViewHolder {
        TextView my_friend;
        TextView my_bonus;
        TextView my_bonus_data;
    }
}
