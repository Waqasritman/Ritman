package angoothape.wallet.di.JSONdi.restRequest.aepsSettlement;

import android.os.Parcel;
import android.os.Parcelable;

import angoothape.wallet.di.JSONdi.restRequest.Cred;

public class AEPSSettlementCommission implements Parcelable {
    Cred Credentials = new Cred();
    public String Transfer_Amount = "";
    public String Payment_Id = "";

    public AEPSSettlementCommission() {
    }

    protected AEPSSettlementCommission(Parcel in) {
        Credentials = in.readParcelable(Cred.class.getClassLoader());
        Transfer_Amount = in.readString();
        Payment_Id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Credentials, flags);
        dest.writeString(Transfer_Amount);
        dest.writeString(Payment_Id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AEPSSettlementCommission> CREATOR = new Creator<AEPSSettlementCommission>() {
        @Override
        public AEPSSettlementCommission createFromParcel(Parcel in) {
            return new AEPSSettlementCommission(in);
        }

        @Override
        public AEPSSettlementCommission[] newArray(int size) {
            return new AEPSSettlementCommission[size];
        }
    };
}
