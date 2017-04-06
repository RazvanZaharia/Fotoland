package eu.mobiletouch.fotoland.fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotoAlbums;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotos;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.holders.localPhotos.PhotoAlbum;
import eu.mobiletouch.fotoland.interfaces.OnBackPressedListener;
import eu.mobiletouch.fotoland.interfaces.OnSelectPhotosFragmentsListener;
import eu.mobiletouch.fotoland.mvps.MvpFragmentFacebookPhotos;
import eu.mobiletouch.fotoland.presenters.PresenterFragmentFacebookPhotos;
import eu.mobiletouch.fotoland.x_base.BaseFragment;

/**
 * Created on 27-Aug-16.
 */
public class FragmentFacebookPhotos extends BaseFragment implements MvpFragmentFacebookPhotos, OnBackPressedListener {

    @Bind(R.id.btn_facebookLogin)
    Button mBtnFacebookLogin;
    @Bind(R.id.tv_loginError)
    TextView mTvLoginError;
    @Bind(R.id.layout_empty)
    View mLayoutEmptyView;
    @Bind(R.id.rv_facebook)
    RecyclerView mRvFacebook;

    private PresenterFragmentFacebookPhotos mPresenterFragmentFacebookPhotos;
    private AdapterRvPhotos mAdapterRvPhotos;
    private AdapterRvPhotoAlbums mAdapterRvPhotoAlbums;

    public static FragmentFacebookPhotos newInstance() {
        return new FragmentFacebookPhotos();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_facebook_photos;
    }

    @Override
    protected void init() {
        super.init();
        OnSelectPhotosFragmentsListener onSelectPhotosFragmentsListener;
        if (activityFragments instanceof OnSelectPhotosFragmentsListener) {
            onSelectPhotosFragmentsListener = (OnSelectPhotosFragmentsListener) activityFragments;
        } else {
            throw new IllegalArgumentException("Activity must implement OnSelectPhotosFragmentsListener");
        }

        mPresenterFragmentFacebookPhotos = new PresenterFragmentFacebookPhotos(getActivity());
        mPresenterFragmentFacebookPhotos.attachView(this);
        mPresenterFragmentFacebookPhotos.init(this, onSelectPhotosFragmentsListener);
    }

    @Override
    protected void setActions() {
        super.setActions();
        mBtnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterFragmentFacebookPhotos.onFacebookLoginClick();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenterFragmentFacebookPhotos.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showLoginErrorMessage(@NonNull String error) {
        mTvLoginError.setVisibility(View.VISIBLE);
        mTvLoginError.setText(error);
    }

    @Override
    public void showEmptyView() {
        mLayoutEmptyView.setVisibility(View.VISIBLE);
        mRvFacebook.setVisibility(View.GONE);
    }

    @Override
    public void showAlbums(@NonNull ArrayList<PhotoAlbum> fbAlbums) {
        mLayoutEmptyView.setVisibility(View.GONE);
        mRvFacebook.setVisibility(View.VISIBLE);

        if (mAdapterRvPhotoAlbums == null) {
            mAdapterRvPhotoAlbums = new AdapterRvPhotoAlbums(getContext(), fbAlbums, mPresenterFragmentFacebookPhotos);
        }
        mRvFacebook.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvFacebook.setAdapter(mAdapterRvPhotoAlbums);
    }

    @Override
    public void showPhotos(@NonNull ArrayList<Photo> photos) {
        if (mAdapterRvPhotos == null) {
            mAdapterRvPhotos = new AdapterRvPhotos(getContext(), photos, mPresenterFragmentFacebookPhotos);
        } else {
            mAdapterRvPhotos.setPhotos(photos);
            mAdapterRvPhotos.notifyDataSetChanged();
        }

        mRvFacebook.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRvFacebook.setAdapter(mAdapterRvPhotos);
    }

    @Override
    public void notifyPhotosAdapter(int position) {
        if (mAdapterRvPhotos != null) {
            if (position < 0) {
                mAdapterRvPhotos.notifyDataSetChanged();
            } else {
                mAdapterRvPhotos.notifyItemChanged(position);
            }
        }
    }

    @Override
    public boolean isShowingAlbums() {
        return mRvFacebook.getAdapter() instanceof AdapterRvPhotoAlbums;
    }

    @Override
    public boolean onBackPressed() {
        return mPresenterFragmentFacebookPhotos.onBackPressed();
    }
}
