package eu.mobiletouch.fotoland.mvps;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;

/**
 * Created on 28-Aug-16.
 */
public interface MvpActivityDisplaySelectedPhotos extends BaseMvp {
    void showSelectedPhotos(@NonNull ArrayList<Photo> photos, @NonNull Item.ItemType itemType);
    void notifyAdapter(int position);
}
