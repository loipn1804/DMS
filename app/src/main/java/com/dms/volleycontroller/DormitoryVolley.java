package com.dms.volleycontroller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dms.R;
import com.dms.daocontroller.BlockController;
import com.dms.daocontroller.DormitoryCategoryController;
import com.dms.daocontroller.DormitoryController;
import com.dms.daocontroller.LevelController;
import com.dms.daocontroller.RoomController;
import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;
import com.dms.volleycontroller.callback.DormitoryCallback;
import com.dms.volleycontroller.callback.WorkerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import greendao.Block;
import greendao.Dormitory;
import greendao.DormitoryCategory;
import greendao.Level;
import greendao.Room;

/**
 * Created by USER on 2/22/2016.
 */
public class DormitoryVolley extends DormitoryCallback {

    public DormitoryVolley(Context context) {
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }

    public void getDormitoryCategory(final DormitoryCallback.GetDormitoryCategoryCallback callback) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "get-all-with-category";
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
                                DormitoryCategoryController.clearAll(context);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    long id = object.getLong("id");
                                    long dormitory_id = object.getLong("dormitory_id");
                                    long category_id = object.getLong("category_id");

                                    DormitoryCategory dormitoryCategory = new DormitoryCategory(id, dormitory_id, category_id);
                                    DormitoryCategoryController.insertOrUpdate(context, dormitoryCategory);
                                }

                                callback.onSuccess(success, message);
                            } else {
                                callback.onSuccess(success, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError(e.getMessage());
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
