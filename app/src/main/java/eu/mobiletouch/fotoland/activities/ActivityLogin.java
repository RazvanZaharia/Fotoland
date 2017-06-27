package eu.mobiletouch.fotoland.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.requests.LoginRequest;
import eu.mobiletouch.fotoland.responses.LoginResponse;
import eu.mobiletouch.fotoland.x_base.BaseRequest;
import eu.mobiletouch.fotoland.x_base.BaseToolbarActivity;

public class ActivityLogin extends BaseToolbarActivity {

    @Bind(R.id.et_email)
    EditText etEmail;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.txt_forgot_password)
    TextView tvForgotPassword;
    @Bind(R.id.btn_authenticate)
    Button btnLogin;
    @Bind(R.id.btn_new_user)
    Button btnRegister;

    public static void launch(@NonNull Context ctx) {
        Intent signInIntent = new Intent(ctx, ActivityLogin.class);
        ctx.startActivity(signInIntent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.screenTitle_login);
    }

    @Override
    protected boolean showCartIcon() {
        return false;
    }

    @Override
    protected void setActions() {
        super.setActions();
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginUser();
                    return true;
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivityRegister.class));
            }
        });
    }

    private void loginUser() {
        final String userEmail = etEmail.getText().toString().trim();
        final String userPassword = etPassword.getText().toString().trim();
        final BaseRequest.Callback<LoginResponse> callback = new BaseRequest.Callback<LoginResponse>() {
            @Override
            public void onResponse(Context context, LoginResponse response) {

            }

            @Override
            public void onError(Context context, Exception exception) {
            }

        };
        new LoginRequest(ActivityLogin.this, userEmail, userPassword, null, callback).run();
    }

}
