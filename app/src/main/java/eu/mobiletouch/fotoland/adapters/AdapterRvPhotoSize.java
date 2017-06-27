package eu.mobiletouch.fotoland.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotoSize.ViewHolderPhotoSize;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotoSize.ViewHolderPhotoSize.OnPhotoSizeClickListener;
import eu.mobiletouch.fotoland.enums.ProductType;
import eu.mobiletouch.fotoland.holders.Size;

/**
 * Created on 25-Aug-16.
 */
public class AdapterRvPhotoSize extends RecyclerView.Adapter<ViewHolderPhotoSize> {

    private Context mCtx;
    private OnPhotoSizeClickListener mOnPhotoSizeClickListener;
    private ArrayList<Size> mSizes;
    private ProductType mProductType;

    public AdapterRvPhotoSize(@NonNull Context ctx, @NonNull ArrayList<Size> sizes, ProductType type, OnPhotoSizeClickListener onPhotoSizeClickListener) {
        this(ctx, sizes, onPhotoSizeClickListener);
        this.mProductType = type;
    }

    public AdapterRvPhotoSize(@NonNull Context ctx, @NonNull ArrayList<Size> sizes, OnPhotoSizeClickListener onPhotoSizeClickListener) {
        this.mCtx = ctx;
        this.mSizes = sizes;
        this.mOnPhotoSizeClickListener = onPhotoSizeClickListener;
        mProductType = ProductType.PRINTS;
    }

    @Override
    public ViewHolderPhotoSize onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderPhotoSize holder;
        switch (mProductType) {
            case PHOTOBOOK:
                holder = ViewHolderPhotobookSize.getInstance(mCtx, parent, mOnPhotoSizeClickListener);
                break;
            case PRINTS:
            default:
                holder = ViewHolderPhotoSize.getInstance(mCtx, parent, mOnPhotoSizeClickListener);

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderPhotoSize holder, int position) {
        holder.onBind(mSizes.get(position));
    }

    @Override
    public int getItemCount() {
        return mSizes == null ? 0 : mSizes.size();
    }

    public static class ViewHolderPhotobookSize extends ViewHolderPhotoSize {
        @Bind(R.id.ck_selectSize)
        CompoundButton mCkSelectSize;

        public static ViewHolderPhotobookSize getInstance(@NonNull Context ctx, @NonNull ViewGroup parent, OnPhotoSizeClickListener onPhotoSizeClickListener) {
            return new ViewHolderPhotobookSize(LayoutInflater.from(ctx).inflate(R.layout.item_photobook_size, parent, false), onPhotoSizeClickListener);
        }

        private ViewHolderPhotobookSize(View itemView, OnPhotoSizeClickListener onPhotoSizeClickListener) {
            super(itemView, onPhotoSizeClickListener);
        }

        @Override
        protected void setName(Size size) {
            //ToDO change with photobook price for @size
            super.setName(size);
        }

        @Override
        protected void selectItem(boolean select) {
            mCkSelectSize.setChecked(select);
        }
    }

    public static class ViewHolderPhotoSize extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView mTvName;

        @Bind(R.id.iv_icon)
        ImageView mIvIcon;

        private OnPhotoSizeClickListener mOnPhotoSizeClickListener;

        public static ViewHolderPhotoSize getInstance(@NonNull Context ctx, @NonNull ViewGroup parent, OnPhotoSizeClickListener onPhotoSizeClickListener) {
            return new ViewHolderPhotoSize(LayoutInflater.from(ctx).inflate(R.layout.item_detail_type, parent, false), onPhotoSizeClickListener);
        }

        private ViewHolderPhotoSize(View itemView, OnPhotoSizeClickListener onPhotoSizeClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnPhotoSizeClickListener = onPhotoSizeClickListener;
        }

        public void onBind(@NonNull final Size size) {
            setName(size);
            Glide.with(itemView.getContext()).load(size.getIconRes()).into(mIvIcon);

            if (mOnPhotoSizeClickListener != null && mOnPhotoSizeClickListener.getSelectedPhotoSize() != null && size.equals(mOnPhotoSizeClickListener.getSelectedPhotoSize())) {
                selectItem(true);
            } else {
                selectItem(false);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnPhotoSizeClickListener != null) {
                        mOnPhotoSizeClickListener.onPhotoSizeSelect(size);
                    }
                }
            });
        }

        protected void setName(Size size) {
            mTvName.setText(size.getSizeToDisplay());
        }

        @SuppressWarnings("ResourceAsColor")
        protected void selectItem(boolean select) {
            int textColor = select ? ContextCompat.getColor(itemView.getContext(), R.color.blue) : ContextCompat.getColor(itemView.getContext(), R.color.gray);
            mTvName.setTextColor(textColor);
            mIvIcon.setColorFilter(textColor);
        }

        public interface OnPhotoSizeClickListener {
            void onPhotoSizeSelect(Size size);

            Size getSelectedPhotoSize();
        }

    }
}
