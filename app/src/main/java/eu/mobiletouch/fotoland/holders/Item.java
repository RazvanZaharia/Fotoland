package eu.mobiletouch.fotoland.holders;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.enums.ItemType;
import eu.mobiletouch.fotoland.utils.ObjectsUtils;

/**
 * Created on 24-Aug-16.
 */
public abstract class Item implements Serializable {
    private static final long serialVersionUID = 4504177110994534416L;

    public static int[] REGULAR_RATIO = {3, 2};
    public static int[] VINTAGE_RATIO = {4, 3};
    public static int[] SQUARE_RATIO = {1, 1};
    public static int[] NO_RATIO = {0, 0};

    @DrawableRes
    protected int mIconRes;
    protected String mName;
    protected ItemType mItemType;
    protected int[] mItemCustomRatio;
    protected ArrayList<Paper> mPapers;
    protected Paper mSelectedPaper;
    protected ArrayList<Size> mSizes;
    protected Size mSelectedSize;

    protected int mRequiredNumberOfPhotos = -1;

    protected abstract ArrayList<Paper> getAvailablePapers(@NonNull Context ctx);

    protected abstract ArrayList<Size> getAvailableSizes(@NonNull Context ctx);

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

    public Item setPapers(ArrayList<Paper> papers) {
        mPapers = papers;
        return this;
    }

    public ArrayList<Size> getSizes() {
        return mSizes;
    }

    public Item setSizes(ArrayList<Size> sizes) {
        mSizes = sizes;
        return this;
    }

    public int[] getItemCustomRatio() {
        return mItemCustomRatio;
    }

    public Item setItemCustomRatio(int[] itemCustomRatio) {
        mItemCustomRatio = itemCustomRatio;
        return this;
    }

    public Size getSelectedSize() {
        return mSelectedSize;
    }

    public Item setSelectedSize(Size selectedSize) {
        mSelectedSize = selectedSize;
        return this;
    }

    public Paper getSelectedPaper() {
        return mSelectedPaper;
    }

    public Item setSelectedPaper(Paper selectedPaper) {
        mSelectedPaper = selectedPaper;
        return this;
    }

    public int getRequiredNumberOfPhotos() {
        return mRequiredNumberOfPhotos;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Item
                && this.mIconRes == ((Item) o).mIconRes
                && ObjectsUtils.equals(this.mName, ((Item) o).mName)
                && ObjectsUtils.equals(this.mItemType, ((Item) o).mItemType)
                && ObjectsUtils.equals(this.mPapers, ((Item) o).mPapers)
                && ObjectsUtils.equals(this.mSizes, ((Item) o).mSizes)
                && ObjectsUtils.equals(this.mSelectedPaper, ((Item) o).mSelectedPaper)
                && ObjectsUtils.equals(this.mSelectedSize, ((Item) o).mSelectedSize)
                && this.mRequiredNumberOfPhotos == ((Item) o).getRequiredNumberOfPhotos();

    }
}
