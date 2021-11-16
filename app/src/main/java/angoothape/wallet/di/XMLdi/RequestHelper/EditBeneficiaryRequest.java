package angoothape.wallet.di.XMLdi.RequestHelper;

import angoothape.wallet.di.XMLdi.Params;
import angoothape.wallet.di.XMLdi.StaticHelper;

public class EditBeneficiaryRequest {
    public String customerNo;
    public String FirstName;  //
    public String MiddleName = ""; //
    public String LastName; //
    public String Address; //
    public String Telephone;
    public String PayOutCurrency;
    public String PaymentMode;
    public String PayOutBranchCode = "";
    public String BankName = "";
    public String BankCountry = "";
    public String BranchNameAndAddress = "";
    public String BankCode = "";
    public String AccountNumber = "";
    public String PurposeCode = "1";
    public String PayoutCountryCode;
    public String CustomerRelation;
    public String BankBranch = "";

    //extra variables
    public Integer beneficiaryCountryId;
    public String beneficiaryNo;
    public String languageId = "1";

    public String getXML() {
        return Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:EditBeneficiaryProfile>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:BeneficiaryNo>" + beneficiaryNo + "</tpay:BeneficiaryNo>" +
                "<tpay:FirstName>" + FirstName + "</tpay:FirstName>" +
                "<tpay:MiddleName>" + MiddleName + "</tpay:MiddleName>" +
                "<tpay:LastName>" + LastName + "</tpay:LastName>" +
                "<tpay:Address>" + Address + "</tpay:Address>" +
                "<tpay:Telephone>" + Telephone + "</tpay:Telephone>" +
                "<tpay:PayOutCurrency>" + PayOutCurrency + "</tpay:PayOutCurrency>" +
                "<tpay:PaymentMode>" + PaymentMode + "</tpay:PaymentMode>" +
                "<tpay:PayOutBranchCode>" + PayOutBranchCode + "</tpay:PayOutBranchCode>" +
                "<tpay:BankName>" + BankName + "</tpay:BankName>" +
                "<tpay:BankCountry>" + BankCountry + "</tpay:BankCountry>" +
                "<tpay:BranchNameAndAddress>" + BranchNameAndAddress + "</tpay:BranchNameAndAddress>" +
                "<tpay:BankCode>" + BankCode + "</tpay:BankCode>" +
                "<tpay:AccountNumber>" + AccountNumber + "</tpay:AccountNumber>" +
                "<tpay:PurposeCode>" + PurposeCode + "</tpay:PurposeCode>" +
                "<tpay:PayoutCountryCode>" + PayoutCountryCode + "</tpay:PayoutCountryCode>" +
                "<tpay:CustomerRelation>" + CustomerRelation + "</tpay:CustomerRelation>" +
                "<tpay:BankBranch>" + BankBranch + "</tpay:BankBranch>" +
                "</tpay:Req>" +
                "</tpay:EditBeneficiaryProfile>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;

    }
}
