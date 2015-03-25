package cit.bjtu.jxk;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import cn.jpush.android.api.JPushInterface;


public class SettingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title));

        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();

        Switch push = (Switch)findViewById(R.id.push);
        Switch newmsg = (Switch)findViewById(R.id.newmsg);
        final Switch ring = (Switch)findViewById(R.id.ring);
        final Switch zhendong = (Switch)findViewById(R.id.zhendong);
        final LinearLayout layout_ring = (LinearLayout)findViewById(R.id.layout_ring);
        final LinearLayout layout_zhendong = (LinearLayout)findViewById(R.id.layout_zhendong);
        LinearLayout checkUpdate = (LinearLayout)findViewById(R.id.checkUpdate);
        checkUpdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    v.setBackgroundColor(getResources().getColor(R.color.lightgray));
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    v.setBackgroundColor(getResources().getColor(android.R.color.white));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    v.setBackgroundColor(getResources().getColor(android.R.color.white));
                    Toast.makeText(SettingActivity.this, "检查中...", Toast.LENGTH_SHORT).show();
                    UmengUpdateAgent.setUpdateAutoPopup(false);
                    UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                        @Override
                        public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                            switch (updateStatus) {
                                case UpdateStatus.Yes: // has update
                                    UmengUpdateAgent.showUpdateDialog(getApplicationContext(), updateInfo);
                                    break;
                                case UpdateStatus.No: // has no update
                                    Toast.makeText(getApplicationContext(), "您的应用已经是最新版本！", Toast.LENGTH_SHORT).show();
                                    break;
                                case UpdateStatus.NoneWifi: // none wifi
                                    Toast.makeText(getApplicationContext(), "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                                    break;
                                case UpdateStatus.Timeout: // time out
                                    Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        }
                    });
                    UmengUpdateAgent.forceUpdate(SettingActivity.this);
                }
                return true;
            }
        });

        if(sp.getBoolean("push", true)){
            push.setChecked(true);
        }else {
            push.setChecked(false);
        }
        if(sp.getBoolean("newmsg", true)){
            newmsg.setChecked(true);
            layout_ring.setVisibility(View.VISIBLE);
            layout_zhendong.setVisibility(View.VISIBLE);
        }else{
            newmsg.setChecked(false);
            layout_ring.setVisibility(View.GONE);
            layout_zhendong.setVisibility(View.GONE);
        }
        if(sp.getBoolean("ring", true))
        {
            ring.setChecked(true);
        }else {
            ring.setChecked(false);
        }
        if(sp.getBoolean("zhendong", true))
        {
            zhendong.setChecked(true);
        }else {
            zhendong.setChecked(false);
        }


        newmsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true)
                {
                    layout_ring.setVisibility(View.VISIBLE);
                    layout_zhendong.setVisibility(View.VISIBLE);
                    editor.putBoolean("newmsg", true);
                    editor.commit();
                }
                else
                {
                    layout_ring.setVisibility(View.GONE);
                    layout_zhendong.setVisibility(View.GONE);
                    editor.putBoolean("newmsg", false);
                    editor.commit();
                }
            }
        });

        push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true)
                {
                    editor.putBoolean("push", true);
                    editor.commit();
                }
                else
                {
                    editor.putBoolean("push", false);
                    editor.commit();
                }
            }
        });

        ring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    editor.putBoolean("ring", true);
                    editor.commit();
                }
                else{
                    editor.putBoolean("ring", false);
                    editor.commit();
                }
            }
        });

        zhendong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    editor.putBoolean("zhendong", true);
                    editor.commit();
                }
                else {
                    editor.putBoolean("zhendong", false);
                    editor.commit();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
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
}
