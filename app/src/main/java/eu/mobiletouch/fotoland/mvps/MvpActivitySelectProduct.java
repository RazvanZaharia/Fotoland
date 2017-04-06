package eu.mobiletouch.fotoland.mvps;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.holders.Product;

/**
 * Created on 10-Aug-16.
 */
public interface MvpActivitySelectProduct extends BaseMvp {
    void showProducts(@NonNull ArrayList<Product> products);
}
