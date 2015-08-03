package cit.bjtu.jxk;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BannerView extends LinearLayout {
    private ViewPager adViewPager;
    private List<ImageView> bannerViewList = new ArrayList<ImageView>();
    private Timer bannerTimer;
    private Handler handler;
    private BannerTimerTask bannerTimerTask;
    public BannerView(Context context) {
        super(context);

    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.banner_view, this);
        initView(context);
        handler = new Handler() {
            public void handleMessage(Message msg) {
                adViewPager.setCurrentItem(msg.what);
                super.handleMessage(msg);

            }
        };
        bannerTimer = new Timer();
    }

    private void initView(final Context context) {
        RelativeLayout layout = (RelativeLayout) this
                .findViewById(R.id.view_pager_content);
        adViewPager = (ViewPager) this.findViewById(R.id.viewPager1);
//        group = (ViewGroup) findViewById(R.id.iv_image);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        LayoutParams params = (LayoutParams) layout
                .getLayoutParams();
        //根据图片高和宽的比例计算高度
        //测试图宽为640 高为388
        params.height = (int) (((double) dm.widthPixels / (double) 640 * (double) 388));
        layout.setLayoutParams(params);

        //初始化数据
        final ImageView imageView1 = new ImageView(context);
        imageView1.setImageResource(R.drawable.main1);
        bannerViewList.add(imageView1);
        final ImageView imageView2 = new ImageView(context);
        imageView2.setImageResource(R.drawable.main2);
        bannerViewList.add(imageView2);
        final ImageView imageView3 = new ImageView(context);
        imageView3.setImageResource(R.drawable.main3);
        bannerViewList.add(imageView3);
        final ImageView imageView4 = new ImageView(context);
        imageView4.setImageResource(R.drawable.main4);
        bannerViewList.add(imageView4);

        final ImageView dots = (ImageView)findViewById(R.id.dots);


        dots.setImageDrawable(getResources().getDrawable(R.drawable.dot1));

        BannerViewAdapter adapter = new BannerViewAdapter(context,
                bannerViewList);
        adViewPager.setAdapter(adapter);
        adViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageSelected(int arg0) {
                //当viewpager换页时 改掉下面对应的小点
                switch (arg0) {
                    case 0:
                        dots.setImageDrawable(getResources().getDrawable(R.drawable.dot1));
                        break;
                    case 1:
                        dots.setImageDrawable(getResources().getDrawable(R.drawable.dot2));
                        break;
                    case 2:
                        dots.setImageDrawable(getResources().getDrawable(R.drawable.dot3));
                        break;
                    case 3:
                        dots.setImageDrawable(getResources().getDrawable(R.drawable.dot4));
                        break;
                }
            }

        });
    }


    //启动banner自动轮播
    public void bannerStartPlay(){
        if (bannerTimer != null) {
            if (bannerTimerTask != null)
                bannerTimerTask.cancel();
            bannerTimerTask = new BannerTimerTask();
            bannerTimer.schedule(bannerTimerTask, 5000, 5000);//5秒后执行，每隔5秒执行一次
        }
    }
    //暂停banner自动轮播
    public void bannerStopPlay(){
        if (bannerTimerTask != null)
            bannerTimerTask.cancel();
    }
    class BannerTimerTask extends TimerTask {
        @Override
        public void run() {
            Message msg = new Message();
            if (bannerViewList.size() <= 1)
                return;
            int currentIndex = adViewPager.getCurrentItem();
            if (currentIndex == bannerViewList.size() - 1)
                msg.what = 0;
            else
                msg.what = currentIndex + 1;

            handler.sendMessage(msg);
        }

    }
}
