package eu.mobiletouch.fotoland.activities;

import android.content.Intent;
import android.os.Handler;

import java.io.IOException;

import butterknife.Bind;
import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.x_base.BaseActivity;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created on 10-Aug-16.
 */
public class ActivitySplashscreen extends BaseActivity {

    @Bind(R.id.gif_anim)
    GifImageView mGifImageView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splashscreen;
    }

    @Override
    protected void init() {
        super.init();
        GifDrawable gifDrawable = null;
        try {
            gifDrawable = new GifDrawable(getResources(), R.drawable.splash_animation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (gifDrawable != null) {
            gifDrawable.setLoopCount(1);
            gifDrawable.setSpeed(1.0f);
            mGifImageView.setImageDrawable(gifDrawable);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openNextActivity();
            }
        }, gifDrawable != null ? (gifDrawable.getDuration() + 500) : 1000);

    }

    private void openNextActivity() {
        startActivity(new Intent(this, ActivitySelectProduct.class));
        this.finish();
    }

}
