package eu.mobiletouch.fotoland.presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.activities.ActivityDisplaySelectedPhotos;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.mvps.MvpActivitySelectPhotos;

/**
 * Created on 27-Aug-16.
 */
public class PresenterActivitySelectPhotos extends BasePresenter<MvpActivitySelectPhotos> {

    private Context mCtx;
    private HashMap<String, Photo> mSelectedLocalPhotos;
    private UserSelections mUserSelections;

    public PresenterActivitySelectPhotos(@NonNull Context context) {
        this.mCtx = context;
    }

    public void init(@NonNull UserSelections userSelections) {
        this.mUserSelections = userSelections;
        mSelectedLocalPhotos = new HashMap<>();
        updateScreenTitle();
        getMvpView().showPhotosFragments();
    }

    public void onNextClick() {
        mUserSelections.setSelectedPhotos(getSelectedPhotos());
        ActivityDisplaySelectedPhotos.launch(mCtx, mUserSelections);
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
        if (isSelected(photo)) {
            mSelectedLocalPhotos.remove(photo.getPhotoPath());
        } else {
            mSelectedLocalPhotos.put(photo.getPhotoPath(), photo);
        }
        updateScreenTitle();
        getMvpView().enableNextButton(arePhotosSelected());
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
        return mSelectedLocalPhotos != null && mSelectedLocalPhotos.size() > 0;
    }

}
