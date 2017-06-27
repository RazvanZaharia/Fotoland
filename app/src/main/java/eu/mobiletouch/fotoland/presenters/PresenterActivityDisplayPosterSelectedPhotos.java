package eu.mobiletouch.fotoland.presenters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.activities.ActivityCart;
import eu.mobiletouch.fotoland.collage.PhotoView;
import eu.mobiletouch.fotoland.dao.Dao;
import eu.mobiletouch.fotoland.enums.ItemType;
import eu.mobiletouch.fotoland.enums.Orientation;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.utils.Utils;

/**
 * Created on 09-Jan-17.
 */
public class PresenterActivityDisplayPosterSelectedPhotos extends PresenterActivityMakeCollage {

    private UserSelections mUserSelections;

    public PresenterActivityDisplayPosterSelectedPhotos(@NonNull Context ctx) {
        super(ctx);
    }

    public void init(@NonNull UserSelections userSelections) {
        mUserSelections = userSelections;
        init(userSelections.getSelectedPhotos());
    }

    @Override
    public void init(@NonNull ArrayList<Photo> preselectedPhotos) {
        this.mAddedPhotos = preselectedPhotos;
        mConfiguredPhotoViewIndex = 0;
        mPhotoViews = new ArrayList<>();
        switch (mAddedPhotos.size()) {
            case 1:
                switch (mUserSelections.getSelectedItem().getItemType()) {
                    case SQUARE:
                        getMvpView().addThisCollagePatternLayout(R.layout.poster_classic_square);
                        break;
                    case REGULAR:
                        getMvpView().addThisCollagePatternLayout(R.layout.poster_classic_regular);
                        break;
                }
                break;
            case 24:
                getMvpView().addThisCollagePatternLayout(R.layout.poster_mosaic_portrait_24);
                break;
        }
    }

    @Override
    public void onSaveMenuClick() {
        Bitmap posterBitmap = getMvpView().getCollageBitmap();
        FileOutputStream out = null;
        Intent resultData = new Intent();
        try {
            File toSaveFile = getToSaveFile();
            out = new FileOutputStream(toSaveFile);
            posterBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            posterBitmap.recycle();
            posterBitmap = null;

            Photo posterPhoto = new Photo();
            posterPhoto.setPhotoPath(toSaveFile.getAbsolutePath());
            posterPhoto.setId(new Random().nextInt());
            posterPhoto = Utils.setPhotoDimensions(posterPhoto);

            mUserSelections.setPosterPhoto(posterPhoto);


            mUserSelections.setPhotosCount(Utils.getTotalPhotosCount(mUserSelections.getSelectedPhotos()));
            Dao.get(mCtx).saveOneCartProduct(mUserSelections);
            ActivityCart.launch(mCtx);
            getMvpView().finishAllProductScreens();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void configurePhotoView(@NonNull PhotoView photoView) {
        photoView.setCenterCropScaleType(true);
        photoView.setMaxInitialScale(50);
        photoView.setFullScreen(true, true);
        photoView.enableImageTransforms(true);
        photoView.setOnClickListener(this);

        if (mAddedPhotos.size() > mConfiguredPhotoViewIndex) {
            loadPhoto(photoView, mAddedPhotos.get(mConfiguredPhotoViewIndex));
        } else {
            if (mAddedPhotos != null && mAddedPhotos.size() > mConfiguredPhotoViewIndex) {
                mAddedPhotos.add(mAddedPhotos.get(mConfiguredPhotoViewIndex));
                loadPhoto(photoView, mAddedPhotos.get(mConfiguredPhotoViewIndex));
            } else {
                mAddedPhotos.add(new Photo());
            }
        }

        if(mAddedPhotos.size() == 1 && mUserSelections.getSelectedItem().getItemType() == ItemType.REGULAR) {
            View photoParent = (View) photoView.getParent();
            PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) photoParent.getLayoutParams();

            PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
            if(mAddedPhotos.get(0).getOrientation() == Orientation.LANDSCAPE) {
                info.aspectRatio = 1.50f;
            }
            else {
                info.aspectRatio = 0.66f;
            }

            photoParent.requestLayout();
        }

        mPhotoViews.add(photoView);

        mConfiguredPhotoViewIndex += 1;
    }

}
