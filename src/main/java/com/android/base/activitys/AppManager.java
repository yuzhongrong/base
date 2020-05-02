package com.android.base.activitys;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Stack;



public  class AppManager {

    protected static Stack<Activity> activityStack;
    private static AppManager instance;




    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }


    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }


    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }


    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }


    public void finishActivity(Activity activity) {
        if (activity != null&&activityStack!=null) {
            activityStack.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
            activity = null;
        }
    }


    public void finishActivityClass(Class<?> cls) {
        for (Activity a:activityStack) {
            if (a.getClass().equals(cls)) {
              finishActivity(a);
            }
        }
    }


    public void finishActivityClassName(String clsName) {
        for (Activity a:activityStack) {
            if (a.getClass().getSimpleName().equals(clsName)) {
                finishActivity(a);
            }
        }
    }


    public void finishActivity(Class<?> cls) {
        Stack<Activity> tempStack = new Stack<>();
        for (Activity activity : activityStack) {
            if (!activity.getClass().equals(cls)) {
                if (!activity.isFinishing())
                    activity.finish();
                tempStack.add(activity);
            }
        }
        activityStack.removeAll(tempStack);
    }


    public void finishAllActivity() {

        if(activityStack==null)return;

        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }




    public void restartApp(Context context,String words) {

//        Intent intent = new Intent(MyApp.getContext(), SplashActivity.class);
//        intent.putExtra("words",words);
//        int mPendingIntentId = 10086;
//        PendingIntent mPendingIntent = PendingIntent.getActivity(MyApp.getContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager mgr = (AlarmManager) MyApp.getContext().getSystemService(ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis(), mPendingIntent);
//        System.exit(0);

//        Intent intent1 = new Intent(MyApp.getContext(),SplashActivity.class);
//        intent1.putExtra("words",words);
//         PendingIntent pi = PendingIntent.getActivity(context,0, intent1,0);
//         AlarmManager alarmManager=(AlarmManager)context.getSystemService(ALARM_SERVICE);
//         alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+1000,pi);
//        android.os.Process.killProcess(android.os.Process.myPid());

        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
//        intent.putExtra("words",words);
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AppManager.getInstance().finishAllActivity();
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10, restartIntent);
        android.os.Process.killProcess(android.os.Process.myPid());


    }



    public void SwitchPeers(Context context,Class activity,String ip) {
//        AppManager.getInstance().finishAllActivity();
//        Intent intent = new Intent(context,activity);
//        //更新节点
//        ACacheUtil.get().put("imhost",ip);
//        ACacheUtil.get().put("apphost",ip);
//        //退出当前账号
//        ChatManagerHolder.gChatManager.disconnect(true);
//        SharedPreferences sp =MyApp.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
//        sp.edit().clear().commit();
//
//        String id = sp.getString("id", null);
//        String token = sp.getString("token", null);
//        Logger.e("-----清理后验证下---->$id  $token");
//
//        int mPendingIntentId = 10087;
//        PendingIntent mPendingIntent = PendingIntent.getActivity(MyApp.getContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager mgr = (AlarmManager) MyApp.getContext().getSystemService(ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis(), mPendingIntent);
//        System.exit(0);
    }








}
