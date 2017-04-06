package eu.mobiletouch.fotoland.holders;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created on 24-Aug-16.
 */
public abstract class Item implements Serializable {
    private static final long serialVersionUID = 4504177110994534416L;

    public enum ItemType {
        REGULAR, SQUARE, VINTAGE
    }

    public static int[] REGULAR_RATIO = {3, 2};
    public static int[] VINTAGE_RATIO = {4, 3};
    public static int[] SQUARE_RATIO = {1, 1};

    @DrawableRes
    protected int mIconRes;
    protected String mName;
    protected ItemType mItemType;
    protected int[] mItemCustomRatio;
    protected ArrayList<Paper> mPapers;
    protected ArrayList<Size> mSizes;

    public int getIconRes() {
        return mIconRes;
    }

    public Item setIconRes(int iconRes) {
        mIconRes = iconRes;
        return this;
    }

    public ItemType getItemType() {
        return mItemType;
    }

    public Item setItemType(ItemType itemType) {
        mItemType = itemType;
        return this;
    }

    public String getName() {
        return mName;
    }

    public Item setName(String name) {
        mName = name;
        return this;
    }

    public ArrayList<Paper> getPapers() {
        return mPapers;
    }

    public void setPapers(ArrayList<Paper> papers) {
        mPapers = papers;
    }

    public ArrayList<Size> getSizes() {
        return mSizes;
    }

    public void setSizes(ArrayList<Size> sizes) {
        mSizes = sizes;
    }

    protected abstract ArrayList<Paper> getAvailablePapers(@NonNull Context ctx);

    protected abstract ArrayList<Size> getAvailableSizes(@NonNull Context ctx);

    public int[] getItemCustomRatio() {
        return mItemCustomRatio;
    }

    public void setItemCustomRatio(int[] itemCustomRatio) {
        mItemCustomRatio = itemCustomRatio;
    }
}
