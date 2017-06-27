package eu.mobiletouch.fotoland.holders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import eu.mobiletouch.fotoland.holders.localPhotos.Photo;

/**
 * Created on 11-Sep-16.
 */
public class UserSelections implements Serializable {
    private static final long serialVersionUID = 8110012885610954100L;

    private int mId;
    private Product mSelectedProduct;
    private Item mSelectedItem;
    private ArrayList<Photo> mSelectedPhotos;
    private int mPhotosCount;
    private int mQuantity = 1;
    private Photo mPosterPhoto;

    public UserSelections() {
        mId = new Random().nextInt();
    }

    public Item getSelectedItem() {
        return mSelectedItem;
    }

    public void setSelectedItem(Item selectedItem) {
        mSelectedItem = selectedItem;
    }

    public ArrayList<Photo> getSelectedPhotos() {
        return mSelectedPhotos;
    }

    public void setSelectedPhotos(ArrayList<Photo> selectedPhotos) {
        mSelectedPhotos = selectedPhotos;
    }

    public Product getSelectedProduct() {
        return mSelectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        mSelectedProduct = selectedProduct;
    }

    public int getPhotosCount() {
        return mPhotosCount;
    }

    public void setPhotosCount(int photosCount) {
        mPhotosCount = photosCount;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UserSelections
                && this.mId == ((UserSelections) o).mId
                && this.mSelectedProduct != null
                && this.mSelectedProduct.equals(((UserSelections) o).mSelectedProduct)
                && this.mSelectedItem != null
                && this.mSelectedItem.equals(((UserSelections) o).mSelectedItem);
    }

    public Photo getPosterPhoto() {
        return mPosterPhoto;
    }

    public void setPosterPhoto(Photo posterPhoto) {
        mPosterPhoto = posterPhoto;
    }
}
