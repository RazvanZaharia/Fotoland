package eu.mobiletouch.fotoland.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvPaperType;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotoSize;
import eu.mobiletouch.fotoland.holders.Paper;
import eu.mobiletouch.fotoland.holders.Size;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.mvps.MvpActivitySelectDetails;
import eu.mobiletouch.fotoland.presenters.PresenterActivitySelectDetails;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.x_base.BaseToolbarActivity;

/**
 * Created on 25-Aug-16.
 */
public class ActivitySelectDetails extends BaseToolbarActivity implements MvpActivitySelectDetails {

    private static final int ALPHA_DURATION = 100;

    @Bind(R.id.tv_screen_title)
    TextView mTvScreenTitle;

    @Bind(R.id.layout_paperType)
    RelativeLayout mLayoutPaperType;
    @Bind(R.id.tv_selected_paperType)
    TextView mTvSelectedPaperType;
    @Bind(R.id.rv_paperType)
    RecyclerView mRvPaperType;

    @Bind(R.id.layout_photoSize)
    RelativeLayout mLayoutPhotoSize;
    @Bind(R.id.tv_selected_photoSize)
    TextView mTvSelectedPhotoSize;
    @Bind(R.id.rv_photoSize)
    RecyclerView mRvPhotoSize;

    @Bind(R.id.btn_selectPhotos)
    Button mBtnSelectPhotos;

    private AdapterRvPaperType mAdapterRvPaperType;
    private AdapterRvPhotoSize mAdapterRvPhotoSize;

    public static void launch(@NonNull Context ctx, @NonNull UserSelections userSelections) {
        Intent selectDetailsIntent = new Intent(ctx, ActivitySelectDetails.class);
        selectDetailsIntent.putExtra(Constants.USER_SELECTION, userSelections);
        ctx.startActivity(selectDetailsIntent);
    }

    private PresenterActivitySelectDetails mPresenterActivitySelectDetails;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_details;
    }

    @Override
    protected void init() {
        super.init();
        mRvPaperType.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRvPhotoSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mPresenterActivitySelectDetails = new PresenterActivitySelectDetails(this);
        mPresenterActivitySelectDetails.attachView(this);
        mPresenterActivitySelectDetails.init(getIntent());
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

        mLayoutPaperType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterActivitySelectDetails.onPaperTypeClick();
            }
        });

        mLayoutPhotoSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterActivitySelectDetails.onPhotoSizeClick();
            }
        });
    }

    @Override
    public void setScreenTitle(@NonNull String screenTitle) {
        mTvScreenTitle.setText(screenTitle);
    }

    @Override
    public void setPapers(@NonNull ArrayList<Paper> papers) {
        mRvPaperType.setAdapter(mAdapterRvPaperType = new AdapterRvPaperType(this, papers, mPresenterActivitySelectDetails));
    }

    @Override
    public void setSizes(@NonNull ArrayList<Size> sizes) {
        mRvPhotoSize.setAdapter(mAdapterRvPhotoSize = new AdapterRvPhotoSize(this, sizes, mPresenterActivitySelectDetails));
    }

    @Override
    public void notifyPapersAdapter() {
        mAdapterRvPaperType.notifyDataSetChanged();
    }

    @Override
    public void notifySizesAdapter() {
        mAdapterRvPhotoSize.notifyDataSetChanged();
    }

    @Override
    public void setSelectedPaper(@NonNull String paperName) {
        mTvSelectedPaperType.setText(paperName);
    }

    @Override
    public void setSelectedPhotoSize(@NonNull String photoSize) {
        mTvSelectedPhotoSize.setText(photoSize);
    }

    @Override
    public void changeVisibility(@IdRes int viewId, final int visibility, long delayAlphaAnimationStart) {
        final View viewToAnimate = findViewById(viewId);
        if (delayAlphaAnimationStart == 0) {
            viewToAnimate.setVisibility(visibility);
        } else {
            if (visibility == View.VISIBLE) {
                viewToAnimate.animate().alpha(1.0f).setStartDelay(delayAlphaAnimationStart).setDuration(ALPHA_DURATION).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        viewToAnimate.setVisibility(visibility);
                    }
                });
            } else {
                viewToAnimate.animate().alpha(0.0f).setStartDelay(delayAlphaAnimationStart).setDuration(ALPHA_DURATION).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        viewToAnimate.setVisibility(visibility);
                    }
                });
            }
        }
    }

    @Override
    public void enableSelectPicturesButton(boolean enable) {
        mBtnSelectPhotos.setEnabled(enable);
    }
}
