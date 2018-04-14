package cn.chinafst.dyquickcheckdevice;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import cn.chinafst.dyquickcheckdevice.bean.DaoMaster;
import cn.chinafst.dyquickcheckdevice.bean.DaoSession;


/**
 * Created by Administrator on 2017/9/12.
 */

public class GreenDaoUtils {

    //基础数据库

    private static SQLiteDatabase basdDB;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private static DaoMaster.DevOpenHelper baseHelper;

    /**
     * 初始化greenDao，这个操作建议在Application初始化的时候添加；
     */
    public static void initDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        baseHelper = new DaoMaster.DevOpenHelper(CheckDeviceApplication.getAppConText(), "baseDB");

        basdDB = baseHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(basdDB);
       mDaoSession = mDaoMaster.newSession();
        /*
        * 用于加密
        * */
       /* mDaoMaster = new DaoMaster(baseHelper.getEncryptedWritableDb("dyfda"));//加密
        mDaoSession = mDaoMaster.newSession();*/
       /* 用于加密*/

    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }

    public static SQLiteDatabase getBasdDB(){

        return basdDB;
    }


    public static String getLastTime(Context context, String dbName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if(dbName.equals("food")||dbName.equals("foodItem") ||dbName.equals("detectItem") ||dbName.equals("standard")||dbName.equals("laws")){
            // ||dbName.equals("regulatory")||dbName.equals("license")||dbName.equals("business")||dbName.equals("personnel")){
            if(sp.getString(dbName, "").equals("")){
                return "2018-03-27 00:00:00";
            }else{
                return sp.getString(dbName, "");
            }

        }
        if (sp.getString(dbName, "").equals("")) {
            return "2000-12-10 00:00:00";
        } else {
            return sp.getString(dbName, "");
        }
    }
    public static String getShortDate(String date){

        return date.substring(0,date.length()-8);
    }
    public static String getShortTime(String date){

        return date.substring(date.length()-8);
    }


}