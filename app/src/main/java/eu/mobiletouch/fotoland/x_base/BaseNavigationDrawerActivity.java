package eu.mobiletouch.fotoland.x_base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import eu.mobiletouch.fotoland.R;

/**
 * Created on 08-Aug-16.
 */
public abstract class BaseNavigationDrawerActivity extends BaseActivity {

    protected DrawerLayout navigationDrawerParentLayout;
    protected NavigationView navigationMenuView;
    protected NavigationView parentNavigationView;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected FrameLayout contentFrameLayout;
    protected Toolbar toolbar;

    abstract protected String getScreenName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        setupToolbar();
        setContentView(navigationDrawerParentLayout);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        final int id = item.getItemId();
        switch (id) {
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void initViews() {
        navigationDrawerParentLayout = (DrawerLayout) mLayoutInflater.inflate(R.layout.nav_drawer_layout, null, false);
        navigationMenuView = (NavigationView) navigationDrawerParentLayout.findViewById(R.id.navigation_view);
        navigationMenuView.setItemIconTintList(null);
        parentNavigationView = (NavigationView) navigationDrawerParentLayout.findViewById(R.id.parent_navigation_view);
        contentFrameLayout = (FrameLayout) navigationDrawerParentLayout.findViewById(R.id.content_frame_layout);
        toolbar = (Toolbar) navigationDrawerParentLayout.findViewById(R.id.toolbar);

        ViewGroup bigParent = (ViewGroup) mContentView.getParent();
        if (bigParent != null) {
            bigParent.removeAllViewsInLayout();
        }
        contentFrameLayout.addView(mContentView, 0);

        navigationDrawerParentLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                navigationDrawerParentLayout,
                toolbar,
                R.string.label_open,
                R.string.label_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        navigationDrawerParentLayout.setDrawerListener(mDrawerToggle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        setupNavigationActions();
    }

    private void setupNavigationActions() {
        navigationMenuView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final int itemId = item.getItemId();

                switch (itemId) {
                    case R.id.nav_menu_item_myAccount:
                    case R.id.nav_menu_item_home:
                    case R.id.nav_menu_item_myOrders:
                    case R.id.nav_menu_item_prices:
                    case R.id.nav_menu_item_faq:
                    case R.id.nav_menu_item_contact:
                }
                return true;
            }
        });
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayUseLogoEnabled(false);
            }
            setToolbarActions();
        }
    }

    private void setToolbarActions() {
        if (!TextUtils.isEmpty(getScreenName())) {
            ((TextView) toolbar.findViewById(R.id.tv_screenName)).setText(getScreenName());
        }
    }

    protected void toggleDrawer() {
        if (navigationDrawerParentLayout.isDrawerOpen(Gravity.LEFT)) {
            navigationDrawerParentLayout.closeDrawer(Gravity.LEFT);
        } else {
            navigationDrawerParentLayout.openDrawer(Gravity.LEFT);
        }
    }

}
