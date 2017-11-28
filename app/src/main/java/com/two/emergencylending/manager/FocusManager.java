package com.two.emergencylending.manager;

import android.view.View;

/**
 * 项目名称：jijietong1.02
 * 类描述：
 * 创建人：szx
 * 创建时间：2016/9/28 11:47
 * 修改人：szx
 * 修改时间：2016/9/28 11:47
 * 修改备注：
 */
public class FocusManager {

    public static void getFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
}
