package com.dms.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;

/**
 * Created by USER on 1/25/2016.
 */
public class ForgotPassActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtDone = (TextView) findViewById(R.id.txtDone);

        rltBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtDone:

                break;
        }
    }
}