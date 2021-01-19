package tootipay.wallet.di.RequestHelper;

import tootipay.wallet.di.Params;
import tootipay.wallet.di.StaticHelper;

public class PrepaidStatusRequest {
    public String requestId;
    public String languageId;

    public String getXML() {
        String xml = Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:WRPrepaidStatus>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:RequestId>" + requestId + "</tpay:RequestId>" +
                "</tpay:Req>" +
                "</tpay:WRPrepaidStatus>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
        return xml;
    }
}
