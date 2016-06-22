package com.dms.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.activity.BaseActivity;
import com.dms.activity.CaseCreateActivity;
import com.dms.adapter.CaseAdapter;
import com.dms.daocontroller.CaseController;
import com.dms.interfaces.MenuCallback;
import com.dms.staticfunction.BroadcastValue;
import com.dms.view.ScrollInterfacedListView;
import com.dms.volleycontroller.CaseVolley;
import com.dms.volleycontroller.callback.CaseCallback;

import java.util.ArrayList;
import java.util.List;

import greendao.CaseObj;

/**
 * Created by USER on 1/14/2016.
 */
public class CaseFragment extends Fragment implements View.OnClickListener, CaseAdapter.ScrollCallback {

    private int FILTER_ALL = 0;
    private int FILTER_LIVE = 1;
    private int FILTER_PROGRESS = 2;
    private int FILTER_APPROVAL = 3;
    private int FILTER_CLOSED = 4;
    private int CURRENT_MODE;

    private MenuCallback menuCallback;
    private RelativeLayout rltMenu;
    private ScrollInterfacedListView lvData;
    private ImageView imvAdd;
    private RelativeLayout rltFilter;
    private RelativeLayout rltNotify;

    private List<CaseObj> listCase;
    private CaseAdapter caseAdapter;

    private boolean isLoading;
    private boolean isPull;

    private int page;
    private int last_page;

    public void setMenuCallback(MenuCallback menuCallback) {
        this.menuCallback = menuCallback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_case, container, false);

        registerReceiver();

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        rltMenu = (RelativeLayout) view.findViewById(R.id.rltMenu);
        lvData = (ScrollInterfacedListView) view.findViewById(R.id.lvData);
        imvAdd = (ImageView) view.findViewById(R.id.imvAdd);
        rltFilter = (RelativeLayout) view.findViewById(R.id.rltFilter);
        rltNotify = (RelativeLayout) view.findViewById(R.id.rltNotify);

        rltMenu.setOnClickListener(this);
        imvAdd.setOnClickListener(this);
        rltFilter.setOnClickListener(this);
        rltNotify.setOnClickListener(this);

        lvData.setOnDetectScrollListener(new ScrollInterfacedListView.OnDetectScrollListener() {
            @Override
            public void onUpScrolling() {
//                Log.e("scroll", "up");
                if (imvAdd.getVisibility() == View.INVISIBLE) {
                    imvAdd.setVisibility(View.VISIBLE);
                    imvAdd.setClickable(true);
                    Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_add_btn_in);
                    imvAdd.setAnimation(a);
                    a.start();
                }
            }

            @Override
            public void onDownScrolling() {
//                Log.e("scroll", "down");
                if (imvAdd.getVisibility() == View.VISIBLE) {
                    imvAdd.setVisibility(View.INVISIBLE);
                    imvAdd.setClickable(false);
                    Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_add_btn_out);
                    imvAdd.setAnimation(a);
                    a.start();
                }
            }
        });
    }

    private void initData() {
        isLoading = false;
        isPull = false;
        page = 1;
        last_page = 0;

        SharedPreferences preferences = getActivity().getSharedPreferences("filter", Context.MODE_PRIVATE);
        CURRENT_MODE = preferences.getInt("case", 0);
        filterCase(CURRENT_MODE);

        listCase = new ArrayList<>();

        caseAdapter = new CaseAdapter(getActivity(), listCase, this);
        lvData.setAdapter(caseAdapter);

        lvData.setOnScrollListener(new EndScrollListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltMenu:
                menuCallback.OpenMenu();
                break;
            case R.id.imvAdd:
                Intent intent = new Intent(getActivity(), CaseCreateActivity.class);
                startActivity(intent);
                break;
            case R.id.rltFilter:
                showPopupFilter();
                break;
            case R.id.rltNotify:

                break;
        }
    }

    public void showPopupFilter() {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_filter_case);

        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        LinearLayout lnlBottom = (LinearLayout) dialog.findViewById(R.id.lnlBottom);
        LinearLayout lnlAll = (LinearLayout) dialog.findViewById(R.id.lnlAll);
        LinearLayout lnlLive = (LinearLayout) dialog.findViewById(R.id.lnlLive);
        LinearLayout lnlProgress = (LinearLayout) dialog.findViewById(R.id.lnlProgress);
        LinearLayout lnlApproval = (LinearLayout) dialog.findViewById(R.id.lnlApproval);
        LinearLayout lnlClosed = (LinearLayout) dialog.findViewById(R.id.lnlClosed);
        TextView txtAll = (TextView) dialog.findViewById(R.id.txtAll);
        TextView txtLive = (TextView) dialog.findViewById(R.id.txtLive);
        TextView txtProgress = (TextView) dialog.findViewById(R.id.txtProgress);
        TextView txtApproval = (TextView) dialog.findViewById(R.id.txtApproval);
        TextView txtClosed = (TextView) dialog.findViewById(R.id.txtClosed);

        lnlAll.setBackgroundResource(R.drawable.btn_white);
        txtAll.setTextColor(getActivity().getResources().getColor(R.color.txt_black_33));
        lnlLive.setBackgroundResource(R.drawable.btn_white);
        txtAll.setTextColor(getActivity().getResources().getColor(R.color.txt_black_33));
        lnlProgress.setBackgroundResource(R.drawable.btn_white);
        txtAll.setTextColor(getActivity().getResources().getColor(R.color.txt_black_33));
        lnlApproval.setBackgroundResource(R.drawable.btn_white);
        txtAll.setTextColor(getActivity().getResources().getColor(R.color.txt_black_33));
        lnlClosed.setBackgroundResource(R.drawable.btn_white);
        txtAll.setTextColor(getActivity().getResources().getColor(R.color.txt_black_33));

        switch (CURRENT_MODE) {
            case 0:
                lnlAll.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_bg_menu));
                txtAll.setTextColor(getActivity().getResources().getColor(R.color.main_color));
                break;
            case 1:
                lnlLive.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_bg_menu));
                txtLive.setTextColor(getActivity().getResources().getColor(R.color.main_color));
                break;
            case 2:
                lnlProgress.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_bg_menu));
                txtProgress.setTextColor(getActivity().getResources().getColor(R.color.main_color));
                break;
            case 3:
                lnlApproval.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_bg_menu));
                txtApproval.setTextColor(getActivity().getResources().getColor(R.color.main_color));
                break;
            case 4:
                lnlClosed.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_bg_menu));
                txtClosed.setTextColor(getActivity().getResources().getColor(R.color.main_color));
                break;
        }

        lnlAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FILTER_ALL != CURRENT_MODE) {
                    page = 1;
                    filterCase(FILTER_ALL);
                    dialog.dismiss();
                }
            }
        });

        lnlLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FILTER_LIVE != CURRENT_MODE) {
                    page = 1;
                    filterCase(FILTER_LIVE);
                    dialog.dismiss();
                }
            }
        });

        lnlProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FILTER_PROGRESS != CURRENT_MODE) {
                    page = 1;
                    filterCase(FILTER_PROGRESS);
                    dialog.dismiss();
                }
            }
        });

        lnlApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FILTER_APPROVAL != CURRENT_MODE) {
                    page = 1;
                    filterCase(FILTER_APPROVAL);
                    dialog.dismiss();
                }
            }
        });

        lnlClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FILTER_CLOSED != CURRENT_MODE) {
                    page = 1;
                    filterCase(FILTER_CLOSED);
                    dialog.dismiss();
                }
            }
        });

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lnlBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();
    }

    private void filterCase(int mode) {
        SharedPreferences preferences = getActivity().getSharedPreferences("filter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("case", mode);
        editor.commit();

        CURRENT_MODE = mode;

        switch (mode) {
            case 0:
//                ((BaseActivity) getActivity()).showToastInfo("All");
                getAllCase(page, 0);
                break;
            case 1:
//                ((BaseActivity) getActivity()).showToastInfo("Live");
                getAllCase(page, 1);
                break;
            case 2:
//                ((BaseActivity) getActivity()).showToastInfo("Progress");
                getAllCase(page, 2);
                break;
            case 3:
//                ((BaseActivity) getActivity()).showToastInfo("Approval");
                getAllCase(page, 3);
                break;
            case 4:
//                ((BaseActivity) getActivity()).showToastInfo("Closed");
                getAllCase(page, 4);
                break;
        }
    }

    @Override
    public void scrollUp(int scrollHeight) {
        lvData.smoothScrollBy(scrollHeight, 500);
    }

    private void getAllCase(int page_input, long case_status_id) {
        if (page_input == 1) {
            ((BaseActivity) getActivity()).showProgressDialog(true);
        }
        CaseVolley caseVolley = new CaseVolley(getActivity());
        caseVolley.getAll(new CaseCallback.GetAllCallback() {
            @Override
            public void onSuccess(boolean success, String result, int current_page, int total_page) {
                page = current_page;
                last_page = total_page;

                listCase = CaseController.getAll(getActivity());
                caseAdapter.setListData(listCase);
                ((BaseActivity) getActivity()).hideProgressDialog();
                isLoading = false;
            }

            @Override
            public void onError(String error) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                isLoading = false;
            }
        }, page_input, case_status_id);
    }

    private class EndScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount >= visibleItemCount + 2) {
                if (firstVisibleItem + 3 >= totalItemCount - visibleItemCount + 2) {
                    if (!isLoading && !isPull) {
                        isLoading = true;
                        if (page < last_page) {
                            page++;
                            filterCase(CURRENT_MODE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

    private void registerReceiver() {
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BroadcastValue.CASE_NOTIFY_LIST);
            intentFilter.addAction(BroadcastValue.CASE_REFRESH_LIST);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }
    }

    private void unRegisterReceiver() {
        if (activityReceiver != null) {
            getActivity().unregisterReceiver(activityReceiver);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(BroadcastValue.CASE_NOTIFY_LIST)) {
                listCase = CaseController.getAll(getActivity());
                caseAdapter.setListData(listCase);
            } else if (intent.getAction().equalsIgnoreCase(BroadcastValue.CASE_REFRESH_LIST)) {
                page = 1;
                CURRENT_MODE = 0;
                getAllCase(page, 0);
            }
        }
    };
}
