package eu.mobiletouch.fotoland.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import eu.mobiletouch.fotoland.enums.PhotoType;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;

/**
 * Created on 14-Sep-16.
 */
public class DownloadImagesAsync extends AsyncTask<ArrayList<Photo>, Void, ArrayList<Photo>> {
    private static final String TAG = DownloadImagesAsync.class.getName();

    private Context mCtx;
    private OnImagesDownloadListener mOnImagesDownloadListener;

    public DownloadImagesAsync(@NonNull Context ctx, @NonNull OnImagesDownloadListener onImagesDownloadListener) {
        this.mCtx = ctx;
        this.mOnImagesDownloadListener = onImagesDownloadListener;
    }

    @Nullable
    private Bitmap getBitmapForPath(String path) {
        Bitmap downloadedBitmap = null;
        try {
            downloadedBitmap = Glide.with(mCtx)
                    .load(path)
                    .asBitmap()
                    .override(2048, 2048)
                    .into(-1, -1)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return downloadedBitmap;
    }

    private File getToSaveFile(@NonNull Photo photo) {
        String file_path = Utils.getAppFilePath(mCtx);
        File dir = new File(file_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, photo.getId() + ".png");
    }

    @Nullable
    private String saveBitmapToFile(@NonNull Photo downloadedPhoto, @NonNull Bitmap photoBitmap) {
        FileOutputStream out = null;
        try {
            File toSaveFile = getToSaveFile(downloadedPhoto);
            out = new FileOutputStream(toSaveFile);
            photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            photoBitmap.recycle();
            photoBitmap = null;
            return toSaveFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
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
        return null;
    }

    @Override
    @SafeVarargs
    protected final ArrayList<Photo> doInBackground(ArrayList<Photo>... params) {
        ArrayList<Photo> toDownloadPhotos = params[0];
        ArrayList<Photo> downloadedPhotos = new ArrayList<>();

        for (Photo photo : toDownloadPhotos) {
            if (photo.getPhotoType() == PhotoType.LOCAL) {
                downloadedPhotos.add(photo);
            } else {
                Bitmap photoBitmap = getBitmapForPath(photo.getPhotoPath());
                if (photoBitmap == null) {
                    continue;
                }
                String filePath = saveBitmapToFile(photo, photoBitmap);

                if (filePath == null) {
                    continue;
                }

                Log.d(TAG, "downloaded facebook " + photo.getId());

                Photo downloadedPhoto = SerializationUtils.clone(photo);
                downloadedPhoto.setCroppedPhotoPath(null);
                downloadedPhoto.setPhotoPath(filePath);
                downloadedPhoto.setPhotoWidth(photoBitmap.getWidth());
                downloadedPhoto.setPhotoHeight(photoBitmap.getHeight());
                downloadedPhotos.add(downloadedPhoto);
            }
        }
        return downloadedPhotos;
    }

    @Override
    protected void onPostExecute(ArrayList<Photo> photos) {
        super.onPostExecute(photos);
        if (mOnImagesDownloadListener != null) {
            mOnImagesDownloadListener.onDownloadFinished(photos);
        }
    }

    public interface OnImagesDownloadListener {
        void onDownloadFinished(ArrayList<Photo> photos);
    }

}
