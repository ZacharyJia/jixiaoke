package cit.bjtu.jxk;

import android.view.View;

/**
 * Created by Zachary on 8/15/2014.
 */
public class main {

    public main(final View rootView)
    {
        BannerView bannerView = (BannerView)rootView.findViewById(R.id.banner_view1);
        bannerView.bannerStartPlay();
    }

}
