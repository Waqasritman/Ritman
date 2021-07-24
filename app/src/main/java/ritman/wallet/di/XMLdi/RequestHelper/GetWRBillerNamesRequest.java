package ritman.wallet.di.XMLdi.RequestHelper;

import android.os.Parcel;
import android.os.Parcelable;

import ritman.wallet.di.XMLdi.Params;
import ritman.wallet.di.XMLdi.StaticHelper;

public class GetWRBillerNamesRequest implements Parcelable {
    public String billerTypeID;
    public String countryCode;
    public String billerCategoryID;
    public String languageId = "1";

    public GetWRBillerNamesRequest() {

    }


    protected GetWRBillerNamesRequest(Parcel in) {
        billerTypeID = in.readString();
        countryCode = in.readString();
        billerCategoryID = in.readString();
    }

    public static final Creator<GetWRBillerNamesRequest> CREATOR = new Creator<GetWRBillerNamesRequest>() {
        @Override
        public GetWRBillerNamesRequest createFromParcel(Parcel in) {
            return new GetWRBillerNamesRequest(in);
        }

        @Override
        public GetWRBillerNamesRequest[] newArray(int size) {
            return new GetWRBillerNamesRequest[size];
        }
    };

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetWRBillerNames>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:BillerTypeId>" + billerTypeID + "</tpay:BillerTypeId>" +
                "<tpay:CountryCode>" + countryCode + "</tpay:CountryCode>" +
                "<tpay:BillerCategoryId>" + billerCategoryID + "</tpay:BillerCategoryId>" +
                "</tpay:Req>" +
                "</tpay:GetWRBillerNames>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(billerTypeID);
        dest.writeString(countryCode);
        dest.writeString(billerCategoryID);
    }
}
