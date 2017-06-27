package eu.mobiletouch.fotoland.collage;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created on 29-Oct-16.
 */
public class DragListener implements View.OnDragListener {

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
//                v.setBackgroundDrawable(enterShape);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
//                v.setBackgroundDrawable(normalShape);
                break;
            case DragEvent.ACTION_DROP:
                // Dropped, reassign View to ViewGroup
                View draggedView = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) draggedView.getParent();
                owner.removeView(draggedView);

                LinearLayout container = (LinearLayout) v;

                View replacedView = container.getChildAt(0);
                container.removeView(replacedView);
                owner.addView(replacedView);

                container.addView(draggedView);
                draggedView.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
//                v.setBackgroundDrawable(normalShape);
            default:
                break;
        }
        return true;
    }
}