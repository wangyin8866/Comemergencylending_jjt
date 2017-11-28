package com.two.emergencylending.activity;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.IntentKey;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：立即借款空态页
 * 创建人：szx
 * 创建时间：2016/9/6 10:22
 * 修改人：szx
 * 修改时间：2016/9/6 10:22
 * 修改备注：
 */
public class ImmediatelyLoanActivity extends BaseActivity implements Topbar.topbarClickListener {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.check)
    Button check;
    @Bind(R.id.immediately_loan_reminder)
    TextView reminder;

    @Override
    public int setContent() {
        return R.layout.activity_immediately_loan;
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
        SpannableString styledText = new SpannableString("请耐心等待审核，您也可以到“我的借款”页面查看审核进度");
        styledText.setSpan(new TextAppearanceSpan(this, R.style.text_orange), 14, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        reminder.setText(styledText, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
    }

    @OnClick(R.id.check)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.check:
                Intent intent = new Intent();
                intent.putExtra(IntentKey.FROM, 1);
                CommonUtils.goToActivity(ImmediatelyLoanActivity.this, MineInstantLoanActivity.class, intent);
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

    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }

}
