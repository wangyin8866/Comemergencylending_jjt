package com.two.emergencylending.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.two.emergencylending.application.IApplication;
import com.zyjr.emergencylending.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：jijietong1.18
 * 类描述：
 * 创建人：szx
 * 创建时间：2017/6/19 14:42
 * 修改人：szx
 * 修改时间：2017/6/19 14:42
 * 修改备注：
 */
public class PopWinStatus extends PopupWindow {
    private View mainView;
    private TextView status0, status1, status2, status3, status4, status5, status6, status7, status8, status9, status10, status11;
    private List<View> views = new ArrayList<View>();

    public PopWinStatus(Activity paramActivity, View.OnClickListener paramOnClickListener, int viewId) {
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.popup_status, null);
        views.clear();
        status0 = ((TextView) mainView.findViewById(R.id.status0));
        status1 = ((TextView) mainView.findViewById(R.id.status1));
        status2 = (TextView) mainView.findViewById(R.id.status2);
        status3 = ((TextView) mainView.findViewById(R.id.status3));
        status4 = (TextView) mainView.findViewById(R.id.status4);
        status5 = ((TextView) mainView.findViewById(R.id.status5));
        status6 = (TextView) mainView.findViewById(R.id.status6);
        status7 = ((TextView) mainView.findViewById(R.id.status7));
        status8 = (TextView) mainView.findViewById(R.id.status8);
        status9 = ((TextView) mainView.findViewById(R.id.status9));
        status10 = (TextView) mainView.findViewById(R.id.status10);
        status11 = (TextView) mainView.findViewById(R.id.status11);
        views.add(status0);
        views.add(status1);
        views.add(status2);
        views.add(status3);
        views.add(status11);
        views.add(status4);
        views.add(status5);
        views.add(status6);
        views.add(status7);
        views.add(status8);
        views.add(status9);
        views.add(status10);
        if (containView(viewId)) {
            selectItem(viewId, false);
        }
        //设置每个子布局的事件监听器
        if (paramOnClickListener != null) {
            status0.setOnClickListener(paramOnClickListener);
            status1.setOnClickListener(paramOnClickListener);
            status2.setOnClickListener(paramOnClickListener);
            status3.setOnClickListener(paramOnClickListener);
            status4.setOnClickListener(paramOnClickListener);
            status5.setOnClickListener(paramOnClickListener);
            status6.setOnClickListener(paramOnClickListener);
            status7.setOnClickListener(paramOnClickListener);
            status8.setOnClickListener(paramOnClickListener);
            status9.setOnClickListener(paramOnClickListener);
            status10.setOnClickListener(paramOnClickListener);
        }
        setContentView(mainView);
        //设置宽度
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置高度
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置显示隐藏动画
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }

    public void selectItem(int viewId) {
        selectItem(viewId, true);
    }

    public void selectItem(int viewId, boolean close) {
        for (View v : views) {
            if (v.getId() == viewId) {
                v.setBackgroundColor(IApplication.globleResource.getColor(R.color.orange));
            } else {
                v.setBackgroundColor(IApplication.globleResource.getColor(R.color.transparent));
            }
        }
        this.update();
        if (close) {
            this.dismiss();
        }
    }

    public boolean containView(int viewId) {
        for (View v : views) {
            if (v.getId() == viewId) {
                return true;
            }
        }
        return false;
    }

}
