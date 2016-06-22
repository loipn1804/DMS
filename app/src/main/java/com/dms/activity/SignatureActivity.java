package com.dms.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import com.dms.view.SignatureView;

/**
 * Created by Huynh Binh PC on 2/26/2016.
 */
public class SignatureActivity extends Activity {

    int DisplayWidth = 0;
    int DisplayHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayWidth = display.getWidth();
        DisplayHeight = display.getHeight();

        SignatureView signatureView = new SignatureView(SignatureActivity.this);
        signatureView.init(DisplayWidth, DisplayHeight);

        setContentView(signatureView);
    }
}
