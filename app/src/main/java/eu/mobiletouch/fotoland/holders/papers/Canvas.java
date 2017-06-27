package eu.mobiletouch.fotoland.holders.papers;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.holders.Paper;

/**
 * Created on 09-Jan-17.
 */
public class Canvas extends Paper implements Serializable {
    private static final long serialVersionUID = 7348146546432790560L;

    public Canvas(@NonNull Context ctx) {
        this.mIconRes = 0;
        this.mName = ctx.getString(R.string.canvas);
        this.mItemType = PaperType.CANVAS;
    }
}
