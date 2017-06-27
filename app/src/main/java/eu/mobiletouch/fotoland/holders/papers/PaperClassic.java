package eu.mobiletouch.fotoland.holders.papers;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.holders.Paper;

/**
 * Created on 09-Jan-17.
 */
public class PaperClassic extends Paper implements Serializable {
    private static final long serialVersionUID = -5633573381977325535L;

    public PaperClassic(@NonNull Context ctx) {
        this.mIconRes = 0;
        this.mName = ctx.getString(R.string.paper_classic);
        this.mItemType = PaperType.CLASSIC;
    }
}
