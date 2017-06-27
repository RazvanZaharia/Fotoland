package eu.mobiletouch.fotoland.requests;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Request;

import eu.mobiletouch.fotoland.responses.LoginResponse;
import eu.mobiletouch.fotoland.x_base.BaseRequest;
import eu.mobiletouch.fotoland.x_base.BaseRunnableRequest;

public class LoginRequest extends BaseRunnableRequest<LoginResponse> {
    private static String ACTION = "loginUser";

    public LoginRequest(@NonNull Context ctx,
                        @Nullable String email,
                        @Nullable String password,
                        @Nullable String socialId,
                        @NonNull BaseRequest.Callback<LoginResponse> callback) {

        request = new BaseRequest.Builder<>(ctx, "", this)
                .method(Request.Method.POST)
                .paramNames(new String[]{"action", "email", "password", "socialId"})
                .paramValues(new Object[]{ACTION, email, password, socialId})
                .callback(callback)
                .build();
    }

}
