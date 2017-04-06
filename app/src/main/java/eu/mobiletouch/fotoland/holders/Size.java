package eu.mobiletouch.fotoland.holders;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * Created on 25-Aug-16.
 */
public class Size implements Serializable {
    private static final long serialVersionUID = -4872043896963092501L;

    @DrawableRes
    private int mIconRes;
    private String mSize;

    public Size(int iconRes, String size) {
        mIconRes = iconRes;
        mSize = size;
    }

    public int getIconRes() {
        return mIconRes;
    }

    public Size setIconRes(int iconRes) {
        mIconRes = iconRes;
        return this;
    }

    public String getSize() {
        return mSize;
    }

    public Size setSize(String name) {
        mSize = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Size && ((Size) o).getSize().equals(mSize) && ((Size) o).mIconRes == mIconRes;
    }
}
