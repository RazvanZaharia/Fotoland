package eu.mobiletouch.fotoland.presenters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.activities.ActivityCart;
import eu.mobiletouch.fotoland.activities.ActivityCropPhoto;
import eu.mobiletouch.fotoland.adapters.AdapterRvSelectedPhotos.ViewHolderSelectedPhoto.OnSelectedPhotoInteractionListener;
import eu.mobiletouch.fotoland.dao.Dao;
import eu.mobiletouch.fotoland.dialogs.DialogAddText;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.interfaces.SelectedPhotoItem;
import eu.mobiletouch.fotoland.mvps.MvpActivityDisplaySelectedPhotos;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.utils.Utils;

import static eu.mobiletouch.fotoland.utils.Constants.REQUEST_CODE_CROP;

/**
 * Created on 28-Aug-16.
 */
public class PresenterActivityDisplaySelectedPhotos<T extends MvpActivityDisplaySelectedPhotos> extends BasePresenter<T> implements OnSelectedPhotoInteractionListener {

    protected Activity mActivity;
    protected UserSelections mUserSelections;
    protected ArrayList<SelectedPhotoItem> mSelectedPhotoItems;
    protected int mToCropPosition = -1;

    public PresenterActivityDisplaySelectedPhotos(@NonNull Activity activity) {
        this.mActivity = activity;
    }

    public void init(@NonNull UserSelections userSelections) {
        mUserSelections = userSelections;
        mSelectedPhotoItems = getSelectedPhotosList();
        getMvpView().showSelectedPhotos(mSelectedPhotoItems, mUserSelections.getSelectedItem(), mUserSelections.getSelectedProduct().getPpiLimit());
    }

    protected ArrayList<SelectedPhotoItem> getSelectedPhotosList() {
        ArrayList<SelectedPhotoItem> selectedPhotoItems = new ArrayList<>();
        ArrayList<Photo> photos = mUserSelections.getSelectedPhotos();
        for (Photo photo : photos) {
            selectedPhotoItems.add(photo);
        }
        return selectedPhotoItems;
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
            mUserSelections.getSelectedPhotos().remove(position);
            init(mUserSelections);
        } else {
            photo.setQuantity(photo.getQuantity() - 1);
            getMvpView().notifyAdapter(position);
        }
    }

    @Override
    public void onCropClick(Photo photo, int position) {
        mToCropPosition = position;
        ActivityCropPhoto.launch(mActivity, mUserSelections.getSelectedItem(), photo, REQUEST_CODE_CROP);
    }

    @Override
    public void onLowPpi(Photo photo, int position, float ppi) {
        new AlertDialog.Builder(mActivity)
                .setTitle(R.string.title_alertDialog_lowPpi)
                .setMessage(R.string.message_alertDialog_lowPpi)
                .setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onAddText(final Photo photo, final int position) {
        getMvpView().showDialogAddText(photo.getCaptionText(), new DialogAddText.OnAddTextListener() {
            @Override
            public void onAdd(String text) {
                photo.setCaptionText(text);
                getMvpView().notifyAdapter(position);
            }
        });
    }

    @Override
    public void onAddNewPageAt(int atPosition) {

    }

    @Override
    public void onRemoveNewPageAt(int atPosition) {

    }

    @Override
    public void onAddPhoto(int atPosition) {
    }

    @Override
    public void onPhotoCLick(@NonNull Photo photo) {
    }

    public void onConfirmClick() {
        mUserSelections.setPhotosCount(Utils.getTotalPhotosCount(mUserSelections.getSelectedPhotos()));
        Dao.get(mActivity).saveOneCartProduct(mUserSelections);
        ActivityCart.launch(mActivity);
        getMvpView().finishAllProductScreens();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CROP && resultCode == Activity.RESULT_OK) {
            if (mToCropPosition != -1) {
                Photo toCropPhoto = mUserSelections.getSelectedPhotos().get(mToCropPosition);
                Uri croppedUri = data.getData();
                File croppedFile = new File(croppedUri.getPath());
                toCropPhoto.setCroppedPhotoPath(croppedFile.getAbsolutePath());
                mUserSelections.getSelectedPhotos().set(mToCropPosition, toCropPhoto);
                getMvpView().notifyAdapter(mToCropPosition);
            }
        }
    }
}
