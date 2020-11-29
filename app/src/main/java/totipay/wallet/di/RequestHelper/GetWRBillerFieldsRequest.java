package totipay.wallet.di.RequestHelper;

import totipay.wallet.di.Params;
import totipay.wallet.di.StaticHelper;

public class GetWRBillerFieldsRequest {
    public String countryCode;
    public Integer billerID;
    public Integer skuID;
    public String languageId = "1";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetWRBillerFields>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:countryCode>" + countryCode + "</tpay:countryCode>" +
                "<tpay:BillerID>" + billerID + "</tpay:BillerID>" +
                "<tpay:SkuID>" + skuID + "</tpay:SkuID>" +
                "</tpay:Req>" +
                "</tpay:GetWRBillerFields>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
