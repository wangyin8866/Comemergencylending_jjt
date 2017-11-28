package com.two.emergencylending.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyjr.emergencylending.R;

/**
 * Created by User on 2016/8/10.
 */
public class BankViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView iv_icon;
    TextView tv_name, tv_bankcard;
    private OnItemClickListener mListener;

    // 构造函数中添加自定义的接口的参数
    public BankViewHold(View view,OnItemClickListener listener) {
        super(view);
        mListener = listener;
        view.setOnClickListener(this);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_bankcard = (TextView) view.findViewById(R.id.tv_bankcard);
        view.setTag(this);

    }

    public void onClick(View v) {
        mListener.onItemClick(v, getPosition());
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
}
