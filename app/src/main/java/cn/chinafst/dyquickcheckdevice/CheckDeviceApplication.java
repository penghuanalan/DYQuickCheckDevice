package cn.chinafst.dyquickcheckdevice;

import android.app.Application;
import android.content.Context;
import android.posapi.PosApi;

public class CheckDeviceApplication  extends Application{

  static   Context context;
  public static boolean isDesign;
private   PosApi mPosApi = null;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        String model= android.os.Build.MODEL;

        //型号 PL-50L----------厂商 alps
        if(model!=null&&(model.equals("PL-50L")||model.equals("PDA"))){
            mPosApi = PosApi.getInstance(this);
            //  LogPrint.toast(context,"定制版");
            isDesign=true;
        }else{
            isDesign=false;
        }
    }

    public static Context getAppConText(){

        return context;
    }
}
