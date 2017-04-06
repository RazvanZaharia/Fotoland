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
public class ProductPosters extends Product implements Serializable {
    private static final long serialVersionUID = 1535374921152840L;

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
        return null;
    }
}
