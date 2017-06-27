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
import eu.mobiletouch.fotoland.dialogs.DialogAddText;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.interfaces.SelectedPhotoItem;
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
    protected String getScreenName() {
        UserSelections userSelections = (UserSelections) getIntent().getSerializableExtra(Constants.USER_SELECTION);
        if (userSelections != null && userSelections.getSelectedItem() != null && userSelections.getSelectedProduct() != null) {
            return userSelections.getSelectedItem().getName().concat(" ").concat(userSelections.getSelectedProduct().getName());
        }
        return null;
    }

    @Override
    protected boolean isProductScreen() {
        return true;
    }

    @Override
    protected boolean showCartIcon() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        mRvSelectedPhotos.setLayoutManager(new LinearLayoutManager(this));
        mRvSelectedPhotos.addItemDecoration(new SimpleDividerItemDecoration(this));

        mPresenterActivityDisplaySelectedPhotos = new PresenterActivityDisplaySelectedPhotos(this);
        mPresenterActivityDisplaySelectedPhotos.attachView(this);
        mPresenterActivityDisplaySelectedPhotos.init((UserSelections) getIntent().getSerializableExtra(Constants.USER_SELECTION));
    }

    @Override
    public void showSelectedPhotos(@NonNull ArrayList<SelectedPhotoItem> photos, @NonNull Item selectedItem, float ppiLimit) {
        if (mAdapterRvSelectedPhotos == null) {
            mAdapterRvSelectedPhotos = new AdapterRvSelectedPhotos(this, mPresenterActivityDisplaySelectedPhotos);
            mAdapterRvSelectedPhotos.setSelectedPhotos(photos, selectedItem, ppiLimit);
            mRvSelectedPhotos.setAdapter(mAdapterRvSelectedPhotos);
        } else {
            mAdapterRvSelectedPhotos.setSelectedPhotos(photos, selectedItem, ppiLimit);
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
    public void notifyAdapterItemRemoved(int position) {
        if (mAdapterRvSelectedPhotos != null) {
            if (position < 0) {
                mAdapterRvSelectedPhotos.notifyDataSetChanged();
            } else {
                mAdapterRvSelectedPhotos.notifyItemRemoved(position);
            }
        }
    }

    @Override
    public void finishAllProductScreens() {
        closeSelectProductsActivities();
    }

    @Override
    public void showDialogAddText(String text, @NonNull DialogAddText.OnAddTextListener onAddTextListener) {
        DialogAddText.newInstance(text).setOnAddTextListener(onAddTextListener).show(getSupportFragmentManager(), "");
    }

    @Override
    protected void onSaveMenuItemAction() {
        mPresenterActivityDisplaySelectedPhotos.onConfirmClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenterActivityDisplaySelectedPhotos.onActivityResult(requestCode, resultCode, data);
    }
}
