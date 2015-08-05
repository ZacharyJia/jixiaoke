package cit.bjtu.jxk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import cit.bjtu.utils.HttpUtils;
import cit.bjtu.utils.HttpUtilsCallBack;
import cit.bjtu.utils.SharedPreferenceTool;
import cn.jpush.android.api.JPushInterface;


public class SplashActivity extends ActionBarActivity implements HttpUtilsCallBack{

    private long startTime;
    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        MobclickAgent.updateOnlineConfig(this);
        MobclickAgent.setDebugMode(false);

        startTime = System.currentTimeMillis();

        //隐去状态栏部分(电池等图标和一切修饰部分)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putInt("currentFragment", 0);
        et.commit();

        JPushInterface.setAliasAndTags(this,sp.getString("Login_name", "null"), null);

        setContentView(R.layout.activity_splash);

        Map<String, String> params = new HashMap<String, String>();
        new HttpUtils().Post( params, "http://bjtucit.sinaapp.com/api/getColumn.php", this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void method(String str) {
        SharedPreferences sp = getSharedPreferences("data",Context.MODE_PRIVATE);
        Boolean checked = sp.getBoolean("checked", false);
        checked = true;
        if(str.equals("ERROR"))
        {
            Toast.makeText(this, "网络错误，请检查您的网络设置！", Toast.LENGTH_SHORT).show();
            final Intent intent;

            intent = new Intent(this, MainActivity.class);
            if(System.currentTimeMillis() - startTime < 3000)
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        SplashActivity.this.finish();

                    }

                }, 3000);

            }
            else {
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                this.finish();
            }
            return;
        }
        try {
            JSONObject json = new JSONObject(str);
            JSONArray columns = json.getJSONArray("columns");
            JSONArray ids = json.getJSONArray("ids");

            String[] column = new String[columns.length()];
            String[] id = new String[columns.length()];
            for(int i = 0; i < columns.length(); i++)
            {
                column[i] = columns.getString(i);
                id[i] = ids.getString(i);
            }
            SharedPreferenceTool.setStringSet(this, "columns", column);
            SharedPreferenceTool.setStringSet(this, "column_id", id);
            final Intent intent;
            intent = new Intent(this, MainActivity.class);
            if(System.currentTimeMillis() - startTime < 3000)
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        SplashActivity.this.finish();

                    }

                }, 3000);

            }
            else {
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                this.finish();
            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
