package eu.mobiletouch.fotoland.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.activities.ActivitySelectItem;
import eu.mobiletouch.fotoland.adapters.AdapterRvProducts;
import eu.mobiletouch.fotoland.holders.Product;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.holders.products.ProductCanvas;
import eu.mobiletouch.fotoland.holders.products.ProductExtra;
import eu.mobiletouch.fotoland.holders.products.ProductPhotobook;
import eu.mobiletouch.fotoland.holders.products.ProductPosters;
import eu.mobiletouch.fotoland.holders.products.ProductPrints;
import eu.mobiletouch.fotoland.mvps.MvpActivitySelectProduct;

/**
 * Created on 08-Aug-16.
 */
public class PresenterActivitySelectProduct extends BasePresenter<MvpActivitySelectProduct> implements AdapterRvProducts.OnProductClickListener {

    private Context mCtx;

    public PresenterActivitySelectProduct(@NonNull Context context) {
        this.mCtx = context;
    }

    public void init() {
        getMvpView().showProducts(getData());
    }

    private ArrayList<Product> getData() {
        ArrayList<Product> items = new ArrayList<>();
        items.add(new ProductPrints(mCtx));
        items.add(new ProductPosters(mCtx));
        items.add(new ProductCanvas(mCtx));
        items.add(new ProductPhotobook(mCtx));
        items.add(new ProductExtra(mCtx));
        return items;
    }

    @Override
    public void onProductClick(Product product) {
        if(product.getItems() == null || product.getItems().size() == 0) {
            Toast.makeText(mCtx, "No Items to select", Toast.LENGTH_LONG).show();
        }
        else {
            UserSelections userSelections = new UserSelections();
            userSelections.setSelectedProduct(product);
            ActivitySelectItem.launch(mCtx, userSelections);
        }
    }
}
