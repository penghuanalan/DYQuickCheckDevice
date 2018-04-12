package cn.chinafst.dyquickcheckdevice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class DetectionActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);
        imageView=findViewById(R.id.iv_singin);
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
                }
                break;
                default:break;
        }

    }
}
