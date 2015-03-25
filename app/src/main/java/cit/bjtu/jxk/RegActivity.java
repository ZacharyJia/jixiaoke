package cit.bjtu.jxk;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RadialGradient;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import cit.bjtu.jxk.R;
import cit.bjtu.utils.HttpUtils;
import cit.bjtu.utils.HttpUtilsCallBack;
import cn.jpush.android.api.JPushInterface;

public class RegActivity extends ActionBarActivity implements HttpUtilsCallBack {

    private String login_name;
    private String pass;

    private ProgressDialog pd;

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        this.getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title));
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("计小科-注册");


        final EditText username = (EditText)findViewById(R.id.username);
        final EditText password = (EditText)findViewById(R.id.password);
        final EditText studentID = (EditText)findViewById(R.id.studentID);

        final Button btn_reg = (Button)findViewById(R.id.btn_reg);
        final Button btn_login = (Button)findViewById(R.id.btn_login);

        btn_login.setText(Html.fromHtml("<u>返回登录</u>"));

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                RegActivity.this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        btn_reg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    btn_reg.setBackgroundColor(getResources().getColor(R.color.press));
                }else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    btn_reg.setBackgroundResource(R.drawable.login_btn);
                    if(username.getText().toString().equals("") || password.getText().toString().equals(""))
                    {
                        Toast.makeText(RegActivity.this,"用户名或密码不能为空！", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String stuID;
                        if(studentID.getText().toString().equals(""))
                        {
                            stuID = "0";
                        }
                        else
                        {
                            stuID = studentID.getText().toString();
                        }
                        login_name = username.getText().toString();
                        pass = password.getText().toString();
                        pd = ProgressDialog.show(RegActivity.this, "注册中", "正在注册...");
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", username.getText().toString());
                        params.put("password", password.getText().toString());
                        params.put("studentID", stuID);
                        new HttpUtils().Post(params, "http://bjtucit.sinaapp.com/api/reg.php", RegActivity.this);
                    }
                }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    btn_reg.setBackgroundResource(R.drawable.login_btn);
                }
                return true;
            }
        });
    }

    @Override
    public void method(String str) {

        if(pd.isShowing()){
            pd.dismiss();
        }

        if(str.equals("成功"))
        {
            Toast.makeText(RegActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
            SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor  editor = sp.edit();
            editor.putString("Login_name", login_name);
            editor.putString("password", pass);
            editor.putBoolean("isLogin", true);
            editor.commit();

            JPushInterface.setAliasAndTags(this, login_name, null);
            this.finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        }
        else
        {
            Toast.makeText(RegActivity.this, str, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 当ActionBar图标被点击时调用
                Intent intent = new Intent(RegActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                RegActivity.this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
