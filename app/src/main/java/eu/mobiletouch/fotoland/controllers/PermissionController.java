package eu.mobiletouch.fotoland.controllers;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import eu.mobiletouch.fotoland.interfaces.PermissionCallback;
import eu.mobiletouch.fotoland.utils.PermissionUtils;


public class PermissionController {


    private Activity activity;
    private PermissionCallback callback;
    private Object mData;

    public PermissionController(@NonNull final Activity activity, @NonNull final PermissionCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    public void setCallback(@NonNull final PermissionCallback callback) {
        this.callback = callback;
    }

    public PermissionCallback getCallback() {
        return callback;
    }

    public void askForPermission(@NonNull final String permission) {
        askForPermission(permission, null);
    }

    public void askForPermission(@NonNull final String permission, final int detailsTextID) {
        askForPermission(permission, activity.getResources().getString(detailsTextID));
    }

    public void askForPermission(@NonNull final String permission, final int detailsTextID, final Object data) {
        askForPermission(permission, activity.getResources().getString(detailsTextID), data);
    }

    public void askForPermission(@NonNull final String permission, final String detailsText) {
        askForPermission(permission, detailsText, null);
    }
    public void askForPermission(@NonNull final String permission, final String detailsText, final Object data) {
        mData = data;
        int state = ActivityCompat.checkSelfPermission(activity, permission);
        switch (state) {
            case PackageManager.PERMISSION_GRANTED:
                callback.onPermissionGranted(PermissionUtils.getCodeForPermission(permission));
                callback.onPermissionGranted(PermissionUtils.getCodeForPermission(permission), data);
                break;
            case PackageManager.PERMISSION_DENIED:
            default:
                requestPermission(permission, detailsText);
                break;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionGranted(requestCode);
            callback.onPermissionGranted(requestCode, mData);
        } else {
            callback.onPermissionDenied(requestCode);
        }
    }

    private void requestPermission(@NonNull final String permission, final String detailsText) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, PermissionUtils.getCodeForPermission(permission));
            }
        };
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            displayPemissionDialog(runnable, permission, detailsText);
        } else {
            runnable.run();
        }
    }

    private void displayPemissionDialog(final Runnable runnable, final String permission, final String detailsText) {
        if (runnable != null && !TextUtils.isEmpty(permission)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            if (TextUtils.isEmpty(detailsText)) {
                final String details = PermissionUtils.getDetailsForPermission(activity, permission);
                builder.setMessage(details);
            } else {
                builder.setMessage(detailsText);
            }
            builder.setCancelable(false);
            builder.setPositiveButton(activity.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    runnable.run();
                }
            });
            builder.show();
        }
    }

}
