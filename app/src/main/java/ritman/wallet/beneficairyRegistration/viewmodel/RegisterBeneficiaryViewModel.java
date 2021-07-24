package ritman.wallet.beneficairyRegistration.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ritman.wallet.R;
import ritman.wallet.di.JSONdi.AppExecutors;
import ritman.wallet.di.JSONdi.NetworkResource;
import ritman.wallet.di.JSONdi.restRequest.GetBeneficiaryRequest;
import ritman.wallet.di.JSONdi.restRequest.RegisterBeneficiaryRequest;
import ritman.wallet.di.JSONdi.restRequest.VerifyBeneficiaryRequest;
import ritman.wallet.di.JSONdi.restResponse.VerifyBeneficiary;
import ritman.wallet.di.JSONdi.retrofit.RestApi;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import ritman.wallet.di.generic_response.SimpleResponse;

public class RegisterBeneficiaryViewModel extends ViewModel {
    public MutableLiveData<RegisterBeneficiaryRequest> beneRegister = new MutableLiveData<>();
    RestApi restApi = RestClient.get();
    AppExecutors appExecutors = new AppExecutors();

    public LiveData<NetworkResource<SimpleResponse>> createBeneficairy(String userName){
        MutableLiveData<NetworkResource<SimpleResponse>> data = new MutableLiveData<>();
        Call<SimpleResponse> call = restApi.createBeneficiary(beneRegister.getValue()
         , userName);
        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call1, Response<SimpleResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");

                        SimpleResponse model = new SimpleResponse();
                        model.responseCode = 500;
                        model.description = message;
                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if(response.isSuccessful()) {
                    data.postValue(NetworkResource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call1, Throwable t) {
                SimpleResponse temp = new SimpleResponse();
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


    public LiveData<NetworkResource<GetBeneficiaryListResponse>> getBeneficiaryList(GetBeneficiaryRequest request ,  String userName){
        MutableLiveData<NetworkResource<GetBeneficiaryListResponse>> data = new MutableLiveData<>();
        Call<GetBeneficiaryListResponse> call = restApi.getBeneficiaryListResponse(RestClient.makeGSONRequestBody(request)
                , userName);
        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<GetBeneficiaryListResponse>() {
            @Override
            public void onResponse(Call<GetBeneficiaryListResponse> call1, Response<GetBeneficiaryListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");

                        GetBeneficiaryListResponse model = new GetBeneficiaryListResponse();
                        model.responseCode = 500;
                        model.description = message;
                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if(response.isSuccessful()) {
                    data.postValue(NetworkResource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<GetBeneficiaryListResponse> call1, Throwable t) {
                GetBeneficiaryListResponse temp = new GetBeneficiaryListResponse();
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


    public LiveData<NetworkResource<VerifyBeneficiary>>
    verifyBeneficairy(VerifyBeneficiaryRequest request , String userName){
        MutableLiveData<NetworkResource<VerifyBeneficiary>> data = new MutableLiveData<>();
        Call<VerifyBeneficiary> call = restApi.verifyBeneficairy(RestClient.makeGSONRequestBody(request)
                , userName);
        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<VerifyBeneficiary>() {
            @Override
            public void onResponse(Call<VerifyBeneficiary> call, Response<VerifyBeneficiary> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        VerifyBeneficiary model = new VerifyBeneficiary();
                        model.responseCode = 500;
                        model.description = message;
                        data.postValue(NetworkResource.unSuccess(R.string.error_fatal, model));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if(response.isSuccessful()) {
                    data.postValue(NetworkResource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<VerifyBeneficiary> call, Throwable t) {
                VerifyBeneficiary temp = new VerifyBeneficiary();
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
