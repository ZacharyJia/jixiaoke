package cit.bjtu.jxk;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cit.bjtu.utils.HttpUtils;
import cit.bjtu.utils.HttpUtilsCallBack;

/**
 * Created by zacharyjia on 2014/7/27.
 */
public class articleRefresh implements HttpUtilsCallBack {

    articleList_adapter adapter;
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    public articleRefresh(ListView listView, articleList_adapter adapter, String id, SwipeRefreshLayout swipeRefreshLayout)
    {

        this.adapter = adapter;
        this.listView = listView;
        this.swipeRefreshLayout = swipeRefreshLayout;
        Map<String, String> params = new HashMap<String, String>();
        new HttpUtils().Post(params, "http://bjtucit.sinaapp.com/api/getArticleList.php?offset=0&id="+id, this);

    }
    @Override
    public void method(String str) {
        this.swipeRefreshLayout.setRefreshing(false);
        if(str.equals("ERROR"))
        {
            return;
        }
        else {
            try {
                JSONObject json = new JSONObject(str);
                JSONArray titles = json.getJSONArray("titles");
                JSONArray ids = json.getJSONArray("ids");
                JSONArray times = json.getJSONArray("time");
                JSONArray image = json.getJSONArray("image");

                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Map<String, Object> map;
                for (int i = 0; i < titles.length(); i++) {
                    map = new HashMap<String, Object>();
                    map.put("title", titles.getString(i));
                    map.put("time", times.getString(i));
                    map.put("id", ids.getString(i));
                    map.put("image", image.getString(i));
                    list.add(map);
                }
                if(list.size() != 0) {
                    adapter.refresh(list);
                    Toast.makeText(listView.getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
