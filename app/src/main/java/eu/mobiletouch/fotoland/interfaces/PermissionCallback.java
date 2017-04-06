package eu.mobiletouch.fotoland.interfaces;

public interface PermissionCallback {

    void onPermissionDenied(final int permissionCode);

    void onPermissionGranted(final int permissionCode);

    void onPermissionGranted(final int permissionCode, final Object data);

}
