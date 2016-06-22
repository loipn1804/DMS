package com.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.daocontroller.DiaryTypeController;
import com.dms.staticfunction.BroadcastValue;
import com.dms.staticfunction.StaticFunction;
import com.dms.volleycontroller.DiaryOtherVolley;
import com.dms.volleycontroller.callback.DiaryOtherCallback;

import greendao.DiaryOther;
import greendao.DiaryType;

/**
 * Created by USER on 3/2/2016.
 */
public class DiaryOtherCreateActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtSave;
    private TextView txtNameActionBar;

    private EditText edtContent;

    private DiaryOther mDiaryOther;
    private long diary_type_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_other_create);

        if (getIntent().hasExtra("diary_type_id")) {
            diary_type_id = getIntent().getLongExtra("diary_type_id", 0);
            if (diary_type_id == 0) {
                finish();
            }
        } else {
            finish();
        }

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtSave = (TextView) findViewById(R.id.txtSave);
        txtNameActionBar = (TextView) findViewById(R.id.txtNameActionBar);

        edtContent = (EditText) findViewById(R.id.edtContent);

        rltBack.setOnClickListener(this);
        txtSave.setOnClickListener(this);

        DiaryType diaryType = DiaryTypeController.getById(this, diary_type_id);
        if (diaryType != null) {
            txtNameActionBar.setText("Add Diary " + diaryType.getName());
        } else {
            txtNameActionBar.setText("Add Diary");
        }
    }

    private void initData() {
        mDiaryOther = new DiaryOther();
        mDiaryOther.setDiary_type_id(diary_type_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtSave:
                createDiaryOther();
                break;
        }
    }

    private void createDiaryOther() {
        String content = edtContent.getText().toString().trim();
        if (content.length() == 0) {
            showToastError(getString(R.string.blank_content));
        } else {
            mDiaryOther.setContent(content);
            showProgressDialog(false);
            final DiaryOtherVolley diaryOtherVolley = new DiaryOtherVolley(this);
            diaryOtherVolley.createDiaryOther(new DiaryOtherCallback.CreateDiaryOtherCallback() {
                @Override
                public void onSuccess(boolean success, String result) {
                    if (success) {
                        Intent intent = new Intent();
                        intent.setAction(BroadcastValue.DIARY_OTHER_REFRESH_LIST);
                        intent.putExtra(BroadcastValue.DIARY_TYPE_ID, diary_type_id);
                        sendBroadcast(intent);
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
            }, mDiaryOther);
        }
    }
}
