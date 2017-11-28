package com.two.emergencylending.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinazyjr.lib.util.ToastUtils;
import com.example.getlimtlibrary.builder.utils.StringUtiles;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.two.emergencylending.activity.GestureLockModule.GuestLoginActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.CityModel1;
import com.two.emergencylending.bean.ContactsBean;
import com.two.emergencylending.bean.DistrictModel;
import com.two.emergencylending.bean.ProvinceModel;
import com.two.emergencylending.service.XmlParserHandler;
import com.two.emergencylending.view.LockPassWordUtil;
import com.zyjr.emergencylending.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * 常用工具类
 */
public class CommonUtils {

    public static ProgressDialog pd;


    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Class<? extends Window> clazz = activity.getWindow().getClass();
            try {
                int darkModeFlag = 0;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
                return true;
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Intent
     *
     * @param context
     * @param c
     */
    public static void goToActivity(Context context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    public static void goToActivityForResult(Activity context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivityForResult(intent, 0);
    }

    public static void goToActivityForResult(Activity context, Class c, int request) {
        Intent intent = new Intent(context, c);
        context.startActivityForResult(intent, request);
    }

    public static void goToActivity(Context context, Class c, Intent intent) {
        intent.setClass(context, c);
        context.startActivity(intent);
    }

    public static void goToActivityForResult(Activity context, Class c, Intent intent) {
        intent.setClass(context, c);
        context.startActivityForResult(intent, 0);
    }

    public static void goToActivityForResult(Activity context, Class c, Intent intent, int request) {
        intent.setClass(context, c);
        context.startActivityForResult(intent, request);
    }

    public static void goToActivity(Activity context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }

    /**
     * 改变密码显示状态
     *
     * @param editText
     */
    public static void changeEditeTextInputType(EditText editText, ImageView img_pwd_encrypt) {
        if (editText.getInputType() == 0x20081 || editText.getInputType() == 0x81) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            img_pwd_encrypt.setBackgroundResource(R.drawable.icon_show);
        } else {
            editText.setInputType(0x81);
            img_pwd_encrypt.setBackgroundResource(R.drawable.icon_hide);
        }
        editText.setSelection(editText.getText().toString().length());
    }

    /**
     * 获取版本号versionCode
     *
     * @param _context
     * @param _package
     * @return
     * @Description
     * @author wangwx
     */
    public static int getVerCode(Context _context, String _package) {
        int verCode = -1;
        try {
            verCode = _context.getPackageManager().getPackageInfo(_package, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verCode;
    }

    /**
     * 获取版本名versionName
     *
     * @param _context
     * @param
     * @return
     * @Description
     */
    public static String getVersionName(Context _context)// 获取版本号
    {
        try {
            return _context.getPackageManager().getPackageInfo(_context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "获取版本名错误";
        }
    }

    /**
     * 获取版本名versionName
     *
     * @param _context
     * @param _package
     * @return
     * @Description
     */
    public static String getVersionName(Context _context, String _package)// 获取版本号
    {
        try {
            return _context.getPackageManager().getPackageInfo(_package, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "获取版本名错误";
        }
    }

    /**
     * 收起键盘
     */
    public static void closeKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        //隐藏键盘
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static int sp2px(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
//    /**
//     * 地址省-市-区数据的处理
//     * @param context
//     * @param mProvinceDatas
//     * @param mCitisDatasMap
//     * @param mDistrictDatasMap
//     */
//    /**
//     * 所有省
//     */
//    public static String[] mProvinceDatas;
//    /**
//     * key - 省 value - 市
//     */
//    public static Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
//    /**
//     * key - 市 values - 区
//     */
//    public static Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
//
//    public static void addressDatas(Context context) {
//        List<ProvinceModel> provinceList = null;
//        AssetManager asset = context.getAssets();
//        try {
//            InputStream input = asset.open("province_data.xml");
//            // 创建一个解析xml的工厂对象
//            SAXParserFactory spf = SAXParserFactory.newInstance();
//            // 解析xml
//            SAXParser parser = spf.newSAXParser();
//            XmlParserHandler handler = new XmlParserHandler();
//            parser.parse(input, handler);
//            input.close();
//            // 获取解析出来的数据
//            provinceList = handler.getDataList();
//            // 初始化默认选中的省、市、区
////            if (provinceList != null && !provinceList.isEmpty()) {
////                mCurrentProviceName = provinceList.get(0).getName();
////                List<CityModel> cityList = provinceList.get(0).getCityList();
////                if (cityList != null && !cityList.isEmpty()) {
////                    mCurrentCityName = cityList.get(0).getName();
////                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
////                    mCurrentDistrictName = districtList.get(0).getName();
////                    mCurrentZipCode = districtList.get(0).getZipcode();
////                }
////            }
//            mProvinceDatas = new String[provinceList.size()];
//            for (int i = 0; i < provinceList.size(); i++) {
//                // 遍历所有省的数据
//                mProvinceDatas[i] = provinceList.get(i).getName();
//                List<CityModel1> cityList = provinceList.get(i).getCityList();
//                String[] cityNames = new String[cityList.size()];
//                for (int j = 0; j < cityList.size(); j++) {
//                    // 遍历省下面的所有市的数据
//                    cityNames[j] = cityList.get(j).getName();
//                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
//                    String[] distrinctNameArray = new String[districtList.size()];
//                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
//                    for (int k = 0; k < districtList.size(); k++) {
//                        // 遍历市下面所有区/县的数据
//                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getZipcode(),districtList.get(k).getName());
//                        // 区/县对于的邮编，保存到mZipcodeDatasMap
////                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
//                        distrinctArray[k] = districtModel;
//                        distrinctNameArray[k] = districtModel.getName();
//                    }
//                    // 市-区/县的数据，保存到mDistrictDatasMap
//                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
//                }
//                // 省-市的数据，保存到mCitisDatasMap
//                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//    }
    /**
     * 地址省-市-区数据的处理
     *
     * @param context
     * @param mProvinceDatas
     * @param mCitisDatasMap
     * @param mDistrictDatasMap
     */
    public static List<ProvinceModel> provinceList = null;
    /**
     * 所有省
     */
    public static String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    public static Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    public static Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    public static void addressDatas(Context context) {
        InputStream input = null;
        try {
            AssetManager asset = context.getAssets();
            input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //            // 初始化默认选中的省、市、区
////            if (provinceList != null && !provinceList.isEmpty()) {
////                mCurrentProviceName = provinceList.get(0).getName();
////                List<CityModel> cityList = provinceList.get(0).getCityList();
////                if (cityList != null && !cityList.isEmpty()) {
////                    mCurrentCityName = cityList.get(0).getName();
////                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
////                    mCurrentDistrictName = districtList.get(0).getName();
////                    mCurrentZipCode = districtList.get(0).getZipcode();
////                }
////            }
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel1> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
//                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static Intent goToSelectPhone() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        return intent;
    }

    public static String getPhoneNo(Activity activity, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return "";
            }
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = activity.getContentResolver().query(contactUri, projection,
                    null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
//                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                // Do something with the phone number
//                String name = cursor.getString(nameIndex);
//                return name+","+number;
                return number;
            }
        }
        return "";
    }

    public static void callPhone(final Context context, final String url) {
        Acp.getInstance(context)
                .request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.CALL_PHONE)
//                                .setDeniedMessage("")
//                                .setDeniedCloseBtn("")
//                                .setDeniedSettingBtn("")
//                                .setRationalMessage("")
//                                .setRationalBtn("")
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + url));
                                context.startActivity(intent);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                            }
                        });
    }


    /**
     * 根据网络环境获取ip地址
     *
     * @param context
     * @return
     */
    public static String getIp(Context context) {
        if (isWifiConnected(context)) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return int2ip(ipAddress);
        } else {
            return getIpAddress();
        }
    }

    /**
     * 获取3G网络IP
     *
     * @return
     */
    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    //是否连接WIFI
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static ProgressDialog showDialog(Context context, String message) {
//        if (pd != null && pd.isShowing()) {
//            pd.dismiss();
//        }
        pd = null;
        pd = new ProgressDialog(context);
        pd.setMessage(message);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.show();
        return pd;
    }

    public static void closeDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    public static boolean checkNull(String value, String remind) {
        if (StringUtil.isNullOrEmpty(value)) {
            ToastUtils.showShort(IApplication.globleContext, remind);
            return true;
        }
        return false;
    }

    public static int getStatusBarColorNormal() {
        return R.color.white;
    }


    public static void DealGeatherPassword(Context mContext) {
        boolean isShowGestureView = SharedPreferencesUtil.getInstance(mContext).getBoolean("isShowGestureView", false);
        if (isShowGestureView) {
            LogUtil.d("CommonUtils", "已经显示手势密码页面,不再处理");
            return;
        }
        boolean isOpenGesturePassword = LockPassWordUtil.getPasswordStatus(mContext);
        boolean isHome = SharedPreferencesUtil.getInstance(mContext).getBoolean("isHome", false);
        LogUtil.d("CommonUtils", "HomeFragmentHost中获取的保存密码为=" + isOpenGesturePassword + "=是否点击了HOME退出键=" + isHome + "=是否点击了BACK退出键=" + IApplication.isBack);
        // && !IApplication.isBack
        if (isOpenGesturePassword && isHome) {// 保存了手势密码并且只是点击了HOME退出键时
            String userId = LockPassWordUtil.getPassword(mContext);
            if (!TextUtils.isEmpty(userId)) {
                Intent i = new Intent(mContext, GuestLoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("isHome", false);
                mContext.startActivity(i);
                SharedPreferencesUtil.getInstance(mContext).setBoolean("isHome", false);
            }
        }
    }

    //获取一个随机数
    public static int getOneRandom(int size) {
        Random random = new Random();
        return random.nextInt(size);
    }

    //获取指定个数随机数
    public static List<Integer> getRandomNoRepeat(int total, int randomCount) {
        List<Integer> randomNum = new ArrayList<Integer>();
        Random random = new Random();
        while (randomNum.size() < randomCount && randomNum.size() < total) {
            int randomInt = random.nextInt(total);
            if (!randomNum.contains(randomInt)) {
                randomNum.add(randomInt);
            }
        }
        return randomNum;
    }

    //判断是否有网
    private static boolean isNetworkAvailable() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) IApplication.globleContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
//                    System.out.println(i + "===状态===" + networkInfo[i].getState());
//                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static boolean isNetAvailable() {
        if (!isNetworkAvailable()) {
            ToastUtils.showShort(IApplication.globleContext, "当前没有网络，请连接网络！");
            return false;
        }
        return true;
    }

    /**
     * 查找该联系人的phone信息
     */
    public static List<ContactsBean> getUserContacts(Context context) {
        List<ContactsBean> list = new ArrayList<>();
        ContactsBean contactsBean;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
//        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
//                null, null, null, null);
            int contactIdIndex = 0;
            int nameIndex = 0;
            if (cursor != null && cursor.getCount() > 0) {
                contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                while (cursor.moveToNext() && list.size() < 10) {
                    contactsBean = new ContactsBean();
                    String contactId = cursor.getString(contactIdIndex);
                    String name = cursor.getString(nameIndex);
                    Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                            null, null);
                    int phoneIndex = 0;
                    if (phones != null) {
                        if (phones.getCount() > 0) {
                            phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        }
                        while (phones.moveToNext()) {
                            String phoneNumber = phone(phones.getString(phoneIndex));
                            contactsBean.setContact_name(name);
                            contactsBean.setContact_phone(phoneNumber);
                        }
                        if (StringUtil.isNullOrEmpty(contactsBean.getContact_phone())) {
                            continue;
                        }
                        list.add(contactsBean);
                    }
                }
            }
        } catch (SecurityException e) {
            Log.e("query", "permission denied");
        }
        if (cursor != null)
            cursor.close();
        return list;
    }

    /**
     * 查询所有联系人
     *
     * @param mContext
     * @return
     */
    public static List<ContactsBean> queryContactPhoneNumber(Context mContext) {
        List<ContactsBean> mConnectInfos = new ArrayList<>();
        String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                cols, null, null, null);
        int count = 0;
        try {
//            count = cursor.getCount() < 100 ? cursor.getCount() : 100;
            count = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            ContactsBean inof = new ContactsBean();
            // 取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String name = cursor.getString(nameFieldColumnIndex);
            String number = StringUtiles.trimBlank(cursor.getString(numberFieldColumnIndex));
            inof.setContact_name(name);
            inof.setContact_phone(number);
            mConnectInfos.add(inof);
        }
        if (cursor != null) {
            cursor.close();
        }
        return mConnectInfos;
    }

    public static String phone(String phone) {
        if (StringUtil.isNotEmpty(phone)) {
            if (phone.contains(" ")) {
                phone = phone.replace(" ", "").toString();
            }
            if (phone.contains("-")) {
                phone = phone.replace("-", "").toString();
            }
            if (phone.length() > 11) {
                phone = phone.substring(phone.length() - 11);
            }
            if (phone.length() != 11) {
                phone = "";
            }
            if (!(RegularExpUtil.isMobile(phone))) {
                phone = "";
            }
            return phone;
        } else {
            return "";
        }
    }

    public static String phoneNumHide(String phone) {
        if (!StringUtil.isNullOrEmpty(phone)) {
            if (phone.length() == 11) {
                String phoneTail = phone.substring(7, 11);
                phone = phone.substring(0, 3) + "****" + phoneTail;
            }
            return phone;
        } else {
            return "";
        }
    }
}
