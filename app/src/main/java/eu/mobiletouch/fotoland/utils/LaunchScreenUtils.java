package eu.mobiletouch.fotoland.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import eu.mobiletouch.fotoland.activities.ActivityDisplayPhotobookSelectedPhotos;
import eu.mobiletouch.fotoland.activities.ActivityDisplayPosterSelectedPhotos;
import eu.mobiletouch.fotoland.activities.ActivityDisplaySelectedPhotos;
import eu.mobiletouch.fotoland.activities.ActivitySelectDetails;
import eu.mobiletouch.fotoland.activities.ActivitySelectSize;
import eu.mobiletouch.fotoland.holders.UserSelections;

/**
 * Created on 26-Sep-16.
 */
public class LaunchScreenUtils {

    public static void launchDetailsActivity(@NonNull Context ctx, @NonNull UserSelections userSelections) {
        if ((userSelections.getSelectedItem().getPapers() == null || userSelections.getSelectedItem().getPapers().size() == 0)
                && (userSelections.getSelectedItem().getSizes() != null && userSelections.getSelectedItem().getSizes().size() >= 0)) { //if item doesn't have paper types, select only size
            ActivitySelectSize.launch(ctx, userSelections);
        } /*else if ((userSelections.getSelectedItem().getPapers() == null || userSelections.getSelectedItem().getPapers().size() == 0)
                && (userSelections.getSelectedItem().getSizes() == null || userSelections.getSelectedItem().getSizes().size() == 0)) {//if item doesn't have paper types neither size types, direct select photos
            ActivitySelectPhotos.launch(ctx, userSelections);
        }*/ else {
            ActivitySelectDetails.launch(ctx, userSelections);
        }
    }

    public static void launchDisplaySelected(@NonNull Context ctx, @NonNull UserSelections userSelections) {
        switch (userSelections.getSelectedProduct().getProductType()) {
            case PHOTOBOOK:
                ActivityDisplayPhotobookSelectedPhotos.launch(ctx, userSelections);
                break;
            case POSTERS:
                ActivityDisplayPosterSelectedPhotos.launch(ctx, userSelections);
                break;
            default:
                ActivityDisplaySelectedPhotos.launch(ctx, userSelections);
        }
    }

}
