package tootipay.wallet.di.RequestHelper;

import tootipay.wallet.di.Params;
import tootipay.wallet.di.StaticHelper;

public class CountryPayOutCurrencyRequest {

    public String countryShortName;
    public String languageId = "1";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:APIGetPayOutCurrencyList>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CountryShortName>" + countryShortName + "</tpay:CountryShortName>" +
                "</tpay:Req>" +
                "</tpay:APIGetPayOutCurrencyList>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
