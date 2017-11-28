package com.two.emergencylending.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chinazyjr.lib.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.megvii.livenesslib.LivenessActivity;
import com.megvii.livenesslib.util.ConUtil;
import com.refresh.PullToRefreshBase;
import com.refresh.PullToRefreshScrollView;
import com.two.emergencylending.activity.ConfirmationOfLoanActivity;
import com.two.emergencylending.activity.MobileAuthenticaActivity;
import com.two.emergencylending.activity.PersonalDataActivity;
import com.two.emergencylending.activity.ZhiMaActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseFragment;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.bean.UserStatuModel;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.CommonConstant;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.FaceVerifyController;
import com.two.emergencylending.controller.GetCredentialState;
import com.two.emergencylending.controller.GetMyStatu;
import com.two.emergencylending.controller.OffLinewSubJXLController;
import com.two.emergencylending.manager.BorrowStatusManager;
import com.two.emergencylending.manager.CustomerManagerManager;
import com.two.emergencylending.permission.ToolPermission;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.zyjr.emergencylending.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.zyjr.emergencylending.R.drawable.icon_verfail;
import static com.zyjr.emergencylending.R.drawable.icon_vering;

/**
 * 信息认证
 */

public class AuthenticateFragment extends BaseFragment implements ControllerCallBack, View.OnClickListener {
    @Bind(R.id.ll_face_recognition)
    LinearLayout faceRecognition;
    @Bind(R.id.certificate_status_text)
    TextView certificateStatusText;
    //    @Bind(R.id.certificate_status)
//    ImageView certificateStatus;
    @Bind(R.id.face_status_text)
    TextView faceStatusText;
    //    @Bind(R.id.face_status)
//    ImageView certificateFaceStatus;
//    @Bind(R.id.face_lock)
//    ImageView face_lock;
    @Bind(R.id.btn_submit)
    Button submit;

    @Bind(R.id.ll_none_authenticate)
    LinearLayout ll_none_authenticate;
    @Bind(R.id.ll_phone_authenticate)
    LinearLayout ll_phone_authenticate;

    GetCredentialState mGetCredentialState;
    GetMyStatu getMyStatu;
    //    BorrowStatusCotroller borrowStatusCotroller;
    PullToRefreshScrollView pullToRefreshScrollView;
    CustomerDialog dialog;
    @Bind(R.id.ll_zhima_authenticate)
    LinearLayout llZhimaAuthenticate;
    @Bind(R.id.zhima_status_text)
    TextView zhimaStatusText;
    private String uuid;
    //    private String renewLoans;
    private String productId;
    public static final int STATUS_UNAUTHORIZED = 0;
    public static final int STATUS_AUTHENTICATING = STATUS_UNAUTHORIZED + 1;
    public static final int STATUS_AUTHENTICATION_FAIL = STATUS_AUTHENTICATING + 1;
    public static final int STATUS_AUTHENTICATION_SUCCESS = STATUS_AUTHENTICATION_FAIL + 1;
    private int mobileStatus = STATUS_UNAUTHORIZED;
    private int faceStatus = STATUS_UNAUTHORIZED;
    private int zhimaStatus = STATUS_UNAUTHORIZED;
    //返回码
    private final int RESULT_CODE = 100;
    //请求码
    private final int REQUEST_CODE = 101;
    //返回码
    private final int PAGE_INTO_LIVENESS = 102;
    //返回码
    private final int RESULT_CODE_FAIL = 103;

    private boolean isAuthorizationSuccess = false;
    private boolean isFirstAuthorization = true;
    private String mName = "";
    private String mIdCard = "";
    OffLinewSubJXLController offLinewSub;
    @Bind(R.id.tv_text1_hint)
    TextView tv_text1_hint;
    private GetMyStatu statu;

    @Override
    protected void lazyLoad() {

    }

    @Override
    public int setContent() {
        return R.layout.layout_infor_certificate2;
    }

    @Override
    public void init() {
        uuid = ConUtil.getUUIDString(getContext());
        statu = new GetMyStatu(getActivity(), new ControllerCallBack() {
            @Override
            public void onSuccess(int returnCode, String value) {
            }

            @Override
            public void onFail(int returnCode, final String errorMessage) {
            }
        });
        statu.getMystatu();
        offLinewSub = new OffLinewSubJXLController(getActivity(), this);
        mGetCredentialState = new GetCredentialState(getActivity(), this);
        pullToRefreshScrollView = (PullToRefreshScrollView) root.findViewById(R.id.refresh);
        dialog = new CustomerDialog(getActivity());
//        mGetCredentialState.getState();
        refreshStatusShow();
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mGetCredentialState.getState();
            }
        });
    }

    @Override
    public void resume() {
        pullToRefreshScrollView.autoRefresh();
    }

    @Override
    public void destroy() {

    }

    @OnClick({R.id.ll_phone_authenticate, R.id.ll_face_recognition, R.id.btn_submit, R.id.ll_zhima_authenticate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_face_recognition:
                if (faceStatus != STATUS_AUTHENTICATION_SUCCESS) {
                    if (zhimaStatus != STATUS_AUTHENTICATION_SUCCESS) {
                        ToastUtils.showShort(IApplication.globleContext, "请先完成芝麻信用认证！");
                        return;
                    }
                    if (mobileStatus == STATUS_AUTHENTICATION_SUCCESS) {
//                        mGetCredentialState.saveFaceState();//屏蔽人脸识别 测试使用，生产请屏蔽
                        if (isAuthorizationSuccess) {
                            getStatusAgain(mName, mIdCard);
                        } else {
                            netWorkWarranty();
                        }
                    } else {

                        ToastUtils.showShort(IApplication.globleContext, "请先完成手机认证！");
                    }
                } else {
                    ToastUtils.showShort(IApplication.globleContext, "人脸识别已认证成功！");
                }
                break;
            case R.id.ll_phone_authenticate:
                if (zhimaStatus != STATUS_AUTHENTICATION_SUCCESS) {
                    ToastUtils.showShort(IApplication.globleContext, "请先完成芝麻信用认证！");
                    return;
                }
                if (mobileStatus == STATUS_UNAUTHORIZED || mobileStatus == STATUS_AUTHENTICATION_FAIL) {
                    CommonUtils.goToActivity(getActivity(), MobileAuthenticaActivity.class);
                } else if (mobileStatus == STATUS_AUTHENTICATING) {
                    ToastUtils.showShort(IApplication.globleContext, "认证中，请耐心等待!");
                } else if (mobileStatus == STATUS_AUTHENTICATION_SUCCESS) {
                    ToastUtils.showShort(IApplication.globleContext, "手机认证已认证成功!");
                }
                break;
            case R.id.ll_zhima_authenticate:
                if (zhimaStatus == STATUS_UNAUTHORIZED) {//未认证
                    CommonUtils.goToActivity(getActivity(), ZhiMaActivity.class);
                } else if (zhimaStatus == STATUS_AUTHENTICATION_SUCCESS) {
                    ToastUtils.showShort(IApplication.globleContext, "芝麻信用已认证成功!");
                }
                break;
            case R.id.btn_submit:
                if (!CommonConstant.PRODUCT_OFFLINE.equals(productId)) {
                    if (zhimaStatus == STATUS_AUTHENTICATION_SUCCESS && mobileStatus == STATUS_AUTHENTICATION_SUCCESS && faceStatus == STATUS_AUTHENTICATION_SUCCESS) {
                        Intent intent = new Intent(getContext(), ConfirmationOfLoanActivity.class);
                        intent.putExtra("data", productId);
                        startActivityForResult(intent, REQUEST_CODE);
                    } else {
                        ToastUtils.showShort(IApplication.globleContext, "请完成所有认证后再提交");
                    }
                } else if (productId.equals(CommonConstant.PRODUCT_OFFLINE)) {
                    if (CustomerManagerManager.isCustomerManager()) {
                        offLinewSub.offlineSubMitCustomer(PersonalDataActivity.custId);
                    } else {
                        offLinewSub.offlineSubMit();
                    }
                }
                break;
            case R.id.cancel:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        pullToRefreshScrollView.onRefreshComplete();
        if (returnCode == CallBackType.CREDENTIAL_STATE) {
//            if (StringUtil.isNullOrEmpty(value)) {
//                dialog.showOrderFailed(getActivity(), this);
//                dialog.show();
//            } else {
            initStatus(value);
//            }
        } else if (returnCode == CallBackType.CREDENTIAL_FACE_STATE) {
            initFaceStatus("1");
        } else if (returnCode == CallBackType.FACE_VERIFY) {
            mGetCredentialState.saveFaceState();
        } else if (returnCode == CallBackType.BORROW_STATUS) {
            secletBorrowInfo(value);
        } else if (returnCode == CallBackType.GET_USER_STATU_FROM_AUTH) {
        } else if (returnCode == CallBackType.OFFLINE_SKIP_JXL) {
            Intent intent = new Intent(getContext(), ConfirmationOfLoanActivity.class);
            intent.putExtra("data", productId);
            if (CustomerManagerManager.isCustomerManager()) {
                intent.putExtra("custId", PersonalDataActivity.custId);
            }
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        pullToRefreshScrollView.onRefreshComplete();
        if (returnCode == CallBackType.CREDENTIAL_STATE || returnCode == CallBackType.CREDENTIAL_FACE_STATE ||
                returnCode == CallBackType.FACE_VERIFY || returnCode == CallBackType.BORROW_STATUS
                || returnCode == CallBackType.OFFLINE_SKIP_JXL
                ) {
            ToastUtils.showShort(IApplication.globleContext, errorMessage);
        }
    }

    //给TextView设置部分颜色
    public void setPartialColor(TextView tv, int start, int end, int textColor) {
        String s = tv.getText().toString();
        Spannable spannable = new SpannableString(s);
        spannable.setSpan(new ForegroundColorSpan(textColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannable);
    }

    public void initStatus(String value) {
        try {
            String authStatus = "0";
            String faceStatus = "0";
            String zhimaStatus = "0";
            mName = SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.NAME);
            mIdCard = SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.ID_CARD);
            JSONObject jsonRes = new JSONObject(value);
            productId = jsonRes.getString("product_id");
            if (!isAuthorizationSuccess) {
                refreshStatusShow();
            }
            String authResult = jsonRes.getString("auth_result");
            JSONArray jsonArray = new JSONArray(authResult);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.get("authType").toString().equals("4")) {
                    zhimaStatus = jsonObject.get("authStatus").toString();
                    initZhiMaStatus(zhimaStatus);
                } else if (jsonObject.get("authType").toString().equals("1")) {
                    authStatus = jsonObject.get("authStatus").toString();
                    UserInfoManager.getInstance().getUserStatuModels().setUserAuthStatusShouji(authStatus);
                    initPhoneStatus(authStatus);
                } else if (jsonObject.get("authType").toString().equals("3")) {
                    faceStatus = jsonObject.get("authStatus").toString();
                    initFaceStatus(faceStatus);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refreshStatusShow() {
        if (!StringUtil.isNullOrEmpty(productId)) {
            if (!CommonConstant.PRODUCT_OFFLINE.equals(productId)) {
                faceRecognition.setVisibility(View.VISIBLE);
                ll_none_authenticate.setVisibility(View.GONE);
                ll_phone_authenticate.setVisibility(View.VISIBLE);
                llZhimaAuthenticate.setVisibility(View.VISIBLE);
                netWorkWarranty();
            } else if (productId.equals(CommonConstant.PRODUCT_OFFLINE)) {
                faceRecognition.setVisibility(View.GONE);
                tv_text1_hint.setText("请按“提交”后等待短信通知。");
                setPartialColor(tv_text1_hint, 3, 5, getResources().getColor(R.color.yj));
//                String html = "请按“<font color='#ffa16e'>提交</font>”后等待短信通知。";
//                tv_text1_hint.setText(Html.fromHtml(html));
                ll_phone_authenticate.setVisibility(View.GONE);
                llZhimaAuthenticate.setVisibility(View.GONE);
                ll_none_authenticate.setVisibility(View.VISIBLE);
            }
        }
    }

    public void initZhiMaStatus(String status) {
        if (!StringUtil.isNullOrEmpty(status)) {
            if (Integer.parseInt(status) != 5) {
                zhimaStatus = STATUS_UNAUTHORIZED;
                zhimaStatusText.setText("未认证");
                zhimaStatusText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_unver), null);
            } else {
                zhimaStatus = STATUS_AUTHENTICATION_SUCCESS;
                zhimaStatusText.setText("认证成功");
                zhimaStatusText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_vers), null);
            }
        }
    }

    public void initPhoneStatus(String status) {
        if (!StringUtil.isNullOrEmpty(status)) {
            if (Integer.parseInt(status) == 0) {
                mobileStatus = STATUS_UNAUTHORIZED;
                certificateStatusText.setText("未认证");
                certificateStatusText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_unver), null);
//                certificateStatus.setImageResource(R.drawable.icon_unver);
            } else if (Integer.parseInt(status) == 7) {
                mobileStatus = STATUS_AUTHENTICATING;
                certificateStatusText.setText("认证中");
//                certificateStatus.setImageResource(R.drawable.icon_vering);
                certificateStatusText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(icon_vering), null);
            } else if (Integer.parseInt(status) == 8) {
                mobileStatus = STATUS_AUTHENTICATION_FAIL;
                certificateStatusText.setText("认证失败");
//                certificateStatus.setImageResource(R.drawable.icon_verfail);
                certificateStatusText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(icon_verfail), null);
            } else if (Integer.parseInt(status) == 9) {
                mobileStatus = STATUS_AUTHENTICATION_SUCCESS;
                certificateStatusText.setText("认证成功");
//                certificateStatus.setImageResource(R.drawable.icon_vers);
                certificateStatusText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_vers), null);
            }
        }
    }

    public void initFaceStatus(String status) {
        if (!StringUtil.isNullOrEmpty(status)) {
            if (Integer.parseInt(status) == 0) {
                faceStatus = STATUS_UNAUTHORIZED;
                faceStatusText.setText("未认证");
//                face_lock.setImageResource(R.drawable.icon_lock);
//                certificateFaceStatus.setImageResource(R.drawable.icon_unver);
                faceStatusText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_unver), null);
            } else if (Integer.parseInt(status) == 2) {
                faceStatus = STATUS_AUTHENTICATION_FAIL;
                faceStatusText.setText("认证失败");
//                face_lock.setImageResource(R.drawable.icon_lock);
//                certificateFaceStatus.setImageResource(icon_verfail);
                faceStatusText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(icon_verfail), null);
            } else if (Integer.parseInt(status) == 1) {
                faceStatus = STATUS_AUTHENTICATION_SUCCESS;
                faceStatusText.setText("认证成功");
//                face_lock.setImageResource(R.drawable.icon_unlock);
//                certificateFaceStatus.setImageResource(R.drawable.icon_vers);
                faceStatusText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_vers), null);
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            ((PersonalDataActivity) getActivity()).setComplete(4);
            ((PersonalDataActivity) getActivity()).isOnClik = true;
            ((PersonalDataActivity) getActivity()).swithFragment(ApplyEndFragment.class);
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE_FAIL) {
            getActivity().finish();
        } else {
            if (requestCode == PAGE_INTO_LIVENESS && resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String resultOBJ = bundle.getString("result");
//                MyLog.i("keey", "result:" + result);
//            try {
//                JSONObject json = new JSONObject(result);
//                final FaceIDDataStruct idDataStruct = new Gson().fromJson(json.get("data").toString(), new TypeToken<FaceIDDataStruct>() {
//                }.getType());
//                final String errorString = json.optString("result");
//                if (idDataStruct != null && errorString.equals(getResources().getString(com.megvii.livenesslib.R.string.verify_success))) {
//                    FaceVerifyController faceVerifyController = new FaceVerifyController(getActivity(), this);
//                    faceVerifyController.imageVerify(idDataStruct, mName, mIdCard);
//                } else {
//                    ToastUtils.showShort(IApplication.globleContext, "人脸识别失败，请重新识别");
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
                try {
                    JSONObject result = new JSONObject(resultOBJ);
                    int resID = result.getInt("resultcode");
                    if (resID == R.string.verify_success) {
                        doPlay(R.raw.meglive_success);
                    } else if (resID == R.string.liveness_detection_failed_not_video) {
                        doPlay(R.raw.meglive_failed);
                    } else if (resID == R.string.liveness_detection_failed_timeout) {
                        doPlay(R.raw.meglive_failed);
                    } else if (resID == R.string.liveness_detection_failed) {
                        doPlay(R.raw.meglive_failed);
                    } else {
                        doPlay(R.raw.meglive_failed);
                    }
                    boolean isSuccess = result.getString("result").equals(
                            getResources().getString(R.string.verify_success));
                    if (isSuccess) {
                        String delta = bundle.getString("delta");
                        Map<String, byte[]> images = (Map<String, byte[]>) bundle.getSerializable("images");
                        FaceVerifyController faceVerifyController = new FaceVerifyController(getActivity(), this);
                        faceVerifyController.imageVerify(images, delta, mName, mIdCard);
                    } else {
                        ToastUtils.showShort(IApplication.globleContext, "人脸识别失败，请重新识别");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private MediaPlayer mMediaPlayer = null;

    private void doPlay(int rawId) {
        if (mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();

        mMediaPlayer.reset();
        try {
            AssetFileDescriptor localAssetFileDescriptor = getResources()
                    .openRawResourceFd(rawId);
            mMediaPlayer.setDataSource(
                    localAssetFileDescriptor.getFileDescriptor(),
                    localAssetFileDescriptor.getStartOffset(),
                    localAssetFileDescriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception localIOException) {
            localIOException.printStackTrace();
        }
    }

    private void netWorkWarranty() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(getActivity());
                LivenessLicenseManager licenseManager = new LivenessLicenseManager(
                        getActivity());
                manager.registerLicenseManager(licenseManager);
                manager.takeLicenseFromNetwork(uuid);
                if (licenseManager.checkCachedLicense() > 0)
                    mHandler.sendEmptyMessage(1);
                else
                    mHandler.sendEmptyMessage(2);
            }
        }).start();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (isFirstAuthorization) {
                        isFirstAuthorization = false;
                    }
                    isAuthorizationSuccess = true;
                    break;
                case 2:
                    isAuthorizationSuccess = false;
                    if (!isFirstAuthorization) {
                        ToastUtils.showShort(IApplication.globleContext, "授权失败，请重新授权！");
                    }
                    break;
            }
        }
    };


    private void secletBorrowInfo(String value) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(value);
            String borrowStatus = jsonObject.getString("BorrowStatus");
            int status = BorrowStatusManager.getBorrowStatus(borrowStatus);
            if (status == BorrowStatusManager.PAGE_STATUS_LOAN) {
//                dialog.showOrderFailed(getActivity(), this);
//                dialog.show();
            } else {
                Intent intent = new Intent(getContext(), ConfirmationOfLoanActivity.class);
                intent.putExtra("data", productId);
                startActivityForResult(intent, REQUEST_CODE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getStatusAgain(String userName, String idCard) {
        if (StringUtil.isNullOrEmpty(userName) || StringUtil.isNullOrEmpty(idCard)) {
            getMyStatu = new GetMyStatu(getActivity(), new ControllerCallBack() {
                @Override
                public void onSuccess(int returnCode, String value) {
                    if (!StringUtil.isNullOrEmpty(value)) {
                        UserStatuModel userStatu = new Gson().fromJson(value, new TypeToken<UserStatuModel>() {
                        }.getType());
                        String name = userStatu.getUsername().toString();
                        String idCard = userStatu.getIdcard().toString();
                        mName = name;
                        mIdCard = idCard;
                        if (StringUtil.isNullOrEmpty(mName) || StringUtil.isNullOrEmpty(mIdCard)) {
                            ToastUtils.showShort(IApplication.globleContext, "资料有误");
                        } else {
                            if (ToolPermission.checkSelfPermission(getActivity(), AuthenticateFragment.this,
                                    new String[]{
                                            Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, "请允许权限进行拍照", 1001)) {
                                Intent intent = new Intent(getActivity(), LivenessActivity.class);
                                //人脸识别
                                startActivityForResult(intent, PAGE_INTO_LIVENESS);
                            } else {
                                ToastAlone.showShortToast(getContext(), "请允许权限进行拍照");
                            }
                        }
                    } else {
                        ToastUtils.showShort(IApplication.globleContext, "识别资料有误");
                    }
                }

                @Override
                public void onFail(int returnCode, String errorMessage) {
                    ToastUtils.showShort(IApplication.globleContext, errorMessage);
                }
            });
            getMyStatu.getMystatuFromAuth();
        } else {
            if (ToolPermission.checkSelfPermission(getActivity(), AuthenticateFragment.this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, "请允许权限进行拍照", 1001)) {
                Intent intent = new Intent(getActivity(), LivenessActivity.class);
                //人脸识别
                startActivityForResult(intent, PAGE_INTO_LIVENESS);
            } else {
                ToastAlone.showShortToast(getContext(), "请允许权限进行拍照");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
