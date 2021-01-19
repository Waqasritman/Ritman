package tootipay.wallet.di.RequestHelper;

import tootipay.wallet.di.Params;
import tootipay.wallet.di.StaticHelper;

public class GetCCYForWalletRequest {
    public String languageID = "1";
    public String customerNo = "";
    public String actionType = "CW";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetCCYForWallet>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageID + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CustomerNumber>" + customerNo + "</tpay:CustomerNumber>" +
                "<tpay:ActionType>" + actionType + "</tpay:ActionType>" +
                "</tpay:Req>" +
                "</tpay:GetCCYForWallet>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
