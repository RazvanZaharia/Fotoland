package eu.mobiletouch.fotoland;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created on 28-Aug-16.
 */
public class ApplicationFotoland extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
