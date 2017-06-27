package eu.mobiletouch.fotoland.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterVpPhotos;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.interfaces.OnBackPressedListener;
import eu.mobiletouch.fotoland.interfaces.OnSelectPhotosFragmentsListener;
import eu.mobiletouch.fotoland.mvps.MvpActivitySelectPhotos;
import eu.mobiletouch.fotoland.presenters.PresenterActivitySelectPhotos;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.x_base.BaseToolbarActivity;

/**
 * Created on 27-Aug-16.
 */
public class ActivitySelectPhotos extends BaseToolbarActivity implements MvpActivitySelectPhotos, OnSelectPhotosFragmentsListener {

    @Bind(R.id.tv_screen_title)
    TextView mTvScreenTitle;

    @Bind(R.id.vp_photos)
    ViewPager mVpPhotos;
    @Bind(R.id.tab_photos)
    TabLayout mTabPhotos;
    @Bind(R.id.btn_next)
    Button mBtnNext;

    private PresenterActivitySelectPhotos mPresenterActivitySelectPhotos;
    private AdapterVpPhotos mAdapterVpPhotos;

    public static void launch(@NonNull Context ctx, @NonNull UserSelections userSelections) {
        Intent selectPhotosIntent = new Intent(ctx, ActivitySelectPhotos.class);
        selectPhotosIntent.putExtra(Constants.USER_SELECTION, userSelections);
        selectPhotosIntent.putExtra(Constants.ARG_SINGLE_PHOTO_SELECT, false);
        ctx.startActivity(selectPhotosIntent);
    }

    public static Intent getStartIntentForSinglePictureSelect(@NonNull Context ctx) {
        Intent selectPhotosIntent = new Intent(ctx, ActivitySelectPhotos.class);
        selectPhotosIntent.putExtra(Constants.ARG_SINGLE_PHOTO_SELECT, true);
        return selectPhotosIntent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_photos;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected boolean isProductScreen() {
        return true;
    }

    @Override
    protected boolean showCartIcon() {
        return false;
    }

    @Override
    protected void init() {
        super.init();

        mPresenterActivitySelectPhotos = new PresenterActivitySelectPhotos(this);
        mPresenterActivitySelectPhotos.attachView(this);
        if (getIntent().getBooleanExtra(Constants.ARG_SINGLE_PHOTO_SELECT, false)) {
            mBtnNext.setVisibility(View.INVISIBLE);
            mPresenterActivitySelectPhotos.initForSinglePictureSelect();
        } else {
            mBtnNext.setVisibility(View.VISIBLE);
            mPresenterActivitySelectPhotos.init((UserSelections) getIntent().getSerializableExtra(Constants.USER_SELECTION));
        }
    }

    @Override
    protected void setActions() {
        super.setActions();

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterActivitySelectPhotos.onNextClick();
            }
        });
    }

    private Fragment getCurrentFragment() {
        Fragment fragment = null;
        if (mAdapterVpPhotos != null) {
            fragment = mAdapterVpPhotos.getFragmentAt(mVpPhotos.getCurrentItem());
        }
        return fragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment fragment = getCurrentFragment();
        if (fragment != null) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void showPhotosFragments() {
        mAdapterVpPhotos = new AdapterVpPhotos(this, getSupportFragmentManager());
        mVpPhotos.setAdapter(mAdapterVpPhotos);
        mTabPhotos.setupWithViewPager(mVpPhotos);
    }

    @Override
    public void setScreenTitle(@NonNull String screenTitle) {
        mTvScreenTitle.setText(screenTitle);
    }

    @Override
    public void enableNextButton(boolean enable) {
        mBtnNext.setEnabled(enable);
    }

    @Override
    public void showLoadingDialog() {
        showLoading();
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoading();
    }

    @Override
    public void setResultAndFinish(@NonNull Intent data) {
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void showToastMessage(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /*
    *
    * OnSelectPhotosFragmentsListener
    *
    * */

    @Override
    public void onPhotoClick(Photo photo) {
        mPresenterActivitySelectPhotos.onPhotoClick(photo);
    }

    @Override
    public boolean isSelected(Photo photo) {
        return mPresenterActivitySelectPhotos.isSelected(photo);
    }

    /*
    *
    **/

    @Override
    public void onBackPressed() {
        Fragment fragment = getCurrentFragment();
        if (!(fragment instanceof OnBackPressedListener && ((OnBackPressedListener) fragment).onBackPressed())) {
            if (mPresenterActivitySelectPhotos.arePhotosSelected()) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_alertDialog_onBackPressed_emptyYourSelection)
                        .setMessage(R.string.message_alertDialog_onBackPressed_emptyYourSelection)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivitySelectPhotos.super.onBackPressed();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                super.onBackPressed();
            }
        }
    }
}
