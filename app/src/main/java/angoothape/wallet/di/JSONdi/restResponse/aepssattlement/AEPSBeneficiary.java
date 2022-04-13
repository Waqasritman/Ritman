package angoothape.wallet.di.JSONdi.restResponse.aepssattlement;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AEPSBeneficiary implements Parcelable {
    @SerializedName("Beneficiary_ID")
    public int id;
    @SerializedName("Beneficiary_Name")
    public String beneficiaryName;
    @SerializedName("Beneficiary_Account_No")
    public String beneficiaryAccountNo;
    @SerializedName("Beneficiary_IFSC_Code")
    public String IFSCCode;

    protected AEPSBeneficiary(Parcel in) {
        id = in.readInt();
        beneficiaryName = in.readString();
        beneficiaryAccountNo = in.readString();
        IFSCCode = in.readString();
    }

    public static final Creator<AEPSBeneficiary> CREATOR = new Creator<AEPSBeneficiary>() {
        @Override
        public AEPSBeneficiary createFromParcel(Parcel in) {
            return new AEPSBeneficiary(in);
        }

        @Override
        public AEPSBeneficiary[] newArray(int size) {
            return new AEPSBeneficiary[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(beneficiaryName);
        dest.writeString(beneficiaryAccountNo);
        dest.writeString(IFSCCode);
    }
}
