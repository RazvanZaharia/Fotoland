package eu.mobiletouch.fotoland.presenters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import eu.mobiletouch.fotoland.activities.ActivityEditPagePhotobook;
import eu.mobiletouch.fotoland.activities.ActivitySelectPhotos;
import eu.mobiletouch.fotoland.adapters.AdapterRvSelectedPhotos;
import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.interfaces.SelectedPhotoItem;
import eu.mobiletouch.fotoland.mvps.MvpActivityDisplayPhotobookSelectedPhotos;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.utils.Utils;

/**
 * Created on 26-Sep-16.
 */
public class PresenterActivityDisplayPhotobookSelectedPhotos extends PresenterActivityDisplaySelectedPhotos<MvpActivityDisplayPhotobookSelectedPhotos> {

    private static final int PAGES_ADDED = 2;
    private int mAddPhotoAtPosition = -1;
    private int minimumAddPages = 0;
    private int numberOfAddPagesAtListEnd = 0;

    public PresenterActivityDisplayPhotobookSelectedPhotos(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    public void init(@NonNull UserSelections userSelections) {
        super.init(userSelections);
        attachItemTouchHelper();
    }

    @Override
    protected ArrayList<SelectedPhotoItem> getSelectedPhotosList() {
        mSelectedPhotoItems = super.getSelectedPhotosList();
        minimumAddPages = getMinimumAddPages();
        numberOfAddPagesAtListEnd = 0;
        mSelectedPhotoItems.add(1, new SelectedPhotoItem.BlankPage());
        for (int i = 0; i < minimumAddPages; i++) {
            mSelectedPhotoItems.add(new SelectedPhotoItem.AddPage());
            numberOfAddPagesAtListEnd++;
        }
        mSelectedPhotoItems.add(new SelectedPhotoItem.BlankPage());

        return mSelectedPhotoItems;
    }

    private int getMinimumAddPages() {
        int minim;
        if (mUserSelections.getSelectedPhotos() == null || mUserSelections.getSelectedPhotos().size() == 0) {
            minim = 0;
        } else {
            if (mUserSelections.getSelectedPhotos().size() % 2 == 0) {
                minim = 3;
            } else {
                minim = 4;
            }
        }
        return minim;
    }

    private void attachItemTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if (viewHolder instanceof AdapterRvSelectedPhotos.ViewHolderPhotobookSelectedPhoto
                        && ((AdapterRvSelectedPhotos.ViewHolderPhotobookSelectedPhoto) viewHolder).getItem().getDisplayItemType() == SelectedPhotoItem.DisplayItemType.PHOTO
                        && target instanceof AdapterRvSelectedPhotos.ViewHolderPhotobookSelectedPhoto
                        && ((AdapterRvSelectedPhotos.ViewHolderPhotobookSelectedPhoto) target).getItem().getDisplayItemType() == SelectedPhotoItem.DisplayItemType.PHOTO) {

                    Photo currentPhoto = (Photo) ((AdapterRvSelectedPhotos.ViewHolderPhotobookSelectedPhoto) viewHolder).getItem();
                    Photo targetPhoto = (Photo) ((AdapterRvSelectedPhotos.ViewHolderPhotobookSelectedPhoto) target).getItem();

                    Collections.swap(mUserSelections.getSelectedPhotos(), mUserSelections.getSelectedPhotos().indexOf(currentPhoto), mUserSelections.getSelectedPhotos().indexOf(targetPhoto));
                    Collections.swap(mSelectedPhotoItems, mSelectedPhotoItems.indexOf(currentPhoto), mSelectedPhotoItems.indexOf(targetPhoto));
                    getMvpView().showSelectedPhotos(mSelectedPhotoItems, mUserSelections.getSelectedItem(), mUserSelections.getSelectedProduct().getPpiLimit());
                    return true;
                }
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }
        });
        getMvpView().attachToRecyclerView(itemTouchHelper);
    }

    public void onAddPagesClick() {
        for (int i = 0; i < PAGES_ADDED; i++) {
            for (int j = mSelectedPhotoItems.size() - 1; j >= 0; j--) {
                if (mSelectedPhotoItems.get(j).getDisplayItemType() != SelectedPhotoItem.DisplayItemType.BLANK_PAGE) {
                    mSelectedPhotoItems.add(j + 1, new SelectedPhotoItem.AddPage());
                    numberOfAddPagesAtListEnd++;
                    break;
                }
            }
        }
        getMvpView().showSelectedPhotos(mSelectedPhotoItems, mUserSelections.getSelectedItem(), mUserSelections.getSelectedProduct().getPpiLimit());
    }

    @Override
    public void onAddNewPageAt(int atPosition) {
        mSelectedPhotoItems.add(atPosition, new SelectedPhotoItem.AddPage());
        numberOfAddPagesAtListEnd++;
        getMvpView().showSelectedPhotos(mSelectedPhotoItems, mUserSelections.getSelectedItem(), mUserSelections.getSelectedProduct().getPpiLimit());
    }

    @Override
    public void onRemoveNewPageAt(int atPosition) {
        if (mSelectedPhotoItems.get(atPosition).getDisplayItemType() == SelectedPhotoItem.DisplayItemType.ADD_PAGE
                && numberOfAddPagesAtListEnd > minimumAddPages) {
            mSelectedPhotoItems.remove(atPosition);
            numberOfAddPagesAtListEnd--;
            getMvpView().showSelectedPhotos(mSelectedPhotoItems, mUserSelections.getSelectedItem(), mUserSelections.getSelectedProduct().getPpiLimit());
        }
    }

    public void onRemovePagesClick() {
        if (numberOfAddPagesAtListEnd - PAGES_ADDED >= minimumAddPages) {
            for (int i = 0; i < PAGES_ADDED; i++) {
                for (int j = mSelectedPhotoItems.size() - 1; j >= 0; j--) {
                    if (mSelectedPhotoItems.get(j).getDisplayItemType() == SelectedPhotoItem.DisplayItemType.ADD_PAGE) {
                        mSelectedPhotoItems.remove(j);
                        numberOfAddPagesAtListEnd--;
                        break;
                    }
                }
            }
            getMvpView().showSelectedPhotos(mSelectedPhotoItems, mUserSelections.getSelectedItem(), mUserSelections.getSelectedProduct().getPpiLimit());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_MAKE_COLLAGE && resultCode == Activity.RESULT_OK) {
            Photo collagePhoto = new Photo();
            collagePhoto.setPhotoPath(data.getStringExtra(Constants.ARG_FILE_PATH));
            collagePhoto.setId(new Random().nextInt());
            collagePhoto = Utils.setPhotoDimensions(collagePhoto);
            mUserSelections.getSelectedPhotos().add(collagePhoto);
            mSelectedPhotoItems.add(collagePhoto);
            getMvpView().showSelectedPhotos(mSelectedPhotoItems, mUserSelections.getSelectedItem(), mUserSelections.getSelectedProduct().getPpiLimit());
        }

        if (requestCode == Constants.REQUEST_CODE_EDIT_PHOTOBOOK_PAGE && resultCode == Activity.RESULT_OK) {
            mUserSelections.setSelectedPhotos((ArrayList<Photo>) data.getSerializableExtra(Constants.SELECTED_PHOTOS));
            mSelectedPhotoItems = getSelectedPhotosList();
            getMvpView().showSelectedPhotos(mSelectedPhotoItems, mUserSelections.getSelectedItem(), mUserSelections.getSelectedProduct().getPpiLimit());
        }

        if (requestCode == Constants.REQUEST_CODE_SELECT_SINGLE_PICTURE && resultCode == Activity.RESULT_OK) {
            Photo selectedPhoto = (Photo) data.getSerializableExtra(Constants.PHOTO);
            if (selectedPhoto != null) {
                if (mAddPhotoAtPosition > -1 && mAddPhotoAtPosition >= mUserSelections.getSelectedPhotos().size()) {
                    mUserSelections.getSelectedPhotos().add(selectedPhoto);
                    mSelectedPhotoItems.set(mAddPhotoAtPosition, selectedPhoto);
                }

                if (mAddPhotoAtPosition > -1 && mAddPhotoAtPosition < mUserSelections.getSelectedPhotos().size()) {
                    mUserSelections.getSelectedPhotos().add(mAddPhotoAtPosition, selectedPhoto);
                    mSelectedPhotoItems.set(mAddPhotoAtPosition, selectedPhoto);
                }

                mAddPhotoAtPosition = -1;
                minimumAddPages = getMinimumAddPages();
                numberOfAddPagesAtListEnd = 0;
                for (SelectedPhotoItem item : mSelectedPhotoItems) {
                    if (item.getDisplayItemType() == SelectedPhotoItem.DisplayItemType.ADD_PAGE) {
                        numberOfAddPagesAtListEnd++;
                    }
                }

                while(numberOfAddPagesAtListEnd < minimumAddPages) {
                    mSelectedPhotoItems.add(mSelectedPhotoItems.size() - 1, new SelectedPhotoItem.AddPage());
                    numberOfAddPagesAtListEnd ++;
                }
                getMvpView().showSelectedPhotos(mSelectedPhotoItems, mUserSelections.getSelectedItem(), mUserSelections.getSelectedProduct().getPpiLimit());
            }
        }
    }

    @Override
    public void onAddPhoto(int atPosition) {
        mAddPhotoAtPosition = atPosition;
        mActivity.startActivityForResult(ActivitySelectPhotos.getStartIntentForSinglePictureSelect(mActivity), Constants.REQUEST_CODE_SELECT_SINGLE_PICTURE);
    }

    @Override
    public void onPhotoCLick(@NonNull Photo photo) {
        int selectedIndex = mUserSelections.getSelectedPhotos().indexOf(photo);
        for (Photo listPhoto : mUserSelections.getSelectedPhotos()) {
            listPhoto.setPhotobookPage(mSelectedPhotoItems.indexOf(listPhoto));
        }
        ActivityEditPagePhotobook.launchForResult(mActivity, mUserSelections.getSelectedPhotos(), selectedIndex > 0 ? selectedIndex : 0, Constants.REQUEST_CODE_EDIT_PHOTOBOOK_PAGE);
    }
}
