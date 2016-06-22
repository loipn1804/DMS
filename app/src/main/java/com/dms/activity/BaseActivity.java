package com.dms.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.dms.R;
import com.dms.view.SimpleToast;

/**
 * Created by USER on 1/13/2016.
 */
public class BaseActivity extends AppCompatActivity {

    private Dialog progress_dialog = null;

    public void showProgressDialog(boolean cancelable) {
        if (progress_dialog == null) {
            progress_dialog = new Dialog(BaseActivity.this);
            progress_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progress_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progress_dialog.setContentView(R.layout.progress_dialog);
        }

        if (!progress_dialog.isShowing()) {
            progress_dialog.setCanceledOnTouchOutside(cancelable);
            progress_dialog.setCancelable(cancelable);
            progress_dialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progress_dialog != null && progress_dialog.isShowing()) {
            progress_dialog.dismiss();
        }
    }

    public void showToastError(String message) {
        SimpleToast.error(BaseActivity.this, message);
    }

    public void showToastInfo(String message) {
        SimpleToast.info(BaseActivity.this, message);
    }

    public void showToastOk(String message) {
        SimpleToast.ok(BaseActivity.this, message);
    }

    public void showPopupPrompt(String message) {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_prompt);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        txtMessage.setText(message);

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
