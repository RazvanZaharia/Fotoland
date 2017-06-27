package eu.mobiletouch.fotoland.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterResponse extends BaseResponse implements Serializable {
    private static final long serialVersionUID = -1389732529372910508L;

    @SerializedName("userId")
    private String mUserId;
    @SerializedName("firstname")
    private String mFirstName;
    @SerializedName("lastname")
    private String mLastName;

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }
}
