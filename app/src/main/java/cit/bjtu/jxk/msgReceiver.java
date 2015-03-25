package cit.bjtu.jxk;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

import cit.bjtu.utils.DBService;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by zacharyjia on 2014/7/31.
 */
public class msgReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);

        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        if(title.equals("msg")) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            String time = "";
            String sender = "";
            try {
                JSONTokener jsonParser = new JSONTokener(extra);
                JSONObject values = (JSONObject) jsonParser.nextValue();
                time = values.getString("time");
                sender = values.getString("sender");
                //将接收到的消息写入本地数据库中
                DBService dbService = new DBService(context);
                dbService.addMsg(sender, time, message);


                //判断程序是否运行中，若未在运行，则弹出notification
                ActivityManager am = null;
                boolean isAppRunning = false;
                am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                ComponentName cn = am.getRunningTasks(2).get(0).topActivity;
                if(cn != null)
                {
                    if(cn.getPackageName().equals("cit.bjtu.jxk"))
                    {
                        isAppRunning = true;
                    }
                }
                if (isAppRunning) {
                    if(sp.getBoolean("newmsg", true)) {
                        Toast.makeText(context, "计小科发来一条新消息", Toast.LENGTH_SHORT).show();
                        if (sp.getBoolean("zhendong", true)) {
                            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(300);
                            vibrator.vibrate(300);
                        }
                        if(sp.getBoolean("ring", true)){
                            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            MediaPlayer mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource(context, alert);
                            context.getSystemService(Context.AUDIO_SERVICE);
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                            mediaPlayer.setLooping(false);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }
                    }
                } else {
                    //判断是否允许后台推送
                    if (sp.getBoolean("push", true)) {
                        NotificationManager notificationManager = (NotificationManager)
                                context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
                        @SuppressWarnings("deprecation")
                        Notification notification = new Notification(R.drawable.ic_launcher, "新消息", System.currentTimeMillis());
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        notification.defaults |= Notification.DEFAULT_ALL;
                        CharSequence contentTitle = "来自计小科的消息"; // 通知栏标题
                        CharSequence contentText = message; // 通知栏内容
                        Intent notificationIntent = new Intent(context, MainActivity.class); // 点击该通知后要跳转的Activity
                        PendingIntent contentItent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
                        notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
                        notificationManager.notify(0, notification);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(title.equals("article"))
        {
            String article_title = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

            try {
                JSONTokener jsonParser = new JSONTokener(extra);
                JSONObject values = (JSONObject) jsonParser.nextValue();
                String id = values.getString("ID");

                //判断是否允许后台推送
                if(sp.getBoolean("push", true)) {
                    NotificationManager notificationManager = (NotificationManager)
                            context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
                    @SuppressWarnings("deprecation")
                    Notification notification = new Notification(R.drawable.ic_launcher, article_title, System.currentTimeMillis());
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    notification.defaults |= Notification.DEFAULT_ALL;
                    CharSequence contentTitle = "计小科"; // 通知栏标题
                    CharSequence contentText = article_title; // 通知栏内容
                    Intent notificationIntent = new Intent(context, ArticleActivity.class); // 点击该通知后要跳转的Activity
                    Bundle bundle_article = new Bundle();
                    bundle_article.putString("id", id);
                    notificationIntent.putExtras(bundle_article);
                    PendingIntent contentItent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
                    notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
                    notificationManager.notify(0, notification);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
