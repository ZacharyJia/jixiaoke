package cit.bjtu.jxk;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

/**
 * Created by zacharyjia on 2014/7/9.
 */
public class chatRoom_Adapter extends BaseAdapter {

    private List<Map<String, String>> list;
    private LayoutInflater layoutInflater;
    private Context context;

    public chatRoom_Adapter(Context context, List<Map<String, String>> list)
    {
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.list != null? this.list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            if(list.get(position).get("sender").equals("Self")) {
                convertView = layoutInflater.inflate(R.layout.chat_item, null);
            }
            else
            {
                convertView = layoutInflater.inflate(R.layout.chat_item2, null);
            }

        final TextView msg = (TextView)convertView.findViewById(R.id.msg_content);
        msg.setText(list.get(position).get("content") + "\n" +list.get(position).get("time"));
        return convertView;
    }

    public void refresh(List<Map<String, String>> newList)
    {
        this.list = newList;
        this.notifyDataSetChanged();
    }

}
