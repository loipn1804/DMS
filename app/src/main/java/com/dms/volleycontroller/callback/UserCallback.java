package com.dms.volleycontroller.callback;

import android.content.Context;

import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;

import greendao.User;

/**
 * Created by USER on 2/16/2016.
 */
public class UserCallback {

    public BackgroundThreadExecutor backgroundThreadExecutor;
    //region VARIABLE
    public Context context;
    //endregion
    public String BASE_URL = StaticFunction.BASE_URL + "api/user/";

    public int TIME_OUT = 10000;

    public interface LoginCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface LoginVisitorStaffCallback {
        void onSuccess(boolean success, String result, User user);

        void onError(String error);
    }

    public interface ChangeAvatarCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }

    public interface ChangePasswordCallback {
        void onSuccess(boolean success, String result);

        void onError(String error);
    }
}
