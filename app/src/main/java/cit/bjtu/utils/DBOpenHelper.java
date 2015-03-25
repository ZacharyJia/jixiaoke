package cit.bjtu.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zacharyjia on 2014/7/31.
 */
public class DBOpenHelper extends SQLiteOpenHelper {



    public DBOpenHelper(Context context)
    {
        super(context, "jxk.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE msg(ID integer primary key , sender varchar(50)," +
                "content varchar(200), time varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
