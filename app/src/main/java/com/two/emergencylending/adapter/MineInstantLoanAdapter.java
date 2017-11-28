package com.two.emergencylending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zyjr.emergencylending.R;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.MineInstantLoan;
import com.two.emergencylending.manager.BorrowStatusManager;
import com.two.emergencylending.utils.StringUtil;

import java.util.List;

public class MineInstantLoanAdapter extends BaseAdapter {
    private Context mContext;
    private List<MineInstantLoan> mineInstantLoans;
    private LayoutInflater inflater;

    public MineInstantLoanAdapter(Context context, List<MineInstantLoan> mineInstantLoans) {
        this.mContext = context;
        this.mineInstantLoans = mineInstantLoans;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mineInstantLoans.size();
    }

    @Override
    public Object getItem(int position) {
        return mineInstantLoans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mine_instant_loan_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mineInstantLoanTime = (TextView) convertView.findViewById(R.id.mine_instant_loan_time);
            viewHolder.mineInstantLoanAmount = (TextView) convertView.findViewById(R.id.mine_instant_loan_amount);
            viewHolder.mineInstantLoanData = (TextView) convertView.findViewById(R.id.mine_instant_loan_data);
            viewHolder.mineInstantState = (TextView) convertView.findViewById(R.id.mine_instant_loan_state);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MineInstantLoan itembean = mineInstantLoans.get(position);
        viewHolder.mineInstantLoanTime.setText(itembean.getCreate_date());
        if (!StringUtil.isNullOrEmpty(itembean.getLoan_amt()) && !StringUtil.isNullOrEmpty(itembean.getPeriods())) {
            viewHolder.mineInstantLoanAmount.setText(itembean.getLoan_amt());
            viewHolder.mineInstantLoanData.setText(itembean.getPeriods());
        } else {
            viewHolder.mineInstantLoanAmount.setText(itembean.getBorrow_limit());
            viewHolder.mineInstantLoanData.setText(itembean.getBorrow_periods());
        }
        String status = "";
        String borrowStatus = itembean.getBorrow_status();
        //根据状态显示不同的提示
        status = BorrowStatusManager.showClerkCustomerStatus(borrowStatus);
        if ("19".equals(borrowStatus) || "1".equals(borrowStatus) || "3".equals(borrowStatus) || "16".equals(borrowStatus) || "20".equals(borrowStatus)
                || "21".equals(borrowStatus) || "22".equals(borrowStatus) || "24".equals(borrowStatus)) {//审核签约放款失败用灰色
            viewHolder.mineInstantState.setTextColor(IApplication.globleResource.getColor(R.color.lightgrays));
        } else if ("4".equals(borrowStatus) || "5".equals(borrowStatus)) {//放款成功和已结清
            viewHolder.mineInstantState.setTextColor(IApplication.globleResource.getColor(R.color.basic_infor));
        } else {
            viewHolder.mineInstantState.setTextColor(IApplication.globleResource.getColor(R.color.fontcolor));
        }
        viewHolder.mineInstantState.setText(status);
        return convertView;
    }

    private static class ViewHolder {
        TextView mineInstantLoanTime;
        TextView mineInstantLoanAmount;
        TextView mineInstantLoanData;
        TextView mineInstantState;
    }

}

