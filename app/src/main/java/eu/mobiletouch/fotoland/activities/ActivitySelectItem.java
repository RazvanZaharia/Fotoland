package eu.mobiletouch.fotoland.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvItems;
import eu.mobiletouch.fotoland.dialogs.DialogOffer;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.mvps.MvpActivitySelectItem;
import eu.mobiletouch.fotoland.presenters.PresenterActivitySelectItem;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.utils.SimpleDividerItemDecoration;
import eu.mobiletouch.fotoland.x_base.BaseToolbarActivity;

/**
 * Created on 24-Aug-16.
 */
public class ActivitySelectItem extends BaseToolbarActivity implements MvpActivitySelectItem {

    @Bind(R.id.vp_banner)
    ViewPager mVpBanners;
    @Bind(R.id.rv_items)
    RecyclerView mRvItems;
    @Bind(R.id.tv_screen_title)
    TextView mTvScreenTitle;

    private PresenterActivitySelectItem mPresenterActivitySelectItem;

    public static void launch(@NonNull Context ctx, @NonNull UserSelections product) {
        Intent productIntent = new Intent(ctx, ActivitySelectItem.class);
        productIntent.putExtra(Constants.USER_SELECTION, product);
        ctx.startActivity(productIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_items;
    }

    @Override
    protected String getScreenName() {
        UserSelections userSelections = (UserSelections) getIntent().getSerializableExtra(Constants.USER_SELECTION);
        if (userSelections != null && userSelections.getSelectedProduct() != null) {
            return userSelections.getSelectedProduct().getName();
        }
        return null;
    }

    @Override
    protected boolean isProductScreen() {
        return true;
    }

    @Override
    protected void init() {
        super.init();
        mVpBanners.setAdapter(new BannerPagerAdapter());
        mRvItems.setLayoutManager(new LinearLayoutManager(this));
        mRvItems.addItemDecoration(new SimpleDividerItemDecoration(this));

        mPresenterActivitySelectItem = new PresenterActivitySelectItem(this);
        mPresenterActivitySelectItem.attachView(this);
        mPresenterActivitySelectItem.init((UserSelections) getIntent().getSerializableExtra(Constants.USER_SELECTION));
    }

    @Override
    public void showItems(@NonNull ArrayList<Item> items, @DrawableRes int offerBannerResId) {
        mRvItems.setAdapter(new AdapterRvItems(this, items, offerBannerResId, mPresenterActivitySelectItem, mPresenterActivitySelectItem));
    }

    @Override
    public void setScreenTitle(@NonNull String screenTitle) {
        mTvScreenTitle.setText(screenTitle);
    }

    @Override
    public void showOfferDialog() {
        DialogOffer.newInstance().show(getSupportFragmentManager(), "");
    }

    private static final class BannerPagerAdapter extends PagerAdapter {
        int[] resIds = new int[]{R.drawable.banner_photo2, R.drawable.regular, R.drawable.square};
        ArrayList<ImageView> convertableViews = new ArrayList<>();

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView pagerImageItem;

            if (convertableViews == null) {
                convertableViews = new ArrayList<>();
            }

            if (convertableViews.isEmpty()) {
                pagerImageItem = new ImageView(container.getContext());
                pagerImageItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                pagerImageItem = convertableViews.remove(0);
            }

            pagerImageItem.setImageResource(resIds[position]);
            container.addView(pagerImageItem);
            return pagerImageItem;
        }

        public void destroyItem(ViewGroup container, int pos, Object object) {
            container.removeView((View) object);
            convertableViews.add((ImageView) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
