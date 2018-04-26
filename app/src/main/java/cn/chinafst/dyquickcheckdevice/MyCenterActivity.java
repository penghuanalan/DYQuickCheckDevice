package cn.chinafst.dyquickcheckdevice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MyCenterActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_center);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ll_sample:
                LogPrint.toast(MyCenterActivity.this,"暂无更新");

                break;
            case R.id.ll_check_item:
                LogPrint.toast(MyCenterActivity.this,"已是更新版本");

                break;
                default:break;
        }
    }
}
