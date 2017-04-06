package eu.mobiletouch.fotoland.holders;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.holders.localPhotos.Photo;

/**
 * Created on 11-Sep-16.
 */
public class UserSelections implements Serializable {
    private static final long serialVersionUID = 8110012885610954100L;

    private Product mSelectedProduct;
    private Item mSelectedItem;
    private ArrayList<Photo> mSelectedPhotos;

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
}
