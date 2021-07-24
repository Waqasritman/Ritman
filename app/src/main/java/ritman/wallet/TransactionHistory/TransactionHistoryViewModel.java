package ritman.wallet.TransactionHistory;

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
import ritman.wallet.adapters.TransactionHistoryAdapter;
import ritman.wallet.di.JSONdi.AppExecutors;
import ritman.wallet.di.JSONdi.NetworkResource;
import ritman.wallet.di.JSONdi.restRequest.SimpleRequest;
import ritman.wallet.di.JSONdi.retrofit.RestApi;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.di.XMLdi.ResponseHelper.TransactionHistoryResponse;

public class TransactionHistoryViewModel extends ViewModel {

    RestApi restApi = RestClient.get();
    AppExecutors appExecutors = new AppExecutors();

    public LiveData<NetworkResource<TransactionHistoryResponse>> getTransactionHistory(SimpleRequest request, String userName) {

        MutableLiveData<NetworkResource<TransactionHistoryResponse>> data = new MutableLiveData<>();
        Call<TransactionHistoryResponse> call =
                restApi.getTransactionHistory(RestClient.makeGSONRequestBody(request) , userName);
        appExecutors.networkIO().execute(() ->
                call.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call1, Response<TransactionHistoryResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        TransactionHistoryResponse model = new TransactionHistoryResponse();
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
            public void onFailure(Call<TransactionHistoryResponse> call1, Throwable t) {
                TransactionHistoryResponse temp = new TransactionHistoryResponse();
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
