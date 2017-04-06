package eu.mobiletouch.fotoland.presenters;

import eu.mobiletouch.fotoland.mvps.BaseMvp;

public interface Presenter<V extends BaseMvp> {

    void attachView(V mvpView);

    void detachView();
}
