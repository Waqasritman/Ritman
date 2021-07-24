package ritman.wallet.di.XMLdi.RequestHelper;

import ritman.wallet.di.JSONdi.restRequest.Credentials;

public class WRPrepaidRechargeRequest {
    public Credentials Credentials = new Credentials();
    public String PayOutCurrency = "INR";
    public Double PayOutAmount = 0.0;
    public String PayinCurrency = "INR";
    public String CountryCode = "IN";
    public String MobileNumber = "";
    public String PlanId = "";
}
