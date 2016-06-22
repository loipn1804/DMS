package com.dms.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dms.R;

/**
 * Created by USER on 1/21/2016.
 */
public class PopupUpdateCase extends PopupWindow implements View.OnClickListener {

    public interface PopupUpdateCaseCallback {
        void onChangeStatus(int position);

        void onAssign(int position);
    }

    private Activity activity;
    private LayoutInflater layoutInflater;

    private TextView txtChangeStatus;
    private TextView txtAssign;

    private PopupUpdateCaseCallback popupUpdateCaseCallback;
    private int position;

    public PopupUpdateCase(Activity activity, PopupUpdateCaseCallback popupUpdateCaseCallback) {
        super(activity);
        this.activity = activity;
        this.popupUpdateCaseCallback = popupUpdateCaseCallback;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.popup_update_case, null);
        setContentView(view);
        initView();
    }

    private void initView() {
        txtChangeStatus = (TextView) getContentView().findViewById(R.id.txtChangeStatus);
        txtAssign = (TextView) getContentView().findViewById(R.id.txtAssign);

        txtChangeStatus.setOnClickListener(this);
        txtAssign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtChangeStatus:
                popupUpdateCaseCallback.onChangeStatus(position);
                break;
            case R.id.txtAssign:
                popupUpdateCaseCallback.onAssign(position);
                break;
        }
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setAnchor(View anchor) {
        setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
//        setWidth((int) activity.getResources().getDimension(R.dimen.height_row_deal));
//        setWidth((int) activity.getResources().getDimension(R.dimen.popup_width));
//        setAnimationStyle(R.style.popup_animation);
        // setBackgroundDrawable(new BitmapDrawable());
        // setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_border_gray));
        setBackgroundDrawable(new ColorDrawable(0));
        setFocusable(true);
        setOutsideTouchable(false);

        showAsDropDown(anchor, 0, -((int) activity.getResources().getDimension(R.dimen.dm_10dp) + (int) activity.getResources().getDimension(R.dimen.dm_5dp)));

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dismiss();
//            }
//        }, 5000);
    }
}
