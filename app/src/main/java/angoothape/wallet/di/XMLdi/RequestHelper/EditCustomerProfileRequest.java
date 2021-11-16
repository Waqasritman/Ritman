package angoothape.wallet.di.XMLdi.RequestHelper;

import angoothape.wallet.di.XMLdi.Params;
import angoothape.wallet.di.XMLdi.StaticHelper;
import angoothape.wallet.login_signup_module.helper.RegisterUserRequest;

public class EditCustomerProfileRequest {
    public RegisterUserRequest customer = new RegisterUserRequest();
    public String customerNo;
    public String idNumber;
    public Integer idType;
    public String idIssueDate;
    public String idExpireDate;
    public String residenceCountry;
    public String languageId = "1";


    public String getXML() {
        String xml = Params.ENVELOP_OPENER
                + Params.HEADER_EMPTY +
                Params.BODY_OPEN +
                "<tpay:EditCustomerProfile>" +
                "<tpay:Req>" +
                "<tpay:Credentials>" +
                "<tpay:PartnerCode>" + StaticHelper.PARTNER_CODE_VALUE + "</tpay:PartnerCode>" +
                "<tpay:UserName>" + StaticHelper.USER_NAME_VALUE + "</tpay:UserName>" +
                "<tpay:UserPassword>" + StaticHelper.USER_PASSWORD_VALUE + "</tpay:UserPassword>" +
                "<tpay:LanguageID>" + languageId + "</tpay:LanguageID>" +
                "</tpay:Credentials>" +
                "<tpay:CustomerNo>" + customerNo + "</tpay:CustomerNo>" +
                "<tpay:FirstName>" + customer.firstName + "</tpay:FirstName>" +
                "<tpay:MiddleName>" + customer.middleName + "</tpay:MiddleName>" +
                "<tpay:LastName>" + customer.lastName + "</tpay:LastName>" +
                "<tpay:Address>" + customer.address + "</tpay:Address>" +
                "<tpay:DateOfBirth>" + customer.dob + "</tpay:DateOfBirth>" +
                "<tpay:Gender>" + customer.gender + "</tpay:Gender>" +
                "<tpay:MobileNumber>" + customer.phoneNumber + "</tpay:MobileNumber>" +
                "<tpay:Nationality>" + customer.nationality + "</tpay:Nationality>" +
                "<tpay:IDNumber>" + idNumber + "</tpay:IDNumber>" +
                "<tpay:IDType>" + idType + "</tpay:IDType>" +
                "<tpay:IDIssueDate>" + idIssueDate + "</tpay:IDIssueDate>" +
                "<tpay:IDExpiryDate>" + idExpireDate + "</tpay:IDExpiryDate>" +
                "<tpay:SourceOfFund>" + "1" + "</tpay:SourceOfFund>" +
                "<tpay:IsActive>" + "1" + "</tpay:IsActive>" +
                "<tpay:SourceOfFund>" + "2" + "</tpay:SourceOfFund>" +
                "<tpay:EmailID>" + customer.email + "</tpay:EmailID>" +
                "<tpay:IDtype_Description>" + "1" + "</tpay:IDtype_Description>" +
                "<tpay:ResidenceCountry>" + residenceCountry + "</tpay:ResidenceCountry>" +
                "<tpay:SourceOfFund_Desc>" + "123" + "</tpay:SourceOfFund_Desc>" +
                "</tpay:Req>" +
                "</tpay:EditCustomerProfile>" +
                Params.BODY_CLOSE +
                Params.ENVELOP_CLOSER;

        return xml;
    }
}
