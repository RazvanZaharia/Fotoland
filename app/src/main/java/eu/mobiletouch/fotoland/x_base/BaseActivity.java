package eu.mobiletouch.fotoland.x_base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created on 09-Aug-16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected View mContentView;
    protected LayoutInflater mLayoutInflater;

    abstract protected int getLayoutResId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLayoutResId() == View.NO_ID) {
            return;
        }

        mLayoutInflater = getLayoutInflater();
        mContentView = mLayoutInflater.inflate(getLayoutResId(), null);

        setContentView(mContentView);

        linkUI();
        init();
        setData();
        setActions();
    }

    protected void linkUI() {
        ButterKnife.bind(this);
    }

    protected void init() {
    }

    protected void setData() {
    }

    protected void setActions() {
    }

}
