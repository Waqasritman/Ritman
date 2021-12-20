package angoothape.wallet.di.JSONdi.retrofit;


import angoothape.wallet.di.JSONdi.restResponse.AEResponse;
import angoothape.wallet.di.JSONdi.restResponse.DistributorDetailsResponse;
import angoothape.wallet.di.JSONdi.restResponse.GetBalanceCustomerLimit;
import angoothape.wallet.di.JSONdi.restResponse.GetBusStationsResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import angoothape.wallet.di.JSONdi.restRequest.HospicareMemebrFormRequest;
import angoothape.wallet.di.JSONdi.restRequest.OsVersionRequest;
import angoothape.wallet.di.JSONdi.restRequest.OtpRequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restRequest.UploadKYCImage;
import angoothape.wallet.di.JSONdi.restRequest.UploadUserImageRequest;
import angoothape.wallet.di.JSONdi.restResponse.BankListResponse;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailResponse;
import angoothape.wallet.di.JSONdi.restResponse.BillPayResponse;
import angoothape.wallet.di.JSONdi.restResponse.CaltransferResponse;
import angoothape.wallet.di.JSONdi.restResponse.CashePreApprovalResponse;
import angoothape.wallet.di.JSONdi.restResponse.CountryIpResponse;
import angoothape.wallet.di.JSONdi.restResponse.GetCasheAccomodationListResponse;
import angoothape.wallet.di.JSONdi.restResponse.GetOtpResponse;
import angoothape.wallet.di.JSONdi.restResponse.GetProfileImage;
import angoothape.wallet.di.JSONdi.restResponse.GetTransactionReceiptResponse;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillDetailResponseN;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerFieldsResponseN;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerTypeResponse;
import angoothape.wallet.di.JSONdi.restResponse.GetWRCountryListResponseC;
import angoothape.wallet.di.JSONdi.restResponse.HospicareMemebrFormResponse;
import angoothape.wallet.di.JSONdi.restResponse.MobileTopUpResponse;
import angoothape.wallet.di.JSONdi.restResponse.OsVersionResponse;
import angoothape.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import angoothape.wallet.di.JSONdi.restResponse.PinCodeListResponse;
import angoothape.wallet.di.JSONdi.restResponse.PlanCategoriesResponse;
import angoothape.wallet.di.JSONdi.restResponse.PrepaidOperatorResponse;
import angoothape.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import angoothape.wallet.di.JSONdi.restResponse.RechargePlansResponse;
import angoothape.wallet.di.JSONdi.restResponse.RelationListResponse;
import angoothape.wallet.di.JSONdi.restResponse.UploadDocumentsResponse;
import angoothape.wallet.di.JSONdi.restResponse.VerifyOtpResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.TransactionHistoryResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.WRCountryListResponse;
import angoothape.wallet.di.generic_response.SimpleResponse;


public interface RestApi {

    @GET("json")
    Call<CountryIpResponse> getIP(@Query("key") String key);

    @POST("RitmanPay/UploadKYCImage")
    Call<SimpleResponse> uploadAttachment(@Body UploadKYCImage uploadKYCImage);

    @GET("RitmanPay/GetCustomerImage2")
    Call<GetProfileImage> getCustomerImage(@Query("Customer_No") String customerNo
            , @Query("PartnerCode") Integer partnerCode, @Query("UserName") String username,
                                           @Query("UserPassword") String password,
                                           @Query("LanguageID") Integer languageId);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Agent/LedgerHistory")
    Call<AEResponse> getMerchantLedger(@Body RequestBody model,
                                       @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Customer/GetBalance")
    Call<GetBalanceCustomerLimit> getBalanceLimitCustomer(@Body RequestBody request, @Header("Username") String userName);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Agent/FundTransferToMerchant")
    Call<SimpleResponse> fundTransferToMerchant(@Body RequestBody request, @Header("Username") String userName);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RPAY/BusBooking/GetBUSStations")
    Call<GetBusStationsResponse> getBusStations(@Body RequestBody request,
                                                @Header("Username") String userName);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RPAY/BusBooking/GetBUSDestinationStations")
    Call<GetBusStationsResponse> getDestinationBus(@Body RequestBody request,
                                                   @Header("Username") String userName);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Agent/GetDistributorMerchants")
    Call<DistributorDetailsResponse> getDistributorMerchants(@Body RequestBody request, @Header("Username") String userName);


    @POST("RitmanPay/AddCustomerImage")
    Call<SimpleResponse> uploadCustomerImage(@Body UploadUserImageRequest request);


    @POST("RitmanPay/Auth/LoginUser")
    Call<SimpleResponse> createMerchant(@Body SimpleRequest credentials,
                                        @Header("Username") String Username, @Header("UserPassword") String UserPassword);

    @POST("RitmanPay/Auth/VerifyOTP")
    Call<SimpleResponse> createotp(@Body OtpRequest credentials,
                                   @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Agent/ChangePassword")
    Call<SimpleResponse> changePassword(@Body RequestBody body,
                                        @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Auth/GetAvailableBalance")
    Call<SimpleResponse> getBalance(@Body RequestBody body, @Header("Username") String userName);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Customer/CustomerRegistration")
    Call<AEResponse> createRegister(@Body RequestBody model,
                                    @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/GetList/GetRelationList")
    Call<RelationListResponse> getRelationList(@Body RequestBody request,
                                               @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/NetworkList/GetBankNetworkList")
    Call<BankListResponse> getBankNetworkList(@Body RequestBody request,
                                              @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Beneficiary/IFSC_Code_By_Bank_Name")
    Call<AEResponse> getBanks(@Body RequestBody request,
                              @Header("key") String key, @Header("secretkey") String sKey);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Beneficiary/BeneficiaryRegistration")
    Call<AEResponse> createBeneficiary(@Body RequestBody model,
                                       @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Beneficiary/GetBeneficiaryList")
    Call<AEResponse> getBeneficiaryListResponse(@Body RequestBody request,
                                                @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Calculate/CalTransfer")
    Call<CaltransferResponse> getCalTransfer(@Body RequestBody requestBody,
                                             @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Transfer/RitmanPaySend")
    Call<AEResponse> getSummary(@Body RequestBody requestBody,
                                @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Transfer/GetTransactionReceipt")
    Call<GetTransactionReceiptResponse> getReciept(@Body RequestBody requestBody,
                                                   @Header("Username") String Username);

    // @POST("RitmanPay/Transfer/TransactionHistory")
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Transfer/TransactionHistoryWithFilter")
    Call<TransactionHistoryResponse> getTransactionHistory(@Body RequestBody requestBody,
                                                           @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Transfer/GetMode")
    Call<AEResponse> getPaymentsModes(@Body RequestBody credentials,
                                      @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Transfer/VerifyBeneficiary")
    Call<AEResponse> verifyBeneficairy(@Body RequestBody requestBody,
                                       @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/WR/GetWRBillerType")
    Call<GetWRBillerTypeResponse> GetWRBillerType(@Body RequestBody requestBody,
                                                  @Header("Username") String Username);

    //Country
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/WR/GetWRCountryList")
    Call<GetWRCountryListResponseC> GetCountryList(@Body RequestBody requestBody,
                                                   @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/WR/GetWRBillerCategory")
    Call<GetWRBillerCategoryResponseC> GetWRBillerCategory(@Body RequestBody requestBody,
                                                           @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/WR/GetWRBillerNames")
    Call<GetWRBillerNamesResponseC> GetWRBillerNames(@Body RequestBody requestBody,
                                                     @Header("Username") String Username);

    //GetWRBillerFields
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/WR/GetWRBillerFields")
    Call<GetWRBillerFieldsResponseN> GetWRBillerFields(@Body RequestBody requestBody,
                                                       @Header("Username") String Username);

    //GetWRBillDetail
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/WR/WRBillDetail")
    Call<GetWRBillDetailResponseN> GetWRBillDetail(@Body RequestBody requestBody,
                                                   @Header("Username") String Username);


    //PayBill
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/WR/WRPayBill")
    Call<BillPayResponse> getBillPay(@Body RequestBody requestBody,
                                     @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/WR/GetWRBillerTypeMobileTopUp")
    Call<MobileTopUpResponse> getMobileTopUp(@Body RequestBody requestBody,
                                             @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/BillPayment/GetPrepaidOperator")
    Call<PrepaidOperatorResponse> getOperator(@Body RequestBody requestBody,
                                              @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/WR/GetWRCountryList")
    Call<WRCountryListResponse> getCountryList(@Body RequestBody requestBody,
                                               @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/BillPayment/GetRechargePlans")
    Call<PrepaidPlanResponse> getPrepaidPlan(@Body RequestBody requestBody,
                                             @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Transfer/TxnRefundHistoryWithFilter")
    Call<TransactionHistoryResponse> getRefundTransactionHistory(@Body RequestBody requestBody,
                                                                 @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/WR/WRPrepaidRecharge")
    Call<SimpleResponse> prepaidRecharge(@Body RequestBody requestBody,
                                         @Header("Username") String Username);


    //GetCashePincodeList
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/GetCashePincodeList")
    Call<PinCodeListResponse> getCashePincodeList(@Body RequestBody requestBody,
                                                  @Header("Username") String Username);

    //GetCasheAccomodationList
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/GetCasheAccomodationList")
    Call<GetCasheAccomodationListResponse> getCasheAccomodationList(@Body RequestBody requestBody,
                                                                    @Header("Username") String Username);

    // GetCasheQualificationList
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/GetCasheQualificationList")
    Call<GetCasheAccomodationListResponse> getCasheQualificationList(@Body RequestBody requestBody,
                                                                     @Header("Username") String Username);

    // GetCasheDesignationList
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/GetCasheDesignationList")
    Call<GetCasheAccomodationListResponse> getCasheDesignationList(@Body RequestBody requestBody,
                                                                   @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Beneficiary/ActivateDeactivate")
    Call<AEResponse> activeDeActiveBeneficiary(@Body RequestBody request, @Header("key") String Username,
                                               @Header("secretkey") String UserPassword);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Agent/ForgetPassword")
    Call<AEResponse> forgotPassword(@Body RequestBody request, @Header("key") String Username,
                                    @Header("secretkey") String UserPassword);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Beneficiary/CreateBeneficiaryOTP")
    Call<AEResponse> createBeneOTP(@Body RequestBody requestBody,
                                   @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Beneficiary/DeactiveList")
    Call<AEResponse> getDeActiveBeneficiary(@Body RequestBody requestBody,
                                            @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Customer/CustomerSupport")
    Call<AEResponse> customerSupportDetails(@Body RequestBody request, @Header("key") String Username,
                                            @Header("secretkey") String UserPassword);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Agent/GetFundingBankDetails")
    Call<AEResponse> getFundingBankDetails(@Body RequestBody request, @Header("key") String Username,
                                           @Header("secretkey") String UserPassword);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Refund/GenerateOTP")
    Call<AEResponse> refundGenerateOTP(@Body RequestBody requestBody,
                                       @Header("key") String key, @Header("secretkey") String sKey);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Transaction/Refund")
    Call<AEResponse> transactionRefund(@Body RequestBody requestBody,
                                       @Header("key") String key, @Header("secretkey") String sKey);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Agent/Verify_eKyc")
    Call<AEResponse> verifyEKYC(@Body RequestBody requestBody,
                                @Header("key") String key, @Header("secretkey") String sKey);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Customer/TransactionHistory")
    Call<AEResponse> customerTransactionHistory(@Body RequestBody request, @Header("key") String Username,
                                                @Header("secretkey") String UserPassword);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Transfer/GetAEPSTransactionReceipt")
    Call<AEResponse> getAEPSReceipt(@Body RequestBody request, @Header("key") String Username,
                                    @Header("secretkey") String UserPassword);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Agent/UploadCashInDetails")
    Call<AEResponse> uploadCashDetails(@Body RequestBody requestBody,
                                       @Header("key") String key, @Header("secretkey") String sKey);


    // GetCasheProductTypes
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/GetCasheProductTypes")
    Call<GetCasheAccomodationListResponse> getCasheProductTypes(@Body RequestBody requestBody,
                                                                @Header("Username") String Username);

    // GetCasheEmploymentType
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/GetCasheEmploymentType")
    Call<GetCasheAccomodationListResponse> getCasheEmploymentType(@Body RequestBody requestBody,
                                                                  @Header("Username") String Username);

    // GetCasheSalaryTypes
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/GetCasheSalaryTypes")
    Call<GetCasheAccomodationListResponse> getCasheSalaryTypes(@Body RequestBody requestBody,
                                                               @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/GetCasheNoOfKids")
    Call<GetCasheAccomodationListResponse> getNoOfKids(@Body RequestBody requestBody,
                                                       @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/GetCasheYearsAtCurrentAdd")
    Call<GetCasheAccomodationListResponse> getCasheYearsAtCurrentAdd(@Body RequestBody requestBody,
                                                                     @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/GetCasheResidingWith")
    Call<GetCasheAccomodationListResponse> getCasheResidingWith(@Body RequestBody requestBody,
                                                                @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/GetCasheWorkSectors")
    Call<GetCasheAccomodationListResponse> getCasheWorkSectors(@Body RequestBody requestBody,
                                                               @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/GetCasheJobFunctions")
    Call<GetCasheAccomodationListResponse> getCasheJobFunctions(@Body RequestBody requestBody,
                                                                @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/GetCasheOrganizations")
    Call<GetCasheAccomodationListResponse> getCasheOrganizations(@Body RequestBody requestBody,
                                                                 @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/BillPayment/GetPlanCategories")
    Call<PlanCategoriesResponse> getPlanName(@Body RequestBody requestBody,
                                             @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/BillPayment/GetRechargePlans")
    Call<RechargePlansResponse> getRechargePlanName(@Body RequestBody requestBody,
                                                    @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/BillPayment/GetBillDetail")
    Call<BillDetailResponse> getBillDetails(@Body RequestBody requestBody,
                                            @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/Run_PreApproval")
    Call<CashePreApprovalResponse> createCustomer(@Body RequestBody requestBody,
                                                  @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/check_Customer_Status")
    Call<CashePreApprovalResponse> getCustomerStatus(@Body RequestBody requestBody,
                                                     @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/BillPayment/PayBillPayment")
    Call<PayBillPaymentResponse> getPayBill(@Body RequestBody requestBody,
                                            @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/IndiaFirst_Life_Group/Hospicare_Member_Form")
    Call<HospicareMemebrFormResponse> getHospicareMemberForm(@Body HospicareMemebrFormRequest hospicarememebrFormrequest,
                                                             @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/Cashe/Upload_KYC_documents")
    Call<UploadDocumentsResponse> uploadDocuments(@Body RequestBody requestBody,
                                                  @Header("Username") String Username);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/OTP/GenerateOTP")
    Call<GetOtpResponse> getOtp(@Body RequestBody requestBody,
                                @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/OTP/VerifyOTP")
    Call<VerifyOtpResponse> verifyOtp(@Body RequestBody requestBody,
                                      @Header("Username") String Username);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/CustomerDevice/OS_Version")
    Call<OsVersionResponse> getOsVersion(@Body OsVersionRequest requestBody);


    //IINData
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RIT/AEPS/IIN_List")
    Call<AEResponse> getIIN(@Body RequestBody requestBody,
                            @Header("key") String key, @Header("secretkey") String sKey);

    //TranceoData
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RIT/AEPS/Transaction")
    Call<AEResponse> getAEPSData(@Body RequestBody requestBody,
                                 @Header("key") String key, @Header("secretkey") String sKey);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/eKYC/YB_Create_Agent")
    Call<AEResponse> YBCreateAgent(@Body RequestBody requestBody,
                                   @Header("key") String key, @Header("secretkey") String sKey);

    //YB_VALIDATE_OTP
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/eKYC/YB_VALIDATE_OTP")
    Call<AEResponse> YBValidateOTP(@Body RequestBody requestBody,
                                   @Header("key") String key, @Header("secretkey") String sKey);

    //Adhar_Bio_kyc
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("RitmanPay/eKYC/YB_Aadhaar_Biometric_KYC")
    Call<AEResponse> YBAdharBioKYC(@Body RequestBody requestBody,
                                   @Header("key") String key, @Header("secretkey") String sKey);
}