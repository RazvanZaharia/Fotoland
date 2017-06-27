package eu.mobiletouch.fotoland.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.x_base.BaseActivity;

/**
 * Created on 11-Feb-17.
 */
public class ActivityGoToAccount extends BaseActivity {

    @Bind(R.id.btn_sign_in)
    Button mBtnSignIn;
    @Bind(R.id.btn_create_account)
    Button mBtnCreateAccount;
    @Bind(R.id.btn_skip)
    Button mSkip;

    public static void launch(@NonNull Context ctx) {
        Intent goToAccountIntent = new Intent(ctx, ActivityGoToAccount.class);
        ctx.startActivity(goToAccountIntent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_go_to_account;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        mSkip.setVisibility(View.GONE);
    }

    @Override
    protected void setActions() {
        super.setActions();
        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignInAccountScreen();
            }
        });
        mBtnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAccountScreen();
            }
        });
        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void openSignInAccountScreen() {
        ActivityLogin.launch(this);
    }

    private void openCreateAccountScreen() {
        ActivityRegister.launch(this);
    }

    @Override
    public void onBackPressed() {
        ActivitySelectProduct.launch(this);
        super.onBackPressed();
    }
}
