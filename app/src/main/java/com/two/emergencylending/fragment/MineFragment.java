package com.two.emergencylending.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinazyjr.lib.util.ToastUtils;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.two.emergencylending.DataBase.NoticeDao;
import com.two.emergencylending.activity.EarnABonusActivity;
import com.two.emergencylending.activity.LoginAndRegisterModule.LoginActivity;
import com.two.emergencylending.activity.MainActivity;
import com.two.emergencylending.activity.MessagerCenterActivity;
import com.two.emergencylending.activity.MineInstantLoanActivity;
import com.two.emergencylending.activity.MyApplyActivity;
import com.two.emergencylending.activity.MyBonusActivity;
import com.two.emergencylending.activity.NoRepaymentActivity;
import com.two.emergencylending.activity.PerformanceActivity;
import com.two.emergencylending.activity.PersonalDataActivity;
import com.two.emergencylending.activity.RepaymentActivity;
import com.two.emergencylending.activity.SettingActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseFragment;
import com.two.emergencylending.bean.Notice;
import com.two.emergencylending.bean.RepayBorrowInfoBean;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.H5PageKey;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.GetMyStatu;
import com.two.emergencylending.controller.IControllerCallBack;
import com.two.emergencylending.controller.MessageCotroller;
import com.two.emergencylending.controller.MyQrCodeController;
import com.two.emergencylending.controller.RepayBorrowInfoController;
import com.two.emergencylending.controller.RepaymentControl;
import com.two.emergencylending.controller.UploadAvatar;
import com.two.emergencylending.controller.WyController;
import com.two.emergencylending.manager.CustomerManagerManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CommonalityFieldUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.view.CircleImageView;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wangyaping
 */
public class MineFragment extends BaseFragment implements ControllerCallBack {

    @Bind(R.id.rl_mine)
    RelativeLayout mine;
    @Bind(R.id.phone_num)
    TextView tv_phone_num;
    @Bind(R.id.head)
    CircleImageView head;
    @Bind(R.id.home_message_center)
    ImageView message;
    @Bind(R.id.home_message_count)
    TextView messageCount;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.rl_performance)
    RelativeLayout performance;
    @Bind(R.id.rl_apply)
    RelativeLayout apply;
    @Bind(R.id.rl_personal)
    RelativeLayout personal;
    @Bind(R.id.rl_loan)
    RelativeLayout loan;
    @Bind(R.id.rl_repayment)
    RelativeLayout repayment;
    @Bind(R.id.tv_customer_manager)
    TextView tv_customer_manager;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.ll_loan)
    LinearLayout ll_loan;


    UploadAvatar uploadAvatar;
    private GetMyStatu myStatu;
    NoticeDao mNoticeDaoDao;
    MessageCotroller messageCotroller;
//    private MineBorrowCotroller mineBorrow;
    private String H5Token;
    private String H5Status;
    //返回码
    private final int RESULT_CODE = 100;
    //消息中心请求码
    private final int REQUEST_MESSAGE = 102;
    private DisplayImageOptions options;
    private TakePhoto takePhoto;
    private String imgPath = "";
    Bitmap bitmap, mBitmap;
    Uri imageUri;
    CustomerDialog dialog;
    private MyQrCodeController myQrCodeController;
    private RepayBorrowInfoController borrowInfo;
    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            takePhoto = new TakePhotoImpl(MineFragment.this, new TakePhoto.TakeResultListener() {
                @Override
                public void takeSuccess(TResult result) {
                    Log.e("result", result.getImage().getOriginalPath());
                    imgPath = result.getImage().getOriginalPath();
                    bitmap = BitmapFactory.decodeFile(imgPath);
                    mBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                    uploadAvatar.uploadhead(imgPath, mBitmap);
                }

                @Override
                public void takeFail(TResult result, String msg) {
                    ToastAlone.showLongToast(getActivity(), "takeFail:" + msg);
                }

                @Override
                public void takeCancel() {
                    ToastAlone.showLongToast(getActivity(), getResources().getString(com.jph.takephoto.R.string.msg_operation_canceled));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        takePhoto.onCreate(savedInstanceState);
    }

    @Override
    public int setContent() {
        return R.layout.activity_mine;
    }

    @Override
    public int setStatusColor() {
        return R.color.orange;
    }

    IControllerCallBack headCall = new IControllerCallBack() {
        @Override
        public void onSuccess(int returnCode, String value) {
            CommonUtils.closeDialog();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bitmap = BitmapFactory.decodeFile(imgPath);
                    mBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                    head.setImageBitmap(mBitmap);
                }
            });
            getUserStatu();
        }

        @Override
        public void onFail(final String errorMessage) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CommonUtils.closeDialog();
                    ToastAlone.showLongToast(getActivity(), errorMessage);
                    head.setImageResource(R.drawable.img_default);
                }
            });
        }
    };

    @Override
    public void init() {
        borrowInfo = new RepayBorrowInfoController(getActivity(), this);
        dialog = new CustomerDialog(getActivity());
        if (mNoticeDaoDao == null) {
            mNoticeDaoDao = new NoticeDao(getActivity());
        }
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.img_default)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.img_default) // 设置图片Uri为空的时候显示的图片
                .showImageOnFail(R.drawable.img_default) // 设置图片失败的时候显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .build();
        LogUtil.d("进来了吗?", "why");
        if (!TextUtils.isEmpty(SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.USERNAME)))
            tv_phone_num.setText(SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.USERNAME).substring(0, 3)
                    + "*****" + SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.USERNAME).substring(8));
        if (uploadAvatar == null)
            uploadAvatar = new UploadAvatar(getActivity(), headCall);
        messageCotroller = new MessageCotroller(getActivity(), this);
//        mineBorrow = new MineBorrowCotroller(getActivity(), this);
        ((MainActivity) getActivity()).setStatusColor(R.color.orange);
        mine.setPadding(0, 0, 0, 0);
        int noticeBorrowCount = getNoticeCount("2");
        int noticeBonusCount = getNoticeCount("3");
        dialog = new CustomerDialog(getActivity());
        if (IApplication.getInstance().isLogin()) {
            CommonUtils.goToActivity(getActivity(),
                    LoginActivity.class, new Intent().putExtra("from", 0));
        } else {
            //获取用户状态信息
            getUserStatu();
//            borrowInfo.getBorrowInfo();
        }
    }

    ControllerCallBack iController = new ControllerCallBack() {
        @Override
        public void onSuccess(int returnCode, String value) {
            if (!TextUtils.isEmpty(UserInfoManager.getInstance().getUserStatuModels().getUsername())) {
                if (UserInfoManager.getInstance().getUserStatuModels().getUsername().length() > 0) {
                    StringBuffer sb = new StringBuffer();
                    String str = UserInfoManager.getInstance().getUserStatuModels().getUsername();
                    sb.append(str.substring(0, 1));
                    int i = str.length() - 1;
                    while (i-- > 0) {
                        sb.append("*");
                    }
                    name.setText(sb.toString());
                }
            }
            //头像
            if (!TextUtils.isEmpty(UserInfoManager.getInstance().getUserStatuModels().getHeadPic())) {
                ImageLoader.getInstance().displayImage(UserInfoManager.getInstance().getUserStatuModels().getHeadPic(), head, options);
            }

            if (!TextUtils.isEmpty(UserInfoManager.getInstance().getUserStatuModels().getMobile())) {
                tv_phone_num.setText(UserInfoManager.getInstance().getUserStatuModels().getMobile().substring(0, 3)
                        + "*****" + UserInfoManager.getInstance().getUserStatuModels().getMobile().substring(8));
            }
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
    };

    public void getUserStatu() {
        if (myStatu == null) {
            myStatu = new GetMyStatu(getActivity(), iController);
        }
        myStatu.getMystatu();
    }


    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
    }

    public void reayment() {
//        mineBorrow.getMineBorrow("1", "20");
        borrowInfo.getBorrowInfo();
    }

    public boolean checkStatus(String status) {
        switch (Integer.valueOf(status)) {
            case 0:
                ToastUtils.showShort(getActivity(), "借款仍在审核中");
                return false;
            case 1:
                return true;
            case 2:
                ToastUtils.showShort(getActivity(), "借款已结清");
                return false;
            case 9:
                ToastUtils.showShort(getActivity(), "借款未通过，请核对资料");
                return false;
            default:
                break;
        }
        return false;
    }

    @OnClick({R.id.mine_code, R.id.home_message_center, R.id.head, R.id.rl_apply,
            R.id.rl_personal, R.id.rl_loan, R.id.rl_repayment, R.id.rl_earning_bonus, R.id.rl_my_winnings, R.id.rl_setting
            , R.id.rl_performance})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_code:
                if (!TextUtils.isEmpty(UserInfoManager.getInstance().getQr_code_url())) {
                    String code = SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.SHARE_CODE);
                    dialog.qrCode(UserInfoManager.getInstance().getQr_code_url() + "?register_platform=" + CommonalityFieldUtils.getDittchStrN(), code);
                    dialog.show();
                }
                break;
            case R.id.rl_setting:
                CommonUtils.goToActivity(mContext, SettingActivity.class);
                break;
            case R.id.home_message_center:
                Intent intentMessage = new Intent(getActivity(), MessagerCenterActivity.class);
                startActivityForResult(intentMessage, REQUEST_MESSAGE);
                break;
            case R.id.head:
                advaterDialog();
                break;
            case R.id.rl_performance:
                Intent intent = new Intent();
//                intent.putExtra("name", UserInfoManager.getInstance().getUserStatuModels().getUsername());
                CommonUtils.goToActivity(mContext, PerformanceActivity.class, intent);//本月累计业绩页面
                break;
            case R.id.rl_apply:
                Intent intent_apply = new Intent();
                CommonUtils.goToActivity(mContext, MyApplyActivity.class, intent_apply);//我的申请
                break;
            case R.id.rl_personal:
//                UserInfoManager.getInstance().setBorrowInfo(null);
                CommonUtils.goToActivity(getActivity(), PersonalDataActivity.class,
                        new Intent().putExtra(SPKey.ISSHOWAUTHOR, false));
                break;
            case R.id.rl_loan:
                CommonUtils.goToActivity(mContext, MineInstantLoanActivity.class);//我的借款
                break;
            case R.id.rl_repayment:
                reayment();//我要还款
                break;
            case R.id.rl_earning_bonus:
                CommonUtils.goToActivity(mContext, EarnABonusActivity.class);//赚钱奖金
                break;
            case R.id.rl_my_winnings:
                CommonUtils.goToActivity(mContext, MyBonusActivity.class);//我的奖金
                break;
        }
    }


    private void advaterDialog() {
        dialog.advatarDialog(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.cancel) {
                    dialog.dismiss();
                } else if (view.getId() == R.id.tv_picture) {//拍照
                    dialog.dismiss();
                    toPhone();
                } else if (view.getId() == R.id.tv_album) {
                    dialog.dismiss();
                    toGallery();
                }
            }
        });
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takePhoto.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MESSAGE && resultCode == RESULT_CODE) {
            if (!IApplication.getInstance().isLogin()) {
                messageCotroller.getMessageReadCount("1", "20");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        takePhoto.onSaveInstanceState(outState);
    }

    public void resume() {
        if (!TextUtils.isEmpty(SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.USERNAME)))
            tv_phone_num.setText(SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.USERNAME).substring(0, 3)
                    + "*****" + SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.USERNAME).substring(8));
        if (!IApplication.getInstance().isLogin()) {
            if (messageCotroller == null) {
                messageCotroller = new MessageCotroller(getActivity(), this);
            }
            messageCotroller.getMessageReadCount("1", "20");
            getUserStatu();
            if (borrowInfo == null) {
                borrowInfo = new RepayBorrowInfoController(getActivity(), this);
            }
//            borrowInfo.getBorrowInfo();
        }
        boolean isloginCancel = SharedPreferencesUtil.getInstance(getActivity()).getBoolean("isLoginCancel", false);
        if (isloginCancel) {
            SharedPreferencesUtil.getInstance(getActivity()).setBoolean("isLoginCancel", false);
            ((MainActivity) getActivity()).changeToHome();
        }
        if (IApplication.isToHome) {
            IApplication.isToHome = false;
            ((MainActivity) getActivity()).changeToHome();
        }
        if (IApplication.getInstance().isRefresh) {
            getUserStatu();
            IApplication.getInstance().isRefresh = false;
        }
        showView();

    }

    @Override
    public void destroy() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }


    @Override
    public void onSuccess(int returnCode, String returnValue) {
        try {
            if (returnCode == CallBackType.MESSAGE_READ_COUNT) {
                JSONObject jsonObject = new JSONObject(returnValue);
                String count = jsonObject.get("count").toString();
                if (Integer.valueOf(count) > 0) {
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = String.valueOf(count);
                    handler.sendMessage(msg);
                } else {
                    message.setImageDrawable(IApplication.globleResource.getDrawable(R.drawable.icon_message));
                    messageCount.setVisibility(View.INVISIBLE);
                }
            } else if (returnCode == CallBackType.REPAYBORROW_INFO) {
                RepayBorrowInfoBean repayBorrowInfoBean= UserInfoManager.getInstance().getRepayBorrowInfo();
                if (repayBorrowInfoBean==null) {
                    CommonUtils.goToActivity(getActivity(), NoRepaymentActivity.class);
                } else {

                    if (repayBorrowInfoBean.getBorrow_status()==4) {
//                        if (!CommonConstant.PRODUCT_FXDONLINE.equals(loan.getProduct_id())) {
                        if (StringUtil.isNullOrEmpty(H5Token)) {
                            if (StringUtil.isNullOrEmpty(SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.ID_CARD))) {
                                ToastUtils.showShort(getActivity(), "加载失败,请重试！");
                                return;
                            }
                            if (StringUtil.isNullOrEmpty(SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.USERNAME))) {
                                ToastUtils.showShort(getActivity(), "加载失败,请重试！");
                                return;
                            }
                            repayment(SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.ID_CARD), SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.USERNAME), repayBorrowInfoBean.getContract_code());
                        } else {
                            if (checkStatus(H5Status) && !StringUtil.isNullOrEmpty(H5Token)) {
                                Intent i = new Intent();
                                i.putExtra("token", H5Token);
                                i.putExtra("page", H5PageKey.REPAYMENT);
                                i.putExtra("id", repayBorrowInfoBean.getContract_code());
                                CommonUtils.goToActivity(getActivity(), RepaymentActivity.class, i);//我的还款
                            }
                        }
//                        } else {
//                            Intent i = new Intent();
//                            i.putExtra("FXDPRODUCTID", loan.getProduct_id());
//                            CommonUtils.goToActivity(getActivity(), RepaymentActivity.class, i);//我的还款
//                        }
                    } else {
                        CommonUtils.goToActivity(getActivity(), NoRepaymentActivity.class);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.MESSAGE_READ_COUNT) {
            message.setImageDrawable(IApplication.globleResource.getDrawable(R.drawable.icon_message));
            messageCount.setVisibility(View.INVISIBLE);
        } else if (returnCode == CallBackType.MINE_BORROW) {
            CommonUtils.goToActivity(getActivity(), NoRepaymentActivity.class);
        }
    }

    public void repayment(String idCard, String phone, final String id) {
        RepaymentControl.getInstance(getActivity()).setOnCompleteListener(new RepaymentControl.OnCompleteListener() {
            @Override
            public void complete(int status, String msg, String token, String result) {
                try {
//                    if (!CommonConstant.PRODUCT_FXDONLINE.equals(UserInfoManager.getInstance().getRepayBorrowInfo().getProduct_id())) {
                    if (status == 0) {
                        JSONObject json = new JSONObject(result);
                        H5Token = token;
                        H5Status = json.get("userState").toString();
                        if (checkStatus(H5Status) && !StringUtil.isNullOrEmpty(H5Token)) {
                            Intent i = new Intent();
                            i.putExtra("token", token);
                            i.putExtra("page", H5PageKey.REPAYMENT);
                            i.putExtra("id", id);
                            CommonUtils.goToActivity(getActivity(), RepaymentActivity.class, i);//我的还款
                        }
                    } else {
                        ToastUtils.showShort(getActivity(), msg);
                    }
//                    } else {
//                        Intent i = new Intent();
//                        i.putExtra("FXDPRODUCTID", UserInfoManager.getInstance().getRepayBorrowInfo().getProduct_id());
//                        CommonUtils.goToActivity(getActivity(), RepaymentActivity.class, i);//我的还款
//                    }
                } catch (JSONException e) {
                    WyController.uploadAppLog(getActivity(), "还款登录", NetContants.REPAYMENT_LOGIN, e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        RepaymentControl.getInstance(getActivity()).requestRepayment(idCard, phone);
    }

    private int getNoticeCount(String type) {
        int i = 0;
        if (mNoticeDaoDao.isExist(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID), type)) {
            List<Notice> notices = mNoticeDaoDao.queryNoices();
            for (Notice bean : notices) {
                if (bean.getUserId().equals(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID)) && bean.getType().equals(type)) {
                    i++;
                }
            }
        }
        return i;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastAlone.showLongToast(getActivity(), msg.obj.toString());
                    break;
                case 2:
                    if (message != null) {
                        message.setImageDrawable(IApplication.globleResource.getDrawable(R.drawable.icon_messageh));
                        messageCount.setVisibility(View.INVISIBLE);
                        messageCount.setText(String.valueOf(msg.obj.toString()));
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    private CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(400).setOutputY(300);
        builder.setWithOwnCrop(true);
        return builder.create();
    }


    private void handleFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        imageUri = Uri.fromFile(file);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void toPhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasCamera = getActivity().checkSelfPermission(Manifest.permission.CAMERA);
            int hasWsd = getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasCamera == PackageManager.PERMISSION_GRANTED && hasWsd == PackageManager.PERMISSION_GRANTED) {
                handleFile();
                takePhoto.setTakePhotoOptions(new TakePhotoOptions.Builder().setWithOwnGallery(true).create());
                CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
                takePhoto.onEnableCompress(compressConfig, true);
                takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            }
        } else {
            handleFile();
            takePhoto.setTakePhotoOptions(new TakePhotoOptions.Builder().setWithOwnGallery(true).create());
            CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
            takePhoto.onEnableCompress(compressConfig, true);
            takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            boolean flag = true;
            for (int i : grantResults) {
                if (i == PackageManager.PERMISSION_DENIED) {
                    flag = false;
                }
            }
            if (flag) {
                toPhone();
            } else {
                ToastAlone.showShortToast(getContext(), "拍照权限被拒绝");
            }
        }
    }

    private void toGallery() {
        handleFile();
        takePhoto.setTakePhotoOptions(new TakePhotoOptions.Builder().setWithOwnGallery(true).create());
        CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig, true);
        takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
    }

    public void showView() {
        if (CustomerManagerManager.isCustomerManager()) {
            performance.setVisibility(View.VISIBLE);
            tv_customer_manager.setVisibility(View.VISIBLE);
            apply.setVisibility(View.VISIBLE);
            personal.setVisibility(View.GONE);
            loan.setVisibility(View.GONE);
            repayment.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            ll_loan.setVisibility(View.GONE);
        } else {
            performance.setVisibility(View.GONE);
            apply.setVisibility(View.GONE);
            tv_customer_manager.setVisibility(View.GONE);
            personal.setVisibility(View.VISIBLE);
            loan.setVisibility(View.VISIBLE);
            repayment.setVisibility(View.VISIBLE);
            message.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            ll_loan.setVisibility(View.VISIBLE);
        }
    }

}
