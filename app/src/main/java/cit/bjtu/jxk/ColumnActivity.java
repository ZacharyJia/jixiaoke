package cit.bjtu.jxk;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cit.bjtu.jxk.R;
import cit.bjtu.utils.HttpUtils;
import cit.bjtu.utils.HttpUtilsCallBack;
import cit.bjtu.utils.SharedPreferenceTool;

public class ColumnActivity extends ActionBarActivity implements HttpUtilsCallBack , SwipeRefreshLayout.OnRefreshListener {

    ListView listView;
    articleList_adapter adapter;
    int num;
    String id;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column);

        this.getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title));

        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();        //获取已有的intent对象
        Bundle bundle = intent.getExtras();    //获取intent里面的bundle对象
        String id = bundle.getString("id");    //获取Bundle里面的字符串
        this.id = id;
        String name = bundle.getString("name");
        setTitle(name);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        Map<String, String> params = new HashMap<String, String>();
        new HttpUtils().Post( params, "http://bjtucit.sinaapp.com/api/getArticleList.php?offset=0&id="+id, this);

        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ColumnActivity.this,ArticleActivity.class);
                Bundle bundle = new Bundle();
                articleList_adapter adapter = (articleList_adapter)listView.getAdapter();
                bundle.putString("id", (String)adapter.list.get(position).get("id"));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
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

    @Override
    public void method(String str) {
        if(str.equals("ERROR"))
        {
            Toast.makeText(this, "网络错误，请检查您的网络设置！", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            try {
                JSONObject json = new JSONObject(str);
                JSONArray titles = json.getJSONArray("titles");
                JSONArray ids = json.getJSONArray("ids");
                JSONArray times = json.getJSONArray("time");
                JSONArray image = json.getJSONArray("image");

                List<Map<String, Object>> list  = new ArrayList<Map<String, Object>>();
                Map<String,Object> map;
                for(int i = 0; i < titles.length(); i++)
                {
                    map = new HashMap<String, Object>();
                    map.put("title", titles.getString(i));
                    map.put("time", times.getString(i));
                    map.put("id", ids.getString(i));
                    map.put("image", image.getString(i));
                    list.add(map);
                }

                articleList_adapter adapter = new articleList_adapter(getApplicationContext(),list);
                ListView listView = (ListView)findViewById(R.id.listView);
                listView.setAdapter(adapter);

                this.listView = listView;
                this.adapter = adapter;

            }catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRefresh() {
        new articleRefresh(listView, adapter, id, swipeRefreshLayout);
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.column, menu);
        return true;
    }

    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 当ActionBar图标被点击时调用
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
