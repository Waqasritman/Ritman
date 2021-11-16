package angoothape.wallet.di.JSONdi.restRequest;

public class Credentials {

    static {
        System.loadLibrary("native-lib");
    }

    public static native String getPartnerCode();

    public static native String getUserName();

    public static native String getPassword();


    public Integer PartnerCode = Integer.parseInt(getPartnerCode());
    public String UserName = getUserName();
    public String UserPassword = getPassword();
    public Integer LanguageID = 1;
}
