package com.two.emergencylending.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.chinazyjr.lib.util.ToastUtils;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.FeedBackController;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：意见反馈页面
 * 创建人：szx
 * 创建时间：2016/8/11 14:49
 * 修改人：szx
 * 修改时间：2016/8/11 14:49
 * 修改备注：
 */
public class FeedBackActivity extends BaseActivity implements Topbar.topbarClickListener {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.et_more_suggest)
    EditText etMoreSuggest;
    FeedBackController feedBack;

    @Override
    public int setContent() {
        return R.layout.activity_feedback;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    CommonUtils.closeDialog();
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    ControllerCallBack feedCall = new ControllerCallBack() {
        @Override
        public void onSuccess(int returnCode, String value) {
            if (returnCode == CallBackType.SUBMIT_SUGGESTION) {
                CommonUtils.closeDialog();
                getContext().finish();
            }
        }

        @Override
        public void onFail(int returnCode, final String errorMessage) {
            if (returnCode == CallBackType.SUBMIT_SUGGESTION) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = errorMessage;
                handler.sendMessage(msg);
            }
        }
    };

    @Override
    public void init() {
        findViewById(R.id.bt_more_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btMoreFeedback();
            }
        });
        if (feedBack == null)
            feedBack = new FeedBackController(this, feedCall);
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
    }

    @Override
    public void destroy() {

    }

    private void btMoreFeedback() {
        String moreSuggest = etMoreSuggest.getText().toString().trim();
        if (TextUtils.isEmpty(moreSuggest)) {
            ToastAlone.showLongToast(getContext(), "请输入反馈意见!");
            return;
        }
        feedBack.feedBack(moreSuggest);
    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }

}
