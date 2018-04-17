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

public class DoScanPicActivity2 extends AppCompatActivity implements View.OnClickListener {


    private CropImageView ivCrop,ivCrop2;
    private static final int TAKE_PIC = 100;
    private File tempFile,tempFile2;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_scan2);
        ivCrop = findViewById(R.id.iv_crop);
        ivCrop2 = findViewById(R.id.iv_crop2);
        String id = getIntent().getStringExtra("id");
        if(TextUtils.isEmpty(id)){
            setResult(RESULT_CANCELED);
            finish();
        }else{
            tempFile = new File(getExternalCacheDir(), id+".jpg");
            tempFile2 = new File(getExternalCacheDir(), id+"_1.jpg");
            if(tempFile.exists()){
                tempFile.delete();

            }   if(tempFile2.exists()){
                tempFile2.delete();

            }
                try {
                    tempFile.createNewFile();
                    tempFile2.createNewFile();
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

            Bitmap rectBitmap = Bitmap.createBitmap(selectedBitmap, 0, 0,  selectedBitmap.getWidth()/2, selectedBitmap.getHeight());//截
            Bitmap rectBitmap2 = Bitmap.createBitmap(selectedBitmap, selectedBitmap.getWidth()/2, 0,  selectedBitmap.getWidth()/2, selectedBitmap.getHeight());//截


            // Bitmap rotaBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
            // mBitmap.recycle();
            // Bitmap rectBitmap = Bitmap.createBitmap(rotaBitmap, width/2+450, height/2-200,  300, 800);//截
            // rotaBitmap.recycle();


            if (rectBitmap != null&&rectBitmap2!=null) {
                ivCrop.setImageToCrop(rectBitmap);
                ivCrop2.setImageToCrop(rectBitmap2);
            }
        }
    }

/*    @Override
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
    }*/

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
                if (ivCrop.canRightCrop()&&ivCrop2.canRightCrop()) {
                    Bitmap crop = ivCrop.crop();
                    Bitmap crop2=ivCrop2.crop();
                    if (crop != null&&crop2!=null) {
                        saveImage(crop, tempFile);
                        saveImage(crop2,tempFile2);
                        setResult(RESULT_OK);
                    } else {
                        setResult(RESULT_CANCELED);
                    }
                    finish();
                } else {
                    Toast.makeText(DoScanPicActivity2.this, "请重试", Toast.LENGTH_SHORT).show();
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
