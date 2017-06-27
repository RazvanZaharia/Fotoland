package eu.mobiletouch.fotoland.mvps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.holders.localPhotos.Photo;

/**
 * Created on 31-Oct-16.
 */
public interface MvpActivityEditPagePhotobook extends BaseMvp {
    void showPhotos(@NonNull ArrayList<Photo> dataSet, int selectedIndex);
    void setResultAndFinish(@NonNull Intent resultIntent, int resultCode);
    void showDialog(DialogFragment dialogFragment);
}
