package eu.mobiletouch.fotoland.collage;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created on 29-Oct-16.
 */
public class TouchListener implements View.OnTouchListener {

    private float startX;
    private float startY;

    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = motionEvent.getX();
                startY = motionEvent.getY();

                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                break;
            case MotionEvent.ACTION_UP: {
                float endX = motionEvent.getX();
                float endY = motionEvent.getY();
                if (isAClick(startX, endX, startY, endY)) {
                    view.performClick();
                }
                break;
            }
        }
        return false;
    }

    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        if (differenceX > 5 || differenceY > 5) {
            return false;
        }
        return true;
    }

}