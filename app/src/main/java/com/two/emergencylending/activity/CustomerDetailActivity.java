package com.two.emergencylending.activity;

import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinazyjr.lib.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.refresh.PullToRefreshBase;
import com.refresh.PullToRefreshListView;
import com.two.emergencylending.adapter.CustomerAdapter;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.CustomerBean;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.controller.ClerkController;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.StringUtil;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：客户未到店页面
 * 创建人：szx
 * 创建时间：2016/10/31 9:55
 * 修改人：szx
 * 修改时间：2016/10/31 9:55
 * 修改备注：
 */
public class CustomerDetailActivity extends BaseActivity implements ControllerCallBack {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_customer_type)
    TextView customerType;
    @Bind(R.id.ll_no_data)
    View noData;
    @Bind(R.id.customer_title)
    LinearLayout customerTitle;

    public static final int DIRECT = 0;
    public static final int INDIRECT = 1;
    private List<CustomerBean> customerList;
    private CustomerAdapter adapter;
    PullToRefreshListView mPullRefreshListView;
    ClerkController clerkController;
    int clientType;
    private int currentPage = 1;
    private String pageSize = "20";

    @Override
    public int setContent() {
        return R.layout.activity_customer_detail;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        clientType = getIntent().getIntExtra("flag", 0);
        if (clientType == DIRECT) {
            customerType.setText("未到店(直接客户)");
        } else if (clientType == INDIRECT) {
            customerType.setText("未到店(间接客户)");
        }
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        customerList = new ArrayList<CustomerBean>();
        clerkController = new ClerkController(this, this);
        adapter = new CustomerAdapter(CustomerDetailActivity.this, customerList, clientType);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_list);
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        currentPage = 1;
        initData(currentPage);
    }

    public void initData(int page) {
        if (clientType == DIRECT) {
            clerkController.getClerkDirect(String.valueOf(page), pageSize);
        } else if (clientType == INDIRECT) {
            clerkController.getClerkIndirect(String.valueOf(page), pageSize);
        }
    }


    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(CustomerDetailActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    initData(1);
                } else if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    initData(currentPage);
                }
            }
        });
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                                        }
                                                    }

        );
    }

    @Override
    public void resume() {
    }

    @Override
    public void destroy() {

    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        Message msg = new Message();
        mPullRefreshListView.onRefreshComplete();
        currentPage++;
        if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            currentPage = 2;
            customerList.clear();
            msg.what = 2;
            msg.obj = value;
            handler.sendMessage(msg);
        } else if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            msg.what = 3;
            msg.obj = value;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        mPullRefreshListView.onRefreshComplete();
        if (returnCode == CallBackType.CLERK_DIRECT) {

        } else if (returnCode == CallBackType.CLERK_INDIRECT) {

        }
        Message msg = new Message();
        msg.what = 1;
        msg.obj = errorMessage;
        handler.sendMessage(msg);
    }


    public void initCustomerDetail(String returnValue, int type) {
        try {
            JSONObject jsonObject = new JSONObject(returnValue);
            String count = jsonObject.getString("count");
            String value = jsonObject.getString("list");
            if (StringUtil.isNullOrEmpty(value) || value.equals("[]")) {
                if (type == 0) {
                    adapter.notifyDataSetChanged();
                    customerTitle.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                } else {
                    ToastUtils.showShort(IApplication.globleContext, "没有更多数据了！");
                }
            } else {
                noData.setVisibility(View.GONE);
                customerTitle.setVisibility(View.VISIBLE);
                List<CustomerBean> mineInstantLoans = new Gson().fromJson(value, new TypeToken<List<CustomerBean>>() {
                }.getType());
                for (CustomerBean customerBean : mineInstantLoans) {
                    customerList.add(customerBean);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                case 2:
                    initCustomerDetail(msg.obj.toString(), 0);
                    break;
                case 3:
                    initCustomerDetail(msg.obj.toString(), 1);
                    break;
                case 4:
                    mPullRefreshListView.onRefreshComplete();
                    ToastUtils.showShort(IApplication.globleContext, "没有更多数据了！");
                    break;
                default:
                    break;
            }
        }
    };
}
