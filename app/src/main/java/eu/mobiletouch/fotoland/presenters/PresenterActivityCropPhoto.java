package eu.mobiletouch.fotoland.presenters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.mvps.MvpActivityCropPhoto;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.utils.Utils;

/**
 * Created on 11-Sep-16.
 */
public class PresenterActivityCropPhoto extends BasePresenter<MvpActivityCropPhoto> {

    private Context mCtx;
    private Item mSelectedItem;
    private Photo mPhoto;
    private Uri mPhotoUri;

    public PresenterActivityCropPhoto(@NonNull Context ctx) {
        this.mCtx = ctx;
    }

    public void init(Intent intent) {
        mSelectedItem = (Item) intent.getSerializableExtra(Constants.ITEM);
        mPhoto = (Photo) intent.getSerializableExtra(Constants.PHOTO);
        mPhotoUri = Uri.fromFile(new File(mPhoto.getPhotoPath()));

        getMvpView().setCustomRatio(mSelectedItem.getItemCustomRatio()[0], mSelectedItem.getItemCustomRatio()[1]);
        getMvpView().loadPhoto(mPhotoUri);
    }

    public Uri getCropSaveUri() {
        return Utils.createCropSaveUri(mCtx, mPhotoUri.getLastPathSegment());
    }

    public void onSuccessLoad() {
        getMvpView().rotate(mPhoto.getRotation());
    }

    public void onSuccessSave(Uri outputUri) {
        getMvpView().finishWithResult(outputUri);
    }

}
