package eu.mobiletouch.fotoland.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvSelectedPhotos;
import eu.mobiletouch.fotoland.dialogs.DialogAddText;
import eu.mobiletouch.fotoland.enums.ProductType;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.interfaces.SelectedPhotoItem;
import eu.mobiletouch.fotoland.mvps.MvpActivityDisplayPhotobookSelectedPhotos;
import eu.mobiletouch.fotoland.presenters.PresenterActivityDisplayPhotobookSelectedPhotos;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.utils.GridPhotobookItemDecoration;
import eu.mobiletouch.fotoland.x_base.BaseSaveToolbarActivity;

/**
 * Created on 26-Sep-16.
 */
public class ActivityDisplayPhotobookSelectedPhotos extends BaseSaveToolbarActivity implements MvpActivityDisplayPhotobookSelectedPhotos {

    @Bind(R.id.rv_selectedPhotos)
    RecyclerView mRvSelectedPhotos;
    @Bind(R.id.btn_addPages)
    View mVAddPages;
    @Bind(R.id.btn_removePages)
    View mVRemovePages;

    private UserSelections mUserSelections;
    private AdapterRvSelectedPhotos mAdapterRvSelectedPhotos;
    private PresenterActivityDisplayPhotobookSelectedPhotos mPresenterActivityDisplayPhotobookSelectedPhotos;

    public static void launch(@NonNull Context context, @NonNull UserSelections userSelections) {
        Intent displaySelectionIntent = new Intent(context, ActivityDisplayPhotobookSelectedPhotos.class);
        displaySelectionIntent.putExtra(Constants.USER_SELECTION, userSelections);
        context.startActivity(displaySelectionIntent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_display_photobook_selection;
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
        mUserSelections = (UserSelections) getIntent().getSerializableExtra(Constants.USER_SELECTION);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                }
                return 1;
            }
        });

        mRvSelectedPhotos.setLayoutManager(gridLayoutManager);
        mRvSelectedPhotos.addItemDecoration(new GridPhotobookItemDecoration(1, mUserSelections.getSelectedItem().getItemType()));

        mPresenterActivityDisplayPhotobookSelectedPhotos = new PresenterActivityDisplayPhotobookSelectedPhotos(this);
        mPresenterActivityDisplayPhotobookSelectedPhotos.attachView(this);
        mPresenterActivityDisplayPhotobookSelectedPhotos.init(mUserSelections);
    }

    @Override
    protected void setActions() {
        super.setActions();
        mVAddPages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterActivityDisplayPhotobookSelectedPhotos.onAddPagesClick();
            }
        });
        mVRemovePages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterActivityDisplayPhotobookSelectedPhotos.onRemovePagesClick();
            }
        });
    }

    @Override
    public void attachToRecyclerView(ItemTouchHelper itemTouchHelper) {
        itemTouchHelper.attachToRecyclerView(mRvSelectedPhotos);
    }

    @Override
    public void notifyAdapterItemMoved(@NonNull ArrayList<SelectedPhotoItem> photos, int oldPosition, int newPosition) {
        mAdapterRvSelectedPhotos.updateDataSet(photos);
        mAdapterRvSelectedPhotos.notifyItemMoved(oldPosition, newPosition);
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
    public void showSelectedPhotos(@NonNull ArrayList<SelectedPhotoItem> photos, @NonNull Item selectedItem, float ppiLimit) {
        if (mAdapterRvSelectedPhotos == null) {
            mAdapterRvSelectedPhotos = new AdapterRvSelectedPhotos(this, ProductType.PHOTOBOOK, mPresenterActivityDisplayPhotobookSelectedPhotos);
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
    public void finishAllProductScreens() {
        closeSelectProductsActivities();
    }

    @Override
    public void showDialogAddText(String text, @NonNull DialogAddText.OnAddTextListener onAddTextListener) {
        DialogAddText.newInstance(text).setOnAddTextListener(onAddTextListener).show(getSupportFragmentManager(), "");
    }

    @Override
    protected void onSaveMenuItemAction() {
        mPresenterActivityDisplayPhotobookSelectedPhotos.onConfirmClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenterActivityDisplayPhotobookSelectedPhotos.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finishSelf() {
        finish();
    }
}
