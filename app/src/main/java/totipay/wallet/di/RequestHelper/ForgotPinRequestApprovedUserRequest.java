package totipay.wallet.di.RequestHelper;

import android.os.Parcel;
import android.os.Parcelable;

import totipay.wallet.di.Params;
import totipay.wallet.di.StaticHelper;

public class ForgotPinRequestApprovedUserRequest implements Parcelable {
    public String emailAddress = "";
    public String mobileNumber = "";
    public String idNumber = "";
    public String idExpireDate = "";
    public String languageId = "1";


    public ForgotPinRequestApprovedUserRequest(){

    }


    protected ForgotPinRequestApprovedUserRequest(Parcel in) {
        emailAddress = in.readString();
        mobileNumber = in.readString();
        idNumber = in.readString();
        idExpireDate = in.readString();
    }

    public static final Creator<ForgotPinRequestApprovedUserRequest> CREATOR = new Creator<ForgotPinRequestApprovedUserRequest>() {
        @Override
        public ForgotPinRequestApprovedUserRequest createFromParcel(Parcel in) {
            return new ForgotPinRequestApprovedUserRequest(in);
        }

        @Override
        public ForgotPinRequestApprovedUserRequest[] newArray(int size) {
            return new ForgotPinRequestApprovedUserRequest[size];
        }
    };

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:ForgetPIN>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Email_Address>" + emailAddress + "</tpay:Email_Address>" +
                "<tpay:Mobile_Number>" + mobileNumber + "</tpay:Mobile_Number>" +
                "<tpay:ID_Number>" + idNumber + "</tpay:ID_Number>" +
                "<tpay:IDExpiry_Date>" + idExpireDate + "</tpay:IDExpiry_Date>" +
                "</tpay:Req>" +
                "</tpay:ForgetPIN>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(emailAddress);
        dest.writeString(mobileNumber);
        dest.writeString(idNumber);
        dest.writeString(idExpireDate);
    }
}
