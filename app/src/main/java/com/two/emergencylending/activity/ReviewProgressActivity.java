package com.two.emergencylending.activity;

import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.controller.BorrowStatusCotroller;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：审核进度
 * 创建人：szx
 * 创建时间：2016/9/26 10:11
 * 修改人：szx
 * 修改时间：2016/9/26 10:11
 * 修改备注：
 */
public class ReviewProgressActivity extends BaseActivity implements Topbar.topbarClickListener, ControllerCallBack {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.count_down)
    TextView countDown;
    @Bind(R.id.status)
    TextView status;
    @Bind(R.id.pointer)
    ImageView pointer;
    BorrowStatusCotroller borrowStatusCotroller;
    Mytimer mytimer;
        RotateAnimation animation;
//    Animation scaleAnimation;

    @Override
    public int setContent() {
        return R.layout.activity_review_progress;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        borrowStatusCotroller = new BorrowStatusCotroller(this, this);
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        mytimer = new Mytimer(60000, 1000);
        mytimer.start();
//        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.count_down);
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.85f);
        animation.setDuration(60000);//设置动画持续时间
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
//        // 匀速转动的代码
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        pointer.setAnimation(animation);
        animation.startNow();
        borrowStatusCotroller.getBorrowStatusBack();
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
        if (returnCode == CallBackType.BORROW_STATUS_BACK) {
            if (StringUtil.isNullOrEmpty(value) || value.equals("1") || value.equals("3") || value.equals("5")) {
                status.setText("审批未通过，请修改资料重新申请");
                pointer.clearAnimation();
                mytimer.cancel();
            } else if (value.equals("6")) {
                status.setText("下一步（前去认证）");
                pointer.clearAnimation();
                mytimer.cancel();
                CommonUtils.goToActivity(this, CredentialsActivity.class);
                finish();
            } else if (value.equals("0") || value.equals("2") || value.equals("4") || value.equals("9")) {
                status.setText("自动审批中，请稍候");
            }
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.BORROW_STATUS_BACK) {

        }
    }

    class Mytimer extends CountDownTimer {
        public Mytimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            countDown.setText(millisUntilFinished / 1000 + "s");
            int m = (int) (millisUntilFinished / 1000);
            if ((m > 0 && m < 60) && m % 15 == 0) {
                borrowStatusCotroller.getBorrowStatusBack();
            }
        }

        @Override
        public void onFinish() {
            countDown.setText("0s");
            status.setText("现在申请用户较多，请稍后再来查看");
        }
    }
}
