package com.dms.volleycontroller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dms.R;
import com.dms.daocontroller.DiaryOtherController;
import com.dms.daocontroller.DiaryShiftController;
import com.dms.daocontroller.UserController;
import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;
import com.dms.volleycontroller.callback.DiaryOtherCallback;
import com.dms.volleycontroller.callback.DiaryShiftCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import greendao.DiaryOther;
import greendao.DiaryShift;

/**
 * Created by USER on 2/29/2016.
 */
public class DiaryOtherVolley extends DiaryOtherCallback {

    public DiaryOtherVolley(Context context) {
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }

    public void getAll(final DiaryOtherCallback.GetAllCallback callback, final int page, final String key_search, final String diary_type_id_input) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "get-all";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            boolean success = root.getBoolean("success");
                            String message = root.getString("message");
                            if (success) {
                                JSONArray data = root.getJSONArray("data");
                                if (page == 1) {
                                    DiaryOtherController.clearAll(context);
                                }
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    long id = object.getLong("id");
                                    long diary_type_id = object.getLong("diary_type_id");
                                    String content = object.getString("content");
                                    long created_by = object.getLong("created_by");
                                    String created_at = object.getString("created_at");
                                    long status = object.getLong("status");
                                    String created_by_name = object.getString("created_by_name");

                                    DiaryOther diaryOther = new DiaryOther(id, diary_type_id, content, created_by, created_at, status, created_by_name);
                                    DiaryOtherController.insertOrUpdate(context, diaryOther);
                                }

                                JSONObject page_info = root.getJSONObject("page_info");
                                int current_page = page_info.getInt("current_page");
                                int total_page = page_info.getInt("total_page");

                                callback.onSuccess(success, message, current_page, total_page);
                            } else {
                                callback.onSuccess(success, message, 1, 1);
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
                params.put("page", page + "");
                params.put("key_search", key_search);
                params.put("diary_type_id", diary_type_id_input);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void createDiaryOther(final DiaryOtherCallback.CreateDiaryOtherCallback callback, final DiaryOther diaryOther) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "create";
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
                params.put("created_by", UserController.getCurrentUser(context).getId() + "");
                params.put("content", diaryOther.getContent());
                params.put("diary_type_id", diaryOther.getDiary_type_id() + "");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }
}
