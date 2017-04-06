package eu.mobiletouch.fotoland.mvps;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.holders.localPhotos.PhotoAlbum;

/**
 * Created on 27-Aug-16.
 */
public interface MvpFragmentLocalPhotos extends BaseMvp {
    void showAlbums(ArrayList<PhotoAlbum> albums);
    void showAlbumPhotos(ArrayList<Photo> photos);
    void notifyPhotosAdapter(int position);
    boolean isShowingAlbums();
}
