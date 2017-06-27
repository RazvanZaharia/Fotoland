package eu.mobiletouch.fotoland.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterVpEditPage;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.mvps.MvpActivityEditPagePhotobook;
import eu.mobiletouch.fotoland.presenters.PresenterActivityEditPagePhotobook;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.x_base.BaseSaveToolbarActivity;

/**
 * Created on 31-Oct-16.
 */
public class ActivityEditPagePhotobook extends BaseSaveToolbarActivity implements MvpActivityEditPagePhotobook {

    @Bind(R.id.vp_pages)
    ViewPager mVpPages;
    @Bind(R.id.btn_remove)
    Button mBtnRemove;
    @Bind(R.id.btn_caption)
    Button mBtnCaption;
    @Bind(R.id.btn_collage)
    Button mBtnCollage;

    private PresenterActivityEditPagePhotobook mPresenterActivityEditPagePhotobook;

    public static void launchForResult(@NonNull Activity activity, @NonNull ArrayList<Photo> photobookPhotos, int selectedIndex, int requestCode) {
        activity.startActivityForResult(getStartIntent(activity, photobookPhotos, selectedIndex), requestCode);
    }

    public static Intent getStartIntent(@NonNull Context ctx, @NonNull ArrayList<Photo> photobookPhotos, int selectedIndex) {
        Intent editPagesIntent = new Intent(ctx, ActivityEditPagePhotobook.class);
        editPagesIntent.putExtra(Constants.SELECTED_PHOTOS, photobookPhotos);
        editPagesIntent.putExtra(Constants.INDEX_OF_PHOTO, selectedIndex);
        return editPagesIntent;
    }

    @Override
    protected void onSaveMenuItemAction() {
        mPresenterActivityEditPagePhotobook.onSaveMenuItemAction();
    }

    @Override
    protected boolean showCartIcon() {
        return false;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_page_photobook;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        mPresenterActivityEditPagePhotobook = new PresenterActivityEditPagePhotobook();
        mPresenterActivityEditPagePhotobook.attachView(this);
        ArrayList<Photo> dataSet = (ArrayList<Photo>) getIntent().getSerializableExtra(Constants.SELECTED_PHOTOS);
        int selectedIndex = getIntent().getIntExtra(Constants.INDEX_OF_PHOTO, 0);
        mPresenterActivityEditPagePhotobook.init(this, dataSet, selectedIndex);
    }

    @Override
    protected void setActions() {
        super.setActions();
        mBtnCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterActivityEditPagePhotobook.onAddCaption(mVpPages.getCurrentItem());
            }
        });

        mBtnCollage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterActivityEditPagePhotobook.onMakeCollageClick(mVpPages.getCurrentItem());
            }
        });
        mBtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterActivityEditPagePhotobook.onRemovePhoto(mVpPages.getCurrentItem());
            }
        });
    }

    @Override
    public void showPhotos(@NonNull ArrayList<Photo> dataSet, int selectedIndex) {
        AdapterVpEditPage adapterVpEditPage = new AdapterVpEditPage(dataSet, getSupportFragmentManager());
        mVpPages.setAdapter(adapterVpEditPage);
        mVpPages.setCurrentItem(selectedIndex);
    }

    @Override
    public void showDialog(DialogFragment dialogFragment) {
        dialogFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenterActivityEditPagePhotobook.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setResultAndFinish(@NonNull Intent resultIntent, int resultCode) {
        setResult(resultCode, resultIntent);
        finish();
    }
}
