package cit.bjtu.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zacharyjia on 2014/7/31.
 */
public class DBService {
    private DBOpenHelper dbOpenHelper;

    public DBService(Context context)
    {
        this.dbOpenHelper = new DBOpenHelper(context);
    }


    public void addMsg(String sender, String time, String content)
    {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL("insert into msg(sender, time, content) values(?,?,?)", new Object[]{sender, time, content});
        db.close();
    }

    public int getMsgCount()
    {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from msg where 1", null);
        int count = cursor.getCount();
        db.close();
        return count;
    }
    public List<Map<String , String>> getMsg()
    {
        List result = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from msg where 1", null);
        while(cursor.moveToNext())
        {
            map = new HashMap<String, String>();
            map.put("sender", cursor.getString(cursor.getColumnIndex("sender")));
            map.put("content", cursor.getString(cursor.getColumnIndex("content")));
            map.put("time", cursor.getString(cursor.getColumnIndex("time")));
            result.add(map);
        }

        db.close();
        return result;
    }
}
