package com.two.emergencylending.manager;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.IdentityConfig;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.utils.SharedPreferencesUtil;

/**
 * 项目名称：jijietong1.18
 * 类描述：
 * 创建人：szx
 * 创建时间：2017/6/28 10:34
 * 修改人：szx
 * 修改时间：2017/6/28 10:34
 * 修改备注：
 */
public class CustomerManagerManager {

    public static boolean isCustomerManager() {//true为业务员
        String clerk = SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.IS_CLERK);
        if (clerk.equals(IdentityConfig.IDENTITY_CLERK)) {
            return true;
        }
        return false;
    }

}
