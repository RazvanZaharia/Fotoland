package eu.mobiletouch.fotoland.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.facebook.share.internal.ShareConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.text.MessageFormat;

/**
 * Created on 28-Aug-16.
 */
public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getFacebookPictureUrl(String id, String token) {
        return MessageFormat.format("https://graph.facebook.com/v2.7/{0}/picture?redirect=true&access_token={1}", new Object[]{id, token});
    }

    public static String getBestMatchImage(JSONArray imagesData, int preferredSize) throws JSONException {
//        long selectImageSizeDiff = 1000000;
        String selectedImage = imagesData.getJSONObject(0).getString(ShareConstants.FEED_SOURCE_PARAM);
        /*for (int i = 0; i < imagesData.length(); i++) {
            JSONObject imageData = imagesData.getJSONObject(i);
            int diff = Math.abs((preferredSize * preferredSize) - (imageData.optInt("width", 0) * imageData.optInt("height", 0)));
            if (diff < selectImageSizeDiff) {
                selectImageSizeDiff = diff;
                selectedImage = imageData.getString(ShareConstants.FEED_SOURCE_PARAM);
            }
        }*/
        return selectedImage;
    }

    @NonNull
    public static Uri createCropSaveUri(@NonNull Context ctx, @NonNull String currentFileName) {
        return Uri.fromFile(new File(ctx.getApplicationContext().getCacheDir(), "cropped_".concat(currentFileName)));
    }

}
