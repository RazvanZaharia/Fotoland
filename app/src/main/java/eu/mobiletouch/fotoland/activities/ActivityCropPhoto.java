package eu.mobiletouch.fotoland.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.mvps.MvpActivityCropPhoto;
import eu.mobiletouch.fotoland.presenters.PresenterActivityCropPhoto;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.x_base.BaseSaveToolbarActivity;

public class ActivityCropPhoto extends BaseSaveToolbarActivity implements MvpActivityCropPhoto {

    @Bind(R.id.iv_image_to_crop)
    CropImageView ivImageToCrop;

    private LoadCallback mLoadCallback;
    private SaveCallback mSaveCallback;

    private PresenterActivityCropPhoto mPresenterActivityCropPhoto;

    public static void launch(@NonNull Activity activity, @NonNull Item item, @NonNull Photo photo, int requestCode) {
        Intent cropPhotoIntent = new Intent(activity, ActivityCropPhoto.class);
        cropPhotoIntent.putExtra(Constants.PHOTO, photo);
        cropPhotoIntent.putExtra(Constants.ITEM, item);
        activity.startActivityForResult(cropPhotoIntent, requestCode);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_crop_photo;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.screenTitle_cropActivity);
    }

    @Override
    protected boolean showCartIcon() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        initCropImageCallbacks();

        mPresenterActivityCropPhoto = new PresenterActivityCropPhoto(this);
        mPresenterActivityCropPhoto.attachView(this);
        mPresenterActivityCropPhoto.init(getIntent());
    }

    private void initCropImageCallbacks() {
        mLoadCallback = new LoadCallback() {
            @Override
            public void onSuccess() {
                dismissLoading();
                mPresenterActivityCropPhoto.onSuccessLoad();
            }

            @Override
            public void onError() {
                dismissLoading();
                finishWithResult(null);
            }
        };

        mSaveCallback = new SaveCallback() {
            @Override
            public void onSuccess(Uri outputUri) {
                dismissLoading();
                mPresenterActivityCropPhoto.onSuccessSave(outputUri);
            }

            @Override
            public void onError() {
                dismissLoading();
                finishWithResult(null);
            }
        };
    }

    @Override
    protected void onSaveMenuItemAction() {
        showLoading();
        ivImageToCrop.startCrop(mPresenterActivityCropPhoto.getCropSaveUri(), null, mSaveCallback);
    }

    @Override
    protected void onDestroy() {
        mLoadCallback = null;
        mSaveCallback = null;
        super.onDestroy();
    }

    @Override
    public void loadPhoto(@NonNull Uri photoUri) {
        showLoading();
        ivImageToCrop.startLoad(photoUri, mLoadCallback);
    }

    @Override
    public void setCustomRatio(int ratioX, int ratioY) {
        ivImageToCrop.setCustomRatio(ratioX, ratioY);
    }

    @Override
    public void rotate(int radius) {
        switch (radius) {
            case 90:
                ivImageToCrop.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                break;
            case 180:
                ivImageToCrop.rotateImage(CropImageView.RotateDegrees.ROTATE_180D);
                break;
            case 270:
                ivImageToCrop.rotateImage(CropImageView.RotateDegrees.ROTATE_270D);
                break;
        }
    }

    @Override
    public void finishWithResult(Uri uri) {
        if (uri == null) {
            setResult(RESULT_CANCELED);
        } else {
            setResult(RESULT_OK, new Intent().setData(uri));
        }
        finish();
    }

}


