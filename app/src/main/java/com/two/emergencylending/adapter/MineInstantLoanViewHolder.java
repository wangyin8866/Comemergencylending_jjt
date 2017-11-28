package com.two.emergencylending.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.zyjr.emergencylending.R;

public class MineInstantLoanViewHolder extends ViewHolder {
    TextView mineInstantLoanTime;
    TextView mineInstantLoanData;
    TextView mineInstantLoanAmount;
    TextView mineInstantState;

    // 构造函数中添加自定义的接口的参数
    public MineInstantLoanViewHolder(View itemView) {
        super(itemView);
        mineInstantLoanTime = (TextView) itemView.findViewById(R.id.mine_instant_loan_time);
        mineInstantLoanData = (TextView) itemView.findViewById(R.id.mine_instant_loan_data);
        mineInstantLoanAmount = (TextView) itemView.findViewById(R.id.mine_instant_loan_amount);
        mineInstantState = (TextView) itemView.findViewById(R.id.mine_instant_loan_state);

    }
}
