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

/**
 * Created on 21-Sep-16.
 */
public class ItemPhotobookLandscape extends Item implements Serializable {
    private static final long serialVersionUID = 8503182147610735934L;

    public ItemPhotobookLandscape(@NonNull Context ctx) {
        this.mIconRes = R.drawable.photobook_landscape;
        this.mItemType = ItemType.LANDSCAPE;
        this.mName = ctx.getString(R.string.name_item_landscape);
        this.mItemCustomRatio = NO_RATIO;
        this.mPapers = getAvailablePapers(ctx);
        this.mSizes = getAvailableSizes(ctx);
    }

    @Override
    protected ArrayList<Paper> getAvailablePapers(@NonNull Context ctx) {
        return null;
    }

    @Override
    protected ArrayList<Size> getAvailableSizes(@NonNull Context ctx) {
        ArrayList<Size> sizes = new ArrayList<>();
        sizes.add(new Size(R.drawable.photobook_landscape, ctx.getResources().getInteger(R.integer.cm_15), ctx.getResources().getInteger(R.integer.cm_20)));
        sizes.add(new Size(R.drawable.photobook_landscape, ctx.getResources().getInteger(R.integer.cm_20), ctx.getResources().getInteger(R.integer.cm_30)));
        return sizes;
    }

}
