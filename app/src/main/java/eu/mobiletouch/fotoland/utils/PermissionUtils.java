package eu.mobiletouch.fotoland.utils;

import android.Manifest;
import android.content.Context;
import android.text.TextUtils;

import eu.mobiletouch.fotoland.R;

public class PermissionUtils {

    public static final int PERMISSION_CODE_STORAGE = 0;
    public static final int PERMISSION_CODE_CONTACTS = 1;

    public static int getCodeForPermission(final String permission) {
        int result = -1;
        if (!TextUtils.isEmpty(permission)) {
            switch (permission) {
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                case Manifest.permission.READ_EXTERNAL_STORAGE:
                    result = PERMISSION_CODE_STORAGE;
                    break;
                case Manifest.permission.GET_ACCOUNTS:
                    result = PERMISSION_CODE_CONTACTS;
                    break;
            }
        }
        return result;
    }

    public static String getDetailsForPermission(final Context context, final String permission) {
        String result = "";
        if (!TextUtils.isEmpty(permission)) {
            switch (permission) {
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                case Manifest.permission.READ_EXTERNAL_STORAGE:
                    result = context.getResources().getString(R.string.permission_details_external_storage);
                    break;
            }
        }
        return result;
    }

}
