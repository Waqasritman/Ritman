package angoothape.wallet.di.XMLdi.RequestHelper;

import angoothape.wallet.di.XMLdi.Params;
import angoothape.wallet.di.XMLdi.StaticHelper;

public class PurposeOfTransferListRequest {
    public String languageId = "1";

    public String getXML(String shortCountryName) {
        String xml = Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetPurposeOfTransferList>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CountryShortName>" + shortCountryName + "</tpay:CountryShortName>" +
                "</tpay:Req>" +
                "</tpay:GetPurposeOfTransferList>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
        return xml;
    }
}
