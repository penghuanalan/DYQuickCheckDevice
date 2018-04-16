package cn.chinafst.dyquickcheckdevice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private GridView gridView;
    private String[] names={"胶体金检测","干化学检测","查询记录","样品查询","项目查询","视频教程"};
    private  int[] images={R.drawable.index_menu01_01,R.drawable.index_menu02_on,R.drawable.index_menu01_05,R.drawable.index_menu02_01,R.drawable.index_menu02_02,R.drawable.index_menu02_03,R.drawable.index_menu02_05};
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        context=this;

        gridView=findViewById(R.id.gv_home);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        startActivity(new Intent(HomeActivity.this,DetectionActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this,DetectionActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(HomeActivity.this, CheckRecordActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(HomeActivity.this,FoodItemActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(HomeActivity.this,CheckItemActivity.class));
                        break;

                    case 5:
                        startActivity(new Intent(HomeActivity.this,StudyVideoActivity.class));
                        break;
                        default:break;

                }
            }
        });
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return names.length;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if(view==null){
                    view = LayoutInflater.from(context).inflate(R.layout.item_index_grid, null);
                }

                final ImageView img_01 =  view.findViewById(R.id.img_01);
                final TextView textview_01 =  view.findViewById(R.id.textview_01);

                textview_01.setText(names[i]);
                img_01.setImageResource(images[i]);

                return view;
            }
        });

    }
}
