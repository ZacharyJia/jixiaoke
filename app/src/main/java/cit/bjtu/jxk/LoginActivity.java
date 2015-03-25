package cit.bjtu.jxk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import cit.bjtu.utils.HttpUtils;
import cit.bjtu.utils.HttpUtilsCallBack;
import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends ActionBarActivity implements HttpUtilsCallBack {

    private ProgressDialog pd;

    String login_name;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title));

        this.getActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("计小科-登录");

        final Button btn_login = (Button)findViewById(R.id.btn_login);
        Button btn_reg = (Button)findViewById(R.id.btn_reg);

        final EditText username = (EditText)findViewById(R.id.username);
        final EditText password = (EditText)findViewById(R.id.password);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
                LoginActivity.this.finish();
            }
        });
        btn_reg.setText(Html.fromHtml("<u>" + "注册" + "</u>"));

        btn_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btn_login.setBackgroundColor(getResources().getColor(R.color.press));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    btn_login.setBackgroundResource(R.drawable.login_btn);
                    if(username.getText().toString().equals("") || password.getText().toString().equals(""))
                    {
                        Toast.makeText(LoginActivity.this, "用户名或密码不能为空！", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        pd = ProgressDialog.show(LoginActivity.this, "登录中", "正在登录...");
                        login_name = username.getText().toString();
                        pass = password.getText().toString();
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", username.getText().toString());
                        params.put("password", password.getText().toString());
                        new HttpUtils().Post(params, "http://bjtucit.sinaapp.com/api/login.php", LoginActivity.this);
                    }
                }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    btn_login.setBackgroundResource(R.drawable.login_btn);
                }
                return true;
            }
        });
    }


    @Override
    public void method(String str) {
        if(pd.isShowing())
        {
            pd.dismiss();
        }
        if(str.equals("成功"))
        {
            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
            SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor  editor = sp.edit();
            editor.putString("Login_name", login_name);
            editor.putString("password", pass);
            editor.putBoolean("isLogin", true);
            editor.commit();

            JPushInterface.setAliasAndTags(this, login_name, null);
            this.finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }else
        {
            Toast.makeText(LoginActivity.this, str, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 当ActionBar图标被点击时调用
                this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

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
}
