package eu.mobiletouch.fotoland.holders.products;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.enums.ProductType;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.Product;
import eu.mobiletouch.fotoland.holders.items.ItemPosterClassicRegular;
import eu.mobiletouch.fotoland.holders.items.ItemPosterClassicSquare;
import eu.mobiletouch.fotoland.holders.items.ItemPosterMosaic24;

/**
 * Created on 25-Aug-16.
 */
public class ProductPosters extends Product implements Serializable {
    private static final long serialVersionUID = 1535374921152840L;
    private static final float PPI_LIMIT = 150;

    public ProductPosters(@NonNull Context ctx) {
        this.mIconRes = R.drawable.posters_banner;
        this.mNoOfFree = null;
        this.mBannerResId = R.drawable.banner2;
        this.mName = ctx.getString(R.string.name_product_posters);
        this.mProductType = ProductType.POSTERS;
        this.mItems = getAvailableItems(ctx);
    }

    @Override
    protected ArrayList<Item> getAvailableItems(@NonNull Context ctx) {
        ArrayList<Item> posterItems = new ArrayList<>();
        final ItemPosterClassicRegular itemPosterClassicRegular = new ItemPosterClassicRegular(ctx);
        final ItemPosterClassicSquare itemPosterClassicSquare = new ItemPosterClassicSquare(ctx);
        final ItemPosterMosaic24 itemPosterMosaic24 = new ItemPosterMosaic24(ctx);
//        final ItemPosterMosaic36 itemPosterMosaic36 = new ItemPosterMosaic36(ctx);

        posterItems.add(itemPosterClassicRegular);
        posterItems.add(itemPosterClassicSquare);
        posterItems.add(itemPosterMosaic24);
//        posterItems.add(itemPosterMosaic36);
        return posterItems;
    }

    @Override
    public float getPpiLimit() {
        return PPI_LIMIT;
    }
}
