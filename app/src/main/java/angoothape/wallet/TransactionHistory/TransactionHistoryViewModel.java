package angoothape.wallet.TransactionHistory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;

import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.TransactionHistoryRequest;
import angoothape.wallet.di.JSONdi.restResponse.AEResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import angoothape.wallet.R;
import angoothape.wallet.di.JSONdi.AppExecutors;
import angoothape.wallet.di.JSONdi.NetworkResource;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.retrofit.RestApi;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.XMLdi.ResponseHelper.TransactionHistoryResponse;

public class TransactionHistoryViewModel extends ViewModel {

    RestApi restApi = RestClient.get();
    RestApi restApii = RestClient.getEKYC();
    RestApi restApiii = RestClient.getEKYC();
    AppExecutors appExecutors = new AppExecutors();

    public LiveData<NetworkResource<TransactionHistoryResponse>> getTransactionHistory(TransactionHistoryRequest request,
                                                                                       String userName) {
        MutableLiveData<NetworkResource<TransactionHistoryResponse>> data = new MutableLiveData<>();
        Call<TransactionHistoryResponse> call =
                restApi.getTransactionHistory(RestClient.makeGSONRequestBody(request), userName);
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


    public LiveData<NetworkResource<TransactionHistoryResponse>> getRefundTransactionHistory(TransactionHistoryRequest request,
                                                                                             String userName) {
        MutableLiveData<NetworkResource<TransactionHistoryResponse>> data = new MutableLiveData<>();
        Call<TransactionHistoryResponse> call =
                restApi.getRefundTransactionHistory(RestClient.makeGSONRequestBody(request), userName);
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

    public LiveData<NetworkResource<AEResponse>> getCustomerTransactionHistory(AERequest request,
                                                                               String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>();
        Call<AEResponse> call =
                restApiii.customerTransactionHistory(RestClient.makeGSONRequestBody(request), key
                        , sKey);
        appExecutors.networkIO().execute(() ->
                call.enqueue(new Callback<AEResponse>() {
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

    public LiveData<NetworkResource<AEResponse>> getAEPSReceipt(AERequest request, String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>();
        Call<AEResponse> call =
                restApiii.getAEPSReceipt(RestClient.makeGSONRequestBody(request), key
                        , sKey);
        appExecutors.networkIO().execute(() ->
                call.enqueue(new Callback<AEResponse>() {
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
