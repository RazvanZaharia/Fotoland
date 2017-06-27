package eu.mobiletouch.fotoland.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import eu.mobiletouch.fotoland.adapters.AdapterRvPaperType.ViewHolderPaperType;
import eu.mobiletouch.fotoland.adapters.AdapterRvPaperType.ViewHolderPaperType.OnPaperTypeClickListener;
import eu.mobiletouch.fotoland.holders.Paper;

/**
 * Created on 25-Aug-16.
 */
public class AdapterRvPaperType extends RecyclerView.Adapter<ViewHolderPaperType> {

    private Context mCtx;
    private OnPaperTypeClickListener mOnPaperTypeClickListener;
    private ArrayList<Paper> mPapers;

    public AdapterRvPaperType(@NonNull Context ctx, @NonNull ArrayList<Paper> papers, OnPaperTypeClickListener onPaperTypeClickListener) {
        this.mCtx = ctx;
        this.mPapers = papers;
        this.mOnPaperTypeClickListener = onPaperTypeClickListener;
    }

    @Override
    public ViewHolderPaperType onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderPaperType(LayoutInflater.from(mCtx).inflate(R.layout.item_detail_type, parent, false), mOnPaperTypeClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderPaperType holder, int position) {
        holder.onBind(mPapers.get(position));
    }

    @Override
    public int getItemCount() {
        return mPapers == null ? 0 : mPapers.size();
    }

    public static class ViewHolderPaperType extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.iv_icon)
        ImageView mIvIcon;

        private OnPaperTypeClickListener mOnPaperTypeClickListener;

        public ViewHolderPaperType(View itemView, OnPaperTypeClickListener onPaperTypeClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnPaperTypeClickListener = onPaperTypeClickListener;
        }

        public void onBind(@NonNull final Paper paper) {
            mTvName.setText(paper.getName());
            if (paper.getIconRes() != 0) {
                Glide.with(itemView.getContext()).load(paper.getIconRes()).into(mIvIcon);
            }

            if (mOnPaperTypeClickListener != null && mOnPaperTypeClickListener.getSelectedPaper() != null && paper.equals(mOnPaperTypeClickListener.getSelectedPaper())) {
                selectItem(true);
            } else {
                selectItem(false);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnPaperTypeClickListener != null) {
                        mOnPaperTypeClickListener.onPaperSelect(paper);
                    }
                }
            });
        }

        @SuppressWarnings("ResourceAsColor")
        private void selectItem(boolean select) {
            int textColor = select ? ContextCompat.getColor(itemView.getContext(), R.color.blue) : ContextCompat.getColor(itemView.getContext(), R.color.gray);
            mTvName.setTextColor(textColor);
            mIvIcon.setColorFilter(textColor);
        }

        public interface OnPaperTypeClickListener {
            void onPaperSelect(Paper paper);

            Paper getSelectedPaper();
        }

    }
}
