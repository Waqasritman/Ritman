package ritman.wallet.di.XMLdi.RequestHelper;

import android.os.Parcel;
import android.os.Parcelable;

import ritman.wallet.di.XMLdi.Params;
import ritman.wallet.di.XMLdi.StaticHelper;

public class WRBillerPlansRequest implements Parcelable {
    public String billerID;
    public String billerTypeID;
    public String countryCode;
    public String billerCategoryId;
    public String languageId = "1";

    public WRBillerPlansRequest() {

    }


    protected WRBillerPlansRequest(Parcel in) {
        if (in.readByte() == 0) {
            billerID = null;
        } else {
            billerID = String.valueOf(in.readInt());
        }
        billerTypeID = in.readString();
        countryCode = in.readString();
        billerCategoryId = in.readString();
    }

    public static final Creator<WRBillerPlansRequest> CREATOR = new Creator<WRBillerPlansRequest>() {
        @Override
        public WRBillerPlansRequest createFromParcel(Parcel in) {
            return new WRBillerPlansRequest(in);
        }

        @Override
        public WRBillerPlansRequest[] newArray(int size) {
            return new WRBillerPlansRequest[size];
        }
    };

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetWRBillerPlan>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:BillerID>" + billerID + "</tpay:BillerID>" +
                "<tpay:BillerTypeId>" + billerTypeID + "</tpay:BillerTypeId>" +
                "<tpay:CountryCode>" + countryCode + "</tpay:CountryCode>" +
                "<tpay:BillerCategoryId>" + billerCategoryId + "</tpay:BillerCategoryId>" +
                "</tpay:Req>" +
                "</tpay:GetWRBillerPlan>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (billerID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Integer.parseInt(billerID));
        }
        dest.writeString(billerTypeID);
        dest.writeString(countryCode);
        dest.writeString(billerCategoryId);
    }
}
