package eu.mobiletouch.fotoland.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import eu.mobiletouch.fotoland.fragments.FragmentPhotobookPage;
import eu.mobiletouch.fotoland.holders.localPhotos.Photo;

/**
 * Created on 31-Oct-16.
 */
public class AdapterVpEditPage extends FragmentStatePagerAdapter {

    private ArrayList<Photo> mDataSet;

    public AdapterVpEditPage(@NonNull ArrayList<Photo> dataSet, FragmentManager fm) {
        super(fm);
        this.mDataSet = dataSet;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentPhotobookPage.newInstance(mDataSet.get(position));
    }

    @Override
    public int getCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }
}
