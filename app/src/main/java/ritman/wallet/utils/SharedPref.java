package ritman.wallet.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    public static String SharedPref = "SharedPref";
    public static String otpId = "otpId";
    public static String benename = "benename";
    public static String count = "count";
    public static String cart_id = "cart_id";
    public static String name = "name";
    public static String address_id = "address_id";
    public static String beneficiary_id = "beneficiary_id";
    public static String mobileno = "mobileno";
    public static String emailid = "emailid";
    static Context context;

    public SharedPref(Context context) {
        this.context = context;
    }

    public static void putBol(Context context , String key, boolean val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    public static boolean getBol(Context context , String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static String getotpId(Context context , String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void putobenename(Context context, String key, String val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public static String getbenename(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SharedPref,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public static void putuserId(Context context, String key, String val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public static String getName(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SharedPref,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public static void putName(Context context, String key, String val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.commit();
    }
    
    public static String getAddressId(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SharedPref,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
    
    public static void putAddressId(Context context, String key, String val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.commit();
    }
    
    public static String getBeneficiaryId(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SharedPref,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
    public static void putBeneficiaryId(Context context, String key, String val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.commit();
    }
    
    public static String getMobileno(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SharedPref,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
    
    public static void putMobileno(Context context, String key, String val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.commit();
    }
    
    public static String getEmailid(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SharedPref,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
    
    public static void putEmailid(Context context, String key, String val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.commit();
    }
    public static void clearData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


}
