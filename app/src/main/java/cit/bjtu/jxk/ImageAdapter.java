package cit.bjtu.jxk;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * Created by Zachary on 8/15/2014.
 */
public class ImageAdapter extends BaseAdapter {

    private int[] Images;
    private Context context;

    public ImageAdapter(Context context, int[] Images)
    {
        this.Images = Images;
        this.context = context;
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        ImageView image = new ImageView(this.context);
        image.setBackgroundResource(Images[position]);
        return image;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        position = position % Images.length;
        ImageView Image=new ImageView(this.context);
        Image.setImageResource(Images[position]);
        Image.setScaleType(ImageView.ScaleType.FIT_XY);
        Image.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT, Gallery.LayoutParams.MATCH_PARENT));
        Image.setAdjustViewBounds(true);
        return Image;

    }
}
