package com.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.daocontroller.UserController;
import com.dms.volleycontroller.UserVolley;
import com.dms.volleycontroller.callback.UserCallback;

import java.util.ArrayList;

/**
 * Created by USER on 1/25/2016.
 */
public class ChangePassActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtSave;
    private EditText edtOldPassword;
    private EditText edtNewPassword;
    private EditText edtConfirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtSave = (TextView) findViewById(R.id.txtSave);
        edtOldPassword = (EditText) findViewById(R.id.edtOldPassword);
        edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = (EditText) findViewById(R.id.edtConfirmNewPassword);

        rltBack.setOnClickListener(this);
        txtSave.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtSave:
                changePassword();
                break;
        }
    }

    private void changePassword() {
        String oldPassword = edtOldPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmNewPassword = edtConfirmNewPassword.getText().toString().trim();
        if (oldPassword.length() == 0) {
            showToastError(getString(R.string.blank_pass));
        } else if (newPassword.length() == 0) {
            showToastError(getString(R.string.blank_new_pass));
        } else if (!newPassword.equals(confirmNewPassword)) {
            showToastError(getString(R.string.confirm_pass_invalidate));
        } else {
            showProgressDialog(true);
            UserVolley userVolley = new UserVolley(this);
            userVolley.changePassword(new UserCallback.ChangePasswordCallback() {
                @Override
                public void onSuccess(boolean success, String result) {
                    if (success) {
                        showToastOk(result);
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
            }, UserController.getCurrentUser(ChangePassActivity.this).getId() + "", oldPassword, newPassword);
        }
    }
}
