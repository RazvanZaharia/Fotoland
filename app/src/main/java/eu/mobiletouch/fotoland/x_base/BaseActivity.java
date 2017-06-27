package eu.mobiletouch.fotoland.x_base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import eu.mobiletouch.fotoland.dialogs.DialogManager;

/**
 * Created on 09-Aug-16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected final static String FINISH_ALL = "finish-all";
    protected final static String FINISH_SELECT_PRODUCT_SCREEN = "finish-selectProductsScreens";

    protected View mContentView;
    protected LayoutInflater mLayoutInflater;
    protected ProgressDialog mProgressDialog;

    protected BaseActivityReceiver baseActivityReceiver = new BaseActivityReceiver();
    protected BaseSelectProductReceiver mBaseSelectProductReceiver = new BaseSelectProductReceiver();

    abstract protected int getLayoutResId();

    abstract protected String getScreenName();

    protected boolean isProductScreen() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLayoutResId() == View.NO_ID) {
            return;
        }

        mLayoutInflater = getLayoutInflater();
        mContentView = mLayoutInflater.inflate(getLayoutResId(), null);

        setContentView(mContentView);

        linkUI();
        init();
        setData();
        setActions();
    }

    protected void linkUI() {
        ButterKnife.bind(this);
    }

    protected void init() {
        registerReceiver(baseActivityReceiver, createIntentFilter());
        if (isProductScreen()) {
            registerReceiver(mBaseSelectProductReceiver, createIntentSelectProductFilter());
        }
    }

    protected void setData() {
    }

    protected void setActions() {
    }

    protected void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = DialogManager.createProgressDialog(this);
        } else {
            mProgressDialog.show();
        }
    }

    protected void dismissLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(baseActivityReceiver);
        if (isProductScreen()) {
            unregisterReceiver(mBaseSelectProductReceiver);
        }
        super.onDestroy();
    }

    protected final void closeAllActivities() {
        sendBroadcast(new Intent(FINISH_ALL));
    }

    protected final void closeSelectProductsActivities() {
        sendBroadcast(new Intent(FINISH_SELECT_PRODUCT_SCREEN));
    }

    protected IntentFilter createIntentSelectProductFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(FINISH_SELECT_PRODUCT_SCREEN);
        return filter;
    }

    public class BaseSelectProductReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(FINISH_SELECT_PRODUCT_SCREEN)) {
                Log.v("FINISH", "finish from + " + BaseActivity.this.getClass().getCanonicalName());
                finish();
            }
        }
    }

    protected IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(FINISH_ALL);
        return filter;
    }

    public class BaseActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(FINISH_ALL)) {
                Log.v("FINISH", "finish from + " + BaseActivity.this.getClass().getCanonicalName());
                finish();
            }
        }
    }

}
