package eu.mobiletouch.fotoland.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.enums.Orientation;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.holders.localPhotos.PhotoAlbum;
import eu.mobiletouch.fotoland.interfaces.OnLocalImagesObtained;

/**
 * Created on 27-Aug-16.
 */
public class DeviceManager {

    public static void getPhoneAlbums(final Context context, final OnLocalImagesObtained listener) {
        new AsyncTask<Void, Void, ArrayList<PhotoAlbum>>() {
            @Override
            protected ArrayList<PhotoAlbum> doInBackground(Void... params) {
                // Creating vectors to hold the final albums objects and albums names
                ArrayList<PhotoAlbum> phoneAlbums = new ArrayList<>();

                // content: style URI for the "primary" external storage volume
                Uri externalImages = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Uri sdCardImages = MediaStore.Images.Media.getContentUri("sdCard");
                Uri internalImages = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

                phoneAlbums.addAll(getImagesForUri(context, externalImages));
                phoneAlbums.addAll(getImagesForUri(context, internalImages));

                return phoneAlbums;
            }

            @Override
            protected void onPostExecute(ArrayList<PhotoAlbum> photoAlba) {
                super.onPostExecute(photoAlba);
                if (photoAlba.size() > 0) {
                    listener.onComplete(photoAlba);
                } else {
                    listener.onError();
                }
            }
        }.execute();
    }

    private static ArrayList<PhotoAlbum> getImagesForUri(Context context, Uri uri) {
        // Creating vectors to hold the final albums objects and albums names
        ArrayList<PhotoAlbum> phoneAlbums = new ArrayList<>();
        ArrayList<String> albumsNames = new ArrayList<>();

        // which image properties are we querying
        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.ORIENTATION
        };
        // Make the query.
        Cursor cursor = context.getContentResolver().query(uri,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                MediaStore.MediaColumns.DATE_ADDED + " DESC"        // Ordering
        );

        if (cursor != null && cursor.getCount() > 0) {
            Log.i("DeviceImageManager", " query count=" + cursor.getCount());

            if (cursor.moveToFirst()) {
                String bucketName;
                String data;
                String imageId;
                String rotationId;

                int bucketNameColumn = cursor.getColumnIndex(
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                int imageUriColumn = cursor.getColumnIndex(
                        MediaStore.Images.Media.DATA);

                int imageIdColumn = cursor.getColumnIndex(
                        MediaStore.Images.Media._ID);

                int imageIdOrientation = cursor.getColumnIndex(
                        MediaStore.Images.Media.ORIENTATION);

                do {
                    // Get the field values
                    bucketName = cursor.getString(bucketNameColumn);
                    data = cursor.getString(imageUriColumn);
                    imageId = cursor.getString(imageIdColumn);
                    rotationId = cursor.getString(imageIdOrientation);

                    // Adding a new PhonePhoto object to phonePhotos vector
                    Photo phonePhoto = new Photo();
                    phonePhoto.setAlbumName(bucketName);
                    phonePhoto.setPhotoPath(data);
                    phonePhoto.setId(Integer.valueOf(imageId));
                    phonePhoto = Utils.setPhotoDimensions(phonePhoto);

                    if (!TextUtils.isEmpty(rotationId)) {
                        int rotation = Integer.parseInt(rotationId);
                        phonePhoto.setOrientation((rotation == 0 || rotation == 180) ? Orientation.LANDSCAPE : Orientation.PORTRAIT);
                    }

                    if (albumsNames.contains(bucketName)) {
                        for (PhotoAlbum album : phoneAlbums) {
                            if (album.getName().equals(bucketName)) {
                                album.getAlbumPhotos().add(phonePhoto);
                                Log.i("DeviceImageManager", "A photo was added to album => " + bucketName);
                                break;
                            }
                        }
                    } else {
                        PhotoAlbum album = new PhotoAlbum();
                        Log.i("DeviceImageManager", "A new album was created => " + bucketName);
                        album.setId(phonePhoto.getId());
                        album.setName(bucketName);
                        album.setCoverPath(phonePhoto.getPhotoPath());
                        album.getAlbumPhotos().add(phonePhoto);
                        Log.i("DeviceImageManager", "A photo was added to album => " + bucketName);

                        phoneAlbums.add(album);
                        albumsNames.add(bucketName);
                    }

                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        return phoneAlbums;
    }

}
