package totipay.wallet.di.RequestHelper;

import totipay.wallet.di.Params;
import totipay.wallet.di.StaticHelper;

public class GetCashNetworkListRequest {
    public String countryCode;
    public String payoutBankName = "";
    public String payoutBranch = "";
    public String languageId = "1";


    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetCashNetworkList>"+
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CountryCode>" + countryCode + "</tpay:CountryCode>" +
                "<tpay:PayOutBankName>" + payoutBankName + "</tpay:PayOutBankName>" +
                "<tpay:PayOutBranch>" + payoutBranch+ "</tpay:PayOutBranch>" +
                "</tpay:Req>" +
                "</tpay:GetCashNetworkList>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
