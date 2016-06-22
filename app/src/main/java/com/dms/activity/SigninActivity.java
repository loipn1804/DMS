package com.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.daocontroller.UserController;
import com.dms.volleycontroller.UserVolley;
import com.dms.volleycontroller.callback.UserCallback;

/**
 * Created by USER on 1/13/2016.
 */
public class SigninActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltSignin;
    private TextView txtForgetPass;
    private EditText edtUsername;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initView();
        initData();
    }

    private void initView() {
        rltSignin = (RelativeLayout) findViewById(R.id.rltSignin);
        txtForgetPass = (TextView) findViewById(R.id.txtForgetPass);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        rltSignin.setOnClickListener(this);
        txtForgetPass.setOnClickListener(this);

        edtUsername.setText("loi@gmail.com");
        edtPassword.setText("123456");

        UserController.isLogin(this);
    }

    private void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserController.isLogin(this)) {
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltSignin:
                login();
                break;
            case R.id.txtForgetPass:
                Intent intentForgotPass = new Intent(this, ForgotPassActivity.class);
                startActivity(intentForgotPass);
                break;
        }
    }

    private void login() {
        String email = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (email.length() == 0) {
            showToastError(getString(R.string.blank_email));
        } else if (password.length() == 0) {
            showToastError(getString(R.string.blank_pass));
        } else {
            showProgressDialog(true);
            UserVolley userVolley = new UserVolley(this);
            userVolley.login(new UserCallback.LoginCallback() {
                @Override
                public void onSuccess(boolean success, String result) {
                    if (success) {
                        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showToastError(result);
                    }
                    hideProgressDialog();
                }

                @Override
                public void onError(String error) {
                    showToastError(error);
                    hideProgressDialog();
                }
            }, email, password);
        }
    }
}
