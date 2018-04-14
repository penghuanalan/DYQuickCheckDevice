package cn.chinafst.dyquickcheckdevice;

import android.os.Environment;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;

import cn.chinafst.dyquickcheckdevice.bean.CheckRecordBean;
import okhttp3.Call;

public class Utils {

    public static String urlBase = "http://zjdy.chinafst.cn:8085/lezhifda/";


    public static void upLoadCheckRecord(List<CheckRecordBean> list) {

        for (CheckRecordBean bean : list) {


        }

/*
        HashMap<String,String> map= new HashMap<>();
        map.put("sampleNO",sampleNum);
        map.put("checkResult","阴性");
        map.put("checkConclusion","合格");
        map.put("checkDate",checkTime);

        HashMap<String,Object> second=new HashMap<>();
        second.put("sampleNO",sampleNum);
        second.put("checkResultList",map);

        HashMap<String,Object> first=new HashMap<>();

        first.put("result",second);

        Gson gson = new Gson();
        String s = gson.toJson(first);

        OkHttpUtils.post().addParams("result",s)
                .url(Utils.urlBase+"app/upLoadData.do")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

            }
        });*/
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
}
