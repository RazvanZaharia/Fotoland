package eu.mobiletouch.fotoland.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

import eu.mobiletouch.fotoland.R;


public class DialogManager {
    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context, R.style.TransparentProgressDialog);
        try {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_bar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

}
