package eu.mobiletouch.fotoland.x_base;

import android.content.Context;
import android.support.v4.util.LruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import eu.mobiletouch.fotoland.utils.Utils;

public class BaseDao {
    public enum StorageMethod {
        SharedPreferences, InternalFiles, ExternalFiles
    }

    private static final String SHARED_PREFS_FILE = "shared_prefs";

    protected static LruCache<String, Serializable> heapCache;

    protected Context context;
    private int heapCacheSize = 128;

    public BaseDao(Context context) {
        this.context = context;
    }

    protected Serializable read(StorageMethod storageMethod, String... keys) {
        return read(storageMethod, null, keys);
    }

    protected Serializable read(StorageMethod storageMethod, Serializable defaultValue,
                                String... keys) {
        String path = getPathFromKeys(keys);
        Serializable data = readHeapCache(path);
        if (data == null) {
            switch (storageMethod) {
                case SharedPreferences:
                    data = readSharedPrefs(path);
                    break;
                case InternalFiles:
                case ExternalFiles:
                    data = readFileSystem(storageMethod, path);
                    break;
            }
            writeHeapCache(path, data);
        }
        return data != null ? data : defaultValue;
    }

    protected void write(StorageMethod storageMethod, Serializable data, String... keys) {
        String path = getPathFromKeys(keys);
        switch (storageMethod) {
            case SharedPreferences:
                writeSharedPrefs(path, data);
                break;
            case InternalFiles:
            case ExternalFiles:
                writeFileSystem(storageMethod, path, data);
                break;
        }
        writeHeapCache(path, data);
    }

    private String getPathFromKeys(String... keys) {
        String path = "";
        for (String key : keys)
            path += File.separator + key;
        return path.substring(1);
    }

    private Serializable readHeapCache(String path) {
        if (!checkHeapCache()) {
            return null;
        }
        ArrayList<Serializable> returnValues = new ArrayList<Serializable>();
        for (String key : heapCache.snapshot().keySet())
            if (key.equals(path) || key.startsWith(path + File.separator)) {
                returnValues.add(heapCache.get(key));
            }
        return returnValues.size() > 1 ? returnValues : !returnValues.isEmpty() ? returnValues.get(
                0) : null;
    }

    private void writeHeapCache(String path, Serializable data) {
        if (!checkHeapCache()) {
            return;
        }
        if (data != null) {
            heapCache.put(path, data);
        } else {
            heapCache.remove(path);
        }
    }

    private boolean checkHeapCache() {
        if (heapCacheSize <= 0) {
            return false;
        }
        if (heapCache == null) {
            heapCache = new LruCache<String, Serializable>(heapCacheSize);
        }
        return true;
    }

    public void setHeapCacheSize(int heapCacheSize) {
        this.heapCacheSize = heapCacheSize;
    }

    private Serializable readSharedPrefs(String path) {
        ArrayList<Serializable> returnValues = new ArrayList<Serializable>();
        Map<String, ?> sharedPrefsValues = context.getSharedPreferences(SHARED_PREFS_FILE,
                Context.MODE_PRIVATE)
                .getAll();
        for (String key : sharedPrefsValues.keySet())
            if (key.equals(path) || key.startsWith(path + File.separator)) {
                returnValues.add((Serializable) sharedPrefsValues.get(key));
            }
        return returnValues.size() > 1 ? returnValues : !returnValues.isEmpty() ? returnValues.get(
                0) : null;
    }

    private void writeSharedPrefs(String path, Serializable data) {
        if (data != null) {
            if (data instanceof Boolean) {
                context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean(path, (Boolean) data)
                        .commit();
            } else if (data instanceof Integer) {
                context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE).edit().putInt(
                        path, (Integer) data).commit();
            } else if (data instanceof Long) {
                context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                        .edit()
                        .putLong(path, (Long) data)
                        .commit();
            } else if (data instanceof Float) {
                context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                        .edit()
                        .putFloat(path, (Float) data)
                        .commit();
            } else {
                context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                        .edit()
                        .putString(path, data.toString())
                        .commit();
            }
        } else {
            context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE).edit().remove(
                    path).commit();
        }
    }

    private Serializable readFileSystem(StorageMethod storageMethod, String path) {
        File file = getFileFromPath(storageMethod, path);
        return file.isFile() ? readFile(file) : readDirectory(file);
    }

    private Serializable readFile(File file) {
        ObjectInputStream ois = null;
        Serializable fileData = null;
        try {
            ois = new ObjectInputStream(new BufferedInputStream(
                    new FileInputStream(file)));
            fileData = (Serializable) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            Utils.closeSilently(ois);
        }
        return fileData;
    }

    private ArrayList<Serializable> readDirectory(File dir) {
        try {
            ArrayList<Serializable> dirData = new ArrayList<Serializable>();
            for (File file : dir.listFiles())
                if (file.isFile()) {
                    dirData.add(readFile(file));
                } else {
                    dirData.addAll(readDirectory(file));
                }
            return dirData;
        } catch (NullPointerException e) {
            return null;
        }
    }

    private void writeFileSystem(StorageMethod storageMethod, String path, Serializable data) {
        ObjectOutputStream oos = null;
        try {
            File file = getFileFromPath(storageMethod, path);
            if (data != null) {
                oos = new ObjectOutputStream(new BufferedOutputStream(
                        new FileOutputStream(file)));
                oos.writeObject(data);
            } else {
                file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.closeSilently(oos);
        }
    }

    private File getFileFromPath(StorageMethod storageMethod, String path) {
        return new File(
                (storageMethod == StorageMethod.InternalFiles ? context.getFilesDir() : context.getExternalFilesDir(null)).getAbsolutePath() + File.separator + path);
    }
}