package com.android.base.net.api;

import com.android.base.app.BaseApp;
import com.android.base.utils.ManifestUtil;
import com.android.base.net.AppConst;
import com.android.base.net.helper.JsonConvert;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okrx2.adapter.ObservableBody;


import io.reactivex.Observable;




public class ImRetrofitService {

    private static String homeDataList = AppConst.BASE_URL + "article/list/{page}/json";
    private static String homeBannerData = AppConst.BASE_URL + "banner/json";
    private static String hotKeyUrl = AppConst.BASE_URL + "hotkey/json";
    private static String loginUrl = AppConst.BASE_URL + "user/login";
    private static String registUrl = AppConst.BASE_URL + "user/register";
    private static String getClollectData = AppConst.BASE_URL + "lg/collect/list/0/json";













}
