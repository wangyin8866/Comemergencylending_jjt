package com.two.emergencylending.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.two.emergencylending.utils.SharedPreferencesUtil;

/**
 * Created by User on 2016/8/8.
 */
public class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {

    static final String SYSTEM_REASON = "reason";
    static final String SYSTEM_HOME_KEY = "homekey";// home key
    static final String SYSTEM_RECENT_APPS = "recentapps";// long home key
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_REASON);
            if (reason != null) {
                if (reason.equals(SYSTEM_HOME_KEY)) {
                    SharedPreferencesUtil.getInstance(context).setBoolean("isHome", true);
                    // home key处理点
                } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                    // long home key处理点
					SharedPreferencesUtil.getInstance(context).setBoolean("isHome", false);
                }
            }
        }
    }
}
