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
import com.two.emergencylending.DataBase.NoticeDao;
import com.two.emergencylending.adapter.MyBonusAdapter;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.MyBonus;
import com.two.emergencylending.bean.Notice;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.MyUserBonusController;
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

/**
 * 项目名称：急借通
 * 类描述：我的奖金
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */
public class MyBonusActivity extends BaseActivity implements Topbar.topbarClickListener, ControllerCallBack {

    @Bind(R.id.my_bonus_topbar)
    Topbar topbar;
    @Bind(R.id.my_current_bonus)
    TextView bonusAvailable;
    @Bind(R.id.my_total_bonus)
    TextView total;
    @Bind(R.id.ll_no_data)
    View noData;
    @Bind(R.id.ll_earn_record_title)
    View earnRecordTitle;
    private List<MyBonus> myBonusList;
    private MyBonusAdapter adapter;
    MyUserBonusController myUserBonus;
    PullToRefreshListView mPullRefreshListView;
    private int currentPage = 1;
    NoticeDao mNoticeDaoDao;

    public int setContent() {
        return R.layout.activity_my_bonus;
    }

    @Override
    public int setStatusColor() {
        return R.color.red;
    }

    public void init() {
        mNoticeDaoDao = new NoticeDao(MyBonusActivity.this);
        Notice notice = new Notice();
        notice.setType("3");
        notice.setUserId(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        notice.setHasNotice("0");
        if (mNoticeDaoDao.isExist(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID), "3")) {
            mNoticeDaoDao.updateNoticeByUserIdAndType(mNoticeDaoDao, notice);
        }
        myUserBonus = new MyUserBonusController(this, this);
        myUserBonus.getBonusTotal();
        myUserBonus.getBonusAvailable();
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back_white);
        topbar.getRightButton().setVisibility(View.VISIBLE);
        topbar.getRightButton().setText("提现历史");
        myBonusList = new ArrayList<MyBonus>();
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.prlv_earn_record);
        adapter = new MyBonusAdapter(MyBonusActivity.this, myBonusList);
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        currentPage = 1;
        mPullRefreshListView.autoRefresh();
        initData(currentPage);
    }

    public void initData(int page) {
        myUserBonus.getEarnRecord(String.valueOf(page), "20");
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
                String label = DateUtils.formatDateTime(MyBonusActivity.this, System.currentTimeMillis(),
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
        CommonUtils.goToActivity(getContext(), DepositHistroyActivity.class);
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        try {
            if (returnCode == CallBackType.BONUS_TOTAL) {
                if (StringUtil.isNullOrEmpty(value)) {
                    total.setText("0");
                } else {
                    JSONObject json = new JSONObject(value);
                    total.setText(json.getString("commission"));
                }
            } else if (returnCode == CallBackType.BONUS_AVAILABLE) {
                if (StringUtil.isNullOrEmpty(value)) {
                    bonusAvailable.setText("0");
                } else {
                    JSONObject json = new JSONObject(value);
                    bonusAvailable.setText(json.getString("commission"));
                }
            } else if (returnCode == CallBackType.BONUS_EARN_RECORD) {
                mPullRefreshListView.onRefreshComplete();
                Message msg = new Message();
                currentPage++;
                if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    currentPage = 2;
                    myBonusList.clear();
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
        if (returnCode == CallBackType.BONUS_TOTAL) {
        } else if (returnCode == CallBackType.BONUS_AVAILABLE) {
        } else if (returnCode == CallBackType.BONUS_EARN_RECORD) {
            mPullRefreshListView.onRefreshComplete();
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
                    earnRecordTitle.setVisibility(View.GONE);
                    mPullRefreshListView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                } else {
                    ToastUtils.showShort(IApplication.globleContext, "没有更多数据了！");
                }
            } else {
                noData.setVisibility(View.GONE);
                earnRecordTitle.setVisibility(View.VISIBLE);
                mPullRefreshListView.setVisibility(View.VISIBLE);
                List<MyBonus> bonus = new Gson().fromJson(value, new TypeToken<List<MyBonus>>() {
                }.getType());
                for (MyBonus bean : bonus) {
                    myBonusList.add(bean);
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
                default:
                    break;
            }
        }
    };
}
