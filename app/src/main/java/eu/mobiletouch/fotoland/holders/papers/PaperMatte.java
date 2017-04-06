package eu.mobiletouch.fotoland.holders.papers;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.holders.Paper;

/**
 * Created on 25-Aug-16.
 */
public class PaperMatte extends Paper implements Serializable {
    private static final long serialVersionUID = 7073266952864394986L;

    public PaperMatte(@NonNull Context ctx) {
        this.mIconRes = 0;
        this.mName = ctx.getString(R.string.paper_matte);
        this.mItemType = PaperType.MATTE;
    }
}
