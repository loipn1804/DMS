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
import com.dms.daocontroller.CaseActionTakenController;
import com.dms.daocontroller.CaseController;
import com.dms.daocontroller.CaseImageController;
import com.dms.daocontroller.CaseReferredToController;
import com.dms.daocontroller.CaseSubCategoryController;
import com.dms.daocontroller.CategoryController;
import com.dms.daocontroller.StaffController;
import com.dms.daocontroller.StatusController;
import com.dms.daocontroller.SubCategoryController;
import com.dms.daocontroller.UserController;
import com.dms.staticfunction.StaticFunction;
import com.dms.thread.BackgroundThreadExecutor;
import com.dms.volleycontroller.callback.CaseCallback;
import com.dms.volleycontroller.customrequest.CaseAddImageRequest;
import com.dms.volleycontroller.customrequest.CaseCreateRequest;
import com.dms.volleycontroller.customrequest.UserChangeAvatarRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import greendao.Block;
import greendao.CaseActionTaken;
import greendao.CaseImage;
import greendao.CaseObj;
import greendao.CaseReferredTo;
import greendao.CaseSubCategory;
import greendao.Category;
import greendao.Dormitory;
import greendao.Staff;
import greendao.Status;
import greendao.SubCategory;

/**
 * Created by USER on 2/18/2016.
 */
public class CaseVolley extends CaseCallback {

    public CaseVolley(Context context) {
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }

    public void getAll(final CaseCallback.GetAllCallback callback, final int page, final long case_status_id) {
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
                                    CaseController.clearAll(context);
                                }
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    long id = object.getLong("id");
                                    long dormitory_id = object.getLong("dormitory_id");
                                    long block_id = object.getLong("block_id");
                                    long category_id = object.getLong("category_id");
                                    long case_status_id = object.getLong("case_status_id");
                                    long create_by = object.getLong("create_by");
                                    long assign_by = object.optLong("assign_by", 0);
                                    long assign_to = object.optLong("assign_to", 0);
                                    String content = object.getString("content");
                                    String created_at = object.getString("created_at");
                                    String updated_at = object.getString("updated_at");
                                    String spf_incident = object.getString("spf_incident");
                                    if (spf_incident.equalsIgnoreCase("null"))
                                        spf_incident = "";
                                    String case_investigation_name = object.getString("case_investigation_name");
                                    if (case_investigation_name.equalsIgnoreCase("null"))
                                        case_investigation_name = "";
                                    String case_investigation_contact = object.getString("case_investigation_contact");
                                    if (case_investigation_contact.equalsIgnoreCase("null"))
                                        case_investigation_contact = "";
                                    String operative_remarks = object.getString("operative_remarks");
                                    if (operative_remarks.equalsIgnoreCase("null"))
                                        operative_remarks = "";
                                    String government_agencies = object.getString("government_agencies");
                                    if (government_agencies.equalsIgnoreCase("null"))
                                        government_agencies = "";
                                    String head_ops_assessment = object.getString("head_ops_assessment");
                                    if (head_ops_assessment.equalsIgnoreCase("null"))
                                        head_ops_assessment = "";
                                    String follow_up_timeline = object.getString("follow_up_timeline");
                                    if (follow_up_timeline.equalsIgnoreCase("null"))
                                        follow_up_timeline = "";
                                    String assign_to_name = object.getString("assign_to_name");
                                    if (assign_to_name.equalsIgnoreCase("null"))
                                        assign_to_name = "";
                                    String create_by_name = object.getString("create_by_name");
                                    if (create_by_name.equalsIgnoreCase("null"))
                                        create_by_name = "";

                                    CaseObj aCase = new CaseObj(id, dormitory_id, block_id, category_id, case_status_id, create_by, assign_by, assign_to, content, created_at, updated_at, spf_incident, case_investigation_name, case_investigation_contact, operative_remarks, government_agencies, head_ops_assessment, follow_up_timeline, assign_to_name, create_by_name);
                                    CaseController.insertOrUpdate(context, aCase);
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
                params.put("case_status_id", case_status_id + "");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void getCaseDetail(final CaseCallback.GetCaseDetailCallback callback, final String case_id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "get-by-id";
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
                                long case_id = data.getLong("id");

                                JSONArray images = data.getJSONArray("images");
                                CaseImageController.clearAll(context);
                                for (int i = 0; i < images.length(); i++) {
                                    JSONObject object = images.getJSONObject(i);
                                    long id = object.getLong("id");
                                    String path = object.getString("path");

                                    CaseImage caseImage = new CaseImage(id, path, case_id);
                                    CaseImageController.insertOrUpdate(context, caseImage);
                                }

                                JSONArray sub_category = data.getJSONArray("sub_category");
                                CaseSubCategoryController.clearAll(context);
                                for (int i = 0; i < sub_category.length(); i++) {
                                    JSONObject object = sub_category.getJSONObject(i);
                                    long id = object.getLong("id");
                                    String name = object.getString("name");
                                    String description = object.getString("description");

                                    CaseSubCategory caseSubCategory = new CaseSubCategory(id, name, description, case_id);
                                    CaseSubCategoryController.insertOrUpdate(context, caseSubCategory);
                                }

                                JSONArray action_taken = data.getJSONArray("action_taken");
                                CaseActionTakenController.clearAll(context);
                                for (int i = 0; i < action_taken.length(); i++) {
                                    JSONObject object = action_taken.getJSONObject(i);
                                    long id = object.getLong("id");
                                    String name = object.getString("name");
                                    int order = object.getInt("order");

                                    CaseActionTaken caseActionTaken = new CaseActionTaken(id, name, order, case_id);
                                    CaseActionTakenController.insertOrUpdate(context, caseActionTaken);
                                }

                                JSONArray referred_to = data.getJSONArray("referred_to");
                                CaseReferredToController.clearAll(context);
                                for (int i = 0; i < referred_to.length(); i++) {
                                    JSONObject object = referred_to.getJSONObject(i);
                                    long id = object.getLong("id");
                                    String name = object.getString("name");
                                    int order = object.getInt("order");

                                    CaseReferredTo caseReferredTo = new CaseReferredTo(id, name, order, case_id);
                                    CaseReferredToController.insertOrUpdate(context, caseReferredTo);
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
                params.put("case_id", case_id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void updateCase(final CaseCallback.UpdateCallback callback, final CaseObj case_obj, final Dormitory dormitory, final Block block, final Category category, final List<SubCategory> listSubCategory) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "update";
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
                                long case_id = data.getLong("id");

                                JSONArray sub_category = data.getJSONArray("sub_category");
                                CaseSubCategoryController.clearAll(context);
                                for (int i = 0; i < sub_category.length(); i++) {
                                    JSONObject object = sub_category.getJSONObject(i);
                                    long id = object.getLong("id");
                                    String name = object.getString("name");
                                    String description = object.getString("description");

                                    CaseSubCategory caseSubCategory = new CaseSubCategory(id, name, description, case_id);
                                    CaseSubCategoryController.insertOrUpdate(context, caseSubCategory);
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
                params.put("case_id", case_obj.getId() + "");
                params.put("dormitory_id", dormitory.getId() + "");
                params.put("block_id", block.getId() + "");
                params.put("category_id", category.getId() + "");
                int i = 0;
                for (SubCategory subCategory : listSubCategory) {
                    params.put("sub_category_id[" + (i++) + "]", subCategory.getId() + "");
                }
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void changeStatus(final CaseCallback.ChangeStatusCallback callback, final long case_id, final long case_status_id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "change-status";
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
                params.put("case_id", case_id + "");
                params.put("case_status_id", case_status_id + "");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void assign(final CaseCallback.AssignCallback callback, final long case_id, final long assign_by, final long assign_to) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "assign";
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
                params.put("case_id", case_id + "");
                params.put("assign_by", assign_by + "");
                params.put("assign_to", assign_to + "");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void updateContent(final CaseCallback.UpdateContentCallback callback, final long case_id, final String content) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "update-content";
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
                params.put("case_id", case_id + "");
                params.put("content", content);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void getListCategory(final CaseCallback.GetListCategoryCallback callback) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "get-list-category";
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
                                CategoryController.clearAll(context);
                                SubCategoryController.clearAll(context);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
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

    public void getListStatus(final CaseCallback.GetListStatusCallback callback) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "get-list-status";
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
                                StatusController.clearAll(context);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    long id = object.getLong("id");
                                    String name = object.getString("name");

                                    Status status = new Status(id, name);
                                    StatusController.insertOrUpdate(context, status);
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

    public void getListStaff(final CaseCallback.GetListStaffCallback callback) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "get-list-staff";
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
                                StaffController.clearAll(context);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    long id = object.getLong("id");
                                    String name = object.getString("name");

                                    Staff staff = new Staff(id, name);
                                    StaffController.insertOrUpdate(context, staff);
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

    public void removeImage(final CaseCallback.RemoveImageCallback callback, final String case_id, final long image_id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "remove-image";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            boolean success = root.getBoolean("success");
                            String message = root.getString("message");
                            if (success) {
                                CaseImageController.clearById(context, image_id);

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
                params.put("case_id", case_id);
                params.put("image_id", image_id + "");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void addImage(final CaseCallback.AddImageCallback callback, final long case_id, final Bitmap bitmap) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "add-image";
        CaseAddImageRequest caseAddImageRequest = new CaseAddImageRequest(url, case_id + "", bitmap,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            boolean success = root.getBoolean("success");
                            String message = root.getString("message");
                            if (success) {
                                JSONArray images = root.getJSONArray("data");
                                CaseImageController.clearAll(context);
                                for (int i = 0; i < images.length(); i++) {
                                    JSONObject object = images.getJSONObject(i);
                                    long id = object.getLong("id");
                                    String path = object.getString("path");

                                    CaseImage caseImage = new CaseImage(id, path, case_id);
                                    CaseImageController.insertOrUpdate(context, caseImage);
                                }

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
        caseAddImageRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(caseAddImageRequest);
    }

    public void createCase(final CaseCallback.CreateCaseCallback callback, final CaseObj case_obj, final List<Uri> uris, final List<SubCategory> listSubCategory, final String create_by) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onError(context.getString(R.string.no_internet));
            return;
        }
        String url = this.BASE_URL + "create";
        CaseCreateRequest caseCreateRequest = new CaseCreateRequest(context, url, case_obj, uris, listSubCategory, create_by,
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
        caseCreateRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT * 10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(caseCreateRequest);
    }


}
