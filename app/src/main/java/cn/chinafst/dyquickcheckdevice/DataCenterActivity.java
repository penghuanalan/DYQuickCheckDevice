package cn.chinafst.dyquickcheckdevice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class DataCenterActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_center);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ll_sample:
                startActivity(new Intent(DataCenterActivity.this,FoodItemActivity.class));

                break;
            case R.id.ll_check_item:
                startActivity(new Intent(DataCenterActivity.this,CheckItemActivity.class));

                break;
                default:break;
        }
    }
}
