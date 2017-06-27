package eu.mobiletouch.fotoland.mvps;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.dialogs.DialogAddText;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.interfaces.SelectedPhotoItem;

/**
 * Created on 28-Aug-16.
 */
public interface MvpActivityDisplaySelectedPhotos extends BaseMvp {
    void showSelectedPhotos(@NonNull ArrayList<SelectedPhotoItem> photos, @NonNull Item selectedItem, float ppiLimit);
    void notifyAdapter(int position);
    void notifyAdapterItemRemoved(int position);
    void finishAllProductScreens();
    void showDialogAddText(String text, @NonNull DialogAddText.OnAddTextListener onAddTextListener);
}
