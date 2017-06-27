package eu.mobiletouch.fotoland.views;

import android.content.Context;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.AttributeSet;

import eu.mobiletouch.fotoland.R;

public class PasswordEditText_2 extends InfoEditText_2 implements InfoEditText_2.OnInfoListener {

    public PasswordEditText_2(Context context) {
        super(context);
        init();
    }

    public PasswordEditText_2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasswordEditText_2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        showInfoDrawable(this, R.drawable.ic__gri_abc);
    }

    @Override
    public void onClick() {
        int selectionEnd = getSelectionEnd(); // save selection because it will reset
        final boolean visibleText = getTransformationMethod() instanceof PasswordTransformationMethod;
        setTransformationMethod(visibleText ? SingleLineTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        setSelection(selectionEnd); // restore selection
    }
}
