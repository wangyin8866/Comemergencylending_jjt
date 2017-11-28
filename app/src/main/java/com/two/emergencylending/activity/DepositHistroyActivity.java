package com.two.emergencylending.activity;

import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinazyjr.lib.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.refresh.PullToRefreshBase;
import com.refresh.PullToRefreshListView;
import com.two.emergencylending.adapter.DepositHistroyAdapter;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.base.DepositHistroy;
import com.two.emergencylending.constant.AppConfig;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.MyUserBonusController;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：我的提现历史
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */
public class DepositHistroyActivity extends BaseActivity implements Topbar.topbarClickListener, ControllerCallBack {
    @Bind(R.id.deposit_history_topbar)
    Topbar topbar;
    @Bind(R.id.deposit_histroy)
    PullToRefreshListView mPullRefreshListView;
    @Bind(R.id.total_deposit)
    TextView totalDeposit;
    @Bind(R.id.ll_content)
    View llContent;
    @Bind(R.id.ll_no_data)
    View llNoData;
    @Bind(R.id.share)
    TextView share;

    private List<DepositHistroy> list;
    private DepositHistroyAdapter adapter;
    MyUserBonusController myUserBonus;
    private int currentPage = 1;
    CustomerDialog dialog;
    private long mExitTime = 0;

    @Override
    public int setContent() {
        return R.layout.activity_deposit_histroy;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        dialog = new CustomerDialog(this);
        myUserBonus = new MyUserBonusController(this, this);
        myUserBonus.getBonusExtract();
        list = new ArrayList<DepositHistroy>();
        adapter = new DepositHistroyAdapter(DepositHistroyActivity.this, list);
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        currentPage = 1;
        mPullRefreshListView.autoRefresh();
        initData(currentPage);
    }

    public void initData(int page) {
        myUserBonus.getExtractHistory(String.valueOf(page), "20");
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(DepositHistroyActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    LogUtil.d("mPullRefreshListView", "PULL_FROM_START");
                    initData(1);
                } else if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    LogUtil.d("mPullRefreshListView", "PULL_FROM_END");
                    initData(currentPage);
                }

            }
        });
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            }
        });
    }

    @OnClick(R.id.share)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.share:
                dialog.showShareInviteDialog(AppConfig.SHARE_WITHDRAW, SharedPreferencesUtil.getInstance(DepositHistroyActivity.this).getString(SPKey.SHARE_CODE), true);
                dialog.show();
                break;
        }
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
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

    @Override
    public void onSuccess(int returnCode, String value) {
        CommonUtils.closeDialog();
        try {
            if (returnCode == CallBackType.BONUS_EXTRACT) {
                if (StringUtil.isNullOrEmpty(value)) {
                    totalDeposit.setText("累计提现 0 元");
                } else {
                    JSONObject json = new JSONObject(value);
                    totalDeposit.setText("累计提现 " + json.getString("commission") + " 元");
                }
            } else if (returnCode == CallBackType.BONUS_EXTRACT_HISTORY) {
                Message msg = new Message();
                mPullRefreshListView.onRefreshComplete();
                currentPage++;
                if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    currentPage = 2;
                    list.clear();
                    msg.what = 2;
                    msg.obj = value;
                    handler.sendMessage(msg);
                } else if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    msg.what = 3;
                    msg.obj = value;
                    handler.sendMessage(msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        CommonUtils.closeDialog();
        if (returnCode == CallBackType.BONUS_EXTRACT) {
        } else if (returnCode == CallBackType.BONUS_EXTRACT_HISTORY) {
            CommonUtils.closeDialog();
            mPullRefreshListView.onRefreshComplete();
        }
        Message msg = new Message();
        msg.what = 1;
        msg.obj = errorMessage;
        handler.sendMessage(msg);
    }

    public void initDepositHistroy(String returnValue, int type) {
        try {
            JSONObject jsonObject = new JSONObject(returnValue);
            String count = jsonObject.getString("count");
            String value = jsonObject.getString("list");
            if (StringUtil.isNullOrEmpty(value) || value.equals("[]")) {
                if (type == 0) {
                    adapter.notifyDataSetChanged();
                    llContent.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                } else {
                    ToastUtils.showShort(IApplication.globleContext, "没有更多数据了！");
                }
            } else {
                llNoData.setVisibility(View.GONE);
                llContent.setVisibility(View.VISIBLE);
                List<DepositHistroy> bonus = new Gson().fromJson(value, new TypeToken<List<DepositHistroy>>() {
                }.getType());
                for (DepositHistroy bean : bonus) {
                    list.add(bean);
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
                    initDepositHistroy(msg.obj.toString(), 0);
                    break;
                case 3:
                    initDepositHistroy(msg.obj.toString(), 1);
                    break;
                default:
                    break;
            }
        }
    };

}
