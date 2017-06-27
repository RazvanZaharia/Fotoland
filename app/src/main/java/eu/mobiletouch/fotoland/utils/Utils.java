package eu.mobiletouch.fotoland.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import com.facebook.share.internal.ShareConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Closeable;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.dao.Dao;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;

/**
 * Created on 28-Aug-16.
 */
public class Utils {
    private static final float INCH_RATIO = 2.54f; // * cm;

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

    public static String getAppFilePath(@NonNull Context ctx) {
       return Environment.getExternalStorageDirectory().getAbsolutePath().concat(File.separator).concat(ctx.getString(R.string.app_name));
    }

    public static float getMinimumPPIofImage(float widthOfImage, float heightOfImage, float cmWidthOfPrint, float cmHeightOfPrint) {
        float widthPpi = widthOfImage / (cmWidthOfPrint / INCH_RATIO);
        float heightPpi = heightOfImage / (cmHeightOfPrint / INCH_RATIO);
        return Math.max(widthPpi, heightPpi);
    }

    public static void closeSilently(@Nullable Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (Throwable t) {
            // Do nothing
        }
    }

    public static int getTotalCartProductsCount(@NonNull Context ctx) {
        ArrayList<UserSelections> cartProducts = Dao.get(ctx).readAllCartProducts();
        return cartProducts.size();
    }

    public static int getTotalPhotosCount(ArrayList<Photo> photos) {
        int photosCount = 0;
        if (photos != null) {
            for (Photo photo : photos) {
                photosCount += photo.getQuantity();
            }
        }
        return photosCount;
    }

    public static Photo setPhotoDimensions(Photo photo) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(photo.getPhotoPath(), options);
        photo.setPhotoWidth(options.outWidth);
        photo.setPhotoHeight(options.outHeight);

        return photo;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static int convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (px / (metrics.densityDpi / 160f));
    }

}
