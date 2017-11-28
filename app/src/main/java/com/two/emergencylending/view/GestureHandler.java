package com.two.emergencylending.view;

import android.graphics.Color;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.two.emergencylending.constant.AppConfig;
import com.zyjr.emergencylending.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2016/2/17.
 */
public class GestureHandler {

    /**
     * 记录输入的次数
     */
    private static int num;
    /**
     * 保存skey（上一次输入）
     */
    private static String skey;
    /**
     * 保存原始密码验证结果
     */
    private static boolean flag = false;


    /**
     * 判断手势密码个数
     *
     * @param gestureLockView
     * @param key
     * @return
     */
    public static boolean islimitNum(GestureLockView gestureLockView, String key) {
        if (key.length() < gestureLockView.limitNum) {
            Toast.makeText(gestureLockView.getContext(), "手势密码至少为4位！", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     * 判断手势密码个数
     *
     * @param lock9View
     * @param key
     * @return
     */
    public static boolean islimitNum(Lock9View lock9View, String key) {
        if (key.length() < lock9View.limitNum) {
            Toast.makeText(lock9View.getContext(), "手势密码至少为4位！", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static void init() {
        flag = false;
        setNumOf0();
        skey = "";
    }


    /**
     * 手势登录处理
     *
     * @param gestureLockView 手势
     * @param sg              预览
     * @param tv_gesture      提示
     * @param key             用户输入key
     * @return
     */
    public static boolean loginGestureLock(GestureLockView gestureLockView, View sg, TextView tv_gesture, String key) {
        skey = key;
        if (LockPassWordUtil.getPassword(gestureLockView.getContext()).equals(key)) {
            return true;
        } else {
            num++;
            if (num >= AppConfig.limitloginPwdNum || num == 5) {
                if (AppConfig.DEBUG) {
                    Toast.makeText(gestureLockView.getContext(), "错误次数超过" + AppConfig.limitloginPwdNum + "次，请重新登录", Toast.LENGTH_SHORT).show();
                }
                LockPassWordUtil.clearPassWork(gestureLockView.getContext());
                return false;
            }

            tv_gesture.setTextColor(Color.parseColor("#FF2525"));
            tv_gesture.setVisibility(View.VISIBLE);
            tv_gesture.setText("密码错误还可以输入" + (AppConfig.limitloginPwdNum - num) + "次");
//            tv_gesture.startAnimation(gestureLockView.getAnimation());
        }
        return false;
    }

    /**
     * 手势登录处理
     *
     * @param lock9View  手势
     * @param sg         预览
     * @param tv_gesture 提示
     * @param key        用户输入key
     * @return
     */
    public static boolean loginGestureLock(Lock9View lock9View, View sg, TextView tv_gesture, String key) {
        skey = key;
        if (LockPassWordUtil.getPassword(lock9View.getContext()).equals(key)) {
            return true;
        } else {
            num++;
            if (num >= AppConfig.limitloginPwdNum || num == 5) {
                if (AppConfig.DEBUG) {
                    Toast.makeText(lock9View.getContext(), "错误次数超过" + AppConfig.limitloginPwdNum + "次，请重新登录", Toast.LENGTH_SHORT).show();
                }
                LockPassWordUtil.clearPassWork(lock9View.getContext());
                return false;
            }

            tv_gesture.setTextColor(Color.parseColor("#FF2525"));
            tv_gesture.setVisibility(View.VISIBLE);
            tv_gesture.setText("密码错误还可以输入" + (AppConfig.limitloginPwdNum - num) + "次");
            tv_gesture.startAnimation(lock9View.getAnimation());
        }
        return false;
    }

    /**
     * 获取手势输入次数
     *
     * @return
     */
    public static int getNum() {
        return num;
    }

    /**
     * 置零
     */
    public static void setNumOf0() {
        GestureHandler.num = 0;
    }

    public static boolean getFlag() {
        return flag;
    }

    /**
     * 修改手势处理
     *
     * @param gestureLockView 手势
     * @param sg              预览
     * @param tv_gesture      提示
     * @param key             用户输入key
     * @return
     */
    public static boolean modifyGestureLock(GestureLockView gestureLockView, View sg, TextView tv_gesture, String key) {
        if (!flag) {
            return false;
        }
        if (LockPassWordUtil.getPassword(gestureLockView.getContext()).equals(key)) {
            tv_gesture.setText("您设置的手势与原来的一致！");
            tv_gesture.setTextColor(Color.RED);
            tv_gesture.setVisibility(View.VISIBLE);
            tv_gesture.startAnimation(gestureLockView.getAnimation());
            num = 0;
            return false;
        }
        num++;
        if (skey.equals(key)) {
            LockPassWordUtil.savePassWord(gestureLockView.getContext(), key, true);
            flag = false;
            return true;
        } else {
            if (num < AppConfig.limitSetPwdNum) {
                gestureLockView.setKey(key);
                skey = key;
                tv_gesture.setTextColor(Color.parseColor("#409DE5"));
                tv_gesture.setText("请再次绘制解锁图案");
                GestureHandler.setImage(sg, key);
            } else {
                gestureLockView.setKey("");
                skey = "";
                tv_gesture.setTextColor(Color.RED);
                tv_gesture.setText("请重新输入解锁图案");
                GestureHandler.setImage(sg, "");
                num = 0;
            }
            tv_gesture.setVisibility(View.VISIBLE);
            tv_gesture.startAnimation(gestureLockView.getAnimation());
        }
        return false;
    }

    /**
     * 修改手势处理
     *
     * @param gestureLockView 手势
     * @param sg              预览
     * @param tv_gesture      提示
     * @param key             用户输入key
     * @return
     */
    public static boolean modifyGestureLock(Lock9View gestureLockView, View sg, TextView tv_gesture, String key) {
        if (!flag) {
            return false;
        }
        if (LockPassWordUtil.getPassword(gestureLockView.getContext()).equals(key)) {
            tv_gesture.setText("您设置的手势与原来的一致！");
            tv_gesture.setTextColor(Color.RED);
            tv_gesture.setVisibility(View.VISIBLE);
            tv_gesture.startAnimation(gestureLockView.getAnimation());
            num = 0;
            return false;
        }
        num++;
        if (skey.equals(key)) {
            LockPassWordUtil.savePassWord(gestureLockView.getContext(), key, true);
            flag = false;
            return true;
        } else {
            if (num < AppConfig.limitSetPwdNum) {
                gestureLockView.setKey(key);
                skey = key;
                tv_gesture.setTextColor(Color.parseColor("#409DE5"));
                tv_gesture.setText("请再次绘制解锁图案");
                GestureHandler.setImage(sg, key);
            } else {
                gestureLockView.setKey("");
                skey = "";
                tv_gesture.setTextColor(Color.RED);
                tv_gesture.setText("请重新输入解锁图案");
                GestureHandler.setImage(sg, "");
                num = 0;
            }
            tv_gesture.setVisibility(View.VISIBLE);
            tv_gesture.startAnimation(gestureLockView.getAnimation());
        }
        return false;
    }

    /**
     * 修改手势验证原密码
     *
     * @param key
     * @return true:一致 ；false ：不一致
     */
    public static boolean modifyChvalidate(GestureLockView gestureLockView, TextView tv_gesture, String key) {
        skey = key;
        if (LockPassWordUtil.getPassword(gestureLockView.getContext()).equals(key)) {
            tv_gesture.setTextColor(Color.parseColor("#409DE5"));
            tv_gesture.setText("绘制解锁图案");
            for (ImageView i : views) {
                i.setVisibility(View.VISIBLE);
            }
            gestureLockView.setKey("");

            new Thread() {
                @Override
                public void run() {
                    SystemClock.sleep(500);
                    flag = true;
                }
            }.start();
            return true;
        } else {
//            tv_gesture.setText("请重新输入原手势密码");
//            tv_gesture.setTextColor(Color.RED);
//            tv_gesture.setVisibility(View.VISIBLE);
//            tv_gesture.startAnimation(gestureLockView.getAnimation());
            num++;
            if (num >= AppConfig.limitloginPwdNum || num == 5) {
                if (AppConfig.DEBUG) {
                    Toast.makeText(gestureLockView.getContext(), "错误次数超过" + AppConfig.limitloginPwdNum + "次，请重新登录", Toast.LENGTH_SHORT).show();
                }
                LockPassWordUtil.clearPassWork(gestureLockView.getContext());
                return false;
            }

            tv_gesture.setTextColor(Color.parseColor("#FF2525"));
            tv_gesture.setVisibility(View.VISIBLE);
            tv_gesture.setText("密码错误还可以输入" + (AppConfig.limitloginPwdNum - num) + "次");
            tv_gesture.startAnimation(gestureLockView.getAnimation());
        }
        return false;
    }

    /**
     * 修改手势验证原密码
     *
     * @param key
     * @return true:一致 ；false ：不一致
     */
    public static boolean modifyChvalidate(Lock9View lock9View, TextView tv_gesture, String key) {
        skey = key;
        if (LockPassWordUtil.getPassword(lock9View.getContext()).equals(key)) {
            tv_gesture.setTextColor(Color.parseColor("#409DE5"));
            tv_gesture.setText("绘制解锁图案");
            for (ImageView i : views) {
                i.setVisibility(View.VISIBLE);
            }
            lock9View.setKey("");

            new Thread() {
                @Override
                public void run() {
                    SystemClock.sleep(500);
                    flag = true;
                }
            }.start();
            return true;
        } else {
//            tv_gesture.setText("请重新输入原手势密码");
//            tv_gesture.setTextColor(Color.RED);
//            tv_gesture.setVisibility(View.VISIBLE);
//            tv_gesture.startAnimation(lock9View.getAnimation());

            num++;
            if (num >= AppConfig.limitloginPwdNum || num == 5) {
                if (AppConfig.DEBUG) {
                    Toast.makeText(lock9View.getContext(), "错误次数超过" + AppConfig.limitloginPwdNum + "次，请重新登录", Toast.LENGTH_SHORT).show();
                }
                LockPassWordUtil.clearPassWork(lock9View.getContext());
                return false;
            }

            tv_gesture.setTextColor(Color.parseColor("#FF2525"));
            tv_gesture.setVisibility(View.VISIBLE);
            tv_gesture.setText("密码错误还可以输入" + (AppConfig.limitloginPwdNum - num) + "次");
            tv_gesture.startAnimation(lock9View.getAnimation());
        }
        return false;
    }

    /**
     * 设置手势处理
     *
     * @param gestureLockView 手势
     * @param sg              预览
     * @param tv_gesture      提示
     * @param key             用户输入key
     * @return
     */
    public static boolean setGestureLock(GestureLockView gestureLockView, View sg, TextView tv_gesture, String key) {
//        Toast.makeText(gestureLockView.getContext(), key, Toast.LENGTH_LONG).show();
        num++;
        if (skey.equals(key)) {
            num = 0;
            LockPassWordUtil.savePassWord(gestureLockView.getContext(), key, true);
//            Logger.d("setGestureLock", "" + LockPassWordUtil.getPassword(gestureLockView.getContext
//                    ()) + LockPassWordUtil.getPasswordStatus(gestureLockView.getContext()));
            return true;
        } else {
            if (num < AppConfig.limitSetPwdNum) {
                gestureLockView.setKey(key);
                skey = key;
                tv_gesture.setText("请再次输入解锁图案");
                GestureHandler.setImage(sg, key);
            } else {
                gestureLockView.setKey("");
                skey = "";
                tv_gesture.setText("请重新输入解锁图案");
                GestureHandler.setImage(sg, "");
                num = 0;
            }
            tv_gesture.setVisibility(View.VISIBLE);
        }
        return false;
    }


    /**
     * 设置手势处理
     *
     * @param lock9View  手势
     * @param sg         预览
     * @param tv_gesture 提示
     * @param key        用户输入key
     * @return
     */
    public static boolean setGestureLock(Lock9View lock9View, View sg, TextView tv_gesture, String key) {
//        Toast.makeText(lock9View.getContext(), key, Toast.LENGTH_LONG).show();
        num++;
        if (skey.equals(key)) {
            num = 0;
            LockPassWordUtil.savePassWord(lock9View.getContext(), key, true);
//            Logger.d("setGestureLock", "" + LockPassWordUtil.getPassword(lock9View.getContext
//                    ()) + LockPassWordUtil.getPasswordStatus(lock9View.getContext()));
            return true;
        } else {
            if (num < AppConfig.limitSetPwdNum) {
                lock9View.setKey(key);
                skey = key;
                tv_gesture.setText("请再次输入解锁图案");
                GestureHandler.setImage(sg, key);
            } else {
                lock9View.setKey("");
                skey = "";
                tv_gesture.setText("请重新输入解锁图案");
                GestureHandler.setImage(sg, "");
                num = 0;
            }
            tv_gesture.setVisibility(View.VISIBLE);
        }
        return false;
    }


    /**
     * 手势预览集合
     */
    private static List<ImageView> views = new ArrayList<ImageView>();

    /**
     * 手势预览FindID
     *
     * @param view
     */
    private static void init(View view) {
        if (views != null) {
            views.clear();
            views.add((ImageView) view.findViewById(R.id.iv_1));
            views.add((ImageView) view.findViewById(R.id.iv_2));
            views.add((ImageView) view.findViewById(R.id.iv_3));
            views.add((ImageView) view.findViewById(R.id.iv_4));
            views.add((ImageView) view.findViewById(R.id.iv_5));
            views.add((ImageView) view.findViewById(R.id.iv_6));
            views.add((ImageView) view.findViewById(R.id.iv_7));
            views.add((ImageView) view.findViewById(R.id.iv_8));
            views.add((ImageView) view.findViewById(R.id.iv_9));
        }
    }

    /**
     * 手势预览隐藏
     *
     * @param sg
     */
    public static void setIsGone(View sg) {
        if (sg == null)
            return;
        init(sg);
        for (ImageView i : views) {
            i.setVisibility(View.GONE);
        }
    }

    /**
     * 显示手势预览信息
     *
     * @param sg
     * @param image
     */
    public static void setImage(View sg, String image) {
        if (sg == null)
            return;
        init(sg);
        //设置提示为空
        for (int j = 0; j < views.size(); j++) {
            views.get(j).setBackgroundResource(R.drawable.node_normal);
            views.get(j).setVisibility(View.VISIBLE);
        }
        //设置提示信息
        if (!"".equals(image)) {
            int temp = Integer.parseInt(image);
            if (image.startsWith("0")) {
                views.get(0).setBackgroundResource(R.drawable.node_small_normal);
            }
            while (temp > 0) {
                views.get(temp % 10).setBackgroundResource(R.drawable.node_small_normal);
                temp = temp / 10;
            }
        }
    }
}
