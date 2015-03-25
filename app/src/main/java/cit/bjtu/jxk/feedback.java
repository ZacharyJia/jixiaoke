package cit.bjtu.jxk;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cit.bjtu.utils.HttpUtils;
import cit.bjtu.utils.HttpUtilsCallBack;

/**
 * Created by zacharyjia on 2014/7/8.
 */
public class feedback implements HttpUtilsCallBack{
    View rootView;
    public feedback(final View rootView)
    {
        this.rootView = rootView;
        final Button submit_feedback = (Button)rootView.findViewById(R.id.submit_feedback);
        final EditText sender = (EditText)rootView.findViewById(R.id.sender);
        final EditText content = (EditText)rootView.findViewById(R.id.content);

        SharedPreferences sp = rootView.getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        sender.setText(sp.getString("Login_name", " "));

        submit_feedback.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    submit_feedback.setBackgroundColor(rootView.getResources().getColor(R.color.press));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    if(content.equals(""))
                    {
                        Toast.makeText(rootView.getContext(), "请输入您的意见和建议", Toast.LENGTH_SHORT).show();
                    }else {
                        submit_feedback.setBackgroundResource(R.drawable.login_btn);
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("sender", sender.getText().toString());
                        params.put("content", content.getText().toString());
                        new HttpUtils().Post(params, "http://bjtucit.sinaapp.com/api/feedback.php", feedback.this);
                    }

                }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    submit_feedback.setBackgroundResource(R.drawable.login_btn);
                }
                return true;
            }
        });
    }

    @Override
    public void method(String str) {
        if(str.equals("ERROR")){
            Toast.makeText(rootView.getContext(), "网络错误，请检查您的网络连接！", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(rootView.getContext(), "您的反馈已提交，我们会尽快查看并解决！", Toast.LENGTH_SHORT).show();
            EditText sender = (EditText) rootView.findViewById(R.id.sender);
            EditText content = (EditText) rootView.findViewById(R.id.content);
            sender.setText("");
            content.setText("");
        }
    }
}
