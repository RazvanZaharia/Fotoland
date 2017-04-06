package eu.mobiletouch.fotoland.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import eu.mobiletouch.fotoland.R;

/**
 * Created on 25-Aug-16.
 */
public class DialogOffer extends DialogFragment {

    public static DialogOffer newInstance() {
        DialogOffer frag = new DialogOffer();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getDecorView().getRootView().setBackgroundResource(android.R.color.transparent);

        return  inflater.inflate(R.layout.dialog_offer, container, true);
    }
}
