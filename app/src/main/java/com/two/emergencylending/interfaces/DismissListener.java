package com.two.emergencylending.interfaces;

import android.widget.PopupWindow;
import android.widget.TextView;

import com.two.emergencylending.manager.BuryPointManager;

/**
 * 项目名称：急借通
 * 类描述：焦点监听事件
 * 创建人：wyp
 * 创建时间：2016/10/18 16:33
 * 修改人：wyp
 * 修改时间：2016/10/18 16:33
 * 修改备注：
 */
public class DismissListener implements PopupWindow.OnDismissListener {
    String mEventId;
    TextView mView;

    public DismissListener(String eventId, TextView view) {
        mEventId = eventId;
        mView = view;
        BuryPointManager.buryBegin(mEventId, view.getText().toString());
    }

    @Override
    public void onDismiss() {
        BuryPointManager.buryEnd(mEventId, mView.getText().toString());

    }
}
