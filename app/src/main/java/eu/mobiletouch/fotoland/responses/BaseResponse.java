package eu.mobiletouch.fotoland.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 7430291019105163242L;

    @SerializedName("for")
    private String mFor;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("errorMessage")
    private String mErrorMessage;
    @SerializedName("code")
    private String mCode;
    @SerializedName("ts")
    private String mServerTimeInSeconds;

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public String getServerTimeInSeconds() {
        return mServerTimeInSeconds;
    }

    public void setServerTimeInSeconds(String serverTimeInSeconds) {
        mServerTimeInSeconds = serverTimeInSeconds;
    }

    public String getFor() {
        return mFor;
    }

    public void setFor(String aFor) {
        mFor = aFor;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }
}
