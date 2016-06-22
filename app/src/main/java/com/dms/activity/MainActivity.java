package com.dms.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.daocontroller.UserController;
import com.dms.fragment.AboutFragment;
import com.dms.fragment.CaseFragment;
import com.dms.fragment.DashboardFragment;
import com.dms.fragment.DiaryFragment;
import com.dms.fragment.NotificationFragment;
import com.dms.fragment.WorkerFragment;
import com.dms.interfaces.MenuCallback;
import com.dms.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import greendao.User;

public class MainActivity extends BaseActivity implements View.OnClickListener, MenuCallback {

    private int MODE_DASHBOARD = 1;
    private int MODE_CASE = 2;
    private int MODE_NOTIFICATION = 3;
    private int MODE_DIARY = 4;
    private int MODE_WORKER = 5;
    private int MODE_ABOUT = 6;

    private int CURRENT_MODE;

    private DashboardFragment dashboardFragment;
    private CaseFragment caseFragment;
    private NotificationFragment notificationFragment;
    private DiaryFragment diaryFragment;
    private WorkerFragment workerFragment;
    private AboutFragment aboutFragment;

    private DrawerLayout drawerLayout;

    private ImageView imvAvatar;
    private TextView txtNotify;
    private TextView txtUsername;
    private TextView txtDashboard;
    private TextView txtCase;
    private TextView txtNotification;
    private TextView txtDiary;
    private TextView txtWorker;
    private TextView txtAbout;
    private TextView txtSignout;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        imvAvatar = (ImageView) findViewById(R.id.imvAvatar);
        txtNotify = (TextView) findViewById(R.id.txtNotify);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtDashboard = (TextView) findViewById(R.id.txtDashboard);
        txtCase = (TextView) findViewById(R.id.txtCase);
        txtNotification = (TextView) findViewById(R.id.txtNotification);
        txtDiary = (TextView) findViewById(R.id.txtDiary);
        txtWorker = (TextView) findViewById(R.id.txtWorker);
        txtAbout = (TextView) findViewById(R.id.txtAbout);
        txtSignout = (TextView) findViewById(R.id.txtSignout);

        imvAvatar.setOnClickListener(this);
        txtDashboard.setOnClickListener(this);
        txtCase.setOnClickListener(this);
        txtNotification.setOnClickListener(this);
        txtDiary.setOnClickListener(this);
        txtWorker.setOnClickListener(this);
        txtAbout.setOnClickListener(this);
        txtSignout.setOnClickListener(this);
    }

    private void initData() {
        SharedPreferences preferences = getSharedPreferences("menu", MODE_PRIVATE);
        CURRENT_MODE = preferences.getInt("mode", 1);
        setModeContentView(CURRENT_MODE);

        User user = UserController.getCurrentUser(this);
        txtUsername.setText(user.getFullname());
        imageLoader.displayImage(StaticFunction.BASE_URL + user.getAvatar(), imvAvatar, options);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvAvatar:
                Intent intentAvatar = new Intent(this, ProfileActivity.class);
                startActivity(intentAvatar);
                break;
            case R.id.txtDashboard:
                if (MODE_DASHBOARD != CURRENT_MODE) {
                    setModeContentView(MODE_DASHBOARD);
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.txtCase:
                if (MODE_CASE != CURRENT_MODE) {
                    setModeContentView(MODE_CASE);
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.txtNotification:
                if (MODE_NOTIFICATION != CURRENT_MODE) {
                    setModeContentView(MODE_NOTIFICATION);
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.txtDiary:
                if (MODE_DIARY != CURRENT_MODE) {
                    setModeContentView(MODE_DIARY);
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.txtWorker:
                if (MODE_WORKER != CURRENT_MODE) {
                    setModeContentView(MODE_WORKER);
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.txtAbout:
                if (MODE_ABOUT != CURRENT_MODE) {
                    setModeContentView(MODE_ABOUT);
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.txtSignout:
                showPopupConfirmtLogout("Do you wish to log out?");
                break;
        }
    }

    private void setModeContentView(int mode) {
        SharedPreferences preferences = getSharedPreferences("menu", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("mode", mode);
        editor.commit();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (CURRENT_MODE) {
            case 1:
                if (dashboardFragment != null) {
                    fragmentTransaction.remove(dashboardFragment);
//                    dashboardFragment = null;
                }
                break;
            case 2:
                if (caseFragment != null) {
                    fragmentTransaction.remove(caseFragment);
//                    caseFragment = null;
                }
                break;
            case 3:
                if (notificationFragment != null) {
                    fragmentTransaction.remove(notificationFragment);
//                    notificationFragment = null;
                }
                break;
            case 4:
                if (diaryFragment != null) {
                    fragmentTransaction.remove(diaryFragment);
//                    diaryFragment = null;
                }
                break;
            case 5:
                if (workerFragment != null) {
                    fragmentTransaction.remove(workerFragment);
//                    workerFragment = null;
                }
                break;
            case 6:
                if (aboutFragment != null) {
                    fragmentTransaction.remove(aboutFragment);
                }
                break;
        }

        CURRENT_MODE = mode;

//        fragmentTransaction.commit();

        setBackgroundMenu();

        switch (mode) {
            case 1:
                if (dashboardFragment == null) {
                    dashboardFragment = new DashboardFragment();
                    dashboardFragment.setMenuCallback(this);
                }
                fragmentTransaction.add(R.id.lnlContent, dashboardFragment, "dashboard");
                txtDashboard.setTextColor(getResources().getColor(R.color.main_color));
                txtDashboard.setBackgroundResource(R.color.gray_bg_menu);
                break;
            case 2:
                if (caseFragment == null) {
                    caseFragment = new CaseFragment();
                    caseFragment.setMenuCallback(this);
                }
                fragmentTransaction.add(R.id.lnlContent, caseFragment, "case");
                txtCase.setTextColor(getResources().getColor(R.color.main_color));
                txtCase.setBackgroundResource(R.color.gray_bg_menu);
                break;
            case 3:
                if (notificationFragment == null) {
                    notificationFragment = new NotificationFragment();
                    notificationFragment.setMenuCallback(this);
                }
                fragmentTransaction.add(R.id.lnlContent, notificationFragment, "notification");
                txtNotification.setTextColor(getResources().getColor(R.color.main_color));
                txtNotification.setBackgroundResource(R.color.gray_bg_menu);
                break;
            case 4:
                if (diaryFragment == null) {
                    diaryFragment = new DiaryFragment();
                    diaryFragment.setMenuCallback(this);
                }
                fragmentTransaction.add(R.id.lnlContent, diaryFragment, "diary");
                txtDiary.setTextColor(getResources().getColor(R.color.main_color));
                txtDiary.setBackgroundResource(R.color.gray_bg_menu);
                break;
            case 5:
                if (workerFragment == null) {
                    workerFragment = new WorkerFragment();
                    workerFragment.setMenuCallback(this);
                }
                fragmentTransaction.add(R.id.lnlContent, workerFragment, "worker");
                txtWorker.setTextColor(getResources().getColor(R.color.main_color));
                txtWorker.setBackgroundResource(R.color.gray_bg_menu);
                break;
            case 6:
                if (aboutFragment == null) {
                    aboutFragment = new AboutFragment();
                    aboutFragment.setMenuCallback(this);
                }
                fragmentTransaction.add(R.id.lnlContent, aboutFragment, "about");
                txtAbout.setTextColor(getResources().getColor(R.color.main_color));
                txtAbout.setBackgroundResource(R.color.gray_bg_menu);
                break;
        }

        fragmentTransaction.commit();
    }

    private void setBackgroundMenu() {
        txtDashboard.setTextColor(getResources().getColor(R.color.txt_black_33));
        txtDashboard.setBackgroundResource(R.drawable.btn_white);
        txtCase.setTextColor(getResources().getColor(R.color.txt_black_33));
        txtCase.setBackgroundResource(R.drawable.btn_white);
        txtNotification.setTextColor(getResources().getColor(R.color.txt_black_33));
        txtNotification.setBackgroundResource(R.drawable.btn_white);
        txtDiary.setTextColor(getResources().getColor(R.color.txt_black_33));
        txtDiary.setBackgroundResource(R.drawable.btn_white);
        txtWorker.setTextColor(getResources().getColor(R.color.txt_black_33));
        txtWorker.setBackgroundResource(R.drawable.btn_white);
        txtAbout.setTextColor(getResources().getColor(R.color.txt_black_33));
        txtAbout.setBackgroundResource(R.drawable.btn_white);
    }

    @Override
    public void OpenMenu() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            drawerLayout.openDrawer(Gravity.LEFT);
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
                UserController.clearAll(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);
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
    protected void onResume() {
        super.onResume();
        if (!UserController.isLogin(this)) {
            Intent intent = new Intent(MainActivity.this, SigninActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
