//package com.android.base.net.interceptors;
//
//import android.app.Application;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.text.TextUtils;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//import com.android.base.net.model.base.bean.EncrypBean;
//import com.android.base.utils.JsonUtil;
//import com.android.base.utils.aes.AESUtils;
//import com.lzy.okgo.utils.OkLogger;
//import com.orhanobut.logger.Logger;
//import okhttp3.*;
//import okio.Buffer;
//import okio.BufferedSource;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//
//import static kotlin.text.Charsets.UTF_8;
//
//public class EncripInterceptor implements Interceptor {
//
//    private Application application;
//    private SharedPreferences mSharedPreferences;
//    private  LocalBroadcastManager broadcastManager;
//    private static final String LOG="EncripInterceptor";
//
//
//    public EncripInterceptor(Application application) {
//        this.application = application;
//        if(mSharedPreferences==null){
//            mSharedPreferences=application.getSharedPreferences("config", Context.MODE_PRIVATE);
//            broadcastManager=LocalBroadcastManager.getInstance(application);
//        }
//    }
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//
//        Request originalRequest = chain.request();
//        HttpUrl originalHttpUrl = originalRequest.url();
//        String originalUrl = originalHttpUrl.url().toString();
//        String method = originalRequest.method();
//
//        Logger.i(LOG, "originalUrl-->" + originalUrl);
//        Logger.i(LOG, "method-->" + originalRequest.method());
//
//        final Response originalResponse = chain.proceed(originalRequest);
//        // 获取返回的数据字符串
//        ResponseBody responseBody = originalResponse.body();
//        BufferedSource source = originalResponse.body().source();
//        source.request(Integer.MAX_VALUE);
//        Buffer buffer = source.buffer();
//        Charset charset = UTF_8;
//        MediaType contentType = responseBody.contentType();
//        if (contentType != null)
//        {
//            charset = contentType.charset();
//            charset = charset == null ? UTF_8 : charset;
//        }
//        String bodyString = buffer.clone().readString(charset);
//        Logger.e("----bodyString------>"+bodyString);
//        bodyString=bodyString.replace("\r\n","");
//        Logger.e("----bodyString1------>"+bodyString);
//        EncrypBean encrypBean= JsonUtil.toBean(bodyString,EncrypBean.class);
//        Logger.i("解密前：" + encrypBean.getEncrypt());
//        String body= AESUtils.decrypt(encrypBean.getEncrypt());
//        Logger.e("----body------>"+body);
//        //需要解密 response 解密
//        return originalResponse.newBuilder()
//                .body(ResponseBody.create(contentType, body))
//                .build();
//
//    }
//
//
//}
