package eu.mobiletouch.fotoland.presenters;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotoAlbums.ViewHolderPhotoAlbum.OnPhotoAlbumClickListener;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotos.ViewHolderPhoto.OnPhotoClickListener;
import eu.mobiletouch.fotoland.controllers.PermissionController;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.holders.localPhotos.PhotoAlbum;
import eu.mobiletouch.fotoland.interfaces.OnLocalImagesObtained;
import eu.mobiletouch.fotoland.interfaces.OnSelectPhotosFragmentsListener;
import eu.mobiletouch.fotoland.interfaces.PermissionCallback;
import eu.mobiletouch.fotoland.mvps.MvpFragmentLocalPhotos;
import eu.mobiletouch.fotoland.utils.DeviceManager;

/**
 * Created on 27-Aug-16.
 */
public class PresenterFragmentLocalPhotos extends BasePresenter<MvpFragmentLocalPhotos> implements PermissionCallback, OnPhotoClickListener, OnPhotoAlbumClickListener {

    private Activity mActivity;
    private ArrayList<PhotoAlbum> mLocalAlbums;
    private PermissionController permissionController;
    private OnSelectPhotosFragmentsListener mOnSelectPhotosFragmentsListener;

    public PresenterFragmentLocalPhotos(@NonNull Activity activity, @NonNull OnSelectPhotosFragmentsListener onSelectPhotosFragmentsListener) {
        this.mActivity = activity;
        this.mOnSelectPhotosFragmentsListener = onSelectPhotosFragmentsListener;
    }

    public void init() {
        permissionController = new PermissionController(mActivity, this);
        permissionController.askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onPhotoClick(int position, Photo photo) {
        mOnSelectPhotosFragmentsListener.onPhotoClick(photo);
        getMvpView().notifyPhotosAdapter(position);
    }

    @Override
    public boolean isSelected(int position, Photo photo) {
        return mOnSelectPhotosFragmentsListener.isSelected(photo);
    }

    @Override
    public void onPhotoAlbumClick(PhotoAlbum album) {
        getMvpView().showAlbumPhotos(album.getAlbumPhotos());
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionController != null) {
            permissionController.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPermissionDenied(int permissionCode) {
        Toast.makeText(mActivity, R.string.permission_denied, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionGranted(int permissionCode) {
        DeviceManager.getPhoneAlbums(mActivity, new OnLocalImagesObtained() {
            @Override
            public void onComplete(ArrayList<PhotoAlbum> albums) {
                mLocalAlbums = albums;
                getMvpView().showAlbums(mLocalAlbums);
            }

            @Override
            public void onError() {
                Toast.makeText(mActivity, "Photos Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPermissionGranted(int permissionCode, Object data) {
    }

    public boolean onBackPressed() {
        if (getMvpView().isShowingAlbums()) {
            return false;
        } else {
            getMvpView().showAlbums(mLocalAlbums);
            return true;
        }
    }

}
