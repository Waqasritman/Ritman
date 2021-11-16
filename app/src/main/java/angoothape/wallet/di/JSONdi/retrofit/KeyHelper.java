package angoothape.wallet.di.JSONdi.retrofit;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

import angoothape.wallet.di.AESHelper;

public class KeyHelper {
    static {
        System.loadLibrary("native-lib");
    }

    public static String key = "";

    public static native String getKey();

    public static String getKey(String text) {
        if (key.isEmpty()) {
            return getBase(text);
        } else {
            return key;
        }

    }

    public static String getSKey(String key) {
        return AESHelper.encrypt(getKey()
                , key.trim());
    }


    public static String getBase(String text) {
        byte[] data = new byte[0];
        try {
            data = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }
}
