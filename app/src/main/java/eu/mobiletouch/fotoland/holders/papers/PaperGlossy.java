package eu.mobiletouch.fotoland.holders.papers;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.holders.Paper;

/**
 * Created on 25-Aug-16.
 */
public class PaperGlossy extends Paper implements Serializable {
    private static final long serialVersionUID = 7348146546432790560L;

    public PaperGlossy(@NonNull Context ctx) {
        this.mIconRes = 0;
        this.mName = ctx.getString(R.string.paper_glossy);
        this.mItemType = PaperType.GLOSSY;
    }
}
