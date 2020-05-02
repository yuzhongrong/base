package com.android.base.viewmodel.share;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import com.android.base.viewmodel.share.AppScopeViewModel;

public class WfcUiKit {

    private ViewModelStore viewModelStore;
    private static ViewModelProvider viewModelProvider;
    private static Application mApplication;

    public  void init(Application application){
        mApplication=application;
        viewModelStore = new ViewModelStore();
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(mApplication);
        viewModelProvider = new ViewModelProvider(viewModelStore, factory);
    }

    /**
     * 当{@link androidx.lifecycle.ViewModel} 需要跨{@link android.app.Activity} 共享数据时使用
     */
    public static <T extends ViewModel> T getAppScopeViewModel(@NonNull Class<T> modelClass) {
        if (!AppScopeViewModel.class.isAssignableFrom(modelClass)) {
            throw new IllegalArgumentException("the model class should be subclass of AppScopeViewModel");
        }
        return viewModelProvider.get(modelClass);
    }





}
