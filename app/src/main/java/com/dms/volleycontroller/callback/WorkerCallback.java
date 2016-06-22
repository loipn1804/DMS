package com.dms.volleycontroller.callback;

import android.content.Context;

import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by USER on 2/18/2016.
 */
public class WorkerCallback {

    public BackgroundThreadExecutor backgroundThreadExecutor;
    //region VARIABLE
    public Context context;
    //endregion
    public String BASE_URL = StaticFunction.BASE_URL + "api/worker/";

    public int TIME_OUT = 10000;

    public interface GetDormitoryInfoCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface GetAllCallback {
        void onSuccess(boolean success, String result, int current_page, int total_page);

        void onError(String error);
    }

    public interface GetNationalityCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface GetLogChangeCallback {
        void onSuccess(boolean success, String result, JSONArray jsonArray);

        void onError(String error);
    }

    public interface CreateWorkerCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface UpdateWorkerCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface ChangeImageCallback {
        void onSuccess(boolean success, String result, String image_1, String image_2, String image_3);

        void onError(String error);
    }
}
