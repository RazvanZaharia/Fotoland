package eu.mobiletouch.fotoland.holders.products;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.enums.ProductType;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.Product;
import eu.mobiletouch.fotoland.holders.items.ItemPhotobookLandscape;
import eu.mobiletouch.fotoland.holders.items.ItemPhotobookSquare;

/**
 * Created on 25-Aug-16.
 */
public class ProductPhotobook extends Product implements Serializable {
    private static final long serialVersionUID = -3033272804636388935L;
    private static final float PPI_LIMIT = 200;

    public ProductPhotobook(@NonNull Context ctx) {
        this.mIconRes = R.drawable.photobook_banner;
        this.mNoOfFree = null;
        this.mBannerResId = R.drawable.banner2;
        this.mName = ctx.getString(R.string.name_product_photobook);
        this.mProductType = ProductType.PHOTOBOOK;
        this.mItems = getAvailableItems(ctx);
    }

    @Override
    protected ArrayList<Item> getAvailableItems(@NonNull Context ctx) {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new ItemPhotobookSquare(ctx));
        items.add(new ItemPhotobookLandscape(ctx));
        return items;
    }

    @Override
    public float getPpiLimit() {
        return PPI_LIMIT;
    }
}
