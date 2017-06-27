package eu.mobiletouch.fotoland.holders.items;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.Paper;
import eu.mobiletouch.fotoland.holders.Size;
import eu.mobiletouch.fotoland.holders.papers.Canvas;

/**
 * Created on 18-Dec-16.
 */
public class ItemCanvasMosaic24 extends Item implements Serializable {
    private static final long serialVersionUID = 3328520034807508989L;

    public ItemCanvasMosaic24(@NonNull Context ctx) {
        this.mName = ctx.getString(R.string.name_item_poster_24);
        this.mRequiredNumberOfPhotos = 24;
        this.mItemCustomRatio = NO_RATIO;
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
        sizes.add(new Size(R.drawable.photo_size2, ctx.getResources().getInteger(R.integer.cm_50), ctx.getResources().getInteger(R.integer.cm_75)));
        return sizes;
    }
}
