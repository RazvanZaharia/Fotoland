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
    private float mWidth; //in cm
    private float mHeight; // in cm

    public Size(int iconRes, float width, float height) {
        this.mIconRes = iconRes;
        this.mHeight = height;
        this.mWidth = width;
    }

    public int getIconRes() {
        return mIconRes;
    }

    public Size setIconRes(int iconRes) {
        mIconRes = iconRes;
        return this;
    }

    public float getHeight() {
        return mHeight;
    }

    public Size setHeight(float height) {
        mHeight = height;
        return this;
    }

    public float getWidth() {
        return mWidth;
    }

    public Size setWidth(float width) {
        mWidth = width;
        return this;
    }

    public String getSizeToDisplay() {
        return mWidth + "x" + mHeight;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Size
                && ((Size) o).mWidth == this.mWidth
                && ((Size) o).mHeight == this.mHeight
                && ((Size) o).mIconRes == this.mIconRes;
    }
}
