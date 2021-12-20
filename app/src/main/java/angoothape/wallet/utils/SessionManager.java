package angoothape.wallet.utils;

import android.content.Context;
import android.content.Intent;

import angoothape.wallet.AutoLogoutActivity;
import angoothape.wallet.di.XMLdi.ResponseHelper.CustomerProfile;


import angoothape.wallet.login_signup_module.helper.RegisterUserRequest;

/**
 * Created by HS on 01-Mar-18.
 * FIL AHM
 */

public class SessionManager {

    private static final String PREF_NAME = "login_session";
    private final MySharedPreferences pref;
    // private final SharedPreferences.pref pref;
    private final Context context;
    private final int PRIVATE_MODE = 0;

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String USER_NAME = "user_name";
    public static final String BENE_NAME = "bene_name";
    public static final String PayInCurrency = "PayInCurrency";


    //    MySharedPreferences prefs = MySharedPreferences.getInstance(this,"tutorialsFACE_Prefs"); //provide context & preferences name.
//
//    //Storing the username inside shared preferences
//prefs.putString("username", "Khushi");
//prefs.commit();
//    //Retrieving username from encrypted shared preferences
//    String name = prefs.getString('username','default_username');
//
    public SessionManager(Context context) {
        this.context = context;
        //  pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        pref = MySharedPreferences.getInstance(context, PREF_NAME); //provide context & preferences name.

        //  pref = pref.edit();
        // pref.commit();
    }


    public void merchantName(String userName) {
        pref.putString(USER_NAME, userName);
        // commit changes
        pref.commit();
    }

    public void beneName(String beneName) {
        pref.putString(BENE_NAME, beneName);
        // commit changes
        pref.commit();
    }

    public String getBeneName() {
        return pref.getString(BENE_NAME, "");
    }


    public String getMerchantName() {
        return pref.getString(USER_NAME, "");
    }


    public void payInCurrency(String payInCurrency) {
        pref.putString(PayInCurrency, payInCurrency);
        // commit changes
        pref.commit();
    }

    public String getpayInCurrency() {
        return pref.getString(PayInCurrency, "");
    }


    public void setIsVerified(String isVerified) {
        pref.putString("isVerified", isVerified);
        pref.commit();
    }

    public boolean getIsVerified() {
        return Boolean.parseBoolean(pref.getString("isVerified", ""));
    }

    /**
     * Create login session
     */
    public void createLoginSession() {
        // Storing login value as TRUE
        pref.putBoolean(IS_LOGIN, true);
        // commit changes
        pref.commit();
    }

    public void setIsLogin(boolean login) {
        pref.putBoolean(IS_LOGIN, login);
        pref.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void logoutUserInBackgroundOrForeground(Context context) {
        setIsLogin(false);
        pref.clear();
        pref.commit();

        Intent i = new Intent(context, AutoLogoutActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        context.startActivity(i);
    }


    public void walletBalance(Double walletBalance) {
        pref.putFloat("wallet_balance", walletBalance.floatValue());
        pref.commit();
    }

    public Float getWalletBalance() {
        return pref.getFloat("wallet_balance", 0.0f);
    }


    public void setCustomerNo(String customerNo) {
        pref.putString("customer_no", customerNo);
        pref.commit();
    }

    public void setCustomer(RegisterUserRequest userRequest) {
        pref.putString("first_name", userRequest.firstName);
        pref.putString("last_name", userRequest.lastName);
        pref.putString("middle_name", userRequest.middleName);
        pref.putString("last_name", userRequest.lastName);
        pref.putString("address", userRequest.address);
        pref.putString("dob", userRequest.dob);
        pref.putString("gender", userRequest.gender);
        pref.putString("phone_number", userRequest.phoneNumber);
        pref.putString("nationlaity", userRequest.nationality);
        pref.putString("emailID", userRequest.email);
        pref.putString("residenceCountry", userRequest.country);
        pref.commit();
    }

    public void setCustomerGet(CustomerProfile userRequest) {
        // pref.putString("customer_no" , userRequest.customerNo);
        pref.putString("first_name", userRequest.firstName);
        pref.putString("last_name", userRequest.lastName);
        pref.putString("middle_name", userRequest.middleName);
        pref.putString("last_name", userRequest.lastName);
        pref.putString("address", userRequest.address);
        pref.putString("dob", userRequest.dateOfBirth);
        pref.putString("gender", userRequest.gender.trim());
        pref.putString("phone_number", userRequest.mobileNo);
        pref.putString("nationlaity", userRequest.nationality);
        pref.putString("emailID", userRequest.email);
        pref.putString("residenceCountry", userRequest.residenceCountry);
        pref.commit();
    }


    public String getUserName() {
        return pref.getString("first_name", "").concat(" ")
                .concat(pref.getString("last_name", ""));
    }


    public void customerImage(String customerImage) {
        pref.putString("customer_image", customerImage);
        pref.commit();
    }


    public String getCustomerImage() {
        return pref.getString("customer_image", "");
    }


    public void putCustomerPhone(String phone) {
        pref.putString("phone_number", phone);
        pref.commit();
    }


    public void putEmailAddress(String email) {
        pref.putString("emailID", email);
        pref.commit();
    }

    public void userEmailRemember(String email) {
        pref.putString("userEmailRemember", email);
        pref.commit();
    }

    public String getRememberEmail() {
        return pref.getString("userEmailRemember", "");
    }

    public void userPhoneRemember(String number) {
        pref.putString("number_remember", number);
        pref.commit();
    }


    public String getURLFlag() {
        return pref.getString("url", "");
    }

    public String getUserCountryCode() {
        return pref.getString("iso_remember", "");
    }

    public String getUserNumberRemember() {
        return pref.getString("number_remember", "");
    }

    public String getCustomerPhone() {
        return pref.getString("phone_number", "");
    }

    public String getEmail() {
        return pref.getString("emailID", "");
    }


    public RegisterUserRequest getCustomerDetails() {
        RegisterUserRequest userRequest = new RegisterUserRequest();
        userRequest.firstName = pref.getString("first_name", "");
        userRequest.lastName = pref.getString("last_name", "");
        userRequest.middleName = pref.getString("middle_name", "");
        userRequest.address = pref.getString("address", "");
        userRequest.dob = pref.getString("dob", "");
        userRequest.gender = pref.getString("gender", "");
        userRequest.phoneNumber = pref.getString("phone_number", "");
        userRequest.nationality = pref.getString("nationlaity", "");
        userRequest.email = pref.getString("emailID", "");
        userRequest.country = pref.getString("residenceCountry", "");
        return userRequest;
    }


    public String getResidenceCountry() {
        return pref.getString("residenceCountry", "GBR");
    }


    public void setDocumentsUploaded(Boolean customerISNew) {
        pref.putBoolean("is_user_new", customerISNew);
        pref.commit();
    }

    public boolean getIsDocumentsUploaded() {
        return pref.getBoolean("is_user_new", true);
    }

    public void isKYCApproved(Boolean status) {
        pref.putBoolean("isKYCApproved", status);
        pref.commit();
    }


    public void putWalletNeedToUpdate(boolean isNeed) {
        pref.putBoolean(Constants.isWalletUpdate, isNeed);
        pref.commit();
    }


    public boolean isWalletNeedToUpdate() {
        return pref.getBoolean(Constants.isWalletUpdate, true);
    }


    public Boolean getISKYCApproved() {
        return pref.getBoolean("isKYCApproved", false);
    }


    public String getCustomerNo() {
        return pref.getString("customer_no", "");
    }

    public boolean getCustomerStatus() {
        return pref.getBoolean("is_user_new", false);
    }


    public void updateUserProfile(String userDetail) {
        pref.putString("user_detail", userDetail);
        pref.commit();
    }

    public boolean getRememberMe() {
        return pref.getBoolean("remember_me", false);
    }

    public void setRememberMe(boolean rememberMe) {
        pref.putBoolean("remember_me", rememberMe);
        pref.commit();
    }

    public String getDeviceToken() {
        return pref.getString("device_token", "");
    }

    public String getLoginWith() {
        return pref.getString("login_with", "");
    }

    public String getuserflagimage() {
        return pref.getString("flag_image", "");
    }

    public void setDeviceToken(String deviceToken) {
        pref.putString("device_token", deviceToken);
        pref.commit();
    }

    public String getlanguageselection() {
        return pref.getString("language_selection", "1");
    }

    public void setlanguageselection(String languageId) {
        pref.putString("language_selection", languageId);
        pref.commit();
    }

    public void setLoginWith(String loginwith) {
        pref.putString("login_with", loginwith);
        pref.commit();
    }

    public void setuserflagimage(String flagimage) {
        pref.putString("flag_image", flagimage);
        pref.commit();
    }


    public boolean getLogin() {
        return pref.getBoolean("is_login", false);
    }

    public void setLogin(boolean flag) {
        pref.putBoolean("is_login", flag);
        pref.commit();
    }


    public void setWalletBalance(float balance) {
        pref.putFloat("wallet_balance", balance);
        pref.commit();
    }


    public boolean getVerify() {
        return pref.getBoolean("is_verify", false);
    }

    public boolean getLogoutVerify() {
        return pref.getBoolean("is_logout_verify", false);
    }

    public void setVerify(boolean flag) {
        pref.putBoolean("is_verify", flag);
        pref.commit();
    }

    public void setLogoutVerify(boolean flag) {
        pref.putBoolean("is_logout_verify", flag);
        pref.commit();
    }

    public boolean isUserDataEmpty() {
        return pref.getString("gender", "").isEmpty();
    }

    public void clearUser() {
        pref.putString("first_name", "");
        pref.putString("last_name", "");
        pref.putString("middle_name", "");
        pref.putString("last_name", "");
        pref.putString("address", "");
        pref.putString("dob", "");
        pref.putString("gender", "");
        pref.putString("phone_number", "");
        pref.putString("nationlaity", "");
        pref.putString("emailID", "");
        pref.putString("residenceCountry", "");
        pref.putString("customer_no", "");
        pref.putFloat("wallet_balance", 0.0f);
        pref.commit();
    }


    public void setDefaultLanuguage(String language) {
        pref.putString("default_language", language);
        pref.commit();
    }


    public String getDefaultLanguage() {
        return pref.getString("default_language", "");
    }

    public void setVirtualCardNo(String customerCardNo) {
        pref.putString("virtual_card_no", customerCardNo);
        pref.commit();
    }

    public String getVirtualCardNo() {
        return pref.getString("virtual_card_no", "");
    }

    public void setIpAddress(String ip) {
        pref.putString("setIpAddress", ip);
        pref.commit();
    }

    public void IpCountryName(String country_name) {
        pref.putString("IpCountryName", country_name);
        pref.commit();
    }

    public String getIpAddress() {
        return pref.getString("setIpAddress", "");
    }

    public String getIpCountryName() {
        return pref.getString("IpCountryName", "");
    }
}