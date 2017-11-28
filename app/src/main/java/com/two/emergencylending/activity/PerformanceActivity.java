package com.two.emergencylending.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinazyjr.lib.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.two.emergencylending.adapter.AnnouncementAdapter;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.AnnouncementBean;
import com.two.emergencylending.bean.ClerkInfoBean;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.controller.ClerkController;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.view.CustomListView;
import com.two.emergencylending.view.Topbar;
import com.two.emergencylending.view.progress.DashedCircularProgress;
import com.zyjr.emergencylending.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：本月累计业绩页面
 * 创建人：szx
 * 创建时间：2016/10/31 9:55
 * 修改人：szx
 * 修改时间：2016/10/31 9:55
 * 修改备注：
 */
public class PerformanceActivity extends BaseActivity implements Topbar.topbarClickListener, AdapterView.OnItemClickListener, ControllerCallBack {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.tv_loan_count)
    TextView tvLoanCount;
    @Bind(R.id.lv_notice)
    CustomListView lvNotice;
    @Bind(R.id.ll_direct_customer)
    LinearLayout directCustomer;
    @Bind(R.id.ll_indirect_customer)
    LinearLayout indirectCustomer;
    @Bind(R.id.tv_direct_customer)
    TextView tvDirectCustomer;
    @Bind(R.id.tv_direct_customer_no_apply)
    TextView tv_direct_customer_no_apply;

    @Bind(R.id.tv_indirect_customer)
    TextView tvIndirectCustomer;
    @Bind(R.id.tv_indirect_customer_no_apply)
    TextView tv_indirect_customer_no_apply;

    @Bind(R.id.circular)
    DashedCircularProgress circular;
    private List<AnnouncementBean> announcementList;
    private AnnouncementAdapter adapter;
    ClerkController clerkController;

    @Override
    public int setContent() {
        return R.layout.activity_performance;
    }

    @Override
    public int setStatusColor() {
        return R.color.transparent;
    }

    @Override
    protected boolean getStick() {
        return true;
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back_white);
        circular.reset();
        circular.setValue(0);

        circular.setOnValueChangeListener(
                new DashedCircularProgress.OnValueChangeListener() {
                    @Override
                    public void onValueChange(float value) {
                        tvLoanCount.setText((int) value + "");
                    }
                });
        announcementList = new ArrayList<AnnouncementBean>();
        clerkController = new ClerkController(this, this);
        initList();
        adapter = new AnnouncementAdapter(PerformanceActivity.this, announcementList);
        lvNotice.setAdapter(adapter);
        clerkController.getClerkInfo();
    }

    public void initList() {
    }

    @Override
    public void setData() {
    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
        lvNotice.setOnItemClickListener(this);
    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }

    @OnClick({R.id.ll_direct_customer, R.id.ll_indirect_customer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_direct_customer:
                Intent intentDirect = new Intent();
                intentDirect.putExtra("flag", CustomerDetailActivity.DIRECT);
                CommonUtils.goToActivity(PerformanceActivity.this, CustomerDetailActivity.class, intentDirect);
                break;
            case R.id.ll_indirect_customer:
                Intent intentIndirect = new Intent();
                intentIndirect.putExtra("flag", CustomerDetailActivity.INDIRECT);
                CommonUtils.goToActivity(PerformanceActivity.this, CustomerDetailActivity.class, intentIndirect);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("data", announcementList.get(position));
        CommonUtils.goToActivity(PerformanceActivity.this, AnnouncementActivity.class, intent);
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.CLERK_INFO) {
            ClerkInfoBean clerkInfoBean = new Gson().fromJson(value,
                    new TypeToken<ClerkInfoBean>() {
                    }.getType());
            initData(clerkInfoBean);
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.CLERK_INFO) {
            ToastUtils.showShort(IApplication.globleContext, errorMessage);
        }
    }

    public void initData(ClerkInfoBean bean) {
        tvLoanCount.setText(bean.getCur_month_com_count());
        if (!StringUtil.isNullOrEmpty(bean.getCur_month_com_count())) {
            circular.reset();
            if (Integer.valueOf(bean.getCur_month_com_count()) < 10) {
                circular.setDuration(500);
            } else {
                circular.setDuration(1000);
            }
            circular.setValue(Integer.valueOf(bean.getCur_month_com_count()));
        }
        tvDirectCustomer.setText(bean.getDir_no_store_count() + "人");
        tv_direct_customer_no_apply.setText(bean.getDir_no_bill_count() + "人");
        tvIndirectCustomer.setText(bean.getIndir_no_store_count() + "人");
        tv_indirect_customer_no_apply.setText(bean.getIndir_no_bill_count() + "人");
        announcementList.clear();
        List<AnnouncementBean> list = bean.getList();
        for (AnnouncementBean announcementBean : list) {
            announcementList.add(announcementBean);
        }
        adapter.notifyDataSetChanged();
    }
}
