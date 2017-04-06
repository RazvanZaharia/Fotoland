package eu.mobiletouch.fotoland.interfaces;

import eu.mobiletouch.fotoland.holders.localPhotos.Photo;

/**
 * Created on 27-Aug-16.
 */
public interface OnSelectPhotosFragmentsListener {
    void onPhotoClick(Photo photo);
    boolean isSelected(Photo photo);
}
