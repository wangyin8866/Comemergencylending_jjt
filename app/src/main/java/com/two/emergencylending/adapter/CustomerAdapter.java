package com.two.emergencylending.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.two.emergencylending.activity.CustomerDetailActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.CustomerBean;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.zyjr.emergencylending.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends BaseAdapter {
    private Context mContext;
    private List<CustomerBean> customerList;
    private LayoutInflater inflater;
    private int type;
    CustomerDialog dialog;

    public CustomerAdapter(Context context, List<CustomerBean> customerList, int type) {
        this.mContext = context;
        this.customerList = customerList;
        this.type = type;
        inflater = LayoutInflater.from(context);
        dialog = new CustomerDialog((Activity) mContext);
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
            convertView = inflater.inflate(R.layout.item_customer_detail, parent, false);
            viewHolder = new CustomerAdapter.ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.phone);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CustomerBean itembean = customerList.get(position);
        viewHolder.name.setText(itembean.getUsername());
        viewHolder.date.setText(itembean.getBi_create());
        viewHolder.phone.setText(itembean.getPhone());
        viewHolder.phone.setTag(itembean.getPhone());
        if (queryRecord(itembean)) {
            Drawable called = IApplication.globleResource.getDrawable(R.drawable.phone_called);
            called.setBounds(0, 0, called.getMinimumWidth(), called.getMinimumHeight());
            viewHolder.phone.setCompoundDrawables(called, null, null, null);
            viewHolder.phone.setTextColor(IApplication.globleResource.getColor(R.color.super_light_grey));
        } else {
            Drawable uncall = IApplication.globleResource.getDrawable(R.drawable.phone_uncall);
            uncall.setBounds(0, 0, uncall.getMinimumWidth(), uncall.getMinimumHeight());
            viewHolder.phone.setCompoundDrawables(uncall, null, null, null);
            viewHolder.phone.setTextColor(IApplication.globleResource.getColor(R.color.light_grey));
        }
        viewHolder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CustomerBean bean : customerList) {
                    if (bean.getPhone().equals(v.getTag())) {
//                        bean.setCalled(true);
//                        Drawable uncall = IApplication.globleResource.getDrawable(R.drawable.phone_called);
//                        uncall.setBounds(0, 0, uncall.getMinimumWidth(), uncall.getMinimumHeight());
//                        ((TextView) v).setCompoundDrawables(uncall, null, null, null);
//                        ((TextView) v).setTextColor(IApplication.globleResource.getColor(R.color.super_light_grey));
                        dialog.showCallDialog(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (view.getId() == R.id.ll_confirm) {
                                    TextView textView = (TextView) view.getTag(R.id.tag_first);
                                    Drawable uncall = IApplication.globleResource.getDrawable(R.drawable.phone_called);
                                    uncall.setBounds(0, 0, uncall.getMinimumWidth(), uncall.getMinimumHeight());
                                    textView.setCompoundDrawables(uncall, null, null, null);
                                    textView.setTextColor(IApplication.globleResource.getColor(R.color.super_light_grey));
                                    CustomerBean customerBean = (CustomerBean) view.getTag(R.id.tag_second);
                                    CommonUtils.callPhone(mContext, customerBean.getPhone());
                                    saveRecord(customerBean);
                                    dialog.dismiss();
                                }
                            }
                        }, v, bean);
                        dialog.show();
                    }
                }
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        TextView date;
        TextView phone;
    }

    public List<CustomerBean> getRecord(CustomerBean bean) {
        String phoneRecord = "";
        if (type == CustomerDetailActivity.DIRECT) {
            phoneRecord = SharedPreferencesUtil.getInstance(mContext).getString(SPKey.CUSTOMER_DERECT, "");
        } else if (type == CustomerDetailActivity.INDIRECT) {
            phoneRecord = SharedPreferencesUtil.getInstance(mContext).getString(SPKey.CUSTOMER_INDERECT, "");
        }
        List<CustomerBean> phones = new Gson().fromJson(phoneRecord, new TypeToken<List<CustomerBean>>() {
        }.getType());
        return phones;
    }

    public boolean queryRecord(CustomerBean bean) {
        boolean flag = false;
        List<CustomerBean> phones = getRecord(bean);
        if (phones != null) {
            for (CustomerBean customerBean : phones) {
                if (customerBean.getPhone().equals(bean.getPhone())) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    public void saveRecord(CustomerBean bean) {
        if (!queryRecord(bean)) {
            List<CustomerBean> phones = getRecord(bean);
            if (phones == null) {
                phones = new ArrayList<CustomerBean>();
            }
            phones.add(bean);
            String savePhone = new Gson().toJson(phones);
            if (type == CustomerDetailActivity.DIRECT) {
                SharedPreferencesUtil.getInstance(mContext).setString(SPKey.CUSTOMER_DERECT, savePhone);
            } else if (type == CustomerDetailActivity.INDIRECT) {
                SharedPreferencesUtil.getInstance(mContext).setString(SPKey.CUSTOMER_INDERECT, savePhone);
            }
        }
    }

}
