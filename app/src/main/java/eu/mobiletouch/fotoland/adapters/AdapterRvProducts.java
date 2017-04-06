package eu.mobiletouch.fotoland.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import eu.mobiletouch.fotoland.holders.Product;

/**
 * Created on 10-Aug-16.
 */
public class AdapterRvProducts extends RecyclerView.Adapter<AdapterRvProducts.ViewHolderProduct> {

    private Context mCtx;
    private OnProductClickListener mOnProductClickListener;
    private ArrayList<Product> mProducts;

    public AdapterRvProducts(@NonNull Context ctx, @NonNull ArrayList<Product> products, OnProductClickListener onProductClickListener) {
        this.mCtx = ctx;
        this.mProducts = products;
        this.mOnProductClickListener = onProductClickListener;
    }

    @Override
    public ViewHolderProduct onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderProduct(LayoutInflater.from(mCtx).inflate(R.layout.item_product, parent, false), mOnProductClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderProduct holder, int position) {
        Product product = mProducts.get(position);
        holder.onBind(product);
    }

    @Override
    public int getItemCount() {
        return mProducts != null ? mProducts.size() : 0;
    }

    public static class ViewHolderProduct extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_offer)
        TextView tvOffer;

        private OnProductClickListener onProductClickListener;

        public ViewHolderProduct(View itemView, OnProductClickListener onProductClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onProductClickListener = onProductClickListener;
        }

        public void onBind(@NonNull final Product product) {
            tvName.setText(product.getName());
            Glide.with(itemView.getContext()).load(product.getIconRes()).into(ivIcon);

            if (!TextUtils.isEmpty(product.getNoOfFree())) {
                tvOffer.setVisibility(View.VISIBLE);
                tvOffer.setText(product.getNoOfFree().concat("\n").concat("FREE\n").concat(product.getName()));
            } else {
                tvOffer.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onProductClickListener != null) {
                        onProductClickListener.onProductClick(product);
                    }
                }
            });
        }
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

}
