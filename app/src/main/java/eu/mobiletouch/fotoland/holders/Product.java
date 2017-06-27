package eu.mobiletouch.fotoland.holders;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.enums.ProductType;
import eu.mobiletouch.fotoland.utils.ObjectsUtils;

/**
 * Created on 10-Aug-16.
 */
public abstract class Product implements Serializable {
    private static final long serialVersionUID = 7259699601698155543L;

    @DrawableRes
    protected int mIconRes;
    @DrawableRes
    protected int mBannerResId;
    protected String mNoOfFree;
    protected String mName;
    protected ProductType mProductType;
    protected ArrayList<Item> mItems;

    @DrawableRes
    public int getIconRes() {
        return mIconRes;
    }

    public Product setIconRes(@DrawableRes int iconRes) {
        mIconRes = iconRes;
        return this;
    }

    public String getName() {
        return mName;
    }

    public Product setName(String name) {
        mName = name;
        return this;
    }

    public String getNoOfFree() {
        return mNoOfFree;
    }

    public Product setNoOfFree(String noOfFree) {
        mNoOfFree = noOfFree;
        return this;
    }

    public ProductType getProductType() {
        return mProductType;
    }

    public Product setProductType(ProductType productType) {
        mProductType = productType;
        return this;
    }

    public ArrayList<Item> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<Item> items) {
        mItems = items;
    }

    public int getBannerResId() {
        return mBannerResId;
    }

    public void setBannerResId(int bannerResId) {
        mBannerResId = bannerResId;
    }

    protected abstract ArrayList<Item> getAvailableItems(@NonNull Context ctx);

    public abstract float getPpiLimit();

    @Override
    public boolean equals(Object o) {
        return o instanceof Product
                && this.mIconRes == ((Product) o).mIconRes
                && this.mBannerResId == ((Product) o).mBannerResId
                && ObjectsUtils.equals(this.mName, ((Product) o).mName)
                && ObjectsUtils.equals(this.mNoOfFree, ((Product) o).mNoOfFree)
                && ObjectsUtils.equals(this.mProductType, ((Product) o).mProductType)
                && ObjectsUtils.equals(this.mItems, ((Product) o).mItems);
    }
}
