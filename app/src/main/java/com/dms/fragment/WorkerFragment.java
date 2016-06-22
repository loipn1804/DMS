package com.dms.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dms.R;
import com.dms.activity.BaseActivity;
import com.dms.activity.WorkerCreateActivity;
import com.dms.adapter.CaseAdapter;
import com.dms.adapter.WorkerAdapter;
import com.dms.daocontroller.CaseController;
import com.dms.daocontroller.WorkerController;
import com.dms.interfaces.MenuCallback;
import com.dms.staticfunction.BroadcastValue;
import com.dms.view.ScrollInterfacedListView;
import com.dms.volleycontroller.CaseVolley;
import com.dms.volleycontroller.WorkerVolley;
import com.dms.volleycontroller.callback.CaseCallback;
import com.dms.volleycontroller.callback.WorkerCallback;

import java.util.ArrayList;
import java.util.List;

import greendao.Worker;

/**
 * Created by USER on 1/21/2016.
 */
public class WorkerFragment extends Fragment implements View.OnClickListener {

    private MenuCallback menuCallback;
    private RelativeLayout rltMenu;
    private ScrollInterfacedListView lvData;
    private ImageView imvAdd;
    private EditText edtSearch;

    private WorkerAdapter workerAdapter;
    private List<Worker> listWorker;

    private boolean isLoading;
    private boolean isPull;

    private int page;
    private int last_page;

    private boolean isLoadNationality;

    public void setMenuCallback(MenuCallback menuCallback) {
        this.menuCallback = menuCallback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker, container, false);

        registerReceiver();

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        rltMenu = (RelativeLayout) view.findViewById(R.id.rltMenu);
        lvData = (ScrollInterfacedListView) view.findViewById(R.id.lvData);
        imvAdd = (ImageView) view.findViewById(R.id.imvAdd);
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);

        rltMenu.setOnClickListener(this);
        imvAdd.setOnClickListener(this);

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

        edtSearch.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        page = 1;
                        last_page = 0;
                        getAllWorker(page, edtSearch.getText().toString().trim());
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
        isLoading = false;
        isPull = false;
        page = 1;
        last_page = 0;

        isLoadNationality = false;

        listWorker = new ArrayList<>();
        workerAdapter = new WorkerAdapter(getActivity(), listWorker);
        lvData.setAdapter(workerAdapter);

        lvData.setOnScrollListener(new EndScrollListener());

        getAllWorker(page, edtSearch.getText().toString().trim());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltMenu:
                menuCallback.OpenMenu();
                break;
            case R.id.imvAdd:
                Intent intent = new Intent(getActivity(), WorkerCreateActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getAllWorker(int page_input, String key_search) {
        if (page_input == 1) {
            ((BaseActivity) getActivity()).showProgressDialog(true);
        }
        WorkerVolley workerVolley = new WorkerVolley(getActivity());
        workerVolley.getAll(new WorkerCallback.GetAllCallback() {
            @Override
            public void onSuccess(boolean success, String result, int current_page, int total_page) {
                page = current_page;
                last_page = total_page;

                listWorker.clear();
                listWorker.addAll(WorkerController.getAll(getActivity()));
                workerAdapter.setListData(listWorker);
                ((BaseActivity) getActivity()).hideProgressDialog();
                isLoading = false;

                if (!isLoadNationality) {
                    getNationality();
                }
            }

            @Override
            public void onError(String error) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                isLoading = false;
            }
        }, page_input, key_search);
    }

    private void getNationality() {
        WorkerVolley workerVolley = new WorkerVolley(getActivity());
        workerVolley.getNationality(new WorkerCallback.GetNationalityCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                isLoadNationality = true;
            }

            @Override
            public void onError(String error) {

            }
        });
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
                            getAllWorker(page, edtSearch.getText().toString().trim());
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
            intentFilter.addAction(BroadcastValue.WORKER_NOTIFY_LIST);
            intentFilter.addAction(BroadcastValue.WORKER_REFRESH_LIST);
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
            if (intent.getAction().equalsIgnoreCase(BroadcastValue.WORKER_NOTIFY_LIST)) {
                listWorker = WorkerController.getAll(getActivity());
                workerAdapter.setListData(listWorker);
            } else if (intent.getAction().equalsIgnoreCase(BroadcastValue.WORKER_REFRESH_LIST)) {
                page = 1;
                last_page = 0;
                getAllWorker(page, edtSearch.getText().toString().trim());
            }
        }
    };
}
