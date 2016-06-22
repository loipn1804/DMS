package com.dms.volleycontroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dms.R;
import com.dms.daocontroller.BlockController;
import com.dms.daocontroller.CategoryController;
import com.dms.daocontroller.DiaryTypeController;
import com.dms.daocontroller.DormitoryController;
import com.dms.daocontroller.LevelController;
import com.dms.daocontroller.NationalityController;
import com.dms.daocontroller.RoomController;
import com.dms.daocontroller.StatusController;
import com.dms.daocontroller.SubCategoryController;
import com.dms.daocontroller.WorkerController;
import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;
import com.dms.volleycontroller.callback.CaseCallback;
import com.dms.volleycontroller.callback.WorkerCallback;
import com.dms.volleycontroller.customrequest.WorkerChangeImageRequest;
import com.dms.volleycontroller.customrequest.WorkerCreateRequest;
import com.dms.volleycontroller.customrequest.WorkerUpdateRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import greendao.Block;
import greendao.Category;
import greendao.DiaryType;
import greendao.Dormitory;
import greendao.Level;
import greendao.Nationality;
import greendao.Room;
import greendao.Status;
import greendao.SubCategory;
import greendao.Worker;

/**
 * Created by USER on 2/18/2016.
 */
public class WorkerVolley extends WorkerCallback {

    public WorkerVolley(Context context) {
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }

    public void getDormitoryInfo(final WorkerCallback.GetDormitoryInfoCallback callback) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "get-dormitory-info-slow";
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

                                JSONArray dormitory = data.getJSONArray("dormitory");
                                DormitoryController.clearAll(context);
                                for (int i = 0; i < dormitory.length(); i++) {
                                    JSONObject object = dormitory.getJSONObject(i);
                                    long id = object.getLong("id");
                                    String name = object.getString("name");

                                    Dormitory dormitoryObj = new Dormitory(id, name);
                                    DormitoryController.insertOrUpdate(context, dormitoryObj);
                                }

                                JSONArray block = data.getJSONArray("block");
                                BlockController.clearAll(context);
                                for (int i = 0; i < block.length(); i++) {
                                    JSONObject object = block.getJSONObject(i);
                                    long id = object.getLong("id");
                                    int dormitory_id = object.getInt("dormitory_id");
                                    String name = object.getString("name");

                                    Block blockObj = new Block(id, dormitory_id, name);
                                    BlockController.insertOrUpdate(context, blockObj);
                                }

                                JSONArray level = data.getJSONArray("level");
                                LevelController.clearAll(context);
                                for (int i = 0; i < level.length(); i++) {
                                    JSONObject object = level.getJSONObject(i);
                                    long id = object.getLong("id");
                                    int block_id = object.getInt("block_id");
                                    String name = object.getString("name");

                                    Level levelObj = new Level(id, block_id, name);
                                    LevelController.insertOrUpdate(context, levelObj);
                                }

                                JSONArray room = data.getJSONArray("room");
                                RoomController.clearAll(context);
                                for (int i = 0; i < room.length(); i++) {
                                    JSONObject object = room.getJSONObject(i);
                                    long id = object.getLong("id");
                                    int level_id = object.getInt("level_id");
                                    String name = object.getString("name");

                                    Room levelObj = new Room(id, level_id, name);
                                    RoomController.insertOrUpdate(context, levelObj);
                                }

                                JSONArray diary_type = data.getJSONArray("diary_type");
                                DiaryTypeController.clearAll(context);
                                for (int i = 0; i < diary_type.length(); i++) {
                                    JSONObject object = diary_type.getJSONObject(i);
                                    long id = object.getLong("id");
                                    String name = object.getString("name");

                                    DiaryType diaryType = new DiaryType(id, name);
                                    DiaryTypeController.insertOrUpdate(context, diaryType);
                                }

                                JSONArray case_status = data.getJSONArray("case_status");
                                StatusController.clearAll(context);
                                for (int i = 0; i < case_status.length(); i++) {
                                    JSONObject object = case_status.getJSONObject(i);
                                    long id = object.getLong("id");
                                    String name = object.getString("name");

                                    Status status = new Status(id, name);
                                    StatusController.insertOrUpdate(context, status);
                                }

                                JSONArray category_arr = data.getJSONArray("category");
                                CategoryController.clearAll(context);
                                SubCategoryController.clearAll(context);
                                for (int i = 0; i < category_arr.length(); i++) {
                                    JSONObject object = category_arr.getJSONObject(i);
                                    long category_id = object.getLong("id");
                                    String name = object.getString("name");
                                    String description = object.getString("description");
                                    long sub_category_id = object.getLong("sub_category_id");
                                    String sub_category_name = object.getString("sub_category_name");
                                    String sub_category_description = object.getString("sub_category_description");

                                    Category category = new Category(category_id, name, description);
                                    CategoryController.insertOrUpdate(context, category);
                                    SubCategory subCategory = new SubCategory(sub_category_id, category_id, sub_category_name, sub_category_description);
                                    SubCategoryController.insertOrUpdate(context, subCategory);
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

    public void getAll(final WorkerCallback.GetAllCallback callback, final int page, final String key_search) {
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
                                    WorkerController.clearAll(context);
                                }
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    long id = object.getLong("id");
                                    String barcode = object.getString("barcode");
                                    String name = object.getString("name");
                                    String nationality_code = object.getString("nationality_code");
                                    String passport = object.getString("passport");
                                    String expiry = object.getString("expiry");
                                    int sex = object.getInt("sex");
                                    String company = object.getString("company");
                                    String work_permit = object.getString("work_permit");
                                    long dormitory_id = object.getLong("dormitory_id");
                                    long block_id = object.getLong("block_id");
                                    long level_id = object.getLong("level_id");
                                    long room_id = object.getLong("room_id");
                                    String unit_number = object.getString("unit_number");
                                    String image_1 = object.getString("image_1");
                                    String image_2 = object.getString("image_2");
                                    String image_3 = object.getString("image_3");
                                    String created_at = object.getString("created_at");
                                    String updated_at = object.getString("updated_at");

                                    Worker worker = new Worker(id, barcode, name, nationality_code, passport, expiry, sex, company, work_permit, dormitory_id, block_id, level_id, room_id, unit_number, image_1, image_2, image_3, created_at, updated_at, "");
                                    WorkerController.insertOrUpdate(context, worker);
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
                params.put("page", page + "");
                params.put("key_search", key_search);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void getNationality(final WorkerCallback.GetNationalityCallback callback) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "get-all-nationality";
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
                                NationalityController.clearAll(context);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    String code = object.getString("code");
                                    String slug = object.getString("slug");
                                    String name = object.getString("name");
                                    int order = object.getInt("order");

                                    Nationality nationality = new Nationality(code, slug, name, order);
                                    NationalityController.insertOrUpdate(context, nationality);
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

    public void getLogChange(final WorkerCallback.GetLogChangeCallback callback, final String worker_id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "get-log-change";
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

                                callback.onSuccess(success, message, data);
                            } else {
                                callback.onSuccess(success, message, null);
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
                params.put("worker_id", worker_id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void createWorker(final WorkerCallback.CreateWorkerCallback callback, final Worker worker, final Uri uri1, final Uri uri2, final Uri uri3) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "create";
        WorkerCreateRequest workerCreateRequest = new WorkerCreateRequest(context, url, worker, uri1, uri2, uri3,
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
        workerCreateRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT * 10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(workerCreateRequest);
    }

    public void updateWorker(final WorkerCallback.UpdateWorkerCallback callback, final Worker worker, final Bitmap bitmap) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "update";
        WorkerUpdateRequest workerUpdateRequest = new WorkerUpdateRequest(context, url, worker, bitmap,
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
        workerUpdateRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT * 10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(workerUpdateRequest);
    }

    public void changeImageWorker(final WorkerCallback.ChangeImageCallback callback, final Worker worker, final String column_image, final Uri uri, final Bitmap bmSign) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "change-image";
        WorkerChangeImageRequest workerChangeImageRequest = new WorkerChangeImageRequest(context, url, worker, column_image, uri, bmSign,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            boolean success = root.getBoolean("success");
                            String message = root.getString("message");
                            if (success) {
                                JSONObject data = root.getJSONObject("data");
                                String image_1 = data.getString("image_1");
                                String image_2 = data.getString("image_2");
                                String image_3 = data.getString("image_3");

                                callback.onSuccess(success, message, image_1, image_2, image_3);
                            } else {
                                callback.onSuccess(success, message, "", "", "");
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
        workerChangeImageRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT * 10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(workerChangeImageRequest);
    }
}