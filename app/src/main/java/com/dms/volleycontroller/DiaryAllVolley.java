package com.dms.volleycontroller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dms.R;
import com.dms.daocontroller.DiaryAllController;
import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;
import com.dms.volleycontroller.callback.DashboardCallback;
import com.dms.volleycontroller.callback.DiaryAllCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import greendao.DiaryAll;

/**
 * Created by USER on 2/29/2016.
 */
public class DiaryAllVolley extends DiaryAllCallback {

    public DiaryAllVolley(Context context) {
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }

    public void getAll(final DiaryAllCallback.GetAllDiaryCallback callback, final int page, final String key_search) {
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
                                    DiaryAllController.clearAll(context);
                                }
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    long id = object.getLong("id");
                                    long diary_type_id = object.getLong("diary_type_id");
                                    String content = object.getString("content");
                                    String created_at = object.getString("created_at");
                                    String created_by_name = object.getString("created_by_name");

                                    DiaryAll diaryAll = new DiaryAll(id, diary_type_id, content, created_at, created_by_name);
                                    DiaryAllController.insertOrUpdate(context, diaryAll);
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
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }
}
