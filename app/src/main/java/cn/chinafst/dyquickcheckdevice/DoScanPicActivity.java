package cn.chinafst.dyquickcheckdevice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.File;

import me.pqpo.smartcropperlib.view.CropImageView;

public class DoScanPicActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private View lineC, lineT;
    private CropImageView ivCrop;
    private static final int TAKE_PIC = 100;
    private File tempFile;
    private int orignHeihtC=500;
    private int orignHeihtT=500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_scan);
        ivCrop = findViewById(R.id.iv_crop);
        lineC = findViewById(R.id.line_c);
        lineT = findViewById(R.id.line_t);
        tempFile = new File(getExternalCacheDir(), "scan.jpg");

       /* RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) lineC.getLayoutParams();
        layoutParams.topMargin=100;*/
        lineC.setPivotY(500);

        Intent startCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        if (startCameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(startCameraIntent, TAKE_PIC);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        Bitmap selectedBitmap = null;
        if (requestCode == TAKE_PIC && tempFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(tempFile.getPath(), options);
            options.inJustDecodeBounds = false;
            options.inSampleSize = calculateSampleSize(options);
            selectedBitmap = BitmapFactory.decodeFile(tempFile.getPath(), options);
            if (selectedBitmap != null) {
                ivCrop.setImageToCrop(selectedBitmap);
            }
        }
    }

    private int calculateSampleSize(BitmapFactory.Options options) {
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        int sampleSize = 1;
        int destHeight = 1000;
        int destWidth = 1000;
        if (outHeight > destHeight || outWidth > destHeight) {
            if (outHeight > outWidth) {
                sampleSize = outHeight / destHeight;
            } else {
                sampleSize = outWidth / destWidth;
            }
        }
        if (sampleSize < 1) {
            sampleSize = 1;
        }
        return sampleSize;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_ok:


                break;
            case R.id.btn_cancel:


                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(view.getId()==R.id.line_c||view.getId()==R.id.line_t){


            return true;
        }


        return false;
    }
}
