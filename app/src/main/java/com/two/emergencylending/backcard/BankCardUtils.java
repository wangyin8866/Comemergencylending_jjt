package com.two.emergencylending.backcard;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.megvii.demo.util.Util;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;

import static android.app.Activity.RESULT_OK;

/**
 * Created by User on 2017/2/16.
 */

public class BankCardUtils {
    private Context context;
    private String uuid;
    private boolean isWarranty = false;
    private static BankCardUtils instance;

    //采用单例模式获取对象
    public static BankCardUtils getInstance() {
        synchronized (BankCardUtils.class) {
            if (instance == null) {
                instance = new BankCardUtils();
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        uuid = Util.getUUIDString(context);
        netWorkWarranty();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                isWarranty = true;
            } else if (msg.what == 2) {
                isWarranty = false;
            }
            Log.d("isWarranty", "isWarranty" + isWarranty);
        }
    };

    /**
     * 联网授权
     */
    private void netWorkWarranty() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(context);
                IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(context);
                manager.registerLicenseManager(idCardLicenseManager);
//                manager.takeLicenseFromNetwork(uuid);
                if (idCardLicenseManager.checkCachedLicense() > 0)
                    mHandler.sendEmptyMessage(1);
                else
                    mHandler.sendEmptyMessage(2);
            }
        }).start();
    }

    /**
     * @param isMode    true:正面；false：反面
     * @param isAllCard true:竖屏；false:横向
     * @return
     */
    public Intent getBankCardIntent(Context context, boolean isMode, boolean isAllCard) {
        if (!isWarranty) {
            netWorkWarranty();
        }
        Intent intent = new Intent(context,
                com.megvii.demo.BankCardScanActivity.class);
        intent.putExtra(Util.KEY_ISDEBUGE, isMode);
        intent.putExtra(Util.KEY_ISALLCARD, isAllCard);
        return intent;
    }

    public static final int INTO_BANKCARDSCAN_PAGE = 200;

    public String onActivityResult(int requestCode, int resultCode, Intent data) {
        String bankNum = "";
        if (requestCode == INTO_BANKCARDSCAN_PAGE && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra("filePath");
            bankNum = data.getStringExtra("bankNum");
            String confidence = data.getStringExtra("confidence");
        }
        return bankNum;
    }
}
