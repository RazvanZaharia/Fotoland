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
import eu.mobiletouch.fotoland.holders.papers.Canvas;

/**
 * Created on 09-Jan-17.
 */
public class ItemCanvasClassicRegular extends Item implements Serializable {
    private static final long serialVersionUID = 964127332785433684L;

    public ItemCanvasClassicRegular(@NonNull Context ctx) {
        this.mName = ctx.getString(R.string.name_item_poster_classic_regular);
        this.mRequiredNumberOfPhotos = 1;
        this.mItemType = ItemType.REGULAR;
        this.mItemCustomRatio = REGULAR_RATIO;
        this.mPapers = getAvailablePapers(ctx);
        this.mSizes = getAvailableSizes(ctx);
    }

    @Override
    protected ArrayList<Paper> getAvailablePapers(@NonNull Context ctx) {
        ArrayList<Paper> papers = new ArrayList<>();
        papers.add(new Canvas(ctx));
        return papers;
    }

    @Override
    protected ArrayList<Size> getAvailableSizes(@NonNull Context ctx) {
        ArrayList<Size> sizes = new ArrayList<>();
        sizes.add(new Size(R.drawable.photo_size2, ctx.getResources().getInteger(R.integer.cm_30), ctx.getResources().getInteger(R.integer.cm_40)));
        sizes.add(new Size(R.drawable.photo_size3, ctx.getResources().getInteger(R.integer.cm_40), ctx.getResources().getInteger(R.integer.cm_60)));
        sizes.add(new Size(R.drawable.photo_size3_1, ctx.getResources().getInteger(R.integer.cm_50), ctx.getResources().getInteger(R.integer.cm_70)));
        sizes.add(new Size(R.drawable.photo_size4, ctx.getResources().getInteger(R.integer.cm_60), ctx.getResources().getInteger(R.integer.cm_80)));
        sizes.add(new Size(R.drawable.photo_size5, ctx.getResources().getInteger(R.integer.cm_70), ctx.getResources().getInteger(R.integer.cm_100)));
        sizes.add(new Size(R.drawable.photo_size5, ctx.getResources().getInteger(R.integer.cm_80), ctx.getResources().getInteger(R.integer.cm_120)));
        return sizes;
    }

}