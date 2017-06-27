package eu.mobiletouch.fotoland.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.io.File;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.x_base.BaseFragment;

/**
 * Created on 31-Oct-16.
 */
public class FragmentPhotobookPage extends BaseFragment {

    @Bind(R.id.tv_page_number)
    TextView mTvPageNumber;
    @Bind(R.id.tv_caption)
    TextView mTvCaption;
    @Bind(R.id.iv_page)
    ImageView mIvPage;

    private Photo mPhoto;

    public static FragmentPhotobookPage newInstance(@NonNull Photo photo) {
        FragmentPhotobookPage fragmentPhotobookPage = new FragmentPhotobookPage();
        Bundle args = new Bundle();
        args.putSerializable(Constants.PHOTO, photo);
        fragmentPhotobookPage.setArguments(args);
        return fragmentPhotobookPage;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_edit_page_photobook;
    }

    @Override
    protected void init() {
        super.init();
        mPhoto = (Photo) getArguments().getSerializable(Constants.PHOTO);
        if (mPhoto != null) {
            mTvPageNumber.setText(String.valueOf(mPhoto.getPhotobookPage()));
            if(!TextUtils.isEmpty(mPhoto.getCaptionText())) {
                mTvCaption.setText(mPhoto.getCaptionText());
                mTvCaption.setVisibility(View.VISIBLE);
            }
            else {
                mTvCaption.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(mPhoto.getCroppedPhotoPath())) {
                Glide.with(getActivity())
                        .load(mPhoto.getPhotoPath())
                        .signature(new StringSignature(String.valueOf(new File(mPhoto.getPhotoPath()).lastModified())))
                        .into(mIvPage);

            } else {
                Glide.with(getActivity())
                        .load(mPhoto.getCroppedPhotoPath())
                        .signature(new StringSignature(String.valueOf(new File(mPhoto.getCroppedPhotoPath()).lastModified())))
                        .into(mIvPage);
            }
        }
    }
}
