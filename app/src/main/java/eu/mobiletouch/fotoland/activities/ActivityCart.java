package eu.mobiletouch.fotoland.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvCartProducts;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.mvps.MvpActivityCart;
import eu.mobiletouch.fotoland.presenters.PresenterActivityCart;
import eu.mobiletouch.fotoland.utils.SimpleDividerItemDecoration;
import eu.mobiletouch.fotoland.x_base.BaseNavigationDrawerActivity;

/**
 * Created on 15-Sep-16.
 */
public class ActivityCart extends BaseNavigationDrawerActivity implements MvpActivityCart {

    @Bind(R.id.rv_cartProducts)
    RecyclerView mRvCartProducts;
    @Bind(R.id.tv_totalPrice)
    TextView mTvTotalPrice;
    @Bind(R.id.btn_addProduct)
    Button mBtnAddProduct;
    @Bind(R.id.btn_checkout)
    Button mBtnCheckout;

    private PresenterActivityCart mPresenterActivityCart;
    private AdapterRvCartProducts mAdapterRvCartProducts;

    public static void launch(@NonNull Context ctx) {
        ctx.startActivity(new Intent(ctx, ActivityCart.class));
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.screenTitle_cartActivity);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cart;
    }

    @Override
    protected boolean showCartIcon() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        mRvCartProducts.setLayoutManager(new LinearLayoutManager(this));
        mRvCartProducts.addItemDecoration(new SimpleDividerItemDecoration(this));

        mPresenterActivityCart = new PresenterActivityCart(this);
        mPresenterActivityCart.attachView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenterActivityCart.init();
    }

    @Override
    protected void setActions() {
        super.setActions();
        mBtnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterActivityCart.onAddProductClick();
            }
        });
        mBtnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterActivityCart.onCheckoutClick();
            }
        });
    }

    @Override
    public void showCartProducts(@NonNull ArrayList<UserSelections> cartProducts) {
        if (mAdapterRvCartProducts == null) {
            mAdapterRvCartProducts = new AdapterRvCartProducts(this, mPresenterActivityCart);
            mAdapterRvCartProducts.setDataSet(cartProducts);
            mRvCartProducts.setAdapter(mAdapterRvCartProducts);
        } else {
            mAdapterRvCartProducts.setDataSet(cartProducts);
            mAdapterRvCartProducts.notifyDataSetChanged();
        }
    }

    @Override
    public void notifyAdapter(int position) {
        if (mAdapterRvCartProducts != null) {
            if (position < 0) {
                mAdapterRvCartProducts.notifyDataSetChanged();
            } else {
                mAdapterRvCartProducts.notifyItemChanged(position);
            }
        }
    }

    @Override
    public void setTotalPrice(String totalPrice) {
        mTvTotalPrice.setText(totalPrice);
    }

    @Override
    public void finishAll() {
        closeAllActivities();
    }
}
