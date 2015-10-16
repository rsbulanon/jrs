package dynobjx.com.jrs.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rsbulanon on 5/22/15.
 */
public class Prefs {

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("myprefs", 0);
    }

    public static String getMyStringPrefs(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static int getMyIntPref(Context context, String key) {
        return getPrefs(context).getInt(key, 0);
    }

    public static float getMyFloatPref(Context context, String key) {
        return getPrefs(context).getFloat(key, 0f);
    }

    public static boolean getMyBooleanPref(Context context, String key) {
        return getPrefs(context).getBoolean(key, false);
    }

    public static void setMyBooleanPref(Context context, String key,
                                        boolean value) {
        getPrefs(context).edit().putBoolean(key, value).commit();
    }

    public static void setMyStringPref(Context context, String key, String value) {
            getPrefs(context).edit().putString(key, value).commit();
    }

    public static void setMyIntPref(Context context, String key, int value) {
        getPrefs(context).edit().putInt(key, value).commit();
    }

    public static void setMyFloatPref(Context context, String key, Float value) {
        getPrefs(context).edit().putFloat(key, value).commit();
    }

    public static void removePref(Context context, String key){
        getPrefs(context).edit().remove(key);
    }
}
