package eu.mobiletouch.fotoland.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.fragments.FragmentFacebookPhotos;
import eu.mobiletouch.fotoland.fragments.FragmentLocalPhotos;

/**
 * Created on 27-Aug-16.
 */
public class AdapterVpPhotos extends FragmentStatePagerAdapter {

    private Context mCtx;
    private HashMap<Integer, Fragment> mFragments;

    public AdapterVpPhotos(@NonNull Context ctx, @NonNull FragmentManager fm) {
        super(fm);
        this.mCtx = ctx;
        mFragments = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = FragmentLocalPhotos.newInstance();
                break;
            case 1:
                fragment = FragmentFacebookPhotos.newInstance();
                break;
        }
        mFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mFragments.remove(position);
    }

    public Fragment getFragmentAt(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mCtx.getString(R.string.title_tabPhotos_phone);
            case 1:
            default:
                return mCtx.getString(R.string.title_tabPhotos_facebook);
        }
    }
}
