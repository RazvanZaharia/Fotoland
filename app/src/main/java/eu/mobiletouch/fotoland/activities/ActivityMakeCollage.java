package eu.mobiletouch.fotoland.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.collage.AdapterCollagePatterns;
import eu.mobiletouch.fotoland.collage.DragListener;
import eu.mobiletouch.fotoland.collage.PhotoView;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.mvps.MvpActivityMakeCollage;
import eu.mobiletouch.fotoland.presenters.PresenterActivityMakeCollage;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.x_base.BaseSaveToolbarActivity;

import static eu.mobiletouch.fotoland.utils.Constants.SELECTED_PHOTOS;

/**
 * Created on 05-Oct-16.
 */
public class ActivityMakeCollage extends BaseSaveToolbarActivity implements MvpActivityMakeCollage {

    @Bind(R.id.layout_collage)
    FrameLayout mLayoutCollage;
    @Bind(R.id.rv_collage_patterns)
    RecyclerView mRvCollagePatters;

    private PresenterActivityMakeCollage mPresenterActivityMakeCollage;

    public static void launchForResult(@NonNull Activity activity, int requestCode) {
        launchForResult(activity, new ArrayList<Photo>(), requestCode);
    }

    public static void launchForResult(@NonNull Activity activity, @Nullable ArrayList<Photo> preselectedPhotos, int requestCode) {
        activity.startActivityForResult(getIntent(activity, preselectedPhotos), requestCode);
    }

    public static void launchForResult(@NonNull Activity activity, @Nullable Photo preselectedPhoto, int requestCode) {
        ArrayList<Photo> preselectedPhotos = null;
        if (preselectedPhoto != null) {
            preselectedPhotos = new ArrayList<>();
            preselectedPhotos.add(preselectedPhoto);
        }
        activity.startActivityForResult(getIntent(activity, preselectedPhotos), requestCode);
    }

    public static Intent getIntent(@NonNull Context ctx, @Nullable ArrayList<Photo> preselectedPhotos) {
        Intent createCollageIntent = new Intent(ctx, ActivityMakeCollage.class);
        createCollageIntent.putExtra(Constants.SELECTED_PHOTOS, preselectedPhotos);
        return createCollageIntent;
    }

    @Override
    protected boolean showCartIcon() {
        return false;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_make_collage;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.screenTitle_makeCollage);
    }

    @Override
    protected void onSaveMenuItemAction() {
        mPresenterActivityMakeCollage.onSaveMenuClick();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void init() {
        super.init();
        mRvCollagePatters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mPresenterActivityMakeCollage = new PresenterActivityMakeCollage(this);
        mPresenterActivityMakeCollage.attachView(this);
        mPresenterActivityMakeCollage.init((ArrayList<Photo>) getIntent().getSerializableExtra(SELECTED_PHOTOS));
    }

    @Override
    public void setCollagePatterns(@NonNull List<Integer> ids) {
        mRvCollagePatters.setAdapter(new AdapterCollagePatterns(ids, mPresenterActivityMakeCollage));
    }

    @Override
    public void addThisCollagePatternLayout(int idOfCollagePatternLayout) {
        mLayoutCollage.removeAllViewsInLayout();
        ViewGroup collagePatternView = (ViewGroup) LayoutInflater.from(this).inflate(idOfCollagePatternLayout, mLayoutCollage, false);
        setPhotoViewsActions(collagePatternView);
        mLayoutCollage.addView(collagePatternView);
    }

    private void setPhotoViewsActions(@NonNull ViewGroup collagePatternViewGroup) {
        for (int i = 0; i < collagePatternViewGroup.getChildCount(); i++) {
            View child = collagePatternViewGroup.getChildAt(i);
            if (child instanceof PhotoView) {
                PhotoView photoView = (PhotoView) child;
                mPresenterActivityMakeCollage.configurePhotoView(photoView);
                photoView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                        view.startDrag(data, shadowBuilder, view, 0);
                        view.setVisibility(View.INVISIBLE);
                        return true;
                    }
                });

                ViewGroup parent = ((ViewGroup) photoView.getParent());
                parent.setOnDragListener(new DragListener());

            } else {
                if (child instanceof ViewGroup) {
                    setPhotoViewsActions((ViewGroup) child);
                }
            }
        }
    }

    @Override
    public void selectSinglePicture() {
        startActivityForResult(ActivitySelectPhotos.getStartIntentForSinglePictureSelect(this), Constants.REQUEST_CODE_SELECT_SINGLE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SELECT_SINGLE_PICTURE && resultCode == RESULT_OK) {
            mPresenterActivityMakeCollage.onSinglePictureSelected((Photo) data.getSerializableExtra(Constants.PHOTO));
        }
    }

    @Override
    public Bitmap getCollageBitmap() {
        this.mLayoutCollage.destroyDrawingCache();
        this.mLayoutCollage.setDrawingCacheEnabled(true);
        return this.mLayoutCollage.getDrawingCache();
    }

    @Override
    public void setResultAndFinish(int resultCode, @NonNull Intent data) {
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void finishAllProductScreens() {
        closeSelectProductsActivities();
    }
}
