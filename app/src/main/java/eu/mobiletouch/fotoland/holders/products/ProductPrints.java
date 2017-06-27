package eu.mobiletouch.fotoland.holders.products;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.enums.ProductType;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.Product;
import eu.mobiletouch.fotoland.holders.items.ItemPrintRegular;
import eu.mobiletouch.fotoland.holders.items.ItemPrintSquare;
import eu.mobiletouch.fotoland.holders.items.ItemPrintVintage;

/**
 * Created on 25-Aug-16.
 */
public class ProductPrints extends Product implements Serializable {
    private static final long serialVersionUID = -4841786335400843209L;
    private static final float PPI_LIMIT = 200;

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
        items.add(new ItemPrintRegular(ctx));
        items.add(new ItemPrintSquare(ctx));
        items.add(new ItemPrintVintage(ctx));
        return items;
    }

    @Override
    public float getPpiLimit() {
        return PPI_LIMIT;
    }
}
