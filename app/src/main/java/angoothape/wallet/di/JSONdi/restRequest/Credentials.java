package angoothape.wallet.di.JSONdi.restRequest;

import android.os.Parcel;
import android.os.Parcelable;

public class Credentials implements Parcelable {

    static {
        System.loadLibrary("native-lib");
    }

    public Credentials() {

    }

    protected Credentials(Parcel in) {
        if (in.readByte() == 0) {
            PartnerCode = null;
        } else {
            PartnerCode = in.readInt();
        }
        UserName = in.readString();
        UserPassword = in.readString();
        if (in.readByte() == 0) {
            LanguageID = null;
        } else {
            LanguageID = in.readInt();
        }
    }

    public static final Creator<Credentials> CREATOR = new Creator<Credentials>() {
        @Override
        public Credentials createFromParcel(Parcel in) {
            return new Credentials(in);
        }

        @Override
        public Credentials[] newArray(int size) {
            return new Credentials[size];
        }
    };

    public static native String getPartnerCode();

    public static native String getUserName();

    public static native String getPassword();


    public Integer PartnerCode = Integer.parseInt(getPartnerCode());
    public String UserName = getUserName();
    public String UserPassword = getPassword();
    public Integer LanguageID = 1;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (PartnerCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(PartnerCode);
        }
        dest.writeString(UserName);
        dest.writeString(UserPassword);
        if (LanguageID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(LanguageID);
        }
    }

    @Override
    public String toString() {
        return "{" +
                "PartnerCode=" + PartnerCode +
                ", UserName='" + UserName + '\'' +
                ", UserPassword='" + UserPassword + '\'' +
                ", LanguageID=" + LanguageID +
                '}';
    }
}
