package com.dms.volleycontroller.callback;

import android.content.Context;

import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;

/**
 * Created by USER on 2/29/2016.
 */
public class DiaryOtherCallback {

    public BackgroundThreadExecutor backgroundThreadExecutor;
    //region VARIABLE
    public Context context;
    //endregion
    public String BASE_URL = StaticFunction.BASE_URL + "api/diary-other/";

    public int TIME_OUT = 10000;

    public interface GetAllCallback {
        void onSuccess(boolean success, String result, int current_page, int total_page);

        void onError(String error);
    }

    public interface CreateDiaryOtherCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }
}
