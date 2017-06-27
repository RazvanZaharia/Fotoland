package eu.mobiletouch.fotoland.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.collage.DragListener;
import eu.mobiletouch.fotoland.collage.PhotoView;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.mvps.MvpActivityDisplayPosterSelectedPhotos;
import eu.mobiletouch.fotoland.presenters.PresenterActivityDisplayPosterSelectedPhotos;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.x_base.BaseSaveToolbarActivity;

/**
 * Created on 04-Jan-17.
 */
public class ActivityDisplayPosterSelectedPhotos extends BaseSaveToolbarActivity implements MvpActivityDisplayPosterSelectedPhotos {

    @Bind(R.id.layout_poster)
    FrameLayout mLayoutPoster;

    private UserSelections mUserSelections;
    private PresenterActivityDisplayPosterSelectedPhotos mPresenterActivityMakePoster;

    public static void launch(@NonNull Context context, @NonNull UserSelections userSelections) {
        Intent displaySelectionIntent = new Intent(context, ActivityDisplayPosterSelectedPhotos.class);
        displaySelectionIntent.putExtra(Constants.USER_SELECTION, userSelections);
        context.startActivity(displaySelectionIntent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_display_poster_selection;
    }

    @Override
    protected void onSaveMenuItemAction() {
        mPresenterActivityMakePoster.onSaveMenuClick();
    }

    @Override
    protected boolean showCartIcon() {
        return false;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.screenTitle_poster);
    }

    @Override
    protected void init() {
        super.init();
        mUserSelections = (UserSelections) getIntent().getSerializableExtra(Constants.USER_SELECTION);

        mPresenterActivityMakePoster = new PresenterActivityDisplayPosterSelectedPhotos(this);
        mPresenterActivityMakePoster.attachView(this);
        mPresenterActivityMakePoster.init(mUserSelections);
    }

    @Override
    public void setCollagePatterns(@NonNull List<Integer> ids) {}

    @Override
    public void addThisCollagePatternLayout(int idOfCollagePatternLayout) {
        mLayoutPoster.removeAllViewsInLayout();
        ViewGroup collagePatternView = (ViewGroup) LayoutInflater.from(this).inflate(idOfCollagePatternLayout, mLayoutPoster, false);
        setPhotoViewsActions(collagePatternView);
        mLayoutPoster.addView(collagePatternView);
    }

    private void setPhotoViewsActions(@NonNull ViewGroup collagePatternViewGroup) {
        for (int i = 0; i < collagePatternViewGroup.getChildCount(); i++) {
            View child = collagePatternViewGroup.getChildAt(i);
            if (child instanceof PhotoView) {
                PhotoView photoView = (PhotoView) child;
                mPresenterActivityMakePoster.configurePhotoView(photoView);
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
    public Bitmap getCollageBitmap() {
        this.mLayoutPoster.destroyDrawingCache();
        this.mLayoutPoster.setDrawingCacheEnabled(true);
        return this.mLayoutPoster.getDrawingCache();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SELECT_SINGLE_PICTURE && resultCode == RESULT_OK) {
            mPresenterActivityMakePoster.onSinglePictureSelected((Photo) data.getSerializableExtra(Constants.PHOTO));
        }
    }

    @Override
    public void setResultAndFinish(int resultCode, Intent data) {}

    @Override
    public void finishAllProductScreens() {
        closeSelectProductsActivities();
    }
}
