package eu.mobiletouch.fotoland.dao;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import eu.mobiletouch.fotoland.holders.UserSelections;
import eu.mobiletouch.fotoland.x_base.BaseDao;

public class Dao extends BaseDao {
    private static final StorageMethod STORAGE_METHOD = StorageMethod.InternalFiles;

    private static final int NO_LIMIT = 0;
    private static final String CART_PRODUCTS = "cartProducts";

    private static Dao instance;

    private Dao(Context context) {
        super(context);
    }

    public static Dao get(Context context) {
        if (instance == null) {
            instance = new Dao(context.getApplicationContext());
        }
        return instance;
    }

    /*public void destroyHeapCache() {
        heapCache = null;
    }

    public static void destroy() {
        if (instance != null) {
            instance.destroyHeapCache();
            instance = null;
        }
    }*/

    public void deleteCartProduct(UserSelections userSelections) {
        deleteSingleItem(CART_PRODUCTS, userSelections);
    }

    public void deleteAllCartProducts() {
        deleteAllItems(CART_PRODUCTS);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<UserSelections> readAllCartProducts() {
        return (ArrayList<UserSelections>) read(STORAGE_METHOD, new ArrayList<UserSelections>(), CART_PRODUCTS);
    }

    public void saveOneCartProduct(UserSelections userSelections) {
        if (userSelections != null) {
            writeSingleItem(CART_PRODUCTS, userSelections, NO_LIMIT);
        }
    }

    private <T> void deleteAllItems(String itemType) {
        write(STORAGE_METHOD, new ArrayList<T>(), itemType);
    }

    private <T> void deleteSingleItem(String itemType, int index) {
        @SuppressWarnings("unchecked")
        ArrayList<T> items = (ArrayList<T>) read(STORAGE_METHOD, itemType);
        items.remove(index);
        write(STORAGE_METHOD, items, itemType);
    }

    private <T> void deleteSingleItem(String itemType, T item) {
        @SuppressWarnings("unchecked")
        ArrayList<T> items = (ArrayList<T>) read(STORAGE_METHOD, itemType);
        items.remove(item);
        write(STORAGE_METHOD, items, itemType);
    }

    private int existsInArray(UserSelections item, ArrayList<UserSelections> items) {
        if (item == null || items == null || items.size() == 0) {
            return -1;
        }
        for (int i = 0; i < items.size(); i++) {
            UserSelections userSelections = (UserSelections) items.get(i);
            if (userSelections == null) {
                continue;
            } else {
                if (userSelections.equals(item)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private <T> int indexOf(T item, ArrayList<T> items) {
        if (item == null || items == null || items.size() == 0) {
            return -1;
        }

        for (int i = 0; i < items.size(); i++) {
            if (item.equals(items.get(i))) {
                return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    private <T> boolean writeSingleItem(String itemType, T item, int limit) {
        boolean existing = false;
        ArrayList<T> items = (ArrayList<T>) read(STORAGE_METHOD, new ArrayList<T>(), itemType);
        int index;

        if (item instanceof UserSelections) {
            index = existsInArray((UserSelections)item, (ArrayList<UserSelections>)items);
        } else {
            index = items.indexOf(item);
        }

        if (limit == NO_LIMIT) {
            if (index != -1) {
                existing = true;
                items.set(index, item);
            } else {
                items.add(0, item);
            }
        } else {
            if (index != -1 && index < items.size()) {
                existing = true;
                items.remove(index);
                items.add(0, item);
            } else {
                if (items.size() >= limit) {
                    items.remove(0);
                    items.add(0, item);
                } else {
                    items.add(0, item);
                }
            }
        }

        write(STORAGE_METHOD, items, itemType);
        return existing;
    }

    public void writeFlag(final boolean flag, @NonNull final String key) {
        write(STORAGE_METHOD, flag, key);
    }

    public boolean getFlag(@NonNull final String key, final boolean defaultValue) {
        Serializable object = read(STORAGE_METHOD, key);
        boolean result = defaultValue;
        if (object != null && object instanceof Boolean) {
            result = (boolean) object;
        }
        return result;
    }
}
