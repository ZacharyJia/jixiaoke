package cit.bjtu.jxk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cit.bjtu.utils.DBOpenHelper;
import cit.bjtu.utils.DBService;
import cit.bjtu.utils.HttpUtils;
import cit.bjtu.utils.HttpUtilsCallBack;

/**
 * Created by zacharyjia on 2014/7/9.
 */
public class chatRoom implements HttpUtilsCallBack {
    View rootView;
    int MsgCount;
    public chatRoom(final View rootView)
    {
        this.rootView = rootView;
        final DBService dbService = new DBService(rootView.getContext());
        final SharedPreferences sp = rootView.getContext().getSharedPreferences("data", Context.MODE_PRIVATE);

        MsgCount = dbService.getMsgCount();

        final List<Map<String, String>> list = dbService.getMsg();
        final ListView listView = (ListView) rootView.findViewById(R.id.chat_list);
        listView.setDivider(null);
        listView.setDividerHeight(5);
        final chatRoom_Adapter adapter = new chatRoom_Adapter(rootView.getContext(), list);
        listView.setAdapter(adapter);

        final EditText msg_content = (EditText) rootView.findViewById(R.id.msg);
        final Button btn_sendmsg = (Button) rootView.findViewById(R.id.send_msg);

        btn_sendmsg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    btn_sendmsg.setBackgroundColor(rootView.getResources().getColor(R.color.press));
                }else if (event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    btn_sendmsg.setBackgroundColor(rootView.getResources().getColor(R.color.button));
                }else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    btn_sendmsg.setBackgroundColor(rootView.getResources().getColor(R.color.button));
                    if (msg_content.getText().toString().equals("")) {
                        Toast.makeText(rootView.getContext(), "发送内容不能为空！", Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("sender", sp.getString("Login_name", "null"));
                        params.put("content", msg_content.getText().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String t = sdf.format(new Date(System.currentTimeMillis()));
                        params.put("time", t);
                        new HttpUtils().Post(params, "http://bjtucit.sinaapp.com/api/postMsg.php", chatRoom.this);

                        dbService.addMsg("Self", t, msg_content.getText().toString());

                        adapter.refresh(dbService.getMsg());
                        MsgCount = dbService.getMsgCount();

                        msg_content.setText("");
                    }

                }
                return true;
            }
        });


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //要做的事情
                int count = dbService.getMsgCount();
                if (MsgCount != count) {
                    List<Map<String, String>> list;
                    list = dbService.getMsg();
                    adapter.refresh(list);
                    MsgCount = count;
                }
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 2000);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((InputMethodManager) rootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(msg_content.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0,0,0,"复制");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listView.showContextMenu();
                Map<String, String> map;
                map = (Map<String, String>)parent.getAdapter().getItem(position);
                SharedPreferences sp = rootView.getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("clipBoard",map.get("content"));
                editor.commit();
                return true;
            }
        });




    }


    @Override
    public void method(String str) {
        if(str.equals("成功"))
        {

        }
        else {
            Toast.makeText(rootView.getContext(),str, Toast.LENGTH_SHORT).show();
            Log.e("Error", str);
        }
    }

}
