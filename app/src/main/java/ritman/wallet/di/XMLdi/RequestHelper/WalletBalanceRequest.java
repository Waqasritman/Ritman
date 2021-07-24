package ritman.wallet.di.XMLdi.RequestHelper;

import ritman.wallet.di.XMLdi.Params;
import ritman.wallet.di.XMLdi.StaticHelper;

public class WalletBalanceRequest {
    public String customerNo = "";
    public String walletCurrency = "";
    public String languageId = "1";
    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                    "<tpay:WalletBalance>" +
                    "<tpay:Req>" +
                    "<tpay:Credentials>" +
                    "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                    "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                    "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                    "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                    "</tpay:Credentials>" +
                    "<tpay:Customer_No>" + customerNo + "</tpay:Customer_No>" +
                    "<tpay:Wallet_Currency>" + walletCurrency + "</tpay:Wallet_Currency>" +
                    "</tpay:Req>" +
                    "</tpay:WalletBalance>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
