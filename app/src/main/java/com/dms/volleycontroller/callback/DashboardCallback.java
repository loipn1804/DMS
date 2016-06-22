package com.dms.volleycontroller.callback;

import android.content.Context;

import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;

import org.json.JSONObject;

/**
 * Created by USER on 2/18/2016.
 */
public class DashboardCallback {

    public BackgroundThreadExecutor backgroundThreadExecutor;
    //region VARIABLE
    public Context context;
    //endregion
    public String BASE_URL = StaticFunction.BASE_URL + "api/dashboard/";

    public int TIME_OUT = 10000;

    public interface GetAllCallback {
        void onSuccess(boolean success, String result, JSONObject data);

        void onError(String error);
    }
}
