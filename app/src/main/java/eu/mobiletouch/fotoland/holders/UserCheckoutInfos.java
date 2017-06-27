package eu.mobiletouch.fotoland.holders;

import java.io.Serializable;
import java.util.ArrayList;

public class UserCheckoutInfos implements Serializable {
    private static final long serialVersionUID = 4410402432899079939L;

    private ArrayList<UserSelections> mCartProducts;
    private String mUserFullName;
    private String mUserPhoneNumber;
    private String mUserEmail;
    private String mDeliveryAddress;
    private String mDeliveryCityAndCounty;
    private String mDeliveryMethod;

    public ArrayList<UserSelections> getCartProducts() {
        return mCartProducts;
    }

    public void setCartProducts(ArrayList<UserSelections> cartProducts) {
        mCartProducts = cartProducts;
    }

    public String getDeliveryAddress() {
        return mDeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        mDeliveryAddress = deliveryAddress;
    }

    public String getDeliveryCityAndCounty() {
        return mDeliveryCityAndCounty;
    }

    public void setDeliveryCityAndCounty(String deliveryCityAndCounty) {
        mDeliveryCityAndCounty = deliveryCityAndCounty;
    }

    public String getDeliveryMethod() {
        return mDeliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        mDeliveryMethod = deliveryMethod;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    public String getUserFullName() {
        return mUserFullName;
    }

    public void setUserFullName(String userFullName) {
        mUserFullName = userFullName;
    }

    public String getUserPhoneNumber() {
        return mUserPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        mUserPhoneNumber = userPhoneNumber;
    }
}
