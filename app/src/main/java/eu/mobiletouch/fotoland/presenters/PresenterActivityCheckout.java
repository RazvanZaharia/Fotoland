package eu.mobiletouch.fotoland.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.dao.Dao;
import eu.mobiletouch.fotoland.holders.UserCheckoutInfos;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.mvps.MvpActivityCheckout;

public class PresenterActivityCheckout extends BasePresenter<MvpActivityCheckout> {

    private Context mCtx;
    private UserCheckoutInfos mUserCheckoutInfos;

    public PresenterActivityCheckout(@NonNull Context ctx) {
        this.mCtx = ctx;
    }

    @Override
    public void attachView(MvpActivityCheckout mvpView) {
        super.attachView(mvpView);
        init();
    }

    public void init() {
        mUserCheckoutInfos = new UserCheckoutInfos();
        ArrayList<UserSelections> cartProducts = Dao.get(mCtx).readAllCartProducts();
        mUserCheckoutInfos.setCartProducts(cartProducts);
    }

    public void goToSummary() {
        boolean isFormCompleted = true;
        String userFullName = getMvpView().getUserFullName();
        if (TextUtils.isEmpty(userFullName)) {
            getMvpView().setUserFullNameError("Complete contact name");
            isFormCompleted = false;
        } else {
            mUserCheckoutInfos.setUserFullName(userFullName);
        }
        String userPhoneNumber = getMvpView().getUserPhoneNumber();
        if (TextUtils.isEmpty(userPhoneNumber)) {
            getMvpView().setUserPhoneNumberError("Complete phone number");
            isFormCompleted = false;
        } else {
            mUserCheckoutInfos.setUserPhoneNumber(userPhoneNumber);
        }
        String deliveryAddress = getMvpView().getDeliveryAddress();
        if (TextUtils.isEmpty(deliveryAddress)) {
            getMvpView().setDeliveryAddressError("Complete address");
            isFormCompleted = false;
        } else {
            mUserCheckoutInfos.setDeliveryAddress(deliveryAddress);
        }
        String deliveryCityAndCounty = getMvpView().getDeliveryCityAndCounty();
        if (TextUtils.isEmpty(deliveryCityAndCounty)) {
            getMvpView().setDeliveryCityAndCountyError("Complete city");
            isFormCompleted = false;
        } else {
            mUserCheckoutInfos.setDeliveryCityAndCounty(deliveryCityAndCounty);
        }
        String deliveryMethod = getMvpView().getDeliveryMethod();
        if (TextUtils.isEmpty(deliveryMethod)) {
            getMvpView().setDeliveryMethodError("Select delivery method");
            isFormCompleted = false;
        } else {
            mUserCheckoutInfos.setDeliveryMethod(deliveryMethod);
        }

        if (isFormCompleted) {
            launchSummary();
        }
    }

    private void launchSummary() {

    }

}
