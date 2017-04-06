package eu.mobiletouch.fotoland.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotos.ViewHolderPhoto;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotos.ViewHolderPhoto.OnPhotoClickListener;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;

/**
 * Created on 27-Aug-16.
 */
public class AdapterRvPhotos extends RecyclerView.Adapter<ViewHolderPhoto> {

    private Context mContext;
    private ArrayList<Photo> mPhotos;
    private OnPhotoClickListener mOnPhotoClickListener;

    public AdapterRvPhotos(@NonNull Context ctx, @NonNull ArrayList<Photo> photos, @NonNull OnPhotoClickListener onPhotoClickListener) {
        this.mContext = ctx;
        this.mPhotos = photos;
        this.mOnPhotoClickListener = onPhotoClickListener;
    }

    @Override
    public ViewHolderPhoto onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderPhoto(LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false), mOnPhotoClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderPhoto holder, int position) {
        holder.onBind(position, mPhotos.get(position));
    }

    @Override
    public int getItemCount() {
        return mPhotos == null ? 0 : mPhotos.size();
    }

    public void setPhotos(ArrayList<Photo> photos) {
        mPhotos = photos;
    }

    public static class ViewHolderPhoto extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_photo)
        ImageView mIvPhoto;
        @Bind(R.id.iv_check)
        ImageView mIvCheck;
        @Bind(R.id.iv_overlay)
        ImageView mIvOverlay;

        private OnPhotoClickListener mOnPhotoClickListener;

        public ViewHolderPhoto(View itemView, @NonNull OnPhotoClickListener onPhotoClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnPhotoClickListener = onPhotoClickListener;
        }

        public void onBind(final int position, final Photo photo) {
            Glide.with(itemView.getContext()).load(photo.getPhotoPath()).into(mIvPhoto);

            if (mOnPhotoClickListener.isSelected(position, photo)) {
                mIvCheck.setVisibility(View.VISIBLE);
                mIvOverlay.setVisibility(View.VISIBLE);
            } else {
                mIvCheck.setVisibility(View.GONE);
                mIvOverlay.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPhotoClickListener.onPhotoClick(position, photo);
                }
            });
        }

        public interface OnPhotoClickListener {
            void onPhotoClick(int position, Photo photo);

            boolean isSelected(int position, Photo photo);
        }

    }

}
