package com.two.emergencylending.fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.two.emergencylending.activity.PersonalDataActivity;
import com.two.emergencylending.base.BaseFragment;
import com.two.emergencylending.bean.CityModel1;
import com.two.emergencylending.bean.CodeBean;
import com.two.emergencylending.bean.DistrictModel;
import com.two.emergencylending.bean.JobInforBean;
import com.two.emergencylending.bean.ProvinceModel;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.AppConfig;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.CommonConstant;
import com.two.emergencylending.controller.AddJobWorkInforCotroller;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.CustomerManagerDataController;
import com.two.emergencylending.controller.QueryWorkinforController;
import com.two.emergencylending.manager.CustomerManagerManager;
import com.two.emergencylending.popupwindow.CityPopupWindow;
import com.two.emergencylending.popupwindow.OneSelectPopupWindow;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.ToastAlone;
import com.zyjr.emergencylending.R;

import butterknife.Bind;
import butterknife.OnClick;

import static com.two.emergencylending.constant.AppConfig.getWorkInfo;
import static com.two.emergencylending.constant.AppConfig.month_salary;
import static com.two.emergencylending.constant.AppConfig.proFession;
import static com.two.emergencylending.constant.CallBackType.GET_JOB_INFOR;
import static com.two.emergencylending.utils.CommonUtils.provinceList;
import static com.zyjr.emergencylending.R.id.ll_job_unit_profession;

/**
 * 工作信息
 */

public class JobFragment extends BaseFragment implements ControllerCallBack {
    @Bind(R.id.ed_unit_tel)
    EditText ed_unit_tel;
    @Bind(R.id.ed_unit_district_num)
    EditText ed_unit_district_num;
    @Bind(R.id.ed_unit_detailed_address)
    EditText edUnitDetailedAddress;
    @Bind(R.id.ed_uniti_name)
    EditText ed_uniti_name;
    @Bind(R.id.tv_income)
    TextView tv_income;
    @Bind(R.id.tv_profession)
    TextView tv_profession;
    @Bind(R.id.tv_unit_address)
    TextView tv_unit_address;
    @Bind(R.id.ed_depaotment)
    EditText ed_depaotment;
    @Bind(R.id.tv_industry)
    TextView tv_industry;
    @Bind(R.id.tv_job)
    TextView tv_job;
    @Bind(R.id.ll_no_work)
    LinearLayout ll_no_work;
    @Bind(R.id.ll_job_unit_profession)
    LinearLayout job_unit_profession;
    @Bind(R.id.view_work)
    View view_work;
    @Bind(R.id.tv_no_work)
    TextView tv_no_work;
    public int mUnitIndustryIndex = -1;//当前学历的名称
    /**
     * 当前省的名称
     */
    public String mCurrentProviceName = "";
    /**
     * 当前市的名称
     */
    public String mCurrentCityName = "";
    /**
     * 当前区的名称
     */
    String tel = "";
    public String mCurrentDistrictName = "";
    private StringBuilder residenceValue = new StringBuilder();
    private String select = "";
    ProvinceModel pCode;
    CityModel1 cityode;
    DistrictModel dCode;
    private static JobInforBean jobInforBean = new JobInforBean();
    private int mjobIndex = -1;
    public int mProfessionIndex = -1;
    public int mSalaryIndex = -1;//当前学历的名称
    QueryWorkinforController queryWorkinforController;
    AddJobWorkInforCotroller savajob;
    private CustomerManagerDataController customerManagerDataController;

    @Override
    protected void lazyLoad() {

    }

    @Override
    public int setContent() {
        return R.layout.layout_job;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    CommonUtils.closeDialog();
                    ToastAlone.showLongToast(getActivity(), msg.obj.toString());
                    break;
                case 2:
                    ToastAlone.showLongToast(getActivity(), msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void init() {
        CommonUtils.addressDatas(getActivity());
        customerManagerDataController = new CustomerManagerDataController(getActivity(), this);
        savajob = new AddJobWorkInforCotroller(getActivity(), new ControllerCallBack() {
            @Override
            public void onSuccess(int returnCode, String value) {
                if (returnCode == CallBackType.SAVA_JOB_INFOR) {
                    CommonUtils.closeDialog();
                    ((PersonalDataActivity) getActivity()).setComplete(2);
                    ((PersonalDataActivity) getActivity()).swithFragment(ContactFragment.class);
                }
            }

            @Override
            public void onFail(int returnCode, final String errorMessage) {
                if (returnCode == CallBackType.SAVA_JOB_INFOR) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = errorMessage;
                    handler.sendMessage(msg);
                }

            }
        });
        queryWorkinforController = new QueryWorkinforController(getActivity(), new ControllerCallBack() {
            @Override
            public void onSuccess(int returnCode, String value) {
                if (returnCode == GET_JOB_INFOR) {
                    if (UserInfoManager.getInstance().getJobBean() != null) {
                        jobInforBean = UserInfoManager.getInstance().getJobBean();
                        show();
                    }
                }
            }

            @Override
            public void onFail(int returnCode, final String errorMessage) {
                if (returnCode == GET_JOB_INFOR) {
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = errorMessage;
                    handler.sendMessage(msg);
                }
            }
        });
        if (CustomerManagerManager.isCustomerManager()) {
            customerManagerDataController.getCustomeerJob(PersonalDataActivity.custId);
        } else {
            queryWorkinforController.queryWorkInfor();
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

    }

    @Override
    public void destroy() {
//        cache();
    }

    @OnClick({
            R.id.ll_income,
            R.id.tv_income,
            R.id.iv_income,
            ll_job_unit_profession,
            R.id.tv_profession,
            R.id.iv_profession,
            R.id.ll_job_unit_industry,
            R.id.tv_industry,
            R.id.iv_industry,
            R.id.btn_job_sava,
            R.id.ll_unit_job,
            R.id.iv_job,
            R.id.tv_job,
            R.id.iv_unit_address,
            R.id.tv_unit_address,
            R.id.ll_unit_address,
            R.id.btn_previous
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_income:
            case R.id.tv_income:
            case R.id.iv_income:
                OneSelectPopupWindow salary_type = new OneSelectPopupWindow(getActivity(), month_salary());
                salary_type.setOnSelectPopupWindow(new OneSelectPopupWindow.onSelectPopupWindow() {
                    @Override
                    public void onSelectClick(int index, CodeBean select) {
                        jobInforBean.setMonth_pay(select.getCode());
                        tv_income.setText(select.getName());
                    }
                });
                salary_type.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.iv_profession:
            case R.id.tv_profession:
            case R.id.ll_job_unit_profession:
                OneSelectPopupWindow professionPopupWindow = new OneSelectPopupWindow(getActivity(), proFession());
                professionPopupWindow.setOnSelectPopupWindow(new OneSelectPopupWindow.onSelectPopupWindow() {
                    @Override
                    public void onSelectClick(int index, CodeBean select) {
                        jobInforBean.setProfessional(select.getCode());
                        if (select.getName().equals("无业")) {
                            ll_no_work.setVisibility(View.GONE);
                        } else {
                            if ("无业".equals(tv_profession.getText().toString())) {
                                tv_industry.setText("");
                                tv_job.setText("");
                                ed_depaotment.setText("");
                                ed_unit_tel.setText("");
                                ed_unit_district_num.setText("");
                                edUnitDetailedAddress.setText("");
                                ed_uniti_name.setText("");
                                tv_income.setText("");
                                tv_unit_address.setText("");
                            }
                            ll_no_work.setVisibility(View.VISIBLE);
                        }
                        tv_profession.setText(select.getName());
                    }
                });
                professionPopupWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ll_job_unit_industry:
            case R.id.tv_industry:
            case R.id.iv_industry:
                //单位行业
                OneSelectPopupWindow jobinfopopwindow = new OneSelectPopupWindow(getActivity(), getWorkInfo());
                jobinfopopwindow.setOnSelectPopupWindow(new OneSelectPopupWindow.onSelectPopupWindow() {
                    @Override
                    public void onSelectClick(int index, CodeBean select) {
                        jobInforBean.setUnit_industry(select.getCode());
                        tv_industry.setText(select.getName());
                    }
                });
                jobinfopopwindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_job_sava:
                submit();
                break;
            case R.id.iv_job:
            case R.id.tv_job:
            case R.id.ll_unit_job:
                OneSelectPopupWindow job = new OneSelectPopupWindow(getActivity(), AppConfig.job());
                job.setOnSelectPopupWindow(new OneSelectPopupWindow.onSelectPopupWindow() {
                    @Override
                    public void onSelectClick(int index, CodeBean select) {
                        jobInforBean.setTitle(select.getCode());
                        tv_job.setText(select.getName());
                    }
                });
                job.showAtLocation(root, Gravity.BOTTOM, 0, 0);

                break;
            case R.id.iv_unit_address:
            case R.id.tv_unit_address:
            case R.id.ll_unit_address:
                CityPopupWindow window = new CityPopupWindow(getActivity(), CommonUtils.mProvinceDatas, CommonUtils.mCitisDatasMap, CommonUtils.mDistrictDatasMap);
                window.setOnCityPopupWindow(new CityPopupWindow.OnCityPopupWindow() {
                    @Override
                    public void onCityClick(String province, int privinceItem, String city, int cityItem, String district, int districtItem) {
                        if (residenceValue.length() > 0) {
                            residenceValue.delete(0, residenceValue.length());
                        }
                        JobFragment.this.mCurrentProviceName = province;
                        JobFragment.this.mCurrentCityName = city;
                        JobFragment.this.mCurrentDistrictName = district;
                        tv_unit_address.setText(residenceValue.
                                append(mCurrentProviceName).
                                append(TextUtils.isEmpty(mCurrentCityName) ? "" : ",").
                                append(mCurrentCityName).
                                append(TextUtils.isEmpty(mCurrentDistrictName) ? "" : ",").
                                append(mCurrentDistrictName));
                        pCode = provinceList.get(privinceItem);
                        cityode = pCode.getCityList().get(cityItem);
                        dCode = cityode.getDistrictList().get(districtItem);
                        jobInforBean.setUnit_adr(pCode.getProvinceCode() + "," + cityode.getCityCode() + "," + dCode.getZipcode());
                    }
                });
                window.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_previous:
                ((PersonalDataActivity) getActivity()).swithFragment(PerSonalFragment.class);
                break;
        }
    }

    private void cache() {
        mProfessionIndex = proFession().indexOf(new CodeBean(1, "", tv_profession.getText().toString().trim()));
        if (mProfessionIndex != -1) {
            jobInforBean.setProfessional(proFession().get(mProfessionIndex).getCode());
        }
        mUnitIndustryIndex = AppConfig.getWorkInfo().indexOf(new CodeBean(1, "", tv_industry.getText().toString().trim()));
        if (mUnitIndustryIndex != -1) {
            jobInforBean.setUnit_industry(AppConfig.getWorkInfo().get(mUnitIndustryIndex).getCode());//单位行业
        }
        jobInforBean.setUnit_name(ed_uniti_name.getText() == null ? "" : ed_uniti_name.getText().toString().trim());//单位名称
        jobInforBean.setUnit_department(ed_depaotment.getText() == null ? "" : ed_depaotment.getText().toString().trim());//部门

        String districtNum = ed_unit_district_num.getText().toString().trim();//单位区号
        String CompanyPhonenum = ed_unit_tel.getText().toString().trim();//单位电话
        jobInforBean.setUnit_phone((!TextUtils.isEmpty(districtNum) && !TextUtils.isEmpty(CompanyPhonenum)) ? districtNum + "-" + CompanyPhonenum : "");

        mSalaryIndex = month_salary().indexOf(new CodeBean(1, "", tv_income.getText().toString().trim()));
        if (mSalaryIndex != -1) {
            jobInforBean.setMonth_pay(AppConfig.month_salary().get(mSalaryIndex).getCode());//月薪
        }

        mjobIndex = AppConfig.job().indexOf(new CodeBean(1, "", tv_job.getText().toString().trim()));//职位
        if (mjobIndex != -1) {
            jobInforBean.setTitle(AppConfig.job().get(mjobIndex).getCode());
        }

        String[] caddress = tv_unit_address.getText().toString().split(",");
        select = "";
        if (caddress != null && caddress.length == 3) {
            for (ProvinceModel provinceModel : provinceList) {
                if (provinceModel.getName().equals(caddress[0])) {
                    select += provinceModel.getProvinceCode();
                    for (CityModel1 cityModel : provinceModel.getCityList()) {
                        if (cityModel.getName().equals(caddress[1])) {
                            select += "," + cityModel.getCityCode();
                            for (DistrictModel districtModel : cityModel.getDistrictList()) {
                                if (districtModel.getName().equals(caddress[2])) {
                                    select += "," + districtModel.getZipcode();
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }
        jobInforBean.setUnit_adr(select);
        jobInforBean.setUnit_adr_detail(edUnitDetailedAddress.getText() == null ? "" : edUnitDetailedAddress.getText().toString().trim());
        if ("1500".equals(jobInforBean.getProfessional())) {
            //设置默认数据
            jobInforBean.setMonth_pay(AppConfig.month_salary().get(0).getCode());//税后月收入
            jobInforBean.setUnit_industry(AppConfig.getWorkInfo().get(0).getCode());//单位行业
            jobInforBean.setUnit_phone("021" + "-" + "7654321");//单位电话
            jobInforBean.setUnit_name("无");//单位名字
            jobInforBean.setUnit_adr_detail("无");//单位详细地址
            jobInforBean.setUnit_adr(provinceList.get(0).getProvinceCode() + "," + provinceList.get(0).getCityList().get(0).getCityCode() + ","
                    + provinceList.get(0).getCityList().get(0).getDistrictList().get(0).getZipcode());//单位地址
            jobInforBean.setTitle(AppConfig.job().get(0).getCode());//职位
            jobInforBean.setUnit_department("无");//部门
        }
    }

    private void show() {
//        isOnline_offline = SharedPreferencesUtil.getInstance(IApplication.gainContext()).getString(SPKey.PRODUCT_ID);
//        if (isOnline_offline.equals("1")) {
//            ll_no_work.setVisibility(View.VISIBLE);
//            job_unit_profession.setVisibility(View.GONE);
//        } else if (isOnline_offline.equals("2")) {
//            ll_no_work.setVisibility(View.VISIBLE);
//            job_unit_profession.setVisibility(View.GONE);
//        } else {
//            job_unit_profession.setVisibility(View.VISIBLE);
//            if (!TextUtils.isEmpty(jobInforBean.getProfessional())) {
//                int index = proFession().indexOf(new CodeBean(0, jobInforBean.getProfessional(), ""));
//                if (index != -1) {
//                    String name = AppConfig.proFession().get(index).getName();
//                    if ("无业".equals(name)) {
//                        ll_no_work.setVisibility(View.GONE);
//                        tv_profession.setText(name);
//                    } else {
//                        ll_no_work.setVisibility(View.VISIBLE);
//                        tv_profession.setText(name);
//                    }
//                } else {
//                    tv_profession.setText("");
//                    jobInforBean.setProfessional("");
//                }
//            }
        if (!CommonConstant.PRODUCT_OFFLINE.equals(UserInfoManager.getInstance().getPerSonalBean().getProduct_id())) {
            tv_no_work.setVisibility(View.GONE);
            view_work.setVisibility(View.VISIBLE);
        } else {
            tv_no_work.setVisibility(View.VISIBLE);
            view_work.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(jobInforBean.getUnit_industry())) {
            int index = getWorkInfo().indexOf(new CodeBean(0, jobInforBean.getUnit_industry(), ""));
            if (index != -1)
                tv_industry.setText(getWorkInfo().get(index).getName());
            else {
                tv_industry.setText("");
                jobInforBean.setUnit_industry("");
            }
        }

        //单位名称
        if (!TextUtils.isEmpty(jobInforBean.getUnit_name())) {
            ed_uniti_name.setText(jobInforBean.getUnit_name());
        }
        //单位电话
        if (!TextUtils.isEmpty(jobInforBean.getUnit_phone())) {
            String[] telephone = new String[2]; //0 - 区号；1 - 电话
            int i = jobInforBean.getUnit_phone().indexOf("-");
            if (i > 0) {
                telephone = jobInforBean.getUnit_phone().split("-");
            } else if (jobInforBean.getUnit_phone().length() >= 10) {
                if (jobInforBean.getUnit_phone().startsWith("01") || jobInforBean.getUnit_phone().startsWith("02")) {
                    telephone[0] = jobInforBean.getUnit_phone().substring(0, 3);
                    telephone[1] = jobInforBean.getUnit_phone().substring(3);
                } else {
                    telephone[0] = jobInforBean.getUnit_phone().substring(0, 4);
                    telephone[1] = jobInforBean.getUnit_phone().substring(4);
                }
            } else {
                // <10直接显示
                telephone[0] = "";
                telephone[1] = jobInforBean.getUnit_phone();
            }
            try {
                ed_unit_district_num.setText(telephone[0]);
                ed_unit_tel.setText(telephone[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(jobInforBean.getTitle())) {
            int index = AppConfig.job().indexOf(new CodeBean(0, jobInforBean.getTitle(), ""));
            if (index != -1) {
                tv_job.setText(AppConfig.job().get(index).getName());
            } else {
                tv_job.setText("");
                jobInforBean.setTitle("");
            }
        }
        if (!TextUtils.isEmpty(jobInforBean.getMonth_pay())) {
            int index = month_salary().indexOf(new CodeBean(0, jobInforBean.getMonth_pay(), ""));
            if (index != -1)
                tv_income.setText(month_salary().get(index).getName());
            else {
                tv_income.setText("");
                jobInforBean.setMonth_pay("");
            }
        }


        //单位地址
        if (!TextUtils.isEmpty(jobInforBean.getUnit_adr())) {
            try {
                String[] code = jobInforBean.getUnit_adr().split(",");
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
                    if (p != null && c != null && d != null)
                        tv_unit_address.setText(p.getName() + "," + c.getName() + "," + d.getName());
                    else {
                        tv_unit_address.setText("");
                        jobInforBean.setUnit_adr("");
                    }
                } else {
                    tv_unit_address.setText("");
                    jobInforBean.setUnit_adr("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(jobInforBean.getUnit_adr_detail())) {
            edUnitDetailedAddress.setText(jobInforBean.getUnit_adr_detail());
        }
//        if (!TextUtils.isEmpty(jobInforBean.getOther_income())) {
//            tv_income.setText(jobInforBean.getOther_income());
//        }
        //单位部门
        if (!TextUtils.isEmpty(jobInforBean.getUnit_department())) {
            ed_depaotment.setText(jobInforBean.getUnit_department());
        }
//            if (!TextUtils.isEmpty(jobInforBean.getMonth_pay())) {
//                int index = month_salary().indexOf(new CodeBean(0, jobInforBean.getMonth_pay(), ""));
//                if (index != -1)
//                    tv_income.setText(month_salary().get(index).getName());
//                else {
//                    tv_income.setText("");
//                    jobInforBean.setMonth_pay("");
//                }
//
//            }
//        }
    }

    private void submit() {
        cache();
//        if (isOnline_offline.equals("1")) {
//            jobInforBean.setProfessional("0");
//        } else if (isOnline_offline.equals("2")) {
//            jobInforBean.setProfessional("0");
//        } else {
//            if (TextUtils.isEmpty(jobInforBean.getProfessional())) {
//                ToastAlone.showLongToast(getContext(), "请选择职业!");
//                return;
//            }
//        }
        if (!CommonConstant.PRODUCT_OFFLINE.equals(UserInfoManager.getInstance().getPerSonalBean().getProduct_id())) {
//        if (!"1500".equals(jobInforBean.getProfessional()))
            if (TextUtils.isEmpty(jobInforBean.getUnit_industry())) {
                ToastAlone.showLongToast(getContext(), "请选择单位行业!");
                return;
            }
            if (TextUtils.isEmpty(jobInforBean.getUnit_name())) {
                ToastAlone.showLongToast(getContext(), "请填写单位名称!");
                return;
            }
            if (TextUtils.isEmpty(ed_unit_district_num.getText().toString().trim())) {
                ToastAlone.showToast(getContext(), "请填写区号!", Toast.LENGTH_LONG);
                return;
            }
            if (TextUtils.isEmpty(ed_unit_tel.getText().toString().trim())) {
                ToastAlone.showToast(getContext(), "请填写单位固话!", Toast.LENGTH_LONG);
                return;
            }
            if (TextUtils.isEmpty(jobInforBean.getUnit_department())) {
                ToastAlone.showToast(getContext(), "请填写单位部门!", Toast.LENGTH_LONG);
                return;
            }
            if (TextUtils.isEmpty(jobInforBean.getTitle())) {
                ToastAlone.showToast(getContext(), "请选择职位!", Toast.LENGTH_LONG);
                return;
            }

            if (TextUtils.isEmpty(jobInforBean.getMonth_pay())) {
                ToastAlone.showToast(getContext(), "请选择税后月收入!", Toast.LENGTH_LONG);
                return;
            }

//            if (TextUtils.isEmpty(jobInforBean.getUnit_adr())) {
//                ToastAlone.showToast(getContext(), "请选择单位地址!", Toast.LENGTH_LONG);
//                return;
//            }
//            if (TextUtils.isEmpty(jobInforBean.getUnit_adr_detail())) {
//                ToastAlone.showToast(getContext(), "请填写单位详细地址!", Toast.LENGTH_LONG);
//                return;
//            }
        }
        if (CustomerManagerManager.isCustomerManager()) {
            customerManagerDataController.saveCustomeerJob(jobInforBean, PersonalDataActivity.custId);
        } else {
            savajob.addWorkInfor(jobInforBean);
        }

    }

    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.GET_CUSTOMER_JOB) {
            if (UserInfoManager.getInstance().getJobBean() != null) {
                jobInforBean = UserInfoManager.getInstance().getJobBean();
                show();
            }
        } else if (returnCode == CallBackType.SAVE_CUSTOMER_JOB) {
            CommonUtils.closeDialog();
            ((PersonalDataActivity) getActivity()).setComplete(2);
            ((PersonalDataActivity) getActivity()).swithFragment(ContactFragment.class);
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.GET_CUSTOMER_JOB) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        } else if (returnCode == CallBackType.SAVE_CUSTOMER_JOB) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        }
    }
}
