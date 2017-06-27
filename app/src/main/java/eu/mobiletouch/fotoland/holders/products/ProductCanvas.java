package eu.mobiletouch.fotoland.holders.products;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.enums.ProductType;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.Product;
import eu.mobiletouch.fotoland.holders.items.ItemCanvasClassicRegular;
import eu.mobiletouch.fotoland.holders.items.ItemCanvasClassicSquare;
import eu.mobiletouch.fotoland.holders.items.ItemCanvasMosaic24;

/**
 * Created on 25-Aug-16.
 */
public class ProductCanvas extends Product implements Serializable {
    private static final long serialVersionUID = 6852437689808518985L;
    private static final float PPI_LIMIT = 200;

    public ProductCanvas(@NonNull Context ctx) {
        this.mIconRes = R.drawable.canvas_banner;
        this.mNoOfFree = null;
        this.mBannerResId = R.drawable.banner2;
        this.mName = ctx.getString(R.string.name_product_canvas);
        this.mProductType = ProductType.POSTERS;
        this.mItems = getAvailableItems(ctx);
    }

    @Override
    protected ArrayList<Item> getAvailableItems(@NonNull Context ctx) {
        ArrayList<Item> posterItems = new ArrayList<>();
        final ItemCanvasClassicRegular itemCanvasClassicRegular = new ItemCanvasClassicRegular(ctx);
        final ItemCanvasClassicSquare itemCanvasClassicSquare = new ItemCanvasClassicSquare(ctx);
        final ItemCanvasMosaic24 itemCanvasMosaic24 = new ItemCanvasMosaic24(ctx);
//        final ItemCanvasMosaic36 itemCanvasMosaic36 = new ItemCanvasMosaic36(ctx);

        posterItems.add(itemCanvasClassicRegular);
        posterItems.add(itemCanvasClassicSquare);
        posterItems.add(itemCanvasMosaic24);
//        posterItems.add(itemCanvasMosaic36);
        return posterItems;
    }

    @Override
    public float getPpiLimit() {
        return PPI_LIMIT;
    }
}
