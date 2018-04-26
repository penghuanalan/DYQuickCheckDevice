package cn.chinafst.dyquickcheckdevice;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.chinafst.dyquickcheckdevice.bean.CheckRecordBean;
import okhttp3.Call;

public class Utils {

    public static String urlBase = "http://zjdy.chinafst.cn:8085/lezhifda/";
   // public static String urlBase = "http://17yt461676.iok.la/dyfda2/";



    public static void upLoadRecord(Context context, List<CheckRecordBean> orign){

        ArrayList<HashMap<String,Object>> list=new ArrayList<>();
        for(CheckRecordBean bean :orign){
            List<HashMap<String,String>> items=new ArrayList<>();
            HashMap<String,Object> second=new HashMap<>();
            HashMap<String,String> map= new HashMap<>();
            map.put("sampleNo",bean.getBatch_number());
            map.put("checkResult",bean.getCheck_result());
            map.put("foodCode",bean.getFood_id());
            map.put("foodName",bean.getFood_name());
            map.put("checkItemName",bean.getItem_name());
            map.put("checkMethod","胶体金法");
            map.put("sysCode",bean.getId());
            map.put("checkConclusion","合格");
            map.put("checkDate",bean.getCheck_date());
            second.put("sampleNO",bean.getSampling_no());
            items.add(map);
            second.put("checkResultList",items);
            list.add(second);
        }
        HashMap<String,List<HashMap<String,Object>>> map1 =new HashMap<>();

        map1.put("result",list);
        Gson gson = new Gson();
        String s = gson.toJson(map1);
        Log.e("上传",s);

        OkHttpUtils.post().addParams("json",s)
                .url(Utils.urlBase+"app/upLoadData.do")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("上传错误",e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("上传",response);

                if(response!=null&&response.contains("操作成功")){


                }
            }
        });
    }





    public static boolean ExpordCheckReocrd(List<String> list) {
        boolean success;
        String path = Environment.getExternalStorageDirectory() + "/DYdetect/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        String fileName = path + "checkRecord.csv";
        File file1 = new File(fileName);
        if(file1.exists()){
            file1.delete();
        }
        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        try {
            out = new FileOutputStream(file1, true);
            osw = new OutputStreamWriter(out, "GBK");
            bw = new BufferedWriter(osw);
            for (int i = 0; i < list.size(); i++) {
                bw.write(list.get(i) + ",");
                if (i % 5 == 4) {
                    bw.newLine();
                }
            }
            success=true;
        } catch (Exception e) {
            e.printStackTrace();
            success=false;

        }finally {
            if(bw!=null){
                try {
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    out.close();
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /**
     * 获取现在时间（数据库的时间格式）
     */
    public static String getDate_sqlite() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


}
