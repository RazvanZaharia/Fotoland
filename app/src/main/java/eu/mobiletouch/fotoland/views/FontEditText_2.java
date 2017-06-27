package eu.mobiletouch.fotoland.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

/**
 * This class will accept Roboto-Medium / Roboto-Black typefaces for all app supported api levels.
 */
public class FontEditText_2 extends TextInputEditText {
    private static final String TAG = FontEditText_2.class.getSimpleName();

    public FontEditText_2(Context context) {
        super(context);
        init(null);
    }

    public FontEditText_2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FontEditText_2(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
//        FontUtils.applyFont(this, getContext(), attrs);
    }
}
