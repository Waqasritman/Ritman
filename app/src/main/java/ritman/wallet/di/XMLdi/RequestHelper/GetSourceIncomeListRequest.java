package ritman.wallet.di.XMLdi.RequestHelper;

import ritman.wallet.di.XMLdi.Params;
import ritman.wallet.di.XMLdi.StaticHelper;

public class GetSourceIncomeListRequest {
    public String languageId = "1";


    public String getXML() {
        String xml = Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
               "<tpay:GetSourceOfIncomeList>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "</tpay:Req>" +
                "</tpay:GetSourceOfIncomeList>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;

        return xml;
    }
}