package cn.chinafst.dyquickcheckdevice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.chinafst.dyquickcheckdevice.bean.CheckRecordBean;
import cn.chinafst.dyquickcheckdevice.bean.CheckRecordBeanDao;

public class CheckRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private List<CheckRecordBean> list;
    private List<CheckRecordBean> selected = new ArrayList<>();
    private CheckRecordAdapter adapter;
    private Context context = this;
    private LinearLayout llBottom;
    private Button bt01, bt02, bt03, bt04;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_record);
        listView = findViewById(R.id.lv_check_record);
        llBottom = findViewById(R.id.ll_bottom);
        llBottom.setVisibility(View.INVISIBLE);
        initData();
        initButton();
    }

    private void initButton() {
        bt01 = findViewById(R.id.bt_01);
        bt02 = findViewById(R.id.bt_02);
        bt03 = findViewById(R.id.bt_03);
        bt04 = findViewById(R.id.bt_04);
        bt01.setText("打印");
        bt02.setText("上传");
        bt03.setText("删除");
        bt04.setText("导出");
    }

    private void initData() {
        list = GreenDaoUtils.getDaoSession().getCheckRecordBeanDao().queryBuilder().limit(50).orderDesc(CheckRecordBeanDao.Properties.Check_date).build().list();
        adapter = new CheckRecordAdapter(context, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (selected.contains(list.get(i))) {
                    selected.remove(list.get(i));
                } else {
                    selected.add(list.get(i));
                }
                adapter.notifyDataSetChanged();

                if (selected.size() == 0) {
                    llBottom.setVisibility(View.INVISIBLE);
                } else {
                    llBottom.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.bt_01:

                break;
            case R.id.bt_02:
                break;
            case R.id.bt_03:
                GreenDaoUtils.getDaoSession().getCheckRecordBeanDao().deleteInTx(selected);
                list.removeAll(selected);
                selected.clear();
                adapter.notifyDataSetChanged();
                break;
            case R.id.bt_04:
                List<String> list= new ArrayList<>();
                list.add("检测项目");
                list.add("样品名称");
                list.add("检测结果");
                list.add("检测结论");
                list.add("检测时间");
                for(CheckRecordBean bean:selected){
                   list.add(bean.getItem_name());
                   list.add(bean.getFood_name());
                   list.add(bean.getCheck_result());
                   list.add(bean.getConclusion());
                   list.add(bean.getCheck_date());
                }
                boolean success=Utils.ExpordCheckReocrd(list);
                if(success){
                    Toast.makeText(getApplicationContext(),"导出成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"导出失败,请重试",Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }
    }

    class CheckRecordAdapter extends BaseAdapter {

        private Context context;
        private List<CheckRecordBean> list;


        public CheckRecordAdapter(Context context, List<CheckRecordBean> list) {

            this.context = context;
            this.list = list;
        }


        @Override
        public int getCount() {
            return list.size();
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
            CheckRecordHolder holder = null;
            if (view == null) {
                holder = new CheckRecordHolder();
                view = LayoutInflater.from(context).inflate(R.layout.item_check_record, null);
                holder.tv01 = view.findViewById(R.id.tv_01);
                holder.tv02 = view.findViewById(R.id.tv_02);
                holder.tv03 = view.findViewById(R.id.tv_03);
                holder.tv04 = view.findViewById(R.id.tv_04);
                holder.tv05 = view.findViewById(R.id.tv_05);
                holder.checkBox = view.findViewById(R.id.cb_select);
                view.setTag(holder);
            } else {
                holder = (CheckRecordHolder) view.getTag();
            }

            holder.tv01.setText("检测项目:" + list.get(i).getItem_name());
            holder.tv02.setText("样品名称:" + list.get(i).getFood_name());
            holder.tv03.setText("检测值:" + list.get(i).getCheck_result());
            holder.tv04.setText("结论:" + list.get(i).getConclusion());
            holder.tv05.setText("检测时间:" + list.get(i).getCheck_date());
            if (selected.contains(list.get(i))) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
            return view;
        }
    }

    class CheckRecordHolder {
        public TextView tv01, tv02, tv03,tv04,tv05;
        public CheckBox checkBox;


    }




}
