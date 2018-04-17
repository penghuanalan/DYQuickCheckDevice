package cn.chinafst.dyquickcheckdevice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import me.pqpo.smartcropperlib.view.CropImageView;

public class DoScanPicActivity extends AppCompatActivity implements View.OnClickListener {


    private CropImageView ivCrop;
    private static final int TAKE_PIC = 100;
    private File tempFile;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_scan);
        ivCrop = findViewById(R.id.iv_crop);
        String id = getIntent().getStringExtra("id");
        if(TextUtils.isEmpty(id)){
            setResult(RESULT_CANCELED);
            finish();
        }else{
            tempFile = new File(getExternalCacheDir(), id+".jpg");
            if(tempFile.exists()){
                tempFile.delete();

            }
                try {
                    tempFile.createNewFile();
                    Intent startCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                    if (startCameraIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(startCameraIntent, TAKE_PIC);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"无权限",Toast.LENGTH_SHORT).show();
                }

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
                if (ivCrop.canRightCrop()) {
                    Bitmap crop = ivCrop.crop();
                    if (crop != null) {
                        saveImage(crop, tempFile);
                        setResult(RESULT_OK);
                    } else {
                        setResult(RESULT_CANCELED);
                    }
                    finish();
                } else {
                    Toast.makeText(DoScanPicActivity.this, "请重试", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                finish();

                break;
            default:
                break;
        }
    }

    private void saveImage(Bitmap bitmap, File saveFile) {
        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
