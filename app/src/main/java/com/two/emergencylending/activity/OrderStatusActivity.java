package com.two.emergencylending.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.chinazyjr.lib.util.ToastUtils;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.OrderStatusTimeController;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.DateUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：订单状态
 * 创建人：szx
 * 创建时间：2016/8/11.
 * 修改人：szx
 * 修改时间：2016/8/11.
 * 修改备注：
 */
public class OrderStatusActivity extends BaseActivity implements Topbar.topbarClickListener, ControllerCallBack {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.perfect_info_time)
    TextView perfect_info_time;
    @Bind(R.id.auth_time)
    TextView auth_time;
    @Bind(R.id.approve_time)
    TextView approve_time;
    @Bind(R.id.end_time)
    TextView end_time;
    OrderStatusTimeController orderStatusTimeController;

    @Override
    public int setContent() {
        return R.layout.activity_order_status;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        orderStatusTimeController = new OrderStatusTimeController(this, this);
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
    }

    @Override
    public void setData() {

    }

    public void initData(String value) {
        JSONObject json = null;
        try {
            json = new JSONObject(value);
            if (!StringUtil.isNullOrEmpty(json.get("perfectInfoTim").toString())) {
                perfect_info_time.setText(DateUtil.stringToDateWithoutYearAndSecond(json.get("perfectInfoTim").toString()));
            }
            if (!StringUtil.isNullOrEmpty(json.get("authTim").toString())) {
                auth_time.setText(DateUtil.stringToDateWithoutYearAndSecond(json.get("authTim").toString()));
            }
            if (!StringUtil.isNullOrEmpty(json.get("audiStartTim").toString())) {
                approve_time.setText(DateUtil.stringToDateWithoutYearAndSecond(json.get("audiStartTim").toString()));
                end_time.setText("预计  " + DateUtil.datePlusByHour(json.get("audiStartTim").toString(), 2));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
        orderStatusTimeController.getTime();
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
        if (returnCode == CallBackType.ORDER_STATUS_TIME) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = value;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.ORDER_STATUS_TIME) {
        }
        Message msg = new Message();
        msg.what = 1;
        msg.obj = errorMessage;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                case 2:
                    initData(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
}
