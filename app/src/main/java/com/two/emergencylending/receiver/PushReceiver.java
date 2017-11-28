package com.two.emergencylending.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.two.emergencylending.DataBase.NoticeDao;
import com.two.emergencylending.activity.MainActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.Notice;
import com.two.emergencylending.utils.LogUtil;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * 个推
 */
public class PushReceiver extends BroadcastReceiver {

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */

    public static StringBuilder payloadData = new StringBuilder();
    public NotificationCompat.Builder mBuilder;
    public int notifyId = 100;
    NoticeDao mNoticeDaoDao;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        LogUtil.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
        mNoticeDaoDao = new NoticeDao(context);
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");
                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
//                Logger.d("", "第三方回执接口调用" + (result ? "成功" : "失败"));
                if (payload != null) {
                    String data = new String(payload);
//                    Logger.d("GetuiSdkDemo", "receiver payload : " + data);
                    payloadData.append(data);
                    payloadData.append("\n");
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String type = jsonObject.getString("type");
                        String juid = jsonObject.getString("juid");
                        String msg = jsonObject.getString("msg");
                        Notice notice = new Notice();
                        notice.setType(jsonObject.getString("type"));
                        notice.setUserId(jsonObject.getString("juid"));
                        notice.setHasNotice("1");
                        if (mNoticeDaoDao.isExist(juid, type)) {
                            mNoticeDaoDao.updateNoticeByUserIdAndType(mNoticeDaoDao, notice);
                        } else {
                            mNoticeDaoDao.putNoticeDate(mNoticeDaoDao, notice);
                        }
                        List<Notice> list = mNoticeDaoDao.queryNoices();
                        for (Notice notice1 : list) {
                            LogUtil.i("Notice:", notice1.toString());
                        }
                        initNotice(context);
//                        ToastUtils.showLong(IApplication.globleContext, "type:" + type + "===juid:" + juid + "===内容:" + msg);
                        send(context, context.getResources().getString(R.string.app_all_name), msg);
                    } catch (JSONException e) {
//                        ToastUtils.showLong(IApplication.globleContext, "推送异常:" + e.getMessage());
                        e.printStackTrace();
                    }

//                    PushBean push = new PushBean();
//                    push = new Gson().fromJson(data, PushBean.class);

//                    mDao.putMessageDate(mDao, push.getPhone(), push.getType(), DBCode.UNREAD, push.getTitle(), push.getContent(), TimeUtils.getCurrentTime());
//                    List<MessageCenterBean> messageCenterBeans = new ArrayList<MessageCenterBean>();
//                    Boolean isExist = false;
//                    if (!SharedPreferencesUtil.getInstance(context).getString(SPKey.MESSAGE_CENTER, "").equals("")) {
//                        messageCenterBeans = new Gson().fromJson(SharedPreferencesUtil.getInstance(context).getString(SPKey.MESSAGE_CENTER, ""), new TypeToken<ArrayList<MessageCenterBean>>() {
//                        }.getType());
//                        for (MessageCenterBean message : messageCenterBeans) {
//                            if (message.getUserName() != null && (message.getUserName().equals(SharedPreferencesUtil.getInstance(context).getString(SPKey.PHONE)) || message.getUserName().equals("noname"))) {
//                                message.setHasNewMessage(true);
//                                isExist = true;
//                            }
//                        }
//                    }
//                    if (!isExist) {
//                        MessageCenterBean message = new MessageCenterBean();
//                        message.setUserName(SharedPreferencesUtil.getInstance(context).getString(SPKey.PHONE));
//                        message.setHasNewMessage(true);
//                        messageCenterBeans.add(message);
//                    }
//                    SharedPreferencesUtil.getInstance(context).setString(SPKey.MESSAGE_CENTER, new Gson().toJson(messageCenterBeans).toString());

                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
//                Logger.d("cid", "" + cid);
//                if (GetuiSdkDemoActivity.tView != null) {
//                    GetuiSdkDemoActivity.tView.setText(cid);
//                }
                break;

            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 * 
                 * Logger.d("GetuiSdkDemo", "appid = " + appid); Logger.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Logger.d("GetuiSdkDemo", "actionid = " + actionid); Logger.d("GetuiSdkDemo",
                 * "result = " + result); Logger.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;

            default:
                break;
        }
    }

    public void send(Context context, String title, String content) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setSmallIcon(R.drawable.icon)
                .setTicker(context.getResources().getString(R.string.app_all_name) + "消息通知");//通知首次出现在通知栏，带上升动画效果的
        Notification mNotification = mBuilder.build();
        //在通知栏上点击此通知后自动清除此通知
        //FLAG_ONGOING_EVENT 在顶部常驻，可以调用下面的清除方法去除  FLAG_AUTO_CANCEL  点击和清理可以去调
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotification.icon = R.drawable.icon;
        //设置显示通知时的默认的发声、震动、Light效果
        mNotification.defaults = Notification.DEFAULT_VIBRATE;
        // 默认系统声音
        mNotification.defaults = Notification.DEFAULT_SOUND;
        mNotificationManager.cancel(notifyId);
        mNotificationManager.notify(notifyId, mNotification);
    }

    public void initNotice(Context context) {
        mBuilder = new NotificationCompat.Builder(context);
//        if (SharedPreferencesUtil.getInstance(context).getBoolean("isLogin", false)) {
        mBuilder.setContentIntent(getDefalutIntent(context, Notification.FLAG_AUTO_CANCEL))
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.drawable.icon);
    }

    public PendingIntent getDefalutIntent(Context context, int flags) {
        Intent appIntent = new Intent(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        if (IApplication.currClass == null) {
            IApplication.currClass = MainActivity.class;
        }
        appIntent.setComponent(new ComponentName(IApplication.globleContext, IApplication.currClass));
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
