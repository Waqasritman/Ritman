package ritman.wallet.di.XMLdi.RequestHelper;

import ritman.wallet.di.XMLdi.Params;
import ritman.wallet.di.XMLdi.StaticHelper;

public class GetCustomerWalletDetailsRequest {

    public String customerNo = "";
    public String mobileNumber = "";
    public String languageId = "1";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetCustWalletDetails>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Customer_Number>" + customerNo + "</tpay:Customer_Number>" +
                "<tpay:Mobile_Number>" + mobileNumber + "</tpay:Mobile_Number>" +
                "</tpay:Req>" +
                "</tpay:GetCustWalletDetails>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
