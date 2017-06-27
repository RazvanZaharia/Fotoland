package eu.mobiletouch.fotoland.requests;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;

import eu.mobiletouch.fotoland.responses.RegisterResponse;
import eu.mobiletouch.fotoland.x_base.BaseRequest;
import eu.mobiletouch.fotoland.x_base.BaseRunnableRequest;

public class RegisterRequest extends BaseRunnableRequest<RegisterResponse> {
    private static String ACTION = "registerUser";

    public RegisterRequest(@NonNull Context ctx,
                           @NonNull String email,
                           @NonNull String password,
                           @NonNull String firstName,
                           @NonNull String lastName,
                           @NonNull BaseRequest.Callback<RegisterResponse> callback) {

        request = new BaseRequest.Builder<>(ctx, "", this)
                .method(Request.Method.POST)
                .paramNames(new String[]{"action", "email", "password", "firstName", "lastName"})
                .paramValues(new Object[]{ACTION, email, password, firstName, lastName})
                .callback(callback)
                .build();
    }
}
