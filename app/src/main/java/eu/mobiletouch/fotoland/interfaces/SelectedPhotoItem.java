package eu.mobiletouch.fotoland.interfaces;

import static eu.mobiletouch.fotoland.interfaces.SelectedPhotoItem.DisplayItemType.ADD_PAGE;
import static eu.mobiletouch.fotoland.interfaces.SelectedPhotoItem.DisplayItemType.BLANK_PAGE;

/**
 * Created on 12-Nov-16.
 */
public interface SelectedPhotoItem {
    public enum DisplayItemType {
        PHOTO, BLANK_PAGE, ADD_PAGE
    }

    public DisplayItemType getDisplayItemType();

    public static class AddPage implements SelectedPhotoItem {
        @Override
        public DisplayItemType getDisplayItemType() {
            return ADD_PAGE;
        }
    }

    public static class BlankPage implements SelectedPhotoItem {
        @Override
        public DisplayItemType getDisplayItemType() {
            return BLANK_PAGE;
        }
    }
}
