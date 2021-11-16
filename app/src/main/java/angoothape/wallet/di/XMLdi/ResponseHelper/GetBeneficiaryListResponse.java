package angoothape.wallet.di.XMLdi.ResponseHelper;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class GetBeneficiaryListResponse extends ApiResponse<List<GetBeneficiaryListResponse>> implements Parcelable {

    @SerializedName("IsHoldForOFAC")
    public boolean isHoldForOfAC;
    @SerializedName("BeneficiaryNo")
    public String beneficiaryNumber;
    @SerializedName("CustomerNo")
    public String customerNo;
    @SerializedName("FirstName")
    public String firstName;
    @SerializedName("MiddleName")
    public String middleName;
    @SerializedName("LastName")
    public String lastName;
    @SerializedName("NickName")
    public String nickName;
    @SerializedName("Address")
    public String address;
    @SerializedName("DateOfBirth")
    public String dateOfBirth;
    @SerializedName("Telephone")
    public String telephone;
    @SerializedName("Nationality")
    public String nationality;

    @SerializedName("PayOutCurrency")
    public String payOutCurrency;
    @SerializedName("PaymentMode")
    public String paymentMode;
    @SerializedName("PaymentBranchCode")
    public String paymentBranchCode;
    @SerializedName("BankName")
    public String bankName;
    @SerializedName("BankCountry")
    public String bankCountry;
    @SerializedName("BranchName")
    public String branchName;
    @SerializedName("BankCode")
    public String bankCode;
    @SerializedName("AccountNumber")
    public String accountNumber;
    @SerializedName("IsActive")
    public String isActive;
    @SerializedName("PurposeCode")
    public String purposeCode;
    @SerializedName("PurposeOfTransfer")
    public String purposeOfTransfer;
    @SerializedName("PayoutAgent")
    public String payoutAgent;
    @SerializedName("PayoutBranchName")
    public String payoutBranchName;
    @SerializedName("PayOutCountryCode")
    public String payOutCountryCode;
    @SerializedName("PayoutCityCode")
    public String payoutCityCode;
    @SerializedName("LandMark")
    public String landMark;
    @SerializedName("ZipCode")
    public String zipCode;
    @SerializedName("EmailID")
    public String emailId;
    @SerializedName("CustomerRelation")
    public String customerRelation;
    @SerializedName("IDtype_Description")
    public String idtype_Description;


    public String imageURL;
    public String idNumber;
    public String idType;
    public String idIssueDate;
    public String idExpiryDate;
    //extra variables
    public String beneficiaryRelationName;
    public String beneficiaryNo;

    public GetBeneficiaryListResponse() {
    }

    public GetBeneficiaryListResponse(Parcel in) {
        isHoldForOfAC = in.readByte() != 0;
        beneficiaryNumber = in.readString();
        customerNo = in.readString();
        beneficiaryNo = in.readString();
        firstName = in.readString();
        middleName = in.readString();
        lastName = in.readString();
        nickName = in.readString();
        address = in.readString();
        dateOfBirth = in.readString();
        telephone = in.readString();
        nationality = in.readString();
        idNumber = in.readString();
        idType = in.readString();
        idIssueDate = in.readString();
        idExpiryDate = in.readString();
        payOutCurrency = in.readString();
        paymentMode = in.readString();
        paymentBranchCode = in.readString();
        bankName = in.readString();
        bankCountry = in.readString();
        branchName = in.readString();
        bankCode = in.readString();
        accountNumber = in.readString();
        isActive = in.readString();
        purposeCode = in.readString();
        purposeOfTransfer = in.readString();
        payoutAgent = in.readString();
        payoutBranchName = in.readString();
        payOutCountryCode = in.readString();
        payoutCityCode = in.readString();
        landMark = in.readString();
        zipCode = in.readString();
        emailId = in.readString();
        customerRelation = in.readString();
        idtype_Description = in.readString();
    }

    public static final Creator<GetBeneficiaryListResponse> CREATOR = new Creator<GetBeneficiaryListResponse>() {
        @Override
        public GetBeneficiaryListResponse createFromParcel(Parcel in) {
            return new GetBeneficiaryListResponse(in);
        }

        @Override
        public GetBeneficiaryListResponse[] newArray(int size) {
            return new GetBeneficiaryListResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isHoldForOfAC ? 1 : 0));
        dest.writeString(beneficiaryNumber);
        dest.writeString(customerNo);
        dest.writeString(beneficiaryNo);
        dest.writeString(firstName);
        dest.writeString(middleName);
        dest.writeString(lastName);
        dest.writeString(nickName);
        dest.writeString(address);
        dest.writeString(dateOfBirth);
        dest.writeString(telephone);
        dest.writeString(nationality);
        dest.writeString(idNumber);
        dest.writeString(idType);
        dest.writeString(idIssueDate);
        dest.writeString(idExpiryDate);
        dest.writeString(payOutCurrency);
        dest.writeString(paymentMode);
        dest.writeString(paymentBranchCode);
        dest.writeString(bankName);
        dest.writeString(bankCountry);
        dest.writeString(branchName);
        dest.writeString(bankCode);
        dest.writeString(accountNumber);
        dest.writeString(isActive);
        dest.writeString(purposeCode);
        dest.writeString(purposeOfTransfer);
        dest.writeString(payoutAgent);
        dest.writeString(payoutBranchName);
        dest.writeString(payOutCountryCode);
        dest.writeString(payoutCityCode);
        dest.writeString(landMark);
        dest.writeString(zipCode);
        dest.writeString(emailId);
        dest.writeString(customerRelation);
        dest.writeString(idtype_Description);
    }
}
