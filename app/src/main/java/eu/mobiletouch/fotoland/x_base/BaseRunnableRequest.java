package eu.mobiletouch.fotoland.x_base;

public class BaseRunnableRequest<T> implements Runnable {
    protected BaseRequest<T> request;

    @Override
    public void run() {
        if (request != null) {
            request.run();
        }
    }

    public void cancel() {
        request.cancel();
    }

}
