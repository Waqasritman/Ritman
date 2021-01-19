package tootipay.wallet.di.RequestHelper;

import tootipay.wallet.di.Params;
import tootipay.wallet.di.StaticHelper;

public class GetWRBillerCategoryRequest {
    public String billerTypeId;
    public String countryCode;
    public String languageId = "1";
    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetWRBillerCategory>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CountryCode>" + countryCode + "</tpay:CountryCode>" +
                "<tpay:BillerTypeId>" + billerTypeId + "</tpay:BillerTypeId>" +
                "</tpay:Req>" +
                "</tpay:GetWRBillerCategory>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
