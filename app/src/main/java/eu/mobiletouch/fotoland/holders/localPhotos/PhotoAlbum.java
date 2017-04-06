package eu.mobiletouch.fotoland.holders.localPhotos;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.enums.PhotoType;

/**
 * Created on 27-Aug-16.
 */
public class PhotoAlbum implements Serializable {
    private static final long serialVersionUID = -6182335767265398271L;

    private long id;
    private String name;
    private String coverPath;
    private ArrayList<Photo> albumPhotos;
    private int mCount;
    private PhotoType mPhotoType = PhotoType.LOCAL;

    public long getId() {
        return id;
    }

    public PhotoAlbum setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PhotoAlbum setName(String name) {
        this.name = name;
        return this;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public PhotoAlbum setCoverPath(String albumCoverUri) {
        this.coverPath = albumCoverUri;
        return this;
    }

    public ArrayList<Photo> getAlbumPhotos() {
        if (albumPhotos == null) {
            albumPhotos = new ArrayList<>();
        }
        return albumPhotos;
    }

    public PhotoAlbum setAlbumPhotos(ArrayList<Photo> albumPhotos) {
        this.albumPhotos = albumPhotos;
        return this;
    }

    public PhotoType getPhotoType() {
        return mPhotoType;
    }

    public PhotoAlbum setPhotoType(PhotoType photoType) {
        mPhotoType = photoType;
        return this;
    }

    public int getCount() {
        return mCount;
    }

    public PhotoAlbum setCount(int count) {
        mCount = count;
        return this;
    }
}
