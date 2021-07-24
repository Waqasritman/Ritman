package ritman.wallet.di.XMLdi.RequestHelper;

import ritman.wallet.di.XMLdi.Params;
import ritman.wallet.di.XMLdi.StaticHelper;

public class TootiPayRequest {
    public Double sendingAmounttomatch;
    public String languageId = "1";

    public String customerNo;
    public String beneficiaryNo;
    public String payOutCurrency;
    public String payInCurrency;
    public Double transferAmount;
    public Integer sourceOfIncome;
    public Integer paymentTypeId;
    public Integer purposeOfTransfer;

    public String cardNumber = "";
    public String expireDate = "";
    public String securityNumber = "";
    public String ipAddress = "";
    public String ipCountryName = "";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:TotiPaySend>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:Customer_No>" + customerNo + "</tpay:Customer_No>" +
                "<tpay:Beneficiary_No>" + beneficiaryNo + "</tpay:Beneficiary_No>" +
                "<tpay:PayOutCurrency>" + payOutCurrency + "</tpay:PayOutCurrency>" +
                "<tpay:TransferAmount>" + transferAmount + "</tpay:TransferAmount>" +
                "<tpay:Payin_Currency>" + payInCurrency + "</tpay:Payin_Currency>" +
                "<tpay:SourceofIncome>" + sourceOfIncome + "</tpay:SourceofIncome>" +
                "<tpay:PurposeOfTransfer>" + purposeOfTransfer + "</tpay:PurposeOfTransfer>" +
                "<tpay:Payment_TypeID>" + paymentTypeId + "</tpay:Payment_TypeID>" +
                "<tpay:IpAddress>" + ipAddress + "</tpay:IpAddress>" +
                "<tpay:IpCountryName>" + ipCountryName + "</tpay:IpCountryName>" +
                "<tpay:Card_Number>" + cardNumber + "</tpay:Card_Number>" +
                "<tpay:ExpiryDate>" + expireDate + "</tpay:ExpiryDate>" +
                "<tpay:SecurityCode>" + securityNumber + "</tpay:SecurityCode>" +
                "</tpay:Req>" +
                "</tpay:TotiPaySend>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;
    }
}
