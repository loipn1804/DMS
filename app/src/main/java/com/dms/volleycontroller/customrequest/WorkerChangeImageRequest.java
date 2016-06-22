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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import greendao.Worker;

/**
 * Created by USER on 2/26/2016.
 */
public class WorkerChangeImageRequest extends Request<String> {

    private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
    private final Response.Listener<String> mListener;
    private Worker worker;
    private String column_image;
    private Uri uri;
    private Bitmap bmSign;
    private Context context;

    public WorkerChangeImageRequest(Context context, String url, Worker worker, String column_image, Uri uri, Bitmap bmSign, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, errorListener);

        this.context = context;
        mListener = listener;
        this.worker = worker;
        this.column_image = column_image;
        this.uri = uri;
        this.bmSign = bmSign;

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
        addImageUri("image", uri);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmSign.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] data = bos.toByteArray();
        mBuilder.addBinaryBody("image_sign", data, ContentType.create("image/*"), "image_sign.jpg");

        mBuilder.addTextBody("worker_id", worker.getId() + "");
        mBuilder.addTextBody("column_image", column_image);
        mBuilder.addTextBody("log_message", worker.getLog_message());
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
