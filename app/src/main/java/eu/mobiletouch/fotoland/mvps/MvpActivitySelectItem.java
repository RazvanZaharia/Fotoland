package eu.mobiletouch.fotoland.mvps;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.holders.Item;

/**
 * Created on 24-Aug-16.
 */
public interface MvpActivitySelectItem extends BaseMvp {
    void showItems(@NonNull ArrayList<Item> items, @DrawableRes int offerBannerResId);
    void setScreenTitle(@NonNull String screenTitle);
    void showOfferDialog();
}
