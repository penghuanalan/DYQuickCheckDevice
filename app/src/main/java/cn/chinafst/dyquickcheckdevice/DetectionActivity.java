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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import cn.chinafst.dyquickcheckdevice.bean.MyFFT;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;
import zyapi.PrintQueue;

public class DetectionActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView, imageView2;
    private Context context = this;
    private ScanDevice sm;
    private final static String SCAN_ACTION = "scan.rcv.message";
    private LineChart chartView, chartView2;
    private TextView   tvUnitNmae, tvOpeName, tvOpeNum, tvSampleDate, tvSampleNum, tvResult, tvResult2;
    private EditText  tvSample,tvItem,tvItem2;
    private PosApi mApi = null;
    private PrintQueue mPrintQueue = null;
    private String checkTime = "";
    private String uuid = "";
    private String foodCode = "", sampleNo = "", sampleNo2 = "";
    private boolean ifTwoChannel = false;
    private LinearLayout llItem2, llresult02;
    private Button bt01, bt02, bt03, bt04;

    //抽样单号
    private String sampleNum = "";

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
        uuid = UUID.randomUUID().toString();
    }

    private void initView() {
        imageView = findViewById(R.id.iv_singin);
        imageView2 = findViewById(R.id.iv_singin2);
        imageView2.setVisibility(View.GONE);
        chartView = findViewById(R.id.chartView);
        chartView.setVisibility(View.GONE);

        chartView2 = findViewById(R.id.chartView2);
        chartView2.setVisibility(View.GONE);
        tvItem = findViewById(R.id.tv_item);
        tvItem2 = findViewById(R.id.tv_item2);
        tvSample = findViewById(R.id.tv_sample);

        llItem2 = findViewById(R.id.channel2_item);
        llItem2.setVisibility(View.GONE);

        llresult02 = findViewById(R.id.ll_result_2);
        llresult02.setVisibility(View.GONE);

        tvUnitNmae = findViewById(R.id.tv_unit_name);
        tvOpeName = findViewById(R.id.tv_ope_name);
        tvOpeNum = findViewById(R.id.tv_ope_num);
        tvSampleNum = findViewById(R.id.tv_sample_num);
        tvSampleDate = findViewById(R.id.tv_sample_date);
        tvResult = findViewById(R.id.tv_check_result);
        tvResult2 = findViewById(R.id.tv_check_result2);

        bt01 = findViewById(R.id.bt_01);
        bt02 = findViewById(R.id.bt_02);
        bt03 = findViewById(R.id.bt_03);
        bt04 = findViewById(R.id.bt_04);

        bt02.setVisibility(View.VISIBLE);
        bt03.setVisibility(View.VISIBLE);
        bt04.setVisibility(View.VISIBLE);
        bt02.setText("上传");
        bt03.setText("打印");
        bt04.setText("返回");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        checkTime = formatter.format(curDate);

    }

    private void initChartView() {
        chartView.setTouchEnabled(true);
        chartView.setDragEnabled(true);
        chartView.setScaleEnabled(true);

        chartView2.setTouchEnabled(true);
        chartView2.setDragEnabled(true);
        chartView2.setScaleEnabled(true);
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
                Intent intent;
                if (ifTwoChannel) {
                    intent = new Intent(DetectionActivity.this, DoScanPicActivity2.class);
                    intent.putExtra("id", uuid);
                    startActivityForResult(intent, 200);

                } else {
                    // intent= new Intent(DetectionActivity.this,DoScanPicActivity.class);
                    startActivityForResult(new Intent(DetectionActivity.this, GetDetectionCardActivity.class), 100);

                }

                break;
            case R.id.bt_03:
                doPrint();
                break;
            case R.id.bt_04:
                finish();
                break;

            case R.id.bt_02:
                upLoadRecord();
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
                    String path = data.getStringExtra("path");

                    File file = new File(path);
                    Bitmap bitmap = BitmapFactory.decodeFile(path);

                    imageView.setImageBitmap(bitmap);
                    sacnLine4(bitmap);
                   /* if (CheckDeviceApplication.isDesign) {
                        if (!sm.isScanOpened()) {
                            sm.openScan();
                        }
                    }*/
                }
                break;
            case 200:

                if (resultCode == RESULT_OK) {
                    File tempFile = new File(getExternalCacheDir(), uuid + ".jpg");
                    File tempFile2 = new File(getExternalCacheDir(), uuid + "_1.jpg");
                    if (tempFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getPath());
                        imageView.setImageBitmap(bitmap);
                        sacnLine3(bitmap);
                    }
                    if (tempFile2.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(tempFile2.getPath());
                        imageView2.setVisibility(View.VISIBLE);
                        imageView2.setImageBitmap(bitmap);
                        sacnLine3_2(bitmap);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "未获取检测卡", Toast.LENGTH_SHORT).show();
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

            if (barcodeStr.contains("samplingNO=")) {

                tvItem.setText("");
                tvItem2.setText("");
                tvSample.setText("");
                tvSampleDate.setText("");
                tvSampleNum.setText("");
                tvOpeName.setText("");
                tvOpeNum.setText("");
                tvUnitNmae.setText("");
                sampleNum = barcodeStr.substring(barcodeStr.indexOf("=") + 1);

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
                                    if (details.length() == 1) {
                                        JSONObject detail = (JSONObject) details.get(0);
                                        String item = detail.getString("checkItem");
                                        String sampleNmae = detail.getString("sampleName");
                                        //  String limit=detail.getString("checkValue");
                                        String stand = detail.getString("checkItem");
//
                                        //foodCode,sampleNo;平台获取的
                                        foodCode = detail.getString("foodCode");
                                        sampleNo = detail.getString("sampleNO");
                                        tvItem.setText(item);
                                        tvSample.setText(sampleNmae);
                                        //  tvLimit.setText(limit);

                                        ifTwoChannel = false;
                                        llItem2.setVisibility(View.GONE);
                                        imageView2.setVisibility(View.GONE);
                                        chartView2.setVisibility(View.GONE);
                                    } else if (details.length() >= 2) {
                                        ifTwoChannel = true;
                                        llresult02.setVisibility(View.VISIBLE);
                                        llItem2.setVisibility(View.VISIBLE);
                                        chartView2.setVisibility(View.VISIBLE);
                                        JSONObject detail = (JSONObject) details.get(0);
                                        String item = detail.getString("checkItem");
                                        String sampleNmae = detail.getString("sampleName");
                                        //  String limit=detail.getString("checkValue");
                                        String stand = detail.getString("checkItem");
//
                                        //foodCode,sampleNo;平台获取的
                                        foodCode = detail.getString("foodCode");
                                        sampleNo = detail.getString("sampleNO");
                                        tvItem.setText(item);
                                        tvSample.setText(sampleNmae);


                                        JSONObject detail2 = (JSONObject) details.get(1);
                                        String item2 = detail2.getString("checkItem");
                                        //foodCode,sampleNo;平台获取的
                                        sampleNo2 = detail2.getString("sampleNO");
                                        tvItem2.setText(item2);

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
      /*  if (sm != null) {
            sm.stopScan();
            unregisterReceiver(mScanReceiver);
        }*/
    }


    //扫描
    @SuppressLint("StaticFieldLeak")
    private void sacnLine4(final Bitmap bitmap) {

        new AsyncTask<Bitmap, Integer, String[]>() {
            @Override
            protected String[] doInBackground(Bitmap... strings) {
                int width = strings[0].getWidth();
                int height = strings[0].getHeight();

                Log.e("图片信息", strings[0].getWidth() + "---" + strings[0].getHeight());
                StringBuffer sb = new StringBuffer();
                int startX = (int) (width / 3);
                int endX = (int) (width * 2 / 3);
                int startY = 0;
                int endY = (int) height;


                for (int i = startY; i < endY; i++) {

                    int count = 0;
                    for (int j = startX; j < endX; j = j + 4) {

                        //  sb.append(Color.green(strings[0].getPixel(width, i)) + ",");
                        int color = strings[0].getPixel(j, i);
                        final int r = (color >> 16) & 0xff;
                        final int g = (color >> 8) & 0xff;
                        final int b = color & 0xff;

                        int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                        ;
                        count = count + gray;
                    }
                    sb.append(count + ",");

                }

                Log.e("原始数据", sb.toString());
                String[] split = sb.toString().split(",");

                //603-2277

                int count = (int) (split.length * 0.28);
                int length = (int) (split.length * 0.3);
                String[] tem = new String[length];
                for (int i = 0; i < length; i++) {
                    tem[i] = split[i + count];
                }
                return tem;
            }


            @Override
            protected void onPostExecute(String[] s) {


                double[] a = new double[s.length];
                double[] b = new double[s.length];

                for (int i = 0; i < s.length; i++) {
                    a[i] = Double.parseDouble(s[i]);
                    b[i] = 0;
                }
                new MyFFT().fft(s.length, a, b, 1);

                int ij = s.length / 6;
                int ii = s.length - ij;
                for (int j = ij; j < ii; j++) {
                    a[j] = 0;
                    b[j] = 0;
                }
                new MyFFT().fft(s.length, a, b, -1);

                double[] doubles = DyUtils2.dyMath(a);
                int[] index = DyUtils2.index;

                if (index[0] != 0 && doubles[0] > 0.005) {

                    if (index[1] != 0 && doubles[1] > 0.005) {

                        tvResult.setText("阴性");
                    } else {
                        tvResult.setText("阳性");
                    }

                    CheckRecordBean checkRecordBean = new CheckRecordBean();
                    checkRecordBean.setId(uuid);
                    checkRecordBean.setFood_name(tvSample.getText().toString().trim());

                    //临时使用
                    checkRecordBean.setFood_id(foodCode);
                    checkRecordBean.setItem_name(tvItem.getText().toString().trim());
                    checkRecordBean.setCheck_date(checkTime);
                    checkRecordBean.setReg_name(tvUnitNmae.getText().toString().trim());
                    checkRecordBean.setOpe_shop_name(tvOpeName.getText().toString().trim());
                    checkRecordBean.setOpe_shop_code(tvOpeNum.getText().toString().trim());
                    checkRecordBean.setSampling_no(sampleNum);
                    checkRecordBean.setBatch_number(sampleNo);

                    //检测结果相关
                    if (tvResult.getText().equals("阴性")) {
                        checkRecordBean.setConclusion("合格");
                        checkRecordBean.setCheck_result("阴性");

                    } else {
                        checkRecordBean.setConclusion("不合格");
                        checkRecordBean.setCheck_result("阳性");
                    }
                    saveCheckRecord(checkRecordBean);

                } else {
                    Toast.makeText(getApplicationContext(), "检测图片异常,请重新拍摄", Toast.LENGTH_SHORT).show();
                    tvResult.setText("无效卡图片,请重新拍摄");
                }

                for (int i = 0; i < doubles.length; i++) {

                    LogPrint.e("坐标" + index[i] + "值" + doubles[i]);
                }

                ArrayList<String> xVals = new ArrayList<String>();
                for (int i = 0; i < s.length; i++) {
                    xVals.add(i + "");
                }
                ArrayList<Entry> yVals = new ArrayList<Entry>();
                for (int i = 0; i < s.length; i++) {
                    yVals.add(new Entry(i, (float) a[i]));

                }
                LineDataSet set1 = new LineDataSet(yVals, "胶体金曲线图");
                set1.setDrawValues(false);
                set1.setCircleRadius(1f);
                set1.setCircleColor(Color.BLUE);
                set1.setColor(Color.RED);
                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1);
                LineData data = new LineData(set1);

                // set data
                chartView.setVisibility(View.VISIBLE);
                chartView.setData(data);
                chartView.invalidate();


                //上传数据
                // upLoadRecord();

            }
        }.execute(bitmap);

    }

    //扫描
    @SuppressLint("StaticFieldLeak")
    private void sacnLine(final Bitmap bitmap) {

        new AsyncTask<Bitmap, Integer, String[]>() {
            @Override
            protected String[] doInBackground(Bitmap... strings) {
                int width = strings[0].getWidth() / 2;
                int height = strings[0].getHeight();

                Log.e("图片信息", strings[0].getWidth() + "---" + strings[0].getHeight());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < height; i = i + 4) {

                    //  sb.append(Color.green(strings[0].getPixel(width, i)) + ",");
                    int color = strings[0].getPixel(width, i);
                    final int r = (color >> 16) & 0xff;
                    final int g = (color >> 8) & 0xff;
                    final int b = color & 0xff;

                    int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                    ;
                    sb.append(gray + ",");
                }

                Log.e("原始数据", sb.toString());
                String[] split = sb.toString().split(",");

                //603-2277

                int count = (int) (split.length * 0.26);
                int length = (int) (split.length * 0.3);
                String[] tem = new String[length];
                for (int i = 0; i < length; i++) {
                    tem[i] = split[i + count];
                }
                return tem;
            }


            @Override
            protected void onPostExecute(String[] s) {


                double[] a = new double[s.length];
                double[] b = new double[s.length];

                for (int i = 0; i < s.length; i++) {
                    a[i] = Double.parseDouble(s[i]);
                    b[i] = 0;
                }
                new MyFFT().fft(s.length, a, b, 1);

                int ij = s.length / 6;
                int ii = s.length - ij;
                for (int j = ij; j < ii; j++) {
                    a[j] = 0;
                    b[j] = 0;
                }
                new MyFFT().fft(s.length, a, b, -1);

                double[] doubles = DyUtils.dyMath(a);
                int[] index = DyUtils.index;

                for (int i = 0; i < doubles.length; i++) {

                    LogPrint.e("坐标" + index[i] + "值" + doubles[i]);
                }

                ArrayList<String> xVals = new ArrayList<String>();
                for (int i = 0; i < s.length; i++) {
                    xVals.add(i + "");
                }
                ArrayList<Entry> yVals = new ArrayList<Entry>();
                for (int i = 0; i < s.length; i++) {
                    yVals.add(new Entry(i, (float) a[i]));

                }
                LineDataSet set1 = new LineDataSet(yVals, "胶体金曲线图");
                set1.setDrawValues(false);
                set1.setCircleRadius(1f);

                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1);
                LineData data = new LineData(set1);

                // set data
                chartView.setVisibility(View.VISIBLE);
                chartView.setData(data);

                //  saveCheckRecord();
                //上传数据
                // upLoadRecord();

            }
        }.execute(bitmap);

    }    //扫描

    @SuppressLint("StaticFieldLeak")
    private void sacnLine2(final Bitmap bitmap) {

        new AsyncTask<Bitmap, Integer, String[]>() {
            @Override
            protected String[] doInBackground(Bitmap... strings) {
                int width = strings[0].getWidth() / 2;
                int height = strings[0].getHeight();

                Log.e("图片信息", strings[0].getWidth() + "---" + strings[0].getHeight());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < height; i = i + 4) {
                    sb.append(Color.green(strings[0].getPixel(width, i)) + ",");
                }

                Log.e("原始数据", sb.toString());
                String[] split = sb.toString().split(",");


                int count = (int) (split.length * 0.4 - 10);
                int length = (int) (split.length * 0.2);
                String[] tem = new String[length];
                for (int i = 0; i < length; i++) {
                    tem[i] = split[i + count];
                }
                return tem;
            }


            @Override
            protected void onPostExecute(String[] s) {


                double[] a = new double[s.length];
                double[] b = new double[s.length];

                for (int i = 0; i < s.length; i++) {
                    a[i] = Double.parseDouble(s[i]);
                    b[i] = 0;
                }
                new MyFFT().fft(s.length, a, b, 1);

                int ij = s.length / 6;
                int ii = s.length - ij;
                for (int j = ij; j < ii; j++) {
                    a[j] = 0;
                    b[j] = 0;
                }
                new MyFFT().fft(s.length, a, b, -1);

                Log.e("数据", Arrays.toString(s));
                ArrayList<String> xVals = new ArrayList<String>();
                for (int i = 0; i < s.length; i++) {
                    xVals.add(i + "");
                }
                ArrayList<Entry> yVals = new ArrayList<Entry>();
                for (int i = 0; i < s.length; i++) {
                    yVals.add(new Entry(i, (float) a[i]));

                }
                LineDataSet set1 = new LineDataSet(yVals, "胶体金曲线图2");
                set1.setDrawValues(false);
                set1.setCircleRadius(1f);

                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1);
                LineData data = new LineData(set1);

                // set data
                chartView2.setVisibility(View.VISIBLE);
                chartView2.setData(data);
                chartView2.invalidate();

                //   saveCheckRecord();
                //上传数据
                // upLoadRecord();

            }
        }.execute(bitmap);

    }


    //扫描
    @SuppressLint("StaticFieldLeak")
    private void sacnLine3(final Bitmap bitmap) {

        new AsyncTask<Bitmap, Integer, String[]>() {
            @Override
            protected String[] doInBackground(Bitmap... strings) {
                int width = strings[0].getWidth();
                int height = strings[0].getHeight();

                Log.e("图片信息", strings[0].getWidth() + "---" + strings[0].getHeight());
                StringBuffer sb = new StringBuffer();

                //这里改为逐行扫描
                int getHeight = (int) (height * 0.39);
                int endHeight = (int) (height * 0.57);

                int getWidght = width / 3;
                int endWidght = width * 2 / 3;


                for (int i = getHeight; i < endHeight; i++) {
                    int count = 0;
                    for (int j = getWidght; j < endWidght; j = j + 4) {
                        int color = strings[0].getPixel(j, i);
                        final int r = (color >> 16) & 0xff;
                        final int g = (color >> 8) & 0xff;
                        final int b = color & 0xff;
                        int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);

                        count = count + gray;
                    }
                    sb.append(count + ",");
                }

                Log.e("原始数据", sb.toString());
                String[] split = sb.toString().split(",");

                return split;
            }


            @Override
            protected void onPostExecute(String[] s) {


                double[] a = new double[s.length];
                double[] b = new double[s.length];

                for (int i = 0; i < s.length; i++) {
                    a[i] = Double.parseDouble(s[i]);
                    b[i] = 0;
                }

                new MyFFT().fft(s.length, a, b, 1);

                int ij = s.length / 6;
                int ii = s.length - ij;
                for (int j = ij; j < ii; j++) {
                    a[j] = 0;
                    b[j] = 0;
                }
                new MyFFT().fft(s.length, a, b, -1);

                Log.e("平滑数据", Arrays.toString(a));

                double[] doubles = DyUtils.dyMath(a);

                int[] index = DyUtils.index;

                for (int i = 0; i < doubles.length; i++) {

                    LogPrint.e("坐标" + index[i] + "值" + doubles[i]);
                }

                if (index[0] != 0 && doubles[0] > 0.005) {
                    if (index[1] != 0 && doubles[1] > 0.005) {
                        tvResult.setText("阴性");

                    } else {
                        tvResult.setText("阳性");
                    }

                    CheckRecordBean checkRecordBean = new CheckRecordBean();
                    checkRecordBean.setId(uuid);
                    checkRecordBean.setFood_name(tvSample.getText().toString().trim());

                    //临时使用
                    checkRecordBean.setFood_id(foodCode);
                    checkRecordBean.setItem_name(tvItem.getText().toString().trim());
                    checkRecordBean.setCheck_date(checkTime);
                    checkRecordBean.setReg_name(tvUnitNmae.getText().toString().trim());
                    checkRecordBean.setOpe_shop_name(tvOpeName.getText().toString().trim());
                    checkRecordBean.setOpe_shop_code(tvOpeNum.getText().toString().trim());
                    checkRecordBean.setSampling_no(sampleNum);
                    checkRecordBean.setBatch_number(sampleNo);

                    //检测结果相关
                    if (tvResult.getText().equals("阴性")) {
                        checkRecordBean.setConclusion("合格");
                        checkRecordBean.setCheck_result("阴性");

                    } else {
                        checkRecordBean.setConclusion("不合格");
                        checkRecordBean.setCheck_result("阳性");
                    }
                    saveCheckRecord(checkRecordBean);

                } else {
                    tvResult.setText("无效图片,请重试");
                }
                Log.e("T点信息", "坐标" + index[1] + "值" + String.format("%.3f", doubles[1]));

                ArrayList<Double> doubles1 = DyUtils.doubles;

                double[] d = new double[doubles1.size()];

                for (int i = 0; i < d.length; i++) {
                    d[i] = doubles1.get(i);
                }

                ArrayList<String> xVals = new ArrayList<String>();
                for (int i = 0; i < s.length; i++) {
                    xVals.add(i + "");
                }
                ArrayList<Entry> yVals = new ArrayList<Entry>();
                for (int i = 0; i < s.length; i++) {
                    //yVals.add(new Entry(i, (float) a[i]));
                    yVals.add(new Entry(i, (float) a[i]));

                }
                LineDataSet set1 = new LineDataSet(yVals, "胶体金曲线图");
                set1.setDrawValues(false);
                set1.setCircleRadius(1f);
                set1.setCircleColor(Color.BLUE);
                set1.setColor(Color.RED);
                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1);
                LineData data = new LineData(set1);

                // set data
                chartView.setVisibility(View.VISIBLE);
                chartView.setData(data);
                chartView.invalidate();
            }
        }.execute(bitmap);

    }


    //扫描
    @SuppressLint("StaticFieldLeak")
    private void sacnLine3_2(final Bitmap bitmap) {

        new AsyncTask<Bitmap, Integer, String[]>() {
            @Override
            protected String[] doInBackground(Bitmap... strings) {
                int width = strings[0].getWidth();
                int height = strings[0].getHeight();

                Log.e("图片信息", strings[0].getWidth() + "---" + strings[0].getHeight());
                StringBuffer sb = new StringBuffer();

                //这里改为逐行扫描
                int getHeight = (int) (height * 0.39);
                int endHeight = (int) (height * 0.57);

                int getWidght = width / 3;
                int endWidght = width * 2 / 3;


                for (int i = getHeight; i < endHeight; i++) {
                    int count = 0;
                    for (int j = getWidght; j < endWidght; j = j + 4) {
                        int color = strings[0].getPixel(j, i);
                        final int r = (color >> 16) & 0xff;
                        final int g = (color >> 8) & 0xff;
                        final int b = color & 0xff;
                        int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);

                        count = count + gray;
                    }
                    sb.append(count + ",");
                }

                Log.e("原始数据", sb.toString());
                String[] split = sb.toString().split(",");

                return split;
            }


            @Override
            protected void onPostExecute(String[] s) {


                double[] a = new double[s.length];
                double[] b = new double[s.length];

                for (int i = 0; i < s.length; i++) {
                    a[i] = Double.parseDouble(s[i]);
                    b[i] = 0;
                }

                new MyFFT().fft(s.length, a, b, 1);

                int ij = s.length / 6;
                int ii = s.length - ij;
                for (int j = ij; j < ii; j++) {
                    a[j] = 0;
                    b[j] = 0;
                }
                new MyFFT().fft(s.length, a, b, -1);

                Log.e("平滑数据", Arrays.toString(a));

                double[] doubles = DyUtils.dyMath(a);

                int[] index = DyUtils.index;

                for (int i = 0; i < doubles.length; i++) {

                    LogPrint.e("坐标" + index[i] + "值" + doubles[i]);
                }

                if (index[0] != 0 && doubles[0] > 0.005) {
                    if (index[1] != 0 && doubles[1] > 0.005) {
                        tvResult2.setText("阴性");

                    } else {
                        tvResult2.setText("阳性");
                    }

                    CheckRecordBean checkRecordBean = new CheckRecordBean();
                    checkRecordBean.setId(uuid + "_2");
                    checkRecordBean.setFood_name(tvSample.getText().toString().trim());

                    //临时使用
                    checkRecordBean.setFood_id(foodCode);
                    checkRecordBean.setItem_name(tvItem2.getText().toString().trim());
                    checkRecordBean.setCheck_date(checkTime);
                    checkRecordBean.setReg_name(tvUnitNmae.getText().toString().trim());
                    checkRecordBean.setOpe_shop_name(tvOpeName.getText().toString().trim());
                    checkRecordBean.setOpe_shop_code(tvOpeNum.getText().toString().trim());
                    checkRecordBean.setSampling_no(sampleNum);
                    checkRecordBean.setBatch_number(sampleNo);

                    //检测结果相关
                    if (tvResult2.getText().equals("阴性")) {
                        checkRecordBean.setConclusion("合格");
                        checkRecordBean.setCheck_result("阴性");

                    } else {
                        checkRecordBean.setConclusion("不合格");
                        checkRecordBean.setCheck_result("阳性");
                    }

                    saveCheckRecord(checkRecordBean);

                } else {
                    tvResult2.setText("无效图片,请重试");
                }
                Log.e("T点信息", "坐标" + index[1] + "值" + String.format("%.3f", doubles[1]));


                ArrayList<Double> doubles1 = DyUtils.doubles;

                double[] d = new double[doubles1.size()];

                for (int i = 0; i < d.length; i++) {
                    d[i] = doubles1.get(i);
                }

                ArrayList<String> xVals = new ArrayList<String>();
                for (int i = 0; i < s.length; i++) {
                    xVals.add(i + "");
                }
                ArrayList<Entry> yVals = new ArrayList<Entry>();
                for (int i = 0; i < s.length; i++) {
                    //yVals.add(new Entry(i, (float) a[i]));
                    yVals.add(new Entry(i, (float) a[i]));

                }
                LineDataSet set1 = new LineDataSet(yVals, "胶体金曲线图");
                set1.setDrawValues(false);
                set1.setCircleRadius(1f);
                set1.setCircleColor(Color.BLUE);
                set1.setColor(Color.RED);
                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1);
                LineData data = new LineData(set1);

                // set data
                chartView2.setVisibility(View.VISIBLE);
                chartView2.setData(data);
                chartView2.invalidate();

                //上传数据
                // upLoadRecord();

            }
        }.execute(bitmap);

    }


    private void saveCheckRecord(CheckRecordBean checkRecordBean) {
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

            if (sm != null) {
                sm.stopScan();
                unregisterReceiver(mScanReceiver);
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

        sb.append("检测结果");
        byte[] text;
        try {
           // text = sb.toString().getBytes("GBK");
            byte[] _2x = new byte[]{0x1b, 0x57, 0x02};
            byte[] _1x = new byte[]{0x1b, 0x57, 0x01};
            //byte[] mData = new byte[3 + text.length];
            //1倍字体大小  默认
          //  System.arraycopy(_2x, 0, mData, 0, _2x.length);
           // System.arraycopy(text, 0, mData, _2x.length, text.length);
           // mPrintQueue.addText(60, mData);

            sb = new StringBuilder();
            sb.append("          检测结果");
            sb.append("\n");
            sb.append("样品名称:" + tvSample.getText());
            sb.append("\n");
            if (ifTwoChannel) {
                sb.append("检测项目一:" + tvItem.getText().toString().trim());
                sb.append("\n");
                if (tvResult.getText().equals("阴性")) {
                    sb.append("检测结果:阴性");
                    sb.append("\n");
                    sb.append("检测结论:合格");
                } else {
                    sb.append("检测结果:阳性");
                    sb.append("\n");
                    sb.append("检测结论:不合格");
                }

                sb.append("\n");
                sb.append("检测项目二:" + tvItem2.getText().toString().trim());
                sb.append("\n");
                if (tvResult2.getText().equals("阴性")) {
                    sb.append("检测结果:阴性");
                    sb.append("\n");
                    sb.append("检测结论:合格");
                } else {
                    sb.append("检测结果:阳性");
                    sb.append("\n");
                    sb.append("检测结论:不合格");
                }

            } else {
                sb.append("检测项目:" + tvItem.getText().toString().trim());
                sb.append("\n");
                if (tvResult.getText().equals("阴性")) {
                    sb.append("检测结果:阴性");
                    sb.append("\n");
                    sb.append("检测结论:合格");
                } else {
                    sb.append("检测结果:阳性");
                    sb.append("\n");
                    sb.append("检测结论:不合格");
                }
            }

            sb.append("\n");
            sb.append("检测时间:" + checkTime);
            sb.append("\n");

            sb.append("检测人:一号检测员");
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

    public void upLoadRecord() {
        List<HashMap<String, String>> items = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("sampleNo", sampleNo);
        map.put("checkResult", tvResult.getText().toString());
        map.put("foodCode", foodCode);
        map.put("foodName", tvSample.getText().toString().trim());
        map.put("checkItemName", tvItem.getText().toString().trim());
        map.put("checkMethod", "胶体金法");
        map.put("sysCode", uuid);
        if (tvResult.getText().toString().equals("阴性")) {
            map.put("checkConclusion", "合格");
        } else {
            map.put("checkConclusion", "不合格");
        }

        map.put("checkDate", checkTime);
        HashMap<String, Object> second = new HashMap<>();
        second.put("sampleNO", sampleNum);
        items.add(map);

        second.put("checkResultList", items);
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        list.add(second);
        HashMap<String, List<HashMap<String, Object>>> map1 = new HashMap<>();

        map1.put("result", list);
        Gson gson = new Gson();
        String s = gson.toJson(map1);
        Log.e("上传", s);

        OkHttpUtils.post().addParams("json", s)
                .url(Utils.urlBase + "app/upLoadData.do")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("上传错误", e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("上传", response);

                if (response != null && response.contains("操作成功")) {
                    LogPrint.toast(context, "上传成功");
                }
            }
        });
    }
}
