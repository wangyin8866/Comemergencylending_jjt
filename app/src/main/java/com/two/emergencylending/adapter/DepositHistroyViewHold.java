package com.two.emergencylending.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zyjr.emergencylending.R;

/**
 * Created by User on 2016/8/10.
 */
public class DepositHistroyViewHold  extends RecyclerView.ViewHolder {
    TextView depositAMount;
    TextView getDepositData;

    // 构造函数中添加自定义的接口的参数
    public DepositHistroyViewHold(View itemView) {
        super(itemView);
        depositAMount = (TextView) itemView.findViewById(R.id.deposit_history_amout);
        getDepositData = (TextView) itemView.findViewById(R.id.deposit_history_data);
    }

}