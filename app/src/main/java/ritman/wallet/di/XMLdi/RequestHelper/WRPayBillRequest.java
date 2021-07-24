package ritman.wallet.di.XMLdi.RequestHelper;

import android.os.Parcel;
import android.os.Parcelable;

import ritman.wallet.di.XMLdi.Params;
import ritman.wallet.di.XMLdi.StaticHelper;

public class WRPayBillRequest implements Parcelable {
    public String customerNo;
    public String payoutCurrency;
    public String payOutAmount;
    public String payInCurrency;
    public String countryCode;
    public Integer billerID;
    public Integer skuID;
    public String mobileAccount = "";
    public String mobileAccount2 = "";
    public String mobileAccount3 = "";
    public String languageId = "1";
    public Integer paymentTypeId;
    public String cardNumber = "";
    public String expireDate = "";
    public String securityCode = "";


    public WRPayBillRequest() {
    }


    protected WRPayBillRequest(Parcel in) {
        customerNo = in.readString();
        payoutCurrency = in.readString();
        payOutAmount = in.readString();
        payInCurrency = in.readString();
        countryCode = in.readString();
        billerID = in.readInt();
        skuID = in.readInt();
        mobileAccount = in.readString();
        mobileAccount2 = in.readString();
        mobileAccount3 = in.readString();
        languageId = in.readString();
        paymentTypeId = in.readInt();
        cardNumber = in.readString();
        expireDate = in.readString();
        securityCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerNo);
        dest.writeString(payoutCurrency);
        dest.writeString(payOutAmount);
        dest.writeString(payInCurrency);
        dest.writeString(countryCode);
        dest.writeInt(billerID);
        dest.writeInt(skuID);
        dest.writeString(mobileAccount);
        dest.writeString(mobileAccount2);
        dest.writeString(mobileAccount3);
        dest.writeString(languageId);
        dest.writeInt(paymentTypeId);
        dest.writeString(cardNumber);
        dest.writeString(expireDate);
        dest.writeString(securityCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WRPayBillRequest> CREATOR = new Creator<WRPayBillRequest>() {
        @Override
        public WRPayBillRequest createFromParcel(Parcel in) {
            return new WRPayBillRequest(in);
        }

        @Override
        public WRPayBillRequest[] newArray(int size) {
            return new WRPayBillRequest[size];
        }
    };

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:WRPayBill>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CustomerNo>" + customerNo + "</tpay:CustomerNo>" +
                "<tpay:PayOutCurrency>" + payoutCurrency + "</tpay:PayOutCurrency>" +
                "<tpay:PayOutAmount>" + payOutAmount + "</tpay:PayOutAmount>" +
                "<tpay:PayinCurrency>" + payInCurrency + "</tpay:PayinCurrency>" +
                "<tpay:countryCode>" + countryCode + "</tpay:countryCode>" +
                "<tpay:BillerID>" + billerID + "</tpay:BillerID>" +
                "<tpay:SkuID>" + skuID + "</tpay:SkuID>" +
                "<tpay:Mob_Acc_No>" + mobileAccount + "</tpay:Mob_Acc_No>" +
                "<tpay:Mob_Acc_No1>" + mobileAccount2 + "</tpay:Mob_Acc_No1>" +
                "<tpay:Mob_Acc_No2>" + mobileAccount3 + "</tpay:Mob_Acc_No2>" +
                "<tpay:Payment_TypeID>" + paymentTypeId + "</tpay:Payment_TypeID>" +
                "<tpay:Card_Number>" + cardNumber + "</tpay:Card_Number>" +
                "<tpay:ExpiryDate>" + expireDate + "</tpay:ExpiryDate>" +
                "<tpay:SecurityCode>" + securityCode + "</tpay:SecurityCode>" +
                "</tpay:Req>" +
                "</tpay:WRPayBill>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }


}
