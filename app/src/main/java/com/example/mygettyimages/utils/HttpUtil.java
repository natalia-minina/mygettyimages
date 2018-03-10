package com.example.mygettyimages.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.example.mygettyimages.Constants;
import com.example.mygettyimages.models.apimodels.BaseResponse;
import com.example.mygettyimages.models.apimodels.ServerErrorResponse;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class HttpUtil {

    private static final String boundary = "HmvGok4CVyYZIkZhT8jeDUgBILxL8dL";

    private static final String twoHyphens = "--";

    private static final String newLine = "\r\n";

    private HttpUtil() {
    }

    public static BaseResponse get(String url, List<NameValuePair> queryParams,
                                   List<NameValuePair> queryParams2, Context ctx, boolean isCheckErrorStatus) throws IOException {
        String fullUrl = url + getQueryWithFractionLine(queryParams);
        if (queryParams2 != null && queryParams2.size() > 0) {
            fullUrl += "?" + getQuery(queryParams2);
        }

        return send(fullUrl, null, null, null, false, null, ctx, Constants.FieldValue.UNDEFINED_FIELD_VALUE, isCheckErrorStatus);
    }

    public static BaseResponse post(String url, List<NameValuePair> queryParams, Context ctx) throws IOException {

        return send(url, queryParams, null, null, false, null, ctx, Constants.FieldValue.UNDEFINED_FIELD_VALUE);
    }

    public static BaseResponse get(String url, List<NameValuePair> queryParams, Context ctx) throws IOException {
        return send(url + getQueryWithFractionLine(queryParams), null, null, null, false, null, ctx, Constants.FieldValue.UNDEFINED_FIELD_VALUE);
    }

    public static BaseResponse post(String url, String jsonBody, Context ctx) throws IOException {
        return send(url, null, null, null, false, jsonBody, ctx, Constants.FieldValue.UNDEFINED_FIELD_VALUE);
    }

    public static BaseResponse post(String url, String jsonBody, Context ctx, boolean isCheckErrorStatus) throws IOException {
        return send(url, null, null, null, false, jsonBody, ctx, Constants.FieldValue.UNDEFINED_FIELD_VALUE, isCheckErrorStatus);
    }

    public static BaseResponse put(String url, String jsonBody, Context ctx) throws IOException {
        return send(url, null, null, null, true, jsonBody, ctx, Constants.FieldValue.UNDEFINED_FIELD_VALUE);
    }

    public static BaseResponse post(String url, List<NameValuePair> params, Bitmap bitmap,
                                    String bitmapParamName, Context ctx) throws IOException {
        List<String> bitmapParamNames = new ArrayList<>();
        List<Bitmap> bitmaps = new ArrayList<>();
        if (!TextUtils.isEmpty(bitmapParamName)) {
            bitmapParamNames.add(bitmapParamName);
            bitmaps.add(bitmap);
        }
        return send(url, params, bitmaps, bitmapParamNames, false, null, ctx, Constants.FieldValue.UNDEFINED_FIELD_VALUE);
    }

    public static BaseResponse post(String url, List<NameValuePair> params, List<Bitmap> bitmaps,
                                    List<String> bitmapParamNames, Context ctx, long maxBitmapBytesLen) throws IOException {
        return send(url, params, bitmaps, bitmapParamNames, false, null, ctx, maxBitmapBytesLen);
    }

    private static BaseResponse send(String url, List<NameValuePair> params, List<Bitmap> bitmaps,
                                     List<String> bitmapParamNames,
                                     boolean isPut, String jsonBody, Context ctx,
                                     long maxBitmapBytesLen) throws IOException {
        return send(url, params, bitmaps,
                bitmapParamNames,
                isPut, jsonBody, ctx,
                maxBitmapBytesLen, true);
    }

    private static BaseResponse send(String url, List<NameValuePair> params, List<Bitmap> bitmaps,
                                     List<String> bitmapParamNames,
                                     boolean isPut, String jsonBody, Context ctx,
                                     long maxBitmapBytesLen, boolean isCheckErrorStatus) throws IOException {
        BufferedWriter writer = null;
        OutputStream os = null;
        InputStream in = null;
        DataOutputStream request = null;
        BaseResponse baseResponse = new BaseResponse();
        try {
            String result = null;
            URL urlObg = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObg.openConnection();
            conn.setReadTimeout(Constants.Server.ServerSettings.CONNECTION_READ_TIMEOUT);
            conn.setConnectTimeout(Constants.Server.ServerSettings.CONNECTION_CONNECT_TIMEOUT);
            if (isPut) {
                conn.setRequestMethod("PUT");
            } else {
                conn.setRequestMethod((params == null && jsonBody == null) ? "GET" : "POST");
            }
            if (bitmapParamNames != null && bitmapParamNames.size() > 0) {
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            } else if (jsonBody != null) {
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
            }
            conn.setRequestProperty("Accept-Language", Locale.getDefault().getLanguage());
            if (url != null && url.startsWith(Constants.Server.Urls.BASE_URL)) {
                conn.setRequestProperty(Constants.Server.Auth.GETTY_IMAGES_API_KEY_NAME,
                        Constants.Server.Auth.GETTY_IMAGES_API_KEY);
            }

            if (Constants.Debug.IS_DEBUG_HTTP) {
                String requestStr = String.format("Sending %s request %s \n"
                                + "\n with "
                                + " body %s"
                                + " with headers %s", conn.getRequestMethod(), url, jsonBody != null ? jsonBody : URLDecoder.decode(getQuery(params), "UTF-8"),
                        formatHeaders(conn.getRequestProperties()));
                Log.i(Constants.Debug.DEBUG_HTTP_TAG, requestStr);
            }
            conn.setDoInput(true);

            if (params != null || jsonBody != null) {
                os = conn.getOutputStream();
                if (bitmapParamNames == null || bitmapParamNames.size() == 0) {
                    writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    if (jsonBody != null) {
                        writer.write(jsonBody);
                    } else {
                        writer.write(getQuery(params));
                    }
                    writer.flush();
                    writer.close();

                } else {
                    ArrayList<byte[]> bitmapBytesList = new ArrayList();
                    for (int i = 0; i < bitmapParamNames.size(); i++) {
                        bitmapBytesList.add(getJPEGImageBytes(bitmaps.get(i), maxBitmapBytesLen));
                    }

                    request = new DataOutputStream(os);
                    writeQuery(request, params, bitmapBytesList, bitmapParamNames);
                    request.flush();
                    request.close();
                }

            }
            if (Constants.Debug.IS_DEBUG_HTTP) {
                String responseStr = String.format("Got response HTTP %s  %s",
                        conn.getResponseCode(), conn.getResponseMessage());
                Log.i(Constants.Debug.DEBUG_HTTP_TAG, responseStr);
            }

            baseResponse.setErrorMessage(conn.getResponseMessage());
            baseResponse.setResponseCode(conn.getResponseCode());

            if (Thread.currentThread().isInterrupted()) {
                conn.disconnect();
                throw new IOException();
            }
            try {
                StringBuilder builder = new StringBuilder();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK ||
                        conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                    in = conn.getInputStream();
                } else {
                    in = conn.getErrorStream();
                }

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                result = builder.toString();
                if ((conn.getResponseCode() == HttpURLConnection.HTTP_OK ||
                        conn.getResponseCode() == HttpURLConnection.HTTP_CREATED
                        || (conn.getResponseCode() == HttpURLConnection.HTTP_CONFLICT && !isCheckErrorStatus)
                        || (conn.getResponseCode() == HttpURLConnection.HTTP_NOT_IMPLEMENTED && !isCheckErrorStatus))
                        && !TextUtils.equals(Constants.Server.ServerResponses.INVALID_ACCESS_TOKEN, result)) {
                    baseResponse.setSuccess(true);
                    baseResponse.setSuccessResponse(result);
                    if (isCheckErrorStatus) {
                        try {
                            ServerErrorResponse serverErrorResponse = new JacksonFactory().createJsonParser(result)
                                    .parse(ServerErrorResponse.class);
                            if (serverErrorResponse != null && Constants.Server.ServerResponses.SERVER_ERROR_STATUS.equals(serverErrorResponse.getStatus())) {
                                baseResponse.setSuccess(false);
                                baseResponse.setSuccessResponse(null);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                result = "FileNotFoundException";
            }

            if (Constants.Debug.IS_DEBUG_HTTP) {
                String headers = formatHeaders(conn.getHeaderFields());
                String responseStr = String.format("\n with body %s \n"
                        + "\n"
                        + " with headers %s", result, headers);
                Utils.logLongMessage(Constants.Debug.DEBUG_HTTP_TAG, responseStr, Log.INFO);
                //   Log.i(Constants.Debug.DEBUG_HTTP_TAG, responseStr);
            }
            return baseResponse;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
            }
            try {
                if (request != null) {
                    request.close();
                }
            } catch (Exception e) {
            }

            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private static String formatHeaders(Map<String, List<String>> headers) {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (entry.getValue() == null || entry.getKey() == null) {
                continue;
            }
            str.append(entry.getKey());
            str.append(": ");
            for (String value : entry.getValue()) {
                str.append(value);
                str.append(", ");
            }
        }
        return str.toString();
    }

    private static void writeQuery(DataOutputStream request, List<NameValuePair> params,
                                   List<byte[]> byteArrayImages, List<String> bitmapParamNames) throws IOException {
        for (NameValuePair param : params) {
            if (TextUtils.isEmpty(param.getValue())) {
                continue;
            }
            request.writeBytes(twoHyphens);
            request.writeBytes(boundary);
            request.writeBytes(newLine);
            request.writeBytes("Content-Disposition: form-data; name=\"" + param.getName() + "\"");
            request.writeBytes(newLine);
            request.writeBytes(newLine);
            request.write(param.getValue().getBytes("UTF-8"));
            request.writeBytes(newLine);
        }
        for (int i = 0; i < byteArrayImages.size(); i++) {
            byte[] byteArrayImage = byteArrayImages.get(i);
            String bitmapParamName = bitmapParamNames.get(i);
            if (byteArrayImage.length != 0) {

                request.writeBytes(twoHyphens);
                request.writeBytes(boundary);
                request.writeBytes(newLine);

                request.writeBytes("Content-Disposition: form-data; name=\"" + bitmapParamName + "\"; filename=\"pic" + i + ".jpg\"");
                request.writeBytes(newLine);
                request.writeBytes("Content-Type: image/jpeg");
                request.writeBytes(newLine);
                request.writeBytes(newLine);
                request.write(byteArrayImage);

                request.writeBytes(newLine);
            }
        }
        request.writeBytes(twoHyphens);
        request.writeBytes(boundary);
        request.writeBytes(twoHyphens);
        request.writeBytes(newLine);
    }

    public static byte[] getJPEGImageBytes(Bitmap bitmap, long maxBitmapBytesLen) {
        if (bitmap == null) {
            return new byte[]{};
        }
        byte[] imageBytes;
        int quality = 90;
        do {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            imageBytes = baos.toByteArray();
            quality--;
        } while (maxBitmapBytesLen > 0 && quality >= 0 && imageBytes.length > maxBitmapBytesLen);
        //    String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageBytes;
    }

    private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        if (params == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private static String getQueryWithFractionLine(List<NameValuePair> params) throws UnsupportedEncodingException {
        if (params == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first) {
                first = false;
            } else {
                result.append("/");
            }
            if (!TextUtils.isEmpty(pair.getName())) {
                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("/");
            }
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}