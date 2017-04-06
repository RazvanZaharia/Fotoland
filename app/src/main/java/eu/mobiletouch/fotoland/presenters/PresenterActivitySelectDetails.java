package eu.mobiletouch.fotoland.presenters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.activities.ActivitySelectPhotos;
import eu.mobiletouch.fotoland.adapters.AdapterRvPaperType;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotoSize;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.Paper;
import eu.mobiletouch.fotoland.holders.Product;
import eu.mobiletouch.fotoland.holders.Size;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.mvps.MvpActivitySelectDetails;
import eu.mobiletouch.fotoland.utils.Constants;

/**
 * Created on 25-Aug-16.
 */
public class PresenterActivitySelectDetails extends BasePresenter<MvpActivitySelectDetails> implements AdapterRvPaperType.ViewHolderPaperType.OnPaperTypeClickListener, AdapterRvPhotoSize.ViewHolderPhotoSize.OnPhotoSizeClickListener {

    private Context mCtx;
    private UserSelections mUserSelections;
    private Paper mSelectedPaper;
    private Size mSelectedSize;

    public PresenterActivitySelectDetails(@NonNull Context ctx) {
        this.mCtx = ctx;
    }

    public void init(@NonNull Intent intent) {
        mUserSelections = (UserSelections) intent.getSerializableExtra(Constants.USER_SELECTION);
        Product selectedProduct = mUserSelections.getSelectedProduct();
        Item selectedItem = mUserSelections.getSelectedItem();

        getMvpView().setScreenTitle(selectedProduct.getName());
        getMvpView().setPapers(selectedItem.getPapers());
        getMvpView().setSizes(selectedItem.getSizes());
    }

    @Override
    public void onPaperSelect(Paper paper) {
        this.mSelectedPaper = paper;
        getMvpView().notifyPapersAdapter();
        getMvpView().setSelectedPaper(mSelectedPaper.getName());
        getMvpView().changeVisibility(R.id.rv_paperType, View.GONE, 1);

        if (getSelectedPhotoSize() == null) {
            getMvpView().changeVisibility(R.id.rv_photoSize, View.VISIBLE, 0);
            getMvpView().changeVisibility(R.id.layout_photoSize, View.VISIBLE, 100);
        }

        checkIfSelectPhotosAvailable();
    }

    @Override
    public Paper getSelectedPaper() {
        return mSelectedPaper;
    }

    @Override
    public void onPhotoSizeSelect(Size size) {
        this.mSelectedSize = size;
        getMvpView().notifySizesAdapter();
        getMvpView().setSelectedPhotoSize(mSelectedSize.getSize());
        getMvpView().changeVisibility(R.id.rv_photoSize, View.GONE, 1);
        getMvpView().changeVisibility(R.id.layout_prices, View.VISIBLE, 100);

        checkIfSelectPhotosAvailable();
    }

    @Override
    public Size getSelectedPhotoSize() {
        return mSelectedSize;
    }

    private void checkIfSelectPhotosAvailable() {
        getMvpView().enableSelectPicturesButton(mSelectedPaper != null && mSelectedSize != null);
    }

    public void onPaperTypeClick() {
        getMvpView().changeVisibility(R.id.rv_photoSize, View.GONE, 1);
        getMvpView().changeVisibility(R.id.rv_paperType, View.VISIBLE, 100);
    }

    public void onPhotoSizeClick() {
        getMvpView().changeVisibility(R.id.rv_paperType, View.GONE, 1);
        getMvpView().changeVisibility(R.id.rv_photoSize, View.VISIBLE, 100);
    }

    public void onPhotosSelectClick() {
        ActivitySelectPhotos.launch(mCtx, mUserSelections);
    }

}
