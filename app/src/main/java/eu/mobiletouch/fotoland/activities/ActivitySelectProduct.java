package eu.mobiletouch.fotoland.activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvProducts;
import eu.mobiletouch.fotoland.holders.Product;
import eu.mobiletouch.fotoland.mvps.MvpActivitySelectProduct;
import eu.mobiletouch.fotoland.presenters.PresenterActivitySelectProduct;
import eu.mobiletouch.fotoland.x_base.BaseNavigationDrawerActivity;

public class ActivitySelectProduct extends BaseNavigationDrawerActivity implements MvpActivitySelectProduct {

    @Bind(R.id.rv_products)
    RecyclerView mRvProducts;

    private PresenterActivitySelectProduct mPresenterActivitySelectProduct;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.title_screen_select_product);
    }

    @Override
    protected void init() {
        super.init();
        mRvProducts.setLayoutManager(new LinearLayoutManager(this));

        mPresenterActivitySelectProduct = new PresenterActivitySelectProduct(this);
        mPresenterActivitySelectProduct.attachView(this);
        mPresenterActivitySelectProduct.init();
    }

    @Override
    public void showProducts(@NonNull ArrayList<Product> items) {
        AdapterRvProducts mAdapterRvProducts = new AdapterRvProducts(this, items, mPresenterActivitySelectProduct);
        mRvProducts.setAdapter(mAdapterRvProducts);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenterActivitySelectProduct.detachView();
    }
}
