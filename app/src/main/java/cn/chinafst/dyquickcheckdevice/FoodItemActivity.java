package cn.chinafst.dyquickcheckdevice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.chinafst.dyquickcheckdevice.bean.SampleFoodBean;
import cn.chinafst.dyquickcheckdevice.bean.SampleFoodBeanDao;
import okhttp3.Call;

public class FoodItemActivity extends AppCompatActivity {
    private ListView listView;
    private Context context=this;
    private ArrayList<String> list=new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private SharedPreferences sp;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);
        sp=this.getPreferences(MODE_PRIVATE);
        listView=findViewById(R.id.lv_list_item);
        progressBar=findViewById(R.id.pb_download);
        adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        initData();
       // checkData();
    }

    private void initData() {

        OkHttpUtils.post().addParams("userName","admin")
                .addParams("passWord","C33367701511B4F6020EC61DED352059")
                .addParams("type","simple")
                .addParams("lastDateTime",sp.getString("simple","2016-02-15 09:20:51"))
                .url(Utils.urlBase+"os/java/pub/data/downloadBasicData.shtml")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("返回",e.toString());
                checkData();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("返回",response);
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    if("success1".equals(jsonObject.get("resultCode"))){
                       // jsonObject.getJSONArray("result")

                        SampleFoodBeanDao sampleFoodBeanDao = GreenDaoUtils.getDaoSession().getSampleFoodBeanDao();
                        List<SampleFoodBean> result = JSON.parseArray(jsonObject.getString("result"), SampleFoodBean.class);
                        for(SampleFoodBean bean:result){
                            sampleFoodBeanDao.insertOrReplace(bean);
                        }
                        sp.edit().putString("simple",Utils.getDate_sqlite()).apply();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }

                checkData();
            }
        });
    }

    private void checkData() {
        List<SampleFoodBean> temp = GreenDaoUtils.getDaoSession().getSampleFoodBeanDao().queryBuilder()
                .where(SampleFoodBeanDao.Properties.IsParent.eq("1")).
                        build().list();

        for(SampleFoodBean bean:temp){
            list.add(bean.getFoodName());
        }
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }
}
