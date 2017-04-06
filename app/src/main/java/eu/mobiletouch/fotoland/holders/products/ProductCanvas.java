package eu.mobiletouch.fotoland.holders.products;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.Product;

/**
 * Created on 25-Aug-16.
 */
public class ProductCanvas extends Product implements Serializable {
    private static final long serialVersionUID = 6852437689808518985L;

    public ProductCanvas(@NonNull Context ctx) {
        this.mIconRes = R.drawable.canvas_banner;
        this.mNoOfFree = null;
        this.mBannerResId = R.drawable.banner2;
        this.mName = ctx.getString(R.string.name_product_canvas);
        this.mProductType = ProductType.CANVAS;
        this.mItems = getAvailableItems(ctx);
    }

    @Override
    protected ArrayList<Item> getAvailableItems(@NonNull Context ctx) {
        return null;
    }
}
