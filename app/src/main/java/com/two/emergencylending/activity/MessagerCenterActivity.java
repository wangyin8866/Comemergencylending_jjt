package com.two.emergencylending.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;

import com.chinazyjr.lib.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.refresh.PullToRefreshBase;
import com.refresh.PullToRefreshListView;
import com.two.emergencylending.DataBase.MessageManagerDao;
import com.two.emergencylending.adapter.CenterMessageAdapter;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.CenterMessage;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.DBCode;
import com.two.emergencylending.constant.IntentKey;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.MessageCotroller;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.DateUtil;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：消息中心页面
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class MessagerCenterActivity extends BaseActivity implements Topbar.topbarClickListener, CenterMessageAdapter.IonSlidingViewClickListener, ControllerCallBack {
    private String TAG = MessagerCenterActivity.class.getSimpleName();
    @Bind(R.id.ll_no_message)
    View noMessage;
    @Bind(R.id.center_message_topbar)
    Topbar topbar;
    private List<CenterMessage> messageList;
    private CenterMessageAdapter adapter;
    MessageManagerDao dao;
    CustomerDialog dialog;
    MessageCotroller messageCotroller;
    PullToRefreshListView mPullRefreshListView;
    private static int currentPage = 1;
    private int messageCountTotal = 0;
    private boolean isLogin;
    private CenterMessage prepareDeleteMessage;
    //消息中心请求码
    private final int REQUEST_MESSAGE = 102;


    @Override
    public int setContent() {
        return R.layout.activity_messager_center;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        if (IApplication.getInstance().isLogin()) {
            isLogin = false;
        } else {
            isLogin = true;
        }
        currentPage = 1;
        messageCotroller = new MessageCotroller(this, this);
        dao = new MessageManagerDao(this);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        topbar.getRightButton().setVisibility(View.VISIBLE);
        topbar.getRightButton().setText("全部清除");
        topbar.getRightButton().setTextColor(IApplication.globleResource.getColor(R.color.register_hint));
        messageList = new ArrayList<CenterMessage>();
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.prlv_message);
        adapter = new CenterMessageAdapter(MessagerCenterActivity.this, messageList);
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        if (isLogin) {
            currentPage = 1;
            initData(currentPage);
        } else {
            showNullPage();
        }
    }

    public void initData(int page) {
        messageCotroller.getMessage(String.valueOf(page), "20");
    }

    @Override
    public void setData() {
    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MessagerCenterActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    LogUtil.d("mPullRefreshListView", "PULL_FROM_START");
                    if (isLogin) {
                        initData(1);
                    } else {
                        mPullRefreshListView.onRefreshComplete();
                    }
                } else if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    LogUtil.d("mPullRefreshListView", "PULL_FROM_END");
                    if (isLogin) {
                        initData(currentPage);
                    } else {
                        mPullRefreshListView.onRefreshComplete();
                    }
                }
            }
        });
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {
        if (messageList.size() > 0) {
            dialog = new CustomerDialog(MessagerCenterActivity.this);
            dialog.messageDialog(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Message msg = new Message();
                    msg.what = 6;
                    handler.sendMessage(msg);
                    dialog.dismiss();
                }
            }, "是否清空所有消息列表？", "确定");
            dialog.show();
        } else {
            ToastUtils.showShort(MessagerCenterActivity.this, "您目前没有消息需要清空");
        }
    }

    @Override
    public void onItemClick(View view, CenterMessage message) {
        Intent intent = new Intent();
        if (message.getStatus().equals(DBCode.UNREAD)) {
            prepareDeleteMessage = message;
            Message msg = new Message();
            msg.what = 4;
            msg.obj = message.getId();
            handler.sendMessage(msg);
        }
        intent.putExtra("data", message);
        CommonUtils.goToActivityForResult(MessagerCenterActivity.this, MessagerDetailActivity.class, intent);
    }

    @Override
    public void onDeleteBtnCilck(View view, final CenterMessage message) {
        dialog = new CustomerDialog(MessagerCenterActivity.this);
        dialog.messageDialog(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareDeleteMessage = message;
                Message msg = new Message();
                msg.what = 5;
                msg.obj = prepareDeleteMessage.getId();
                handler.sendMessage(msg);
                dialog.dismiss();
            }
        }, "是否删除该条通知？", "确定");
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            CenterMessage message = (CenterMessage) data.getSerializableExtra(IntentKey.DATA);
            for (CenterMessage bean : messageList) {
                if (bean.getId().equals(message.getId())) {
                    bean.setStatus(DBCode.IS_READ);
                }
            }
            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        try {
            if (returnCode == CallBackType.MESSAGE_GET) {
                mPullRefreshListView.onRefreshComplete();
                JSONObject jsonObject = new JSONObject(value);
                messageCountTotal = Integer.valueOf(jsonObject.getString("count").toString());
                String list = jsonObject.get("list").toString();
                Message msg = new Message();
                currentPage++;
                if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    currentPage = 2;
                    messageList.clear();
                    msg.what = 2;
                    msg.obj = list;
                    handler.sendMessage(msg);
                } else if (mPullRefreshListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    msg.what = 3;
                    msg.obj = list;
                    handler.sendMessage(msg);
                }
            } else if (returnCode == CallBackType.MESSAGE_READ) {
                dao.updateMessageStatus(prepareDeleteMessage.getId());
            } else if (returnCode == CallBackType.MESSAGE_DEL) {
                dao.deleteMessageById(prepareDeleteMessage.getId());
                messageList.remove(prepareDeleteMessage);
                adapter.closeMenu();
                adapter.notifyDataSetChanged();
                showNullPage();
            } else if (returnCode == CallBackType.MESSAGE_DEL_ALL) {
                dao.deleteAllMessage();
                messageList.clear();
                adapter.notifyDataSetChanged();
                showNullPage();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.MESSAGE_GET) {
            mPullRefreshListView.onRefreshComplete();
            Message msg = new Message();
            msg.what = 7;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        } else if (returnCode == CallBackType.MESSAGE_READ) {

        } else if (returnCode == CallBackType.MESSAGE_DEL || returnCode == CallBackType.MESSAGE_DEL_ALL) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        }
    }

    public void initMessage(String value, int type) {
        List<CenterMessage> dateBasemessages = getUserMessage();
        if (StringUtil.isNullOrEmpty(value) || value.equals("[]")) {
            if (type == 0) {
                if (dateBasemessages.size() > 0) {
                    List<CenterMessage> messages = new Gson().fromJson(value, new TypeToken<List<CenterMessage>>() {
                    }.getType());
                    dao.putMessageDate(dao, messages);
                    dateBasemessages = getUserMessage();
                    dateBasemessages = sortList(dateBasemessages);
                    for (CenterMessage bean : dateBasemessages) {
                        messageList.add(bean);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.notifyDataSetChanged();
                    noMessage.setVisibility(View.VISIBLE);
                    mPullRefreshListView.setVisibility(View.GONE);
                }
            } else {
                ToastUtils.showShort(IApplication.globleContext, "没有更多数据了！");
            }
        } else {
            mPullRefreshListView.setVisibility(View.VISIBLE);
            noMessage.setVisibility(View.GONE);
            List<CenterMessage> messages = new Gson().fromJson(value, new TypeToken<List<CenterMessage>>() {
            }.getType());
            if (type == 0) {
                if(messages.size()>0){
                    dao.deleteMessageByUserId(messages.get(0).getUserId());
                }
            }
            dao.putMessageDate(dao, messages);
            dateBasemessages = getUserMessage();
            dateBasemessages = sortList(dateBasemessages);
            if(messageList.size()>0){
                messageList.clear();
            }
            for (CenterMessage bean : dateBasemessages) {
                messageList.add(bean);
            }
            adapter.notifyDataSetChanged();
        }
    }

    public List<CenterMessage> getUserMessage() {
        List<CenterMessage> filterMessage = new ArrayList<CenterMessage>();
        List<CenterMessage> messages = dao.queryMessages();
        for (CenterMessage message : messages) {
            if (message.getUserId().equals(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID))) {
                filterMessage.add(message);
            }
        }
        return filterMessage;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                case 2:
                    initMessage(msg.obj.toString(), 0);
                    break;
                case 3:
                    initMessage(msg.obj.toString(), 1);
                    break;
                case 4://已读
                    messageCotroller.readMessage(msg.obj.toString());
                    break;
                case 5://删除单条
                    messageCotroller.deleteMessageById(msg.obj.toString());
                    break;
                case 6://清空
                    messageCotroller.deleteAllMessage();
                    break;
                case 7:
                    initMessage("", 0);
                    break;
                default:
                    break;
            }
        }
    };


    public void showNullPage() {
        if (messageList.size() == 0) {
            noMessage.setVisibility(View.VISIBLE);
        } else {
            noMessage.setVisibility(View.GONE);
        }
    }

    public void back() {
        setResult(REQUEST_MESSAGE);
        finish();
    }

    public List<CenterMessage> sortList(List<CenterMessage> messages) {
        List<CenterMessage> list = messages;
        if (list.size() == 0) {
            noMessage.setVisibility(View.VISIBLE);
        } else {
            noMessage.setVisibility(View.GONE);
            Collections.sort(list, new Comparator<CenterMessage>() {
                @Override
                public int compare(CenterMessage lhs, CenterMessage rhs) {
                    Date date1 = DateUtil.stringToTime(lhs.getCreateDate());
                    Date date2 = DateUtil.stringToTime(rhs.getCreateDate());
                    LogUtil.check(TAG, "date1:" + date1);
                    LogUtil.check(TAG, "date2:" + date2);
                    if (date1 != null && date2 != null) {
                        // 对日期字段进行升序，如果欲降序可采用after方法
                        if (date1.before(date2)) {
                            return 1;
                        }
                    }
                    return -1;
                }

                @Override
                public boolean equals(Object object) {
                    return false;
                }
            });
        }
        return list;
    }
}
