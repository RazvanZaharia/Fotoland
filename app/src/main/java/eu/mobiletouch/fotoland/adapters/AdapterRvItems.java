package eu.mobiletouch.fotoland.adapters;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.holders.Item;

/**
 * Created on 24-Aug-16.
 */
public class AdapterRvItems extends RecyclerView.Adapter<AdapterRvItems.BaseViewHolderItem> {

    private static final int ITEM_TYPE = 1;
    private static final int OFFER_BANNER_TYPE = 2;

    private Context mCtx;
    private OnItemClickListener mOnItemClickListener;
    private ViewHolderOfferBanner.OnOfferBannerClickListener mOnOfferBannerClickListener;
    private ArrayList<Item> mItems;
    @DrawableRes
    private int mOfferBannerResId;

    public AdapterRvItems(@NonNull Context ctx, @NonNull ArrayList<Item> items, @DrawableRes int offerBannerResId, OnItemClickListener onItemClickListener, ViewHolderOfferBanner.OnOfferBannerClickListener onOfferBannerClickListener) {
        this.mCtx = ctx;
        this.mItems = items;
        this.mOfferBannerResId = offerBannerResId;
        this.mOnItemClickListener = onItemClickListener;
        this.mOnOfferBannerClickListener = onOfferBannerClickListener;
    }

    @Override
    public BaseViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case OFFER_BANNER_TYPE:
                return new ViewHolderOfferBanner(new ImageView(mCtx), mOnOfferBannerClickListener);
        }
        return new ViewHolderItem(LayoutInflater.from(mCtx).inflate(R.layout.item_product_item, parent, false), mOnItemClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        if (hasOfferBanner() && position == 0) {
            return OFFER_BANNER_TYPE;
        }
        return ITEM_TYPE;
    }

    @Override
    public void onBindViewHolder(BaseViewHolderItem holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE) {
            if (hasOfferBanner()) {
                position -= 1;
            }
            Item item = mItems.get(position);
            holder.onBind(item);
        }
        if (getItemViewType(position) == OFFER_BANNER_TYPE) {
            holder.onBind(mOfferBannerResId);
        }
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : (hasOfferBanner() ? mItems.size() + 1 : mItems.size());
    }

    private boolean hasOfferBanner() {
        return mOfferBannerResId != 0;
    }

    public static class ViewHolderOfferBanner extends BaseViewHolderItem {

        private OnOfferBannerClickListener mOnOfferBannerClickListener;

        public ViewHolderOfferBanner(View itemView, OnOfferBannerClickListener onOfferBannerClickListener) {
            super(itemView);
            this.mOnOfferBannerClickListener = onOfferBannerClickListener;
        }

        public void onBind(@DrawableRes int offerBannerResId) {
            Glide.with(itemView.getContext()).load(offerBannerResId).into((ImageView) itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnOfferBannerClickListener != null) {
                        mOnOfferBannerClickListener.onOfferBannerClick();
                    }
                }
            });
        }

        public interface OnOfferBannerClickListener {
            void onOfferBannerClick();
        }
    }

    public static class ViewHolderItem extends BaseViewHolderItem {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_description)
        TextView tvDescription;
        @Bind(R.id.btn_itemSelect)
        Button btnItemSelect;

        private OnItemClickListener mOnItemClickListener;

        public ViewHolderItem(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnItemClickListener = onItemClickListener;
        }

        public void onBind(@NonNull final Item item) {
            tvName.setText(item.getName());
            tvDescription.setText("");
            if (item.getSizes() != null) {
                if (item.getSizes().size() == 1) {
                    tvDescription.setText(item.getSizes().get(0).getSizeToDisplay());
                }

                if (item.getSizes().size() > 1) {
                    tvDescription.setText(itemView.getContext().getString(R.string.description_item_regular));
                }
            }

            Glide.with(itemView.getContext()).load(item.getIconRes()).into(ivIcon);

            btnItemSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(item);
                    }
                }
            });
        }
    }

    public static class BaseViewHolderItem extends RecyclerView.ViewHolder {
        public BaseViewHolderItem(View itemView) {
            super(itemView);
        }

        public void onBind(@NonNull final Item item) {
        }

        public void onBind(@DrawableRes int offerBannerResId) {
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }
}
