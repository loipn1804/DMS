package com.dms.volleycontroller;

import android.content.Context;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dms.R;
import com.dms.daocontroller.DiaryAllController;
import com.dms.daocontroller.DiaryVisitorController;
import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;
import com.dms.volleycontroller.callback.DiaryAllCallback;
import com.dms.volleycontroller.callback.DiaryVisitorCallback;
import com.dms.volleycontroller.customrequest.DiaryVisitorCreateRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import greendao.DiaryAll;
import greendao.DiaryVisitor;

/**
 * Created by USER on 2/29/2016.
 */
public class DiaryVisitorVolley extends DiaryVisitorCallback {

    public DiaryVisitorVolley(Context context) {
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }

    public void getAll(final DiaryVisitorCallback.GetAllCallback callback, final int page, final String key_search) {
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
                                    DiaryVisitorController.clearAll(context);
                                }
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    long id = object.getLong("id");
                                    long diary_type_id = object.getLong("diary_type_id");
                                    int is_staff = object.getInt("is_staff");
                                    long user_id = object.getLong("user_id");
                                    String name = object.getString("name");
                                    String content = object.getString("content");
                                    String image_1 = object.getString("image_1");
                                    String image_2 = object.getString("image_2");
                                    String enter_time = object.getString("enter_time");
                                    String exit_time = object.getString("exit_time");
                                    int is_exited = object.getInt("is_exited");
                                    long created_by = object.getLong("created_by");

                                    DiaryVisitor diaryVisitor = new DiaryVisitor(id, diary_type_id, is_staff, user_id, name, content, image_1, image_2, enter_time, exit_time, is_exited, created_by);
                                    DiaryVisitorController.insertOrUpdate(context, diaryVisitor);
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
                params.put("diary_type_id", "1");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void createDiaryVisitor(final DiaryVisitorCallback.CreateDiaryVisitorCallback callback, final DiaryVisitor diaryVisitor, final Uri uri1, final Uri uri2) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "create";
        DiaryVisitorCreateRequest diaryVisitorCreateRequest = new DiaryVisitorCreateRequest(context, url, diaryVisitor, uri1, uri2,
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
                }
        );
        diaryVisitorCreateRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT * 10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(diaryVisitorCreateRequest);
    }

    public void visitorExit(final DiaryVisitorCallback.VisitorExitCallback callback, final String diary_id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "exit";
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
                                String exit_time = data.getString("exit_time");

                                callback.onSuccess(success, message, exit_time);
                            } else {
                                callback.onSuccess(success, message, "");
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
                params.put("diary_id", diary_id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }
}
