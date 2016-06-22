package com.dms.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.staticfunction.BroadcastValue;
import com.dms.staticfunction.StaticFunction;
import com.dms.volleycontroller.DiaryShiftVolley;
import com.dms.volleycontroller.UserVolley;
import com.dms.volleycontroller.callback.DiaryShiftCallback;
import com.dms.volleycontroller.callback.UserCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import greendao.DiaryShift;
import greendao.User;

/**
 * Created by USER on 3/1/2016.
 */
public class DiaryShiftCreateActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtSave;
    private TextView txtNameActionBar;

    private RelativeLayout rltName;
    private TextView txtName;
    private EditText edtContent;

    private DiaryShift mDiaryShift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_shift_create);

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtSave = (TextView) findViewById(R.id.txtSave);
        txtNameActionBar = (TextView) findViewById(R.id.txtNameActionBar);

        rltName = (RelativeLayout) findViewById(R.id.rltName);
        txtName = (TextView) findViewById(R.id.txtName);
        edtContent = (EditText) findViewById(R.id.edtContent);

        rltBack.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        rltName.setOnClickListener(this);

        txtNameActionBar.setText("Add Diary Shift");

        findViewById(R.id.lnlEnterTime).setVisibility(View.GONE);
        findViewById(R.id.lineEnterTime).setVisibility(View.GONE);
        findViewById(R.id.lnlExitTime).setVisibility(View.GONE);
        findViewById(R.id.lineExitTime).setVisibility(View.GONE);
        findViewById(R.id.rltShiftExit).setVisibility(View.GONE);
    }

    private void initData() {
        mDiaryShift = new DiaryShift();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltName:
                showPopupLoginStaff();
                break;
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtSave:
                createDiaryShift();
                break;
        }
    }

    private void createDiaryShift() {
        String content = edtContent.getText().toString().trim();
        if (mDiaryShift.getUser_id() == null) {
            showToastError(getString(R.string.choose_shift_staff));
        } else if (content.length() == 0) {
            showToastError(getString(R.string.blank_content));
        } else {
            mDiaryShift.setContent(content);
            showProgressDialog(false);
            DiaryShiftVolley diaryShiftVolley = new DiaryShiftVolley(this);
            diaryShiftVolley.createDiaryShift(new DiaryShiftCallback.CreateDiaryShiftCallback() {
                @Override
                public void onSuccess(boolean success, String result) {
                    if (success) {
                        StaticFunction.sendBroadCast(DiaryShiftCreateActivity.this, BroadcastValue.DIARY_OTHER_REFRESH_LIST);
                        finish();

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
            }, mDiaryShift);
        }
    }

    public void showPopupLoginStaff() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_login_staff);

        final EditText edtUsername = (EditText) dialog.findViewById(R.id.edtUsername);
        final EditText edtPassword = (EditText) dialog.findViewById(R.id.edtPassword);
        RelativeLayout rltSignin = (RelativeLayout) dialog.findViewById(R.id.rltSignin);

        rltSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (username.length() == 0) {
                    showToastError(getString(R.string.blank_email));
                } else if (password.length() == 0) {
                    showToastError(getString(R.string.blank_pass));
                } else {
                    showProgressDialog(false);
                    UserVolley userVolley = new UserVolley(DiaryShiftCreateActivity.this);
                    userVolley.loginVisitorStaff(new UserCallback.LoginVisitorStaffCallback() {
                        @Override
                        public void onSuccess(boolean success, String result, User user) {
                            if (success) {
                                txtName.setText(user.getName());
                                mDiaryShift.setUser_id(user.getId());
                                mDiaryShift.setName(user.getName());

                                dialog.dismiss();
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
                    }, username, password);
                }
            }
        });

        dialog.show();
    }
}
