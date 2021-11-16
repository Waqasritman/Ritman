package angoothape.wallet.di.XMLdi.RequestHelper;

import angoothape.wallet.di.XMLdi.Params;
import angoothape.wallet.di.XMLdi.StaticHelper;

public class GetTransactionReceiptRequest {
    public String transactionNumber;
    public String clientTxnNumber = "";
    public String languageId = "1";
    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:GetTransactionReceipt>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:TransactionNumber>" + transactionNumber + "</tpay:TransactionNumber>" +
                "<tpay:ClientTxnNumber>" + clientTxnNumber + "</tpay:ClientTxnNumber>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "</tpay:Req>" +
                "</tpay:GetTransactionReceipt>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
