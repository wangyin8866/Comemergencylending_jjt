package com.two.emergencylending.utils;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.zyjr.emergencylending.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 项目名称：MySeekBar
 * 类描述：
 * 创建人：szx
 * 创建时间：2017/1/11 16:20
 * 修改人：szx
 * 修改时间：2017/1/11 16:20
 * 修改备注：
 */
public class CarouselUtil {
    public static CarouselUtil instance;
    TimerTask task;
    Timer timer;
    int carouselCount = 20;
    String[] familyNames = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "卫", "沈", "杨", "朱", "尤", "何", "吕", "张"};
    String[] sexs = {"先生", "女士"};
    int[] types = {0, 1};
    View.OnClickListener mOnClickListener;

    public static CarouselUtil getInstance() {
        if (instance == null) {
            instance = new CarouselUtil();
        }
        return instance;
    }

    private LinearLayout notice_parent_ll;
    private LinearLayout notice_ll;
    ArrayList<String> titleList = new ArrayList<String>();
    private ViewFlipper notice_vf;
    private int mCurrPos;


    public void clearNotice() {
        if (titleList.size() > 0) {
            titleList.clear();
        }
    }

    public void addNotice(String notice) {
        titleList.add(notice);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void initRollNotice(final Activity activity, View root) {
        initTitle();
        FrameLayout main_notice = (FrameLayout) root.findViewById(R.id.home_notice);
        notice_parent_ll = (LinearLayout) activity.getLayoutInflater().inflate(
                R.layout.layout_notice, null);
        notice_ll = ((LinearLayout) this.notice_parent_ll
                .findViewById(R.id.homepage_notice_ll));
        notice_vf = ((ViewFlipper) this.notice_parent_ll
                .findViewById(R.id.homepage_notice_vf));
        main_notice.addView(notice_parent_ll);
        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            moveNext(activity);
                        }
                    });
                }
            };
        }
        if (timer == null) {
            timer = new Timer();
            timer.schedule(task, 0, 4000);
        }

    }

    private void moveNext(Activity activity) {
        setView(activity, this.mCurrPos, this.mCurrPos + 1);
        this.notice_vf.setInAnimation(activity, R.anim.in_bottomtop);
        this.notice_vf.setOutAnimation(activity, R.anim.out_bottomtop);
        this.notice_vf.showNext();
    }

    private void setView(Activity activity, int curr, int next) {
        View noticeView = activity.getLayoutInflater().inflate(R.layout.notice_item,
                null);
        RelativeLayout rl_invite = (RelativeLayout) noticeView.findViewById(R.id.rl_invite);
        TextView notice_tv = (TextView) noticeView.findViewById(R.id.notice_tv);
        TextView invite = (TextView) noticeView.findViewById(R.id.invite);
        if ((curr < next) && (next > (titleList.size() - 1))) {
            next = 0;
        } else if ((curr > next) && (next < 0)) {
            next = titleList.size() - 1;
        }
        String title = titleList.get(next);
        notice_tv.setText(title);
        noticeView.setOnClickListener(mOnClickListener);
        if (title.contains("邀请")) {
            rl_invite.setClickable(true);
//            notice_tv.setTextColor(IApplication.globleResource.getColor(R.color.title));
            invite.setVisibility(View.VISIBLE);
        } else {
            rl_invite.setClickable(false);
//            notice_tv.setTextColor(IApplication.globleResource.getColor(R.color.seekbar));
            invite.setVisibility(View.INVISIBLE);
        }
        if (notice_vf.getChildCount() > 1) {
            notice_vf.removeViewAt(0);
        }
        notice_vf.addView(noticeView, notice_vf.getChildCount());
        mCurrPos = next;
    }

    public void initTitle() {
        titleList.clear();
        for (int i = 0; i < carouselCount; i++) {
            addNotice(initData());
        }
    }


    public String initData() {
        String CarouselFamilyName = familyNames[CommonUtils.getOneRandom(familyNames.length)];
        String CarouselSex = sexs[CommonUtils.getOneRandom(sexs.length)];
        int CarouselType = types[CommonUtils.getOneRandom(types.length)];
        if (CarouselType == 0) {
            int random = CommonUtils.getOneRandom(28);
            int money = 2000 + random * 1000;
            return "喜讯  " + CarouselFamilyName + CarouselSex + "  成功借款" + money + "元";
        } else {
            String inviteFamilyName = familyNames[CommonUtils.getOneRandom(familyNames.length)];
            String inviteSex = sexs[CommonUtils.getOneRandom(sexs.length)];
            return CarouselFamilyName + CarouselSex + "  成功邀请  " + inviteFamilyName + inviteSex;
        }
    }

}
