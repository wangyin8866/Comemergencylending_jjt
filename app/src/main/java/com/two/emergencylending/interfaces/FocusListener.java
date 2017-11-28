package com.two.emergencylending.interfaces;

import android.view.View;
import android.widget.EditText;

import com.two.emergencylending.manager.BuryPointManager;

/**
 * 项目名称：急借通
 * 类描述：焦点监听事件
 * 创建人：szx
 * 创建时间：2016/9/28 11:33
 * 修改人：szx
 * 修改时间：2016/9/28 11:33
 * 修改备注：
 */
public class FocusListener implements View.OnFocusChangeListener {
    String mEventId;

    public FocusListener(String eventId) {
        mEventId = eventId;
    }

    @Override
    public  void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            BuryPointManager.buryBegin(mEventId, ((EditText) view).getText().toString());
        } else {
            BuryPointManager.buryEnd(mEventId, ((EditText) view).getText().toString());
        }
    }


}
