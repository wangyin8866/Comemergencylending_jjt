package com.two.emergencylending.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ScrollView;

import com.chinazyjr.lib.util.ToastUtils;
import com.refresh.PullToRefreshBase;
import com.refresh.PullToRefreshScrollView;
import com.two.emergencylending.adapter.CredentialAdapter;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.GetCredentialState;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.view.Topbar;
import com.two.emergencylending.widget.NGridView;
import com.zyjr.emergencylending.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * 项目名称：急借通
 * 类描述：认证页面(废弃)
 * 创建人：wyp
 * 创建时间：2016/5/26 14:49
 * 修改人：wyp
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class CredentialsActivity extends BaseActivity implements Topbar.topbarClickListener, AdapterView.OnItemClickListener, View.OnClickListener {
    @Bind(R.id.credentials_topbar)
    Topbar topbar;
    @Bind(R.id.gv_credential)
    NGridView gv_credential;
    private static int[] credentialspic = {R.drawable.icon_operationn};
    private String[] credentialText = {"手机运营商认证"};
    private boolean[] credentialverifty = {false};
    private String[] isCredentState = {"未认证"};
    private CredentialAdapter credentialAdapter;
    private String id, token;
    @Bind(R.id.btn_credentials)
    Button btn_credentials;
    Map<String, String> parameter;
    GetCredentialState mGetCredentialState;
    PullToRefreshScrollView pullToRefreshScrollView;

    @Override
    public int setContent() {
        return R.layout.activity_credentials;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }


    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        mGetCredentialState = new GetCredentialState(getContext(), null);
        mGetCredentialState.getState();
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refresh);
        btn_credentials.setClickable(false);
        btn_credentials.setBackgroundResource(R.drawable.button_grey_selector);
        String status = UserInfoManager.getInstance().getUserStatuModels().getUserAuthStatusShouji();
        initPhoneStatus(status);

//        if (Integer.parseInt(UserInfoManager.getInstance().getUserStatuModels().getUserAuthStatusJindong())==0||
//                Integer.parseInt(UserInfoManager.getInstance().getUserStatuModels().getUserAuthStatusJindong())==1){
//            credentialspic[1] = R.drawable.icon_jingdongn;
//            credentialverifty[1]=false;
//        }else if (Integer.parseInt(UserInfoManager.getInstance().getUserStatuModels().getUserAuthStatusJindong())==2||
//                Integer.parseInt(UserInfoManager.getInstance().getUserStatuModels().getUserAuthStatusJindong())==3){
//            credentialverifty[1]=true;
//            credentialspic[1] = R.drawable.icon_jingdong;
//        }
        credentialAdapter = new CredentialAdapter(this, credentialspic, credentialText, credentialverifty, isCredentState);
        gv_credential.setAdapter(credentialAdapter);
    }

    @Override
    public void setData() {

    }

    public void initPhoneStatus(String status) {
        if (!StringUtil.isNullOrEmpty(status)) {
            if (Integer.parseInt(status) == 0) {
                credentialverifty[0] = false;
                credentialspic[0] = R.drawable.icon_operationn;
                isCredentState[0] = "未认证";
                btn_credentials.setClickable(false);
                btn_credentials.setBackgroundResource(R.drawable.button_grey_selector);
            } else if (Integer.parseInt(status) == 7) {
                credentialverifty[0] = false;
                credentialspic[0] = R.drawable.icon_operationn;
                isCredentState[0] = "认证中";
                btn_credentials.setClickable(false);
                btn_credentials.setBackgroundResource(R.drawable.button_grey_selector);
            } else if (Integer.parseInt(status) == 8) {
                credentialverifty[0] = false;
                credentialspic[0] = R.drawable.icon_operationn;
                isCredentState[0] = "认证失败";
                btn_credentials.setClickable(false);
                btn_credentials.setBackgroundResource(R.drawable.button_grey_selector);
            } else if (Integer.parseInt(status) == 9) {
                credentialverifty[0] = true;
                credentialspic[0] = R.drawable.icon_operation;
                isCredentState[0] = "认证成功";
                btn_credentials.setClickable(true);
                btn_credentials.setBackgroundResource(R.drawable.button_orange_selector);
            }
        }
    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
        gv_credential.setOnItemClickListener(this);
        btn_credentials.setOnClickListener(this);
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                LogUtil.i("=====", "====================");
                new GetCredentialState(CredentialsActivity.this, new ControllerCallBack() {

                    @Override
                    public void onSuccess(int returnCode, String value) {
                        pullToRefreshScrollView.onRefreshComplete();
                        initPhoneStatus(value);
                        credentialAdapter.notifyDataSetInvalidated();
                        credentialAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFail(int returnCode, String errorMessage) {
                        pullToRefreshScrollView.onRefreshComplete();
                    }
                }).getState();
            }
        });
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
        pullToRefreshScrollView.autoRefresh();
        new GetCredentialState(this, new ControllerCallBack() {
            @Override
            public void onSuccess(int returnCode, String value) {
                pullToRefreshScrollView.onRefreshComplete();
                initPhoneStatus(value);
//                if (Integer.parseInt(UserInfoManager.getInstance().getUserStatuModels().getUserAuthStatusJindong())==0||
//                        Integer.parseInt(UserInfoManager.getInstance().getUserStatuModels().getUserAuthStatusJindong())==1){
//                    credentialspic[1] = R.drawable.icon_jingdongn;
//                    credentialverifty[1]=false;
//                }else if (Integer.parseInt(UserInfoManager.getInstance().getUserStatuModels().getUserAuthStatusJindong())==2||
//                        Integer.parseInt(UserInfoManager.getInstance().getUserStatuModels().getUserAuthStatusJindong())==3){
//                    credentialverifty[1]=true;
//                    credentialspic[1] = R.drawable.icon_jingdong;
//                }
                credentialAdapter.notifyDataSetInvalidated();
                credentialAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(int returnCode, String errorMessage) {
                pullToRefreshScrollView.onRefreshComplete();
            }
        }).getState();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void leftClick() {
        IApplication.isRefresh = true;
        finish();
    }

    @Override
    public void rightClick() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        IApplication.isRefresh = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_credentials:
                credentials();
                break;
        }
    }

    private void credentials() {
        if (!isCredentState[0].equals("认证成功")) {
            ToastAlone.showLongToast(getContext(), "认证通过后，再提交");
            return;
        }
        parameter = new HashMap<String, String>();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        CommonUtils.showDialog(this, "正在加载中...");
        OKManager.getInstance().sendComplexForm(NetContants.TIJIAOAUTH, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                LogUtil.d("CredentialsActivity", response);
                try {
                    if (!StringUtil.isNullOrEmpty(response)) {
                        LogUtil.d(TAG, response);
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            CommonUtils.goToActivity(CredentialsActivity.this, ReviewCompleteActivity.class);
                            finish();
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(CredentialsActivity.this);
                        } else {
                            ToastUtils.showShort(CredentialsActivity.this, msg);
                            LogUtil.d(TAG, msg);
                        }
                    } else {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    }
                } catch (Exception e) {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Bundle bundle = new Bundle();
//        MxParam mxParam = new MxParam();
//        mxParam.setUserId("id");
//        mxParam.setApiKey(Moxie.mApiKey);
//        mxParam.setBannerBgColor(Moxie.mBannerColor);
//        mxParam.setBannerTxtColor(Moxie.mTextColor);
//        mxParam.setThemeColor(Moxie.mThemeColor);
//        mxParam.setAgreementUrl(Moxie.mAgreementUrl);
//        if (credentialverifty[position]) {
//            ToastAlone.showLongToast(getContext(), "认证通过不能再次点击!");
//            return;
//        }
        if (isCredentState[0].equals("认证成功")) {
            ToastAlone.showLongToast(getContext(), "认证成功不能再次点击!");
            return;
        } else if (isCredentState[0].equals("认证中")) {
            ToastAlone.showLongToast(getContext(), "认证中不能再次点击!");
            return;
        }
        switch (position) {
            case 0:
//                mxParam.setFunction(MxParam.PARAM_FUNCTION_CARRIER);
//                HashMap<String, String> extendParam = new HashMap<String, String>();
//                extendParam.put(MxParam.PARAM_CARRIER_IDCARD,"");
//                extendParam.put(MxParam.PARAM_CARRIER_PHONE, "");
//                extendParam.put(MxParam.PARAM_CARRIER_NAME, "");
//                extendParam.put(MxParam.PARAM_CARRIER_PASSWORD, "");
//                extendParam.put(MxParam.PARAM_COMMON_EDITABLE, MxParam.PARAM_COMMON_YES);
//                mxParam.setExtendParams(extendParam);
//                bundle.putParcelable("param", mxParam);
//                Intent intent2 = new Intent(getContext(),com.moxie.client.MainActivity.class);
//                intent2.putExtras(bundle);
//                startActivityForResult(intent2, 0);
                CommonUtils.goToActivity(getContext(), MobileAuthenticaActivity.class);
                break;
//            case 1:
//                mxParam.setFunction(MxParam.PARAM_FUNCTION_JINGDONG);
//                mxParam.setSubType(MxParam.PARAM_SUBTYPE_SDK);
//                bundle.putParcelable("param", mxParam);
//                Intent intent3 = new Intent(getContext(),com.moxie.client.MainActivity.class);
//                intent3.putExtras(bundle);
//                startActivityForResult(intent3, 0);
//                break;
//            case 2:
//                mxParam.setFunction(MxParam.PARAM_FUNCTION_TAOBAO);
//                bundle.putParcelable("param", mxParam);
//                Intent intent4 = new Intent(getContext(),com.moxie.client.MainActivity.class);
//                intent4.putExtras(bundle);
//                startActivityForResult(intent4, 0);
//                break;
            default:
                break;
        }
    }


    /**
     * 接收SDK的回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
//            case RESULT_OK:
//                Bundle b = data.getExtras();              //data为B中回传的Intent
//                String result = b.getString("result");    //result即为回传的值(JSON格式)
//                LogUtil.e("MainActivity", "result=" + result);
//                /**
//                 *  result的格式如下：
//                 *  1.1.没有进行账单导入(后台没有通知)
//                 *      {"code" : -1, "function" : "mail", "searchId" : "", "taskId" : "}
//                 *  1.2.服务不可用(后台没有通知)
//                 *      {"code" : -2, "function" : "mail", "searchId" : "", "taskId" : "}
//                 *  1.3.任务创建失败(后台没有通知)
//                 *      {"code" : -3, "function" : "mail", "searchId" : "", "taskId" : "}
//                 *  2.账单导入失败(后台有通知)
//                 *      {"code" : 0, "function" : "mail", "searchId" : "3550622685459407187", "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112"}
//                 *  3.账单导入成功(后台有通知)
//                 *      {"code" : 1, "function" : "mail", "searchId" : "3550622685459407187", "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112"}
//                 *  4.账单导入中(后台有通知)
//                 *      {"code" : 2, "function" : "mail", "searchId" : "3550622685459407187", "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112"}
//                 */
//
//                if(TextUtils.isEmpty(result)) {
//                    Toast.makeText(getContext(), "用户没有进行导入操作!", Toast.LENGTH_SHORT).show();
//                } else {
//                    try {
//                        int code = 0;
//                        String emailId = "";
//                        token =Moxie.mToken;
//                        JSONObject jsonObject = new JSONObject(result);
//
//                        code = jsonObject.getInt("code");
//                        if(code == -1) {
//                        } else if(code == 0) {
//                            Toast.makeText(getContext(), "导入失败!", Toast.LENGTH_SHORT).show();
//                        } else if(code == 1) {
//                            switch (jsonObject.getString("function")) {
//                                case "mail":
//                                    //邮箱导入
//                                    emailId = jsonObject.getString("searchId");
//                                    GetImportResult getImportResult = new GetImportResult(emailId);
//                                    getImportResult.execute();
//                                    break;
//                                case "onlinebank":
//                                    //网银导入
//                                    openWebViewResult("http://api.51datakey.com:8093/h5/result/onlinebank/cardlist.html?task_id=" + jsonObject.getString("searchId"));
//                                    break;
//
//                                case "carrier":
//                                    //运营商导入
//                                    openWebViewResult("https://api.51datakey.com/h5/result/carrier/tel.html?mobile=" + jsonObject.getString("searchId") + "&token=" + token);
//                                    break;
//                                case "qq":
//                                    //QQ导入
//                                    openWebViewResult("https://api.51datakey.com/h5/result/qq/?qq=" + jsonObject.getString("searchId") + "&token=" + token);
//                                    break;
//                                case "alipay":
//                                case "taobao":
//                                case "jingdong":
//                                    //支付宝、淘宝、京东导入
//                                    openWebViewResult("https://api.51datakey.com/h5/result/" + jsonObject.getString("function") + "/?mappingId=" + jsonObject.getString("searchId") + "&token=" + token);
//                                    break;
//
//                                case "insurance":
//                                    //保险导入
//                                    openWebViewResult("https://api.51datakey.com/h5/result/" + jsonObject.getString("function") + "/?mappingId=" + jsonObject.getString("searchId") + "&token=" + token);
//                                    break;
//
//                                case "chsi":
//                                    //学信网导入
//                                    openWebViewResult("https://api.51datakey.com/h5/result/chsi/index.html?taskid=" + jsonObject.getString("searchId") + "&token=" + token);
//                                    break;
//
//                                case "fund":
//                                    //公积金导入
//                                    openWebViewResult("https://api.51datakey.com/h5/result/fund/index.html?taskid=" + jsonObject.getString("searchId") + "&token=" + token);
//                                    break;
//
//                                case "zhengxin":
//                                    //征信导入
//                                    openWebViewResult("https://api.51datakey.com/h5/result/zhengxin/index.html?mappingId=" + jsonObject.getString("searchId") + "&token=" + token);
//                                    break;
//
//                                case "maimai":
//                                    //脉脉导入
//                                    openWebViewResult("https://api.51datakey.com/h5/result/maimai/index.html?taskId=" + jsonObject.getString("searchId") + "&token=" + token);
//                                    break;
//
//                                default:
//                                    break;
//                            }
//                        }
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//    }

    /**
     * 打开结果展示页
     * @param url
     */
//    private void openWebViewResult(String url){
//        Bundle bundle = new Bundle();
//        bundle.putString("openUrl", url);
//        Intent intent = new Intent(getContext(), WebViewActivity.class);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }
    /**
     * 获取邮箱导入结果
     */
//    class GetImportResult extends CommonAsyncTask<Void, Void, Integer> {
//
//        String emailId = "";
//
//        public GetImportResult (String emailId) {
//            this.emailId = emailId;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Integer doInBackground(Void... params) {
//            String url = "https://api.51datakey.com/email/v1/bills?email_id=" + emailId;
//            HashMap<String, String> hmHeader = new HashMap<>();
//            hmHeader.put("Authorization", "token " + token);
//
//            try {
//                String body = HttpUrlConnection.getInstance().getString(url, hmHeader);
//                JSONObject jsonObject = new JSONObject(body);
//                if (jsonObject.has("total_size"))
//                    return jsonObject.getInt("total_size");
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return 0;
//        }
//
//        @Override
//        protected void onPostExecute(Integer result) {
//            super.onPostExecute(result);
//
//            if(result > 0) {
//                Bundle bundle = new Bundle();
//                bundle.putString("openUrl", "https://api.51datakey.com/h5/result/mail/cardlist.html?emailId="+emailId);
//                Intent intent = new Intent(getContext(), WebViewActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            } else {
//                Toast.makeText(getContext(), "没有获取到账单!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

}
