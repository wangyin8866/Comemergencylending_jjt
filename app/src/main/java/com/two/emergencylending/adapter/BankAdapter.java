package com.two.emergencylending.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.two.emergencylending.bean.BankBean;
import com.zyjr.emergencylending.R;

import java.util.List;

/**
 * Created by User on 2016/8/10.
 */
public class BankAdapter extends RecyclerView.Adapter<BankViewHold> {
    private Context mContext;
    private List<BankBean> bankList;
    private OnItemClickListener mClickListener;
    // 添加数据集合
    public void addData(List<BankBean> bankList) {
        bankList.addAll(bankList);
        notifyDataSetChanged();
    }

    public BankAdapter(Context context, List<BankBean> bankList ) {
        this.mContext = context;
        this.bankList = bankList;
    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

    public void onBindViewHolder(BankViewHold viewHolder, int position) {
//        viewHolder.iv_icon.setText(bankList.get(position).getMyBonus());
        viewHolder.tv_name.setText(bankList.get(position).getBankName());
        viewHolder.tv_bankcard.setText(bankList.get(position).getTheBank());
    }

    public BankViewHold onCreateViewHolder(ViewGroup arg0, int arg1) {
        View view = null;
        BankViewHold holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.bank_card_item, arg0, false);
            holder = new BankViewHold(view,mClickListener);
            view.setTag(holder);
        } else {
            holder = (BankViewHold) view.getTag();
        }

        return holder;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }
    
}
