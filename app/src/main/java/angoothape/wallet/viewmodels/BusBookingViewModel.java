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
import angoothape.wallet.di.JSONdi.restRequest.GetBUSStationsRequest;
import angoothape.wallet.di.JSONdi.restRequest.GetBusDestinationsRequest;
import angoothape.wallet.di.JSONdi.restResponse.GetBusStationsResponse;
import angoothape.wallet.di.JSONdi.retrofit.RestApi;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusBookingViewModel extends ViewModel {

    RestApi restApi = RestClient.getBase();
    AppExecutors appExecutors = new AppExecutors();

    public LiveData<NetworkResource<GetBusStationsResponse>> getBusStations(GetBUSStationsRequest request, String key) {
        MutableLiveData<NetworkResource<GetBusStationsResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetBusStationsResponse> call = restApi.getBusStations(RestClient.makeGSONRequestBody(request)
                , key); // rest api declaration

        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<GetBusStationsResponse>() {
            @Override
            public void onResponse(Call<GetBusStationsResponse> call1, Response<GetBusStationsResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetBusStationsResponse model = new GetBusStationsResponse();
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
            public void onFailure(Call<GetBusStationsResponse> call1, Throwable t) {
                GetBusStationsResponse temp = new GetBusStationsResponse();
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

    public LiveData<NetworkResource<GetBusStationsResponse>> getBusDestination(GetBusDestinationsRequest request, String key) {
        MutableLiveData<NetworkResource<GetBusStationsResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetBusStationsResponse> call = restApi.getDestinationBus(RestClient.makeGSONRequestBody(request)
                , key); // rest api declaration

        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<GetBusStationsResponse>() {
            @Override
            public void onResponse(Call<GetBusStationsResponse> call1, Response<GetBusStationsResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetBusStationsResponse model = new GetBusStationsResponse();
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
            public void onFailure(Call<GetBusStationsResponse> call1, Throwable t) {
                GetBusStationsResponse temp = new GetBusStationsResponse();
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
