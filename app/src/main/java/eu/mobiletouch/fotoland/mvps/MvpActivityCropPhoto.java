package eu.mobiletouch.fotoland.mvps;

import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created on 11-Sep-16.
 */
public interface MvpActivityCropPhoto extends BaseMvp {
    void loadPhoto(@NonNull Uri photoUri);
    void setCustomRatio(int ratioX, int ratioY);
    void rotate(int radius);
    void finishWithResult(Uri uri);
}
