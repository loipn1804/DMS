package com.dms.volleycontroller;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dms.R;
import com.dms.daocontroller.UserController;
import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;
import com.dms.volleycontroller.callback.UserCallback;
import com.dms.volleycontroller.customrequest.UserChangeAvatarRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import greendao.User;

/**
 * Created by USER on 2/16/2016.
 */
public class UserVolley extends UserCallback {

    public UserVolley(Context context) {
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }

    public void login(final LoginCallback callback, final String email, final String password) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            boolean success = root.getBoolean("success");
                            String message = root.getString("message");
                            if (success) {
                                JSONObject data = root.getJSONObject("data");
                                long id = data.getLong("id");
                                int role_id = data.getInt("role_id");
                                String avatar = data.getString("avatar");
                                String name = data.getString("name");
                                String fullname = data.getString("fullname");
                                String phone = data.getString("phone");
                                String email = data.getString("email");
                                String created_at = data.getString("created_at");
                                String updated_at = data.getString("updated_at");
                                String last_login = data.getString("last_login");
                                int is_active = data.getInt("is_active");
                                String remember_token = data.getString("remember_token");
                                if (remember_token.equalsIgnoreCase("null")) remember_token = "";

                                User user = new User(id, role_id, avatar, name, fullname, phone, email, created_at, updated_at, last_login, is_active, remember_token);
                                UserController.insert(context, user);

                                callback.onSuccess(success, message);
                            } else {
                                callback.onSuccess(success, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void loginVisitorStaff(final LoginVisitorStaffCallback callback, final String email, final String password) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            boolean success = root.getBoolean("success");
                            String message = root.getString("message");
                            if (success) {
                                JSONObject data = root.getJSONObject("data");
                                long id = data.getLong("id");
                                int role_id = data.getInt("role_id");
                                String avatar = data.getString("avatar");
                                String name = data.getString("name");
                                String fullname = data.getString("fullname");
                                String phone = data.getString("phone");
                                String email = data.getString("email");
                                String created_at = data.getString("created_at");
                                String updated_at = data.getString("updated_at");
                                String last_login = data.getString("last_login");
                                int is_active = data.getInt("is_active");
                                String remember_token = data.getString("remember_token");
                                if (remember_token.equalsIgnoreCase("null")) remember_token = "";

                                User user = new User(id, role_id, avatar, name, fullname, phone, email, created_at, updated_at, last_login, is_active, remember_token);

                                callback.onSuccess(success, message, user);
                            } else {
                                callback.onSuccess(success, message, null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void changeAvatar(final ChangeAvatarCallback callback, final User user, final Bitmap bitmap) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "change-avatar";
        UserChangeAvatarRequest userChangeAvatarRequest = new UserChangeAvatarRequest(url, user, bitmap,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            boolean success = root.getBoolean("success");
                            String message = root.getString("message");
                            if (success) {
                                JSONObject data = root.getJSONObject("data");
                                String avatar = data.getString("avatar");

                                user.setAvatar(avatar);
                                UserController.update(context, user);

                                callback.onSuccess(success, message);
                            } else {
                                callback.onSuccess(success, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                }
        );
        userChangeAvatarRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(userChangeAvatarRequest);
    }

    public void changePassword(final ChangePasswordCallback callback, final String user_id, final String old_password, final String new_password) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "change-password";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            boolean success = root.getBoolean("success");
                            String message = root.getString("message");
                            if (success) {
                                callback.onSuccess(success, message);
                            } else {
                                callback.onSuccess(success, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("old_password", old_password);
                params.put("new_password", new_password);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }
}
