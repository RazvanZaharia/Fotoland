package eu.mobiletouch.fotoland.mvps;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.holders.localPhotos.PhotoAlbum;

/**
 * Created on 28-Aug-16.
 */
public interface MvpFragmentFacebookPhotos extends BaseMvp {
    void showLoginErrorMessage(@NonNull String error);

    void showAlbums(@NonNull ArrayList<PhotoAlbum> fbAlbums);

    void showPhotos(@NonNull ArrayList<Photo> photos);

    void notifyPhotosAdapter(int position);

    void showEmptyView();

    boolean isShowingAlbums();
}
