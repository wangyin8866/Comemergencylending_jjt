package com.two.emergencylending.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chinazyjr.lib.util.ToastUtils;
import com.example.getlimtlibrary.builder.utils.MyLog;
import com.example.getlimtlibrary.utils.IDCard;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.two.emergencylending.activity.PersonalDataActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.backcard.BankCardUtils;
import com.two.emergencylending.base.BaseFragment;
import com.two.emergencylending.bean.CityModel1;
import com.two.emergencylending.bean.CodeBean;
import com.two.emergencylending.bean.DistrictModel;
import com.two.emergencylending.bean.OfflineBean;
import com.two.emergencylending.bean.OpenBank;
import com.two.emergencylending.bean.PerSonalBean;
import com.two.emergencylending.bean.ProvinceModel;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.AppConfig;
import com.two.emergencylending.constant.BuryAction;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.CommonConstant;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.CustomerManagerDataController;
import com.two.emergencylending.controller.GetProductIDController;
import com.two.emergencylending.controller.GetUserDataControlller;
import com.two.emergencylending.controller.IControllerCallBack;
import com.two.emergencylending.controller.IDcardController;
import com.two.emergencylending.controller.PersonalDataSavaController;
import com.two.emergencylending.controller.UploadController;
import com.two.emergencylending.idcard.IdcardUtils;
import com.two.emergencylending.interfaces.DismissListener;
import com.two.emergencylending.manager.CustomerManagerManager;
import com.two.emergencylending.permission.ToolPermission;
import com.two.emergencylending.popupwindow.CityPopupWindow;
import com.two.emergencylending.popupwindow.OneSelectPopupWindow;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.ImageHandleUtil;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.RegularExpUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.utils.ToolImage;
import com.zyjr.emergencylending.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

import static com.two.emergencylending.idcard.IdcardUtils.INTO_IDCARDSCAN_PAGE;
import static com.two.emergencylending.utils.CommonUtils.provinceList;
import static com.two.emergencylending.utils.ToolImage.comp;
import static com.zyjr.emergencylending.R.id.ed_detailed_address;

/**
 * 个人资料
 */

public class PerSonalFragment extends BaseFragment implements TakePhoto.TakeResultListener, ControllerCallBack {
    @Bind(R.id.et_personal_name)
    TextView etPersonalName;
    @Bind(R.id.et_peronal_idcard)
    TextView etPeronalIdcard;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(ed_detailed_address)
    TextView edDetailedAddress;
    @Bind(R.id.tv_residence_address)
    TextView tvResidenceAddress;
    @Bind(R.id.ed_residence_detailed_address)
    EditText edResidenceDetailedAddress;
    @Bind(R.id.ed_bankno)
    EditText edBankno;
    @Bind(R.id.tv_openbank)
    TextView tvOpenbank;
    @Bind(R.id.ed_pesonal_phone)
    EditText edPesonalPhone;
    private PerSonalBean perSonalBean = new PerSonalBean();
    private String home = "";
    private String live = "";
    private String job_address = "";
    private int bankName = -1;
    String filePath = null;
    private TakePhoto takePhoto;
    /**
     * 当前省的名称
     */
    public String mCurrentProviceName = "";
    public String mLiveProviceName = "";
    /**
     * 当前市的名称
     */
    public String mCurrentCityName = "";
    public String mLiveCityName = "";
    public int mEducationIndex = -1;//当前学历的名称
    public int marriageStatuIndex = -1;//当前学历的名称
    public int mlive_StatuIndex = -1;//当前学历的名称

    /**
     * 当前区的名称
     */
    public String mCurrentDistrictName = "";
    public String mLiveDistrictName = "";
    private StringBuilder residenceValue = new StringBuilder();

    @Bind(R.id.tv_education)
    TextView tv_education;
    @Bind(R.id.tv_marriage)
    TextView tv_marriage;
    @Bind(R.id.tv_livestatu)
    TextView tv_livestatu;
    @Bind(R.id.imageView3)
    ImageView imageView3;
    @Bind(R.id.imageView2)
    ImageView imageView2;
    @Bind(R.id.iv_self)
    ImageView iv_self;
    @Bind(R.id.ed_job_detailed_address)
    EditText ed_job_detailed_address;
    @Bind(R.id.tv_job_address)
    TextView tv_job_address;

    private int mWidth;
    private int mHeight;
    private String path = "";
    private boolean isGetUserDate = false, isGetStore = false;
    CustomerDialog dialog;
    private String product_type;
    private boolean isChange;
    private String imgPath = "";
    private GetProductIDController getProductID = null;
    PersonalDataSavaController personalDataSava;
    private GetUserDataControlller getUserData;
    private UploadController uploadController;
    private CustomerManagerDataController customerManagerDataController;
    //    public static String product_id = "0";
    //身份证正则
    private static String idRex = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";

    @Override
    protected void lazyLoad() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            takePhoto = new TakePhotoImpl(this, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        takePhoto.onCreate(savedInstanceState);
    }

    @Override
    public int setContent() {
        return R.layout.layout_pesonal;
    }

    Bitmap mBitmapIDcardZ, mBitmapIDcardF, mBitmapSelfHand;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11:
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                case 22:
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                case 3:
                    CommonUtils.closeDialog();
                    ToastAlone.showLongToast(getActivity(), msg.obj.toString());
                    break;
                case UploadController.idcardZ:
                    if (msg.arg1 == 1) {
                        perSonalBean.setIdCardZId(UserInfoManager.getInstance().getIdCardZId());
                        mBitmapIDcardZ = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path), mWidth, mHeight, true);
                        imageView3.setImageBitmap(mBitmapIDcardZ);
                    } else if (msg.arg1 == 2) {
                        imageView3.setImageResource(R.drawable.idcard_front);
                        ToastAlone.showLongToast(getContext(), (String) msg.obj);
                    }
                    break;
                case UploadController.idcardF:
                    if (msg.arg1 == 1) {
                        perSonalBean.setIdCardFId(UserInfoManager.getInstance().getIdCardFId());
                        mBitmapIDcardF = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path), mWidth, mHeight, true);
                        imageView2.setImageBitmap(mBitmapIDcardF);
                    } else if (msg.arg1 == 2) {
                        imageView2.setImageResource(R.drawable.idcard_back);
                        ToastAlone.showLongToast(getContext(), (String) msg.obj);
                    }
                    break;
                case UploadController.selfhand:
                    if (msg.arg1 == 1) {
                        perSonalBean.setIdCardHandId(UserInfoManager.getInstance().getIdCardHandId());
                        perSonalBean.setIdcard_hand(UserInfoManager.getInstance().getSelfHand());
                        mBitmapSelfHand = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(filePath), mWidth, mHeight, true);
                        iv_self.setImageBitmap(mBitmapSelfHand);
                    } else if (msg.arg1 == 2) {
                        LogUtil.e("wy","handler");
                        iv_self.setImageResource(R.drawable.idcard_bahold);
                        ToastAlone.showLongToast(getContext(), (String) msg.obj);
                    }
                    break;
                case 55:
                    ToastAlone.showLongToast(getContext(), (String) msg.obj);
                    break;
                case 56:
                    ToastAlone.showLongToast(getContext(), (String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void init() {
        dialog = new CustomerDialog(getActivity());
        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.idcard_front);
        mWidth = d.getMinimumWidth();
        mHeight = d.getMinimumHeight();
        Log.d("idcard", mWidth + "-----" + mHeight);
        IdcardUtils.getInstance().init(getActivity().getApplicationContext());
        //BankCardUtils.getInstance().init(getActivity());
        perSonalBean = new PerSonalBean();
        CommonUtils.addressDatas(getContext());
        getProductID = new GetProductIDController(getActivity(), this);
        uploadController = new UploadController(getActivity(), new UploadController.Controller() {

            @Override
            public void onSuccess(final int type, String value) {
                CommonUtils.closeDialog();
                Message msg = new Message();
                msg.what = type;
                msg.arg1 = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void onFail(final int type, final String errorMessage) {
                CommonUtils.closeDialog();
                Message msg = new Message();
                msg.what = type;
                msg.arg1 = 2;
                msg.obj = errorMessage;
                handler.sendMessage(msg);
            }
        });
        customerManagerDataController = new CustomerManagerDataController(getActivity(), this);
        personalDataSava = new PersonalDataSavaController(getActivity(), this);
        getUserData = new GetUserDataControlller(getActivity(), this);
        if (CustomerManagerManager.isCustomerManager()) {
            customerManagerDataController.getCustomeerData(PersonalDataActivity.custId);
        } else {
            getUserData.getuserPersonalData();
        }


    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void resume() {
        if (savedInstanceState != null) {
            filePath = savedInstanceState.getString("filePath");
        }
    }


    @Override
    public void destroy() {
        if (idCardz != null && !idCardz.isRecycled()) {
            idCardz.recycle();
            idCardz = null;
        }
        if (idCardf != null && !idCardf.isRecycled()) {
            idCardf.recycle();
            idCardf = null;
        }
        if (seleHandBitmap != null && !seleHandBitmap.isRecycled()) {
            seleHandBitmap.recycle();
            seleHandBitmap = null;
        }
        if (mBitmapIDcardZ != null && !mBitmapIDcardZ.isRecycled()) {
            mBitmapIDcardZ.recycle();
            mBitmapIDcardZ = null;
        }
        if (mBitmapIDcardF != null && !mBitmapIDcardF.isRecycled()) {
            mBitmapIDcardF.recycle();
            mBitmapIDcardF = null;
        }
        if (mBitmapSelfHand != null && !mBitmapSelfHand.isRecycled()) {
            mBitmapSelfHand.recycle();
            mBitmapSelfHand = null;
        }

    }

    @OnClick({
            R.id.ll_education_type, R.id.tv_education, R.id.education_type,
            R.id.ll_marriage, R.id.tv_marriage, R.id.iv_marriage,
            R.id.ll_livestatu, R.id.tv_livestatu, R.id.iv_livestatu,
            R.id.ll_idcard_fornt, R.id.ll_idcard_back, R.id.ll_selfie,
            R.id.tv_address, R.id.iv_huijiaddress,
            R.id.ll_huijiAddress_type,
            R.id.tv_residence_address,
            R.id.iv_residence_address,
            R.id.ll_residence_address_type,
            R.id.btn_personal_sava,
            R.id.ll_job_address,
            R.id.tv_job_address,
            R.id.iv_job_address,
            R.id.iv_photo,
            R.id.ll_openBank, R.id.iv_question_bank
    })
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_education:
            case R.id.education_type:
            case R.id.ll_education_type:
                OneSelectPopupWindow eduWindow = new OneSelectPopupWindow(getActivity(), AppConfig.degree());
                eduWindow.setOnSelectPopupWindow(new OneSelectPopupWindow.onSelectPopupWindow() {
                    @Override
                    public void onSelectClick(int index, CodeBean select) {
                        perSonalBean.setEducation(select.getCode());
                        tv_education.setText(select.getName());
                    }
                });
                eduWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ll_marriage:
            case R.id.tv_marriage:
            case R.id.iv_marriage:
                OneSelectPopupWindow residence_type = new OneSelectPopupWindow(getActivity(), AppConfig.marriageStatus());
                residence_type.setOnSelectPopupWindow(new OneSelectPopupWindow.onSelectPopupWindow() {
                    @Override
                    public void onSelectClick(int index, CodeBean select) {
                        perSonalBean.setMarriage(select.getCode());
                        tv_marriage.setText(select.getName());
                    }
                });
                residence_type.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_livestatu:
            case R.id.iv_livestatu:
            case R.id.ll_livestatu:
                OneSelectPopupWindow liveStatu = new OneSelectPopupWindow(getActivity(), AppConfig.liveStatu());
                liveStatu.setOnSelectPopupWindow(new OneSelectPopupWindow.onSelectPopupWindow() {
                    @Override
                    public void onSelectClick(int index, CodeBean select) {
                        perSonalBean.setLive_status(select.getCode());
                        tv_livestatu.setText(select.getName());
                    }
                });
                liveStatu.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_personal_sava:
                submit();
                break;
            case R.id.ll_idcard_fornt:
                if (ToolPermission.checkSelfPermission(
                        getActivity(), this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, "请允许权限进行拍照", 1000)) {
                    intent = IdcardUtils.getInstance().getIdCardIntent(getActivity(), 0, false);
                    startActivityForResult(intent, INTO_IDCARDSCAN_PAGE);
                } else {
                    ToastAlone.showShortToast(getContext(), "请允许权限进行拍照");
                }
                break;
            case R.id.ll_idcard_back:
                if (ToolPermission.checkSelfPermission(
                        getActivity(), this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, "请允许权限进行拍照", 1001)) {
                    intent = IdcardUtils.getInstance().getIdCardIntent(getActivity(), 1, false);
                    startActivityForResult(intent, INTO_IDCARDSCAN_PAGE);
                } else {
                    ToastAlone.showShortToast(getContext(), "请允许权限进行拍照");
                }
                break;
            case R.id.ll_selfie:
                dialog.showPhotoTip(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        toPhone(101);
                    }
                });
                dialog.show();
                break;
            case R.id.ll_job_address:
            case R.id.iv_job_address:
            case R.id.tv_job_address:
                if (StringUtil.isNullOrEmpty(etPeronalIdcard.getText().toString().trim())) {
                    ToastAlone.showShortToast(getContext(), "请先识别身份证！");
                    return;
                }
                CityPopupWindow job_address = new CityPopupWindow(getActivity(), CommonUtils.mProvinceDatas, CommonUtils.mCitisDatasMap, CommonUtils.mDistrictDatasMap);
                job_address.setOnCityPopupWindow(new CityPopupWindow.OnCityPopupWindow() {

                    @Override
                    public void onCityClick(String province, int privinceItem, String city, int cityItem, String district, int districtItem) {
                        if (residenceValue.length() > 0) {
                            residenceValue.delete(0, residenceValue.length());
                        }
                        PerSonalFragment.this.mCurrentProviceName = province;
                        PerSonalFragment.this.mCurrentCityName = city;
                        PerSonalFragment.this.mCurrentDistrictName = district;
                        tv_job_address.setText(residenceValue.append(mCurrentProviceName).
                                append(TextUtils.isEmpty(mCurrentCityName) ? "" : ",").
                                append(mCurrentCityName).
                                append(TextUtils.isEmpty(mCurrentDistrictName) ? "" : ",").
                                append(mCurrentDistrictName));
                        ProvinceModel pCode;
                        CityModel1 cityode;
                        DistrictModel dCode;

                        pCode = provinceList.get(privinceItem);
                        cityode = pCode.getCityList().get(cityItem);
                        dCode = cityode.getDistrictList().get(districtItem);
                        perSonalBean.setUnit_province(pCode.getProvinceCode());
                        perSonalBean.setUnit_city(cityode.getCityCode());
                        perSonalBean.setUnit_county(dCode.getZipcode());
                        perSonalBean.setUnit_adr(pCode.getProvinceCode() + "," + cityode.getCityCode() + "," + dCode.getZipcode());
                        isChange = true;
                        productId(false);
                    }
                });
                job_address.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_address:
            case R.id.iv_huijiaddress:
            case R.id.ll_huijiAddress_type:
                if (StringUtil.isNullOrEmpty(etPeronalIdcard.getText().toString().trim())) {
                    ToastAlone.showShortToast(getContext(), "请先识别身份证！");
                    return;
                }
                CityPopupWindow window = new CityPopupWindow(getActivity(), CommonUtils.mProvinceDatas, CommonUtils.mCitisDatasMap, CommonUtils.mDistrictDatasMap);
                window.setOnCityPopupWindow(new CityPopupWindow.OnCityPopupWindow() {
                    @Override
                    public void onCityClick(String province, int privinceItem, String city, int cityItem, String district, int districtItem) {
                        if (residenceValue.length() > 0) {
                            residenceValue.delete(0, residenceValue.length());
                        }
                        PerSonalFragment.this.mCurrentProviceName = province;
                        PerSonalFragment.this.mCurrentCityName = city;
                        PerSonalFragment.this.mCurrentDistrictName = district;
                        tvAddress.setText(residenceValue.append(mCurrentProviceName).
                                append(TextUtils.isEmpty(mCurrentCityName) ? "" : ",").
                                append(mCurrentCityName).
                                append(TextUtils.isEmpty(mCurrentDistrictName) ? "" : ",").
                                append(mCurrentDistrictName));
                        ProvinceModel pCode;
                        CityModel1 cityode;
                        DistrictModel dCode;
                        pCode = provinceList.get(privinceItem);
                        cityode = pCode.getCityList().get(cityItem);
                        dCode = cityode.getDistrictList().get(districtItem);
                        perSonalBean.setHuji_province(pCode.getProvinceCode());
                        perSonalBean.setHuji_city(cityode.getCityCode());
                        perSonalBean.setHuji_county(dCode.getZipcode());
                        perSonalBean.setHuji_adr(pCode.getProvinceCode() + "," + cityode.getCityCode() + "," + dCode.getZipcode());
                        isChange = true;
                        productId(false);
                    }
                });
                window.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                window.setOnDismissListener(new DismissListener(BuryAction.MD_PULL_DOWN_PERSONAL_HOUSEHOLD_REGISTER_ADDRESS, tvAddress));
                break;
            case R.id.tv_residence_address:
            case R.id.iv_residence_address:
            case R.id.ll_residence_address_type:
                if (StringUtil.isNullOrEmpty(etPeronalIdcard.getText().toString().trim())) {
                    ToastAlone.showShortToast(getContext(), "请先识别身份证！");
                    return;
                }
                CityPopupWindow live_address = new CityPopupWindow(getActivity(), CommonUtils.mProvinceDatas, CommonUtils.mCitisDatasMap, CommonUtils.mDistrictDatasMap);
                live_address.setOnCityPopupWindow(new CityPopupWindow.OnCityPopupWindow() {
                    @Override
                    public void onCityClick(String province, int privinceItem, String city, int cityItem, String district, int districtItem) {
                        if (residenceValue.length() > 0) {
                            residenceValue.delete(0, residenceValue.length());
                        }
                        PerSonalFragment.this.mLiveProviceName = province;
                        PerSonalFragment.this.mLiveCityName = city;
                        PerSonalFragment.this.mLiveDistrictName = district;
                        tvResidenceAddress.setText(residenceValue.
                                append(mLiveProviceName)
                                .append(TextUtils.isEmpty(mLiveCityName) ? "" : ",").
                                        append(mLiveCityName)
                                .append(TextUtils.isEmpty(mLiveDistrictName) ? "" : ",").
                                        append(mLiveDistrictName));
                        ProvinceModel pCode;
                        CityModel1 cityode;
                        DistrictModel dCode;
                        pCode = provinceList.get(privinceItem);
                        cityode = pCode.getCityList().get(cityItem);
                        dCode = cityode.getDistrictList().get(districtItem);
                        perSonalBean.setLive_province(pCode.getProvinceCode());
                        perSonalBean.setLive_city(cityode.getCityCode());
                        perSonalBean.setLive_county(dCode.getZipcode());
                        perSonalBean.setLive_adr(pCode.getProvinceCode() + "," + cityode.getCityCode() + "," + dCode.getZipcode());
                        isChange = true;
                        productId(false);
                    }
                });
                if (!TextUtils.isEmpty(UserInfoManager.getInstance().getLocation().getmCurrentCity())) {
                    live_address.setWheel();
                }
                live_address.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ll_openBank:
                if (StringUtil.isNullOrEmpty(etPeronalIdcard.getText().toString().trim())) {
                    ToastAlone.showShortToast(getContext(), "请先识别身份证！");
                    return;
                }
                OneSelectPopupWindow bankPopupWindow = null;
                if (productId(true)) break;

                if (TextUtils.isEmpty(product_type)) {
                    product_type = UserInfoManager.getInstance().getPerSonalBean().getProduct_id();
                }
                if (!TextUtils.isEmpty(product_type)) {
                    if (!CommonConstant.PRODUCT_OFFLINE.equals(product_type)) {
                        bankPopupWindow = new OneSelectPopupWindow(getActivity(), openBankToCodeBean(perSonalBean.getOnline_bank_list()));//1 2,4
                    } else {
                        bankPopupWindow = new OneSelectPopupWindow(getActivity(), openBankToCodeBean(perSonalBean.getOffline_bank_list()));//3
                    }
                }
                if (bankPopupWindow == null) {
                    ToastAlone.showShortToast(getActivity(), "正在获取银行卡信息");
                    break;
                }
                bankPopupWindow.setOnSelectPopupWindow(new OneSelectPopupWindow.onSelectPopupWindow() {
                    @Override
                    public void onSelectClick(int index, CodeBean select) {
                        perSonalBean.setBank_code(select.getCode());
                        tvOpenbank.setText(select.getName());
                    }
                });
                bankPopupWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.iv_question_bank:
                dialog.bankDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.iv_photo://暂不使用银行卡扫描
//                if (ToolPermission.checkSelfPermission(
//                        getActivity(), this,
//                        new String[]{Manifest.permission.CAMERA,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, "请允许权限进行拍照", 1012)) {
//                    intent = BankCardUtils.getInstance().getBankCardIntent(getActivity(), false, false);
//                    startActivityForResult(intent, BankCardUtils.INTO_BANKCARDSCAN_PAGE);
//                } else {
//                    ToastAlone.showShortToast(getContext(), "请允许权限进行拍照!");
//                }
                break;

        }
    }

    private boolean productId(boolean b) {
        String huji = "";
        String live = "";
        String work = "";
        if (TextUtils.isEmpty(perSonalBean.getHuji_adr()) || perSonalBean.getHuji_adr().split(",").length != 3) {
            if (b)
                ToastAlone.showShortToast(getActivity(), "请选择户籍地址");
            return true;
        } else {
            if (perSonalBean.getHuji_adr().contains(","))
                huji = perSonalBean.getHuji_adr().split(",")[1];
        }
        if (TextUtils.isEmpty(perSonalBean.getLive_adr()) || perSonalBean.getLive_adr().split(",").length != 3) {
            if (b)
                ToastAlone.showShortToast(getActivity(), "请选择居住地址");
            return true;
        } else {
            if (perSonalBean.getLive_adr().contains(","))
                live = perSonalBean.getLive_adr().split(",")[1];
        }
        if (CommonConstant.PRODUCT_FXDONLINE.equals(perSonalBean.getProduct_id())
                || CommonConstant.PRODUCT_ONLINE_LOW_QUALITY.equals(perSonalBean.getProduct_id())) {
            if (TextUtils.isEmpty(perSonalBean.getUnit_adr()) || perSonalBean.getUnit_adr().split(",").length != 3) {
                if (b)
                    ToastAlone.showShortToast(getActivity(), "请选择工作地址");
                return true;
            } else {
                if (perSonalBean.getUnit_adr().contains(","))//"1".equals(perSonalBean.getProduct_id())&&
                    work = perSonalBean.getUnit_adr().split(",")[1];
            }
        } else {
            if (!TextUtils.isEmpty(perSonalBean.getUnit_adr()) && perSonalBean.getUnit_adr().split(",").length == 3)//"1".equals(perSonalBean.getProduct_id())&&
                work = perSonalBean.getUnit_adr().split(",")[1];
        }
        if (!isChange && !TextUtils.isEmpty(perSonalBean.getProduct_id()) && !PersonalDataActivity.isShowAuthor) {
            product_type = perSonalBean.getProduct_id();
            return false;
        } else {
            isChange = false;//改变
        }
        if (CustomerManagerManager.isCustomerManager()) {
            MyLog.i("keey", "1");
            getProductID.getCustomerProductID(PersonalDataActivity.custId, PersonalDataActivity.phoneNum, perSonalBean);
//            getProductID.getCustomerProductID(etPeronalIdcard.getText().toString().trim(), huji, live, work, UserInfoManager.getInstance().getBorrowInfo().getBorrow_limit());
        } else {
            MyLog.i("keey", "2");
            String phone = SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.USERNAME);
            getProductID.getProductID(phone, perSonalBean);
//            getProductID.getProductID(etPeronalIdcard.getText().toString().trim(), huji, live, work, UserInfoManager.getInstance().getBorrowInfo().getBorrow_limit());
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (ToolPermission.checkPermission(permissions, grantResults)) {
                Intent intent = IdcardUtils.getInstance().getIdCardIntent(getActivity(), 0, false);
                startActivityForResult(intent, INTO_IDCARDSCAN_PAGE);
            } else {
                ToastAlone.showShortToast(getContext(), "拍照权限被拒绝");
            }
        }
        if (requestCode == 1001) {
            if (ToolPermission.checkPermission(permissions, grantResults)) {
                Intent intent = IdcardUtils.getInstance().getIdCardIntent(getActivity(), 1, false);
                startActivityForResult(intent, INTO_IDCARDSCAN_PAGE);
            } else {
                ToastAlone.showShortToast(getContext(), "拍照权限被拒绝");
            }
        }
        if (requestCode == 1012) {
            if (ToolPermission.checkPermission(permissions, grantResults)) {
                Intent intent = BankCardUtils.getInstance().getBankCardIntent(getActivity(), false, false);
                startActivityForResult(intent, BankCardUtils.INTO_BANKCARDSCAN_PAGE);
            }
        } else {
            ToastAlone.showShortToast(getContext(), "拍照权限被拒绝");
        }
        if (requestCode == 101) {
            if (ToolPermission.checkPermission(permissions, grantResults)) {
            } else {
                ToastAlone.showShortToast(getContext(), "拍照权限被拒绝");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        takePhoto.onActivityResult(requestCode, resultCode, data);
        Map map = IdcardUtils.getInstance().onActivityResult(requestCode, resultCode, data);
        String bankNum = BankCardUtils.getInstance().onActivityResult(requestCode, resultCode, data);
        if (!TextUtils.isEmpty(bankNum)) {
            edBankno.setText(bankNum);
            return;
        }

        if (map.size() > 0) {
            final Bitmap bitmap = IdcardUtils.getInstance().gitBitmap(map);
            if (IdcardUtils.getInstance().getSide() == 0) {

                //身份证正
                IDcardController.getInstance().getIDInfo(getActivity(), bitmap, new IControllerCallBack() {
                    @Override
                    public void onSuccess(int returnCode, String value) {
                        CommonUtils.closeDialog();
                        boolean res = IDCard.isIDCard(UserInfoManager.getInstance().getUser_IdCard());
                        if (!res) {//如果身份证号检查不合格
                            ToastAlone.showLongToast(getActivity(), "身份证号有误，请从新拍摄");
                            return;
                        }

                        dialog.idCardDialog(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    switch (view.getId()) {
                                                        case R.id.confirm:
                                                            dialog.dismiss();
                                                            if (!TextUtils.isEmpty(UserInfoManager.getInstance().getUserName())) {
                                                                etPersonalName.setText(UserInfoManager.getInstance().getUserName());
                                                                perSonalBean.setUsername(etPersonalName.getText() == null ? "" : etPersonalName.getText().toString().trim());
                                                            }
                                                            if (!TextUtils.isEmpty(UserInfoManager.getInstance().getUser_IdCard())) {
                                                                etPeronalIdcard.setText(UserInfoManager.getInstance().getUser_IdCard());
                                                                perSonalBean.setIdcard(etPeronalIdcard.getText() == null ? "" : etPeronalIdcard.getText().toString().trim());
                                                            }
                                                            if (!TextUtils.isEmpty(UserInfoManager.getInstance().getUserHuijiDelAddress())) {
                                                                edDetailedAddress.setText(UserInfoManager.getInstance().getUserHuijiDelAddress());
                                                                perSonalBean.setHuji_adr_detail(edDetailedAddress.getText() == null ? "" : edDetailedAddress.getText().toString().trim());
                                                            }
                                                            path = IDcardController.getInstance().getFile().getPath();
                                                            uploadController.upload(path, 1);
                                                            break;
                                                        case R.id.begin_scan:
                                                            dialog.dismiss();
                                                            Intent intent = null;
                                                            intent = IdcardUtils.getInstance().getIdCardIntent(getActivity(), 0, false);
                                                            startActivityForResult(intent, INTO_IDCARDSCAN_PAGE);
                                                            break;
                                                        case R.id.iv_dismisspop:
                                                            dialog.dismiss();
                                                            break;
                                                    }
                                                }
                                            }, IdcardUtils.getInstance().getSide(),
                                !TextUtils.isEmpty(UserInfoManager.getInstance().getUserName()) ? UserInfoManager.getInstance().getUserName() : "",
                                !TextUtils.isEmpty(UserInfoManager.getInstance().getUser_IdCard()) ? UserInfoManager.getInstance().getUser_IdCard() : "",
                                !TextUtils.isEmpty(UserInfoManager.getInstance().getUserHuijiDelAddress()) ?
                                        UserInfoManager.getInstance().getUserHuijiDelAddress() : "");
                        dialog.show();
                    }

                    @Override
                    public void onFail(String errorMessage) {
                        CommonUtils.closeDialog();
                        Message msg = new Message();
                        msg.what = 55;
                        msg.obj = errorMessage;
                        handler.sendMessage(msg);
                    }
                });
            } else if (IdcardUtils.getInstance().getSide() == 1) {
                //反面

                IDcardController.getInstance().getIDInfo(getActivity(), bitmap, new IControllerCallBack() {
                    @Override
                    public void onSuccess(int returnCode, String value) {
                        CommonUtils.closeDialog();
                        path = IDcardController.getInstance().getFile().getPath();
                        uploadController.upload(path, 2);

                    }

                    @Override
                    public void onFail(String errorMessage) {
                        CommonUtils.closeDialog();
                        Message msg = new Message();
                        msg.what = 56;
                        msg.obj = errorMessage;
                        handler.sendMessage(msg);
                    }
                });
            }
        }
    }

    private void cache() {
        perSonalBean.setUsername(etPersonalName.getText() == null ? "" : etPersonalName.getText().toString().trim());
        perSonalBean.setIdcard(etPeronalIdcard.getText() == null ? "" : etPeronalIdcard.getText().toString().trim());
        perSonalBean.setHuji_adr_detail(edDetailedAddress.getText() == null ? "" : edDetailedAddress.getText().toString().trim());
        perSonalBean.setLive_adr_detail(edResidenceDetailedAddress.getText() == null ? "" : edResidenceDetailedAddress.getText().toString().trim());
        perSonalBean.setUnit_adr_detail(ed_job_detailed_address.getText() == null ? "" : ed_job_detailed_address.getText().toString().trim());

        perSonalBean.setBankcard_no(edBankno.getText() == null ? "" : edBankno.getText().toString().trim());
        perSonalBean.setBank_phone(edPesonalPhone.getText() == null ? "" : edPesonalPhone.getText().toString().trim());

        mEducationIndex = AppConfig.degree().indexOf(new CodeBean(1, "", tv_education.getText() == null ? "" : tv_education.getText().toString().trim()));
        if (mEducationIndex != -1) {
            perSonalBean.setEducation(AppConfig.degree().get(mEducationIndex).getCode());
        }

        marriageStatuIndex = AppConfig.marriageStatus().indexOf(new CodeBean(1, "", tv_marriage.getText() == null ? "" : tv_marriage.getText().toString().trim()));
        if (marriageStatuIndex != -1) {
            perSonalBean.setMarriage(AppConfig.marriageStatus().get(marriageStatuIndex).getCode());
        }

        mlive_StatuIndex = AppConfig.liveStatu().indexOf(new CodeBean(1, "", tv_livestatu.getText() == null ? "" : tv_livestatu.getText().toString().trim()));
        if (mlive_StatuIndex != -1) {
            perSonalBean.setLive_status(AppConfig.liveStatu().get(mlive_StatuIndex).getCode());
        }

        String[] caddress = tvAddress.getText().toString().trim().split(",");
        home = "";
        if (caddress != null && caddress.length == 3)
            for (ProvinceModel provinceModel : provinceList) {
                if (provinceModel.getName().equals(caddress[0])) {
                    home += provinceModel.getProvinceCode();
                    for (CityModel1 cityModel : provinceModel.getCityList()) {
                        if (cityModel.getName().equals(caddress[1])) {
                            home += "," + cityModel.getCityCode();
                            for (DistrictModel districtModel : cityModel.getDistrictList()) {
                                if (districtModel.getName().equals(caddress[2])) {
                                    home += "," + districtModel.getZipcode();
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }


        String[] liveAddress = tvResidenceAddress.getText().toString().trim().split(",");
        live = "";

        if (liveAddress != null && liveAddress.length == 3)
            for (ProvinceModel provinceModel : provinceList) {
                if (provinceModel.getName().equals(liveAddress[0])) {
                    live += provinceModel.getProvinceCode();
                    for (CityModel1 cityModel : provinceModel.getCityList()) {
                        if (cityModel.getName().equals(liveAddress[1])) {
                            live += "," + cityModel.getCityCode();
                            for (DistrictModel districtModel : cityModel.getDistrictList()) {
                                if (districtModel.getName().equals(liveAddress[2])) {
                                    live += "," + districtModel.getZipcode();
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }

        String[] job_addressSlip = tv_job_address.getText().toString().trim().split(",");
        job_address = "";
        if (job_addressSlip != null && job_addressSlip.length == 3)
            for (ProvinceModel provinceModel : provinceList) {
                if (provinceModel.getName().equals(job_addressSlip[0])) {
                    job_address += provinceModel.getProvinceCode();
                    for (CityModel1 cityModel : provinceModel.getCityList()) {
                        if (cityModel.getName().equals(job_addressSlip[1])) {
                            job_address += "," + cityModel.getCityCode();
                            for (DistrictModel districtModel : cityModel.getDistrictList()) {
                                if (districtModel.getName().equals(job_addressSlip[2])) {
                                    job_address += "," + districtModel.getZipcode();
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }

        if (UserInfoManager.getInstance().getOpenBanks() != null) {
            bankName = UserInfoManager.getInstance().getOpenBanks().indexOf(new CodeBean(1, "", tvOpenbank.getText() == null ? "" : tvOpenbank.getText().toString().trim()));
            if (bankName != -1) {
                perSonalBean.setBank_code(UserInfoManager.getInstance().getOpenBanks().get(bankName).getCode());
            }
        }
        perSonalBean.setUnit_adr(job_address);
        perSonalBean.setLive_adr(live);
        perSonalBean.setHuji_adr(home);
    }

    Bitmap idCardz = null, idCardf = null, seleHandBitmap = null;

    private void show() {
        try {
            if (!TextUtils.isEmpty(perSonalBean.getIdcard_z())) {
                String idCardUrl = perSonalBean.getIdcard_z();
                Glide.with(getActivity()).load(idCardUrl).placeholder(R.drawable.idcard_front)
                        .override(mWidth, mHeight)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                Bitmap bmp = ImageHandleUtil.drawableToBitmap(resource);
                                idCardz = Bitmap.createScaledBitmap(bmp, mWidth, mHeight, true);
                                bmp.recycle();
                                System.gc();
                                imageView3.setImageBitmap(idCardz);
                            }
                        });
            }

            if (!TextUtils.isEmpty(perSonalBean.getIdcard_f())) {
                String idCardF = perSonalBean.getIdcard_f();
                Glide.with(getActivity())
                        .load(idCardF)
                        .placeholder(R.drawable.idcard_back)
                        .override(mWidth, mHeight)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                Bitmap bmp = ImageHandleUtil.drawableToBitmap(resource);
                                idCardf = Bitmap.createScaledBitmap(bmp, mWidth, mHeight, true);
                                bmp.recycle();
                                System.gc();
                                imageView2.setImageBitmap(idCardf);
                            }
                        });
            }


            if (!TextUtils.isEmpty(perSonalBean.getIdcard_hand())) {
                String idCardHand = perSonalBean.getIdcard_hand();
                Glide.with(getActivity())
                        .load(idCardHand)
                        .placeholder(R.drawable.idcard_bahold)
                        .override(mWidth, mHeight)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                Bitmap bmp = ImageHandleUtil.drawableToBitmap(resource);
                                seleHandBitmap = Bitmap.createScaledBitmap(bmp, mWidth, mHeight, true);
                                bmp.recycle();
                                System.gc();
                                iv_self.setImageBitmap(seleHandBitmap);
                            }
                        });
            }


//                }
//            });
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (idCardz != null)
                        imageView3.setImageBitmap(idCardz);
                    if (idCardf != null)
                        imageView2.setImageBitmap(idCardf);
                    if (seleHandBitmap != null) {
                        iv_self.setImageBitmap(seleHandBitmap);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!TextUtils.isEmpty(perSonalBean.getUsername())) {
            etPersonalName.setText(perSonalBean.getUsername());
        }
        if (!TextUtils.isEmpty(perSonalBean.getIdcard())) {
            etPeronalIdcard.setText(perSonalBean.getIdcard());
        }
        if (!TextUtils.isEmpty(perSonalBean.getEducation())) {
            int index = AppConfig.degree().indexOf(new CodeBean(0, perSonalBean.getEducation(), ""));
            if (index != -1)
                tv_education.setText(AppConfig.degree().get(index).getName());
            else {
                tv_education.setText("");
                perSonalBean.setEducation("");
            }
        }
        if (!TextUtils.isEmpty(perSonalBean.getMarriage())) {
            int index = AppConfig.marriageStatus().indexOf(new CodeBean(0, perSonalBean.getMarriage(), ""));
            if (index != -1)
                tv_marriage.setText(AppConfig.marriageStatus().get(index).getName());
            else {
                tv_marriage.setText("");
                perSonalBean.setMarriage("");
            }
        }
        if (!TextUtils.isEmpty(perSonalBean.getLive_status())) {
            int index = AppConfig.liveStatu().indexOf(new CodeBean(0, perSonalBean.getLive_status(), ""));
            if (index != -1)
                tv_livestatu.setText(AppConfig.liveStatu().get(index).getName());
            else {
                tv_livestatu.setText("");
                perSonalBean.setLive_status("");
            }
        }

        if (!TextUtils.isEmpty(perSonalBean.getHuji_adr())) {
            try {
                String[] code = perSonalBean.getHuji_adr().split(",");
                if (code != null && code.length == 3) {
                    ProvinceModel p = null;
                    CityModel1 c = null;
                    DistrictModel d = null;
                    if (provinceList.indexOf(new ProvinceModel(code[0])) != -1) {
                        p = provinceList.get(provinceList.indexOf(new ProvinceModel(code[0])));
                    }
                    if (p != null && p.getCityList().indexOf(new CityModel1(code[1])) != -1) {
                        c = p.getCityList().get(p.getCityList().indexOf(new CityModel1(code[1])));
                    }
                    if (c != null && c.getDistrictList().indexOf(new DistrictModel(code[2])) != -1) {
                        d = c.getDistrictList().get(c.getDistrictList().indexOf(new DistrictModel(code[2])));
                    }
                    if (p != null && c != null && d != null) {
                        if (StringUtil.isNullOrEmpty(perSonalBean.getHuji_province()) || StringUtil.isNullOrEmpty(perSonalBean.getHuji_city()) || StringUtil.isNullOrEmpty(perSonalBean.getHuji_county())) {
                            tvAddress.setText("");
                        } else {
                            tvAddress.setText(p.getName() + "," + c.getName() + "," + d.getName());
                        }
                    } else {
                        tvAddress.setText("");
                        perSonalBean.setHuji_adr("");
                    }
                } else {
                    tvAddress.setText("");
                    perSonalBean.setHuji_adr("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(perSonalBean.getHuji_adr_detail())) {
            edDetailedAddress.setText(perSonalBean.getHuji_adr_detail());
        }
        if (!TextUtils.isEmpty(perSonalBean.getUnit_adr())) {
            try {
                String[] code = perSonalBean.getUnit_adr().split(",");
                if (code != null && code.length == 3) {
                    ProvinceModel p = null;
                    CityModel1 c = null;
                    DistrictModel d = null;
                    if (provinceList.indexOf(new ProvinceModel(code[0])) != -1) {
                        p = provinceList.get(provinceList.indexOf(new ProvinceModel(code[0])));
                    }
                    if (p != null && p.getCityList().indexOf(new CityModel1(code[1])) != -1) {
                        c = p.getCityList().get(p.getCityList().indexOf(new CityModel1(code[1])));
                    }
                    if (c != null && c.getDistrictList().indexOf(new DistrictModel(code[2])) != -1) {
                        d = c.getDistrictList().get(c.getDistrictList().indexOf(new DistrictModel(code[2])));
                    }
                    if (p != null && c != null && d != null) {
                        if (StringUtil.isNullOrEmpty(perSonalBean.getUnit_province()) || StringUtil.isNullOrEmpty(perSonalBean.getUnit_city()) || StringUtil.isNullOrEmpty(perSonalBean.getUnit_county())) {
                            tv_job_address.setText("");
                        } else {
                            tv_job_address.setText(p.getName() + "," + c.getName() + "," + d.getName());
                        }
                    } else {
                        tv_job_address.setText("");
                        perSonalBean.setUnit_adr("");

                    }
                } else {
                    tv_job_address.setText("");
                    perSonalBean.setUnit_adr("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(perSonalBean.getUnit_adr_detail())) {
            ed_job_detailed_address.setText(perSonalBean.getUnit_adr_detail());
        }

        if (!TextUtils.isEmpty(perSonalBean.getLive_adr())) {
            try {
                String[] code = perSonalBean.getLive_adr().split(",");
                if (code != null && code.length == 3) {
                    ProvinceModel p = null;
                    CityModel1 c = null;
                    DistrictModel d = null;
                    if (provinceList.indexOf(new ProvinceModel(code[0])) != -1) {
                        p = provinceList.get(provinceList.indexOf(new ProvinceModel(code[0])));
                    }
                    if (p != null && p.getCityList().indexOf(new CityModel1(code[1])) != -1) {
                        c = p.getCityList().get(p.getCityList().indexOf(new CityModel1(code[1])));
                    }
                    if (c != null && c.getDistrictList().indexOf(new DistrictModel(code[2])) != -1) {
                        d = c.getDistrictList().get(c.getDistrictList().indexOf(new DistrictModel(code[2])));
                    }
                    if (p != null && c != null && d != null) {
                        if (StringUtil.isNullOrEmpty(perSonalBean.getLive_province()) || StringUtil.isNullOrEmpty(perSonalBean.getLive_city()) || StringUtil.isNullOrEmpty(perSonalBean.getLive_county())) {
                            tvResidenceAddress.setText("");
                        } else {
                            tvResidenceAddress.setText(p.getName() + "," + c.getName() + "," + d.getName());
                        }
                    } else {
                        tvResidenceAddress.setText("");
                        perSonalBean.setLive_adr("");
                    }

                } else {
                    tvResidenceAddress.setText("");
                    perSonalBean.setLive_adr("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(perSonalBean.getLive_adr_detail())) {
            edResidenceDetailedAddress.setText(perSonalBean.getLive_adr_detail());
        }

        if (!TextUtils.isEmpty(perSonalBean.getBankcard_no())) {
            edBankno.setText(perSonalBean.getBankcard_no());
        }

        if (!TextUtils.isEmpty(perSonalBean.getBank_name())) {
            tvOpenbank.setText(perSonalBean.getBank_name());
        }
        if (!TextUtils.isEmpty(perSonalBean.getBank_phone())) {
            edPesonalPhone.setText(perSonalBean.getBank_phone());
        }

        if (!TextUtils.isEmpty(perSonalBean.getProduct_id())) {
            int index = -1;
            if (!CommonConstant.PRODUCT_OFFLINE.equals(perSonalBean.getProduct_id())) {
                if (!TextUtils.isEmpty(perSonalBean.getBank_code()) && !TextUtils.isEmpty(perSonalBean.getBank_name())) {
                    index = openBankToCodeBean(UserInfoManager.getInstance().getPerSonalBean().getOnline_bank_list()).indexOf(new CodeBean(0, perSonalBean.getBank_code(), ""));
                    if (index == -1) {
                        edBankno.setText("");
                        tvOpenbank.setText("");
                        perSonalBean.setBankcard_no("");
                        perSonalBean.setBank_name("");
                        perSonalBean.setBank_code("");
                    } else {
                        edBankno.setText(perSonalBean.getBankcard_no());
                        tvOpenbank.setText(perSonalBean.getBank_name());
                    }
                }
            } else {
                if (!TextUtils.isEmpty(perSonalBean.getBank_code()) && !TextUtils.isEmpty(perSonalBean.getBank_name())) {
                    index = openBankToCodeBean(UserInfoManager.getInstance().getPerSonalBean().getOffline_bank_list()).indexOf(new CodeBean(0, perSonalBean.getBank_code(), ""));
                    if (index == -1) {
                        edBankno.setText("");
                        tvOpenbank.setText("");
                        perSonalBean.setBankcard_no("");
                        perSonalBean.setBank_name("");
                        perSonalBean.setBank_code("");
                    } else {
                        edBankno.setText(perSonalBean.getBankcard_no());
                        tvOpenbank.setText(perSonalBean.getBank_name());
                    }
                }
            }
        }
        if (PersonalDataActivity.isShowAuthor)
            productId(false);
    }

    private void submit() {
        cache();
        if (TextUtils.isEmpty(perSonalBean.getUsername())) {
            ToastAlone.showToast(getContext(), "姓名不能为空!", Toast.LENGTH_LONG);
            return;
        }

        if (TextUtils.isEmpty(perSonalBean.getIdcard())) {
            ToastAlone.showToast(getContext(), "身份证不能为空!", Toast.LENGTH_LONG);
            return;
        }
        if (!perSonalBean.getIdcard().matches(idRex)) {
            ToastAlone.showToast(getContext(), "身份证号码有误，请重新拍照!", Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(perSonalBean.getMarriage())) {
            ToastAlone.showToast(getContext(), "请选择婚姻关系!", Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(perSonalBean.getLive_status())) {
            ToastAlone.showToast(getContext(), "请选择居住状况!", Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(perSonalBean.getIdCardZId())) {
            ToastAlone.showLongToast(getContext(), "请拍照上传身份证正面!");
            return;
        }
        if (TextUtils.isEmpty(perSonalBean.getIdCardFId())) {
            ToastAlone.showLongToast(getContext(), "请拍照上传身份证反面!");
            return;
        }
        if (TextUtils.isEmpty(perSonalBean.getIdCardHandId())) {
            ToastAlone.showLongToast(getContext(), "请手拿身份证自拍!");
            return;
        }

        if (TextUtils.isEmpty(perSonalBean.getHuji_adr())) {
            ToastAlone.showToast(getContext(), "请选户籍地址!", Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(perSonalBean.getHuji_adr_detail())) {
            ToastAlone.showToast(getContext(), "详细地址不能为空!", Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(perSonalBean.getLive_adr())) {
            ToastAlone.showToast(getContext(), "请选择居住地址!", Toast.LENGTH_LONG);
            return;
        }

        if (TextUtils.isEmpty(perSonalBean.getLive_adr_detail())) {
            ToastAlone.showToast(getContext(), "现居住详细地址不能为空!", Toast.LENGTH_LONG);
            return;
        }

        if (CommonConstant.PRODUCT_ONLINE_LOW_QUALITY.equals(UserInfoManager.getInstance().getPerSonalBean().getProduct_id())
                || CommonConstant.PRODUCT_FXDONLINE.equals(UserInfoManager.getInstance().getPerSonalBean().getProduct_id())
                ) {
            if (TextUtils.isEmpty(perSonalBean.getUnit_adr())) {
                ToastAlone.showToast(getContext(), "请选择工作地址!", Toast.LENGTH_LONG);
                return;
            }
            if (TextUtils.isEmpty(perSonalBean.getUnit_adr_detail())) {
                ToastAlone.showToast(getContext(), "工作详细地址不能为空!", Toast.LENGTH_LONG);
                return;
            }
        }

        if (TextUtils.isEmpty(perSonalBean.getBank_code())) {
            ToastAlone.showLongToast(getContext(), "请选择开户银行");
            return;
        }
        if (TextUtils.isEmpty(perSonalBean.getBankcard_no())) {
            ToastAlone.showLongToast(getContext(), "银行卡不能为空!");
            return;
        }

        if (TextUtils.isEmpty(perSonalBean.getBank_phone())) {
            ToastAlone.showLongToast(getContext(), "银行预留手机不能为空!");
            return;
        }
        if (!RegularExpUtil.isMobile(perSonalBean.getBank_phone())) {
            ToastAlone.showShortToast(IApplication.globleContext, "请输入正确的银行预留手机！");
            return;
        }
        if (perSonalBean.getMarriage().equals("802")) {
            SharedPreferencesUtil.getInstance(IApplication.gainContext()).setBoolean(SPKey.IS_MARRIED, true);
        } else {
            SharedPreferencesUtil.getInstance(IApplication.gainContext()).setBoolean(SPKey.IS_MARRIED, false);
        }
        //把婚姻状态保存起来
        SharedPreferencesUtil.getInstance(getActivity()).setString(SPKey.MARRIDE_CODE, perSonalBean.getMarriage());
        if (!StringUtil.isNullOrEmpty(perSonalBean.getResult()) && !perSonalBean.getResult().equals("{}")) {
            OfflineBean offline = new Gson().fromJson(perSonalBean.getResult(), OfflineBean.class);
            if (offline.getRenew_loans().equals(CommonConstant.LOANS_RENEW_STORE) || offline.getRenew_loans().equals(CommonConstant.LOANS_RENEW_ONLINE)) {//从后台获取续贷身份
                SharedPreferencesUtil.getInstance(IApplication.gainContext()).setString(SPKey.IS_RENEW_LOANS, offline.getRenew_loans());
            } else {
                SharedPreferencesUtil.getInstance(IApplication.gainContext()).setString(SPKey.IS_RENEW_LOANS, CommonConstant.LOANS_FIRST);
            }
            SharedPreferencesUtil.getInstance(IApplication.gainContext()).setString(SPKey.APPLY_INFO, new Gson().toJson(offline));
        } else {
            SharedPreferencesUtil.getInstance(IApplication.gainContext()).setString(SPKey.IS_RENEW_LOANS, CommonConstant.LOANS_FIRST);
            SharedPreferencesUtil.getInstance(IApplication.gainContext()).setString(SPKey.APPLY_INFO, "");
        }
        MyLog.i("keey", "product_type:" + product_type);
        if (StringUtil.isNullOrEmpty(product_type) || "f".equals(product_type)) {
            ToastAlone.showLongToast(getContext(), "数据异常，请重新进入页面填写!");
            return;
        }
        SharedPreferencesUtil.getInstance(IApplication.gainContext()).setString(SPKey.PRODUCT_ID, product_type);
        if (CustomerManagerManager.isCustomerManager()) {
            customerManagerDataController.saveCustomeerData(perSonalBean, PersonalDataActivity.custId);
        } else {
            personalDataSava.PersonalDataSava(perSonalBean);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putString("filePath", filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<CodeBean> openBankToCodeBean(List<OpenBank> openBank) {
        List<CodeBean> codeBeanList = new ArrayList<CodeBean>();
        if (openBank != null && openBank.size() > 0) {
            for (int i = 0; i < openBank.size(); i++) {
                codeBeanList.add(new CodeBean(i, openBank.get(i).getCode_(), openBank.get(i).getDesc_()));
            }
        }
        return codeBeanList;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void toPhone(final int type) {
        if (ToolPermission.checkSelfPermission(
                getActivity(), this,
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, "请允许权限进行拍照", type)) {
            File file = new File(Environment.getExternalStorageDirectory(), "/JJT/temp/" + System.currentTimeMillis() + ".jpg");
            imgPath = file.getAbsolutePath();
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            Uri imageUri = Uri.fromFile(file);
            takePhoto.setTakePhotoOptions(new TakePhotoOptions.Builder().setWithOwnGallery(true).create());
            CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
            takePhoto.onEnableCompress(compressConfig, true);
            takePhoto.onPickFromCapture(imageUri);

        }
    }

    @Override
    public void takeSuccess(TResult result) {
        filePath = result.getImage().getOriginalPath();
        File file = new File(filePath);
        if (!file.exists()) {//返回的filepath路径有问题
            filePath = imgPath;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        if (bitmap == null) {
            ToastAlone.showLongToast(getActivity(), "图片为空");
            return;
        }
        Bitmap decode = comp(bitmap);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        ToolImage.compressBitmapToFile(decode, Bitmap.CompressFormat.PNG, 100, new File(filePath));
        if (decode != null && !decode.isRecycled()) {
            decode.recycle();
        }
        System.out.print("filePath:" + filePath);
        uploadController.upload(filePath, 5);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Toast.makeText(getContext(), "takeFail:" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {
        Toast.makeText(getContext(), getResources().getString(com.jph.takephoto.R.string.msg_operation_canceled), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.GET_PRODUCT_ID) {
            product_type = value;
            if (!PersonalDataActivity.isShowAuthor) {
                edBankno.setText(perSonalBean.getBankcard_no());
                tvOpenbank.setText(perSonalBean.getBank_name());
            } else {
                edBankno.setText("");
                tvOpenbank.setText("");
                perSonalBean.setBankcard_no("");
                perSonalBean.setBank_name("");
                perSonalBean.setBank_code("");
            }
        } else if (returnCode == CallBackType.GET_PRODUCT_ID_SAME) {
            product_type = value;
        } else if (returnCode == CallBackType.GET_PERSONAL_INFOR || returnCode == CallBackType.GET_CUSTOMER_DATA) {
            if (UserInfoManager.getInstance().getPerSonalBean() != null) {
                perSonalBean = UserInfoManager.getInstance().getPerSonalBean();
                product_type = perSonalBean.getProduct_id();
                show();
                isGetUserDate = true;
            }
        } else if (returnCode == CallBackType.SAVA_PERSONAL_INFOR || returnCode == CallBackType.SAVE_CUSTOMER_DATA) {
            CommonUtils.closeDialog();
            ((PersonalDataActivity) getActivity()).setComplete(1);
            ((PersonalDataActivity) getActivity()).swithFragment(JobFragment.class);
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        MyLog.i("keey", "msg:" + errorMessage);
        if (returnCode == CallBackType.GET_PERSONAL_INFOR || returnCode == CallBackType.GET_CUSTOMER_DATA) {
            Message msg = new Message();
            msg.what = 22;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        } else if (returnCode == CallBackType.SAVA_PERSONAL_INFOR || returnCode == CallBackType.SAVE_CUSTOMER_DATA) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        } else if (returnCode == CallBackType.GET_PRODUCT_ID || returnCode == CallBackType.GET_PRODUCT_ID_SAME) {
            product_type = "f";
        }
    }
}
