package com.two.emergencylending.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.zyjr.emergencylending.R;

public class MyBonusViewHolder extends ViewHolder {
    TextView myBonus;
    TextView myBonusFriend;
    TextView myBonusData;

    // 构造函数中添加自定义的接口的参数
    public MyBonusViewHolder(View itemView) {
        super(itemView);
        myBonus = (TextView) itemView.findViewById(R.id.my_bonus);
        myBonusFriend = (TextView) itemView.findViewById(R.id.my_friend);
        myBonusData = (TextView) itemView.findViewById(R.id.my_bonus_data);

    }

}
