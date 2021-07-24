package ritman.wallet.di.XMLdi.RequestHelper;

import ritman.wallet.di.XMLdi.Params;
import ritman.wallet.di.XMLdi.StaticHelper;

public class GetYLocationRequest {
    public int cityID;
    public String languageID;

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetYLocation>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageID + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:City_ID>" + cityID + "</tpay:City_ID>" +
                "</tpay:Req>" +
                "</tpay:GetYLocation>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
