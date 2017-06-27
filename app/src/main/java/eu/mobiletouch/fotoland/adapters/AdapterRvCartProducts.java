package eu.mobiletouch.fotoland.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvCartProducts.ViewHolderCartProduct;
import eu.mobiletouch.fotoland.adapters.AdapterRvCartProducts.ViewHolderCartProduct.OnCartProductListener;
import eu.mobiletouch.fotoland.enums.ProductType;
import eu.mobiletouch.fotoland.holders.UserSelections;

/**
 * Created on 19-Sep-16.
 */
public class AdapterRvCartProducts extends RecyclerView.Adapter<ViewHolderCartProduct> {

    private Context mCtx;
    private OnCartProductListener mOnCartProductListener;
    private ArrayList<UserSelections> mDataSet;

    public AdapterRvCartProducts(@NonNull Context ctx, OnCartProductListener onCartProductListener) {
        this.mCtx = ctx;
        this.mOnCartProductListener = onCartProductListener;
    }

    @Override
    public ViewHolderCartProduct onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderCartProduct(LayoutInflater.from(mCtx).inflate(R.layout.item_cart_product, parent, false), mOnCartProductListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderCartProduct holder, int position) {
        holder.onBind(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public void setDataSet(ArrayList<UserSelections> dataSet) {
        if (mDataSet == null) {
            mDataSet = new ArrayList<>(dataSet);
        } else {
            mDataSet.clear();
            mDataSet.addAll(dataSet);
        }
    }

    public static class ViewHolderCartProduct extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_cartProductImage)
        ImageView mIvCartProductImage;
        @Bind(R.id.tv_cartProductEdit)
        TextView mTvCartProductEdit;
        @Bind(R.id.tv_cartProductName)
        TextView mTvCartProductName;
        @Bind(R.id.tv_cartProductItemType)
        TextView mTvCartProductItemType;
        @Bind(R.id.tv_cartProductPrice)
        TextView mTvCartProductPrice;
        @Bind(R.id.layout_quantity)
        View mLayoutQuantity;
        @Bind(R.id.iv_decrease)
        View mIvDecrease;
        @Bind(R.id.iv_increase)
        View mIvIncrease;
        @Bind(R.id.tv_quantity)
        TextView mTvQuantity;
        @Bind(R.id.iv_cartProductDelete)
        View mIvCartProductDelete;

        private OnCartProductListener mOnCartProductListener;

        public ViewHolderCartProduct(View itemView, OnCartProductListener onCartProductListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnCartProductListener = onCartProductListener;
        }

        public void onBind(final UserSelections item) {
            Glide.with(itemView.getContext()).load(item.getSelectedPhotos().get(0).getPhotoPath()).into(mIvCartProductImage);

            mTvCartProductName.setText(String.valueOf(item.getPhotosCount())
                    .concat(" ")
                    .concat(item.getSelectedItem().getName()));

            if (item.getSelectedItem().getSelectedPaper() != null) {// if item has multiple papers
                mTvCartProductItemType.setText(item.getSelectedItem().getSelectedSize().getSizeToDisplay()
                        .concat(", ")
                        .concat(item.getSelectedItem().getSelectedPaper().getName()));
            }

            if (item.getSelectedProduct().getProductType() == ProductType.PHOTOBOOK) {
                mIvCartProductDelete.setVisibility(View.GONE);
                mLayoutQuantity.setVisibility(View.VISIBLE);
            } else {
                mIvCartProductDelete.setVisibility(View.VISIBLE);
                mLayoutQuantity.setVisibility(View.GONE);
            }

            mTvQuantity.setText(String.valueOf(item.getQuantity()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCartProductListener.onProductClick(item, getAdapterPosition());
                }
            });

            mIvIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCartProductListener.onIncreaseQuantity(item, getAdapterPosition());
                }
            });

            mIvDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCartProductListener.onDecreaseQuantity(item, getAdapterPosition());
                }
            });

            mIvCartProductDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCartProductListener.onDelete(item, getAdapterPosition());
                }
            });

        }

        public interface OnCartProductListener {
            void onProductClick(UserSelections cartProduct, int position);

            void onIncreaseQuantity(UserSelections cartProduct, int position);

            void onDecreaseQuantity(UserSelections cartProduct, int position);

            void onDelete(UserSelections cartProduct, int position);
        }

    }

}
