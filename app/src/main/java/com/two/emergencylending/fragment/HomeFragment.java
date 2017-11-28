package com.two.emergencylending.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chinazyjr.lib.util.DensityUtils;
import com.chinazyjr.lib.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.refresh.PullToRefreshBase;
import com.refresh.PullToRefreshScrollView;
import com.two.emergencylending.activity.ApplyActivity;
import com.two.emergencylending.activity.ApplyCheckActivity;
import com.two.emergencylending.activity.BannerDetailActivity;
import com.two.emergencylending.activity.BasicInforActivity;
import com.two.emergencylending.activity.ContactVerify;
import com.two.emergencylending.activity.CustomerConfirmActivity;
import com.two.emergencylending.activity.ElectronicAgreementActivity;
import com.two.emergencylending.activity.ImmediatelyLoanActivity;
import com.two.emergencylending.activity.LoginAndRegisterModule.LoginActivity;
import com.two.emergencylending.activity.MainActivity;
import com.two.emergencylending.activity.MessagerCenterActivity;
import com.two.emergencylending.activity.MineInstantLoanActivity;
import com.two.emergencylending.activity.OrderStatusActivity;
import com.two.emergencylending.activity.PersonalDataActivity;
import com.two.emergencylending.adapter.BannerAdapter;
import com.two.emergencylending.adapter.ClerkCustomerAdapter;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseFragment;
import com.two.emergencylending.bean.BannerBean;
import com.two.emergencylending.bean.BorrowInfoBean;
import com.two.emergencylending.bean.ClerkCustomerBean;
import com.two.emergencylending.bean.UsageBean;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.BuryAction;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.CommonConstant;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.BorrowStatusCotroller;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.CustomerManagerController;
import com.two.emergencylending.controller.HomeAdCotroller;
import com.two.emergencylending.controller.MessageCotroller;
import com.two.emergencylending.controller.MyQrCodeController;
import com.two.emergencylending.interfaces.EventClick;
import com.two.emergencylending.manager.BorrowStatusManager;
import com.two.emergencylending.manager.BuryPointManager;
import com.two.emergencylending.manager.CustomerManagerManager;
import com.two.emergencylending.utils.Arithmetic;
import com.two.emergencylending.utils.CarouselUtil;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CommonalityFieldUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.widget.BubbleIndicator;
import com.two.emergencylending.widget.BubbleSeekBar;
import com.two.emergencylending.widget.FlowTagLayout;
import com.two.emergencylending.widget.OnTagSelectListener;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：关于页面
 * 创建人：szx
 * 创建时间：2016/8/11.
 * 修改人：szx
 * 修改时间：2016/8/11.
 * 修改备注：
 */
public class HomeFragment extends BaseFragment implements ControllerCallBack, EventClick, View.OnClickListener, OnTagSelectListener, AdapterView.OnItemClickListener {
    private String TAG = HomeFragment.class.getSimpleName();
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.mainlayout)
    LinearLayout dotlayout;
    @Bind(R.id.btn_credit)
    Button btnCredit;
    @Bind(R.id.rl_message)
    RelativeLayout rlMessage;
    @Bind(R.id.home_message_center)
    ImageView message;
    @Bind(R.id.home_message_count)
    TextView messageCount;
    @Bind(R.id.home_code)
    ImageView home_code;
    @Bind(R.id.seekbar_money)
    BubbleSeekBar seekbarMoney;
    @Bind(R.id.seekbar_week)
    BubbleSeekBar seekbarWeek;
    @Bind(R.id.home_notice)
    FrameLayout homeNotice;
    @Bind(R.id.ll_client)
    LinearLayout llClient;
    @Bind(R.id.ll_clerk)
    LinearLayout llClerk;
    @Bind(R.id.list)
    ListView list;
    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;

    private static final int STATUS_DEFAULT = 0;
    private static final int STATUS_LOAN_ABLE = STATUS_DEFAULT + 1;
    private static final int STATUS_CERTIFICATION = STATUS_LOAN_ABLE + 1;
    private static final int STATUS_PROGRESS = STATUS_CERTIFICATION + 1;
    private static final int STATUS_CONFIRM = STATUS_PROGRESS + 1;
    private static final int STATUS_OFFLINE = STATUS_CONFIRM + 1;
    private static final int STATUS_ORDER = STATUS_OFFLINE + 1;
    private static final int STATUS_OPEN_ACCOUNT = STATUS_ORDER + 1;
    private static final int STATUS_BIND_CARD = STATUS_OPEN_ACCOUNT + 1;
    private static final int STATUS_ELECTRONIC_AGREEMENT = STATUS_BIND_CARD + 1;
    private int loanStatus = STATUS_CERTIFICATION;

    private static int money = 2000;
    private static int week = 5;
    private static int moneyProgress = 12;
    private static int weekProgress = 0;
    private static int moneyCritical = 30;
    //返回码
    private final int RESULT_CODE = 100;
    //消息中心请求码
    private final int REQUEST_MESSAGE = 102;

    ClerkCustomerAdapter clerkCustomerAdapter;
    private List<ClerkCustomerBean> clerkCustomers = new ArrayList<ClerkCustomerBean>();
    private List<BannerBean> banners = new ArrayList<BannerBean>();
    private List<ImageView> viewList = new ArrayList<ImageView>();
    private int oldPosition;
    private int j = 0;
    private Timer timer;
    private TimerTask timerTask;

    private DisplayImageOptions options;
    private CustomerDialog dialog;

    private BorrowInfoBean bean;
    private static UsageBean usageBean;

    private boolean isResume = true;
    PullToRefreshScrollView pullToRefreshScrollView;
    HomeAdCotroller homeAdCotroller;
    BorrowStatusCotroller borrowStatusCotroller;
    MyQrCodeController myQrCodeController;
    MessageCotroller messageCotroller;
    CustomerManagerController customerManagerController;

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setContent() {
        return R.layout.activity_home;
    }

    public static void reset() {
        moneyProgress = moneyCritical;
        weekProgress = 20;
        usageBean = null;
        usageBean = new UsageBean();
    }


    @Override
    public void init() {


        bean = new BorrowInfoBean();
        bean.setBorrow_limit("5000");
        UserInfoManager.getInstance().setBorrowInfo(bean);
        dialog = new CustomerDialog(getActivity());
        seekbarMoney.setType(BubbleIndicator.MONEY);
        seekbarWeek.setType(BubbleIndicator.WEEK);
        if (myQrCodeController == null) {
            myQrCodeController = new MyQrCodeController();
        }
        myQrCodeController.getMyCode(getActivity(), null);
        ((MainActivity) getActivity()).setStatusColor(R.color.pickerview_timebtn_pre);
        rlMessage.setPadding(10, 10 + getStatusBarHeight(), 10, 10);
        messageCotroller = new MessageCotroller(getActivity(), this);
        customerManagerController = new CustomerManagerController(getActivity(), this);
        borrowStatusCotroller = new BorrowStatusCotroller(getActivity(), this);
        seekbarMoney.setProgress(1);
        seekbarWeek.setProgress(1);
        homeAdCotroller = new HomeAdCotroller(getActivity(), this);
        homeAdCotroller.getHomeAd();
        pullToRefreshScrollView = (PullToRefreshScrollView) root.findViewById(R.id.refresh);
        if (usageBean == null || StringUtil.isNullOrEmpty(usageBean.getUsageCode())) {
            usageBean = new UsageBean();
        }
        CarouselUtil.getInstance().clearNotice();
        CarouselUtil.getInstance().initRollNotice(getActivity(), root);
        CarouselUtil.getInstance().setOnClickListener(this);
        addClerkCustomer();
        clerkCustomerAdapter = new ClerkCustomerAdapter(getActivity(), clerkCustomers);
        list.setAdapter(clerkCustomerAdapter);
        list.setOnItemClickListener(HomeFragment.this);
    }


    public void addClerkCustomer() {
//        ClerkCustomerBean bean1 = new ClerkCustomerBean();
//        bean1.setUsername("张明远");
//        bean1.setPhone("13915531624");
//        bean1.setAmount("5000");
//        bean1.setPeriod("15");
//        bean1.setApply_status("");
//        ClerkCustomerBean bean2 = new ClerkCustomerBean();
//        bean2.setUsername("张兆远");
//        bean2.setPhone("13925531624");
//        bean2.setAmount("9000");
//        bean2.setPeriod("10");
//        bean2.setApply_status("1");
//        ClerkCustomerBean bean3 = new ClerkCustomerBean();
//        bean3.setUsername("李大山");
//        bean3.setPhone("13915531624");
//        bean3.setAmount("5000");
//        bean3.setPeriod("15");
//        bean3.setApply_status("2");
//        ClerkCustomerBean bean4 = new ClerkCustomerBean();
//        bean4.setUsername("沈馨瑶");
//        bean4.setPhone("13925531624");
//        bean4.setAmount("9000");
//        bean4.setPeriod("10");
//        bean4.setApply_status("4");
//        ClerkCustomerBean bean5 = new ClerkCustomerBean();
//        bean5.setUsername("沈馨瑶");
//        bean5.setPhone("13925531624");
//        bean5.setAmount("9000");
//        bean5.setPeriod("10");
//        bean5.setApply_status("放款成功");
//        ClerkCustomerBean bean6 = new ClerkCustomerBean();
//        bean6.setUsername("沈馨瑶");
//        bean6.setPhone("13925531624");
//        bean6.setAmount("9000");
//        bean6.setPeriod("10");
//        bean6.setApply_status("19");
//        ClerkCustomerBean bean7 = new ClerkCustomerBean();
//        bean7.setUsername("沈馨瑶");
//        bean7.setPhone("13925531624");
//        bean7.setAmount("9000");
//        bean7.setPeriod("10");
//        bean7.setApply_status("5");
//        ClerkCustomerBean bean8 = new ClerkCustomerBean();
//        bean8.setUsername("沈馨瑶");
//        bean8.setPhone("13925531624");
//        bean8.setAmount("9000");
//        bean8.setPeriod("10");
//        bean8.setApply_status("18");
//        clerkCustomers.add(bean1);
//        clerkCustomers.add(bean2);
//        clerkCustomers.add(bean3);
//        clerkCustomers.add(bean4);
//        clerkCustomers.add(bean5);
//        clerkCustomers.add(bean6);
//        clerkCustomers.add(bean7);
//        clerkCustomers.add(bean8);
    }

    public void initImages() {
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_banner)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_banner) // 设置图片Uri为空的时候显示的图片
                .showImageOnFail(R.drawable.default_banner) // 设置图片失败的时候显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .build();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        viewList.clear();
        for (int i = 0; i < banners.size(); i++) {
            if (getActivity() == null) {
                LogUtil.i(TAG, "getActivity is null");
            }
            if (getContext() != null) {
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                ImageLoader.getInstance().displayImage(banners.get(i).getAd_pic(), imageView, options);
                viewList.add(imageView);
            }
        }
    }

    private void initList() {
        if (dotlayout != null) {
            dotlayout.removeAllViews();
            for (int i = 0; i < banners.size(); i++) {
                if (i == 0) {
                    dotlayout.addView(setDaoHangText(R.drawable.dot_highlight));
                } else {
                    dotlayout.addView(setDaoHangText(R.drawable.dot_default));
                }
            }
        }
    }

    private View setDaoHangText(int id) {
        View text = new View(getActivity());
        LinearLayout.LayoutParams Viewpar = new LinearLayout.LayoutParams(DensityUtils.dp2px(getActivity(), 8), DensityUtils.dp2px(getActivity(), 8));
        Viewpar.setMargins(5, 5, 5, 5);
        text.setLayoutParams(Viewpar);
        text.setBackgroundResource(id);
        return text;
    }

    private void initAdapter() {
        viewPager.setAdapter(new BannerAdapter(banners, viewList, HomeFragment.this));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                j = arg0;
                dotlayout.getChildAt(oldPosition).setBackgroundResource(R.drawable.dot_default);
                dotlayout.getChildAt(arg0).setBackgroundResource(R.drawable.dot_highlight);
                oldPosition = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void myTask() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isResume) {
                    handler.sendEmptyMessage(0);
                }
            }
        };
        timer.schedule(timerTask, 1000, 2000);
    }


    public void initSeekMoney() {
        if (seekbarMoney != null) {
            seekbarMoney.setProgress(moneyProgress);
        }
        money = Arithmetic.progressToMoney(moneyProgress);
        if (seekbarWeek != null) {
            seekbarWeek.setProgress(weekProgress);
        }
        week = Arithmetic.progressToWeek(weekProgress);
    }

    @Override
    public void setData() {
        initSeekMoney();
    }

    @Override
    public void resume() {
        showView();
        initSeekMoney();
        BuryPointManager.buryActivityBegin(BuryAction.MD_AVTIVITY_HOME);
        isResume = true;
        if (!IApplication.getInstance().isLogin()) {
            if (CustomerManagerManager.isCustomerManager()) {
                customerManagerController.queryCustomerRecord("", "", "-1", 7);
            } else {
                borrowStatusCotroller.getBorrowStatusBack();
                messageCotroller.getMessageReadCount("1", "20");
            }
        }
        if (IApplication.getInstance().isToMine) {
            ((MainActivity) getActivity()).changeToMine();
            IApplication.getInstance().isToMine = false;
        }
    }

    @Override
    public void onPause() {
        BuryPointManager.buryActivityEnd(BuryAction.MD_AVTIVITY_HOME);
        isResume = false;
        super.onPause();
    }


    @Override
    public void setListener() {
        seekbarMoney.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                moneyProgress = progress;
                money = Arithmetic.progressToMoney(moneyProgress);
                bean.setBorrow_limit(String.valueOf(money));
                UserInfoManager.getInstance().setBorrowInfo(bean);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekbarWeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                weekProgress = progress;
                week = Arithmetic.progressToWeek(weekProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbarWeek.hideIndicator();
            }
        });

        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                homeAdCotroller.getHomeAd();
                if (!IApplication.getInstance().isLogin()) {
                    if (CustomerManagerManager.isCustomerManager()) {
                        customerManagerController.queryCustomerRecord("", "", "-1", 7);
                    } else {
                        borrowStatusCotroller.getBorrowStatusBack();
                    }
                }
            }
        });
    }


    @Override
    public void destroy() {
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @OnClick({R.id.home_code, R.id.btn_credit, R.id.home_message_center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_credit:
                if (IApplication.getInstance().isLogin()) {
                    LogUtil.i(TAG, "go to BasicInforActivity");
                    CommonUtils.goToActivity(mContext, BasicInforActivity.class);
                } else if (CustomerManagerManager.isCustomerManager()) {
                    CommonUtils.goToActivity(mContext, ApplyCheckActivity.class);
                } else {

                    LogUtil.i(TAG, "go to getBorrowStatus");
                    //把点击控件传过去，防止网络不好的时候，重复点击
                    btnCredit.setEnabled(false);
                    borrowStatusCotroller.getBorrowStatus(btnCredit);
                }
                break;

            case R.id.home_code:
                if (!TextUtils.isEmpty(UserInfoManager.getInstance().getQr_code_url())) {
                    String code = SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.SHARE_CODE);
                    dialog.qrCode(UserInfoManager.getInstance().getQr_code_url() + "?register_platform=" + CommonalityFieldUtils.getDittchStrN(), code);
                    dialog.show();
                } else {
                    IApplication.getInstance().clearUserInfo(getActivity());
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    startActivity(login);
                }
                break;
            case R.id.home_message_center:
                Intent intentMessage = new Intent(getActivity(), MessagerCenterActivity.class);
                startActivityForResult(intentMessage, REQUEST_MESSAGE);
                break;
            case R.id.rl_invite:
                if (UserInfoManager.getInstance().getQr_code_url() != null && !UserInfoManager.getInstance().getQr_code_url().equals("")) {
                    String code = SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.SHARE_CODE);
                    dialog.qrCode(UserInfoManager.getInstance().getQr_code_url(), code);
                    dialog.show();
                } else {
                    IApplication.getInstance().clearUserInfo(getActivity());
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    startActivity(login);
                }
                break;
            default:
                break;

        }
    }


    private void secletBorrowInfo(String value, boolean isClick) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(value);
            String borrowStatus = jsonObject.getString("BorrowStatus");
            String productId = "";
            if (jsonObject.has("product_id")) {
                productId = jsonObject.getString("product_id");
            }
            String isClerkOpt = "";
            String isVerifiedContract = "";
            if (jsonObject.has("is_clerk_opt") && jsonObject.has("is_verified_contract")) {
                isClerkOpt = jsonObject.getString("is_clerk_opt");
                isVerifiedContract = jsonObject.getString("is_verified_contract");
            }
            int status = BorrowStatusManager.getBorrowStatus(borrowStatus);
            if (status == BorrowStatusManager.PAGE_STATUS_LOAN) {
                loanStatus = STATUS_LOAN_ABLE;
                setButtonStatus("立即借款");
            } else if (status == BorrowStatusManager.PAGE_STATUS_AUTH) {
                loanStatus = STATUS_CERTIFICATION;
                setButtonStatus("前往认证");
            } else if (status == BorrowStatusManager.PAGE_STATUS_PROGRESS) {
                loanStatus = STATUS_PROGRESS;
                setButtonStatus("查看订单状态");
            } else if (status == BorrowStatusManager.PAGE_STATUS_CONFIRM_MONEY) {
                if (!StringUtil.isNullOrEmpty(productId)) {
                    if (!CommonConstant.PRODUCT_OFFLINE.equals(productId)) {
                        loanStatus = STATUS_CONFIRM;
                        setButtonStatus("确认审批金额");
                    } else {
                        loanStatus = STATUS_ORDER;
                        setButtonStatus("查看订单状态");
                    }
                } else {
                    loanStatus = STATUS_ORDER;
                    setButtonStatus("查看订单状态");
                }
            } else if (status == BorrowStatusManager.PAGE_STATUS_VIEW_ORDER) {
                loanStatus = STATUS_ORDER;
                setButtonStatus("查看订单状态");
            } else if (status == BorrowStatusManager.PAGE_STATUS_ELECTRONIC_AGREEMENT) {
                loanStatus = STATUS_ELECTRONIC_AGREEMENT;
                setButtonStatus("立即签约");
            }
            if (isClick) {
                saveBorrowInfo(isClerkOpt, isVerifiedContract);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setButtonStatus(String status) {
        if (btnCredit == null) {
            LogUtil.i(TAG, "btnCredit is null");
        } else {
            btnCredit.setText(status);
        }
    }

    private void saveBorrowInfo(String isClerkOpt, String isVerifiedContract) {
        if (loanStatus == STATUS_LOAN_ABLE) {
            loan();
        } else if (loanStatus == STATUS_CERTIFICATION) {
            CommonUtils.goToActivity(mContext, PersonalDataActivity.class, new Intent().putExtra(SPKey.ISSHOWAUTHOR, true));
        } else if (loanStatus == STATUS_PROGRESS) {
            CommonUtils.goToActivity(mContext, OrderStatusActivity.class);
        } else if (loanStatus == STATUS_CONFIRM) {
            CommonUtils.goToActivity(mContext, CustomerConfirmActivity.class);
        } else if (loanStatus == STATUS_ORDER) {
            CommonUtils.goToActivity(mContext, MineInstantLoanActivity.class);
        } else if (loanStatus == STATUS_ELECTRONIC_AGREEMENT) {
            if (isClerkOpt.equals("1") && isVerifiedContract.equals("0")) {
                CommonUtils.goToActivity(mContext, ContactVerify.class);
            } else {
                CommonUtils.goToActivity(mContext, ElectronicAgreementActivity.class);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MESSAGE && resultCode == RESULT_CODE) {
            if (!IApplication.getInstance().isLogin()) {
                messageCotroller.getMessageReadCount("1", "20");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            LogUtil.e("sbar", "get status bar height fail");
            e1.printStackTrace();
        }
        return sbar;
    }


    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.BORRABLE) {
            try {
                JSONObject jsonObject = new JSONObject(value);
                if ((Boolean) jsonObject.get("isborrow")) {
                    CommonUtils.goToActivity(mContext, PersonalDataActivity.class, new Intent().putExtra(SPKey.ISSHOWAUTHOR, true));
                } else {
                    CommonUtils.goToActivity(mContext, ImmediatelyLoanActivity.class);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (returnCode == CallBackType.MESSAGE_READ_COUNT) {
            try {
                JSONObject jsonObject = new JSONObject(value);
                String count = jsonObject.get("count").toString();
                if (Integer.valueOf(count) > 0) {
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = String.valueOf(count);
                    handler.sendMessage(msg);
                } else {
                    if (message == null) {
                        LogUtil.i(TAG, "message is null");
                    } else {
                        message.setImageDrawable(IApplication.globleResource.getDrawable(R.drawable.icon_message));
                        messageCount.setVisibility(View.INVISIBLE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (returnCode == CallBackType.HOME_AD) {
            pullToRefreshScrollView.onRefreshComplete();
            j = 0;
            oldPosition = j;
            if (timer != null) {
                timer.cancel();
            }
            if (timerTask != null) {
                timerTask.cancel();
            }
            initAD(value);
        } else if (returnCode == CallBackType.BORROW_STATUS_BACK) {
            secletBorrowInfo(value, false);
        } else if (returnCode == CallBackType.BORROW_STATUS) {
            secletBorrowInfo(value, true);
        } else if (returnCode == CallBackType.QUERY_CUSTOMER_RECORD) {
            try {
                JSONObject json = new JSONObject(value);
                int count = json.getInt("count");
                String listJson = json.getString("list");
                if (count > 0) {
                    clerkCustomers.clear();
                    clerkCustomers = new Gson().fromJson(listJson, new TypeToken<List<ClerkCustomerBean>>() {
                    }.getType());
                    clerkCustomerAdapter.refresh(clerkCustomers);
                    list.setVisibility(View.VISIBLE);
                    ll_nodata.setVisibility(View.GONE);
                } else {
                    list.setVisibility(View.GONE);
                    ll_nodata.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.BORRABLE) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        } else if (returnCode == CallBackType.MESSAGE_READ_COUNT) {
            Message msg = new Message();
            msg.what = 5;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        } else if (returnCode == CallBackType.HOME_AD) {
            pullToRefreshScrollView.onRefreshComplete();
        } else if (returnCode == CallBackType.BORROW_STATUS_BACK) {

        } else if (returnCode == CallBackType.BORROW_STATUS) {
        } else if (returnCode == CallBackType.QUERY_CUSTOMER_RECORD) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        }
    }

    public void initAD(String value) {
        List<BannerBean> bannerBeans = new Gson().fromJson(value, new TypeToken<List<BannerBean>>() {
        }.getType());
        banners.clear();
        for (BannerBean bean : bannerBeans) {
            banners.add(bean);
        }
        initImages();
        initList();
        initAdapter();
        myTask();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (isResume) {
                        j++;
                        if (j < banners.size()) {
                            viewPager.setCurrentItem(j);
                        } else {
                            j = 0;
                            viewPager.setCurrentItem(j);
                        }
                    }
                    break;
                case 1:
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                case 2:
                    if (message != null) {
                        message.setImageDrawable(IApplication.globleResource.getDrawable(R.drawable.icon_messageh));
                        messageCount.setVisibility(View.INVISIBLE);
                        messageCount.setText(msg.obj.toString());
                    }
                    break;
                case 5:
                    if (message != null) {
                        message.setImageDrawable(IApplication.globleResource.getDrawable(R.drawable.icon_message));
                        messageCount.setVisibility(View.INVISIBLE);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void eventClick(String url) {
        if (!StringUtil.isNullOrEmpty(url)) {
            Intent intent = new Intent();
            intent.putExtra("url", url);
            CommonUtils.goToActivity(getActivity(), BannerDetailActivity.class, intent);
        }
    }

    @Override
    public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
        if (selectedList != null && selectedList.size() > 0) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public void loan() {
        bean.setProduct_id("1");
        bean.setRefund_way("按周还款");
        bean.setBorrow_use_code("525");
        bean.setBorrow_use("其他");
        bean.setBorrow_limit(String.valueOf(money));
        bean.setBorrow_periods(String.valueOf(week));
        bean.setManagement_cost(String.valueOf(Arithmetic.expenseByManager(money, week)));
        if (CommonUtils.checkNull(bean.getBorrow_limit(), "金额不能为空")) return;
        if (CommonUtils.checkNull(bean.getBorrow_periods(), "周期不能为空")) return;
        if (CommonUtils.checkNull(bean.getRefund_way(), "还款方式不能为空")) return;
        UserInfoManager.getInstance().setBorrowInfo(bean);
        CommonUtils.goToActivity(mContext, PersonalDataActivity.class, new Intent().putExtra(SPKey.ISSHOWAUTHOR, true));
    }

    public void showView() {
        if (CustomerManagerManager.isCustomerManager()) {
            homeNotice.setVisibility(View.GONE);
            llClient.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
            llClerk.setVisibility(View.VISIBLE);

        } else {
            homeNotice.setVisibility(View.VISIBLE);
            llClient.setVisibility(View.VISIBLE);
            message.setVisibility(View.VISIBLE);
            llClerk.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (clerkCustomers.get(position).getBorrow_status().equals("")) {
            String custId = clerkCustomers.get(position).getCust_id();
            String phone = clerkCustomers.get(position).getPhone();
            Intent intent = new Intent();
            intent.putExtra("cust_id", custId);
            intent.putExtra("phone", phone);
            CommonUtils.goToActivity(getContext(), ApplyActivity.class, intent);
        }
    }
}
