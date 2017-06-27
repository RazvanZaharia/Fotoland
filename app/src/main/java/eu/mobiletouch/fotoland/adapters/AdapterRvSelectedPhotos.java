package eu.mobiletouch.fotoland.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvSelectedPhotos.BaseViewHolderSelectedPhoto;
import eu.mobiletouch.fotoland.adapters.AdapterRvSelectedPhotos.ViewHolderSelectedPhoto.OnSelectedPhotoInteractionListener;
import eu.mobiletouch.fotoland.enums.ItemType;
import eu.mobiletouch.fotoland.enums.Orientation;
import eu.mobiletouch.fotoland.enums.ProductType;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.interfaces.SelectedPhotoItem;
import eu.mobiletouch.fotoland.utils.Utils;

/**
 * Created on 28-Aug-16.
 */
public class AdapterRvSelectedPhotos extends RecyclerView.Adapter<BaseViewHolderSelectedPhoto> {

    private Context mCtx;
    private ArrayList<SelectedPhotoItem> mSelectedPhotos;
    private OnSelectedPhotoInteractionListener mOnSelectedPhotoInteractionListener;
    private Item mSelectedItem;
    private float mPpiLimit;
    private ProductType mProductType;

    public AdapterRvSelectedPhotos(@NonNull Context ctx, ProductType type, OnSelectedPhotoInteractionListener onSelectedPhotoInteractionListener) {
        this(ctx, onSelectedPhotoInteractionListener);
        this.mProductType = type;
    }

    public AdapterRvSelectedPhotos(@NonNull Context ctx, OnSelectedPhotoInteractionListener onSelectedPhotoInteractionListener) {
        this.mCtx = ctx;
        this.mOnSelectedPhotoInteractionListener = onSelectedPhotoInteractionListener;
        this.mProductType = ProductType.PRINTS;
    }

    @Override
    public BaseViewHolderSelectedPhoto onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolderSelectedPhoto viewHolder;
        switch (mProductType) {
            case PHOTOBOOK:
                viewHolder = ViewHolderPhotobookSelectedPhoto.getInstance(mCtx, parent, mOnSelectedPhotoInteractionListener, mPpiLimit);
                break;
            case PRINTS:
            default:
                viewHolder = ViewHolderSelectedPhoto.getInstance(mCtx, parent, mOnSelectedPhotoInteractionListener, mPpiLimit);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolderSelectedPhoto holder, int position) {
        holder.onBind(mSelectedPhotos.get(position), position, mSelectedItem);
    }

    @Override
    public int getItemCount() {
       return mSelectedPhotos != null ? mSelectedPhotos.size() : 0;
    }

    public void setSelectedPhotos(@NonNull ArrayList<SelectedPhotoItem> selectedPhotos, @NonNull Item selectedItem, float ppiLimit) {
        this.mSelectedItem = selectedItem;
        this.mPpiLimit = ppiLimit;
        if (mSelectedPhotos == null) {
            mSelectedPhotos = new ArrayList<>(selectedPhotos);
        } else {
            mSelectedPhotos.clear();
            mSelectedPhotos.addAll(selectedPhotos);
        }
    }

    public void updateDataSet(@NonNull ArrayList<SelectedPhotoItem> selectedPhotos) {
        mSelectedPhotos = new ArrayList<>(selectedPhotos);
    }

    static class BaseViewHolderSelectedPhoto extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_photo)
        ImageView mIvPhoto;
        @Bind(R.id.iv_alert)
        ImageView mIvAlert;

        OnSelectedPhotoInteractionListener mOnSelectedPhotoInteractionListener;
        float mPpiLimit;

        BaseViewHolderSelectedPhoto(View itemView, OnSelectedPhotoInteractionListener onSelectedPhotoInteractionListener, float ppiLimit) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnSelectedPhotoInteractionListener = onSelectedPhotoInteractionListener;
            this.mPpiLimit = ppiLimit;
        }

        public float getPhotoWidth() {
            return itemView.getContext().getResources().getDimension(R.dimen.item_list_selectedPhotos);
        }

        public void onBind(@Nullable final SelectedPhotoItem displayItem, final int position, @NonNull Item selectedItem) {
            if (displayItem != null && displayItem.getDisplayItemType() == SelectedPhotoItem.DisplayItemType.PHOTO) {
                final Photo photo = (Photo) displayItem;
                if (photo.getOrientation() == Orientation.LANDSCAPE) {
                    setImageViewDimensions(mIvPhoto, selectedItem.getItemCustomRatio()[0], selectedItem.getItemCustomRatio()[1]);
                } else {
                    setImageViewDimensions(mIvPhoto, selectedItem.getItemCustomRatio()[1], selectedItem.getItemCustomRatio()[0]);
                }

                if (TextUtils.isEmpty(photo.getCroppedPhotoPath())) {
                    Glide.with(itemView.getContext())
                            .load(photo.getPhotoPath())
                            .signature(new StringSignature(String.valueOf(new File(photo.getPhotoPath()).lastModified())))
                            .into(mIvPhoto);

                    mIvPhoto.setRotation(photo.getRotation());
                } else {
                    Glide.with(itemView.getContext())
                            .load(photo.getCroppedPhotoPath())
                            .signature(new StringSignature(String.valueOf(new File(photo.getCroppedPhotoPath()).lastModified())))
                            .into(mIvPhoto);
                }

                calculatePPI(photo, position, selectedItem);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnSelectedPhotoInteractionListener != null) {
                            mOnSelectedPhotoInteractionListener.onPhotoCLick(photo);
                        }
                    }
                });
            }
        }

        protected void calculatePPI(@NonNull final Photo photo, final int position, @NonNull Item selectedItem) {
            final float ppi = Utils.getMinimumPPIofImage(photo.getPhotoWidth(), photo.getPhotoHeight(),
                    selectedItem.getSelectedSize().getWidth(), selectedItem.getSelectedSize().getHeight());

            Log.d("PPI", ppi + "");

            if (ppi < mPpiLimit) {
                mIvAlert.setVisibility(View.VISIBLE);
                mIvAlert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnSelectedPhotoInteractionListener.onLowPpi(photo, position, ppi);
                    }
                });
            } else {
                mIvAlert.setVisibility(View.GONE);
                mIvAlert.setOnClickListener(null);
            }
        }

        private void setImageViewDimensions(@NonNull ImageView imageView, int ratioX, int ratioY) {
            float photoWidth = getPhotoWidth();

            if (ratioX == 0 || ratioY == 0 || photoWidth == 0.0f) {
                return;
            }

            if (ratioX > ratioY) {
                imageView.getLayoutParams().height = (int) ((photoWidth / ratioX) * ratioY);
            } else {
                imageView.getLayoutParams().width = (int) ((photoWidth / ratioY) * ratioX);
            }
        }

    }

    public static class ViewHolderPhotobookSelectedPhoto extends BaseViewHolderSelectedPhoto {

        @Bind(R.id.tv_remove)
        View mRemove;
        @Bind(R.id.layout_addPhoto)
        View mLayoutAddPhoto;
        @Bind(R.id.tv_info)
        TextView mTvInfo;
        @Bind(R.id.tv_caption)
        TextView mTvCaption;
        @Bind(R.id.img_add_page)
        View mAddPage;

        private SelectedPhotoItem mSelectedPhotoItem;

        static ViewHolderPhotobookSelectedPhoto getInstance(@NonNull Context ctx, ViewGroup parent, OnSelectedPhotoInteractionListener onSelectedPhotoInteractionListener, float ppiLimit) {
            return new ViewHolderPhotobookSelectedPhoto(LayoutInflater.from(ctx).inflate(R.layout.item_selected_photobook_photo, parent, false), onSelectedPhotoInteractionListener, ppiLimit);
        }

        ViewHolderPhotobookSelectedPhoto(View itemView, OnSelectedPhotoInteractionListener onSelectedPhotoInteractionListener, float ppiLimit) {
            super(itemView, onSelectedPhotoInteractionListener, ppiLimit);
        }

        @Override
        public float getPhotoWidth() {
            return 0;
        }

        @Override
        public void onBind(SelectedPhotoItem selectedPhotoItem, final int position, @NonNull Item selectedItem) {
            super.onBind(selectedPhotoItem, position, selectedItem);

            mTvInfo.setText("");
            this.mSelectedPhotoItem = selectedPhotoItem;

            switch (mSelectedPhotoItem.getDisplayItemType()) {
                case PHOTO:
                    final Photo photo = (Photo) mSelectedPhotoItem;
                    mIvPhoto.setVisibility(View.VISIBLE);
                    mLayoutAddPhoto.setVisibility(View.INVISIBLE);
                    if (!TextUtils.isEmpty(photo.getCaptionText())) {
                        mTvCaption.setText(photo.getCaptionText());
                        mTvCaption.setVisibility(View.VISIBLE);
                    } else {
                        mTvCaption.setVisibility(View.GONE);
                    }

                    if (position == 0) {
                        mTvInfo.setText(R.string.label_coverPhoto);
                    } else {
                        mTvInfo.setText(String.valueOf(position));
                    }
                    break;
                case BLANK_PAGE:
                    mIvAlert.setVisibility(View.INVISIBLE);
                    mTvCaption.setVisibility(View.INVISIBLE);
                    mIvPhoto.setVisibility(View.INVISIBLE);
                    mLayoutAddPhoto.setVisibility(View.INVISIBLE);
                    break;
                case ADD_PAGE:
                    mIvAlert.setVisibility(View.INVISIBLE);
                    mTvCaption.setVisibility(View.INVISIBLE);
                    mIvPhoto.setVisibility(View.INVISIBLE);
                    mLayoutAddPhoto.setVisibility(View.VISIBLE);
                    break;
            }

            mAddPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnSelectedPhotoInteractionListener.onAddNewPageAt(position);
                }
            });

            mRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnSelectedPhotoInteractionListener.onRemoveNewPageAt(position);
                }
            });

            mLayoutAddPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnSelectedPhotoInteractionListener.onAddPhoto(position);
                }
            });
        }

        @Override
        protected void calculatePPI(@NonNull final Photo photo, final int position, @NonNull Item selectedItem) {
            final float ppi = Utils.getMinimumPPIofImage(photo.getPhotoWidth(), photo.getPhotoHeight(),
                    selectedItem.getSelectedSize().getWidth() / 2, selectedItem.getSelectedSize().getHeight() / 2);

            Log.d("PPI", ppi + "");

            if (ppi < mPpiLimit) {
                mIvAlert.setVisibility(View.VISIBLE);
                mIvAlert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnSelectedPhotoInteractionListener.onLowPpi(photo, position, ppi);
                    }
                });
            } else {
                mIvAlert.setVisibility(View.GONE);
                mIvAlert.setOnClickListener(null);
            }
        }

        public SelectedPhotoItem getItem() {
            return mSelectedPhotoItem;
        }

    }

    public static class ViewHolderSelectedPhoto extends BaseViewHolderSelectedPhoto {
        @Bind(R.id.layout_image)
        LinearLayout mLlImage;
        @Bind(R.id.tv_message)
        TextView mTvMessage;
        @Bind(R.id.tv_index)
        TextView mTvIndex;
        @Bind(R.id.iv_rotate)
        View mIvRotate;
        @Bind(R.id.iv_decrease)
        View mIvDecrease;
        @Bind(R.id.tv_quantity)
        TextView mTvQuantity;
        @Bind(R.id.iv_increase)
        View mIvIncrease;
        @Bind(R.id.iv_crop)
        View mIvCrop;

        public static ViewHolderSelectedPhoto getInstance(@NonNull Context ctx, ViewGroup parent, OnSelectedPhotoInteractionListener onSelectedPhotoInteractionListener, float ppiLimit) {
            return new ViewHolderSelectedPhoto(LayoutInflater.from(ctx).inflate(R.layout.item_selected_photo, parent, false), onSelectedPhotoInteractionListener, ppiLimit);
        }

        private ViewHolderSelectedPhoto(View itemView, OnSelectedPhotoInteractionListener onSelectedPhotoInteractionListener, float ppiLimit) {
            super(itemView, onSelectedPhotoInteractionListener, ppiLimit);
        }

        @Override
        public void onBind(final SelectedPhotoItem selectedPhotoItem, final int position, @NonNull Item selectedItem) {
            super.onBind(selectedPhotoItem, position, selectedItem);

            if(selectedPhotoItem.getDisplayItemType() != SelectedPhotoItem.DisplayItemType.PHOTO) {
                return;
            }

            final Photo photo = (Photo) selectedPhotoItem;

            mTvIndex.setText(String.valueOf(position + 1));

            if (selectedItem.getItemType() == ItemType.REGULAR) {
                mIvRotate.setVisibility(View.VISIBLE);
            } else {
                mIvRotate.setVisibility(View.INVISIBLE);
            }

            if (selectedItem.getItemType() == ItemType.VINTAGE) {
                mLlImage.setBackgroundResource(R.color.white);
                mTvMessage.setVisibility(View.VISIBLE);
                mTvMessage.setText(photo.getCaptionText());
            } else {
                mLlImage.setBackgroundResource(android.R.color.transparent);
                mTvMessage.setVisibility(View.GONE);
            }

            mTvQuantity.setText(String.valueOf(photo.getQuantity()));

            mIvRotate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnSelectedPhotoInteractionListener.onRotateClick(photo, position);
                }
            });

            mIvDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnSelectedPhotoInteractionListener.onDecreaseCountClick(photo, position);
                }
            });

            mIvIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnSelectedPhotoInteractionListener.onIncreaseCountClick(photo, position);
                }
            });

            mIvCrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnSelectedPhotoInteractionListener.onCropClick(photo, position);
                }
            });

            mTvMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnSelectedPhotoInteractionListener.onAddText(photo, position);
                }
            });

        }

        public interface OnSelectedPhotoInteractionListener {
            void onRotateClick(Photo photo, int position);

            void onIncreaseCountClick(Photo photo, int position);

            void onDecreaseCountClick(Photo photo, int position);

            void onCropClick(Photo photo, int position);

            void onLowPpi(Photo photo, int position, float ppi);

            void onAddText(Photo photo, int position);

            void onAddPhoto(int atPosition);

            void onPhotoCLick(@NonNull Photo photo);

            void onAddNewPageAt(int atPosition);

            void onRemoveNewPageAt(int atPosition);
        }

    }

}
