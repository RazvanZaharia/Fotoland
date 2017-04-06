package eu.mobiletouch.fotoland.holders.items;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.Paper;
import eu.mobiletouch.fotoland.holders.Size;
import eu.mobiletouch.fotoland.holders.papers.PaperGlossy;
import eu.mobiletouch.fotoland.holders.papers.PaperMatte;

/**
 * Created on 25-Aug-16.
 */
public class ItemVintage extends Item implements Serializable {
    private static final long serialVersionUID = 4824156472025920685L;

    public ItemVintage(@NonNull Context ctx) {
        this.mIconRes = R.drawable.photo_wintage;
        this.mItemType = ItemType.VINTAGE;
        this.mName = ctx.getString(R.string.name_item_vintage);
        this.mItemCustomRatio = VINTAGE_RATIO;
        this.mPapers = getAvailablePapers(ctx);
        this.mSizes = getAvailableSizes(ctx);
    }

    @Override
    protected ArrayList<Paper> getAvailablePapers(@NonNull Context ctx) {
        ArrayList<Paper> papers = new ArrayList<>();
        papers.add(new PaperGlossy(ctx));
        papers.add(new PaperMatte(ctx));
        return papers;
    }

    @Override
    protected ArrayList<Size> getAvailableSizes(@NonNull Context ctx) {
        ArrayList<Size> sizes = new ArrayList<>();
        sizes.add(new Size(0, ctx.getString(R.string.cm_10x13)));
        return sizes;
    }

}
