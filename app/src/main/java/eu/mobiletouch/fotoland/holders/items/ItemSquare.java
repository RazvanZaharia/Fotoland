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
public class ItemSquare extends Item implements Serializable {
    private static final long serialVersionUID = 6592529361399655305L;

    public ItemSquare(@NonNull Context ctx) {
        this.mIconRes = R.drawable.photo_square;
        this.mItemType = ItemType.SQUARE;
        this.mName = ctx.getString(R.string.name_item_square);
        this.mItemCustomRatio = SQUARE_RATIO;
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
        sizes.add(new Size(0, ctx.getString(R.string.cm_10x10)));
        return sizes;
    }
}
