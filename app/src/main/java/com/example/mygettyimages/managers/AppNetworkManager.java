package com.example.mygettyimages.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.example.mygettyimages.Constants;
import com.example.mygettyimages.R;
import com.example.mygettyimages.activities.BaseActivity;
import com.example.mygettyimages.models.GettyImage;
import com.example.mygettyimages.models.apimodels.BaseResponse;
import com.example.mygettyimages.models.apimodels.EmptyModel;
import com.example.mygettyimages.models.apimodels.ServerGettyImage;
import com.example.mygettyimages.models.apimodels.ServerGettyImageDisplaySize;
import com.example.mygettyimages.models.apimodels.ServerGettyImageList;
import com.example.mygettyimages.models.apimodels.ServerGettyImageMaxDimensions;
import com.example.mygettyimages.models.apimodels.StringModel;
import com.example.mygettyimages.utils.HttpUtil;
import com.example.mygettyimages.utils.Utils;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class AppNetworkManager {

    private Context ctx;

    public AppNetworkManager(Context ctx) {
        this.ctx = ctx;
    }

    public GettyImage searchGettyImage(String query) {
        String url = Constants.Server.Urls.IMAGE_SEARCH_URL;
        List<NameValuePair> queryParams = new ArrayList<>();
        queryParams.add(new BasicNameValuePair(Constants.Server.Urls.PHRASE_QUERY,
                query));
        queryParams.add(new BasicNameValuePair(Constants.Server.Urls.FIELDS_QUERY,
                Constants.Server.Urls.FIELDS_VALUE));
        queryParams.add(new BasicNameValuePair(Constants.Server.Urls.SORT_ORDER_QUERY,
                "" + Constants.Server.Urls.BEST_MATH_VALUE));
        queryParams.add(new BasicNameValuePair(Constants.Server.Urls.PAGE_SIZE_QUERY,
                "" + Constants.Data.SEARCH_COUNT));
        BaseResponse<ServerGettyImageList> endResponse = getObjData(url, null,
                queryParams, true, ServerGettyImageList.class);
        if (endResponse.isSuccess()) {
            ServerGettyImageList response = endResponse.getData();
            if (response != null) {
                List<ServerGettyImage> serverGettyImages = response.getImages();
                if (serverGettyImages != null) {
                    if (serverGettyImages.size() == 0) {
                        if (ctx instanceof BaseActivity) {
                            ((BaseActivity) ctx).showMessage(R.string.empty_search_result_message);
                        }
                        return null;
                    }
                    for (ServerGettyImage serverGettyImage : serverGettyImages) {
                        if (serverGettyImage != null) {
                            List<ServerGettyImageDisplaySize> displaySizes = serverGettyImage.getDisplaySizes();
                            ServerGettyImageMaxDimensions maxDimensions = serverGettyImage.getMaxDimensions();
                            if (displaySizes != null && displaySizes.size() > 0) {
                                for (int i = 0; i < displaySizes.size(); i++) {

                                    ServerGettyImageDisplaySize displaySize = displaySizes.get(i);
                                    if (displaySize != null &&
                                            (TextUtils.equals(displaySize.getName(), Constants.Server.Urls.THUMB_VALUE)
                                                    || i == displaySizes.size() - 1)) {
                                        GettyImage im = new GettyImage(query
                                                , displaySize.getUri(), serverGettyImage.getId());
                                        im.setSearchDate(Utils.getCurrentTime());
                                        if (maxDimensions != null) {
                                            im.setMaxDimensionsHeight(maxDimensions.getHeight());
                                            im.setMaxDimensionsWidth(maxDimensions.getWidth());
                                        }
                                        return im;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        return null;

    }

    private <T> BaseResponse<T> getObjData(String url, List<NameValuePair> queryParams,
                                           List<NameValuePair> queryParams2,
                                           boolean isShowErrorMessage, Class<T> objClass) {
        return getObjData(url, queryParams, queryParams2, isShowErrorMessage, objClass, null, null,
                Constants.FieldValue.UNDEFINED_FIELD_VALUE, false);

    }

    private <T> BaseResponse<T> getObjData(String url, List<NameValuePair> queryParams,
                                           List<NameValuePair> queryParams2,
                                           boolean isShowErrorMessage, Class<T> objClass,
                                           List<Bitmap> bitmaps, List<String> bitmapNames,
                                           long maxBitmapBytesLen
            , boolean isPost) {
        return getObjData(url, queryParams, queryParams2,
                isShowErrorMessage, objClass, bitmaps, bitmapNames, maxBitmapBytesLen
                , isPost, true);
    }

    private <T> BaseResponse<T> getObjData(String url, List<NameValuePair> queryParams,
                                           List<NameValuePair> queryParams2,
                                           boolean isShowErrorMessage, Class<T> objClass,
                                           List<Bitmap> bitmaps, List<String> bitmapNames,
                                           long maxBitmapBytesLen
            , boolean isPost, boolean isCheckErrorStatus) {
        BaseResponse<T> endResponse = new BaseResponse();
        BaseResponse response = getData(url, queryParams, queryParams2, isShowErrorMessage,
                bitmaps, bitmapNames, maxBitmapBytesLen, isPost, isCheckErrorStatus);

        if (response.isSuccess()) {
            String post = response.getSuccessResponse();
            try {
                if (TextUtils.isEmpty(post) && objClass.isInstance(new EmptyModel())) {
                    endResponse.setData(null);
                    endResponse.setSuccess(true);
                } else if (objClass.isInstance(new StringModel())) {
                    endResponse.setSuccessResponse(post);
                    endResponse.setData(null);
                    endResponse.setSuccess(true);
                } else {
                    T result = new JacksonFactory().createJsonParser(post)
                            .parse(objClass);
                    if (result != null) {
                        endResponse.setData(result);
                        endResponse.setSuccess(true);
                    }
                }
            } catch (Exception e) {
                if (isShowErrorMessage) {
                    if (ctx instanceof BaseActivity) {
                        ((BaseActivity) ctx).showMessage(R.string.has_error_occurred);
                    }
                }
                Log.e(Constants.Debug.ERROR_TAG, "AppNetworkManager getObjData url=" + url, e);
            }
        }
        return endResponse;
    }

    private BaseResponse getData(String url, List<NameValuePair> queryParams,
                                 List<NameValuePair> queryParams2, boolean isShowErrorMessage,
                                 List<Bitmap> bitmaps, List<String> bitmapNames, long maxBitmapBytesLen,
                                 boolean isPost, boolean isCheckErrorStatus) {
        BaseResponse endResponse = new BaseResponse();
        try {

            BaseResponse response;
            if (bitmapNames == null || bitmapNames.size() == 0) {
                if (isPost) {
                    response = HttpUtil.post(url, queryParams, ctx);
                } else {
                    response = HttpUtil.get(url, queryParams, queryParams2, ctx, isCheckErrorStatus);
                }
            } else {
                response = HttpUtil.post(url, queryParams, bitmaps, bitmapNames, ctx, maxBitmapBytesLen);
            }
            if (response != null && response.isSuccess()) {
                endResponse.setSuccessResponse(response.getSuccessResponse());
                endResponse.setSuccess(true);
            }

            if (isShowErrorMessage && !endResponse.isSuccess()) {
                if (ctx instanceof BaseActivity) {
                    ((BaseActivity) ctx).showMessage(R.string.has_error_occurred);
                }
            }
        } catch (Exception e) {
            if (isShowErrorMessage) {
                showNetworkError(e);
            }
            Log.e(Constants.Debug.ERROR_TAG, "AppNetworkManager getData url=" + url, e);
        }
        return endResponse;
    }

    private void showNetworkError(Exception e) {
        if (ctx == null) {
            return;
        }
        if (ctx instanceof BaseActivity) {
            ((BaseActivity) ctx).showMessage(R.string.network_error_in_update);
        }
        if (e != null) {
            StringBuilder errorMsg = new StringBuilder("error: ");
            errorMsg.append(e.toString());
            errorMsg.append("\n");
            StackTraceElement[] trace = e.getStackTrace();
            if (trace != null) {
                for (StackTraceElement el : trace) {
                    errorMsg.append(el.toString());
                    errorMsg.append("\n");
                }
            }
            Log.e(Constants.Debug.DEBUG_HTTP_TAG, errorMsg.toString());
        }
    }
}
