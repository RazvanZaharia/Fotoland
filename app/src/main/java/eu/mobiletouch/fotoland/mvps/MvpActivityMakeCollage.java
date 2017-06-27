package eu.mobiletouch.fotoland.mvps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created on 05-Oct-16.
 */
public interface MvpActivityMakeCollage extends BaseMvp {
    void setCollagePatterns(@NonNull List<Integer> ids);

    void addThisCollagePatternLayout(int idOfCollagePatternLayout);

    void selectSinglePicture();

    Bitmap getCollageBitmap();

    void setResultAndFinish(int resultCode, Intent data);

    void finishAllProductScreens();
}
