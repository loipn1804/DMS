package com.dms.volleycontroller.callback;

import android.content.Context;

import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;

import org.json.JSONObject;

/**
 * Created by USER on 2/29/2016.
 */
public class DiaryAllCallback {

    public BackgroundThreadExecutor backgroundThreadExecutor;
    //region VARIABLE
    public Context context;
    //endregion
    public String BASE_URL = StaticFunction.BASE_URL + "api/diary/";

    public int TIME_OUT = 10000;

    public interface GetAllDiaryCallback {
        void onSuccess(boolean success, String result, int current_page, int total_page);

        void onError(String error);
    }
}
