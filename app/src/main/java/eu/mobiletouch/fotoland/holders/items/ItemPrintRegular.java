package eu.mobiletouch.fotoland.holders.items;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.enums.ItemType;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.Paper;
import eu.mobiletouch.fotoland.holders.Size;
import eu.mobiletouch.fotoland.holders.papers.PaperGlossy;
import eu.mobiletouch.fotoland.holders.papers.PaperMatte;

/**
 * Created on 25-Aug-16.
 */
public class ItemPrintRegular extends Item implements Serializable {
    private static final long serialVersionUID = -8696185068008158085L;

    public ItemPrintRegular(@NonNull Context ctx) {
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
        sizes.add(new Size(R.drawable.photo_size2, ctx.getResources().getInteger(R.integer.cm_9), ctx.getResources().getInteger(R.integer.cm_13)));
        sizes.add(new Size(R.drawable.photo_size3, ctx.getResources().getInteger(R.integer.cm_10), ctx.getResources().getInteger(R.integer.cm_15)));
        sizes.add(new Size(R.drawable.photo_size3_1, ctx.getResources().getInteger(R.integer.cm_13), ctx.getResources().getInteger(R.integer.cm_18)));
        sizes.add(new Size(R.drawable.photo_size4, ctx.getResources().getInteger(R.integer.cm_15), ctx.getResources().getInteger(R.integer.cm_21)));
        sizes.add(new Size(R.drawable.photo_size5, ctx.getResources().getInteger(R.integer.cm_20), ctx.getResources().getInteger(R.integer.cm_30)));
        return sizes;
    }
}
