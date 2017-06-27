package eu.mobiletouch.fotoland.collage;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;

/**
 * Created on 12-Oct-16.
 */
public class CollageImageView extends ImageView implements GestureDetector.OnGestureListener,
        ScaleGestureDetector.OnScaleGestureListener,
        View.OnDragListener {

    private static final boolean DEBUG = false;
    private static final float SCALE_MAX = 1.8F;
    private static final float SCALE_MIN = 1.0F;
    private static final String TAG = "CollageImageView";
    private Bitmap mBitmap;
    private int mBitmapHeight;
    private int mBitmapWidth;
    private GestureDetector mGestureDector;
    private Matrix mImageViewmatrix = new Matrix();
    private Photo mItem;
    private CollageChangeImageListener mListner;
    float[] mMatrixValues = new float[9];
    private boolean mMoving = false;
    private int mOrgBitmapHeight;
    private int mOrgBitmapWidth;
    private boolean mPause = false;
    private int mPrevX = 0;
    private int mPrevY = 0;
    private int mResizedHeight;
    private int mResizedWidth;
    private ScaleGestureDetector mSGestureDector;
    private float mScaleFactor = 1.0F;
    private boolean mScaling = false;

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            CollageImageView.this.getViewTreeObserver().removeOnGlobalLayoutListener(CollageImageView.this.mOnGlobalLayoutListener);
            int i = CollageImageView.this.mResizedWidth;
            int j = CollageImageView.this.mResizedHeight;
//            LGCollageImageView.access$102(CollageImageView.this, CollageImageView.this.getMeasuredWidth());
//            LGCollageImageView.access$202(CollageImageView.this, CollageImageView.this.getMeasuredHeight());
            if ((i != CollageImageView.this.mResizedWidth) || (j != CollageImageView.this.mResizedHeight)) {
                CollageImageView.this.setImageFrameFocusd(false);
                CollageImageView.this.updateImageBitmap();
            }
        }
    };

    public CollageImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ImageView.ScaleType.MATRIX);
//        this.mGestureDector = new GestureDetector(context, this);
//        this.mSGestureDector = new ScaleGestureDetector(context, this);
//        setOnDragListener(this);
        getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
    }

    /**
     * Interfaces methods
     */

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        this.mListner.onCollageImageReplaced(getId());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (this.mPause) {
            return;
        }
        startDrag(ClipData.newPlainText("", ""), new View.DragShadowBuilder(this), this, 0);
        setVisibility(INVISIBLE);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        this.mListner.onDrag(view, dragEvent);
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (this.mScaling) {
            return false;
        }

        if (!this.mMoving) {
            this.mMoving = true;
        }

        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        this.mScaling = true;
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float f = this.mScaleFactor;
        this.mScaleFactor *= detector.getScaleFactor();
        this.mScaleFactor = Math.max(1.0F, Math.min(this.mScaleFactor, 1.8F));
        if (Float.compare(this.mScaleFactor, 1.0F) >= 0) {
            f = this.mScaleFactor / f;
            this.mImageViewmatrix.postScale(f, f, this.mResizedWidth / 2, this.mResizedHeight / 2);
        }

        setImageMatrix(this.mImageViewmatrix);
        this.mImageViewmatrix.postScale(this.mScaleFactor, this.mScaleFactor, this.mResizedWidth / 2, this.mResizedHeight / 2);
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((this.mListner == null) || (this.mListner.onTouchEvent(this, event))) {

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            this.mMoving = false;
            this.mScaling = false;
        }
        this.mSGestureDector.onTouchEvent(event);
        this.mGestureDector.onTouchEvent(event);
        return true;
    }

    /*****/

    public void pause() {
        this.mPause = true;
    }

    public void resume() {
        this.mPause = false;
    }

    public void setImageFrameFocusd(boolean paramBoolean) {
        LinearLayout localLinearLayout = (LinearLayout) getParent().getParent();
        if (paramBoolean) {
            localLinearLayout.setBackgroundResource(R.drawable.gallery_collage_focus);
            return;
        }
        localLinearLayout.setBackgroundResource(0);
    }

    public void setImageItem(Photo paramMediaItem, Bitmap paramBitmap) {
        this.mItem = paramMediaItem;
        this.mBitmap = paramBitmap;
    }

    public void setImageChangeListner(CollageChangeImageListener paramCollageChangeImageListener) {
        this.mListner = paramCollageChangeImageListener;
    }

    public void clearData() {
        if (this.mBitmap != null) {
            this.mBitmap.recycle();
        }
        Object localObject = getDrawable();
        if ((localObject instanceof BitmapDrawable)) {
            localObject = ((BitmapDrawable) localObject).getBitmap();
            if (localObject != null) {
                ((Bitmap) localObject).recycle();
            }
        }
    }

    private Bitmap getRotateImage(Bitmap paramBitmap) {
        if (paramBitmap == null) {
            return null;
        }
        int i = paramBitmap.getWidth();
        int j = paramBitmap.getHeight();
        Matrix localMatrix = new Matrix();
        localMatrix.postRotate(this.mItem.getRotation());
        try {
            paramBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, i, j, localMatrix, true);
            return paramBitmap;
        } catch (OutOfMemoryError e) {
            Log.w("CollageImageView", "[getRotateImage] createBitmap OutOfMemoryError ");
            return null;
        } catch (Exception e) {
            Log.w("CollageImageView", "[getRotateImage] createBitmap exception ");
        }
        return null;
    }

    public void updateImageBitmap() {
        Bitmap localBitmap = mBitmap;
        if (localBitmap == null) {
            return;
        }
        int k = localBitmap.getWidth();
        int m = localBitmap.getHeight();
        int i = 0;
        int j = 0;
        Matrix localMatrix = new Matrix();
        float f1 = this.mResizedWidth / k;
        float f2 = this.mResizedHeight / m;
        if (f1 > f2) {
        }
        for (; ; ) {
            try {
                localMatrix.setScale(f1, f1);
                localBitmap = Bitmap.createBitmap(localBitmap, 0, 0, k, m, localMatrix, true);
                j = -(localBitmap.getHeight() - this.mResizedHeight) / 2;
                k = localBitmap.getWidth();
                this.mOrgBitmapWidth = k;
                this.mBitmapWidth = k;
                k = localBitmap.getHeight();
                this.mOrgBitmapHeight = k;
                this.mBitmapHeight = k;
                setImageBitmap(localBitmap);
                this.mImageViewmatrix.reset();
                this.mImageViewmatrix.postTranslate(i, j);
                setImageMatrix(this.mImageViewmatrix);
                this.mScaleFactor = 1.0F;
                return;
            } catch (OutOfMemoryError localOutOfMemoryError) {
                Log.w("CollageImageView", "[updateImageBitmap] createBitmap OutOfMemoryError ");
                return;
            } catch (Exception localException) {
                Log.w("CollageImageView", "[drawImageBitmap] createBitmap exception ");
            }
            localMatrix.setScale(f2, f2);
            localBitmap = Bitmap.createBitmap(localBitmap, 0, 0, k, m, localMatrix, true);
            i = -(localBitmap.getWidth() - this.mResizedWidth) / 2;
        }
    }

    public static abstract interface CollageChangeImageListener {
        public abstract void onCollageImageReplaced(int paramInt);

        public abstract void onDrag(View paramView, DragEvent paramDragEvent);

        public abstract boolean onTouchEvent(View paramView, MotionEvent paramMotionEvent);
    }
}
