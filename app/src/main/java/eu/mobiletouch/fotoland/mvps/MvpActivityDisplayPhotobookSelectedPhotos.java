package eu.mobiletouch.fotoland.mvps;

import android.support.annotation.NonNull;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.interfaces.SelectedPhotoItem;

/**
 * Created on 26-Sep-16.
 */
public interface MvpActivityDisplayPhotobookSelectedPhotos extends MvpActivityDisplaySelectedPhotos {
    void attachToRecyclerView(ItemTouchHelper itemTouchHelper);
    void notifyAdapterItemMoved(@NonNull ArrayList<SelectedPhotoItem> photos, int oldPosition, int newPosition);
    void finishSelf();
}
