package com.dms.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dms.R;
import com.dms.daocontroller.UserController;
import com.dms.staticfunction.StaticFunction;
import com.dms.volleycontroller.UserVolley;
import com.dms.volleycontroller.callback.UserCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import greendao.User;

/**
 * Created by USER on 1/25/2016.
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private RelativeLayout rltChangePass;
    private RelativeLayout rltSignout;
    private ImageView imvAvatar;
    private TextView txtUsername;
    private TextView txtEmail;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(R.drawable.avatar_default)
                .showImageOnLoading(R.drawable.avatar_default)
                .cacheInMemory(true)
                .cacheOnDisk(true).build();

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltChangePass = (RelativeLayout) findViewById(R.id.rltChangePass);
        rltSignout = (RelativeLayout) findViewById(R.id.rltSignout);
        imvAvatar = (ImageView) findViewById(R.id.imvAvatar);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtEmail = (TextView) findViewById(R.id.txtEmail);

        rltBack.setOnClickListener(this);
        rltChangePass.setOnClickListener(this);
        rltSignout.setOnClickListener(this);
        imvAvatar.setOnClickListener(this);
    }

    private void initData() {
        User user = UserController.getCurrentUser(this);
        txtUsername.setText(user.getFullname());
        txtEmail.setText(user.getEmail());
        imageLoader.displayImage(StaticFunction.BASE_URL + user.getAvatar(), imvAvatar, options);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.imvAvatar:
                Crop.pickImage(this);
                break;
            case R.id.rltChangePass:
                Intent intentChangePass = new Intent(this, ChangePassActivity.class);
                startActivity(intentChangePass);
                break;
            case R.id.rltSignout:
                showPopupConfirmtLogout("Do you wish to log out?");
                break;
        }
    }

    public void showPopupConfirmtLogout(String message) {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        txtMessage.setText(message);

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserController.clearAll(ProfileActivity.this);
                finish();
                dialog.dismiss();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = Crop.getOutput(result);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                Bitmap b = Bitmap.createScaledBitmap(bitmap, 200, 200, false);

                changeAvatar(b);

            } catch (Exception e) {

            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void changeAvatar(Bitmap bitmap) {
        showProgressDialog(true);
        UserVolley userVolley = new UserVolley(this);
        userVolley.changeAvatar(new UserCallback.ChangeAvatarCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    User user = UserController.getCurrentUser(ProfileActivity.this);
                    imageLoader.displayImage(StaticFunction.BASE_URL + user.getAvatar(), imvAvatar, options);
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
        }, UserController.getCurrentUser(this), bitmap);
    }
}
