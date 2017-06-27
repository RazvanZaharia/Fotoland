package eu.mobiletouch.fotoland.x_base;

import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


@SuppressWarnings("unused")
public class BaseRequest<T> implements Runnable, VolleyRequest.VolleyRequestListener {

    private final static String BASE_URL = "http://www.fotoland.ro/api/";

    public static abstract class Callback<T> {

        protected abstract void onResponse(Context context, T response);

        protected abstract void onError(Context context, Exception exception);
    }

    public VolleyRequest volleyRequest;
    private byte[] responseBytes;

    private static String[] debugParam(String[] paramNames) {
        ArrayList<String> paramNamesArr = new ArrayList<String>(Arrays.asList(paramNames));
        return paramNamesArr.toArray(new String[]{});
    }

    private static Object[] debugValues(Context context, Object[] paramValues) {
        ArrayList<Object> paramValuesArr = new ArrayList<Object>(Arrays.asList(paramValues));
        return paramValuesArr.toArray(new Object[]{});
    }

    private WeakReference<Context> mWeakContext;
    private int method;
    private String endpoint;
    private String baseUrl;
    private String[] paramNames;
    private Object[] paramValues;
    private String[] headerParamNames = new String[]{};
    private Object[] headerParamValues = new Object[]{};
    private Object requestData;
    private Callback<T> callback;
    private boolean showLoadingDialog = true;
    private BaseRunnableRequest<T> request;


    public static class Builder<T> {
        private static final int DEFAULT_METHOD = Method.GET;

        private WeakReference<Context> mWeakContext;
        private String endpoint;
        private String baseUrl;
        private BaseRunnableRequest<T> request;


        private int method = DEFAULT_METHOD;
        private String[] paramNames = new String[]{};
        private Object[] paramValues = new Object[]{};

        private String[] headerParamNames = new String[]{};
        private Object[] headerParamValues = new Object[]{};

        private Object requestData;
        private Callback<T> callback;
        private boolean showLoadingDialog = true;

        public Builder(Context context, String endpoint, BaseRunnableRequest<T> request) {
            this.mWeakContext = new WeakReference<>(context);
            this.endpoint = endpoint;
            this.request = request;
        }

        public Builder<T> baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder<T> method(int method) {
            this.method = method;
            return this;
        }

        public Builder<T> headerParamNames(String[] headerParamNames) {
            this.headerParamNames = headerParamNames;
            return this;
        }

        public Builder<T> headerParamValues(Object[] headerParamValues) {
            this.headerParamValues = headerParamValues;
            return this;
        }

        public Builder<T> paramNames(String[] paramNames) {
            this.paramNames = paramNames;
            return this;
        }

        public Builder<T> paramValues(Object[] paramValues) {
            this.paramValues = paramValues;
            return this;
        }

        public Builder<T> requestData(Object requestData) {
            this.requestData = requestData;
            return this;
        }

        public Builder<T> callback(Callback<T> callback) {
            this.callback = callback;
            return this;
        }

        public Builder<T> showLoadingDialog(boolean showLoadingDialog) {
            this.showLoadingDialog = showLoadingDialog;
            return this;
        }

        public BaseRequest<T> build() {
            return new BaseRequest<>(this);
        }

    }

    private BaseRequest(Builder builder) {
        this.mWeakContext = builder.mWeakContext;
        this.method = builder.method;
        this.endpoint = builder.endpoint;
        this.baseUrl = builder.baseUrl;
        this.paramNames = builder.paramNames;
        this.paramValues = builder.paramValues;
        this.headerParamNames = builder.headerParamNames;
        this.headerParamValues = builder.headerParamValues;
        this.requestData = builder.requestData;
        this.callback = builder.callback;
        this.showLoadingDialog = builder.showLoadingDialog;
        this.request = builder.request;
    }

    private void initRequest() {
        if (mWeakContext == null || mWeakContext.get() == null) {
            return;
        }
        final long requestStartTime = System.currentTimeMillis();

        String url = "";
        String bodyContentType = null;
        Object bodyData = null;

        if (method == Method.GET || method == Method.DELETE) {
            url = getUrlWithParams(endpoint, paramNames, paramValues);
        } else {
            if (requestData != null) {
                url = getUrlWithParams(endpoint, paramNames, paramValues);
                Pair<String, byte[]> contentTypeAndRawBodyObject = getContentTypeAndRawBodyObject(
                        requestData);
                bodyContentType = contentTypeAndRawBodyObject.first;
                bodyData = contentTypeAndRawBodyObject.second;
            } else {
                url = getBaseUrl() + endpoint;
                boolean multipart = false;
                for (Object paramValue : paramValues)
                    if (paramValue instanceof File) {
                        multipart = true;
                        break;
                    }
                if (multipart) {
                    Pair<String, byte[]> contentTypeAndRawBodyMultipart = getContentTypeAndRawBodyMultipart(
                            paramNames, paramValues);
                    bodyContentType = contentTypeAndRawBodyMultipart.first;
                    bodyData = contentTypeAndRawBodyMultipart.second;
                } else {
                    bodyData = getParamsForBody(paramNames, paramValues);
                }
            }
        }

        final String finalUrl = url;

        Listener<String> onResponse = new Listener<String>() {
            @Override
            public void onResponse(final String stringResponse) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null && mWeakContext != null && mWeakContext.get() != null) {
                            final Context context = mWeakContext.get();
                            try {
                                Log.i("RESPONSES " + context.getClass().getSimpleName(), request.getClass()
                                        .getCanonicalName() + "\n " + stringResponse);

                                if (stringResponse == null) {
                                    callback.onError(context, new NullPointerException("response is null"));
                                    return;
                                }

                                final T object = getResponse(stringResponse);
                                callback.onResponse(context, object);
                            } catch (final Exception e) {
                                e.printStackTrace();
                                callback.onError(context, e);
                            }
                        }
                    }
                };
            }
        };
        ErrorListener onError = new ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                if (callback != null && mWeakContext != null && mWeakContext.get() != null) {
                    final Context context = mWeakContext.get();
                    callback.onError(context, volleyError);
                }
            }
        };
        if (mWeakContext == null || mWeakContext.get() == null) {
            return;
        }
        Context context = mWeakContext.get();
        volleyRequest = new VolleyRequest(context, method, url, bodyContentType, bodyData, getHeaderParams(headerParamNames, headerParamValues),
                onResponse, onError, this, showLoadingDialog(), endpoint);

        volleyRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 0.0f));
        volleyRequest.setShouldCache(false);

        Log.i("REQUESTS: " + context.getClass().getSimpleName() + "\n", url);
        if (bodyData != null) {
            Log.i("REQUEST-BODY: " + context.getClass().getSimpleName() + "\n", bodyData.toString());
        }
    }

    private Map<String, String> getHeaderParams(String[] headerParamNames, Object[] headerParamValues) {
        if (headerParamValues == null || headerParamNames == null || headerParamValues.length != headerParamNames.length) {
            return null;
        }
        Map<String, String> headerParams = new HashMap<String, String>();
        for (int i = 0; i < headerParamValues.length; i++) {
            if (headerParamValues[i] != null) {
                final String paramValueToString = headerParamValues[i].toString();
                headerParams.put(headerParamNames[i], paramValueToString);
            }
        }
        return headerParams;
    }

    protected String getBaseUrl() {
        return BASE_URL;
    }

    private String getDeviceID(Context context, boolean encodedBase64) {
        String deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

        if (encodedBase64) {
            byte[] encodedBytes = Base64.encode(deviceId.getBytes(), Base64.NO_WRAP);
            deviceId = new String(encodedBytes);
        }

        try {
            return URLEncoder.encode(deviceId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return deviceId;
    }

    private String getUrlWithParams(String endpoint, String[] paramNames, Object[] paramValues) {
        String paramsString = "";
        String url = getBaseUrl() + endpoint;

        if (paramValues == null) {
            return url;
        }

        for (int i = 0; i < paramValues.length; i++)
            if (paramValues[i] != null) {
                try {
                    final String encodedParamValue = URLEncoder
                            .encode(paramValues[i].toString(), "UTF-8");
                    if (encodedParamValue != null) {
                        paramsString += (paramsString.length() == 0 ? "?" : "&") + paramNames[i] + "=" + encodedParamValue;
                    }
                } catch (UnsupportedEncodingException e) {
                }
            }
        return url + paramsString;
    }

    private HashMap<String, String> getParamsForBody(String[] paramNames, Object[] paramValues) {
        if (paramValues == null) {
            return null;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        for (int i = 0; i < paramValues.length; i++) {
            if (paramValues[i] != null) {
                final String paramValueToString = paramValues[i].toString();
                params.put(paramNames[i], paramValueToString);
            }
        }
        return params;
    }

    private Pair<String, byte[]> getContentTypeAndRawBodyObject(Object requestData) {
        if (requestData instanceof byte[]) {
            return new Pair<String, byte[]>("application/octet-stream", (byte[]) requestData);
        }
        if (requestData instanceof String) {
            return new Pair<String, byte[]>("text/plain", requestData.toString().getBytes());
        }
        if (requestData instanceof JSONObject) {
            return new Pair<String, byte[]>("application/json", requestData.toString().getBytes());
        }
        return new Pair<String, byte[]>("application/json", new Gson().toJson(requestData)
                .getBytes());
    }

    private Pair<String, byte[]> getContentTypeAndRawBodyMultipart(String[] paramNames,
                                                                   Object[] paramValues) {
//        MultipartEntity multipartEntity = new MultipartEntity();
        MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
        multipartBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (int i = 0; i < paramValues.length; i++)
            if (paramValues[i] != null) {
                if (paramValues[i] instanceof File)
//                  multipartEntity.addPart(paramNames[i], new FileBody((File) paramValues[i]));
                {
                    multipartBuilder.addPart(paramNames[i], new FileBody((File) paramValues[i]));
                } else {
                    try {
//                        multipartEntity.addPart(paramNames[i], new StringBody(paramValues[i].toString()));
                        multipartBuilder.addPart(paramNames[i], new StringBody(
                                paramValues[i].toString()));
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }

//        HttpEntity entity = multipartBuilder.build();

//        String contentType = multipartEntity.getContentType().getValue();
        String contentType = multipartBuilder.build().getContentType().getValue();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
//            multipartEntity.writeTo(baos);
            multipartBuilder.build().writeTo(baos);
        } catch (IOException e) {
        }

        return new Pair<String, byte[]>(contentType, baos.toByteArray());
    }

    @SuppressWarnings("unchecked")
    protected T getResponse(String stringResponse) throws Exception {
        stringResponse = adaptResponse(stringResponse);
        Type parameterType = ((ParameterizedType) (request.getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0];
        if (isTypeOfClass(parameterType, Void.class)) {
            return null;
        }
        if (isTypeOfClass(parameterType, byte[].class)) {
            return (T) stringResponse.getBytes("ISO-8859-1");
        }
        if (isTypeOfClass(parameterType, String.class)) {
            return (T) stringResponse;
        }
        if (isTypeOfClass(parameterType, JSONObject.class)) {
            return (T) new JSONObject(stringResponse);
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
        Gson gson = gsonBuilder.create();
        return gson.fromJson(stringResponse, parameterType);
    }

    protected String adaptResponse(String stringResponse) throws Exception {
        return stringResponse;
    }

    protected boolean isTypeOfClass(Type type, Class<?> clazz) {
        String typeString = type.toString().replace("class ", "");
        String classString = clazz.toString().replace("class ", "");
        return typeString.equals(classString)
                || (classString.matches("\\[.") && typeString.endsWith("[]") && typeString.charAt(
                0) == classString.toLowerCase().charAt(1));
    }

    public void cancelAll() {
        if (mWeakContext == null || mWeakContext.get() == null) {
            return;
        }
        ApiManager.getInstance(mWeakContext.get()).cancelAll();
    }

    public void cancel() {
        volleyRequest.cancel();
    }


    protected boolean showLoadingDialog() {
        return showLoadingDialog;
    }

    @Override
    public void run() {
        initRequest();
        if (mWeakContext == null || mWeakContext.get() == null || volleyRequest == null) {
            return;
        }
        Context context = mWeakContext.get();

        ApiManager.getInstance(context).addToQueue(volleyRequest);
    }

    @Override
    public void onParseResponse(NetworkResponse response) {
        if (response != null && response.data != null) {
            responseBytes = response.data;
        }
    }

    public static class NukeSSLCerts {
        protected static final String TAG = "NukeSSLCerts";

        public static void nuke() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                        return myTrustedAnchors;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            } catch (Exception e) {
            }
        }

    }

}