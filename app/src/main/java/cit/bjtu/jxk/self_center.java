package cit.bjtu.jxk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Zachary on 2014/8/29.
 */
public class self_center {

    private static final String IMAGE_FILE_NAME = "header.jpg";

    public self_center(final View rootView){

        TextView username = (TextView)rootView.findViewById(R.id.username);
        SharedPreferences sp = rootView.getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        Boolean isLogin = sp.getBoolean("isLogin", false);
        String Login_name = sp.getString("Login_name", "");
        if (isLogin == false) {
            username.setText("未登录");
        }else{
            username.setText(Login_name);
            ImageView imageView = (ImageView)rootView.findViewById(R.id.imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    cameraIntent.setType("image/*");
                    rootView.getContext().startActivity(cameraIntent);
                }
            });
        }

    }

    private Uri getImageUri() {
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                IMAGE_FILE_NAME));
    }

}
