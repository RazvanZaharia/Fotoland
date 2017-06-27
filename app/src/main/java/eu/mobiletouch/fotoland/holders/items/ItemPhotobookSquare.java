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
public class ItemPhotobookSquare extends Item implements Serializable {
    private static final long serialVersionUID = -4314443101924423843L;

    public ItemPhotobookSquare(@NonNull Context ctx) {
        this.mIconRes = R.drawable.photobook_square;
        this.mItemType = ItemType.SQUARE;
        this.mName = ctx.getString(R.string.name_item_square);
        this.mItemCustomRatio = SQUARE_RATIO;
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
        sizes.add(new Size(R.drawable.photobook_square, ctx.getResources().getInteger(R.integer.cm_20), ctx.getResources().getInteger(R.integer.cm_20)));
        sizes.add(new Size(R.drawable.photobook_square, ctx.getResources().getInteger(R.integer.cm_30), ctx.getResources().getInteger(R.integer.cm_30)));
        return sizes;
    }

}
