package angoothape.wallet.di.JSONdi.restRequest;

import android.os.Parcel;
import android.os.Parcelable;

public class RegisterBeneficiaryRequest implements Parcelable {
    public Cred Credentials = new Cred();
    public String CustomerNo = "";
    public String title = "";
    public String FirstName = "";
    public String Address = "";
    public String Telephone = "";
    public String PaymentMode = "";
    public String BankName = "";
    public String BranchNameAndAddress = "";
    public String BankCode = "";
    public String AccountNumber = "";
    public String pincode = "";
    public String otp = "";
    public int PurposeCode = 0;
    public String CustomerRelation = "";
    public String BankBranch = "";


    public RegisterBeneficiaryRequest() {
    }

    protected RegisterBeneficiaryRequest(Parcel in) {
        CustomerNo = in.readString();
        title = in.readString();
        FirstName = in.readString();
        Address = in.readString();
        Telephone = in.readString();
        PaymentMode = in.readString();
        BankName = in.readString();
        BranchNameAndAddress = in.readString();
        BankCode = in.readString();
        AccountNumber = in.readString();
        pincode = in.readString();
        otp = in.readString();
        PurposeCode = in.readInt();
        CustomerRelation = in.readString();
        BankBranch = in.readString();
    }

    public static final Creator<RegisterBeneficiaryRequest> CREATOR = new Creator<RegisterBeneficiaryRequest>() {
        @Override
        public RegisterBeneficiaryRequest createFromParcel(Parcel in) {
            return new RegisterBeneficiaryRequest(in);
        }

        @Override
        public RegisterBeneficiaryRequest[] newArray(int size) {
            return new RegisterBeneficiaryRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CustomerNo);
        dest.writeString(title);
        dest.writeString(FirstName);
        dest.writeString(Address);
        dest.writeString(Telephone);
        dest.writeString(PaymentMode);
        dest.writeString(BankName);
        dest.writeString(BranchNameAndAddress);
        dest.writeString(BankCode);
        dest.writeString(AccountNumber);
        dest.writeString(pincode);
        dest.writeString(otp);
        dest.writeInt(PurposeCode);
        dest.writeString(CustomerRelation);
        dest.writeString(BankBranch);
    }
}
