package com.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.dms.R;
import com.dms.volleycontroller.CaseVolley;
import com.dms.volleycontroller.DormitoryVolley;
import com.dms.volleycontroller.WorkerVolley;
import com.dms.volleycontroller.callback.CaseCallback;
import com.dms.volleycontroller.callback.DormitoryCallback;
import com.dms.volleycontroller.callback.WorkerCallback;

/**
 * Created by USER on 1/13/2016.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getListCategory();
//            }
//        }, 100);

        Log.e("time", System.currentTimeMillis() + "--in");
//        getListCategory();
        getDormitoryInfo();
    }

    private void getListCategory() {
        CaseVolley caseVolley = new CaseVolley(this);
        caseVolley.getListCategory(new CaseCallback.GetListCategoryCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                getListStatus();
            }

            @Override
            public void onError(String error) {
                getListStatus();
            }
        });
    }

    private void getListStatus() {
        CaseVolley caseVolley = new CaseVolley(this);
        caseVolley.getListStatus(new CaseCallback.GetListStatusCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
//                getDormitoryCategory();
                getDormitoryInfo();
            }

            @Override
            public void onError(String error) {
//                getDormitoryCategory();
                getDormitoryInfo();
            }
        });
    }

    private void getDormitoryCategory() {
        DormitoryVolley dormitoryVolley = new DormitoryVolley(this);
        dormitoryVolley.getDormitoryCategory(new DormitoryCallback.GetDormitoryCategoryCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                getDormitoryInfo();
            }

            @Override
            public void onError(String error) {
                getDormitoryInfo();
            }
        });
    }

    private void getDormitoryInfo() {
        WorkerVolley workerVolley = new WorkerVolley(this);
        workerVolley.getDormitoryInfo(new WorkerCallback.GetDormitoryInfoCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                Intent intent = new Intent(SplashActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
                Log.e("time", System.currentTimeMillis() + "--out");
            }

            @Override
            public void onError(String error) {
                Intent intent = new Intent(SplashActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
                Log.e("time", System.currentTimeMillis() + "--out");
            }
        });
    }
}
