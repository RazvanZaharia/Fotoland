package eu.mobiletouch.fotoland.mvps;

import android.support.annotation.NonNull;

/**
 * Created on 27-Aug-16.
 */
public interface MvpActivitySelectPhotos extends BaseMvp {
    void showPhotosFragments();

    void setScreenTitle(@NonNull String screenTitle);

    void enableNextButton(boolean enable);
}
