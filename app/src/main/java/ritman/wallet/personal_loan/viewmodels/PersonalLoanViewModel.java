package ritman.wallet.personal_loan.viewmodels;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.ConnectException;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Response;
import ritman.wallet.R;
import ritman.wallet.di.JSONdi.AppExecutors;
import ritman.wallet.di.JSONdi.NetworkResource;
import ritman.wallet.di.JSONdi.restRequest.CashePreApprovalRequest;
import ritman.wallet.di.JSONdi.restRequest.CheckCustomerStatusRequest;
import ritman.wallet.di.JSONdi.restRequest.PinCodeListRequest;
import ritman.wallet.di.JSONdi.restRequest.SimpleRequest;
import ritman.wallet.di.JSONdi.restRequest.UploadDocumentsRequest;
import ritman.wallet.di.JSONdi.restResponse.CashePreApprovalResponse;
import ritman.wallet.di.JSONdi.restResponse.GetCasheAccomodationListResponse;
import ritman.wallet.di.JSONdi.restResponse.PinCodeListResponse;
import ritman.wallet.di.JSONdi.restResponse.UploadDocumentsResponse;
import ritman.wallet.di.JSONdi.retrofit.RestApi;
import ritman.wallet.di.JSONdi.retrofit.RestClient;

public class PersonalLoanViewModel extends ViewModel {
    String amesg;
    RestApi restApi = RestClient.get();
    AppExecutors appExecutors = new AppExecutors();

    //store data in object
    public MutableLiveData<String> city = new MutableLiveData<>();
    public MutableLiveData<String> state = new MutableLiveData<>();

    public LiveData<NetworkResource<PinCodeListResponse>> getCashePincodeList (PinCodeListRequest request , String userName){
        MutableLiveData<NetworkResource<PinCodeListResponse>> data = new MutableLiveData<>(); // receiving
        Call<PinCodeListResponse> call = restApi.getCashePincodeList(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<PinCodeListResponse>() {
            @Override
            public void onResponse(Call<PinCodeListResponse> call1, Response<PinCodeListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        PinCodeListResponse model = new PinCodeListResponse();
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
            public void onFailure(Call<PinCodeListResponse> call1, Throwable t) {
                PinCodeListResponse temp = new PinCodeListResponse();
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

    //GetCasheAccomodationList
    public LiveData<NetworkResource<GetCasheAccomodationListResponse>> getCasheAccomodationList (SimpleRequest request , String userName){
        MutableLiveData<NetworkResource<GetCasheAccomodationListResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetCasheAccomodationListResponse> call = restApi.getCasheAccomodationList(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetCasheAccomodationListResponse>() {
            @Override
            public void onResponse(Call<GetCasheAccomodationListResponse> call1, Response<GetCasheAccomodationListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetCasheAccomodationListResponse model = new GetCasheAccomodationListResponse();
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
            public void onFailure(Call<GetCasheAccomodationListResponse> call1, Throwable t) {
                GetCasheAccomodationListResponse temp = new GetCasheAccomodationListResponse();
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


    //GetCasheQualificationList
    public LiveData<NetworkResource<GetCasheAccomodationListResponse>> getCasheQualificationList (SimpleRequest request , String userName){
        MutableLiveData<NetworkResource<GetCasheAccomodationListResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetCasheAccomodationListResponse> call = restApi.getCasheQualificationList(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetCasheAccomodationListResponse>() {
            @Override
            public void onResponse(Call<GetCasheAccomodationListResponse> call1, Response<GetCasheAccomodationListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetCasheAccomodationListResponse model = new GetCasheAccomodationListResponse();
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
            public void onFailure(Call<GetCasheAccomodationListResponse> call1, Throwable t) {
                GetCasheAccomodationListResponse temp = new GetCasheAccomodationListResponse();
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

    //GetCasheDesignationList
    public LiveData<NetworkResource<GetCasheAccomodationListResponse>> getCasheDesignationList (SimpleRequest request , String userName){
        MutableLiveData<NetworkResource<GetCasheAccomodationListResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetCasheAccomodationListResponse> call = restApi.getCasheDesignationList(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetCasheAccomodationListResponse>() {
            @Override
            public void onResponse(Call<GetCasheAccomodationListResponse> call1, Response<GetCasheAccomodationListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetCasheAccomodationListResponse model = new GetCasheAccomodationListResponse();
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
            public void onFailure(Call<GetCasheAccomodationListResponse> call1, Throwable t) {
                GetCasheAccomodationListResponse temp = new GetCasheAccomodationListResponse();
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

    //GetCasheProductTypes
    public LiveData<NetworkResource<GetCasheAccomodationListResponse>> getCasheProductTypes (SimpleRequest request , String userName){
        MutableLiveData<NetworkResource<GetCasheAccomodationListResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetCasheAccomodationListResponse> call = restApi.getCasheProductTypes(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetCasheAccomodationListResponse>() {
            @Override
            public void onResponse(Call<GetCasheAccomodationListResponse> call1, Response<GetCasheAccomodationListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetCasheAccomodationListResponse model = new GetCasheAccomodationListResponse();
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
            public void onFailure(Call<GetCasheAccomodationListResponse> call1, Throwable t) {
                GetCasheAccomodationListResponse temp = new GetCasheAccomodationListResponse();
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

    //GetCasheEmploymentTypec
    public LiveData<NetworkResource<GetCasheAccomodationListResponse>> getCasheEmploymentType (SimpleRequest request , String userName){
        MutableLiveData<NetworkResource<GetCasheAccomodationListResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetCasheAccomodationListResponse> call = restApi.getCasheEmploymentType(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetCasheAccomodationListResponse>() {
            @Override
            public void onResponse(Call<GetCasheAccomodationListResponse> call1, Response<GetCasheAccomodationListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetCasheAccomodationListResponse model = new GetCasheAccomodationListResponse();
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
            public void onFailure(Call<GetCasheAccomodationListResponse> call1, Throwable t) {
                GetCasheAccomodationListResponse temp = new GetCasheAccomodationListResponse();
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

    //GetCasheSalaryTypes
    public LiveData<NetworkResource<GetCasheAccomodationListResponse>> getCasheSalaryTypes (SimpleRequest request , String userName){
        MutableLiveData<NetworkResource<GetCasheAccomodationListResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetCasheAccomodationListResponse> call = restApi.getCasheSalaryTypes(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetCasheAccomodationListResponse>() {
            @Override
            public void onResponse(Call<GetCasheAccomodationListResponse> call1, Response<GetCasheAccomodationListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetCasheAccomodationListResponse model = new GetCasheAccomodationListResponse();
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
            public void onFailure(Call<GetCasheAccomodationListResponse> call1, Throwable t) {
                GetCasheAccomodationListResponse temp = new GetCasheAccomodationListResponse();
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

    public LiveData<NetworkResource<GetCasheAccomodationListResponse>> getNoOfKids (SimpleRequest request , String userName){
        MutableLiveData<NetworkResource<GetCasheAccomodationListResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetCasheAccomodationListResponse> call = restApi.getNoOfKids(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetCasheAccomodationListResponse>() {
            @Override
            public void onResponse(Call<GetCasheAccomodationListResponse> call1, Response<GetCasheAccomodationListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetCasheAccomodationListResponse model = new GetCasheAccomodationListResponse();
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
            public void onFailure(Call<GetCasheAccomodationListResponse> call1, Throwable t) {
                GetCasheAccomodationListResponse temp = new GetCasheAccomodationListResponse();
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



    public LiveData<NetworkResource<GetCasheAccomodationListResponse>> getCasheResidingWith (SimpleRequest request , String userName){
        MutableLiveData<NetworkResource<GetCasheAccomodationListResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetCasheAccomodationListResponse> call = restApi.getCasheResidingWith(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetCasheAccomodationListResponse>() {
            @Override
            public void onResponse(Call<GetCasheAccomodationListResponse> call1, Response<GetCasheAccomodationListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetCasheAccomodationListResponse model = new GetCasheAccomodationListResponse();
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
            public void onFailure(Call<GetCasheAccomodationListResponse> call1, Throwable t) {
                GetCasheAccomodationListResponse temp = new GetCasheAccomodationListResponse();
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

    public LiveData<NetworkResource<GetCasheAccomodationListResponse>> getCasheYearsAtCurrentAdd (SimpleRequest request , String userName){
        MutableLiveData<NetworkResource<GetCasheAccomodationListResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetCasheAccomodationListResponse> call = restApi.getCasheYearsAtCurrentAdd(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetCasheAccomodationListResponse>() {
            @Override
            public void onResponse(Call<GetCasheAccomodationListResponse> call1, Response<GetCasheAccomodationListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetCasheAccomodationListResponse model = new GetCasheAccomodationListResponse();
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
            public void onFailure(Call<GetCasheAccomodationListResponse> call1, Throwable t) {
                GetCasheAccomodationListResponse temp = new GetCasheAccomodationListResponse();
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

    public LiveData<NetworkResource<GetCasheAccomodationListResponse>> getCasheWorkSectors (SimpleRequest request , String userName){
        MutableLiveData<NetworkResource<GetCasheAccomodationListResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetCasheAccomodationListResponse> call = restApi.getCasheWorkSectors(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetCasheAccomodationListResponse>() {
            @Override
            public void onResponse(Call<GetCasheAccomodationListResponse> call1, Response<GetCasheAccomodationListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetCasheAccomodationListResponse model = new GetCasheAccomodationListResponse();
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
            public void onFailure(Call<GetCasheAccomodationListResponse> call1, Throwable t) {
                GetCasheAccomodationListResponse temp = new GetCasheAccomodationListResponse();
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


    public LiveData<NetworkResource<GetCasheAccomodationListResponse>> getCasheJobFunctions (SimpleRequest request , String userName){
        MutableLiveData<NetworkResource<GetCasheAccomodationListResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetCasheAccomodationListResponse> call = restApi.getCasheJobFunctions(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetCasheAccomodationListResponse>() {
            @Override
            public void onResponse(Call<GetCasheAccomodationListResponse> call1, Response<GetCasheAccomodationListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetCasheAccomodationListResponse model = new GetCasheAccomodationListResponse();
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
            public void onFailure(Call<GetCasheAccomodationListResponse> call1, Throwable t) {
                GetCasheAccomodationListResponse temp = new GetCasheAccomodationListResponse();
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


    public LiveData<NetworkResource<GetCasheAccomodationListResponse>> getCasheOrganizations (SimpleRequest request , String userName){
        MutableLiveData<NetworkResource<GetCasheAccomodationListResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetCasheAccomodationListResponse> call = restApi.getCasheOrganizations(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetCasheAccomodationListResponse>() {
            @Override
            public void onResponse(Call<GetCasheAccomodationListResponse> call1, Response<GetCasheAccomodationListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetCasheAccomodationListResponse model = new GetCasheAccomodationListResponse();
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
            public void onFailure(Call<GetCasheAccomodationListResponse> call1, Throwable t) {
                GetCasheAccomodationListResponse temp = new GetCasheAccomodationListResponse();
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


    public LiveData<NetworkResource<UploadDocumentsResponse>> uploadDocuments (UploadDocumentsRequest request , String userName){
        MutableLiveData<NetworkResource<UploadDocumentsResponse>> data = new MutableLiveData<>(); // receiving
        Call<UploadDocumentsResponse> call = restApi.uploadDocuments(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<UploadDocumentsResponse>() {
            @Override
            public void onResponse(Call<UploadDocumentsResponse> call1, Response<UploadDocumentsResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        UploadDocumentsResponse model = new UploadDocumentsResponse();
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
            public void onFailure(Call<UploadDocumentsResponse> call1, Throwable t) {
                UploadDocumentsResponse temp = new UploadDocumentsResponse();
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



    public LiveData<NetworkResource<CashePreApprovalResponse>> createCustomer (CashePreApprovalRequest request , String userName){
        MutableLiveData<NetworkResource<CashePreApprovalResponse>> data = new MutableLiveData<>(); // receiving
        Call<CashePreApprovalResponse> call = restApi.createCustomer(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<CashePreApprovalResponse>() {
            @Override
            public void onResponse(Call<CashePreApprovalResponse> call1, Response<CashePreApprovalResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        CashePreApprovalResponse model = new CashePreApprovalResponse();
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
            public void onFailure(Call<CashePreApprovalResponse> call1, Throwable t) {
                CashePreApprovalResponse temp = new CashePreApprovalResponse();
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


    public LiveData<NetworkResource<CashePreApprovalResponse>> getCustomerStatus (CheckCustomerStatusRequest request , String userName){
        MutableLiveData<NetworkResource<CashePreApprovalResponse>> data = new MutableLiveData<>(); // receiving
        Call<CashePreApprovalResponse> call = restApi.getCustomerStatus(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<CashePreApprovalResponse>() {
            @Override
            public void onResponse(Call<CashePreApprovalResponse> call1, Response<CashePreApprovalResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        CashePreApprovalResponse model = new CashePreApprovalResponse();
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
            public void onFailure(Call<CashePreApprovalResponse> call1, Throwable t) {
                CashePreApprovalResponse temp = new CashePreApprovalResponse();
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

