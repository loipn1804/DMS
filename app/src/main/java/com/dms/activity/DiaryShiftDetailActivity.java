package com.dms.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.daocontroller.DiaryShiftController;
import com.dms.daocontroller.DiaryVisitorController;
import com.dms.staticfunction.BroadcastValue;
import com.dms.staticfunction.StaticFunction;
import com.dms.volleycontroller.DiaryShiftVolley;
import com.dms.volleycontroller.callback.DiaryShiftCallback;

import greendao.DiaryShift;

/**
 * Created by USER on 3/2/2016.
 */
public class DiaryShiftDetailActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtSave;
    private TextView txtNameActionBar;

    private RelativeLayout rltName;
    private TextView txtName;
    private EditText edtContent;
    private TextView txtEnterTime;
    private TextView txtExitTime;
    private RelativeLayout rltShiftExit;
    private LinearLayout lnlExitTime;
    private View lineExitTime;

    private DiaryShift mDiaryShift;
    private long diary_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_shift_create);

        if (getIntent().hasExtra("diary_id")) {
            diary_id = getIntent().getLongExtra("diary_id", 0);
            if (diary_id == 0) {
                finish();
            }
            mDiaryShift = DiaryShiftController.getById(this, diary_id);
            if (mDiaryShift == null) {
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

        rltName = (RelativeLayout) findViewById(R.id.rltName);
        txtName = (TextView) findViewById(R.id.txtName);
        edtContent = (EditText) findViewById(R.id.edtContent);
        txtEnterTime = (TextView) findViewById(R.id.txtEnterTime);
        txtExitTime = (TextView) findViewById(R.id.txtExitTime);
        rltShiftExit = (RelativeLayout) findViewById(R.id.rltShiftExit);
        lnlExitTime = (LinearLayout) findViewById(R.id.lnlExitTime);
        lineExitTime = findViewById(R.id.lineExitTime);

        rltBack.setOnClickListener(this);
        rltShiftExit.setOnClickListener(this);

        txtSave.setVisibility(View.GONE);

        txtNameActionBar.setText("Shift Detail");
    }

    private void initData() {
        txtName.setText(mDiaryShift.getName());
        edtContent.setText(mDiaryShift.getContent());
        edtContent.setEnabled(false);
        txtEnterTime.setText(mDiaryShift.getEnter_time());

        if (mDiaryShift.getIs_exited() == 1) {
            rltShiftExit.setVisibility(View.GONE);
            lnlExitTime.setVisibility(View.VISIBLE);
            lineExitTime.setVisibility(View.VISIBLE);
            txtExitTime.setText(mDiaryShift.getExit_time());
        } else {
            rltShiftExit.setVisibility(View.VISIBLE);
            lnlExitTime.setVisibility(View.GONE);
            lineExitTime.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltShiftExit:
                shiftExit();
                break;
            case R.id.rltBack:
                finish();
                break;
        }
    }

    private void shiftExit() {
        showProgressDialog(false);
        DiaryShiftVolley diaryShiftVolley = new DiaryShiftVolley(this);
        diaryShiftVolley.shiftExit(new DiaryShiftCallback.ShiftExitCallback() {
            @Override
            public void onSuccess(boolean success, String result, String exit_time) {
                if (success) {
                    mDiaryShift.setIs_exited(1);
                    mDiaryShift.setExit_time(exit_time);
                    DiaryShiftController.insertOrUpdate(DiaryShiftDetailActivity.this, mDiaryShift);

                    rltShiftExit.setVisibility(View.GONE);
                    lnlExitTime.setVisibility(View.VISIBLE);
                    lineExitTime.setVisibility(View.VISIBLE);
                    txtExitTime.setText(mDiaryShift.getExit_time());

                    StaticFunction.sendBroadCast(DiaryShiftDetailActivity.this, BroadcastValue.DIARY_SHIFT_NOTIFY_LIST);

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
        }, diary_id + "");
    }
}
