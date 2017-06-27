package eu.mobiletouch.fotoland.presenters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.activities.ActivityCheckout;
import eu.mobiletouch.fotoland.activities.ActivitySelectPhotos;
import eu.mobiletouch.fotoland.activities.ActivitySelectProduct;
import eu.mobiletouch.fotoland.adapters.AdapterRvCartProducts;
import eu.mobiletouch.fotoland.dao.Dao;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.mvps.MvpActivityCart;

/**
 * Created on 19-Sep-16.
 */
public class PresenterActivityCart extends BasePresenter<MvpActivityCart> implements AdapterRvCartProducts.ViewHolderCartProduct.OnCartProductListener {

    private Context mCtx;
    private ArrayList<UserSelections> mCartProducts;
    private AccessToken mFbAccessToken;

    public PresenterActivityCart(@NonNull Context ctx) {
        this.mCtx = ctx;
    }

    @Override
    public void attachView(MvpActivityCart mvpView) {
        super.attachView(mvpView);
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                mFbAccessToken = newAccessToken;
            }
        };
        accessTokenTracker.startTracking();
        mFbAccessToken = AccessToken.getCurrentAccessToken();
    }

    public void init() {
        mCartProducts = Dao.get(mCtx).readAllCartProducts();
        getMvpView().showCartProducts(mCartProducts);
    }

    public void onAddProductClick() {
        getMvpView().finishAll();
        ActivitySelectProduct.launch(mCtx);
    }

    public void onCheckoutClick() {
//ToDO change this
        ActivityCheckout.launch(mCtx);
       /* if (mFbAccessToken != null && !mFbAccessToken.isExpired()) {
            ActivityCheckout.launch(mCtx);
        } else {
            ActivityGoToAccount.launch(mCtx);
        }*/
    }

    /**
     * START OnCartProductListener methods
     */

    @Override
    public void onProductClick(UserSelections cartProduct, int position) {
        ActivitySelectPhotos.launch(mCtx, cartProduct);
    }

    @Override
    public void onIncreaseQuantity(UserSelections cartProduct, int position) {
        cartProduct.setQuantity(cartProduct.getQuantity() + 1);
        getMvpView().notifyAdapter(position);
    }

    @Override
    public void onDecreaseQuantity(UserSelections cartProduct, int position) {
        if (cartProduct.getQuantity() == 1) {
            onDelete(cartProduct, position);
        } else {
            cartProduct.setQuantity(cartProduct.getQuantity() - 1);
            getMvpView().notifyAdapter(position);
        }
    }

    @Override
    public void onDelete(final UserSelections cartProduct, final int position) {
        new AlertDialog.Builder(mCtx)
                .setTitle(R.string.title_alertDialog_onCartProductDelete)
                .setMessage(R.string.message_alertDialog_onCartProductDelete)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Dao.get(mCtx).deleteCartProduct(cartProduct);
                        mCartProducts.remove(cartProduct);
                        getMvpView().showCartProducts(mCartProducts);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * END OnCartProductListener methods
     */


}
