package cit.bjtu.utils;

import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by zacharyjia on 2014/7/7.
 */
public class HttpUtils {

    private HttpUtilsCallBack callBack;

    private Handler handler = new Handler(){
        @Override
        public  void handleMessage(Message msg)
        {
            callBack.method((String)msg.obj);
        }
    };

    public void Post(final Map<String, String> params, final String url, HttpUtilsCallBack callBack)
    {
        this.callBack = callBack;
        new Thread(){
            public void run(){
                String result = PostUtils.submitPostData(params, "UTF-8", url);
                handler.obtainMessage(0, result).sendToTarget();
            }
        }.start();
    }


    public static InputStream getStreamFromURL(String imageURL) {
        InputStream in=null;
        try {
            URL url=new URL(imageURL);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            in=connection.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;

    }
}
