package eu.mobiletouch.fotoland.activities;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.mvps.MvpActivityCheckout;
import eu.mobiletouch.fotoland.presenters.PresenterActivityCheckout;
import eu.mobiletouch.fotoland.x_base.BaseToolbarActivity;

public class ActivityCheckout extends BaseToolbarActivity implements MvpActivityCheckout {

    public final static String DELIVERY_EXPRESS = "express";
    public final static String DELIVERY_CLUB = "club";

    @Bind(R.id.btn_goto_summary)
    View mViewGoToSummary;
    @Bind(R.id.et_full_name)
    EditText mEtFullName;
    @Bind(R.id.et_phone_number)
    EditText mEtPhoneNumber;
    @Bind(R.id.et_address)
    EditText mEtAddress;
    @Bind(R.id.et_city_and_county)
    EditText mEtCityAndCounty;
    @Bind(R.id.rg_delivery_method)
    RadioGroup mRgDeliveryMethod;

    private PresenterActivityCheckout mPresenterActivityCheckout;

    public static void launch(@NonNull Context ctx) {
        ctx.startActivity(new Intent(ctx, ActivityCheckout.class));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_checkout;
    }

    @Override
    protected boolean showCartIcon() {
        return false;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.screenTitle_checkoutActivity);
    }

    @Override
    protected void init() {
        super.init();
        mPresenterActivityCheckout = new PresenterActivityCheckout(this);
        mPresenterActivityCheckout.attachView(this);
    }

    @Override
    protected void setActions() {
        super.setActions();
        mViewGoToSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterActivityCheckout.goToSummary();
            }
        });
    }

    @Override
    public String getUserFullName() {
        return mEtFullName.getText().toString();
    }

    @Override
    public void setUserFullNameError(@NonNull String message) {
        mEtFullName.setError(message);
    }

    @Override
    public String getUserPhoneNumber() {
        return mEtPhoneNumber.getText().toString();
    }

    @Override
    public void setUserPhoneNumberError(@NonNull String message) {
        mEtPhoneNumber.setError(message);
    }

    @Override
    public String getDeliveryAddress() {
        return mEtAddress.getText().toString();
    }

    @Override
    public void setDeliveryAddressError(@NonNull String message) {
        mEtAddress.setError(message);
    }

    @Override
    public String getDeliveryCityAndCounty() {
        return mEtCityAndCounty.getText().toString();
    }

    @Override
    public void setDeliveryCityAndCountyError(@NonNull String message) {
        mEtCityAndCounty.setError(message);
    }

    @Override
    public String getDeliveryMethod() {
        switch (mRgDeliveryMethod.getCheckedRadioButtonId()) {
            case R.id.rb_delivery_method_express:
                return DELIVERY_EXPRESS;
            case R.id.rb_delivery_method_club:
                return DELIVERY_CLUB;
            default:
                return null;
        }
    }

    @Override
    public void setDeliveryMethodError(@NonNull String message) {

    }
}
