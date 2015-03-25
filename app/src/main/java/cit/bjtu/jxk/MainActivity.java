package cit.bjtu.jxk;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import cit.bjtu.jxk.R;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private long mExitTime;
    int number;
    private static final String IMAGE_FILE_NAME = "header.jpg";
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int RESIZE_REQUEST_CODE = 2;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        new Thread(){
            public void run()
            {
                UmengUpdateAgent.update(MainActivity.this);
            }
        }.start();

*/
        UmengUpdateAgent.setUpdateAutoPopup(true);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(getApplicationContext());

        this.getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title));

        number = 0;

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        number = position;
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commitAllowingStateLoss();
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
            case 7:
                mTitle = getString(R.string.title_section7);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
        restoreActionBar();
        View drawerView = mNavigationDrawerFragment.drawerView;
        final SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        final TextView textView = (TextView)drawerView.findViewById(R.id.username);
        final TextView switchID = (TextView)drawerView.findViewById(R.id.swithID);
        boolean isLogin = sp.getBoolean("isLogin", false);
        if(isLogin == true)
        {
            textView.setText(sp.getString("Login_name", "未登录"));
            switchID.setText("切换账号");
        }
        else {
            textView.setText("未登录");
            switchID.setText("");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(number + 1))
                .commitAllowingStateLoss();

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId() == R.id.action_settings)
        {
            //设置按钮被点击
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static int selectedNumber;
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {

            selectedNumber = sectionNumber;
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView;
            switch (selectedNumber)
            {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    new main(rootView);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_article_center, container, false);
                    new article_center(rootView, getActivity());
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_msg_center, container, false);
                    self_center(rootView);
                    break;
                case 4:
                    final SharedPreferences sp = inflater.getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                    boolean isLogin = sp.getBoolean("isLogin", false);
                    if(isLogin == true) {
                        rootView = inflater.inflate(R.layout.fragment_chatroom, container, false);
                        new chatRoom(rootView);
                    }
                    else
                    {
                        rootView = inflater.inflate(R.layout.no_login, container, false);
                        final Button btn_login = (Button)rootView.findViewById(R.id.btn_login);
                        btn_login.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if(event.getAction() == MotionEvent.ACTION_DOWN)
                                {
                                    btn_login.setBackgroundColor(getResources().getColor(R.color.press));
                                }
                                else if (event.getAction() == MotionEvent.ACTION_UP)
                                {
                                    btn_login.setBackgroundResource(R.drawable.login_btn);
                                    Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                                else if (event.getAction() == MotionEvent.ACTION_CANCEL)
                                {
                                    btn_login.setBackgroundResource(R.drawable.login_btn);
                                }
                                return false;
                            }
                        });
                    }
                    break;
                case 5:
                    rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
                    new feedback(rootView);
                    break;
                case 6:
                    rootView = inflater.inflate(R.layout.fragment_exam, container, false);
                    new exam(rootView);
                    break;
                case 7:
                    rootView = inflater.inflate(R.layout.fragment_about, container, false);
                    break;
                default:
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    break;
            }
            //View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis() - mExitTime) > 2000){
                Toast.makeText(MainActivity.this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }
            else
            {
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putInt("currentFragment", number);
        et.commit();
        super.onPause();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

     SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
     String content = sp.getString("clipBoard", "");
     ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
     ClipData clipData = ClipData.newPlainText("content", content);
     clipboardManager.setPrimaryClip(clipData);

        return super.onContextItemSelected(item);
    }

    private static void self_center(final View rootView)
    {
        TextView username = (TextView)rootView.findViewById(R.id.username);
        SharedPreferences sp = rootView.getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        Boolean isLogin = sp.getBoolean("isLogin", false);
        String Login_name = sp.getString("Login_name", "");
        if (isLogin == false) {
            username.setText("未登录");
        }else{
            username.setText(Login_name);
            ImageView imageView = (ImageView)rootView.findViewById(R.id.imageView);
            /*
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    ((Activity)rootView.getContext()).startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
                }
            });
            */
        }

        TextView account_change = (TextView)rootView.findViewById(R.id.account_change);
        account_change.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    v.setBackgroundColor(rootView.getResources().getColor(R.color.white));
                    SharedPreferences sp = rootView.getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor  editor = sp.edit();
                    editor.putString("Login_name", "");
                    editor.putString("password", "");
                    editor.putBoolean("isLogin", false);
                    editor.commit();
                    Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
                    rootView.getContext().startActivity(intent);

                }
                else if (event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    v.setBackgroundColor(rootView.getResources().getColor(R.color.white));
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    v.setBackgroundColor(rootView.getResources().getColor(R.color.lightgray));
                }

                return true;
            }
        });

        TextView information = (TextView)rootView.findViewById(R.id.information);
        information.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    v.setBackgroundColor(rootView.getResources().getColor(R.color.white));
                }
                else if (event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    v.setBackgroundColor(rootView.getResources().getColor(R.color.white));
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    v.setBackgroundColor(rootView.getResources().getColor(R.color.lightgray));
                }

                return true;
            }
        });
        TextView zhitiao = (TextView)rootView.findViewById(R.id.zhitiao);
        zhitiao.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    v.setBackgroundColor(rootView.getResources().getColor(R.color.white));
                    Intent intent = new Intent(rootView.getContext(), MsgActivity.class);
                    rootView.getContext().startActivity(intent);
                }
                else if (event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    v.setBackgroundColor(rootView.getResources().getColor(R.color.white));
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    v.setBackgroundColor(rootView.getResources().getColor(R.color.lightgray));
                }

                return true;
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            return;
        }else {
            switch (requestCode)
            {
                case IMAGE_REQUEST_CODE:
                    resizeImage(data.getData());
                    break;
                case RESIZE_REQUEST_CODE:
                    if(data != null)
                    {
                    }
                    break;
            }
        }
    }

    public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }

    public Drawable getResizeImage(Intent data)
    {
        Bundle extras = data.getExtras();
        if(extras != null){
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            return drawable;
        }
        else {
            return null;
        }
    }
}
