package totipay.wallet.di.retrofit;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import totipay.wallet.di.restResponse.GetProfileImage;
import totipay.wallet.di.restRequest.UploadKYCImage;
import totipay.wallet.di.restRequest.UploadUserImageRequest;
import totipay.wallet.di.restResponse.ResponseApi;


public interface RestApi {

    @POST("TotiPayRestAPI/Images/UploadKYCImage")
    Call<ResponseApi> uploadAttachment(@Body UploadKYCImage uploadKYCImage);

    @GET("TotiPayRestAPI/Images/GetCustomerImage2")
    Call<GetProfileImage> getCustomerImage(@Query("Customer_No") String customerNo
            , @Query("PartnerCode") Integer partnerCode, @Query("UserName") String username,
                                           @Query("UserPassword") String password,
     @Query("LanguageID") Integer languageId);


//    @HTTP(method = "GET", path = "TotiPayRestAPI/Images/GetCustomerImage", hasBody = true)
//    Call<GetProfileImage> getCustomerImage(@Body GetCustomerProfileImageRequest request);

    @POST("TotiPayRestAPI/Images/AddCustomerImage")
    Call<ResponseApi> uploadCustomerImage(@Body UploadUserImageRequest request);

}