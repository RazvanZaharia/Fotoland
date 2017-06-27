package eu.mobiletouch.fotoland.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import eu.mobiletouch.fotoland.collage.AdapterCollagePatterns;
import eu.mobiletouch.fotoland.collage.CollagePatternsContractsUtils;
import eu.mobiletouch.fotoland.collage.PhotoView;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.mvps.MvpActivityMakeCollage;
import eu.mobiletouch.fotoland.utils.Constants;
import eu.mobiletouch.fotoland.utils.Utils;

/**
 * Created on 05-Oct-16.
 */
public class PresenterActivityMakeCollage extends BasePresenter<MvpActivityMakeCollage> implements AdapterCollagePatterns.OnCollagePatternSelect,
        View.OnClickListener {

    protected Context mCtx;
    protected ArrayList<Photo> mAddedPhotos;
    protected ArrayList<PhotoView> mPhotoViews;
    protected int mConfiguredPhotoViewIndex;
    protected int mClickedPhotoViewIndex;

    public PresenterActivityMakeCollage(@NonNull Context ctx) {
        this.mCtx = ctx;
    }

    public void init(@NonNull ArrayList<Photo> preselectedPhotos) {
        this.mAddedPhotos = preselectedPhotos;
        showCollagePatternsIcons();
    }

    public void onSaveMenuClick() {
        Bitmap collageBitmap = getMvpView().getCollageBitmap();
        FileOutputStream out = null;
        Intent resultData = new Intent();
        try {
            File toSaveFile = getToSaveFile();
            out = new FileOutputStream(toSaveFile);
            collageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            collageBitmap.recycle();
            collageBitmap = null;

            resultData.putExtra(Constants.ARG_FILE_PATH, toSaveFile.getAbsolutePath());
            getMvpView().setResultAndFinish(Activity.RESULT_OK, resultData);

        } catch (Exception e) {
            e.printStackTrace();
            getMvpView().setResultAndFinish(Activity.RESULT_CANCELED, null);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected File getToSaveFile() {
        String file_path = Utils.getAppFilePath(mCtx);
        File dir = new File(file_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, "collage" + System.currentTimeMillis() + ".png");
    }

    private void showCollagePatternsIcons() {
        List<Integer> patternsIconsList = new ArrayList<>();
        patternsIconsList.addAll(CollagePatternsContractsUtils.collagePatternsContracts.keySet());
        getMvpView().setCollagePatterns(patternsIconsList);
    }

    @Override
    public void onSelect(Integer idOfPattern) {
        mConfiguredPhotoViewIndex = 0;
        mPhotoViews = new ArrayList<>();
        getMvpView().addThisCollagePatternLayout(CollagePatternsContractsUtils.collagePatternsContracts.get(idOfPattern));
    }

    public void configurePhotoView(@NonNull PhotoView photoView) {
        photoView.setMaxInitialScale(1);
        photoView.setFullScreen(true, false);
        photoView.enableImageTransforms(true);
        photoView.setOnClickListener(this);

        if (mAddedPhotos.size() > mConfiguredPhotoViewIndex) {
            loadPhoto(photoView, mAddedPhotos.get(mConfiguredPhotoViewIndex));
        } else {
            if (mAddedPhotos != null && mAddedPhotos.size() > mConfiguredPhotoViewIndex) {
                mAddedPhotos.add(mAddedPhotos.get(mConfiguredPhotoViewIndex));
                loadPhoto(photoView, mAddedPhotos.get(mConfiguredPhotoViewIndex));
            } else {
                mAddedPhotos.add(new Photo());
            }
        }
        mPhotoViews.add(photoView);

        mConfiguredPhotoViewIndex += 1;
    }

    protected void loadPhoto(@NonNull final PhotoView photoView, @NonNull Photo photo) {
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    return Glide.
                            with(mCtx).
                            load(params[0]).
                            asBitmap().
                            into(-1, -1).
                            get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                photoView.bindPhoto(bitmap);
            }
        }.execute(photo.getPhotoPath());
    }

    public void onSinglePictureSelected(@NonNull final Photo photo) {
        loadPhoto(mPhotoViews.get(mClickedPhotoViewIndex), photo);
        mAddedPhotos.set(mClickedPhotoViewIndex, photo);
    }

    @Override
    public void onClick(View clickedView) {
        if (clickedView instanceof PhotoView) {
            mClickedPhotoViewIndex = mPhotoViews.indexOf(clickedView);
            getMvpView().selectSinglePicture();
        }
    }
}
