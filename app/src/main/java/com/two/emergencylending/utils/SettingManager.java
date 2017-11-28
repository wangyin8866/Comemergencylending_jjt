package com.two.emergencylending.utils;

/**
 * Created by User on 2016/2/1.
 */
public class SettingManager {
    public static SettingManager s_Instance;

    private int mSystemStatusAreaHeight = 0;
    private int mSystemBottomAreaheight = 0;
    private int mScreenWidth = 0;
    private int mScreenHeight = 0;
    private float density;


    public static SettingManager getInstance() {
        if (s_Instance == null) {
            s_Instance = new SettingManager();
        }
        return s_Instance;
    }

    public static SettingManager getS_Instance() {
        return s_Instance;
    }


    public int getSystemStatusAreaHeight() {
        return mSystemStatusAreaHeight;
    }

    public void setSystemStatusAreaHeight(int mSystemStatusAreaHeight) {
        this.mSystemStatusAreaHeight = mSystemStatusAreaHeight;
    }

    public int getSystemBottomAreaheight() {
        return mSystemBottomAreaheight;
    }

    public void setSystemBottomAreaheight(int mSystemBottomAreaheight) {
        this.mSystemBottomAreaheight = mSystemBottomAreaheight;
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }

    public void setScreenWidth(int mScreenWidth) {
        this.mScreenWidth = mScreenWidth;
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public void setScreenHeight(int mScreenHeight) {
        this.mScreenHeight = mScreenHeight;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }
}
