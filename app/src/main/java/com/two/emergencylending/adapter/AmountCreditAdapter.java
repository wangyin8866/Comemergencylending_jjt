package com.two.emergencylending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyjr.emergencylending.R;
import com.two.emergencylending.bean.AmountCreditBean;

import java.util.List;

/**
 * Created by User on 2016/9/29.
 */
public class AmountCreditAdapter extends BaseAdapter {
    private Context mContext;
    List<AmountCreditBean> amountCreditBeans;
    private LayoutInflater inflater;

    public AmountCreditAdapter(Context context, List<AmountCreditBean> amountCreditBeans) {
        this.mContext = context;
        this.amountCreditBeans = amountCreditBeans;
//        this.mpic = pic;
//        this.mAmountContext = amountContent;
//        isSelected = new boolean[amountContent.length];
        inflater = LayoutInflater.from(mContext);
//        //默认状态false
//        for (int i = 0; i < isSelected.length; i++) {
//            isSelected[i] = false;
//        }
    }

    @Override
    public int getCount() {
        return amountCreditBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return amountCreditBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AmountViewHolder amount = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_amount_credit_item, null);
            amount = new AmountViewHolder(convertView);
            convertView.setTag(amount);
        } else {
            amount = (AmountViewHolder) convertView.getTag();
        }
        AmountCreditBean amountCredit = amountCreditBeans.get(position);
        amount.cb_check.setTag(amountCredit.getmId());
        amount.cb_check.setChecked(amountCredit.isSeclect());
        amount.cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (AmountCreditBean amountCredit : amountCreditBeans) {
                    if ((int) buttonView.getTag() == amountCredit.getmId()) {
                        amountCredit.setSeclect(isChecked);//选择状态改为true
                    }
                }
            }
        });
        amount.tv_content.setText(amountCredit.getAmountContent());
        amount.tv_pic.setImageResource(amountCredit.getPic());
        return convertView;
    }

    class AmountViewHolder {
        ImageView tv_pic;
        TextView tv_content;
        CheckBox cb_check;

        public AmountViewHolder(View convertView) {
            tv_pic = (ImageView) convertView.findViewById(R.id.iv_icon);
            tv_content = (TextView) convertView.findViewById(R.id.tv_name);
            cb_check = (CheckBox) convertView.findViewById(R.id.cb_check);
        }

    }

}
