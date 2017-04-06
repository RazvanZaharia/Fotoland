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
public class ItemRegular extends Item implements Serializable {
    private static final long serialVersionUID = -8696185068008158085L;

    public ItemRegular(@NonNull Context ctx) {
        this.mIconRes = R.drawable.photo_regular;
        this.mItemType = ItemType.REGULAR;
        this.mName = ctx.getString(R.string.name_item_regular);
        this.mItemCustomRatio = REGULAR_RATIO;
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
        sizes.add(new Size(R.drawable.photo_size2, ctx.getString(R.string.cm_9x13)));
        sizes.add(new Size(R.drawable.photo_size3, ctx.getString(R.string.cm_10x15)));
        sizes.add(new Size(R.drawable.photo_size3_1, ctx.getString(R.string.cm_13x18)));
        sizes.add(new Size(R.drawable.photo_size4, ctx.getString(R.string.cm_15x21)));
        sizes.add(new Size(R.drawable.photo_size5, ctx.getString(R.string.cm_20x30)));
        return sizes;
    }
}
