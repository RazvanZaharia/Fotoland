package eu.mobiletouch.fotoland.presenters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.internal.ShareConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotoAlbums.ViewHolderPhotoAlbum.OnPhotoAlbumClickListener;
import eu.mobiletouch.fotoland.adapters.AdapterRvPhotos.ViewHolderPhoto.OnPhotoClickListener;
import eu.mobiletouch.fotoland.enums.PhotoType;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;
import eu.mobiletouch.fotoland.holders.localPhotos.PhotoAlbum;
import eu.mobiletouch.fotoland.interfaces.OnSelectPhotosFragmentsListener;
import eu.mobiletouch.fotoland.mvps.MvpFragmentFacebookPhotos;
import eu.mobiletouch.fotoland.utils.Utils;

/**
 * Created on 28-Aug-16.
 */
public class PresenterFragmentFacebookPhotos extends BasePhotosPresenter<MvpFragmentFacebookPhotos> implements OnPhotoAlbumClickListener, OnPhotoClickListener {

    private Activity mActivity;
    private CallbackManager mFacebookCallbackManager;
    private GraphRequestAsyncTask mAlbumsRequest;
    private GraphRequestAsyncTask mPhotosRequest;
    private Fragment mCurrentFragment;
    private OnSelectPhotosFragmentsListener mOnSelectPhotosFragmentsListener;
    private ArrayList<PhotoAlbum> mFbAlbums;
    private ArrayList<Photo> mPhotos;

    public PresenterFragmentFacebookPhotos(@NonNull Activity activity) {
        this.mActivity = activity;
    }

    public void init(Fragment currentFragment, OnSelectPhotosFragmentsListener onSelectPhotosFragmentsListener) {
        this.mOnSelectPhotosFragmentsListener = onSelectPhotosFragmentsListener;
        this.mCurrentFragment = currentFragment;
        mFacebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(this.mFacebookCallbackManager, new FacebookLoginCallback());

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (!(accessToken == null || accessToken.isExpired() || !accessToken.getPermissions().contains("user_photos"))) {
            loadAlbums();
        } else {
            getMvpView().showEmptyView();
        }

    }

    public void onFacebookLoginClick() {
        LoginManager.getInstance().logInWithReadPermissions(mCurrentFragment, Arrays.asList(new String[]{"public_profile", "user_photos", NotificationCompat.CATEGORY_EMAIL}));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void loadAlbums() {
        if (this.mAlbumsRequest != null) {
            return;
        }
        if (Utils.isNetworkAvailable(mActivity)) {
            showLoading(mActivity);
            Bundle args = new Bundle();
            args.putString(GraphRequest.FIELDS_PARAM, "id,count,cover_photo,name");
            this.mAlbumsRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "me/albums", args, null, new FacebookAlbumsCallback()).executeAsync();
        } else {
            getMvpView().showLoginErrorMessage(mActivity.getString(R.string.label_messageNoInternetError));
        }
    }

    private void onAlbumsLoaded(JSONArray albumsData) throws JSONException {
        if (mActivity != null && !mActivity.isFinishing()) {
            String token = AccessToken.getCurrentAccessToken().getToken();
            mFbAlbums = new ArrayList<>(albumsData.length());

            for (int i = 0; i < albumsData.length(); i++) {
                JSONObject albumData = albumsData.getJSONObject(i);
                String coverPhoto = null;
                JSONObject coverPhotoData = albumData.optJSONObject("cover_photo");
                if (coverPhotoData != null) {
                    coverPhoto = Utils.getFacebookPictureUrl(coverPhotoData.optString(ShareConstants.WEB_DIALOG_PARAM_ID), token);
                }
                mFbAlbums.add(new PhotoAlbum()
                        .setPhotoType(PhotoType.FACEBOOK)
                        .setId(albumData.getLong(ShareConstants.WEB_DIALOG_PARAM_ID))
                        .setName(albumData.getString(ShareConstants.WEB_DIALOG_PARAM_NAME))
                        .setCoverPath(coverPhoto)
                        .setCount(albumData.optInt("count", 0)));
            }
            getMvpView().showAlbums(mFbAlbums);
        }
    }

    private void loadPhotosFromAlbum(@NonNull PhotoAlbum fbAlbum) {
        if (this.mPhotosRequest != null) {
            return;
        }
        if (Utils.isNetworkAvailable(mActivity)) {
            showLoading(mActivity);
            Bundle args = new Bundle();
            args.putString(GraphRequest.FIELDS_PARAM, "id,images");
            args.putInt("limit", fbAlbum.getCount());
            this.mPhotosRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), fbAlbum.getId() + "/photos", args, null, new FacebookPhotosCallback()).executeAsync();
        } else {
            getMvpView().showLoginErrorMessage(mActivity.getString(R.string.label_messageNoInternetError));
        }
    }

    private void onPhotosLoaded(JSONArray photosData) throws JSONException {
        if (mActivity != null && !mActivity.isFinishing()) {
            mPhotos = new ArrayList<>(photosData.length());
            for (int i = 0; i < photosData.length(); i++) {
                JSONObject photoData = photosData.getJSONObject(i);
                JSONArray imagesData = photoData.optJSONArray("images");

                if (imagesData != null) {
                    Photo photo = new Photo()
                            .setPhotoType(PhotoType.FACEBOOK)
                            .setId(photoData.getLong(ShareConstants.WEB_DIALOG_PARAM_ID))
                            .setPhotoPath(imagesData.getJSONObject(0).getString(ShareConstants.FEED_SOURCE_PARAM))
                            .setPhotoHeight(imagesData.getJSONObject(0).getInt("height"))
                            .setPhotoWidth(imagesData.getJSONObject(0).getInt("width"));

                    mPhotos.add(photo);
                }
            }
            getMvpView().showPhotos(mPhotos);
        }
    }

    /**
     * OnPhotoAlbumClickListener
     *
     * @param album - clicked fb album
     */

    @Override
    public void onPhotoAlbumClick(PhotoAlbum album) {
        loadPhotosFromAlbum(album);
    }

    /**
     * OnPhotoClickListener
     *
     * @param position
     * @param photo
     */
    @Override
    public void onPhotoClick(int position, Photo photo) {
        mOnSelectPhotosFragmentsListener.onPhotoClick(photo);
        getMvpView().notifyPhotosAdapter(position);
    }

    /**
     * OnPhotoClickListener
     *
     * @param position
     * @param photo
     * @return
     */
    @Override
    public boolean isSelected(int position, Photo photo) {
        return mOnSelectPhotosFragmentsListener.isSelected(photo);
    }

    public boolean onBackPressed() {
        if (getMvpView().isShowingAlbums()) {
            return false;
        } else {
            getMvpView().showAlbums(mFbAlbums);
            return true;
        }
    }

    private class FacebookPhotosCallback implements GraphRequest.Callback {
        @Override
        public void onCompleted(GraphResponse response) {
            mPhotosRequest = null;
            dismissLoading();
            if (response.getError() == null) {
                try {
                    onPhotosLoaded(response.getJSONObject().getJSONArray(ShareConstants.WEB_DIALOG_PARAM_DATA));
                } catch (JSONException e) {
                    e.printStackTrace();
                    getMvpView().showLoginErrorMessage(mActivity.getString(R.string.message_facebookAlbumsError));
                }
            } else {
                getMvpView().showLoginErrorMessage(mActivity.getString(R.string.message_facebookAlbumsError));
            }
        }
    }

    private class FacebookAlbumsCallback implements GraphRequest.Callback {
        @Override
        public void onCompleted(GraphResponse response) {
            mAlbumsRequest = null;
            dismissLoading();
            if (response.getError() == null) {
                try {
                    onAlbumsLoaded(response.getJSONObject().getJSONArray(ShareConstants.WEB_DIALOG_PARAM_DATA));
                } catch (JSONException e) {
                    e.printStackTrace();
                    getMvpView().showLoginErrorMessage(mActivity.getString(R.string.message_facebookAlbumsError));
                }
            } else {
                getMvpView().showLoginErrorMessage(mActivity.getString(R.string.message_facebookAlbumsError));
            }
        }
    }

    private class FacebookLoginCallback implements FacebookCallback<LoginResult> {
        @Override
        public void onSuccess(LoginResult loginResult) {
            if (loginResult.getAccessToken().getPermissions().contains("user_photos")) {
                loadAlbums();
            }
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException error) {
            if (!mCurrentFragment.getFragmentManager().isDestroyed() && !(error instanceof FacebookOperationCanceledException)) {
                String message;
                if (error instanceof FacebookAuthorizationException) {
                    message = mActivity.getString(R.string.message_facebookLoginFailed);
                } else {
                    message = error.getMessage();
                }
                getMvpView().showLoginErrorMessage(message);
            }
        }
    }

}
