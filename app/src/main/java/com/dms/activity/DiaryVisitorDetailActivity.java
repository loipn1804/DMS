package com.dms.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.daocontroller.DiaryVisitorController;
import com.dms.daocontroller.WorkerController;
import com.dms.staticfunction.BroadcastValue;
import com.dms.staticfunction.StaticFunction;
import com.dms.volleycontroller.DiaryVisitorVolley;
import com.dms.volleycontroller.callback.DiaryVisitorCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import greendao.DiaryVisitor;

/**
 * Created by USER on 3/1/2016.
 */
public class DiaryVisitorDetailActivity extends BaseActivity implements View.OnClickListener {

    private int CAMERA_REQUEST = 127;
    private int PICK_IMAGE_REQUEST = 128;

    private String FOLDER = "DMS";
    private String FILENAME = "";
    private String FILETYPE = ".png";

    private RelativeLayout rltBack;
    private TextView txtSave;
    private TextView txtNameActionBar;

    private RelativeLayout rltType;
    private TextView txtType;
    private EditText edtName;
    private EditText edtContent;
    private TextView txtEnterTime;
    private TextView txtExitTime;
    private RelativeLayout rltVisitorExit;
    private LinearLayout lnlExitTime;
    private View lineExitTime;

    private ImageView imv1;
    private ImageView imv2;
    private ImageView imvEdit1;
    private ImageView imvEdit2;

    private DiaryVisitor mDiaryVisitor;
    private long diary_id;

    private Uri uriImage1;
    private Uri uriImage2;
    private int iChooseImage;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_visitor_detail);

        if (getIntent().hasExtra("diary_id")) {
            diary_id = getIntent().getLongExtra("diary_id", 0);
            if (diary_id == 0) {
                finish();
            }
            mDiaryVisitor = DiaryVisitorController.getById(this, diary_id);
            if (mDiaryVisitor == null) {
                finish();
            }
        } else {
            finish();
        }

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.color.white)
                .showImageOnLoading(R.color.white)
                .cacheInMemory(true)
                .cacheOnDisk(true).build();

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtSave = (TextView) findViewById(R.id.txtSave);
        txtNameActionBar = (TextView) findViewById(R.id.txtNameActionBar);

        rltType = (RelativeLayout) findViewById(R.id.rltType);
        txtType = (TextView) findViewById(R.id.txtType);
        edtName = (EditText) findViewById(R.id.edtName);
        edtContent = (EditText) findViewById(R.id.edtContent);
        txtEnterTime = (TextView) findViewById(R.id.txtEnterTime);
        txtExitTime = (TextView) findViewById(R.id.txtExitTime);
        rltVisitorExit = (RelativeLayout) findViewById(R.id.rltVisitorExit);
        lnlExitTime = (LinearLayout) findViewById(R.id.lnlExitTime);
        lineExitTime = findViewById(R.id.lineExitTime);

        imv1 = (ImageView) findViewById(R.id.imv1);
        imv2 = (ImageView) findViewById(R.id.imv2);
        imvEdit1 = (ImageView) findViewById(R.id.imvEdit1);
        imvEdit2 = (ImageView) findViewById(R.id.imvEdit2);

        rltBack.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        rltVisitorExit.setOnClickListener(this);
//        imv1.setOnClickListener(this);
//        imv2.setOnClickListener(this);
//        imvEdit1.setOnClickListener(this);
//        imvEdit2.setOnClickListener(this);
//        rltType.setOnClickListener(this);

        txtNameActionBar.setText("Visitor Detail");
        imvEdit1.setVisibility(View.GONE);
        imvEdit2.setVisibility(View.GONE);
        txtSave.setVisibility(View.GONE);
        edtName.setEnabled(false);
        edtContent.setEnabled(false);
    }

    private void initData() {
        txtType.setText(mDiaryVisitor.getIs_staff() == 1 ? "Staff" : "Visitor");
        edtName.setText(mDiaryVisitor.getName());
        edtContent.setText(mDiaryVisitor.getContent());
        imageLoader.displayImage(StaticFunction.BASE_URL + mDiaryVisitor.getImage_1(), imv1, options);
        imageLoader.displayImage(StaticFunction.BASE_URL + mDiaryVisitor.getImage_2(), imv2, options);
        txtEnterTime.setText(mDiaryVisitor.getEnter_time());
        if (mDiaryVisitor.getIs_exited() == 1) {
            rltVisitorExit.setVisibility(View.GONE);
            lnlExitTime.setVisibility(View.VISIBLE);
            lineExitTime.setVisibility(View.VISIBLE);
            txtExitTime.setText(mDiaryVisitor.getExit_time());
        } else {
            rltVisitorExit.setVisibility(View.VISIBLE);
            lnlExitTime.setVisibility(View.GONE);
            lineExitTime.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltType:

                break;
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltVisitorExit:
                visitorExit();
                break;
            case R.id.txtSave:

                break;
            case R.id.imv1:

                break;
            case R.id.imv2:

                break;
            case R.id.imvEdit1:

                break;
            case R.id.imvEdit2:

                break;
        }
    }

    private void visitorExit() {
        showProgressDialog(false);
        DiaryVisitorVolley diaryVisitorVolley = new DiaryVisitorVolley(this);
        diaryVisitorVolley.visitorExit(new DiaryVisitorCallback.VisitorExitCallback() {
            @Override
            public void onSuccess(boolean success, String result, String exit_time) {
                if (success) {
                    mDiaryVisitor.setIs_exited(1);
                    mDiaryVisitor.setExit_time(exit_time);
                    DiaryVisitorController.insertOrUpdate(DiaryVisitorDetailActivity.this, mDiaryVisitor);

                    rltVisitorExit.setVisibility(View.GONE);
                    lnlExitTime.setVisibility(View.VISIBLE);
                    lineExitTime.setVisibility(View.VISIBLE);
                    txtExitTime.setText(mDiaryVisitor.getExit_time());

                    StaticFunction.sendBroadCast(DiaryVisitorDetailActivity.this, BroadcastValue.DIARY_VISITOR_NOTIFY_LIST);

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
