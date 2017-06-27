package eu.mobiletouch.fotoland.holders.localPhotos;

import java.io.Serializable;

import eu.mobiletouch.fotoland.enums.Orientation;
import eu.mobiletouch.fotoland.enums.PhotoType;
import eu.mobiletouch.fotoland.interfaces.SelectedPhotoItem;
import eu.mobiletouch.fotoland.utils.ObjectsUtils;

/**
 * Created on 27-Aug-16.
 */
public class Photo implements Serializable, SelectedPhotoItem {
    private static final long serialVersionUID = 7090567631674145986L;

    private long id;
    private String albumName;
    private String photoPath;
    private String croppedPhotoPath;
    private PhotoType mPhotoType = PhotoType.LOCAL;
    private int mQuantity = 1;
    private int mRotation;
    private Orientation mOrientation = Orientation.NO_ORIENTATION;
    private float mPhotoWidth;
    private float mPhotoHeight;
    private float mPpi = 0.0f;
    private String mCaptionText;
    private int mPhotobookPage;

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
        return o instanceof Photo
                && ObjectsUtils.equals(((Photo) o).id, this.id)
                && ObjectsUtils.equals(((Photo) o).albumName, this.albumName)
                && ObjectsUtils.equals(((Photo) o).photoPath, this.photoPath)
                && ObjectsUtils.equals(((Photo) o).croppedPhotoPath, this.croppedPhotoPath);
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

    public Photo setCroppedPhotoPath(String croppedPhotoPath) {
        this.croppedPhotoPath = croppedPhotoPath;
        return this;
    }

    public Orientation getOrientation() {
        if(mOrientation == Orientation.NO_ORIENTATION) {
            if(mPhotoWidth >= mPhotoHeight) {
                mOrientation = Orientation.LANDSCAPE;
            }
            else {
                mOrientation = Orientation.PORTRAIT;
            }
        }
        return mOrientation;
    }

    public Photo setOrientation(Orientation orientation) {
        mOrientation = orientation;
        return this;
    }

    public float getPpi() {
        return mPpi;
    }

    public Photo setPpi(float ppi) {
        mPpi = ppi;
        return this;
    }

    public float getPhotoHeight() {
        return mPhotoHeight;
    }

    public Photo setPhotoHeight(float photoHeight) {
        mPhotoHeight = photoHeight;
        return this;
    }

    public float getPhotoWidth() {
        return mPhotoWidth;
    }

    public Photo setPhotoWidth(float photoWidth) {
        mPhotoWidth = photoWidth;
        return this;
    }

    public String getCaptionText() {
        return mCaptionText;
    }

    public Photo setCaptionText(String captionText) {
        mCaptionText = captionText;
        return this;
    }

    public int getPhotobookPage() {
        return mPhotobookPage;
    }

    public void setPhotobookPage(int photobookPage) {
        mPhotobookPage = photobookPage;
    }

    @Override
    public DisplayItemType getDisplayItemType() {
        return DisplayItemType.PHOTO;
    }
}
