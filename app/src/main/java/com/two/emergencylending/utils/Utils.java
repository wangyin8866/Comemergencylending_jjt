package com.two.emergencylending.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cs on 2016/8/14.
 */
public class Utils {
    public static int dp2px(Context context,float dp)
    {
        return (int ) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE );
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics .widthPixels ;
    }

    /**
     * 计算时间
     */
    public static String dayCount(int day){
        Date dt= new Date();
        Long time= dt.getTime();
        Long time2=(long) day*24*60*60*1000;
        Long time3=time+time2;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(time3);
        return dateFormat.format(date);
    }
}
