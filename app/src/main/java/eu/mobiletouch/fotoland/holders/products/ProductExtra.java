package eu.mobiletouch.fotoland.holders.products;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.enums.ProductType;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.Product;

/**
 * Created on 25-Aug-16.
 */
public class ProductExtra extends Product implements Serializable {
    private static final long serialVersionUID = -3095130233449447523L;
    private static final float PPI_LIMIT = 200;

    public ProductExtra(@NonNull Context ctx) {
        this.mIconRes = R.drawable.extra_banner;
        this.mNoOfFree = null;
        this.mBannerResId = R.drawable.banner2;
        this.mName = ctx.getString(R.string.name_product_extra);
        this.mProductType = ProductType.EXTRA;
        this.mItems = getAvailableItems(ctx);
    }

    @Override
    protected ArrayList<Item> getAvailableItems(@NonNull Context ctx) {
        return null;
    }

    @Override
    public float getPpiLimit() {
        return PPI_LIMIT;
    }
}
