package org.tiger.practice.testlibmod;

/**
 * Created by tiger on 2018/1/15.
 */


/**
 * override android.util.Log for control level
 */
public final class Log {
    private static int LEVEL = BuildConfig.DEBUG ? android.util.Log.DEBUG : android.util.Log.WARN;

    public static void setLevel(int level) {
        if (level >= android.util.Log.VERBOSE && level <= android.util.Log.ASSERT) {
            LEVEL = level;
        }
    }

    public static int getLevel() {
        return LEVEL;
    }

    public static int v(String tag, String msg) {
        if (android.util.Log.VERBOSE < LEVEL) {
            return 0;
        } else {
            return android.util.Log.v(tag, msg);
        }
    }

    public static int d(String tag, String msg) {
        if (android.util.Log.DEBUG < LEVEL) {
            return 0;
        } else {
            return android.util.Log.v(tag, msg);
        }
    }

    public static int i(String tag, String msg) {
        if (android.util.Log.INFO < LEVEL) {
            return 0;
        } else {
            return android.util.Log.v(tag, msg);
        }
    }

    public static int w(String tag, String msg) {
        if (android.util.Log.WARN < LEVEL) {
            return 0;
        } else {
            return android.util.Log.v(tag, msg);
        }
    }

    public static int e(String tag, String msg) {
        if (android.util.Log.ERROR < LEVEL) {
            return 0;
        } else {
            return android.util.Log.v(tag, msg);
        }
    }
}
