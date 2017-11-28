package com.two.emergencylending.idcard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.getlimtlibrary.builder.utils.MyLog;
import com.megvii.idcardlib.IDCardScanActivity;
import com.megvii.idcardlib.util.Util;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.idcardquality.bean.IDCardAttr;
import com.megvii.licensemanager.Manager;
import com.two.emergencylending.utils.ToastAlone;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by binghezhouke on 15-8-12.
 */
public class IdcardUtils {
    public static final int INTO_IDCARDSCAN_PAGE = 100;
    private Context context;
    private String uuid;
    private boolean isWarranty = false;
    private static IdcardUtils instance;
    private int side;


    //采用单例模式获取对象
    public static IdcardUtils getInstance() {
        synchronized (IdcardUtils.class) {
            if (instance == null) {
                instance = new IdcardUtils();
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        uuid = Util.getUUIDString(context);
        netWorkWarranty();
    }

    /**
     * 联网授权
     */
    private void netWorkWarranty() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(context);
                IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(context);
                if (idCardLicenseManager == null) {
                    mHandler.sendEmptyMessage(3);
                    return;
                }
                manager.registerLicenseManager(idCardLicenseManager);
                manager.takeLicenseFromNetwork(uuid);
                if (idCardLicenseManager.checkCachedLicense() > 0)
                    mHandler.sendEmptyMessage(1);
                else
                    mHandler.sendEmptyMessage(2);
            }
        }).start();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                isWarranty = true;
            } else if (msg.what == 2) {
                isWarranty = false;
            } else if (msg.what == 3) {
                ToastAlone.showLongToast(context, "idCard初始化失败");
            }
            Log.d("isWarranty", "isWarranty" + isWarranty);
        }
    };

    public int getSide() {
        return side;
    }

    /**
     * @param value      0:正面；1：反面
     * @param isVertical true:竖屏；false:横向
     * @return
     */
    public Intent getIdCardIntent(Context context, int value, boolean isVertical) {
        if (!isWarranty) {
            netWorkWarranty();
        }
        this.side = value;
        Intent intent = new Intent(context,
                IDCardScanActivity.class);
        intent.putExtra("side", value);
        intent.putExtra("isvertical", isVertical);
        return intent;
    }

    public Map onActivityResult(int requestCode, int resultCode, Intent data) {
        Map map = new HashMap<>();
        if (requestCode == INTO_IDCARDSCAN_PAGE && resultCode == RESULT_OK) {
            map.put("side", data.getIntExtra("side", 0));
            map.put("idcardImg", data.getByteArrayExtra("idcardImg"));
            if (data.getIntExtra("side", 0) == 0) {
                map.put("portraitImg",
                        data.getByteArrayExtra("portraitImg"));
            }
        }
        return map;
    }

    public Bitmap gitBitmap(Map map) {
        IDCardAttr.IDCardSide mIDCardSide = ((int) map.get("side")) == 0 ? IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT
                : IDCardAttr.IDCardSide.IDCARD_SIDE_BACK;
        Bitmap img = null;
//        if (mIDCardSide == IDCardAttr.IDCardSide.IDCARD_SIDE_BACK) {
        byte[] idcardImgData = (byte[]) map.get("idcardImg");
        img = BitmapFactory.decodeByteArray(idcardImgData, 0,
                idcardImgData.length);
//        }
////        portraitImg
//        if (mIDCardSide == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT) {
//            byte[] portraitImgData = (byte[]) map.get("idcardImg");
//            img = BitmapFactory.decodeByteArray(portraitImgData, 0,
//                    portraitImgData.length);
//        }
        Log.d("Icon_hw", img.getWidth() + "_" + img.getHeight());
        return img;
    }

}