package eu.mobiletouch.fotoland.collage;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.lge.gallery.common.Utils;
import com.lge.gallery.data.MediaItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import eu.mobiletouch.fotoland.collage.OldCollageImageView;

public class OldCollageView
        extends FrameLayout
        implements OldCollageImageView.CollageChangeImageListener
{
    public static final String CHANGE_COLLAGE = "change_collage";
    private static final int[] COLLAGE_DEFAULT_LAYOUT_IDS = { 2130968583, 2130968589, 2130968601, 2130968610, 2130968620, 2130968632, 2130968644, 2130968653 };
    private static final boolean DEBUG = false;
    private static final int[] IMAGE_FRAME_IDS = { 2131623970, 2131623971, 2131623972, 2131623973, 2131623974, 2131623975, 2131623976, 2131623977, 2131623978 };
    private static final String TAG = "CollageView";
    private static final HashMap<Integer, Integer> mLayoutIds;
    private ArrayList<Bitmap> mBitmapList = new ArrayList();
    private LinearLayout mCollageLayout = null;
    private ArrayList<MediaItem> mCollageList = new ArrayList();
    private int mCurrentViewId;
    private OldCollageImageView mFocusedView;
    private ArrayList<OldCollageImageView> mImageViewList;
    private final LayoutInflater mInflater;
    private int mReplaceImageViewId;
    private View mTouchedView;

    static
    {
        int[][] arrayOfInt = new int[79][];
        arrayOfInt[0] = { 2131623969, 2130968583 };
        arrayOfInt[1] = { 2131623983, 2130968584 };
        arrayOfInt[2] = { 2131623984, 2130968585 };
        arrayOfInt[3] = { 2131623985, 2130968586 };
        arrayOfInt[4] = { 2131623986, 2130968587 };
        arrayOfInt[5] = { 2131623987, 2130968588 };
        arrayOfInt[6] = { 2131623989, 2130968589 };
        arrayOfInt[7] = { 2131623990, 2130968593 };
        arrayOfInt[8] = { 2131623991, 2130968594 };
        arrayOfInt[9] = { 2131623992, 2130968595 };
        arrayOfInt[10] = { 2131623993, 2130968596 };
        arrayOfInt[11] = { 2131623994, 2130968597 };
        arrayOfInt[12] = { 2131623995, 2130968598 };
        arrayOfInt[13] = { 2131623996, 2130968599 };
        arrayOfInt[14] = { 2131623997, 2130968600 };
        arrayOfInt[15] = { 2131623998, 2130968590 };
        arrayOfInt[16] = { 2131623999, 2130968591 };
        arrayOfInt[17] = { 2131624000, 2130968592 };
        arrayOfInt[18] = { 2131624002, 2130968601 };
        arrayOfInt[19] = { 2131624003, 2130968602 };
        arrayOfInt[20] = { 2131624004, 2130968603 };
        arrayOfInt[21] = { 2131624005, 2130968604 };
        arrayOfInt[22] = { 2131624006, 2130968605 };
        arrayOfInt[23] = { 2131624007, 2130968606 };
        arrayOfInt[24] = { 2131624008, 2130968607 };
        arrayOfInt[25] = { 2131624009, 2130968608 };
        arrayOfInt[26] = { 2131624010, 2130968609 };
        arrayOfInt[27] = { 2131624012, 2130968610 };
        arrayOfInt[28] = { 2131624013, 2130968612 };
        arrayOfInt[29] = { 2131624014, 2130968613 };
        arrayOfInt[30] = { 2131624015, 2130968614 };
        arrayOfInt[31] = { 2131624016, 2130968615 };
        arrayOfInt[32] = { 2131624017, 2130968616 };
        arrayOfInt[33] = { 2131624018, 2130968617 };
        arrayOfInt[34] = { 2131624019, 2130968618 };
        arrayOfInt[35] = { 2131624020, 2130968619 };
        arrayOfInt[36] = { 2131624021, 2130968611 };
        arrayOfInt[37] = { 2131624023, 2130968620 };
        arrayOfInt[38] = { 2131624024, 2130968624 };
        arrayOfInt[39] = { 2131624025, 2130968625 };
        arrayOfInt[40] = { 2131624026, 2130968626 };
        arrayOfInt[41] = { 2131624027, 2130968627 };
        arrayOfInt[42] = { 2131624028, 2130968628 };
        arrayOfInt[43] = { 2131624029, 2130968629 };
        arrayOfInt[44] = { 2131624030, 2130968630 };
        arrayOfInt[45] = { 2131624031, 2130968631 };
        arrayOfInt[46] = { 2131624032, 2130968621 };
        arrayOfInt[47] = { 2131624033, 2130968622 };
        arrayOfInt[48] = { 2131624034, 2130968623 };
        arrayOfInt[49] = { 2131624036, 2130968632 };
        arrayOfInt[50] = { 2131624037, 2130968636 };
        arrayOfInt[51] = { 2131624038, 2130968637 };
        arrayOfInt[52] = { 2131624039, 2130968638 };
        arrayOfInt[53] = { 2131624040, 2130968639 };
        arrayOfInt[54] = { 2131624041, 2130968640 };
        arrayOfInt[55] = { 2131624042, 2130968641 };
        arrayOfInt[56] = { 2131624043, 2130968642 };
        arrayOfInt[57] = { 2131624044, 2130968643 };
        arrayOfInt[58] = { 2131624045, 2130968633 };
        arrayOfInt[59] = { 2131624046, 2130968634 };
        arrayOfInt[60] = { 2131624047, 2130968635 };
        arrayOfInt[61] = { 2131624049, 2130968644 };
        arrayOfInt[62] = { 2131624050, 2130968645 };
        arrayOfInt[63] = { 2131624051, 2130968646 };
        arrayOfInt[64] = { 2131624052, 2130968647 };
        arrayOfInt[65] = { 2131624053, 2130968648 };
        arrayOfInt[66] = { 2131624054, 2130968649 };
        arrayOfInt[67] = { 2131624055, 2130968650 };
        arrayOfInt[68] = { 2131624056, 2130968651 };
        arrayOfInt[69] = { 2131624057, 2130968652 };
        arrayOfInt[70] = { 2131624059, 2130968653 };
        arrayOfInt[71] = { 2131624060, 2130968654 };
        arrayOfInt[72] = { 2131624061, 2130968655 };
        arrayOfInt[73] = { 2131624062, 2130968656 };
        arrayOfInt[74] = { 2131624063, 2130968657 };
        arrayOfInt[75] = { 2131624064, 2130968658 };
        arrayOfInt[76] = { 2131624065, 2130968659 };
        arrayOfInt[77] = { 2131624066, 2130968660 };
        arrayOfInt[78] = { 2131624067, 2130968661 };
        HashMap localHashMap = new HashMap();
        int i = 0;
        while (i < arrayOfInt.length)
        {
            localHashMap.put(Integer.valueOf(arrayOfInt[i][0]), Integer.valueOf(arrayOfInt[i][1]));
            i += 1;
        }
        mLayoutIds = localHashMap;
    }

    public OldCollageView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        this.mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
        this.mImageViewList = new ArrayList();
    }

    private int findImageViewIndex(int paramInt)
    {
        int k = 0;
        int i = 0;
        for (;;)
        {
            int j = k;
            if (i < IMAGE_FRAME_IDS.length)
            {
                if (IMAGE_FRAME_IDS[i] == paramInt) {
                    j = i;
                }
            }
            else {
                return j;
            }
            i += 1;
        }
    }

    private void onCollageImageChanged(int paramInt1, int paramInt2)
    {
        paramInt1 = findImageViewIndex(paramInt1);
        paramInt2 = findImageViewIndex(paramInt2);
        MediaItem localMediaItem1 = (MediaItem)this.mCollageList.get(paramInt2);
        Bitmap localBitmap1 = (Bitmap)this.mBitmapList.get(paramInt2);
        MediaItem localMediaItem2 = (MediaItem)this.mCollageList.get(paramInt1);
        Bitmap localBitmap2 = (Bitmap)this.mBitmapList.get(paramInt1);
        this.mCollageList.set(paramInt1, localMediaItem1);
        this.mBitmapList.set(paramInt1, localBitmap1);
        this.mCollageList.set(paramInt2, localMediaItem2);
        this.mBitmapList.set(paramInt2, localBitmap2);
        ((OldCollageImageView)this.mImageViewList.get(paramInt1)).setImageItem((MediaItem)this.mCollageList.get(paramInt1), (Bitmap)this.mBitmapList.get(paramInt1));
        ((OldCollageImageView)this.mImageViewList.get(paramInt1)).updateImageBitmap();
        ((OldCollageImageView)this.mImageViewList.get(paramInt2)).setImageItem((MediaItem)this.mCollageList.get(paramInt2), (Bitmap)this.mBitmapList.get(paramInt2));
        ((OldCollageImageView)this.mImageViewList.get(paramInt2)).updateImageBitmap();
    }

    private void updateLayout(View paramView)
    {
        if (paramView != null) {
            removeView(paramView);
        }
        this.mImageViewList.clear();
        int i = 0;
        while (i < this.mBitmapList.size())
        {
            paramView = (OldCollageImageView)this.mCollageLayout.findViewById(IMAGE_FRAME_IDS[i]);
            paramView.setImageChangeListner(this);
            paramView.setImageItem((MediaItem)this.mCollageList.get(i), (Bitmap)this.mBitmapList.get(i));
            this.mImageViewList.add(paramView);
            i += 1;
        }
    }

    public void clearData()
    {
        this.mCollageList.clear();
        Iterator localIterator = this.mBitmapList.iterator();
        while (localIterator.hasNext())
        {
            Bitmap localBitmap = (Bitmap)localIterator.next();
            if (localBitmap != null) {
                localBitmap.recycle();
            }
        }
        this.mBitmapList.clear();
        localIterator = this.mImageViewList.iterator();
        while (localIterator.hasNext()) {
            ((OldCollageImageView)localIterator.next()).clearData();
        }
    }

    public int getCurrentLayoutId()
    {
        return this.mCurrentViewId;
    }

    public MediaItem getDefaultImageItem()
    {
        return (MediaItem)this.mCollageList.get(0);
    }

    public int getImageCount()
    {
        return this.mBitmapList.size();
    }

    public int getItemCount()
    {
        return this.mCollageList.size();
    }

    public ArrayList<MediaItem> getItemList()
    {
        return this.mCollageList;
    }

    public int getReplaceViewId()
    {
        return this.mReplaceImageViewId;
    }

    public Bitmap getSaveCollageImage()
    {
        this.mCollageLayout.destroyDrawingCache();
        this.mCollageLayout.setDrawingCacheEnabled(true);
        return this.mCollageLayout.getDrawingCache();
    }

    public void onCollageImageReplaced(int paramInt)
    {
        this.mReplaceImageViewId = paramInt;
        Intent localIntent = new Intent("android.intent.action.GET_CONTENT");
        localIntent.setType("image/*");
        localIntent.putExtra("android.intent.extra.LOCAL_ONLY", true);
        localIntent.putExtra("change_collage", true);
        localIntent.setComponent(new ComponentName("com.android.gallery3d", "com.android.gallery3d.app.Gallery"));
        ((Activity)getContext()).startActivityForResult(localIntent, 1);
    }

    public void onDrag(View paramView, DragEvent paramDragEvent)
    {
        switch (paramDragEvent.getAction())
        {
            default:
            case 5:
            case 6:
                do
                {
                    do
                    {
                        return;
                    } while (!(paramView instanceof OldCollageImageView));
                    this.mFocusedView = ((OldCollageImageView)paramView);
                    this.mFocusedView.setImageFrameFocusd(true);
                    return;
                } while (this.mFocusedView == null);
                this.mFocusedView.setImageFrameFocusd(false);
                this.mFocusedView = null;
                return;
            case 3:
                onCollageImageChanged(((View)paramDragEvent.getLocalState()).getId(), paramView.getId());
                return;
        }
        if (this.mFocusedView != null)
        {
            this.mFocusedView.setImageFrameFocusd(false);
            this.mFocusedView = null;
        }
        int i = findImageViewIndex(((View)paramDragEvent.getLocalState()).getId());
        ((OldCollageImageView)this.mImageViewList.get(i)).setVisibility(0);
    }

    public boolean onTouchEvent(View paramView, MotionEvent paramMotionEvent)
    {
        if ((paramMotionEvent.getAction() == 1) || (paramMotionEvent.getAction() == 3)) {
            this.mTouchedView = null;
        }
        do
        {
            return true;
            if (this.mTouchedView == null)
            {
                this.mTouchedView = paramView;
                return true;
            }
        } while (paramView.equals(this.mTouchedView));
        return false;
    }

    public void pause()
    {
        int i = 0;
        while (i < this.mImageViewList.size())
        {
            ((OldCollageImageView)this.mImageViewList.get(i)).pause();
            i += 1;
        }
    }

    public void replaceImageBitmap(Bitmap paramBitmap)
    {
        if (paramBitmap == null) {
            return;
        }
        int i = findImageViewIndex(this.mReplaceImageViewId);
        this.mBitmapList.set(i, paramBitmap);
        ((OldCollageImageView)this.mImageViewList.get(i)).setImageItem((MediaItem)this.mCollageList.get(i), (Bitmap)this.mBitmapList.get(i));
        ((OldCollageImageView)this.mImageViewList.get(i)).updateImageBitmap();
    }

    public void resume()
    {
        int i = 0;
        while (i < this.mImageViewList.size())
        {
            ((OldCollageImageView)this.mImageViewList.get(i)).resume();
            i += 1;
        }
    }

    public void setImageBitmap(MediaItem paramMediaItem, Bitmap paramBitmap)
    {
        if ((paramMediaItem == null) || (paramBitmap == null)) {
            return;
        }
        this.mCollageList.add(paramMediaItem);
        this.mBitmapList.add(paramBitmap);
    }

    public void setLayoutById(int paramInt)
    {
        LinearLayout localLinearLayout = this.mCollageLayout;
        this.mCurrentViewId = paramInt;
        if (paramInt == 0) {
            paramInt = Utils.clamp(this.mBitmapList.size() - 2, 0, COLLAGE_DEFAULT_LAYOUT_IDS.length - 1);
        }
        for (this.mCollageLayout = ((LinearLayout)this.mInflater.inflate(COLLAGE_DEFAULT_LAYOUT_IDS[paramInt], null, false));; this.mCollageLayout = ((LinearLayout)this.mInflater.inflate(((Integer)mLayoutIds.get(Integer.valueOf(paramInt))).intValue(), null, false)))
        {
            addView(this.mCollageLayout);
            updateLayout(localLinearLayout);
            return;
        }
    }

    public void setMediaItemList(MediaItem paramMediaItem)
    {
        int i = findImageViewIndex(this.mReplaceImageViewId);
        this.mCollageList.set(i, paramMediaItem);
    }

    public void setReplaceViewId(int paramInt)
    {
        this.mReplaceImageViewId = paramInt;
    }
}
