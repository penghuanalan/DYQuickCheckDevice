package cn.chinafst.dyquickcheckdevice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.posapi.PosApi;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cn.chinafst.dyquickcheckdevice.bean.CheckRecordBean;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;
import zyapi.PrintQueue;

public class DetectionActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private Context context = this;
    private ScanDevice sm;
    private final static String SCAN_ACTION = "scan.rcv.message";
    private LineChart chartView;
    private TextView tvItem, tvSample, tvUnitNmae, tvOpeName, tvOpeNum, tvSampleDate, tvSampleNum;
    private PosApi mApi = null;
    private PrintQueue mPrintQueue = null;
    private String checkTime = "";
    private String uuid = "";

    //抽样单号
    private String sampleNum="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detection);
        initView();
        initChartView();
        initPrwmission();
        if (CheckDeviceApplication.isDesign) {
            initScan();
            initPrint();
        }

        UUID tem = UUID.randomUUID();

        uuid = tem.toString();

    }

    private void initView() {
        imageView = findViewById(R.id.iv_singin);
        chartView = findViewById(R.id.chartView);
        tvItem = findViewById(R.id.tv_item);
        tvSample = findViewById(R.id.tv_sample);

        tvUnitNmae = findViewById(R.id.tv_unit_name);
        tvOpeName = findViewById(R.id.tv_ope_name);
        tvOpeNum = findViewById(R.id.tv_ope_num);
        tvSampleNum = findViewById(R.id.tv_sample_num);
        tvSampleDate = findViewById(R.id.tv_sample_date);
    }

    private void initChartView() {
        chartView.setTouchEnabled(true);
        //设置是否可以拖拽，缩放
        chartView.setDragEnabled(true);
        chartView.setScaleEnabled(true);
       /* chartView.getAxisLeft().setAxisMinimum(0);
        chartView.getAxisRight().setAxisMinimum(0);*/
    }

    private void initScan() {
        sm = new ScanDevice();
        sm.setScanLaserMode(8);
        sm.setOutScanMode(0);
        if (!sm.isScanOpened()) {
            sm.openScan();
        }
    }

    private void initPrwmission() {
        //新增6.0权限
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET};
        if (!EasyPermissions.hasPermissions(context, perms)) {
            EasyPermissions.requestPermissions(this, "检测需要相关权限",
                    200, perms);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CheckDeviceApplication.isDesign) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(SCAN_ACTION);
            registerReceiver(mScanReceiver, filter);
        }

        //  sm.startScan();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.scan_reg:
                Toast.makeText(DetectionActivity.this, "扫描", Toast.LENGTH_SHORT).show();
                break;

            case R.id.iv_singin:
                //  Toast.makeText(DetectionActivity.this,"获取检测卡图片",Toast.LENGTH_SHORT).show();
                //  startActivity(new Intent(DetectionActivity.this,GetDetectionCardActivity.class));
                //先将扫描关闭
                if (CheckDeviceApplication.isDesign) {
                    if (sm.isScanOpened()) {
                        sm.stopScan();
                        sm.closeScan();
                    }
                }
                startActivityForResult(new Intent(DetectionActivity.this, GetDetectionCardActivity.class), 100);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case 100:
                if (resultCode == RESULT_OK) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());
                    checkTime = formatter.format(curDate);

                    String path = data.getStringExtra("path");

                    File file = new File(path);
                    Bitmap bitmap = BitmapFactory.decodeFile(path);

                    imageView.setImageBitmap(bitmap);
                    sacnLine(bitmap);
                    //打印
                    doPrint();

                    if (CheckDeviceApplication.isDesign) {
                        if (!sm.isScanOpened()) {
                            sm.openScan();
                        }
                    }
                }
                break;
            default:
                break;
        }

    }

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            String barcodeStr = new String(barocode, 0, barocodelen);
            Log.e("扫描", barcodeStr);
            //http://zjdy.chinafst.cn:8085/lezhifda/os/java/pub/sampling/detail.shtml?samplingNO=W201804040003
           /* showScanResult.append("广播输出：");
            showScanResult.append(barcodeStr);
            showScanResult.append("\n");*/
            //       showScanResult.setText(barcodeStr);

            if (barcodeStr.contains("samplingNO=")) {

                tvItem.setText("");
                tvSample.setText("");
                tvSampleDate.setText("");
                tvSampleNum.setText("");
                tvOpeName.setText("");
                tvOpeNum.setText("");
                tvUnitNmae.setText("");
                sampleNum=barcodeStr.substring(barcodeStr.indexOf("=") + 1);

                requestInfo(sampleNum);
            } else {
                Toast.makeText(getApplicationContext(), "未找到相应信息,请重新扫描", Toast.LENGTH_SHORT).show();
            }


            sm.stopScan();
        }

    };

    private void requestInfo(String substring) {
        String type = "app/downloadSample?samplingNO=";
        OkHttpUtils
                .post()
                .url(Utils.urlBase + type + substring)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Log.e("获取数据", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if ("success1".equals(jsonObject.getString("resultCode"))) {
                                JSONArray result = jsonObject.getJSONArray("result");
                                if (result.length() > 0) {
                                    JSONObject object = (JSONObject) result.get(0);
                                    JSONArray details = object.getJSONArray("details");
                                    if (details.length() > 0) {
                                        JSONObject detail = (JSONObject) details.get(0);
                                        String item = detail.getString("checkItem");
                                        String sampleNmae = detail.getString("sampleName");
                                        //  String limit=detail.getString("checkValue");
                                        String stand = detail.getString("checkItem");

                                        tvItem.setText(item);
                                        tvSample.setText(sampleNmae);
                                        //  tvLimit.setText(limit);
                                    }
                                    tvUnitNmae.setText(object.getString("ckoName"));
                                    tvOpeName.setText(object.getString("cdName"));
                                    tvOpeNum.setText(object.getString("cdIdNum"));
                                    tvSampleDate.setText(object.getString("sampingDate"));
                                    tvSampleNum.setText(object.getString("sampingNO"));
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("解析错误", e.toString());

                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sm != null) {
            sm.stopScan();
            unregisterReceiver(mScanReceiver);
        }
    }

    //扫描
    @SuppressLint("StaticFieldLeak")
    private void sacnLine(final Bitmap bitmap) {

        new AsyncTask<Bitmap, Integer, String[]>() {
            @Override
            protected String[] doInBackground(Bitmap... strings) {
                int width = strings[0].getWidth() / 2;
                int height = strings[0].getHeight();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < height; i = i + 4) {
                    sb.append(Color.green(strings[0].getPixel(width, i)) + ",");
                }
                String[] split = sb.toString().split(",");
                String[] tem = new String[70];
                for (int i = 0; i < 70; i++) {
                    tem[i] = split[i + 50];

                }

              /*  for(int i=0;i<tem.length;i++){
                    tem[i]=
                }*/
                return tem;
            }

       /*     @Override
            protected String[] doInBackground(Bitmap... bitmaps) {
                return new String[0];
            }*/

            @Override
            protected void onPostExecute(String[] s) {


                Log.e("数据", Arrays.toString(s));
                ArrayList<String> xVals = new ArrayList<String>();
                for (int i = 0; i < s.length; i++) {
                    xVals.add(i + "");
                }
                ArrayList<Entry> yVals = new ArrayList<Entry>();
                for (int i = 0; i < s.length; i++) {
                    yVals.add(new Entry(i, Float.parseFloat(s[i]) * 10));

                }
                LineDataSet set1 = new LineDataSet(yVals, "胶体金曲线图");
                set1.setDrawValues(false);
                set1.setCircleRadius(1f);

                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1);
                LineData data = new LineData(set1);

                // set data
                chartView.setData(data);

                saveCheckRecord();


            }
        }.execute(bitmap);

    }

    private void saveCheckRecord() {

        CheckRecordBean checkRecordBean = new CheckRecordBean();
        checkRecordBean.setId(uuid);
        checkRecordBean.setFood_name(tvSample.getText().toString().trim());
        checkRecordBean.setItem_name(tvItem.getText().toString().trim());
        checkRecordBean.setCheck_date(checkTime);
        checkRecordBean.setReg_name(tvUnitNmae.getText().toString().trim());
        checkRecordBean.setOpe_shop_name(tvOpeName.getText().toString().trim());
        checkRecordBean.setOpe_shop_code(tvOpeNum.getText().toString().trim());
        checkRecordBean.setSampling_no(sampleNum);

        //检测结果相关
        checkRecordBean.setConclusion("合格");
        checkRecordBean.setCheck_result("阴性");
        try {
            GreenDaoUtils.getDaoSession().getCheckRecordBeanDao().insertOrReplace(checkRecordBean);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "存储中", Toast.LENGTH_SHORT).show();
        }

    }

    private void initPrint() {
        //1  单片机上电
        try {
            FileWriter localFileWriterOn = new FileWriter(new File("/proc/gpiocontrol/set_sam"));
            localFileWriterOn.write("1");
            localFileWriterOn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //2 接口初始化
        mApi = CheckDeviceApplication.getInstance().getPosApi();
        //设置初始化回调
        mApi.setOnComEventListener(mCommEventListener);
        //使用扩展方式初始化接口
        mApi.initDeviceEx("/dev/ttyMT2");
        mPrintQueue = new PrintQueue(this, mApi);
        mPrintQueue.init();
    }

    PosApi.OnCommEventListener mCommEventListener = new PosApi.OnCommEventListener() {

        @Override
        public void onCommState(int cmdFlag, int state, byte[] resp, int respLen) {
            // TODO Auto-generated method stub
            switch (cmdFlag) {
                case PosApi.POS_INIT:
                    if (state == PosApi.COMM_STATUS_SUCCESS) {
                        //  LogPrint.toast(context,"初始化成功");
                    } else {
                        //   LogPrint.toast(context,"初始化失败");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (CheckDeviceApplication.isDesign) {
            //销毁接口
            if (mPrintQueue != null) {
                mPrintQueue.close();
            }

            if (mApi != null) {
                mApi.closeDev();
            }

            try {
                FileWriter localFileWriterOn = new FileWriter(new File("/proc/gpiocontrol/set_sam"));
                localFileWriterOn.write("0");
                localFileWriterOn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //打印
    private void doPrint() {

        StringBuilder sb = new StringBuilder();
        //  sb.append("  ");
        // sb.append(tvItem.getText().toString().trim());
        byte[] text;
        try {
            text = sb.toString().getBytes("GBK");
            byte[] _2x = new byte[]{0x1b, 0x57, 0x02};
            byte[] _1x = new byte[]{0x1b, 0x57, 0x01};
/*
            byte[] mData = new byte[3+text.length];
            //1倍字体大小  默认
            System.arraycopy(_1x, 0, mData, 0, _1x.length);
            System.arraycopy(text, 0, mData, _1x.length, text.length);
            mPrintQueue.addText(60, mData);*/


            sb = new StringBuilder();
           /* sb.append(name.getIndex());
            sb.append("\n");*/
            sb.append("检测项目:" + tvItem.getText().toString().trim());
            sb.append("\n");
            sb.append("样品名称:" + tvSample.getText());
            sb.append("\n");
            sb.append("检测结果:阴性");
            sb.append("\n");
            sb.append("检测结论:合格");
            sb.append("\n");
            sb.append("检测时间:" + checkTime);
            sb.append("\n");
            sb.append("------------------------------");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");

            text = sb.toString().getBytes("GBK");

            //1倍字体大小
            byte[] mData2 = new byte[3 + text.length];
            //1倍字体大小  默认
            System.arraycopy(_1x, 0, mData2, 0, _1x.length);
            System.arraycopy(text, 0, mData2, _1x.length, text.length);
            mPrintQueue.addText(60, mData2);
            mPrintQueue.printStart();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void upLoadRecord(){
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
        });
    }


}
