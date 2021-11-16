package angoothape.wallet.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;

import angoothape.wallet.R;
import angoothape.wallet.di.JSONdi.AppExecutors;
import angoothape.wallet.di.JSONdi.NetworkResource;
import angoothape.wallet.di.JSONdi.restRequest.FundTransferToMerchantRequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restResponse.DistributorDetailsResponse;
import angoothape.wallet.di.JSONdi.retrofit.RestApi;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.generic_response.SimpleResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FundTransferViewModel extends ViewModel {

    RestApi restApi = RestClient.get();
    AppExecutors appExecutors = new AppExecutors();

    public LiveData<NetworkResource<SimpleResponse>> fundTransferToMerchant(FundTransferToMerchantRequest request, String key) {
        MutableLiveData<NetworkResource<SimpleResponse>> data = new MutableLiveData<>(); // receiving
        Call<SimpleResponse> call = restApi.fundTransferToMerchant(RestClient.makeGSONRequestBody(request)
                , key); // rest api declaration

        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call1, Response<SimpleResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        SimpleResponse model = new SimpleResponse();
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

    public LiveData<NetworkResource<DistributorDetailsResponse>> getDistributorMerchants(SimpleRequest request, String key) {
        MutableLiveData<NetworkResource<DistributorDetailsResponse>> data = new MutableLiveData<>(); // receiving
        Call<DistributorDetailsResponse> call = restApi.getDistributorMerchants(RestClient.makeGSONRequestBody(request)
                , key); // rest api declaration

        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<DistributorDetailsResponse>() {
            @Override
            public void onResponse(Call<DistributorDetailsResponse> call1, Response<DistributorDetailsResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        DistributorDetailsResponse model = new DistributorDetailsResponse();
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
            public void onFailure(Call<DistributorDetailsResponse> call1, Throwable t) {
                DistributorDetailsResponse temp = new DistributorDetailsResponse();
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
