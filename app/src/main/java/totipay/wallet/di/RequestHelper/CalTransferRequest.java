package totipay.wallet.di.RequestHelper;

import totipay.wallet.di.MethodNameHelper;
import totipay.wallet.di.Params;
import totipay.wallet.di.StaticHelper;

public class CalTransferRequest {

    public String PayInCurrency;
    public String PayoutCurrency;
    public String TransferCurrency;
    public Double TransferAmount;
    public String PaymentMode;
    public String languageId = "1";
    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                MethodNameHelper.CAL_TRANSFER_OPENER +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:PayInCurrency>" + PayInCurrency + "</tpay:PayInCurrency >" +
                "<tpay:PayoutCurrency>" + PayoutCurrency + "</tpay:PayoutCurrency>" +
                "<tpay:TransferCurrency>" + TransferCurrency + "</tpay:TransferCurrency>" +
                "<tpay:TransferAmount>" + TransferAmount + "</tpay:TransferAmount>" +
                "<tpay:PaymentMode>" + PaymentMode + "</tpay:PaymentMode>" +
                "</tpay:Req>" +
                MethodNameHelper.CAL_TRANSFER_CLOSER +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
