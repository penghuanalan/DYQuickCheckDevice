package cn.chinafst.dyquickcheckdevice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import okhttp3.Call;

public class StudyVideoActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private ListView listView;
    private Context context=this;
    private ArrayList<String> list=new ArrayList<>();
    private ArrayAdapter<String> adapter;
    String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/DyVideo/";
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
        File file= new File(path);
        if(!file.exists()){
            file.mkdir();
        }

        initData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(list.get(i).endsWith("mp4")){
                    File file1= new File(path+list.get(i));
                    if(file1.exists()){
                        Intent intent= new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file1);
                            intent.setDataAndType(contentUri, "video/mp4");
                        }else{
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setDataAndType(Uri.fromFile(file1), "video/mp4");
                        }
                        startActivity(intent);

                    }else{
                        Toast.makeText(context,"下载中",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                        OkHttpUtils.get().url("http://fc.chinafst.cn:9002/home/video/"+list.get(i)).build().execute(new FileCallBack(path,list.get(i)) {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResponse(File response, int id) {
                                progressBar.setVisibility(View.GONE);
                                Intent intent= new Intent(Intent.ACTION_VIEW);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", response);
                                    intent.setDataAndType(contentUri, "video/mp4");
                                }else{
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.setDataAndType(Uri.fromFile(response), "video/mp4");
                                }
                                startActivity(intent);
                            }
                        });


                    }
                }
            }
        });
    }

    private void initData() {

        OkHttpUtils.get().url("http://fc.chinafst.cn:9002/home/video/video.json").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(context,"暂无网络",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                checkData();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray produces = jsonObject.getJSONArray("Produces");
                    if(produces.length()>0){
                        for(int i=0;i<produces.length();i++){

                            JSONObject object = (JSONObject) produces.get(i);
                            list.add(object.getString("name"));
                        }

                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkData() {

        File file= new File(path);
        if(file.exists()&&file.isDirectory()){
            File[] files = file.listFiles();
            for(File file1: files){
                list.add(file1.getName());
            }
            adapter.notifyDataSetChanged();

        }else{
            Toast.makeText(getApplicationContext(),"教程文件不存在",Toast.LENGTH_SHORT).show();
        }
    }
}
