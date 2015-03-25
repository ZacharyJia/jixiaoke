package cit.bjtu.jxk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cit.bjtu.utils.HttpUtils;
import cit.bjtu.jxk.R;
import cit.bjtu.utils.HttpUtilsCallBack;
import cit.bjtu.utils.SharedPreferenceTool;

public class CheckActivity extends ActionBarActivity implements HttpUtilsCallBack{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        setTitle("计小科-邀请码验证");

        Button btn_ok = (Button)findViewById(R.id.btn_ok);
        final EditText editText = (EditText)findViewById(R.id.code);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editText.getText().toString();
                Map<String, String>  params = new HashMap<String, String>();
                params.put("code", code);
                new HttpUtils().Post(params, "http://bjtucit.sinaapp.com/api/check.php", CheckActivity.this);
            }
        });


    }

    @Override
    public void method(String str) {
        if(str.equals("成功"))
        {
            Toast.makeText(CheckActivity.this, "恭喜您获得内测资格!", Toast.LENGTH_LONG).show();
            SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor et = sp.edit();
            et.putBoolean("checked", true);
            et.commit();
            Intent intent = new Intent(CheckActivity.this, MainActivity.class);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            startActivity(intent);
            this.finish();
        }
        else
        {
            Toast.makeText(CheckActivity.this, "对不起，网络错误或您还没有获得测试资格！", Toast.LENGTH_LONG).show();
        }
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    */

}
