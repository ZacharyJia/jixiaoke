package cit.bjtu.jxk;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by zacharyjia on 2014/7/27.
 */
public class articleList_adapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    public List<Map<String, Object>> list;
    private AsyncBitmapLoader asyncBitmapLoader;

    public articleList_adapter(Context context, List<Map<String, Object>> list)
    {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        asyncBitmapLoader=new AsyncBitmapLoader();
    }

    @Override
    public int getCount() {
        return this.list!=null? this.list.size(): 0 ;
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
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.article_list, null);
        }

        TextView tv_title = (TextView)convertView.findViewById(R.id.title);
        TextView tv_time = (TextView)convertView.findViewById(R.id.time);

        tv_title.setText(list.get(position).get("title").toString());
        tv_time.setText(list.get(position).get("time").toString());

        ImageView image = (ImageView)convertView.findViewById(R.id.image);
        String imageURL=list.get(position).get("image").toString();
        Bitmap bitmap=asyncBitmapLoader.loadBitmap(image, imageURL, new AsyncBitmapLoader.ImageCallBack() {

            @Override
            public void imageLoad(ImageView imageView, Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        });
        if(bitmap == null)
        {
            image.setImageResource(R.drawable.nopic);
        }
        else
        {
            image.setImageBitmap(bitmap);
        }

        return convertView;
    }

    public void refresh(List<Map<String, Object>> newList)
    {
        this.list = newList;
        this.notifyDataSetChanged();
    }

}
