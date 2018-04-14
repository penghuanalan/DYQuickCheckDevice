package cn.chinafst.dyquickcheckdevice;

import android.app.Application;
import android.content.Context;
import android.posapi.PosApi;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class CheckDeviceApplication  extends Application{

  static   Context context;
  public static boolean isDesign;
  public static CheckDeviceApplication instance = null;
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
            GreenDaoUtils.initDatabase();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static Context getAppConText(){

        return context;
    }

    public PosApi getPosApi(){
        return mPosApi;
    }
    public static  CheckDeviceApplication getInstance(){
        if(instance==null){
            instance =new CheckDeviceApplication();
        }
        return instance;
    }
   public CheckDeviceApplication(){
       super.onCreate();
       instance = this;
   }
}
