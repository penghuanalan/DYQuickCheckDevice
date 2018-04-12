package cn.chinafst.dyquickcheckdevice;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class DetectionActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView;
    private Context context=this;
    private ScanDevice sm;
    private final static String SCAN_ACTION = "scan.rcv.message";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);
        imageView=findViewById(R.id.iv_singin);
        initPrwmission();
        if(CheckDeviceApplication.isDesign){
            initScan();
        }

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
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(context, perms)) {
            EasyPermissions.requestPermissions(this, "检测需要相关权限",
                    200, perms);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
        //  sm.startScan();
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.scan_reg:
                Toast.makeText(DetectionActivity.this,"扫描",Toast.LENGTH_SHORT).show();


                break;

            case R.id.iv_singin:
              //  Toast.makeText(DetectionActivity.this,"获取检测卡图片",Toast.LENGTH_SHORT).show();
             //  startActivity(new Intent(DetectionActivity.this,GetDetectionCardActivity.class));
                //先将扫描关闭
                if(CheckDeviceApplication.isDesign){
                    if(sm.isScanOpened()){
                        sm.stopScan();
                        sm.closeScan();
                    }
                }
                startActivityForResult(new Intent(DetectionActivity.this,GetDetectionCardActivity.class),100);
                break;
                default:break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){

            case 100:
                if(resultCode==RESULT_OK){
                    String path=data.getStringExtra("path");

                    File file =new File(path);
                    Bitmap bitmap= BitmapFactory.decodeFile(path);

                    imageView.setImageBitmap(bitmap);
                    if(CheckDeviceApplication.isDesign){
                        if (!sm.isScanOpened()) {
                            sm.openScan();
                        }
                    }
                }
                break;
                default:break;
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
            int spance = barcodeStr.indexOf("?id=");
            if (spance != -1) {
                String result2 = barcodeStr.substring(spance + 4);

            } else {

            }

           /* showScanResult.append("广播输出：");
            showScanResult.append(barcodeStr);
            showScanResult.append("\n");*/
            //       showScanResult.setText(barcodeStr);
            sm.stopScan();
        }

    };

    @Override
    protected void onPause() {
        super.onPause();
        if (sm != null) {
            sm.stopScan();
        }
    }
}
