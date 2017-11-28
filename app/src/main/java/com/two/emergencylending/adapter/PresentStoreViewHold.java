package com.two.emergencylending.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zyjr.emergencylending.R;

/**
 * Created by User on 2016/8/9.
 */
public class PresentStoreViewHold extends RecyclerView.ViewHolder {
    TextView presentStoreCompay;
    TextView presentStoreItem;
    TextView getPresentStoreAddress;

    // 构造函数中添加自定义的接口的参数
    public PresentStoreViewHold(View itemView) {
        super(itemView);
        presentStoreCompay = (TextView) itemView.findViewById(R.id.present_store_company);
        presentStoreItem = (TextView) itemView.findViewById(R.id.present_store_item);
        getPresentStoreAddress = (TextView) itemView.findViewById(R.id.present_store_address);

    }
}