package eu.mobiletouch.fotoland.presenters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.activities.ActivityCropPhoto;
import eu.mobiletouch.fotoland.adapters.AdapterRvSelectedPhotos.ViewHolderSelectedPhoto.OnSelectedPhotoInteractionListener;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.mvps.MvpActivityDisplaySelectedPhotos;
import eu.mobiletouch.fotoland.utils.Constants;

/**
 * Created on 28-Aug-16.
 */
public class PresenterActivityDisplaySelectedPhotos extends BasePresenter<MvpActivityDisplaySelectedPhotos> implements OnSelectedPhotoInteractionListener {

    private static final int REQUEST_CROP = 1234;

    private Activity mActivity;
    private UserSelections mUserSelections;
    private ArrayList<Photo> mSelectedPhotos;
    private int mToCropPosition = -1;

    public PresenterActivityDisplaySelectedPhotos(@NonNull Activity activity) {
        this.mActivity = activity;
    }

    public void init(Intent intent) {
        mUserSelections = (UserSelections) intent.getSerializableExtra(Constants.USER_SELECTION);
        mSelectedPhotos = mUserSelections.getSelectedPhotos();
        getMvpView().showSelectedPhotos(mSelectedPhotos, mUserSelections.getSelectedItem().getItemType());
    }

    @Override
    public void onRotateClick(Photo photo, int position) {
        int currentRotation = photo.getRotation();
        if (currentRotation == Constants.ROTATION_270_DEGREE) {
            photo.setRotation(Constants.ROTATION_0_DEGREE);
        } else {
            photo.setRotation(currentRotation + Constants.ROTATION_90_DEGREE);
        }
        getMvpView().notifyAdapter(position);
    }

    @Override
    public void onIncreaseCountClick(Photo photo, int position) {
        photo.setQuantity(photo.getQuantity() + 1);
        getMvpView().notifyAdapter(position);
    }

    @Override
    public void onDecreaseCountClick(Photo photo, int position) {
        int currentQuantity = photo.getQuantity();
        if (currentQuantity == 1) {
            mSelectedPhotos.remove(position);
            getMvpView().notifyAdapter(-1);
        } else {
            photo.setQuantity(photo.getQuantity() - 1);
            getMvpView().notifyAdapter(position);
        }
    }

    @Override
    public void onCropClick(Photo photo, int position) {
        mToCropPosition = position;
        ActivityCropPhoto.launch(mActivity, mUserSelections.getSelectedItem(), photo, REQUEST_CROP);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            if (mToCropPosition != -1) {
                Photo toCropPhoto = mSelectedPhotos.get(mToCropPosition);
                Uri croppedUri = data.getData();
                File croppedFile = new File(croppedUri.getPath());
                toCropPhoto.setCroppedPhotoPath(croppedFile.getAbsolutePath());
                mSelectedPhotos.set(mToCropPosition, toCropPhoto);
                getMvpView().notifyAdapter(mToCropPosition);
            }
        }
    }
}
