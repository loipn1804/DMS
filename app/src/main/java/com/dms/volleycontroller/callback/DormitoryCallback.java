package com.dms.volleycontroller.callback;

import android.content.Context;

import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;

/**
 * Created by USER on 2/22/2016.
 */
public class DormitoryCallback {

    public BackgroundThreadExecutor backgroundThreadExecutor;
    //region VARIABLE
    public Context context;
    //endregion
    public String BASE_URL = StaticFunction.BASE_URL + "api/dormitory/";

    public int TIME_OUT = 10000;

    public interface GetDormitoryCategoryCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }
}
