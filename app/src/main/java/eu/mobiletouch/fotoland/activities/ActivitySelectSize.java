package eu.mobiletouch.fotoland.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotoSize;
import eu.mobiletouch.fotoland.enums.ProductType;
import eu.mobiletouch.fotoland.holders.Paper;
import eu.mobiletouch.fotoland.holders.Size;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.mvps.MvpActivitySelectDetails;
import eu.mobiletouch.fotoland.presenters.PresenterActivitySelectDetails;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.x_base.BaseToolbarActivity;

/**
 * Created on 26-Sep-16.
 */
public class ActivitySelectSize extends BaseToolbarActivity implements MvpActivitySelectDetails {

    @Bind(R.id.rv_photobookSizes)
    RecyclerView mRvPhotobookSizes;
    @Bind(R.id.btn_selectPhotos)
    Button mBtnSelectPhotos;

    private PresenterActivitySelectDetails mPresenterActivitySelectDetails;
    private AdapterRvPhotoSize mAdapterRvPhotoSize;

    public static void launch(@NonNull Context ctx, @NonNull UserSelections userSelections) {
        Intent selectDetailsIntent = new Intent(ctx, ActivitySelectSize.class);
        selectDetailsIntent.putExtra(Constants.USER_SELECTION, userSelections);
        ctx.startActivity(selectDetailsIntent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_size;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.screenTitle_selectSize);
    }

    @Override
    protected void init() {
        super.init();
        mRvPhotobookSizes.setLayoutManager(new LinearLayoutManager(this));

        mPresenterActivitySelectDetails = new PresenterActivitySelectDetails(this);
        mPresenterActivitySelectDetails.attachView(this);
        mPresenterActivitySelectDetails.init((UserSelections) getIntent().getSerializableExtra(Constants.USER_SELECTION));
    }

    @Override
    protected void setActions() {
        super.setActions();
        mBtnSelectPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterActivitySelectDetails.onPhotosSelectClick();
            }
        });
    }

    @Override
    public void setSizes(@NonNull ArrayList<Size> sizes) {
        mRvPhotobookSizes.setAdapter(mAdapterRvPhotoSize = new AdapterRvPhotoSize(this, sizes, ProductType.PHOTOBOOK, mPresenterActivitySelectDetails));
    }

    @Override
    public void notifySizesAdapter() {
        mAdapterRvPhotoSize.notifyDataSetChanged();
    }

    @Override
    public void enableSelectPicturesButton(boolean enable) {}
    @Override
    public void setSelectedPhotoSize(@NonNull String photoSize) {}
    @Override
    public void changeVisibility(@IdRes int viewId, int visibility, long delayAlphaAnimationStart) {}
    @Override
    public void setScreenTitle(@NonNull String screenTitle) {}
    @Override
    public void setPapers(@NonNull ArrayList<Paper> papers) {}
    @Override
    public void notifyPapersAdapter() {}
    @Override
    public void setSelectedPaper(@NonNull String paperName) {}
}
