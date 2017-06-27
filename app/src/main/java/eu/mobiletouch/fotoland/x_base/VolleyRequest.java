package eu.mobiletouch.fotoland.x_base;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class VolleyRequest extends StringRequest {

    private WeakReference<Context> context;
    private Map<String, String> paramsForBody;
    private Map<String, String> mHeaderParams;
    private String bodyContentType;
    private byte[] rawBody;
    private VolleyRequestListener onParseListener;
    private String endpoint;
    private boolean showLoadingDialog;

    @SuppressWarnings("unchecked")
    public VolleyRequest(
            final Context context,
            int method,
            String url,
            String bodyContentType,
            Object bodyData,
            Map<String, String> headers,
            Response.Listener<String> onResponse,
            Response.ErrorListener onError,
            VolleyRequestListener onParseListener,
            final boolean showLoadingDialog,
            String endpoint
    ) {
        super(method, url, onResponse, onError);
        this.context = new WeakReference<>(context);
        this.bodyContentType = bodyContentType;
        if (bodyData instanceof HashMap<?, ?>) {
            paramsForBody = new TreeMap<>((HashMap<String, String>) bodyData);
        } else if (bodyData instanceof byte[])
            rawBody = (byte[]) bodyData;
        this.setTag("tag");
        this.onParseListener = onParseListener;

        this.showLoadingDialog = showLoadingDialog;
        this.endpoint = endpoint;
        this.mHeaderParams = headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return (paramsForBody != null) ? paramsForBody : super.getParams();
    }

    @Override
    public String getBodyContentType() {
        return (bodyContentType != null) ? bodyContentType : super.getBodyContentType();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return (rawBody != null) ? rawBody : super.getBody();
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {

        if (volleyError != null) {
            volleyError.printStackTrace();
        }

        if (volleyError != null && volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            parseNetworkResponse(volleyError.networkResponse);
            return null;
        }

        if (volleyError instanceof NoConnectionError) {
//            showNoInternetDialog();
        }

        if (volleyError instanceof TimeoutError) {
//            showTimeOutDialoag();
        }

        deliverError(volleyError);
        return null;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        if (onParseListener != null) {
            onParseListener.onParseResponse(response);
        }
        return super.parseNetworkResponse(response);
    }

    public boolean showLoadingDialog() {
        return showLoadingDialog;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public interface VolleyRequestListener {
        void onParseResponse(final NetworkResponse response);
    }


}
