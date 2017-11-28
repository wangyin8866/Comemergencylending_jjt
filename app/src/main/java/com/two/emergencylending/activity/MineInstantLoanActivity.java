package com.two.emergencylending.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinazyjr.lib.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.refresh.PullToRefreshBase;
import com.refresh.PullToRefreshListView;
import com.two.emergencylending.DataBase.NoticeDao;
import com.two.emergencylending.adapter.MineInstantLoanAdapter;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.MineInstantLoan;
import com.two.emergencylending.bean.Notice;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.CommonConstant;
import com.two.emergencylending.constant.H5PageKey;
import com.two.emergencylending.constant.IntentKey;
import com.two.emergencylending.constant.ManagerKey;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.MineBorrowCotroller;
import com.two.emergencylending.controller.RepaymentControl;
import com.two.emergencylending.manager.ActivityManager;
import com.two.emergencylending.utils.CommonUtils;
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
 * 类描述：我的借款页面
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class MineInstantLoanActivity extends BaseActivity implements Topbar.topbarClickListener, ControllerCallBack {

    @Bind(R.id.mine_instant_loan_topbar)
    Topbar topbar;
    @Bind(R.id.mine_instant_loan_total)
    TextView amount;
    @Bind(R.id.ll_no_data)
    View noData;
    @Bind(R.id.ll_borrow)
    View borrow;
    @Bind(R.id.mine_instant_loan_title)
    View loanTitle;
    @Bind(R.id.apply_borrow)
    TextView applyBorrow;
    private List<MineInstantLoan> list;
    private MineInstantLoanAdapter adapter;
    private MineBorrowCotroller mineBorrow;
    private int from;
    PullToRefreshListView mPullRefreshListView;
    private int currentPage = 1;
    private String H5Token;
    private String H5Status;
    NoticeDao mNoticeDaoDao;
    MineInstantLoan loan;

    @Override
    public int setContent() {
        return R.layout.activity_mine_instant_loan;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        from = getIntent().getIntExtra(IntentKey.FROM, 0);
        ActivityManager.getInstance().inputActivity(ManagerKey.ACTIVITY_REPAYMENT, this);
        mNoticeDaoDao = new NoticeDao(MineInstantLoanActivity.this);
        Notice notice = new Notice();
        notice.setType("2");
        notice.setUserId(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        notice.setHasNotice("0");
        if (mNoticeDaoDao.isExist(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID), "2")) {
            mNoticeDaoDao.updateNoticeByUserIdAndType(mNoticeDaoDao, notice);
        }
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        mineBorrow = new MineBorrowCotroller(this, this);
//        mineBorrow.getMineBorrowAmount();
        list = new ArrayList<MineInstantLoan>();
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.mine_instant_loan);
        adapter = new MineInstantLoanAdapter(MineInstantLoanActivity.this, list);
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        currentPage = 1;
        mPullRefreshListView.autoRefresh();
        CommonUtils.showDialog(banseContext, "加载中...");
        initData(currentPage);
    }

    public void initData(int page) {
        mineBorrow.getMineBorrow(String.valueOf(page), "20");
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
                String label = DateUtils.formatDateTime(MineInstantLoanActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    LogUtil.d("mPullRefreshListView", "PULL_FROM_START");
                    initData(1);
                } else if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    LogUtil.d("mPullRefreshListView", "PULL_FROM_END");
//                    initData(currentPage);
                    Message msg = new Message();
                    msg.what = 4;
                    handler.sendMessage(msg);

                }
            }
        });
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                            loan = list.get(position - 1);
//                                                            if (!CommonConstant.PRODUCT_FXDONLINE.equals(loan.getProduct_id())) {
                                                                if (loan.getBorrow_status().equals("4") || loan.getBorrow_status().equals("5")) {
                                                                    if (StringUtil.isNullOrEmpty(H5Token)) {
                                                                        if (StringUtil.isNullOrEmpty(SharedPreferencesUtil.getInstance(MineInstantLoanActivity.this).getString(SPKey.ID_CARD))) {
                                                                            ToastUtils.showShort(MineInstantLoanActivity.this, "加载失败,请重试！");
                                                                            return;
                                                                        }
                                                                        if (StringUtil.isNullOrEmpty(SharedPreferencesUtil.getInstance(MineInstantLoanActivity.this).getString(SPKey.USERNAME))) {
                                                                            ToastUtils.showShort(MineInstantLoanActivity.this, "加载失败,请重试！");
                                                                            return;
                                                                        }
                                                                        repayment(SharedPreferencesUtil.getInstance(MineInstantLoanActivity.this).getString(SPKey.ID_CARD), SharedPreferencesUtil.getInstance(MineInstantLoanActivity.this).getString(SPKey.USERNAME), loan.getContract_code());
                                                                    } else {
                                                                        if (!StringUtil.isNullOrEmpty(H5Token)) {
                                                                            Intent i = new Intent();
                                                                            i.putExtra("token", H5Token);
                                                                            i.putExtra("page", H5PageKey.BILL);
                                                                            i.putExtra("id", loan.getContract_code());
                                                                            CommonUtils.goToActivity(MineInstantLoanActivity.this, RepaymentActivity.class, i);//我的还款
                                                                        }
                                                                    }
                                                                }
//                                                            } else {
//                                                                if (loan.getBorrow_status().equals("4")
//                                                                        || loan.getBorrow_status().equals("5")) {
//                                                                    Intent i = new Intent();
//                                                                    i.putExtra("FXDREPAYMENT", loan.getContract_code());
//                                                                    CommonUtils.goToActivity(getContext(), RepaymentActivity.class, i);//我的还款
//                                                                }
//
//                                                            }

                                                        }
                                                    }
        );
    }

    @OnClick(R.id.apply_borrow)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply_borrow:
                IApplication.getInstance().isToHome = true;
                finish();
                break;
        }
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
    }

    @Override
    public void destroy() {
        ActivityManager.getInstance().removeActivity(ManagerKey.ACTIVITY_REPAYMENT, this);
    }

    @Override
    public void leftClick() {
        back();
    }

    @Override
    public void rightClick() {

    }

    /**
     * 手机上的物理返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            back();
        }
        return false;
    }


    public void repayment(String idCard, String phone, final String id) {
        RepaymentControl.getInstance(MineInstantLoanActivity.this).setOnCompleteListener(new RepaymentControl.OnCompleteListener() {
            @Override
            public void complete(int status, String msg, String token, String result) {
                try {
//                    if (!CommonConstant.PRODUCT_FXDONLINE.equals(loan.getProduct_id())) {
                        if (status == 0) {
                            JSONObject json = new JSONObject(result);
                            H5Token = token;
                            H5Status = json.get("userState").toString();
                            if (!StringUtil.isNullOrEmpty(H5Token)) {
                                Intent i = new Intent();
                                i.putExtra("token", token);
                                i.putExtra("page", H5PageKey.BILL);
                                i.putExtra("id", id);
                                CommonUtils.goToActivity(MineInstantLoanActivity.this, RepaymentActivity.class, i);//我的还款
                            }
                        } else {
                            ToastUtils.showShort(MineInstantLoanActivity.this, msg);
                        }
//                    } else {
//                        Intent i = new Intent();
//                        i.putExtra("FXDPRODUCTID", loan.getProduct_id());
//                        CommonUtils.goToActivity(MineInstantLoanActivity.this, RepaymentActivity.class, i);//我的还款
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        RepaymentControl.getInstance(MineInstantLoanActivity.this).requestRepayment("360730198405061720", "18679317867");
        RepaymentControl.getInstance(MineInstantLoanActivity.this).requestRepayment(idCard, phone);
    }

    public boolean checkStatus(String status) {
        switch (Integer.valueOf(status)) {
            case 0:
                ToastUtils.showShort(MineInstantLoanActivity.this, "借款仍在审核中");
                return false;
            case 1:
                return true;
            case 2:
                return true;
            case 9:
                ToastUtils.showShort(MineInstantLoanActivity.this, "借款未通过，请核对资料");
                return false;
            default:
                break;
        }
        return false;
    }


    @Override
    public void onSuccess(int returnCode, String value) {
        CommonUtils.closeDialog();
        Message msg = new Message();
        if (returnCode == CallBackType.MINE_BORROW) {
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
        } else if (returnCode == CallBackType.MINE_BORROW_AMOUNT) {
            if (StringUtil.isNullOrEmpty(value)) {
                amount.setText("累计借款 0 元");
            } else {
                try {
                    JSONObject json = new JSONObject(value);
                    amount.setText("累计借款 " + json.getString("borrow_limit") + " 元");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        CommonUtils.closeDialog();
        if (returnCode == CallBackType.MINE_BORROW) {
            mPullRefreshListView.onRefreshComplete();
        } else if (returnCode == CallBackType.MINE_BORROW_AMOUNT) {
        }
        Message msg = new Message();
        msg.what = 1;
        msg.obj = errorMessage;
        handler.sendMessage(msg);
    }

    public void initMineBorrow(String returnValue, int type) {
        try {
            JSONObject jsonObject = new JSONObject(returnValue);
            String count = jsonObject.getString("count");
            String value = jsonObject.getString("list");
            if (StringUtil.isNullOrEmpty(value) || value.equals("[]")) {
                if (type == 0) {
                    adapter.notifyDataSetChanged();
                    borrow.setVisibility(View.GONE);
                    loanTitle.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                } else {
                    ToastUtils.showShort(IApplication.globleContext, "没有更多数据了！");
                }
            } else {
                noData.setVisibility(View.GONE);
                borrow.setVisibility(View.VISIBLE);
                loanTitle.setVisibility(View.VISIBLE);
                List<MineInstantLoan> mineInstantLoans = new Gson().fromJson(value, new TypeToken<List<MineInstantLoan>>() {
                }.getType());
                for (MineInstantLoan mineInstantLoan : mineInstantLoans) {
                    list.add(mineInstantLoan);
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
                    initMineBorrow(msg.obj.toString(), 0);
                    break;
                case 3:
                    initMineBorrow(msg.obj.toString(), 1);
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


    public void back() {
        if (from == 1) {
            IApplication.getInstance().isToMine = true;
        }
        finish();
    }
}
