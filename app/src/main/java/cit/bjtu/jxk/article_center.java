package cit.bjtu.jxk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cit.bjtu.utils.HttpUtils;
import cit.bjtu.utils.HttpUtilsCallBack;
import cit.bjtu.utils.SharedPreferenceTool;

/**
 * Created by zacharyjia on 2014/7/8.
 * 该类用于处理文章中心页面的创建显示任务
 */
public class article_center implements HttpUtilsCallBack {

    int mWidth;

    Context context;
    View rootView;
    public article_center(View rootView, Context context)
    {
        this.context = context;
        this.rootView = rootView;

        WindowManager wm = ((Activity)context).getWindowManager();
        mWidth = wm.getDefaultDisplay().getWidth();

        rootView.setBackgroundColor(rootView.getResources().getColor(R.color.background));
        showColumns();
        showHotArticle();
    }

    private void showColumns()
    {
        String[] columns = SharedPreferenceTool.getStringSet(rootView.getContext(), "columns");
        String[] ids = SharedPreferenceTool.getStringSet(rootView.getContext(),"column_id");
        final Map<String, String> column = new HashMap<String, String>();
        for(int i = 0; i < columns.length; i++)
        {
            column.put(columns[i], ids[i]);
        }

        GridLayout gridLayout = (GridLayout)rootView.findViewById(R.id.gridLayout);
        LinearLayout linearLayout;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((mWidth - 40) / 2, (mWidth - 40) / 2);
        params.setMargins(10,10,10,10);

        TextView textView;

        for(int i = 0; i < columns.length; i++)
        {
            linearLayout = new LinearLayout(rootView.getContext());
            textView = new TextView(rootView.getContext());
            textView.setTextColor(rootView.getResources().getColor(R.color.white));
            textView.setTextSize(20);
            textView.setBackgroundColor(rootView.getResources().getColor(R.color.metro_blue + i % 8));
            textView.setHeight((mWidth - 40) / 2);
            textView.setWidth((mWidth - 40) / 2);
            textView.setGravity(Gravity.CENTER);
            textView.setText(columns[i]);
            final int finalI = i % 8;
            textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        v.setBackgroundColor(rootView.getResources().getColor(R.color.lightgray));
                    }
                    else if(event.getAction() == MotionEvent.ACTION_CANCEL)
                    {
                        v.setBackgroundColor(rootView.getResources().getColor(R.color.metro_blue +finalI % 8));
                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        v.setBackgroundColor(rootView.getResources().getColor(R.color.metro_blue +finalI % 8));
                        TextView t = (TextView)v;
                        //Toast.makeText(context, (String)column.get(t.getText().toString()), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(rootView.getContext(), ColumnActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", (String)column.get(t.getText().toString()));
                        bundle.putString("name", t.getText().toString());
                        intent.putExtras(bundle);
                        context.startActivity(intent);

                    }
                    return true;
                }
            });
            linearLayout.addView(textView,params);
            gridLayout.addView(linearLayout);
        }

    }

    private void showHotArticle()
    {
        Map<String, String> params = new HashMap<String, String>();
        new HttpUtils().Post( params, "http://bjtucit.sinaapp.com/api/getHotArticle.php", this);
    }

    @Override
    public void method(String str) {
        if(str.equals("ERROR"))
        {
            Toast.makeText(context, "网络错误，请检查您的网络设置！", Toast.LENGTH_SHORT).show();
            TextView textView = (TextView)rootView.findViewById(R.id.hot1);
            textView.setText("网络错误，无法加载热门文章！");
            return;
        }
        try {
            TextView textView;
            JSONObject json = new JSONObject(str);
            JSONArray articles = json.getJSONArray("articles");
            JSONArray ids = json.getJSONArray("ids");
            final Map<String, String> article = new HashMap<String, String>();

            for(int i = 0; i < articles.length(); i++)
            {
                article.put(articles.getString(i), ids.getString(i));
                textView = (TextView)rootView.findViewById(R.id.hot1 + i);
                textView.setText(articles.getString(i));
                textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN)
                        {
                            v.setBackgroundColor(context.getResources().getColor(R.color.lightgray));
                        }
                        else if(event.getAction() == MotionEvent.ACTION_CANCEL)
                        {
                            v.setBackgroundColor(context.getResources().getColor(R.color.background));
                        }
                        else if (event.getAction() == MotionEvent.ACTION_UP)
                        {
                            v.setBackgroundColor(context.getResources().getColor(R.color.background));
                            TextView t = (TextView)v;
                            //Toast.makeText(context, (String)article.get(t.getText().toString()), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(rootView.getContext(), ArticleActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("id", (String)article.get(t.getText().toString()));
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                        return true;
                    }
                });
            }

        }catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}
