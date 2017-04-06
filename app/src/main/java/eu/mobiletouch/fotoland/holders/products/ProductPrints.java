package eu.mobiletouch.fotoland.holders.products;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.Product;
import eu.mobiletouch.fotoland.holders.items.ItemRegular;
import eu.mobiletouch.fotoland.holders.items.ItemSquare;
import eu.mobiletouch.fotoland.holders.items.ItemVintage;

/**
 * Created on 25-Aug-16.
 */
public class ProductPrints extends Product implements Serializable {
    private static final long serialVersionUID = -4841786335400843209L;

    public ProductPrints(@NonNull Context ctx) {
        this.mIconRes = R.drawable.prints_banner;
        this.mNoOfFree = "30";
        this.mBannerResId = R.drawable.free30_banner;
        this.mName = ctx.getString(R.string.name_product_prints);
        this.mProductType = ProductType.PRINTS;
        this.mItems = getAvailableItems(ctx);
    }

    @Override
    protected ArrayList<Item> getAvailableItems(@NonNull Context ctx) {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new ItemRegular(ctx));
        items.add(new ItemSquare(ctx));
        items.add(new ItemVintage(ctx));
        return items;
    }
}
