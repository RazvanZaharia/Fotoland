package eu.mobiletouch.fotoland.mvps;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.holders.UserSelections;

/**
 * Created on 19-Sep-16.
 */
public interface MvpActivityCart extends BaseMvp {
    void showCartProducts(ArrayList<UserSelections> cartProducts);
    void notifyAdapter(int position);
    void setTotalPrice(String totalPrice);
    void finishAll();
}
