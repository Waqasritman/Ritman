package totipay.wallet.di.RequestHelper;

import totipay.wallet.di.Params;
import totipay.wallet.di.StaticHelper;

public class AuthenticationRequest {
    public String mobileNumber = "";
    public String email = "";
    public String languageId = "1";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:Authentication>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Email_Address>" + email + "</tpay:Email_Address>" +
                "<tpay:Mobile_Number>" + mobileNumber + "</tpay:Mobile_Number>" +
                "</tpay:Req>" +
                "</tpay:Authentication>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
