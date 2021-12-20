package angoothape.wallet.ekyc.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;

import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restResponse.AEResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import angoothape.wallet.R;
import angoothape.wallet.di.JSONdi.AppExecutors;
import angoothape.wallet.di.JSONdi.NetworkResource;
import angoothape.wallet.di.JSONdi.restRequest.AdharBioKycRequest;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerFieldsRequestN;
import angoothape.wallet.di.JSONdi.restRequest.ValidateOtpRequest;
import angoothape.wallet.di.JSONdi.restRequest.YBCreateAgentRequest;
import angoothape.wallet.di.JSONdi.restResponse.AdharBioKycResponse;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerFieldsResponseN;
import angoothape.wallet.di.JSONdi.restResponse.ValidateOtpResponse;
import angoothape.wallet.di.JSONdi.restResponse.YBCreateAgentResponse;
import angoothape.wallet.di.JSONdi.retrofit.RestApi;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.XMLdi.RequestHelper.TootiPayRequest;

public class EKYCViewModel extends ViewModel {
    //to use to store selcted beneficairy

    public TootiPayRequest request = new TootiPayRequest();
    RestApi restApi = RestClient.getEKYC();
    AppExecutors appExecutors = new AppExecutors();
    public MutableLiveData<String> mobile = new MutableLiveData<>();
    public MutableLiveData<String> otpToken = new MutableLiveData<>();
    public MutableLiveData<String> wadh = new MutableLiveData<>();
    public MutableLiveData<String> kycToken = new MutableLiveData<>();


    //YB_Create_Agent
    public LiveData<NetworkResource<AEResponse>> YBCreateAgent(AERequest request, String key
            , String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.YBCreateAgent(RestClient.makeGSONRequestBody(request)
                , key, sKey); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        AEResponse model = new AEResponse();
                        model.responseCode = 500;
                        model.description = message;
                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.isSuccessful()) {
                    data.postValue(NetworkResource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<AEResponse> call1, Throwable t) {
                AEResponse temp = new AEResponse();
                if (t instanceof ConnectException) {
                    // COMPLETED handle no internet access case

                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
                } else if (t instanceof IOException) {
                    // handle server error case
                    data.postValue(NetworkResource.error(R.string.error_server, temp));
                } else {
                    // serialization case, throw abort message (maybe save crash log)
                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
                }
            }
        }));
        return data;
    }

    //YB_VALIDATE_OTP
    public LiveData<NetworkResource<AEResponse>> YBValidateOTP(AERequest request, String key
            , String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.YBValidateOTP(RestClient.makeGSONRequestBody(request)
                , key, sKey); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        AEResponse model = new AEResponse();
                        model.responseCode = 500;
                        model.description = message;
                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.isSuccessful()) {
                    data.postValue(NetworkResource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<AEResponse> call1, Throwable t) {
                AEResponse temp = new AEResponse();
                if (t instanceof ConnectException) {
                    // COMPLETED handle no internet access case

                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
                } else if (t instanceof IOException) {
                    // handle server error case
                    data.postValue(NetworkResource.error(R.string.error_server, temp));
                } else {
                    // serialization case, throw abort message (maybe save crash log)
                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
                }
            }
        }));
        return data;
    }


    //GetBioData
    public LiveData<NetworkResource<AEResponse>> YBAdharBioKYC(AERequest request, String key
            , String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>(); // receiving
        Call<AEResponse> call = restApi.YBAdharBioKYC(RestClient.makeGSONRequestBody(request)
                , key, sKey); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        AEResponse model = new AEResponse();
                        model.responseCode = 500;
                        model.description = message;
                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.isSuccessful()) {
                    data.postValue(NetworkResource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<AEResponse> call1, Throwable t) {
                AEResponse temp = new AEResponse();
                if (t instanceof ConnectException) {
                    // COMPLETED handle no internet access case

                    data.postValue(NetworkResource.error(R.string.error_internet, temp));
                } else if (t instanceof IOException) {
                    // handle server error case
                    data.postValue(NetworkResource.error(R.string.error_server, temp));
                } else {
                    // serialization case, throw abort message (maybe save crash log)
                    data.postValue(NetworkResource.error(R.string.error_fatal, temp));
                }
            }
        }));
        return data;
    }

}
