package com.dms.volleycontroller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dms.R;
import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;
import com.dms.volleycontroller.callback.DashboardCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 2/18/2016.
 */
public class DashboardVolley extends DashboardCallback {

    public DashboardVolley(Context context) {
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }

    public void getAll(final DashboardCallback.GetAllCallback callback) {
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
                                JSONObject data = root.getJSONObject("data");

                                callback.onSuccess(success, message, data);
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
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }
}