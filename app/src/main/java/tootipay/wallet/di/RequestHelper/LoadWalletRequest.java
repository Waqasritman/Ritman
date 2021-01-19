package tootipay.wallet.di.RequestHelper;

import tootipay.wallet.di.Params;
import tootipay.wallet.di.StaticHelper;

public class LoadWalletRequest {
    public String customerNo;
    public String transferAmount;
    public String cardNumber = "";
    public String expireDate = "";
    public String securityNumber = "";
    public String walletCurrency;
    public String languageId = "1";
    public Integer paymentType;
    public String ipAddress = "";
    public String ipCountryName = "";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:LoadWallet>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Customer_No>" + customerNo + "</tpay:Customer_No>" +
                "<tpay:Transfer_Amount>" + transferAmount + "</tpay:Transfer_Amount>" +
                "<tpay:Card_Number>" + cardNumber + "</tpay:Card_Number>" +
                "<tpay:Expiry_Date>" + expireDate + "</tpay:Expiry_Date>" +
                "<tpay:Security_Code>" + securityNumber + "</tpay:Security_Code>" +
                "<tpay:Wallet_Currency>" + walletCurrency + "</tpay:Wallet_Currency>" +
                "<tpay:Payment_Type>" + paymentType + "</tpay:Payment_Type>" +
                "<tpay:IpAddress>" + ipAddress + "</tpay:IpAddress>" +
                "<tpay:IpCountryName>" + ipCountryName + "</tpay:IpCountryName>" +
                "</tpay:Req>" +
                "</tpay:LoadWallet>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;

    }
}
