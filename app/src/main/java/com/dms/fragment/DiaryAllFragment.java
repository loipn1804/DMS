package com.dms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.activity.BaseActivity;
import com.dms.adapter.DiaryAllAdapter;
import com.dms.adapter.NotificationAdapter;
import com.dms.daocontroller.CaseController;
import com.dms.daocontroller.DiaryAllController;
import com.dms.interfaces.MenuCallback;
import com.dms.volleycontroller.CaseVolley;
import com.dms.volleycontroller.DiaryAllVolley;
import com.dms.volleycontroller.callback.CaseCallback;
import com.dms.volleycontroller.callback.DiaryAllCallback;

import java.util.ArrayList;
import java.util.List;

import greendao.DiaryAll;

/**
 * Created by USER on 1/22/2016.
 */
public class DiaryAllFragment extends Fragment implements View.OnClickListener {

    private EditText edtSearch;

    private ListView lvData;
    private DiaryAllAdapter diaryAllAdapter;
    private List<DiaryAll> listDiaryAll;

    private boolean isLoading;
    private boolean isPull;

    private int page;
    private int last_page;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_all, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        lvData = (ListView) view.findViewById(R.id.lvData);
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        page = 1;
                        last_page = 0;
                        getAllDiary(page, edtSearch.getText().toString().trim());
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

        listDiaryAll = new ArrayList<>();
        diaryAllAdapter = new DiaryAllAdapter(getActivity(), listDiaryAll);
        lvData.setAdapter(diaryAllAdapter);

        lvData.setOnScrollListener(new EndScrollListener());

        getAllDiary(page, edtSearch.getText().toString().trim());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void getAllDiary(int page_input, String key_search) {
        if (page_input == 1) {
            ((BaseActivity) getActivity()).showProgressDialog(true);
        }
        DiaryAllVolley diaryAllVolley = new DiaryAllVolley(getActivity());
        diaryAllVolley.getAll(new DiaryAllCallback.GetAllDiaryCallback() {
            @Override
            public void onSuccess(boolean success, String result, int current_page, int total_page) {
                page = current_page;
                last_page = total_page;

                listDiaryAll = DiaryAllController.getAll(getActivity());
                diaryAllAdapter.setListData(listDiaryAll);
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
                            getAllDiary(page, edtSearch.getText().toString().trim());
                        }
                    }
                }
            }
        }
    }
}
