package com.two.emergencylending.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.LocationBean;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.GetMyStatu;
import com.two.emergencylending.controller.GpsController;
import com.two.emergencylending.fragment.ApplyEndFragment;
import com.two.emergencylending.fragment.AuthenticateFragment;
import com.two.emergencylending.fragment.ContactFragment;
import com.two.emergencylending.fragment.JobFragment;
import com.two.emergencylending.fragment.PerSonalFragment;
import com.two.emergencylending.manager.BorrowStatusManager;
import com.two.emergencylending.manager.CustomerManagerManager;
import com.two.emergencylending.service.LocationService;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.KeyBoard;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.view.LoanActionSchedule;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;

/**
 *
 *
 */
public class PersonalDataActivity extends BaseActivity implements Topbar.topbarClickListener, LoanActionSchedule.onClickItemListener {
    @Bind(R.id.personal_tolbar)
    Topbar personal_tolbar;
    @Bind(R.id.schedule)
    LoanActionSchedule schedule;
    @Bind(R.id.main)
    FrameLayout main;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Fragment current;
    public static boolean isShowAuthor = false;
    public static String custId;
    public static String phoneNum;
    public static String offline_calp_msg;
    public static String org_no;
    public static String sall_emp_no;
    public static String org_name;

    private int complete = 0;
    private GetMyStatu statu;
    public boolean isOnClik = true;

    public static final int STEP_PERSONAL = 0;
    public static final int STEP_JOB = STEP_PERSONAL + 1;
    public static final int STEP_CONTACT = STEP_JOB + 1;
    public static final int STEP_AUTHENTICATE = STEP_CONTACT + 1;
    public static final int STEP_APPLE_END = STEP_AUTHENTICATE + 1;
    public static int curStep = STEP_PERSONAL;
    public LocationService locationService;
    private LocationBean locationBean;
    private GpsController gps;

    @Override
    public int setContent() {
        return R.layout.activity_personal_data;
    }

    @Override
    public int setStatusColor() {
        return R.color.white;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastAlone.showLongToast(getContext(), msg.obj.toString());
                    swithFragment(PerSonalFragment.class);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void init() {
        locationService = new LocationService(this);
        locationBean = new LocationBean();
        locationService.registerListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                locationBean.setmCurrentLatitude(Double.valueOf(bdLocation.getLatitude()));
                ;//纬度
                locationBean.setmCurrentLongitude(Double.valueOf(bdLocation.getLongitude()));//经度
                locationBean.setmCurrentProvince(bdLocation.getProvince());//省
                locationBean.setmCurrentCity(bdLocation.getCity());// 城市
                locationBean.setmCurrentStreet(bdLocation.getStreet());// 街道
                locationBean.setmCurrentDistrict(bdLocation.getDistrict());// 区
                locationBean.setmCurrentStreetNumber(bdLocation.getStreetNumber());//街道号码
                locationBean.setmCurrentAddrStr(bdLocation.getAddrStr());// 地址信息
                UserInfoManager.getInstance().setLocation(locationBean);
                gps = new GpsController(null);
                if (!TextUtils.isEmpty(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID))
                        && !TextUtils.isEmpty(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN))
                        && !TextUtils.isEmpty(UserInfoManager.getInstance().getLocation().getmCurrentProvince())
                        && !TextUtils.isEmpty(UserInfoManager.getInstance().getLocation().getmCurrentCity())
                        && !TextUtils.isEmpty(UserInfoManager.getInstance().getLocation().getmCurrentDistrict())
                        && !TextUtils.isEmpty(UserInfoManager.getInstance().getLocation().getmCurrentStreet())
                        && !TextUtils.isEmpty(UserInfoManager.getInstance().getLocation().getmCurrentStreetNumber())
                        && !TextUtils.isEmpty(String.valueOf(Double.valueOf(UserInfoManager.getInstance().getLocation().getmCurrentLatitude())))
                        && !TextUtils.isEmpty(String.valueOf(Double.valueOf(UserInfoManager.getInstance().getLocation().getmCurrentLongitude())))
                        && !TextUtils.isEmpty(UserInfoManager.getInstance().getLocation().getmCurrentAddrStr())
                        ) {
                    gps.gpsLocationMsg(UserInfoManager.getInstance().getLocation());
                }

                locationService.stop();
                locationService.unregisterListener(this);
            }
        });
        locationService.start();
        Log.e("Location", "Location  init...");

        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        statu = new GetMyStatu(this, new ControllerCallBack() {
            @Override
            public void onSuccess(int returnCode, String value) {
                toStep();
            }

            @Override
            public void onFail(int returnCode, final String errorMessage) {
                if (returnCode == CallBackType.GET_USER_STATU) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = errorMessage;
                    handler.sendMessage(msg);
                }
            }
        });

//        complete = SharedPreferencesUtil.getInstance(getContext()).getInt(SPKey.complete, 0);
//        setComplete(complete);
        KeyBoard.closeSoftKeyboard(this);
        personal_tolbar.getLeftIco().setVisibility(View.VISIBLE);
        personal_tolbar.getLeftIco().setImageResource(R.drawable.icon_back);
        schedule.setOnClikItemListener(this);
//        swithFragment(PerSonalFragment.class);
        custId = getIntent().getStringExtra("cust_id");
        phoneNum = getIntent().getStringExtra("phone");
        if (CustomerManagerManager.isCustomerManager()) {
            toStep();
        } else {
            statu.getMystatu();
        }
        isShowAuthor = getIntent().getBooleanExtra(SPKey.ISSHOWAUTHOR, false);
        if (isShowAuthor) {
            schedule.setArrow(View.VISIBLE);
        } else
            schedule.setArrow(View.GONE);
//        showArrow(isShowAuthor);
    }

    public void showArrow(boolean isShowAuthor) {
        PersonalDataActivity.isShowAuthor = isShowAuthor;
        if (isShowAuthor) {
            schedule.setArrow(View.VISIBLE);
        } else {
            schedule.setArrow(View.GONE);
        }
    }

    private void toStep() {
        int borrowStatus = BorrowStatusManager.getBorrowStatus(UserInfoManager.getInstance().getUserStatuModels().getBorrowStatus());
//        borrowStatus = BorrowStatusManager.PAGE_STATUS_AUTH;
        if (borrowStatus == BorrowStatusManager.PAGE_STATUS_LOAN) {
            setComplete(LoanActionSchedule.EDIT_PERSONAL_STATUS);
            curStep = STEP_PERSONAL;
            swithFragment(PerSonalFragment.class);
        } else if (borrowStatus == BorrowStatusManager.PAGE_STATUS_AUTH) {
            setComplete(LoanActionSchedule.EDIT_AUTHOR_STATUS);
            curStep = STEP_AUTHENTICATE;
            if (isShowAuthor) {
                swithFragment(AuthenticateFragment.class);
            } else {
                swithFragment(PerSonalFragment.class);
            }
        } else {
            swithFragment(PerSonalFragment.class);
        }
//        else {
//            setComplete(LoanActionSchedule.END_STATUS);
//            curStep = STEP_APPLE_END;
//            isOnClik = true;
//            if (isShowAuthor) {
//                swithFragment(ApplyEndFragment.class);
//            }
//        }
// else if (UserInfoManager.getInstance().getUserStatuModels().getUserJobStatus() == 0) {
//            setComplete(LoanActionSchedule.EDIT_JOB_STATUS);
//            curStep = STEP_JOB;
//            swithFragment(JobFragment.class);
//        } else if (UserInfoManager.getInstance().getUserStatuModels().getUserContactStatus() == 0 || borrowStatus == 0 || borrowStatus == 1 || borrowStatus == 3 || borrowStatus == 5|| borrowStatus == 16) {
//            setComplete(LoanActionSchedule.EDIT_CONTACT_STATUS);
//            curStep = STEP_CONTACT;
//            swithFragment(ContactFragment.class);
//        } else

//        if ("7".equals(UserInfoManager.getInstance().getUserStatuModels().getUserAuthStatusShouji())) {
//            CommonUtils.goToActivity(this, CredentialsActivity.class);
//            finish();
//        }
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        personal_tolbar.setOnTopbarClickListener(this);
    }

    @Override
    public void resume() {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void destroy() {
    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            CustomerDialog customerDialog = new CustomerDialog(this);
//            customerDialog.showShareDialog();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public void swithFragment(Class<? extends Fragment> fragment) {
        try {
            if (null == manager) {
                manager = getSupportFragmentManager();
            }
            transaction = manager.beginTransaction();
            Fragment temp = null;
//            temp = manager.findFragmentByTag(fragment.getSimpleName());
//            if (current != null && temp != null &&
//                    current.getClass().getSimpleName().equals(temp.getClass().getSimpleName())) {
//                Log.e(getClass().getSimpleName(), fragment.getSimpleName() + "is show");
//                return;
//            }
            if (current != null
                    && current.getClass().getSimpleName().equals(fragment.getSimpleName())) {
                return;
            }
//            if (temp == null) {
            temp = fragment.newInstance();
            Log.e(getClass().getSimpleName(), fragment.getSimpleName() + "init...");
            transaction.add(R.id.main, temp, fragment.getSimpleName());
//            }
            if (current != null) {
                transaction.hide(current);
                transaction.remove(current);
            }
            if (temp != null) {
                transaction.show(temp);
                current = temp;
            }
            transaction.commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }

    public void setComplete(int complete) {
        if (this.complete <= complete) {
            this.complete = complete;
//            SharedPreferencesUtil.getInstance(getContext()).setInt(SPKey.complete, complete);
            schedule.setStatus(complete);
        }
    }

    @Override
    public void onItemsClick(int item) {
        if (isOnClik) {
            return;
        }
        switch (item) {
            case LoanActionSchedule.EDIT_PERSONAL_STATUS:
                if (complete >= LoanActionSchedule.EDIT_PERSONAL_STATUS)
                    swithFragment(PerSonalFragment.class);
                break;
            case LoanActionSchedule.EDIT_JOB_STATUS:
                if (complete >= LoanActionSchedule.EDIT_JOB_STATUS)
                    swithFragment(JobFragment.class);
                break;
            case LoanActionSchedule.EDIT_CONTACT_STATUS:
                if (complete >= LoanActionSchedule.EDIT_CONTACT_STATUS)
                    swithFragment(ContactFragment.class);
                break;
            case LoanActionSchedule.EDIT_AUTHOR_STATUS:
                if (complete >= LoanActionSchedule.EDIT_AUTHOR_STATUS)
                    swithFragment(AuthenticateFragment.class);
                break;
            case LoanActionSchedule.END_STATUS:
                if (complete >= LoanActionSchedule.END_STATUS)
                    swithFragment(ApplyEndFragment.class);
                break;
        }

    }

}
