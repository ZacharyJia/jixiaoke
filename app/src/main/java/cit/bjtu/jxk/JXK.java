package cit.bjtu.jxk;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zacharyjia on 2014/8/2.
 */
public class JXK extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
    }
}
