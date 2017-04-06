package eu.mobiletouch.fotoland.x_base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    private String LOG_TAG = BaseFragment.class.getSimpleName();

    protected BaseActivity activityFragments;

    public BaseFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutResId() != View.NO_ID ? inflater.inflate(getLayoutResId(), container, false) : null;
        ButterKnife.bind(this, view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof BaseActivity) {
            activityFragments = (BaseActivity) getActivity();
        }
        init();
        setData();
        setActions();
    }


    abstract protected int getLayoutResId();

    protected void init() {
    }

    protected void setData() {
    }

    protected void setActions() {
    }

    public void remove() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }

        try {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getSupportFragmentManager().popBackStack();
            else
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}