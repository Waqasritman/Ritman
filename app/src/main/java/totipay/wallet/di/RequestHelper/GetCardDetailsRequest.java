package totipay.wallet.di.RequestHelper;

import totipay.wallet.di.Params;
import totipay.wallet.di.StaticHelper;

public class GetCardDetailsRequest {
    public String customerNo;
    public String languageID;


    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetCardDetails>"+
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageID + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Customer_No>" + customerNo + "</tpay:Customer_No>" +
                "</tpay:Req>" +
                "</tpay:GetCardDetails>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
