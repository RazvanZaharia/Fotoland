package eu.mobiletouch.fotoland.mvps;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.holders.Paper;
import eu.mobiletouch.fotoland.holders.Size;

/**
 * Created on 25-Aug-16.
 */
public interface MvpActivitySelectDetails extends BaseMvp {
    void setScreenTitle(@NonNull String screenTitle);

    void setPapers(@NonNull ArrayList<Paper> papers);

    void setSizes(@NonNull ArrayList<Size> sizes);

    void notifyPapersAdapter();

    void notifySizesAdapter();

    void setSelectedPaper(@NonNull String paperName);

    void setSelectedPhotoSize(@NonNull String photoSize);

    void changeVisibility(@IdRes int viewId, int visibility, long delayAlphaAnimationStart);

    void enableSelectPicturesButton(boolean enable);
}
