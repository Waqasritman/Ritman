package angoothape.wallet.di.JSONdi.restRequest.aepsSettlement;

import angoothape.wallet.di.JSONdi.restRequest.Cred;

public class AEPSAddBeneficiaryRequest {
    Cred Credentials = new Cred();
    public String Beneficiary_Name;
    public String Beneficiary_Account_No;
    public String Beneficiary_IFSC_Code;
}
