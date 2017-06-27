package eu.mobiletouch.fotoland.presenters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.mvps.MvpActivitySelectPhotos;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.utils.LaunchScreenUtils;

/**
 * Created on 27-Aug-16.
 */
public class PresenterActivitySelectPhotos extends BasePresenter<MvpActivitySelectPhotos> {

    private Context mCtx;
    private LinkedHashMap<String, Photo> mSelectedLocalPhotos;
    private UserSelections mUserSelections;
    private boolean mSinglePhotoSelectionMode = false;

    public PresenterActivitySelectPhotos(@NonNull Context context) {
        this.mCtx = context;
    }

    public void init(@NonNull UserSelections userSelections) {
        this.mUserSelections = userSelections;
        initSelectedPhotos();
        getMvpView().enableNextButton(arePhotosSelected());
        updateScreenTitle();
        getMvpView().showPhotosFragments();
        if (getRequiredNumberOfPhotos() > 0) {
            getMvpView().showToastMessage(String.format(mCtx.getString(R.string.message_required_number_of_photos), mUserSelections.getSelectedItem().getRequiredNumberOfPhotos()));
        }
    }

    private int getRequiredNumberOfPhotos() {
        return mUserSelections.getSelectedItem() != null ? mUserSelections.getSelectedItem().getRequiredNumberOfPhotos() : -1;
    }

    public void initForSinglePictureSelect() {
        mSinglePhotoSelectionMode = true;
        init(new UserSelections());
    }

    @SuppressWarnings("unchecked")
    public void onNextClick() {
        getMvpView().showLoadingDialog();

        getMvpView().dismissLoadingDialog();
        if (mSinglePhotoSelectionMode) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Constants.PHOTO, getSelectedPhotos().get(0));
            getMvpView().setResultAndFinish(resultIntent);
        } else {
            mUserSelections.setSelectedPhotos(getSelectedPhotos());
            LaunchScreenUtils.launchDisplaySelected(mCtx, mUserSelections);
        }

        /*new DownloadImagesAsync(mCtx, new DownloadImagesAsync.OnImagesDownloadListener() {
            @Override
            public void onDownloadFinished(ArrayList<Photo> photos) {
                getMvpView().dismissLoadingDialog();
                if (mSinglePhotoSelectionMode) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Constants.PHOTO, photos.get(0));
                    getMvpView().setResultAndFinish(resultIntent);
                } else {
                    mUserSelections.setSelectedPhotos(photos);
                    LaunchScreenUtils.launchDisplaySelected(mCtx, mUserSelections);
                }
            }
        }).execute(getSelectedPhotos());*/

    }

    private ArrayList<Photo> getSelectedPhotos() {
        ArrayList<Photo> selectedPhotos = new ArrayList<>();
        if (mSelectedLocalPhotos != null) {
            for (String key : mSelectedLocalPhotos.keySet()) {
                selectedPhotos.add(mSelectedLocalPhotos.get(key));
            }
        }
        return selectedPhotos;
    }

    public void onPhotoClick(Photo photo) {
        if (mSinglePhotoSelectionMode) {
            mSelectedLocalPhotos = new LinkedHashMap<>();
            mSelectedLocalPhotos.put(photo.getPhotoPath(), photo);
            onNextClick();
        } else {
            if (isSelected(photo)) {
                mSelectedLocalPhotos.remove(photo.getPhotoPath());
            } else {
                if (getRequiredNumberOfPhotos() > 0 && mSelectedLocalPhotos.size() >= getRequiredNumberOfPhotos()) {
                    getMvpView().showToastMessage(mCtx.getString(R.string.message_required_number_of_photos_limit));
                } else {
                    mSelectedLocalPhotos.put(photo.getPhotoPath(), photo);
                }
            }
            updateScreenTitle();
            getMvpView().enableNextButton(arePhotosSelected());
        }
    }

    public boolean isSelected(Photo photo) {
        return mSelectedLocalPhotos.get(photo.getPhotoPath()) != null;
    }

    private void updateScreenTitle() {
        if (mSelectedLocalPhotos == null || mSelectedLocalPhotos.size() == 0) {
            getMvpView().setScreenTitle(mCtx.getString(R.string.title_sceen_selectPhotos));
        } else {
            getMvpView().setScreenTitle(mSelectedLocalPhotos.size() + " " + mCtx.getResources().getQuantityString(R.plurals.selectedPhotos, mSelectedLocalPhotos.size()));
        }
    }

    public boolean arePhotosSelected() {
        return ((getRequiredNumberOfPhotos() <= 0 && mSelectedLocalPhotos != null && mSelectedLocalPhotos.size() > 0)
                || (getRequiredNumberOfPhotos() > 0 && mSelectedLocalPhotos != null && mSelectedLocalPhotos.size() == getRequiredNumberOfPhotos()));
    }

    private void initSelectedPhotos() {
        mSelectedLocalPhotos = new LinkedHashMap<>();
        if (mUserSelections.getSelectedPhotos() != null && mUserSelections.getSelectedPhotos().size() != 0) {
            for (Photo photo : mUserSelections.getSelectedPhotos()) {
                mSelectedLocalPhotos.put(photo.getPhotoPath(), photo);
            }
        }
    }

}
