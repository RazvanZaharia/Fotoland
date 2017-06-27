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
 * Created on 06-Oct-16.
 */
public class OldCollageImageView extends ImageView
        implements GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener, View.OnDragListener {
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
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            OldCollageImageView.this.getViewTreeObserver().removeOnGlobalLayoutListener(OldCollageImageView.this.mOnGlobalLayoutListener);
            int i = OldCollageImageView.this.mResizedWidth;
            int j = OldCollageImageView.this.mResizedHeight;
            OldCollageImageView.access$102(OldCollageImageView.this, OldCollageImageView.this.getMeasuredWidth());
            OldCollageImageView.access$202(OldCollageImageView.this, OldCollageImageView.this.getMeasuredHeight());
            if ((i != OldCollageImageView.this.mResizedWidth) || (j != OldCollageImageView.this.mResizedHeight)) {
                OldCollageImageView.this.setImageFrameFocusd(false);
                OldCollageImageView.this.updateImageBitmap();
            }
        }
    };
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

    public OldCollageImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setScaleType(ImageView.ScaleType.MATRIX);
        this.mGestureDector = new GestureDetector(paramContext, this);
        this.mSGestureDector = new ScaleGestureDetector(paramContext, this);
        setOnDragListener(this);
        getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
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

    public boolean onDown(MotionEvent paramMotionEvent) {
        Log.d("CollageImageView", "[onDown] ");
        return true;
    }

    public boolean onDrag(View paramView, DragEvent paramDragEvent) {
        this.mListner.onDrag(paramView, paramDragEvent);
        return true;
    }

    public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
        return true;
    }

    public void onLongPress(MotionEvent paramMotionEvent) {
        if (this.mPause) {
            return;
        }
        startDrag(ClipData.newPlainText("", ""), new View.DragShadowBuilder(this), this, 0);
        setVisibility(INVISIBLE);
    }

    public boolean onScale(ScaleGestureDetector paramScaleGestureDetector) {
        float f = this.mScaleFactor;
        this.mScaleFactor *= paramScaleGestureDetector.getScaleFactor();
        this.mScaleFactor = Math.max(1.0F, Math.min(this.mScaleFactor, 1.8F));
        if (Float.compare(this.mScaleFactor, 1.0F) >= 0) {
            f = this.mScaleFactor / f;
            this.mImageViewmatrix.postScale(f, f, this.mResizedWidth / 2, this.mResizedHeight / 2);
        }

        setImageMatrix(this.mImageViewmatrix);
        this.mImageViewmatrix.postScale(this.mScaleFactor, this.mScaleFactor, this.mResizedWidth / 2, this.mResizedHeight / 2);
        return true;
    }

    public boolean onScaleBegin(ScaleGestureDetector paramScaleGestureDetector) {
        this.mScaling = true;
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector paramScaleGestureDetector) {
        this.mBitmapWidth = ((int) (this.mOrgBitmapWidth * this.mScaleFactor));
        this.mBitmapHeight = ((int) (this.mOrgBitmapHeight * this.mScaleFactor));
        getImageMatrix().getValues(this.mMatrixValues);
        int i;
        int j;
        if (this.mMatrixValues[2] > 0.0F) {
            i = (int) -this.mMatrixValues[2];
            if (this.mMatrixValues[5] <= 0.0F) {
                break label151;
            }
            j = (int) -this.mMatrixValues[5];
        }
        for (; ; ) {
            this.mImageViewmatrix.postTranslate(i, j);
            setImageMatrix(this.mImageViewmatrix);
            return;
            if (this.mMatrixValues[2] + this.mBitmapWidth < this.mResizedWidth) {
                i = (int) (this.mResizedWidth - (this.mBitmapWidth + this.mMatrixValues[2]));
                break;
            }
            i = 0;
            break;
            label151:
            if (this.mMatrixValues[5] + this.mBitmapHeight < this.mResizedHeight) {
                j = (int) (this.mResizedHeight - (this.mBitmapHeight + this.mMatrixValues[5]));
            } else {
                j = 0;
            }
        }
    }

    public boolean onScroll(MotionEvent paramMotionEvent1,
                            MotionEvent paramMotionEvent2,
                            float paramFloat1,
                            float paramFloat2) {
        if (this.mScaling) {
            return false;
        }
        int j = (int) paramMotionEvent1.getRawX();
        int i = (int) paramMotionEvent1.getRawY();
        int m = (int) paramMotionEvent2.getRawX();
        int n = (int) paramMotionEvent2.getRawY();
        int k;
        if (!this.mMoving) {
            k = m - j;
            i = n - i;
            this.mMoving = true;
            getImageMatrix().getValues(this.mMatrixValues);
            if (Float.compare(this.mMatrixValues[2] + k, 0.0F) <= 0) {
                break label185;
            }
            j = (int) -this.mMatrixValues[2];
            label101:
            if (Float.compare(this.mMatrixValues[5] + i, 0.0F) <= 0) {
                break label242;
            }
            k = (int) -this.mMatrixValues[5];
        }
        for (; ; ) {
            this.mImageViewmatrix.postTranslate(j, k);
            setImageMatrix(this.mImageViewmatrix);
            this.mPrevX = m;
            this.mPrevY = n;
            return true;
            k = m - this.mPrevX;
            i = n - this.mPrevY;
            break;
            label185:
            j = k;
            if (Float.compare(-this.mMatrixValues[2] + -k + this.mResizedWidth, this.mBitmapWidth) <= 0) {
                break label101;
            }
            j = (int) (-this.mMatrixValues[2] + this.mResizedWidth) - this.mBitmapWidth;
            break label101;
            label242:
            k = i;
            if (Float.compare(-this.mMatrixValues[5] + -i + this.mResizedHeight, this.mBitmapHeight) > 0) {
                k = (int) (-this.mMatrixValues[5] + this.mResizedHeight - this.mBitmapHeight);
            }
        }
    }

    public void onShowPress(MotionEvent paramMotionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
        this.mListner.onCollageImageReplaced(getId());
        return true;
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        if ((this.mListner == null) || (this.mListner.onTouchEvent(this, paramMotionEvent))) {

        }

        if (paramMotionEvent.getAction() == 1) {
            this.mMoving = false;
            this.mScaling = false;
        }
        this.mSGestureDector.onTouchEvent(paramMotionEvent);
        this.mGestureDector.onTouchEvent(paramMotionEvent);
        return true;
    }

    public void pause() {
        this.mPause = true;
    }

    public void resume() {
        this.mPause = false;
    }

    public void setImageChangeListner(CollageChangeImageListener paramCollageChangeImageListener) {
        this.mListner = paramCollageChangeImageListener;
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

    public void updateImageBitmap() {
        Bitmap localBitmap = getRotateImage(this.mBitmap);
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