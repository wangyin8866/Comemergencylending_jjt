package com.two.emergencylending.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.example.getlimtlibrary.builder.utils.MyLog;
import com.google.gson.Gson;
import com.two.emergencylending.utils.CommonalityFieldUtils;
import com.two.emergencylending.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wyp on 16/5/13.
 * 封装工具类
 */
public class OKManager {

    private final OkHttpClient client;
    private volatile static OKManager manager;
    private final String TAG = OKManager.class.getSimpleName();//获得类名
    private Handler handler;
    private CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
    //提交json数据
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    //提交字符串
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
    //多文件上伟
    private static final MediaType UPLOAD_FILE = MediaType.parse("multipart/form-data");
    private static final MediaType UPLOAD_IMAGE = MediaType.parse("image/png");
    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 60;
    public final static int WRITE_TIMEOUT = 60;

    private OKManager() {
        client = new OkHttpClient.Builder().readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).
                connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        handler = new Handler(Looper.getMainLooper());
    }

    //采用单例模式获取对象
    public static OKManager getInstance() {
        if (manager == null) {
            synchronized (OKManager.class) {
                manager = new OKManager();
            }
        }
        return manager;
    }

    /**
     * 同步请求，在android开发中不常用，因为会阻塞UI线程
     *
     * @param url
     * @return
     */
    public String syncGetByURL(String url) {
        //构建一个request请求
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();//同步请求数据
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传图片
     *
     * @param url
     * @param map
     * @param callBack
     */
    public void uploadImage(final String url, Map<String, String> map, final Func1 callBack) {
        try {
            String content = new Gson().toJson(map);
            RequestBody requestBody = RequestBody.create(UPLOAD_IMAGE, content);
            Request request = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onFailureLog(url, call, e, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response != null && response.isSuccessful()) {
                        onSuccessJsonStringMethod(response.body().string(), callBack);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     *
     * @param url
     * @param file
     * @param callBack
     */
    public void postAsynFile(final String url, File file, final Func1 callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("file", file.getName(), RequestBody.create(UPLOAD_IMAGE, file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailureLog(url, call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessJsonStringMethod(response.body().string(), callBack);
            }
        });
    }

    /**
     * 文件上传
     *
     * @param url
     * @param file
     * @param params
     * @param callBack
     */
    public void postAsynFile(final String url, String nane, File file, Map<String, String> params, final Func1 callBack) {
        if (params != null) {
        } else {
            params = new HashMap<>();
        }
        params.put("register_platform", CommonalityFieldUtils.getDittchStr());
        MyLog.i("parameter", "params:" + params.toString());
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart(nane, file.getName(), RequestBody.create(UPLOAD_FILE, file));

            if (params != null && !params.isEmpty()) {
//                String gson = new Gson().toJson(params);
//                String des3 = Des3.encode(gson);
//                params.clear();
//                params.put("record", des3);
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.addFormDataPart(entry.getKey(), entry.getValue());
                }
            }
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onFailureLog(url, call, e, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    onSuccessJsonStringMethod(response.body().string(), callBack);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 请求指定的url返回的结果是json字符串
     *
     * @param url
     * @param callBack
     */
    public void asyncJsonStringByURL(final String url, final Func1 callBack) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailureLog(url, call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonStringMethod(response.body().string(), callBack);
                }
            }
        });
    }

    /**
     * 请求返回的是json对象
     *
     * @param url
     * @param callBack
     */
    public void asyncJsonObjectByURL(final String url, final Func4 callBack) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailureLog(url, call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(), callBack);
                }
            }
        });
    }

    /**
     * 请求返回的是byte字节数组
     *
     * @param url
     * @param callBack
     */
    public void asyncGetByteByURL(final String url, final Func2 callBack) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailureLog(url, call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessByteMethod(response.body().bytes(), callBack);
                }
            }
        });
    }

    /**
     * 请求返回结果是imageView类型 bitmap 类型
     *
     * @param url
     * @param callBack
     */
    public void asyncDownLoadImageByURL(final String url, final Func3 callBack) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailureLog(url, call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    byte[] data = response.body().bytes();
                    Bitmap bitmap = new CropSquareTrans().transform(BitmapFactory.decodeByteArray(data, 0, data.length));
                    callBack.onResponse(bitmap);
                }
            }
        });
    }


    /**
     * 提交json请求
     *
     * @param url
     * @param params
     * @param callBack
     */
    public void sendJsonPost(final String url, Map<String, String> params, final Func1 callBack) {
        try {
            System.setProperty("http.keepAlive", "false");
            if (params != null) {
            } else {
                params = new HashMap<>();
            }
            //  params.put("register_platform", CommonalityFieldUtils.getDittchStr());
            MyLog.i("parameter", "params:" + params.toString());
            FormBody.Builder form_builder = new FormBody.Builder();//表单对象，包含以input开始的对象，以html表单为主
            if (params != null && !params.isEmpty()) {
                String gson = new Gson().toJson(params);
                String des3 = HDes3.encode(gson);
                params.clear();
                params.put("record", des3);
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    form_builder.add(entry.getKey(), entry.getValue());
                }
            }
            RequestBody request_body = form_builder.build();
            Request request = new Request.Builder().url(url).post(request_body).build();//采用post方式提交
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onFailureLog(url, call, e, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response != null && response.isSuccessful()) {
                        onSuccessJsonStringMethod(response.body().string(), callBack);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param url
     * @param params
     * @param callBack
     */
    public void sendComplexForm(final String url, Map<String, String> params, final Func1 callBack) {
        sendComplexForm(url, params, callBack, true);
    }

    /**
     * 模拟表单提交
     *
     * @param url
     * @param params
     * @param callBack
     */
    public void sendComplexForm(final String url, Map<String, String> params, final Func1 callBack, boolean isPar) {
        try {
            System.setProperty("http.keepAlive", "false");
            if (params != null) {
            } else {
                params = new HashMap<>();
            }
            if (isPar) {
                params.put("register_platform", CommonalityFieldUtils.getDittchStr());
            }
            MyLog.i("parameter", "params:" + params.toString());
            FormBody.Builder form_builder = new FormBody.Builder();//表单对象，包含以input开始的对象，以html表单为主
            if (params != null && !params.isEmpty()) {
//                String gson = new Gson().toJson(params);
//               String des3 = Des3.encode(gson);
//                params.clear();
//                params.put("record", gson);
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    form_builder.add(entry.getKey(), entry.getValue() == null ? "" : entry.getValue());
                }
            }
//            if (!TextUtils.isEmpty(UserInfoManager.getInstance().getJuid())) {
//                form_builder.add("juid",UserInfoManager.getInstance().getJuid());
//            }
//            if (!TextUtils.isEmpty(UserInfoManager.getInstance().getLogin_token())) {
//                form_builder.add("login_token" ,UserInfoManager.getInstance().getJuid());
//            }
            RequestBody request_body = form_builder.build();
            Request request = new Request.Builder().url(url).post(request_body).build();//采用post方式提交
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onFailureLog(url, call, e, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response != null && response.isSuccessful()) {
                        onSuccessJsonStringMethod(response.body().string(), callBack);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendComplexFormNo(final String url, Map<String, String> params, final Func1 callBack) {
        try {
            System.setProperty("http.keepAlive", "false");
            FormBody.Builder form_builder = new FormBody.Builder();//表单对象，包含以input开始的对象，以html表单为主
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    form_builder.add(entry.getKey(), entry.getValue() == null ? "" : entry.getValue());
                }
            }
            RequestBody request_body = form_builder.build();
            Request request = new Request.Builder().url(url).post(request_body).build();//采用post方式提交
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onFailureLog(url, call, e, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response != null && response.isSuccessful()) {
                        callBack.onResponse(response.body().string());
//                        onSuccessJsonStringMethod(response.body().string(), callBack);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务器提交String请求
     *
     * @param url
     * @param content
     * @param callBack
     */
    public void sendStringByPostMethod(final String url, String content, final Func4 callBack) {
        Request request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_MARKDOWN, content)).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailureLog(url, call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(), callBack);
                }
            }
        });
    }

    /**
     * 向服务器提交String请求
     *
     * @param url
     * @param content
     * @param callBack
     */
    public void sendStringByPost(final String url, String content, final Func1 callBack) throws JSONException {
//        JSONObject json = new JSONObject();
//        json.put("record", content);
        Request request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_MARKDOWN, content)).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailureLog(url, call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonStringMethod(response.body().string(), callBack);
                }
            }
        });
    }

    /**
     * 请求返回的结果是json字符串
     *
     * @param jsonValue
     * @param callBack
     */
    public void onSuccessJsonStringMethod(final String jsonValue, final Func1 callBack) {
        MyLog.i("OkHttp", "re:" + jsonValue);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(jsonValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 请求返回的是byte[] 数组
     *
     * @param data
     * @param callBack
     */
    public void onSuccessByteMethod(final byte[] data, final Func2 callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(data);
                }
            }
        });
    }

    /**
     * 返回响应的结果是json对象
     *
     * @param jsonValue
     * @param callBack
     */
    public void onSuccessJsonObjectMethod(final String jsonValue, final Func4 callBack) {
        handler.post(new Runnable() {
            @Override

            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(new JSONObject(jsonValue));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 请求失败日志处理
     *
     * @param url
     * @param call
     * @param callback
     * @param e
     */
    private void onFailureLog(String url, final Call call, final IOException e, final func callback) {
        LogUtil.e(TAG, "This request is errno!");
        LogUtil.e(TAG, "Url:" + url);
        LogUtil.e(TAG, "Call:" + call);
        LogUtil.e(TAG, e.getLocalizedMessage());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(call, e);
                }
            }
        });
    }


    private interface func {
        void onFailure(Call call, IOException e);
    }

    /**
     * 返回字符串
     */
    public interface Func1 extends func {
        void onResponse(String result);

    }

    /**
     * 返回字节数组
     */
    interface Func2 extends func {
        void onResponse(byte[] result);
    }

    /**
     * 返回视图
     */
    interface Func3 extends func {
        void onResponse(Bitmap bitmap);
    }

    /**
     * 返回json数据
     */
    interface Func4 extends func {
        void onResponse(JSONObject jsonObject);
    }
}
