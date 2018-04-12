package cn.chinafst.dyquickcheckdevice;

import android.app.Application;
import android.content.Context;

public class CheckDeviceApplication  extends Application{

  static   Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }

    public static Context getAppConText(){

        return context;
    }
}
