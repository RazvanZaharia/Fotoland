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
import eu.mobiletouch.fotoland.requests.RegisterRequest;
import eu.mobiletouch.fotoland.responses.RegisterResponse;
import eu.mobiletouch.fotoland.x_base.BaseRequest;
import eu.mobiletouch.fotoland.x_base.BaseToolbarActivity;

public class ActivityRegister extends BaseToolbarActivity {

    @Bind(R.id.et_email)
    EditText etEmail;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_firstname)
    EditText etFirstname;
    @Bind(R.id.et_lastname)
    EditText etLastname;
    @Bind(R.id.btn_register)
    Button btnRegister;

    public static void launch(@NonNull Context ctx) {
        Intent registerIntent = new Intent(ctx, ActivityRegister.class);
        ctx.startActivity(registerIntent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.screenTitle_register);
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
                    registerUser();
                    return true;
                }
                return false;
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String userEmail = etEmail.getText().toString().trim();
        final String userPassword = etPassword.getText().toString().trim();
        final String userFirstname = etFirstname.getText().toString().trim();
        final String userLastname = etLastname.getText().toString().trim();
        final BaseRequest.Callback<RegisterResponse> callback = new BaseRequest.Callback<RegisterResponse>() {
            @Override
            public void onResponse(Context context, RegisterResponse response) {
                if(response != null) {

                }
            }

            @Override
            public void onError(Context context, Exception exception) {
            }

        };
        new RegisterRequest(ActivityRegister.this, userEmail, userPassword, userFirstname, userLastname, callback).run();
    }

}
