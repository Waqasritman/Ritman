package angoothape.wallet.di.JSONdi.restRequest;

import android.os.Parcel;
import android.os.Parcelable;

public class Cred implements Parcelable {
    public Integer LanguageID = 1;

    protected Cred(Parcel in) {
        if (in.readByte() == 0) {
            LanguageID = null;
        } else {
            LanguageID = in.readInt();
        }
    }

    public Cred() {
    }

    public static final Creator<Cred> CREATOR = new Creator<Cred>() {
        @Override
        public Cred createFromParcel(Parcel in) {
            return new Cred(in);
        }

        @Override
        public Cred[] newArray(int size) {
            return new Cred[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (LanguageID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(LanguageID);
        }
    }
}
