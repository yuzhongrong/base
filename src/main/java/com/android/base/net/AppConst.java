package com.android.base.net;


import com.android.base.BuildConfig;
import com.android.base.app.BaseApp;

public class AppConst {

    public static  String BASE_URL ="http://91.193.100.29:8888";
    public static String TESTADDRESS="";
    public static boolean isPay= !BuildConfig.DEBUG;
    public static String walletdir=BaseApp.getContext().getFilesDir().getPath()+"/"+"cmcwallet";

}
