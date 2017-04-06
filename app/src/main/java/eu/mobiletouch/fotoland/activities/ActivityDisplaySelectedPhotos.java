package eu.mobiletouch.fotoland.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvSelectedPhotos;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.mvps.MvpActivityDisplaySelectedPhotos;
import eu.mobiletouch.fotoland.presenters.PresenterActivityDisplaySelectedPhotos;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.utils.SimpleDividerItemDecoration;
import eu.mobiletouch.fotoland.x_base.BaseSaveToolbarActivity;

/**
 * Created on 28-Aug-16.
 */
public class ActivityDisplaySelectedPhotos extends BaseSaveToolbarActivity implements MvpActivityDisplaySelectedPhotos {

    @Bind(R.id.rv_selectedPhotos)
    RecyclerView mRvSelectedPhotos;

    private AdapterRvSelectedPhotos mAdapterRvSelectedPhotos;

    private PresenterActivityDisplaySelectedPhotos mPresenterActivityDisplaySelectedPhotos;

    public static void launch(@NonNull Context context, @NonNull UserSelections userSelections) {
        Intent displaySelectionIntent = new Intent(context, ActivityDisplaySelectedPhotos.class);
        displaySelectionIntent.putExtra(Constants.USER_SELECTION, userSelections);
        context.startActivity(displaySelectionIntent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_display_selection;
    }

    @Override
    protected void init() {
        super.init();
        mRvSelectedPhotos.setLayoutManager(new LinearLayoutManager(this));
        mRvSelectedPhotos.addItemDecoration(new SimpleDividerItemDecoration(this));

        mPresenterActivityDisplaySelectedPhotos = new PresenterActivityDisplaySelectedPhotos(this);
        mPresenterActivityDisplaySelectedPhotos.attachView(this);
        mPresenterActivityDisplaySelectedPhotos.init(getIntent());
    }

    @Override
    public void showSelectedPhotos(@NonNull ArrayList<Photo> photos, @NonNull Item.ItemType itemType) {
        if (mAdapterRvSelectedPhotos == null) {
            mAdapterRvSelectedPhotos = new AdapterRvSelectedPhotos(this, photos, itemType, mPresenterActivityDisplaySelectedPhotos);
            mRvSelectedPhotos.setAdapter(mAdapterRvSelectedPhotos);
        } else {
            mAdapterRvSelectedPhotos.setSelectedPhotos(photos, itemType);
            mAdapterRvSelectedPhotos.notifyDataSetChanged();
        }
    }

    @Override
    public void notifyAdapter(int position) {
        if (mAdapterRvSelectedPhotos != null) {
            if (position < 0) {
                mAdapterRvSelectedPhotos.notifyDataSetChanged();
            } else {
                mAdapterRvSelectedPhotos.notifyItemChanged(position);
            }
        }
    }

    @Override
    protected void onSaveMenuItemAction() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenterActivityDisplaySelectedPhotos.onActivityResult(requestCode, resultCode, data);
    }
}
