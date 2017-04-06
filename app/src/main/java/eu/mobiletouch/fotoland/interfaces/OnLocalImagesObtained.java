package eu.mobiletouch.fotoland.interfaces;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.holders.localPhotos.PhotoAlbum;

/**
 * Created on 27-Aug-16.
 */
public interface OnLocalImagesObtained {
    void onComplete( ArrayList<PhotoAlbum> albums );
    void onError();
}
