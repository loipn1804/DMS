package com.dms.volleycontroller.callback;

import android.content.Context;

import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;

import org.json.JSONObject;

/**
 * Created by USER on 2/18/2016.
 */
public class CaseCallback {

    public BackgroundThreadExecutor backgroundThreadExecutor;
    //region VARIABLE
    public Context context;
    //endregion
    public String BASE_URL = StaticFunction.BASE_URL + "api/case/";

    public int TIME_OUT = 10000;

    public interface GetAllCallback {
        void onSuccess(boolean success, String result, int current_page, int total_page);

        void onError(String error);
    }

    public interface GetCaseDetailCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface CreateCaseCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface GetListCategoryCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface GetListStatusCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface GetListStaffCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface RemoveImageCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface AddImageCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface UpdateCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface ChangeStatusCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface AssignCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface UpdateContentCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }
}