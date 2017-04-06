package eu.mobiletouch.fotoland.holders.localPhotos;

import java.io.Serializable;

import eu.mobiletouch.fotoland.enums.PhotoType;

/**
 * Created on 27-Aug-16.
 */
public class Photo implements Serializable {
    private static final long serialVersionUID = 7090567631674145986L;

    private long id;
    private String albumName;
    private String photoPath;
    private String croppedPhotoPath;
    private PhotoType mPhotoType = PhotoType.LOCAL;
    private int mQuantity = 1;
    private int mRotation;

    public long getId() {
        return id;
    }

    public Photo setId(long id) {
        this.id = id;
        return this;
    }

    public String getAlbumName() {
        return albumName;
    }

    public Photo setAlbumName(String name) {
        this.albumName = name;
        return this;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public Photo setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Photo && ((Photo) o).id == this.id && ((Photo) o).albumName.equals(this.albumName) && ((Photo) o).photoPath.equals(this.photoPath);
    }

    public PhotoType getPhotoType() {
        return mPhotoType;
    }

    public Photo setPhotoType(PhotoType photoType) {
        mPhotoType = photoType;
        return this;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public Photo setQuantity(int quantity) {
        mQuantity = quantity;
        return this;
    }

    public int getRotation() {
        return mRotation;
    }

    public Photo setRotation(int rotation) {
        mRotation = rotation;
        return this;
    }

    public String getCroppedPhotoPath() {
        return croppedPhotoPath;
    }

    public void setCroppedPhotoPath(String croppedPhotoPath) {
        this.croppedPhotoPath = croppedPhotoPath;
    }
}
