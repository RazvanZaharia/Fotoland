package eu.mobiletouch.fotoland.utils;

/**
 * Created on 20-Sep-16.
 */
public class ObjectsUtils {
    /**
     * Null-safe equivalent of {@code a.equals(b)}.
     */
    public static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
