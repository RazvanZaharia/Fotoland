package eu.mobiletouch.fotoland.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.utils.Utils;

@SuppressWarnings("unused")
public class InfoEditText_2 extends FontEditText_2 implements OnTouchListener {
    private static final String TAG = InfoEditText_2.class.getSimpleName();

    private Drawable infoDrawable;
    private static final int INFO_BUBBLE_RES_ID = R.drawable.icon_info_bubble;
    private static final int EXTRA_PADDING = 5; //dp

    private OnInfoListener mListener;
    private OnTouchListener mTouchListener;

    public InfoEditText_2(Context context) {
        super(context);
    }

    public InfoEditText_2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfoEditText_2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setInfoListener(OnInfoListener listener) {
        mListener = listener;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
//        super.setOnTouchListener(l);
        mTouchListener = l;
    }

    private void init() {
        super.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (infoDrawable != null) {
            boolean tappedInfoBubble = event.getX() > (getWidth() - getPaddingRight() - infoDrawable
                    .getIntrinsicWidth() - (int) Utils.convertDpToPixel(EXTRA_PADDING, getContext()));

            if (tappedInfoBubble) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mListener != null) {
                        mListener.onClick();
                    } else {
                        Log.e(TAG,
                                "Missing callback. Should set an OnInfoListener.onClick() listener!");
                    }
                }
                return true;
            }
        }

        if (mTouchListener != null) {
            return mTouchListener.onTouch(v, event);
        }

        return false;
    }


    public void showInfoDrawable(OnInfoListener listener) {
        showInfoDrawable(listener, INFO_BUBBLE_RES_ID);
    }

    public void showInfoDrawable(OnInfoListener listener, int infoBubbleResId) {
        mListener = listener;

        infoDrawable = ContextCompat.getDrawable(getContext(), infoBubbleResId);
        infoDrawable.setBounds(0, 0, infoDrawable.getIntrinsicWidth(),
                infoDrawable.getIntrinsicHeight());
        this.setCompoundDrawables(null, null, infoDrawable, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        infoDrawable = getCompoundDrawables()[2];
    }

    public interface OnInfoListener {
        void onClick();
    }
}