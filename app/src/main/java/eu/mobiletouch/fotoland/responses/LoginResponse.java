package eu.mobiletouch.fotoland.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created on 31-Jan-17.
 */
public class LoginResponse extends BaseResponse implements Serializable {
    private static final long serialVersionUID = -1389732529372910508L;

    @SerializedName("userId")
    private String mUserId;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }
}
