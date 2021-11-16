package angoothape.wallet.di.JSONdi.restResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class VerifyBeneficiary extends ApiResponse<VerifyBeneficiary> implements Parcelable {
    @SerializedName("TransactionNumber")
    public String transactionNumber;
    @SerializedName("CustomerNo")
    public String customerNo;
    @SerializedName("BeneficiaryNo")
    public String beneficiaryNo;

    @SerializedName("Beneficiary_data")
    public BeneficiaryData beneficiaryData;


    public static class BeneficiaryData implements Parcelable {
        @SerializedName("beneficiaryDetails")
        public BeneficiaryDetails details;

        protected BeneficiaryData(Parcel in) {
            details = in.readParcelable(BeneficiaryDetails.class.getClassLoader());
        }

        public static final Creator<BeneficiaryData> CREATOR = new Creator<BeneficiaryData>() {
            @Override
            public BeneficiaryData createFromParcel(Parcel in) {
                return new BeneficiaryData(in);
            }

            @Override
            public BeneficiaryData[] newArray(int size) {
                return new BeneficiaryData[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(details, flags);
        }
    }


    public static class BeneficiaryDetails implements Parcelable {
        @SerializedName("beneficiaryId")
        public String _id;
        @SerializedName("name")
        public String name;
        @SerializedName("bankAccountNumber")
        public String bankAccountNumber;
        @SerializedName("ifscCode")
        public String ifscCode;
        @SerializedName("bankName")
        public String bankName;
        @SerializedName("benefMobileNumber")
        public String mobileNumber;

        protected BeneficiaryDetails(Parcel in) {
            _id = in.readString();
            name = in.readString();
            bankAccountNumber = in.readString();
            ifscCode = in.readString();
            bankName = in.readString();
            mobileNumber = in.readString();
        }

        public static final Creator<BeneficiaryDetails> CREATOR = new Creator<BeneficiaryDetails>() {
            @Override
            public BeneficiaryDetails createFromParcel(Parcel in) {
                return new BeneficiaryDetails(in);
            }

            @Override
            public BeneficiaryDetails[] newArray(int size) {
                return new BeneficiaryDetails[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(_id);
            dest.writeString(name);
            dest.writeString(bankAccountNumber);
            dest.writeString(ifscCode);
            dest.writeString(bankName);
            dest.writeString(mobileNumber);
        }
    }

    public VerifyBeneficiary() {
    }

    protected VerifyBeneficiary(Parcel in) {
        transactionNumber = in.readString();
        customerNo = in.readString();
        beneficiaryNo = in.readString();
        data = in.readParcelable(BeneficiaryDetails.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(transactionNumber);
        dest.writeString(customerNo);
        dest.writeString(beneficiaryNo);
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VerifyBeneficiary> CREATOR = new Creator<VerifyBeneficiary>() {
        @Override
        public VerifyBeneficiary createFromParcel(Parcel in) {
            return new VerifyBeneficiary(in);
        }

        @Override
        public VerifyBeneficiary[] newArray(int size) {
            return new VerifyBeneficiary[size];
        }
    };
}
