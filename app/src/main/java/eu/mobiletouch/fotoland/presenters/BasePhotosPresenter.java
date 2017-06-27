package eu.mobiletouch.fotoland.presenters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import eu.mobiletouch.fotoland.mvps.BaseMvp;

/**
 * Created on 21-Sep-16.
 */
public class BasePhotosPresenter<T extends BaseMvp> extends BasePresenter<T> {
    private ProgressDialog mLoadingDialog;

    protected void showLoading(@NonNull Context context) {
        mLoadingDialog = ProgressDialog.show(context, "", "", true);
    }

    protected void dismissLoading() {
        if (mLoadingDialog != null) {
            try {
                mLoadingDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
