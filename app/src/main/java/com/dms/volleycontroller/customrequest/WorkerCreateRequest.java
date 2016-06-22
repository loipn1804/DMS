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
import greendao.Worker;

/**
 * Created by USER on 2/25/2016.
 */
public class WorkerCreateRequest extends Request<String> {

    private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
    private final Response.Listener<String> mListener;
    private Worker worker;
    private Uri uri1;
    private Uri uri2;
    private Uri uri3;
    private Context context;

    public WorkerCreateRequest(Context context, String url, Worker worker, Uri uri1, Uri uri2, Uri uri3, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        this.context = context;
        mListener = listener;
        this.worker = worker;
        this.uri1 = uri1;
        this.uri2 = uri2;
        this.uri3 = uri3;

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
        addImageUri("image_1", uri1);
        addImageUri("image_2", uri2);
        addImageUri("image_3", uri3);

        mBuilder.addTextBody("barcode", worker.getBarcode());
        mBuilder.addTextBody("name", worker.getName());
        mBuilder.addTextBody("nationality_code", worker.getNationality_code());
        mBuilder.addTextBody("passport", worker.getPassport());
        mBuilder.addTextBody("expiry", worker.getExpiry());
        mBuilder.addTextBody("company", worker.getCompany());
        mBuilder.addTextBody("work_permit", worker.getWork_permit());
        mBuilder.addTextBody("dormitory_id", worker.getDormitory_id() + "");
        mBuilder.addTextBody("block_id", worker.getBlock_id() + "");
        mBuilder.addTextBody("level_id", worker.getLevel_id() + "");
        mBuilder.addTextBody("room_id", worker.getRoom_id() + "");
        mBuilder.addTextBody("unit_number", worker.getUnit_number());
        mBuilder.addTextBody("sex", worker.getSex() + "");
        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
    }

    private void addImageUri(String name, Uri uri) {
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            mBuilder.addBinaryBody(name, is, ContentType.create("image/*"), name + ".jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
