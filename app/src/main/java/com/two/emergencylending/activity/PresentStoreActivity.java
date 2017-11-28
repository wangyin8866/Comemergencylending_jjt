package com.two.emergencylending.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chinazyjr.lib.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.refresh.PullToRefreshBase;
import com.refresh.PullToRefreshListView;
import com.two.emergencylending.adapter.PresentStoreAdapter;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.PresentStore;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.IntentKey;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.PresentStoreCotroller;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：提现地址页面
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class PresentStoreActivity extends BaseActivity implements AdapterView.OnItemClickListener, Topbar.topbarClickListener, ControllerCallBack {
    private final String TAG = PresentStoreActivity.class.getSimpleName();
    @Bind(R.id.present_store_topbar)
    Topbar topbar;
    @Bind(R.id.present_store)
    PullToRefreshListView mPullRefreshListView;
    @Bind(R.id.ll_no_data)
    View noData;
    @Bind(R.id.other_city)
    View otherCity;
    private List<PresentStore> list;
    private PresentStoreAdapter adapter;
    String cityName;
    PresentStoreCotroller psCotroller;
    //返回码
    private final int RESULT_CODE = 100;
    //请求码
    private final int REQUEST_CODE = 101;
    private boolean isPulldown = false;
    PresentStore presentStore;
    private int currentPage = 1;
    private Context context;

    @Override
    public int setContent() {
        return R.layout.activity_present_store;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        context = this;
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        presentStore = (PresentStore) getIntent().getSerializableExtra(IntentKey.DATA);
        if (StringUtil.isNullOrEmpty(presentStore.getCity())) {
            SharedPreferencesUtil.getInstance(PresentStoreActivity.this).setString(SPKey.CITY, "");
//            Intent intent = new Intent(PresentStoreActivity.this, CityActivity.class);
//            startActivityForResult(intent, REQUEST_CODE);
            cityName = "";
        } else {
            cityName = presentStore.getCity();
        }
        psCotroller = new PresentStoreCotroller(this, this);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
//        topbar.getRightButton().setVisibility(View.VISIBLE);
//        topbar.getRightButton().setText(ruleCityname(cityName));
//        topbar.getRightButton().setTextColor(IApplication.globleResource.getColor(R.color.orange));
        list = new ArrayList<PresentStore>();
        adapter = new PresentStoreAdapter(getContext(), list);
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        currentPage = 1;
        isPulldown = false;
        initDataFirst(currentPage);
    }


    public void initDataFirst(int page) {
        if (!StringUtil.isNullOrEmpty(cityName)) {
            LogUtil.check(TAG, "initDataFirst：" + cityName);
            mPullRefreshListView.autoRefresh();
            CommonUtils.showDialog(banseContext, "加载中...");
            psCotroller.getPresentStore(cityName, String.valueOf(page), "20");
        }
    }

    public void initData(int page) {
        if (!StringUtil.isNullOrEmpty(cityName)) {
            LogUtil.check(TAG, "initData：" + cityName);
            psCotroller.getPresentStore(cityName, String.valueOf(page), "20");
        }
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
                String label = DateUtils.formatDateTime(PresentStoreActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    LogUtil.d("mPullRefreshListView", "PULL_FROM_START");
                    isPulldown = false;
                    initData(1);
                } else if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    LogUtil.d("mPullRefreshListView", "PULL_FROM_END");
                    isPulldown = true;
                    initData(currentPage);
                }

            }
        });
        mPullRefreshListView.setOnItemClickListener(this);
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
//        Intent intent = new Intent(PresentStoreActivity.this, CityActivity.class);
//        startActivityForResult(intent, REQUEST_CODE);
    }

    @OnClick(R.id.other_city)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.other_city:
//                Intent intent = new Intent(PresentStoreActivity.this, CityActivity.class);
//                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            cityName = SharedPreferencesUtil.getInstance(PresentStoreActivity.this).getString(SPKey.CITY);
            if (StringUtil.isNullOrEmpty(cityName)) {
                finish();
            } else {
                topbar.getRightButton().setText(ruleCityname(cityName));
                list.clear();
                adapter.notifyDataSetChanged();
                currentPage = 1;
                isPulldown = false;
                initDataFirst(currentPage);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PresentStore presentStore = list.get(position - 1);
        presentStore.setCity(cityName);
        Intent intent = new Intent();
        intent.putExtra(IntentKey.DATA, presentStore);
        setResult(100, intent);
        finish();
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        CommonUtils.closeDialog();
        if (returnCode == CallBackType.PRESENT_STORE) {
            Message msg = new Message();
            mPullRefreshListView.onRefreshComplete();
            currentPage++;
            if (isPulldown) {
                msg.what = 3;
                msg.obj = value;
                handler.sendMessage(msg);
            } else {
                currentPage = 2;
                list.clear();
                msg.what = 2;
                msg.obj = value;
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        CommonUtils.closeDialog();
        mPullRefreshListView.onRefreshComplete();
        Message msg = new Message();
        msg.what = 1;
        msg.obj = errorMessage;
        handler.sendMessage(msg);
    }

    public void initStore(String value, int type) {
        if (StringUtil.isNullOrEmpty(value) || value.equals("[]")) {
            if (type == 0) {
                adapter.notifyDataSetChanged();
                noData.setVisibility(View.VISIBLE);
                mPullRefreshListView.setVisibility(View.GONE);
            } else {
                ToastUtils.showShort(IApplication.globleContext, "没有更多数据了！");
            }
        } else {
            if (type == 0) {
                list.clear();
            }
            mPullRefreshListView.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
            List<PresentStore> stores = new Gson().fromJson(value, new TypeToken<List<PresentStore>>() {
            }.getType());
            for (PresentStore store : stores) {
                LogUtil.check(TAG, "initStore：" + store.toString());
                list.add(store);
            }
            LogUtil.check(TAG, "list size：" + list.size());
            adapter.notifyDataSetChanged();
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
                    initStore(msg.obj.toString(), 0);
                    break;
                case 3:
                    initStore(msg.obj.toString(), 1);
                    break;
                default:
                    break;
            }
        }
    };

    public String ruleCityname(String cityName) {
        if (cityName.length() > 4) {
            String city = cityName.substring(0, 3);
            return city + "...";
        }
        return cityName;
    }
}

