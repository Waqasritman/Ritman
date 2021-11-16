package angoothape.wallet.beneficairyRegistration.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;

import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.ActiveDeActiveBeneRequest;
import angoothape.wallet.di.JSONdi.restResponse.AEResponse;
import angoothape.wallet.di.JSONdi.restResponse.GetBalanceCustomerLimit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import angoothape.wallet.R;
import angoothape.wallet.di.JSONdi.AppExecutors;
import angoothape.wallet.di.JSONdi.NetworkResource;
import angoothape.wallet.di.JSONdi.restRequest.GetBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restRequest.RegisterBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restRequest.VerifyBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restResponse.VerifyBeneficiary;
import angoothape.wallet.di.JSONdi.retrofit.RestApi;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;

public class RegisterBeneficiaryViewModel extends ViewModel {
    public MutableLiveData<RegisterBeneficiaryRequest> beneRegister = new MutableLiveData<>();

    RestApi restApii = RestClient.get();
    RestApi restApiii = RestClient.getREST();
    AppExecutors appExecutors = new AppExecutors();

    public LiveData<NetworkResource<AEResponse>> createBeneficairyOTP(AERequest aeRequest, String key
            , String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>();
        Call<AEResponse> call = restApiii.createBeneOTP(RestClient.makeGSONRequestBody(aeRequest)
                , key, sKey);
        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");

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

    public LiveData<NetworkResource<AEResponse>> activeDeActiveBeneficiary(AERequest aeRequest, String key
            , String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>();
        Call<AEResponse> call = restApiii.activeDeActiveBeneficiary(RestClient.makeGSONRequestBody(aeRequest)
                , key, sKey);
        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");

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

    public LiveData<NetworkResource<AEResponse>> createBeneficairy(AERequest aeRequest, String key
            , String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>();
        Call<AEResponse> call = restApiii.createBeneficiary(RestClient.makeGSONRequestBody(aeRequest)
                , key, sKey);
        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");

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

    public LiveData<NetworkResource<GetBalanceCustomerLimit>> getBalanceLimitForCustomer(GetBeneficiaryRequest request,
                                                                            String key) {
        MutableLiveData<NetworkResource<GetBalanceCustomerLimit>> data = new MutableLiveData<>();
        Call<GetBalanceCustomerLimit> call = restApii.getBalanceLimitCustomer(RestClient.makeGSONRequestBody(request)
                , key);
        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<GetBalanceCustomerLimit>() {
            @Override
            public void onResponse(Call<GetBalanceCustomerLimit> call1, Response<GetBalanceCustomerLimit> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");

                        GetBalanceCustomerLimit model = new GetBalanceCustomerLimit();
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
            public void onFailure(Call<GetBalanceCustomerLimit> call1, Throwable t) {
                GetBalanceCustomerLimit temp = new GetBalanceCustomerLimit();
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

    public LiveData<NetworkResource<AEResponse>> getBeneficiaryList(AERequest request,
                                                                    String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>();
        Call<AEResponse> call = restApiii.getBeneficiaryListResponse(RestClient.makeGSONRequestBody(request)
                , key, sKey);
        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");

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



    public LiveData<NetworkResource<AEResponse>>  verifyBeneficairy(AERequest request, String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>();
        Call<AEResponse> call = restApiii.verifyBeneficairy(RestClient.makeGSONRequestBody(request)
                , key, sKey);
        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call, Response<AEResponse> response) {
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
            public void onFailure(Call<AEResponse> call, Throwable t) {
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


    public LiveData<NetworkResource<AEResponse>> getDeActiveBenficiary(AERequest request,
                                                                       String key, String sKey) {
        MutableLiveData<NetworkResource<AEResponse>> data = new MutableLiveData<>();
        Call<AEResponse> call = restApiii.getDeActiveBeneficiary(RestClient.makeGSONRequestBody(request)
                , key, sKey);
        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call1, Response<AEResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");

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
