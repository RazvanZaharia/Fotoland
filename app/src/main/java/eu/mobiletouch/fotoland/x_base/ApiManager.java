package eu.mobiletouch.fotoland.x_base;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApiManager {
    private static final String TAG = "requests";
    private static ApiManager instance;
    private RequestQueue requestQueue;

    private ApiManager(Context context) {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static synchronized ApiManager getInstance(Context context) {
        if (instance == null) {
            instance = new ApiManager(context.getApplicationContext());
        }
        return instance;
    }

    public void addToQueue(VolleyRequest request) {
        request.setTag(TAG);
        requestQueue.add(request);
    }

    public void cancelAll() {
        this.requestQueue.cancelAll(TAG);
    }

    /*public void onRequestEnd(Context context, boolean showLoadingDialog) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).requestFinished(showLoadingDialog);
        }
    }*/

//    public void onActivityDestroyed(Context context) {
//        dismissLoadingDialog(context);
//    }


    /*public void dismissLoadingDialog(Context context) {
        if (showLoadingDialog) {
            try {
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).removeLoading();
                } else {
                    if (context instanceof BaseActivityNavigationDrawer) {
                        ((BaseActivityNavigationDrawer) context).removeLoading();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
}
