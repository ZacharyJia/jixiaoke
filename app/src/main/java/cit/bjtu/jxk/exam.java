package cit.bjtu.jxk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
 * Created by Zachary on 2014/8/27.
 */
public class exam implements HttpUtilsCallBack {

    View rootView;
    ListView listView;
    private List<Map<String, String>> data;
    private ProgressDialog progressDialog;
    public exam(View rootView)
    {
        this.rootView = rootView;
        progressDialog = ProgressDialog.show(rootView.getContext(), "加载中", "加载试题列表中，请稍后");
        Map<String ,String> map = new HashMap<String, String>();
        new HttpUtils().Post(map, "http://bjtucit.sinaapp.com/api/getPapers.php", this);
    }

    @Override
    public void method(String str) {
        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
        if(str.equals("ERROR"))
        {
            Toast.makeText(rootView.getContext(), "网络错误，请检查您的网络设置！", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONObject json = new JSONObject(str);
            JSONArray ids = json.getJSONArray("id");
            JSONArray titles = json.getJSONArray("title");
            JSONArray contents = json.getJSONArray("content");

            List<Map<String, String>> list  = new ArrayList<Map<String, String>>();
            Map<String, String> map;
            for(int i = 0; i < titles.length(); i++)
            {
                map = new HashMap<String, String>();
                map.put("title", titles.getString(i));
                map.put("content", contents.getString(i));
                map.put("paperID", ids.getString(i));
                list.add(map);
            }

            this.data = list;
            this.listView = (ListView)rootView.findViewById(R.id.listView);
            ListAdapter adapter = new SimpleAdapter(rootView.getContext(),
                    data, R.layout.exam_list, new String[]{"title", "content", "PaperID"}, new int[]{R.id.title, R.id.content, R.id.paperID});
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(rootView.getContext(), ExamActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("paperID", data.get(position).get("paperID"));
                    intent.putExtras(bundle);
                    rootView.getContext().startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
