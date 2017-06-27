package eu.mobiletouch.fotoland.mvps;

import android.support.annotation.NonNull;

public interface MvpActivityCheckout extends BaseMvp {
    String getUserFullName();
    void setUserFullNameError(@NonNull String message);
    String getUserPhoneNumber();
    void setUserPhoneNumberError(@NonNull String message);
    String getDeliveryAddress();
    void setDeliveryAddressError(@NonNull String message);
    String getDeliveryCityAndCounty();
    void setDeliveryCityAndCountyError(@NonNull String message);
    String getDeliveryMethod();
    void setDeliveryMethodError(@NonNull String message);
}
