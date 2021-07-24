package ritman.wallet.di.JSONdi.retrofit;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ritman.wallet.di.JSONdi.restRequest.CashePreApprovalRequest;
import ritman.wallet.di.JSONdi.restRequest.HospicareMemebrFormRequest;
import ritman.wallet.di.JSONdi.restRequest.OsVersionRequest;
import ritman.wallet.di.JSONdi.restRequest.VerifyOtpRequest;
import ritman.wallet.di.JSONdi.restRequest.esb_hospicare_group_req;
import ritman.wallet.di.JSONdi.restRequest.OtpRequest;
import ritman.wallet.di.JSONdi.restRequest.RegisterBeneficiaryRequest;
import ritman.wallet.di.JSONdi.restRequest.SimpleRequest;
import ritman.wallet.di.JSONdi.restRequest.UploadKYCImage;
import ritman.wallet.di.JSONdi.restRequest.UploadUserImageRequest;
import ritman.wallet.di.JSONdi.restResponse.BankListResponse;
import ritman.wallet.di.JSONdi.restResponse.BillDetailResponse;
import ritman.wallet.di.JSONdi.restResponse.BillPayResponse;
import ritman.wallet.di.JSONdi.restResponse.CaltransferResponse;
import ritman.wallet.di.JSONdi.restResponse.CashePreApprovalResponse;
import ritman.wallet.di.JSONdi.restResponse.CountryIpResponse;
import ritman.wallet.di.JSONdi.restResponse.GetCasheAccomodationListResponse;
import ritman.wallet.di.JSONdi.restResponse.GetOtpResponse;
import ritman.wallet.di.JSONdi.restResponse.GetProfileImage;
import ritman.wallet.di.JSONdi.restResponse.GetTransactionReceiptResponse;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillDetailResponseN;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerFieldsResponseN;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerTypeResponse;
import ritman.wallet.di.JSONdi.restResponse.GetWRCountryListResponseC;
import ritman.wallet.di.JSONdi.restResponse.HospicareMemebrFormResponse;
import ritman.wallet.di.JSONdi.restResponse.MobileTopUpResponse;
import ritman.wallet.di.JSONdi.restResponse.OsVersionResponse;
import ritman.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import ritman.wallet.di.JSONdi.restResponse.PinCodeListResponse;
import ritman.wallet.di.JSONdi.restResponse.PlanCategoriesResponse;
import ritman.wallet.di.JSONdi.restResponse.PrepaidOperatorResponse;
import ritman.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import ritman.wallet.di.JSONdi.restResponse.RechargePlansResponse;
import ritman.wallet.di.JSONdi.restResponse.RelationListResponse;
import ritman.wallet.di.JSONdi.restResponse.RitmanPaySendResponse;
import ritman.wallet.di.JSONdi.restResponse.UploadDocumentsResponse;
import ritman.wallet.di.JSONdi.restResponse.VerifyBeneficiary;
import ritman.wallet.di.JSONdi.restResponse.VerifyOtpResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.TransactionHistoryResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.WRCountryListResponse;
import ritman.wallet.di.generic_response.SimpleResponse;

import ritman.wallet.model.RegisterModel;


public interface RestApi {

    @GET("json")
    Call<CountryIpResponse> getIP(@Query("key") String key);

    @POST("UploadKYCImage")
    Call<SimpleResponse> uploadAttachment(@Body UploadKYCImage uploadKYCImage);

    @GET("GetCustomerImage2")
    Call<GetProfileImage> getCustomerImage(@Query("Customer_No") String customerNo
            , @Query("PartnerCode") Integer partnerCode, @Query("UserName") String username,
                                           @Query("UserPassword") String password,
                                           @Query("LanguageID") Integer languageId);


//    @HTTP(method = "GET", path = "TotiPayRestAPI/Images/GetCustomerImage", hasBody = true)
//    Call<GetProfileImage> getCustomerImage(@Body GetCustomerProfileImageRequest request);

    @POST("AddCustomerImage")
    Call<SimpleResponse> uploadCustomerImage(@Body UploadUserImageRequest request);


    @POST("Auth/LoginUser")
    Call<SimpleResponse> createMerchant(@Body SimpleRequest credentials,
                                        @Header("Username") String Username, @Header("UserPassword") String UserPassword);

    @POST("Auth/VerifyOTP")
    Call<SimpleResponse> createotp(@Body OtpRequest credentials,
                                   @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Auth/GetAvailableBalance")
    Call<SimpleResponse> getBalance(@Body RequestBody body, @Header("Username") String userName);

    @POST("Customer/CustomerRegistration")
    Call<SimpleResponse> createRegister(@Body RegisterModel model,
                                        @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("GetList/GetRelationList")
    Call<RelationListResponse> getRelationList(@Body RequestBody request,
                                               @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("NetworkList/GetBankNetworkList")
    Call<BankListResponse> getBankNetworkList(@Body RequestBody request,
                                              @Header("Username") String Username);


    @POST("Beneficiary/BeneficiaryRegistration")
    Call<SimpleResponse> createBeneficiary(@Body RegisterBeneficiaryRequest model,
                                           @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Beneficiary/GetBeneficiaryList")
    Call<GetBeneficiaryListResponse> getBeneficiaryListResponse(@Body RequestBody request,
                                                                @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Calculate/CalTransfer")
    Call<CaltransferResponse> getCalTransfer(@Body RequestBody requestBody,
                                             @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Transfer/RitmanPaySend")
    Call<RitmanPaySendResponse> getSummary(@Body RequestBody requestBody,
                                           @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Transfer/GetTransactionReceipt")
    Call<GetTransactionReceiptResponse> getReciept(@Body RequestBody requestBody,
                                                   @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Transfer/TransactionHistory")
    Call<TransactionHistoryResponse> getTransactionHistory(@Body RequestBody requestBody,
                                                           @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Transfer/VerifyBeneficiary")
    Call<VerifyBeneficiary> verifyBeneficairy(@Body RequestBody requestBody,
                                              @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WR/GetWRBillerType")
    Call<GetWRBillerTypeResponse> GetWRBillerType(@Body RequestBody requestBody,
                                                  @Header("Username") String Username);

    //Country
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WR/GetWRCountryList")
    Call<GetWRCountryListResponseC> GetCountryList(@Body RequestBody requestBody,
                                                   @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WR/GetWRBillerCategory")
    Call<GetWRBillerCategoryResponseC> GetWRBillerCategory(@Body RequestBody requestBody,
                                                           @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WR/GetWRBillerNames")
    Call<GetWRBillerNamesResponseC> GetWRBillerNames(@Body RequestBody requestBody,
                                                     @Header("Username") String Username);
    //GetWRBillerFields
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WR/GetWRBillerFields")
    Call<GetWRBillerFieldsResponseN> GetWRBillerFields(@Body RequestBody requestBody,
                                                       @Header("Username") String Username);

    //GetWRBillDetail
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WR/WRBillDetail")
    Call<GetWRBillDetailResponseN> GetWRBillDetail(@Body RequestBody requestBody,
                                                   @Header("Username") String Username);


    //PayBill
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WR/WRPayBill")
    Call<BillPayResponse> getBillPay(@Body RequestBody requestBody,
                                     @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WR/GetWRBillerTypeMobileTopUp")
    Call<MobileTopUpResponse> getMobileTopUp(@Body RequestBody requestBody,
                                             @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("BillPayment/GetPrepaidOperator")
    Call<PrepaidOperatorResponse> getOperator(@Body RequestBody requestBody,
                                              @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WR/GetWRCountryList")
    Call<WRCountryListResponse> getCountryList(@Body RequestBody requestBody,
                                               @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("BillPayment/GetRechargePlans")
    Call<PrepaidPlanResponse> getPrepaidPlan(@Body RequestBody requestBody,
                                             @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("WR/WRPrepaidRecharge")
    Call<SimpleResponse> prepaidRecharge(@Body RequestBody requestBody,
                                         @Header("Username") String Username);


    //GetCashePincodeList
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/GetCashePincodeList")
    Call<PinCodeListResponse> getCashePincodeList(@Body RequestBody requestBody,
                                                  @Header("Username") String Username);

    //GetCasheAccomodationList
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/GetCasheAccomodationList")
    Call<GetCasheAccomodationListResponse> getCasheAccomodationList(@Body RequestBody requestBody,
                                                                    @Header("Username") String Username);
    // GetCasheQualificationList
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/GetCasheQualificationList")
    Call<GetCasheAccomodationListResponse> getCasheQualificationList(@Body RequestBody requestBody,
                                                                     @Header("Username") String Username);
    // GetCasheDesignationList
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/GetCasheDesignationList")
    Call<GetCasheAccomodationListResponse> getCasheDesignationList(@Body RequestBody requestBody,
                                                                   @Header("Username") String Username);

    // GetCasheProductTypes
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/GetCasheProductTypes")
    Call<GetCasheAccomodationListResponse> getCasheProductTypes(@Body RequestBody requestBody,
                                                                @Header("Username") String Username);

    // GetCasheEmploymentType
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/GetCasheEmploymentType")
    Call<GetCasheAccomodationListResponse> getCasheEmploymentType(@Body RequestBody requestBody,
                                                                  @Header("Username") String Username);

    // GetCasheSalaryTypes
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/GetCasheSalaryTypes")
    Call<GetCasheAccomodationListResponse> getCasheSalaryTypes(@Body RequestBody requestBody,
                                                               @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/GetCasheNoOfKids")
    Call<GetCasheAccomodationListResponse> getNoOfKids(@Body RequestBody requestBody,
                                                               @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/GetCasheYearsAtCurrentAdd")
    Call<GetCasheAccomodationListResponse> getCasheYearsAtCurrentAdd(@Body RequestBody requestBody,
                                                       @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/GetCasheResidingWith")
    Call<GetCasheAccomodationListResponse> getCasheResidingWith(@Body RequestBody requestBody,
                                                                     @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/GetCasheWorkSectors")
    Call<GetCasheAccomodationListResponse> getCasheWorkSectors(@Body RequestBody requestBody,
                                                                @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/GetCasheJobFunctions")
    Call<GetCasheAccomodationListResponse> getCasheJobFunctions(@Body RequestBody requestBody,
                                                               @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/GetCasheOrganizations")
    Call<GetCasheAccomodationListResponse> getCasheOrganizations(@Body RequestBody requestBody,
                                                                @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("BillPayment/GetPlanCategories")
    Call<PlanCategoriesResponse> getPlanName(@Body RequestBody requestBody,
                                             @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("BillPayment/GetRechargePlans")
    Call<RechargePlansResponse> getRechargePlanName(@Body RequestBody requestBody,
                                                    @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("BillPayment/GetBillDetail")
    Call<BillDetailResponse> getBillDetails(@Body RequestBody requestBody,
                                            @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/Run_PreApproval")
    Call<CashePreApprovalResponse> createCustomer(@Body RequestBody requestBody,
                                                  @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/check_Customer_Status")
    Call<CashePreApprovalResponse> getCustomerStatus(@Body RequestBody requestBody,
                                                  @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("BillPayment/PayBillPayment")
    Call<PayBillPaymentResponse> getPayBill(@Body RequestBody requestBody,
                                            @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("IndiaFirst_Life_Group/Hospicare_Member_Form")
    Call<HospicareMemebrFormResponse> getHospicareMemberForm(@Body HospicareMemebrFormRequest hospicarememebrFormrequest,
                                                 @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("Cashe/Upload_KYC_documents")
    Call<UploadDocumentsResponse> uploadDocuments(@Body RequestBody requestBody,
                                                  @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("OTP/GenerateOTP")
    Call<GetOtpResponse> getOtp(@Body RequestBody requestBody,
                                @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("OTP/VerifyOTP")
    Call<VerifyOtpResponse> verifyOtp(@Body RequestBody requestBody,
                                      @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("CustomerDevice/OS_Version")
    Call<OsVersionResponse> getOsVersion(@Body OsVersionRequest requestBody,
                                         @Header("Username") String Username);
}