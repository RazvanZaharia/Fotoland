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
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotoAlbums.ViewHolderPhotoAlbum;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotoAlbums.ViewHolderPhotoAlbum.OnPhotoAlbumClickListener;
import eu.mobiletouch.fotoland.holders.localPhotos.PhotoAlbum;

/**
 * Created on 27-Aug-16.
 */
public class AdapterRvPhotoAlbums extends RecyclerView.Adapter<ViewHolderPhotoAlbum> {

    private Context mContext;
    private ArrayList<PhotoAlbum> mAlbums;
    private OnPhotoAlbumClickListener mOnPhotoAlbumClickListener;

    public AdapterRvPhotoAlbums(@NonNull Context ctx, @NonNull ArrayList<PhotoAlbum> albums, OnPhotoAlbumClickListener onPhotoAlbumClickListener) {
        this.mContext = ctx;
        this.mAlbums = albums;
        this.mOnPhotoAlbumClickListener = onPhotoAlbumClickListener;
    }

    @Override
    public ViewHolderPhotoAlbum onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderPhotoAlbum(LayoutInflater.from(mContext).inflate(R.layout.item_photo_album, parent, false), mOnPhotoAlbumClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderPhotoAlbum holder, int position) {
        holder.onBind(mAlbums.get(position));
    }

    @Override
    public int getItemCount() {
        return mAlbums == null ? 0 : mAlbums.size();
    }

    public static class ViewHolderPhotoAlbum extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_album)
        ImageView mIvAlbum;
        @Bind(R.id.tv_albumName)
        TextView mTvAlbumName;
        @Bind(R.id.tv_albumSize)
        TextView mTvAlbumSize;

        OnPhotoAlbumClickListener mOnPhotoAlbumClickListener;

        public ViewHolderPhotoAlbum(View itemView, OnPhotoAlbumClickListener onPhotoAlbumClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnPhotoAlbumClickListener = onPhotoAlbumClickListener;
        }

        public void onBind(final PhotoAlbum album) {
            Glide.with(itemView.getContext()).load(album.getCoverPath()).into(mIvAlbum);
            mTvAlbumName.setText(album.getName());

            switch (album.getPhotoType()) {
                case LOCAL:
                    mTvAlbumSize.setText(album.getAlbumPhotos() != null ? album.getAlbumPhotos().size() + "" : "0");
                    break;
                case FACEBOOK:
                default:
                    mTvAlbumSize.setText(album.getCount() > 0 ? album.getCount() + "" : "");
                    break;
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPhotoAlbumClickListener.onPhotoAlbumClick(album);
                }
            });
        }

        public interface OnPhotoAlbumClickListener {
            void onPhotoAlbumClick(PhotoAlbum album);
        }

    }

}
