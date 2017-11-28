package com.two.emergencylending.manager;

import android.content.Context;

import com.two.emergencylending.DataBase.MessageManagerDao;
import com.two.emergencylending.constant.DBCode;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：急借通
 * 类描述：消息中心管理
 * 创建人：szx
 * 创建时间：2016/8/24 11:48
 * 修改人：szx
 * 修改时间：2016/8/24 11:48
 * 修改备注：
 */
public class MessageCenterManager {

    public static int getUnreadMessageCount(Context context) {
        int count = 0;
        MessageManagerDao dao = new MessageManagerDao(context);
        List<HashMap<String, Object>> messageList = dao.queryMessageInfo();
        for (HashMap<String, Object> msg : messageList) {
            if (msg.get(DBCode.KEY_PHONE).equals(SharedPreferencesUtil.getInstance(context).getString(SPKey.USERNAME)) && msg.get(DBCode.KEY_READ).equals(DBCode.UNREAD)) {
                count++;
            }
        }
        return count;
    }
}
