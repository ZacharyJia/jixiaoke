package cit.bjtu.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zacharyjia on 2014/7/7.
 * 本类用于存储和读取SharedPreferences中的字符数组
 */

public class SharedPreferenceTool {

    /**
     * 字符串读取函数
     * @param context 上下文
     * @param key 存储SharedPreferences时的关键字
     * @return 读取到的字符数组
     */
    public static String[] getStringSet(Context context, String key)
    {
        String regularEx = "#";
        String[] str = null;
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String values;
        values = sp.getString(key, "");
        str = values.split(regularEx);
        return str;
    }

    /**
     * 字符数组写入函数
     * @param context 上下文
     * @param key 用于存储的关键字
     * @param values 要存储的值
     */
    public static void setStringSet(Context context, String key, String[] values)
    {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        if(values != null && values.length > 0)
        {
            for(String value:values)
            {
                str += value;
                str += regularEx;
            }
            SharedPreferences.Editor et = sp.edit();
            et.putString(key, str);
            et.commit();
        }
    }
}
