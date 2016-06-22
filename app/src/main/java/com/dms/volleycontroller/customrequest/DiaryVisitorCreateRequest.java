package com.dms.volleycontroller.customrequest;

import android.content.Context;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dms.daocontroller.UserController;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import greendao.DiaryVisitor;
import greendao.Worker;

/**
 * Created by USER on 3/1/2016.
 */
public class DiaryVisitorCreateRequest extends Request<String> {

    private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
    private final Response.Listener<String> mListener;
    private DiaryVisitor diaryVisitor;
    private Uri uri1;
    private Uri uri2;
    private Context context;

    public DiaryVisitorCreateRequest(Context context, String url, DiaryVisitor diaryVisitor, Uri uri1, Uri uri2, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        this.context = context;
        mListener = listener;
        this.diaryVisitor = diaryVisitor;
        this.uri1 = uri1;
        this.uri2 = uri2;

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
        if (diaryVisitor.getIs_staff() == 1) {
            mBuilder.addTextBody("is_staff", diaryVisitor.getIs_staff() + "");
            mBuilder.addTextBody("user_id", diaryVisitor.getUser_id() + "");
            mBuilder.addTextBody("content", diaryVisitor.getContent());
            mBuilder.addTextBody("created_by", UserController.getCurrentUser(context).getId() + "");
            mBuilder.addTextBody("diary_type_id", "1");
        } else {
            addImageUri("image_1", uri1);
            addImageUri("image_2", uri2);

            mBuilder.addTextBody("name", diaryVisitor.getName());
            mBuilder.addTextBody("is_staff", diaryVisitor.getIs_staff() + "");
            mBuilder.addTextBody("content", diaryVisitor.getContent());
            mBuilder.addTextBody("created_by", UserController.getCurrentUser(context).getId() + "");
            mBuilder.addTextBody("diary_type_id", "1");
        }


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
