package eu.mobiletouch.fotoland.collage;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import eu.mobiletouch.fotoland.R;

/**
 * Created on 27-Oct-16.
 */
public class AdapterCollagePatterns extends RecyclerView.Adapter<AdapterCollagePatterns.ViewHolderCollagePattern> {
    private List<Integer> mDataSet;
    private OnCollagePatternSelect mOnCollagePatternSelect;

    public AdapterCollagePatterns(@NonNull List<Integer> ids, OnCollagePatternSelect onCollagePatternSelect) {
        mDataSet = ids;
        mOnCollagePatternSelect = onCollagePatternSelect;
    }

    @Override
    public ViewHolderCollagePattern onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderCollagePattern(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collage_pattern, parent, false), mOnCollagePatternSelect);
    }

    @Override
    public void onBindViewHolder(ViewHolderCollagePattern holder, int position) {
        holder.bind(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet != null ? mDataSet.size() : 0;
    }

    static class ViewHolderCollagePattern extends RecyclerView.ViewHolder {
        private OnCollagePatternSelect mOnCollagePatternSelect;

        public ViewHolderCollagePattern(View itemView, OnCollagePatternSelect onCollagePatternSelect) {
            super(itemView);
            this.mOnCollagePatternSelect = onCollagePatternSelect;
        }

        public void bind(final int id) {
            Glide.with(itemView.getContext()).load(id).into(((ImageView) itemView));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCollagePatternSelect.onSelect(id);
                }
            });
        }

    }

    public interface OnCollagePatternSelect {
        void onSelect(Integer idOfPattern);
    }

}
