package eu.mobiletouch.fotoland.presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import eu.mobiletouch.fotoland.activities.ActivitySelectDetails;
import eu.mobiletouch.fotoland.adapters.AdapterRvItems;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.Product;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.mvps.MvpActivitySelectItem;

/**
 * Created on 24-Aug-16.
 */
public class PresenterActivitySelectItem extends BasePresenter<MvpActivitySelectItem> implements AdapterRvItems.OnItemClickListener, AdapterRvItems.ViewHolderOfferBanner.OnOfferBannerClickListener {

    private Context mCtx;
    private UserSelections mUserSelections;

    public PresenterActivitySelectItem(@NonNull Context context) {
        this.mCtx = context;
    }

    public void init(@NonNull UserSelections userSelections) {
        this.mUserSelections = userSelections;
        Product selectedProduct = userSelections.getSelectedProduct();
        if (getMvpView() != null) {
            getMvpView().setScreenTitle(selectedProduct.getName());
            getMvpView().showItems(selectedProduct.getItems(), selectedProduct.getBannerResId());
        }
    }

    @Override
    public void onItemClick(Item item) {
        mUserSelections.setSelectedItem(item);
        ActivitySelectDetails.launch(mCtx, mUserSelections);
    }

    @Override
    public void onOfferBannerClick() {
        getMvpView().showOfferDialog();
    }
}
