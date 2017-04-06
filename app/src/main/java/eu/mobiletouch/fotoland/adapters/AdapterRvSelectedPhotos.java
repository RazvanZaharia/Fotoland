package eu.mobiletouch.fotoland.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import eu.mobiletouch.fotoland.adapters.AdapterRvSelectedPhotos.ViewHolderSelectedPhoto;
import eu.mobiletouch.fotoland.adapters.AdapterRvSelectedPhotos.ViewHolderSelectedPhoto.OnSelectedPhotoInteractionListener;
import eu.mobiletouch.fotoland.holders.Item;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;

/**
 * Created on 28-Aug-16.
 */
public class AdapterRvSelectedPhotos extends RecyclerView.Adapter<ViewHolderSelectedPhoto> {

    private Context mCtx;
    private ArrayList<Photo> mSelectedPhotos;
    private OnSelectedPhotoInteractionListener mOnSelectedPhotoInteractionListener;
    private Item.ItemType mItemType;

    public AdapterRvSelectedPhotos(@NonNull Context ctx, @NonNull ArrayList<Photo> selectedPhotos, @NonNull Item.ItemType itemType, OnSelectedPhotoInteractionListener onSelectedPhotoInteractionListener) {
        this.mCtx = ctx;
        this.mSelectedPhotos = selectedPhotos;
        this.mItemType = itemType;
        this.mOnSelectedPhotoInteractionListener = onSelectedPhotoInteractionListener;
    }

    @Override
    public ViewHolderSelectedPhoto onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderSelectedPhoto(LayoutInflater.from(mCtx).inflate(R.layout.item_selected_photo, parent, false), mOnSelectedPhotoInteractionListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderSelectedPhoto holder, int position) {
        holder.onBind(mSelectedPhotos.get(position), position, mItemType);
    }

    @Override
    public int getItemCount() {
        return mSelectedPhotos == null ? 0 : mSelectedPhotos.size();
    }

    public void setSelectedPhotos(@NonNull ArrayList<Photo> selectedPhotos, @NonNull Item.ItemType itemType) {
        this.mItemType = itemType;
        if (mSelectedPhotos == null) {
            mSelectedPhotos = new ArrayList<>(selectedPhotos);
        } else {
            mSelectedPhotos.clear();
            mSelectedPhotos.addAll(selectedPhotos);
        }
    }

    public static class ViewHolderSelectedPhoto extends RecyclerView.ViewHolder {
        @Bind(R.id.layout_image)
        LinearLayout mLlImage;
        @Bind(R.id.tv_message)
        TextView mTvMessage;
        @Bind(R.id.tv_index)
        TextView mTvIndex;
        @Bind(R.id.iv_photo)
        ImageView mIvPhoto;
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

        private OnSelectedPhotoInteractionListener mOnSelectedPhotoInteractionListener;

        public ViewHolderSelectedPhoto(View itemView, OnSelectedPhotoInteractionListener onSelectedPhotoInteractionListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnSelectedPhotoInteractionListener = onSelectedPhotoInteractionListener;
        }

        public void onBind(final Photo photo, final int position, @NonNull Item.ItemType itemType) {
            mTvIndex.setText(String.valueOf(position + 1));

            if (itemType == Item.ItemType.REGULAR) {
                mIvRotate.setVisibility(View.VISIBLE);
            } else {
                mIvRotate.setVisibility(View.INVISIBLE);
            }

            if (itemType == Item.ItemType.VINTAGE) {
                mLlImage.setBackgroundResource(R.color.white);
                mTvMessage.setVisibility(View.VISIBLE);
            } else {
                mLlImage.setBackgroundResource(android.R.color.transparent);
                mTvMessage.setVisibility(View.GONE);
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

        }

        public interface OnSelectedPhotoInteractionListener {
            void onRotateClick(Photo photo, int position);

            void onIncreaseCountClick(Photo photo, int position);

            void onDecreaseCountClick(Photo photo, int position);

            void onCropClick(Photo photo, int position);
        }

    }

}
