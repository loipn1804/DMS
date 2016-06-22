package com.dms.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.adapter.ListAdapter;
import com.dms.interfaces.ItemListCallback;
import com.dms.staticfunction.BroadcastValue;
import com.dms.staticfunction.StaticFunction;
import com.dms.volleycontroller.DiaryVisitorVolley;
import com.dms.volleycontroller.UserVolley;
import com.dms.volleycontroller.callback.DiaryVisitorCallback;
import com.dms.volleycontroller.callback.UserCallback;
import com.dms.volleycontroller.customrequest.DiaryVisitorCreateRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import greendao.DiaryVisitor;
import greendao.User;
import greendao.Worker;

/**
 * Created by USER on 3/1/2016.
 */
public class DiaryVisitorCreateActivity extends BaseActivity implements View.OnClickListener {

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

    private ImageView imv1;
    private ImageView imv2;
    private ImageView imvEdit1;
    private ImageView imvEdit2;

    private DiaryVisitor mDiaryVisitor;

    private Uri uriImage1;
    private Uri uriImage2;
    private int iChooseImage;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_visitor_create);

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

        imv1 = (ImageView) findViewById(R.id.imv1);
        imv2 = (ImageView) findViewById(R.id.imv2);
        imvEdit1 = (ImageView) findViewById(R.id.imvEdit1);
        imvEdit2 = (ImageView) findViewById(R.id.imvEdit2);

        rltBack.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        imv1.setOnClickListener(this);
        imv2.setOnClickListener(this);
        imvEdit1.setOnClickListener(this);
        imvEdit2.setOnClickListener(this);
        rltType.setOnClickListener(this);

        txtNameActionBar.setText("Add Diary Visitor");
    }

    private void initData() {
        mDiaryVisitor = new DiaryVisitor();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltType:
                showPopupListType();
                break;
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtSave:
                createDiaryVisitor();
                break;
            case R.id.imv1:
                if (uriImage1 == null) {
                    iChooseImage = 1;
                    showPopupPickImage();
                }
                break;
            case R.id.imv2:
                if (uriImage2 == null) {
                    iChooseImage = 2;
                    showPopupPickImage();
                }
                break;
            case R.id.imvEdit1:
                iChooseImage = 1;
                showPopupPickImage();
                break;
            case R.id.imvEdit2:
                iChooseImage = 2;
                showPopupPickImage();
                break;
        }
    }

    private void createDiaryVisitor() {
        String name = edtName.getText().toString().trim();
        String content = edtContent.getText().toString().trim();
        if (mDiaryVisitor.getIs_staff() == null) {
            showToastError(getString(R.string.choose_visitor_type));
        } else if (name.length() == 0) {
            showToastError(getString(R.string.blank_name));
        } else if (content.length() == 0) {
            showToastError(getString(R.string.blank_content));
        } else if (mDiaryVisitor.getIs_staff() == 0 && uriImage1 == null) {
            showToastError(getString(R.string.choose_img_visitor_1));
        } else if (mDiaryVisitor.getIs_staff() == 0 && uriImage2 == null) {
            showToastError(getString(R.string.choose_img_visitor_2));
        } else {
            mDiaryVisitor.setName(name);
            mDiaryVisitor.setContent(content);
            showProgressDialog(false);
            DiaryVisitorVolley diaryVisitorVolley = new DiaryVisitorVolley(this);
            diaryVisitorVolley.createDiaryVisitor(new DiaryVisitorCallback.CreateDiaryVisitorCallback() {
                @Override
                public void onSuccess(boolean success, String result) {
                    if (success) {
                        StaticFunction.sendBroadCast(DiaryVisitorCreateActivity.this, BroadcastValue.DIARY_VISITOR_REFRESH_LIST);
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
            }, mDiaryVisitor, uriImage1, uriImage2);
        }
    }

    public void showPopupListType() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list);

        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        ListView lvData = (ListView) dialog.findViewById(R.id.lvData);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText("Sex");

        final List<String> list = new ArrayList<>();
        list.add("Visitor");
        list.add("Staff");

        ItemListCallback itemListCallback = new ItemListCallback() {
            @Override
            public void ItemClick(int position) {
                txtType.setText(list.get(position));
                mDiaryVisitor.setIs_staff(position);
                if (position == 0) {
                    setTypeVisitor();
                } else {
                    showPopupLoginStaff();
                }
                dialog.dismiss();
            }
        };

        ListAdapter listAdapter = new ListAdapter(this, list, itemListCallback);
        lvData.setAdapter(listAdapter);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
                    UserVolley userVolley = new UserVolley(DiaryVisitorCreateActivity.this);
                    userVolley.loginVisitorStaff(new UserCallback.LoginVisitorStaffCallback() {
                        @Override
                        public void onSuccess(boolean success, String result, User user) {
                            if (success) {
                                edtName.setText(user.getName());
                                mDiaryVisitor.setUser_id(user.getId());
                                imageLoader.displayImage(StaticFunction.BASE_URL + user.getAvatar(), imv1, options);
                                imageLoader.displayImage(StaticFunction.BASE_URL + user.getAvatar(), imv2, options);
                                imvEdit1.setVisibility(View.GONE);
                                imvEdit2.setVisibility(View.GONE);
                                imv1.setClickable(false);
                                imv2.setClickable(false);
                                edtName.setEnabled(false);

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

    private void setTypeVisitor() {
        edtName.setText("");
        mDiaryVisitor.setUser_id(0l);
        imageLoader.displayImage("drawable://" + R.drawable.add_picture_bg, imv1, options);
        imageLoader.displayImage("drawable://" + R.drawable.add_picture_bg, imv2, options);
        imvEdit1.setVisibility(View.VISIBLE);
        imvEdit2.setVisibility(View.VISIBLE);
        imv1.setClickable(true);
        imv2.setClickable(true);
        edtName.setEnabled(true);

        uriImage1 = null;
        uriImage2 = null;
    }

    ///// choose image
    public void showPopupPickImage() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_pick_image);

        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        LinearLayout lnlCamera = (LinearLayout) dialog.findViewById(R.id.lnlCamera);
        LinearLayout lnlGallery = (LinearLayout) dialog.findViewById(R.id.lnlGallery);

        lnlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                dialog.dismiss();
            }
        });

        lnlGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                dialog.dismiss();
            }
        });

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openCamera() {
        FILENAME = FOLDER + "_" + Settings.Secure.ANDROID_ID + "_" + System.currentTimeMillis() + FILETYPE;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, createUriForCamera());
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        try {
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } catch (ActivityNotFoundException e) {

        }
    }

    private Uri createUriForCamera() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + FOLDER);

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File file = new File(myDir, FILENAME);
        Uri uri = Uri.fromFile(file);

//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

        return uri;
    }

    private Uri getFile() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + FOLDER);
        File file = new File(myDir, FILENAME);
        Uri uri = Uri.fromFile(file);

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    switch (iChooseImage) {
                        case 1:
                            uriImage1 = uri;
                            imageLoader.displayImage(uri.toString(), imv1, options);
                            break;
                        case 2:
                            uriImage2 = uri;
                            imageLoader.displayImage(uri.toString(), imv2, options);
                            break;
                    }
                } catch (Exception e) {

                }
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Uri uri = getFile();
            if (uri != null) {
                try {
                    switch (iChooseImage) {
                        case 1:
                            uriImage1 = uri;
                            imageLoader.displayImage(uri.toString(), imv1, options);
                            break;
                        case 2:
                            uriImage2 = uri;
                            imageLoader.displayImage(uri.toString(), imv2, options);
                            break;
                    }
                } catch (Exception e) {

                }
            }
        }
    }
    ///// choose image
}
