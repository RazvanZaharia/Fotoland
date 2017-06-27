package eu.mobiletouch.fotoland.x_base;

import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.activities.ActivityCart;
import eu.mobiletouch.fotoland.utils.Utils;

public abstract class BaseToolbarActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mTvScreenTitle;

    @Override
    protected abstract int getLayoutResId();

    @Override
    protected void onResume() {
        super.onResume();
        setToolbarBadge();
    }

    @Override
    protected void linkUI() {
        super.linkUI();
        setupToolbar();
        setupTitle();
    }

    protected void setupTitle() {
        mTvScreenTitle = (TextView) findViewById(R.id.tv_screen_title);
        if (mTvScreenTitle != null && getScreenName() != null) {
            mTvScreenTitle.setText(getScreenName());
        }
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

    private void setToolbarBadge() {
        if (mToolbar.findViewById(R.id.layout_toolbar_cart) != null && showCartIcon()) {
            mToolbar.findViewById(R.id.layout_toolbar_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCart.launch(BaseToolbarActivity.this);
                }
            });

            if (mToolbar.findViewById(R.id.tv_toolbar_cartBadge) != null) {
                int cartProductsCount = Utils.getTotalCartProductsCount(this);
                if (cartProductsCount == 0) {
                    mToolbar.findViewById(R.id.layout_toolbar_cart).setVisibility(View.GONE);
                } else {
                    mToolbar.findViewById(R.id.layout_toolbar_cart).setVisibility(View.VISIBLE);
                    ((TextView) mToolbar.findViewById(R.id.tv_toolbar_cartBadge)).setText(String.valueOf(Utils.getTotalCartProductsCount(this)));
                }
            }
        }
    }

    protected boolean showCartIcon() {
        return true;
    }

    protected void onNavigationClick() {
        onBackPressed();
    }

    @Nullable
    protected Toolbar getToolbar() {
        return mToolbar;
    }

}
