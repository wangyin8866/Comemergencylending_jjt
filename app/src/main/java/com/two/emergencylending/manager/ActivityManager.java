package com.two.emergencylending.manager;

import android.annotation.SuppressLint;
import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：Hanfen
 * 类描述：
 * 创建人：szx
 * 创建时间：2016/7/7 17:19
 * 修改人：szx
 * 修改时间：2016/7/7 17:19
 * 修改备注：
 */
public class ActivityManager {
    private static ActivityManager instance;
    private static Map<String, List<Activity>> activityMap;

    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
            activityMap = new HashMap<String, List<Activity>>();
        }
        return instance;
    }

    public int getActivityMapSize() {
        return activityMap.size();
    }

    public void inputActivity(String key, Activity activity) {
        if (activityMap.containsKey(key)) {
            List<Activity> list = activityMap.get(key);
            if (!list.contains(activity)) {
                list.add(activity);
            }
        } else {
            List<Activity> list = new ArrayList<Activity>();
            list.add(activity);
            activityMap.put(key, list);
        }
    }

    public void removeActivity(String key, Activity activity) {
        if (activityMap.containsKey(key)) {
            List<Activity> list = activityMap.get(key);
            if (list.contains(activity)) {
                list.remove(activity);
            }
        }
    }

    public void removeAllActivitys(String key) {
        if (activityMap.containsKey(key)) {
            List<Activity> list = activityMap.get(key);
            for (Activity activity : list) {
                list.remove(activity);
            }
        }
    }
    public void finishAllActivitys(){
        for (Map.Entry<String, List<Activity>> v:activityMap.entrySet()) {
            List<Activity> list = v.getValue();
            for (Activity activity : list) {
                list.remove(activity);
            }
        }
    }
    

    public void finishActivity(String key, Activity activity) {
        if (activityMap.containsKey(key)) {
            List<Activity> list = activityMap.get(key);
            if (list.contains(activity)) {
                list.remove(activity);
                activity.finish();
            }
        }
    }

    @SuppressLint("NewApi")
    public void finishAllActivitys(String key) {
        if (activityMap.containsKey(key)) {
            List<Activity> list = activityMap.get(key);
            for (Activity activity : list) {
                activity.finish();
            }
            activityMap.get(key).clear();
        }
    }

    public void clearActivitys(String key) {
        if (activityMap.containsKey(key)) {
            List<Activity> list = activityMap.get(key);
            list.clear();
        }
    }

    public int getActivitysSize(String key) {
        if (getActivityMapSize() > 0) {
            List<Activity> list = activityMap.get(key);
            return list.size();
        }
        return 0;
    }

    public List<Activity> getActivitys(String key) {
        List<Activity> list;
        if (getActivityMapSize() > 0) {
            list = activityMap.get(key);
        } else {
            list = new ArrayList<>();
        }
        return list;
    }
    
}
