package eu.mobiletouch.fotoland.holders;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * Created on 25-Aug-16.
 */
public class Paper implements Serializable {
    private static final long serialVersionUID = 1406106769309693550L;

    public enum PaperType {
        GLOSSY, MATTE, CLASSIC, CANVAS
    }

    @DrawableRes
    protected int mIconRes;
    protected String mName;
    protected PaperType mItemType;

    public int getIconRes() {
        return mIconRes;
    }

    public Paper setIconRes(int iconRes) {
        mIconRes = iconRes;
        return this;
    }

    public PaperType getItemType() {
        return mItemType;
    }

    public Paper setItemType(PaperType itemType) {
        mItemType = itemType;
        return this;
    }

    public String getName() {
        return mName;
    }

    public Paper setName(String name) {
        mName = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Paper
                && ((Paper) o).getName().equals(mName)
                && ((Paper) o).mIconRes == mIconRes
                && ((Paper) o).getItemType() == mItemType;
    }
}
