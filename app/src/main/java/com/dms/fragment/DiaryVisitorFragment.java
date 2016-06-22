package com.dms.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.dms.R;
import com.dms.activity.BaseActivity;
import com.dms.activity.DiaryVisitorCreateActivity;
import com.dms.activity.WorkerCreateActivity;
import com.dms.adapter.DiaryVisitorAdapter;
import com.dms.adapter.WorkerAdapter;
import com.dms.daocontroller.DiaryAllController;
import com.dms.daocontroller.DiaryVisitorController;
import com.dms.daocontroller.WorkerController;
import com.dms.interfaces.MenuCallback;
import com.dms.staticfunction.BroadcastValue;
import com.dms.view.ScrollInterfacedListView;
import com.dms.volleycontroller.DiaryAllVolley;
import com.dms.volleycontroller.DiaryVisitorVolley;
import com.dms.volleycontroller.callback.DiaryAllCallback;
import com.dms.volleycontroller.callback.DiaryVisitorCallback;

import java.util.ArrayList;
import java.util.List;

import greendao.DiaryVisitor;
import greendao.Worker;

/**
 * Created by USER on 2/29/2016.
 */
public class DiaryVisitorFragment extends Fragment implements View.OnClickListener {

    private MenuCallback menuCallback;
    private ScrollInterfacedListView lvData;
    private ImageView imvAdd;
    private EditText edtSearch;

    private DiaryVisitorAdapter diaryVisitorAdapter;
    private List<DiaryVisitor> listDiaryVisitor;

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
        View view = inflater.inflate(R.layout.fragment_diary_visitor, container, false);

        registerReceiver();

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        lvData = (ScrollInterfacedListView) view.findViewById(R.id.lvData);
        imvAdd = (ImageView) view.findViewById(R.id.imvAdd);
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);

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

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        page = 1;
                        last_page = 0;
                        getAll(page, edtSearch.getText().toString().trim());
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

        listDiaryVisitor = new ArrayList<>();
        diaryVisitorAdapter = new DiaryVisitorAdapter(getActivity(), listDiaryVisitor);
        lvData.setAdapter(diaryVisitorAdapter);

        lvData.setOnScrollListener(new EndScrollListener());

        getAll(page, edtSearch.getText().toString().trim());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvAdd:
                Intent intent = new Intent(getActivity(), DiaryVisitorCreateActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getAll(int page_input, String key_search) {
        if (page_input == 1) {
            ((BaseActivity) getActivity()).showProgressDialog(true);
        }
        DiaryVisitorVolley diaryVisitorVolley = new DiaryVisitorVolley(getActivity());
        diaryVisitorVolley.getAll(new DiaryVisitorCallback.GetAllCallback() {
            @Override
            public void onSuccess(boolean success, String result, int current_page, int total_page) {
                page = current_page;
                last_page = total_page;

                listDiaryVisitor = DiaryVisitorController.getAll(getActivity());
                diaryVisitorAdapter.setListData(listDiaryVisitor);
                ((BaseActivity) getActivity()).hideProgressDialog();
                isLoading = false;
            }

            @Override
            public void onError(String error) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                isLoading = false;
            }
        }, page_input, key_search);
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
                            getAll(page, edtSearch.getText().toString().trim());
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
            intentFilter.addAction(BroadcastValue.DIARY_VISITOR_NOTIFY_LIST);
            intentFilter.addAction(BroadcastValue.DIARY_VISITOR_REFRESH_LIST);
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
            if (intent.getAction().equalsIgnoreCase(BroadcastValue.DIARY_VISITOR_NOTIFY_LIST)) {
                listDiaryVisitor = DiaryVisitorController.getAll(getActivity());
                diaryVisitorAdapter.setListData(listDiaryVisitor);
            } else if (intent.getAction().equalsIgnoreCase(BroadcastValue.DIARY_VISITOR_REFRESH_LIST)) {
                page = 1;
                last_page = 0;
                getAll(page, edtSearch.getText().toString().trim());
            }
        }
    };
}
