package eu.mobiletouch.fotoland.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.enums.ItemType;

/**
 * Created on 27-Sep-16.
 */
public class GridPhotobookItemDecoration extends RecyclerView.ItemDecoration {
    private static final int VERTICAL = OrientationHelper.VERTICAL;

    private int orientation = -1;
    private int spanCount = -1;
    private int spacing;
    private int halfSpacing;

    private ItemType mItemType;

    public GridPhotobookItemDecoration(Context context, @DimenRes int spacingDimen, ItemType itemType) {
        this(context.getResources().getDimensionPixelSize(spacingDimen), itemType);
    }

    public GridPhotobookItemDecoration(int spacingPx, ItemType itemType) {
        spacing = spacingPx;
        halfSpacing = spacing / 2;
        this.mItemType = itemType;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);

        if (orientation == -1) {
            orientation = getOrientation(parent);
        }

        if (spanCount == -1) {
            spanCount = getTotalSpan(parent);
        }

        int childCount = parent.getLayoutManager().getItemCount();
        int childIndex = parent.getChildAdapterPosition(view);

        int itemSpanSize = getItemSpanSize(parent, childIndex);
        int spanIndex = getItemSpanIndex(parent, childIndex);

    /* INVALID SPAN */
        if (spanCount < 1) return;

        setSpacings(outRect, parent, view, childCount, childIndex, itemSpanSize, spanIndex);
    }

    protected void setSpacings(Rect outRect, RecyclerView parent, View childView, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        outRect.top = halfSpacing;
        outRect.bottom = halfSpacing;
        outRect.left = halfSpacing;
        outRect.right = halfSpacing;
        childView.setBackground(null);

        if (isTopEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.top = spacing;
        }

        if (isLeftEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.left = spacing;
            setLeftBackground(childView);
            moveInfoToLeftOfPhoto(childView);
            moveAddToRightOfPhoto(childView);
            moveRemoveToLeftOfPhoto(childView);
        }

        if (isRightEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.right = spacing;
            setRightBackground(childView);
            moveInfoToRightOfPhoto(childView);
            moveAddToLeftOfPhoto(childView);
            moveRemoveToRightOfPhoto(childView);
        }

        if (isBottomEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.bottom = spacing;
        }
    }

    private void setLeftBackground(View view) {
        switch (mItemType) {
            case SQUARE:
                if (view.findViewById(R.id.layout_photo) != null) {
                    view.findViewById(R.id.layout_photo).setBackgroundResource(R.drawable.photobook_canvas_left);
                }
                break;
            case LANDSCAPE:
                if (view.findViewById(R.id.layout_photo) != null) {
                    view.findViewById(R.id.layout_photo).setBackgroundResource(R.drawable.photobook_canvas_landscape_left);
                }
                break;
        }
    }

    private void moveRemoveToLeftOfPhoto(View view) {
        View remove = view.findViewById(R.id.tv_remove);
        if(remove != null) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) remove.getLayoutParams();
            lp.gravity = Gravity.LEFT;
            remove.setLayoutParams(lp);
        }
    }

    private void moveAddToRightOfPhoto(View view) {
        ImageView add = (ImageView) view.findViewById(R.id.img_add_page);
        if(add != null) {
            add.setImageResource(R.drawable.ic_add_left);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) add.getLayoutParams();
            lp.gravity = Gravity.RIGHT;
            add.setLayoutParams(lp);
        }
    }

    private void moveInfoToRightOfPhoto(View view) {
        View tvInfo = view.findViewById(R.id.tv_info);
        if(tvInfo != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) tvInfo.getLayoutParams();
            lp.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
            tvInfo.setLayoutParams(lp);
        }
        View photoLay = view.findViewById(R.id.layout_photo);
        if(photoLay != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) photoLay.getLayoutParams();
            lp.rightMargin = (int) view.getContext().getResources().getDimension(R.dimen.dp_15);
            lp.leftMargin = 0;
            photoLay.setLayoutParams(lp);
        }
    }

    private void setRightBackground(View view) {
        switch (mItemType) {
            case SQUARE:
                if (view.findViewById(R.id.layout_photo) != null) {
                    view.findViewById(R.id.layout_photo).setBackgroundResource(R.drawable.photobook_canvas_right);
                }
                break;
            case LANDSCAPE:
                if (view.findViewById(R.id.layout_photo) != null) {
                    view.findViewById(R.id.layout_photo).setBackgroundResource(R.drawable.photobook_canvas_landscape_right);
                }
                break;
        }
    }

    private void moveRemoveToRightOfPhoto(View view) {
        View remove = view.findViewById(R.id.tv_remove);
        if(remove != null) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) remove.getLayoutParams();
            lp.gravity = Gravity.RIGHT;
            remove.setLayoutParams(lp);
        }
    }

    private void moveAddToLeftOfPhoto(View childView) {
        ImageView add = (ImageView) childView.findViewById(R.id.img_add_page);
        if(add != null) {
            add.setImageResource(R.drawable.ic_add_right);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) add.getLayoutParams();
            lp.gravity = Gravity.LEFT;
            add.setLayoutParams(lp);
        }
    }

    private void moveInfoToLeftOfPhoto(View view) {
        View tvInfo = view.findViewById(R.id.tv_info);
        if(tvInfo != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) tvInfo.getLayoutParams();
            lp.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
            tvInfo.setLayoutParams(lp);
        }
        View photoLay = view.findViewById(R.id.layout_photo);
        if(photoLay != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) photoLay.getLayoutParams();
            lp.rightMargin = 0;
            lp.leftMargin = (int) view.getContext().getResources().getDimension(R.dimen.dp_15);
            photoLay.setLayoutParams(lp);
        }
    }

    @SuppressWarnings("all")
    protected int getTotalSpan(RecyclerView parent) {

        RecyclerView.LayoutManager mgr = parent.getLayoutManager();
        if (mgr instanceof GridLayoutManager) {
            return ((GridLayoutManager) mgr).getSpanCount();
        } else if (mgr instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) mgr).getSpanCount();
        } else if (mgr instanceof LinearLayoutManager) {
            return 1;
        }

        return -1;
    }

    @SuppressWarnings("all")
    protected int getItemSpanSize(RecyclerView parent, int childIndex) {

        RecyclerView.LayoutManager mgr = parent.getLayoutManager();
        if (mgr instanceof GridLayoutManager) {
            return ((GridLayoutManager) mgr).getSpanSizeLookup().getSpanSize(childIndex);
        } else if (mgr instanceof StaggeredGridLayoutManager) {
            return 1;
        } else if (mgr instanceof LinearLayoutManager) {
            return 1;
        }

        return -1;
    }

    @SuppressWarnings("all")
    protected int getItemSpanIndex(RecyclerView parent, int childIndex) {

        RecyclerView.LayoutManager mgr = parent.getLayoutManager();
        if (mgr instanceof GridLayoutManager) {
            return ((GridLayoutManager) mgr).getSpanSizeLookup().getSpanIndex(childIndex, spanCount);
        } else if (mgr instanceof StaggeredGridLayoutManager) {
            return childIndex % spanCount;
        } else if (mgr instanceof LinearLayoutManager) {
            return 0;
        }

        return -1;
    }

    @SuppressWarnings("all")
    protected int getOrientation(RecyclerView parent) {

        RecyclerView.LayoutManager mgr = parent.getLayoutManager();
        if (mgr instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) mgr).getOrientation();
        } else if (mgr instanceof GridLayoutManager) {
            return ((GridLayoutManager) mgr).getOrientation();
        } else if (mgr instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) mgr).getOrientation();
        }

        return VERTICAL;
    }

    protected boolean isLeftEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        if (orientation == VERTICAL) {

            return spanIndex == 0;

        } else {

            return (childIndex == 0) || isFirstItemEdgeValid((childIndex < spanCount), parent, childIndex);
        }
    }

    protected boolean isRightEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        if (orientation == VERTICAL) {

            return (spanIndex + itemSpanSize) == spanCount;

        } else {

            return isLastItemEdgeValid((childIndex >= childCount - spanCount), parent, childCount, childIndex, spanIndex);
        }
    }

    protected boolean isTopEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        if (orientation == VERTICAL) {

            return (childIndex == 0) || isFirstItemEdgeValid((childIndex < spanCount), parent, childIndex);

        } else {

            return spanIndex == 0;
        }
    }

    protected boolean isBottomEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        if (orientation == VERTICAL) {

            return isLastItemEdgeValid((childIndex >= childCount - spanCount), parent, childCount, childIndex, spanIndex);

        } else {

            return (spanIndex + itemSpanSize) == spanCount;
        }
    }

    protected boolean isFirstItemEdgeValid(boolean isOneOfFirstItems, RecyclerView parent, int childIndex) {

        int totalSpanArea = 0;
        if (isOneOfFirstItems) {
            for (int i = childIndex; i >= 0; i--) {
                totalSpanArea = totalSpanArea + getItemSpanSize(parent, i);
            }
        }

        return isOneOfFirstItems && totalSpanArea <= spanCount;
    }

    protected boolean isLastItemEdgeValid(boolean isOneOfLastItems, RecyclerView parent, int childCount, int childIndex, int spanIndex) {

        int totalSpanRemaining = 0;
        if (isOneOfLastItems) {
            for (int i = childIndex; i < childCount; i++) {
                totalSpanRemaining = totalSpanRemaining + getItemSpanSize(parent, i);
            }
        }

        return isOneOfLastItems && (totalSpanRemaining <= spanCount - spanIndex);
    }
}