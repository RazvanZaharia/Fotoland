package eu.mobiletouch.fotoland.presenters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.activities.ActivityMakeCollage;
import eu.mobiletouch.fotoland.dialogs.DialogAddText;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.mvps.MvpActivityEditPagePhotobook;
import eu.mobiletouch.fotoland.utils.Constants;

/**
 * Created on 31-Oct-16.
 */
public class PresenterActivityEditPagePhotobook extends BasePresenter<MvpActivityEditPagePhotobook> {

    private Activity mActivity;
    private ArrayList<Photo> mPhotos;
    private int mSelectedPosition = -1;

    public void init(Activity activity, @NonNull ArrayList<Photo> photos, int selectedPage) {
        mActivity = activity;
        mPhotos = photos;
        getMvpView().showPhotos(mPhotos, selectedPage);
    }

    public void onMakeCollageClick(int position) {
        mSelectedPosition = position;
        ActivityMakeCollage.launchForResult(mActivity, mPhotos.get(mSelectedPosition), Constants.REQUEST_CODE_MAKE_COLLAGE);
    }

    public void onRemovePhoto(int position) {
        mSelectedPosition = -1;
        mPhotos.remove(position);
        int selectedPosition = position - 1;
        getMvpView().showPhotos(mPhotos, selectedPosition < 0 ? 0 : selectedPosition > mPhotos.size()? mPhotos.size() - 1 : selectedPosition);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_MAKE_COLLAGE) {
            if (resultCode == Activity.RESULT_OK) {
                String collagePhotoPath = data.getStringExtra(Constants.ARG_FILE_PATH);
                mPhotos.get(mSelectedPosition).setCroppedPhotoPath(null);
                mPhotos.get(mSelectedPosition).setPhotoPath(collagePhotoPath);
                getMvpView().showPhotos(mPhotos, mSelectedPosition);
            }
        }
    }

    public void onSaveMenuItemAction() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.SELECTED_PHOTOS, mPhotos);
        getMvpView().setResultAndFinish(resultIntent, Activity.RESULT_OK);
    }

    public void onAddCaption(final int currentItem) {
        DialogAddText dialogAddText = DialogAddText.newInstance(mPhotos.get(currentItem).getCaptionText());
        dialogAddText.setOnAddTextListener(new DialogAddText.OnAddTextListener() {
            @Override
            public void onAdd(String text) {
                mPhotos.get(currentItem).setCaptionText(text);
                getMvpView().showPhotos(mPhotos, currentItem);
            }
        });
        getMvpView().showDialog(dialogAddText);
    }
}
