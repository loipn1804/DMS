package com.dms.volleycontroller.customrequest;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import greendao.CaseObj;
import greendao.SubCategory;

/**
 * Created by USER on 2/23/2016.
 */
public class CaseCreateRequest extends Request<String> {

    private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
    private final Response.Listener<String> mListener;
    private List<Uri> mUris;
    private CaseObj case_obj;
    private List<SubCategory> listSubCategory;
    private String create_by;
    private Context context;

    public CaseCreateRequest(Context context, String url, CaseObj case_obj, List<Uri> uris, List<SubCategory> listSubCategory, String create_by, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        this.context = context;
        mListener = listener;
        this.case_obj = case_obj;
        this.listSubCategory = new ArrayList<>();
        this.listSubCategory.addAll(listSubCategory);
        this.create_by = create_by;
        mUris = new ArrayList<>();
        mUris.addAll(uris);

        buildMultipartEntity();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        headers.put("Accept", "application/json");

        return headers;
    }

    private void buildMultipartEntity() {
        int i = 0;
        for (Uri uri : mUris) {
            i++;
            try {
                InputStream is = context.getContentResolver().openInputStream(uri);
                mBuilder.addBinaryBody("image[" + i + "]", is, ContentType.create("image/*"), "image" + i + ".jpg");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        int j = 0;
        for (SubCategory subCategory : listSubCategory) {
            mBuilder.addTextBody("sub_category_id[" + (j++) + "]", subCategory.getId() + "");
        }

        mBuilder.addTextBody("dormitory_id", case_obj.getDormitory_id() + "");
        mBuilder.addTextBody("block_id", case_obj.getBlock_id() + "");
        mBuilder.addTextBody("category_id", case_obj.getCategory_id() + "");
        mBuilder.addTextBody("case_status_id", "1");
        mBuilder.addTextBody("create_by", create_by);
        mBuilder.addTextBody("content", case_obj.getContent());
        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
    }

    @Override
    public String getBodyContentType() {
        String contentTypeHeader = mBuilder.build().getContentType().getValue();
        return contentTypeHeader;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mBuilder.build().writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
        }

        return bos.toByteArray();
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
