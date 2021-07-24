package ritman.wallet.model;

import ritman.wallet.di.JSONdi.restRequest.Credentials;

public class RegisterModel {

    public Credentials credentials = new Credentials();
    public String firstName;
    public String middleName;
    public String lastName;
    public String address;
    public String gender = "m";
    public String mobileNumber;
    public String emailID;
    public String pincode;
    public String dateofbirth;

}