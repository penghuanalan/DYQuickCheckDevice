package cn.chinafst.dyquickcheckdevice;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.device.ScanDevice;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GetDetectionCardActivity extends AppCompatActivity implements View.OnClickListener{

    private SurfaceView surfaceView;
    private RedLineView redLineView;
    private Camera camera;
    int width,height;
    private static AsyncTask task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//把屏幕设置成横屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_card);

        surfaceView = findViewById(R.id.sv_idcard);
        redLineView = findViewById(R.id.rv_card);

        closeScan();

    }


    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder holder = surfaceView.getHolder();
        holder.setKeepScreenOn(true);
        holder.addCallback(new MySurfaceCallback());

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

    }

    private void closeScan() {
        if(CheckDeviceApplication.isDesign){
            ScanDevice sm=new ScanDevice();
            if(sm.isScanOpened()){
                sm.stopScan();
                sm.closeScan();

                Log.e("openCscan","摄像头关闭");
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_take_pic:
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean b, Camera camera) {

                        Toast.makeText(getApplicationContext(),"自动对焦",Toast.LENGTH_SHORT).show();
                        if (b) {
                            camera.takePicture(null, null, new Camera.PictureCallback() {
                                @Override
                                public void onPictureTaken(byte[] data, Camera camera) {
                                    if (null != data) {
                                        Bitmap mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图

                                        Matrix matrix = new Matrix();
                                        matrix.postRotate((float)90.0);
                                        Bitmap rotaBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
                                        mBitmap.recycle();
                                        Bitmap rectBitmap = Bitmap.createBitmap(rotaBitmap, width/2+450, height/2-200,  300, 800);//截
                                        rotaBitmap.recycle();

                                        camera.stopPreview();
                                        savePic(rectBitmap);

                                    }
                                }
                            });
                        }
                    }
                });

                break;
                default:break;
        }
    }


    class MySurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {



            try {
                camera = Camera.open();
                camera.setDisplayOrientation(90);//相机旋转90度
                Camera.Parameters params = camera.getParameters();
                camera.setPreviewDisplay(surfaceView.getHolder());
                camera.startPreview();


            } catch (IOException e) {
                e.printStackTrace();

            }
        }




        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {


        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (camera != null) {
                camera.release();
                camera = null;
            }
        }
    }

    private void savePic(final Bitmap mBitmap) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory() + "/DYdetect/";
                File file= new File(path);
                if(!file.exists()){
                    file.mkdir();
                }
                long dataTake = System.currentTimeMillis();
                String picName=path+dataTake+".jpg";
                try {
                    FileOutputStream fout = new FileOutputStream(picName);
                    BufferedOutputStream bos = new BufferedOutputStream(fout);
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                    mBitmap.recycle();
                    Intent intent= new Intent();
                    intent.putExtra("path",picName);
                    setResult(RESULT_OK,intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }){}.start();


    }
}
