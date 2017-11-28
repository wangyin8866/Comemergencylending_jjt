package com.two.emergencylending.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.ClerkCustomerBean;
import com.two.emergencylending.manager.BorrowStatusManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.StringUtil;
import com.zyjr.emergencylending.R;

import java.util.List;

public class ClerkCustomerAdapter extends BaseAdapter {
    private Context mContext;
    private List<ClerkCustomerBean> customerList;
    private LayoutInflater inflater;
    CustomerDialog dialog;

    public ClerkCustomerAdapter(Context context, List<ClerkCustomerBean> customerList) {
        this.mContext = context;
        this.customerList = customerList;
        inflater = LayoutInflater.from(context);
        dialog = new CustomerDialog((Activity) mContext);
    }

    public void refresh(List<ClerkCustomerBean> customerList) {
        this.customerList = customerList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return customerList.size();
    }

    @Override
    public Object getItem(int position) {
        return customerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_clerk_customer, parent, false);
            viewHolder = new ClerkCustomerAdapter.ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
            viewHolder.period = (TextView) convertView.findViewById(R.id.period);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ClerkCustomerBean itembean = customerList.get(position);
        if (position % 2 == 0) {
            viewHolder.icon.setImageResource(R.drawable.label_a);
        } else {
            viewHolder.icon.setImageResource(R.drawable.label_b);
        }
        if (StringUtil.isNullOrEmpty(itembean.getBorrow_limit()) || itembean.getBorrow_limit().equals("0")) {
            viewHolder.amount.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.amount.setVisibility(View.VISIBLE);
            viewHolder.amount.setText(itembean.getBorrow_limit() + "元");
        }
        if (StringUtil.isNullOrEmpty(itembean.getBorrow_periods()) || itembean.getBorrow_periods().equals("0")) {
            viewHolder.period.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.period.setVisibility(View.VISIBLE);
            viewHolder.period.setText(itembean.getBorrow_periods() + "周");
        }
        if (StringUtil.isNullOrEmpty(itembean.getUsername())) {
            viewHolder.name.setText(CommonUtils.phoneNumHide(itembean.getPhone()));
//            viewHolder.name.setTextSize(16);
        } else {
            viewHolder.name.setText(itembean.getUsername());
        }
        int clerkCustomerStatus = BorrowStatusManager.getClerkCustomerStatus(itembean.getBorrow_status());
        if (clerkCustomerStatus == BorrowStatusManager.CLERK_STATUS_APPLY) {
//            viewHolder.status.setBackground(IApplication.globleResource.getDrawable(R.drawable.btn_lineframe));
            viewHolder.status.setTextColor(IApplication.globleResource.getColor(R.color.orange));
        } else if (clerkCustomerStatus == BorrowStatusManager.CLERK_STATUS_AUDIT_FAIL || clerkCustomerStatus == BorrowStatusManager.CLERK_STATUS_LOANING_FAIL || clerkCustomerStatus == BorrowStatusManager.CLERK_STATUS_SIGN_FAIL) {
            viewHolder.status.setTextColor(IApplication.globleResource.getColor(R.color.register_hint));
        } else {
            viewHolder.status.setTextColor(IApplication.globleResource.getColor(R.color.fontcolor));
        }
        viewHolder.status.setText(BorrowStatusManager.showClerkCustomerStatus(itembean.getBorrow_status()));
        return convertView;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView name;
        TextView amount;

        TextView period;
        TextView status;
    }

}
