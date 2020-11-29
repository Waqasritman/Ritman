package totipay.wallet.di.RequestHelper;

import totipay.wallet.di.Params;
import totipay.wallet.di.StaticHelper;

public class VerifyOTPRequest {
    public String mobileNumber = "";
    public String emailAddress = "";
    public String OTP = "";
    public String languageId = "1";
    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:VerifyOTP>"+
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Email_Address>" + emailAddress + "</tpay:Email_Address>" +
                "<tpay:Mobile_Number>" + mobileNumber + "</tpay:Mobile_Number>" +
                "<tpay:OTP>" + OTP+ "</tpay:OTP>" +
                "</tpay:Req>" +
                "</tpay:VerifyOTP>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
