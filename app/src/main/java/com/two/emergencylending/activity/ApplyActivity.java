package com.two.emergencylending.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.BorrowInfoBean;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.utils.Arithmetic;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.view.Topbar;
import com.two.emergencylending.widget.BubbleIndicator;
import com.two.emergencylending.widget.BubbleSeekBar;
import com.two.emergencylending.widget.FlowTagLayout;
import com.two.emergencylending.widget.OnTagSelectListener;
import com.zyjr.emergencylending.R;

import java.util.List;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：立即申请
 * 创建人：szx
 * 创建时间：2017/6/15.
 * 修改人：szx
 * 修改时间：2017/6/15.
 * 修改备注：
 */
public class ApplyActivity extends BaseActivity implements Topbar.topbarClickListener, View.OnClickListener, OnTagSelectListener {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.seekbar_money)
    BubbleSeekBar seekbarMoney;
    @Bind(R.id.seekbar_week)
    BubbleSeekBar seekbarWeek;
    @Bind(R.id.btn_next)
    Button btnNext;

    private CustomerDialog dialog;
    private int money = 5000;
    private int week = 15;
    private String custId, phoneNum;

    @Override
    public int setContent() {
        return R.layout.activity_apple;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        custId = getIntent().getStringExtra("cust_id");
        phoneNum = getIntent().getStringExtra("phone");
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        seekbarMoney.setType(BubbleIndicator.MONEY);
        seekbarWeek.setType(BubbleIndicator.WEEK);
        seekbarMoney.setProgress(30);
        seekbarWeek.setProgress(20);
        money = 5000;
        week = 15;
        dialog = new CustomerDialog(this);
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
        btnNext.setOnClickListener(this);
        seekbarMoney.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                moneyProgress = progress;
                money = Arithmetic.progressToMoney(progress);
//                bean.setBorrow_limit(String.valueOf(money));
//                UserInfoManager.getInstance().setBorrowInfo(bean);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekbarWeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                weekProgress = progress;
                week = Arithmetic.progressToWeek(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbarWeek.hideIndicator();
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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                loan();
                break;
            default:
                break;

        }
    }

    @Override
    public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
        if (selectedList != null && selectedList.size() > 0) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            for (int i : selectedList) {
            }

        }
    }


    public void loan() {
        BorrowInfoBean bean = new BorrowInfoBean();
        bean.setProduct_id("1");
        bean.setRefund_way("按周还款");
        bean.setBorrow_use_code("525");
        bean.setBorrow_use("其他");
        bean.setBorrow_limit(String.valueOf(money));
        bean.setBorrow_periods(String.valueOf(week));
        if (CommonUtils.checkNull(bean.getBorrow_limit(), "金额不能为空")) return;
        if (CommonUtils.checkNull(bean.getBorrow_periods(), "周期不能为空")) return;
        if (CommonUtils.checkNull(bean.getRefund_way(), "还款方式不能为空")) return;
        UserInfoManager.getInstance().setBorrowInfo(bean);
        Intent intent = new Intent();
        intent.putExtra("cust_id", custId);
        intent.putExtra("phone", phoneNum);
        intent.putExtra(SPKey.ISSHOWAUTHOR, true);
        CommonUtils.goToActivity(getContext(), PersonalDataActivity.class, intent);
        finish();
    }
}
