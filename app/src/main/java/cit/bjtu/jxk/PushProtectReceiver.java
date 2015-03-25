package cit.bjtu.jxk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Zachary on 2014/9/4.
 */
public class PushProtectReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        JPushInterface.init(context.getApplicationContext());
    }
}
