//package Util;
//
//import android.app.Application;
//import android.content.SharedPreferences;
//import android.database.sqlite.SQLiteDatabase;
//import android.preference.PreferenceManager;
//
//import com.example.application.tablayoutexample.R;
//
///**
// * Created by 8470p on 11/11/2016.
// */
//public class RemindMe extends Application {
//
//    public static DbHelper dbHelper;
//    public static SQLiteDatabase db;
//    public static SharedPreferences sp;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
//        sp = PreferenceManager.getDefaultSharedPreferences(this);
//
//        dbHelper = new DbHelper(this);
//        db = dbHelper.getWritableDatabase();
//    }
//
//    public static String getRingtone() {
//        return sp.getString(RemindMe.RINGTONE_PREF, DEFAULT_NOTIFICATION_URI.toString());
//    }
//
//}
