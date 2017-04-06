package eu.mobiletouch.fotoland.x_base;

import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import eu.mobiletouch.fotoland.R;

public abstract class BaseToolbarActivity extends BaseActivity {

    private Toolbar mToolbar;

    @Override
    protected abstract int getLayoutResId();

    @Override
    protected void linkUI() {
        super.linkUI();
        setupToolbar();
    }

    protected void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.ic_back_support);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNavigationClick();
                }
            });
        }
    }

    protected void onNavigationClick() {
        onBackPressed();
    }


    @Nullable
    protected Toolbar getToolbar() {
        return mToolbar;
    }

}
