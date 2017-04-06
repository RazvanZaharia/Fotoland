package eu.mobiletouch.fotoland.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotoAlbums;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotos;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.holders.localPhotos.PhotoAlbum;
import eu.mobiletouch.fotoland.interfaces.OnBackPressedListener;
import eu.mobiletouch.fotoland.interfaces.OnSelectPhotosFragmentsListener;
import eu.mobiletouch.fotoland.mvps.MvpFragmentLocalPhotos;
import eu.mobiletouch.fotoland.presenters.PresenterFragmentLocalPhotos;
import eu.mobiletouch.fotoland.x_base.BaseFragment;

/**
 * Created on 27-Aug-16.
 */
public class FragmentLocalPhotos extends BaseFragment implements MvpFragmentLocalPhotos, OnBackPressedListener {

    @Bind(R.id.rv_photos)
    RecyclerView mRvPhotos;

    private AdapterRvPhotos mAdapterRvPhotos;
    private AdapterRvPhotoAlbums mAdapterRvPhotoAlbums;

    private PresenterFragmentLocalPhotos mPresenterFragmentLocalPhotos;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_local_photos;
    }

    public static FragmentLocalPhotos newInstance() {
        return new FragmentLocalPhotos();
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

        mPresenterFragmentLocalPhotos = new PresenterFragmentLocalPhotos(getActivity(), onSelectPhotosFragmentsListener);
        mPresenterFragmentLocalPhotos.attachView(this);
        mPresenterFragmentLocalPhotos.init();
    }


    @Override
    public void showAlbums(ArrayList<PhotoAlbum> albums) {
        if (mAdapterRvPhotoAlbums == null) {
            mAdapterRvPhotoAlbums = new AdapterRvPhotoAlbums(getContext(), albums, mPresenterFragmentLocalPhotos);
        }
        mRvPhotos.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvPhotos.setAdapter(mAdapterRvPhotoAlbums);
    }

    @Override
    public void showAlbumPhotos(ArrayList<Photo> photos) {
        if (mAdapterRvPhotos == null) {
            mAdapterRvPhotos = new AdapterRvPhotos(getContext(), photos, mPresenterFragmentLocalPhotos);
        } else {
            mAdapterRvPhotos.setPhotos(photos);
            mAdapterRvPhotos.notifyDataSetChanged();
        }

        mRvPhotos.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRvPhotos.setAdapter(mAdapterRvPhotos);
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
        return mRvPhotos.getAdapter() instanceof AdapterRvPhotoAlbums;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenterFragmentLocalPhotos.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onBackPressed() {
        return mPresenterFragmentLocalPhotos.onBackPressed();
    }
}
