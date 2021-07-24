package ritman.wallet.billpayment.viewmodel;
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
import ritman.wallet.di.JSONdi.restRequest.BillDetailRequest;
import ritman.wallet.di.JSONdi.restRequest.BillPayRequest;
import ritman.wallet.di.JSONdi.restRequest.CalTransferRequest;
import ritman.wallet.di.JSONdi.restRequest.Credentials;
import ritman.wallet.di.JSONdi.restRequest.GetWRBillDetailRequestN;
import ritman.wallet.di.JSONdi.restRequest.GetWRBillerCategoryRequestC;
import ritman.wallet.di.JSONdi.restRequest.GetWRBillerFieldsRequestN;
import ritman.wallet.di.JSONdi.restRequest.GetWRBillerNamesRequestC;
import ritman.wallet.di.JSONdi.restRequest.GetWRBillerTypeRequestNew;
import ritman.wallet.di.JSONdi.restRequest.GetWRCountryListRequestC;
import ritman.wallet.di.JSONdi.restRequest.MobileTopUpRequest;
import ritman.wallet.di.JSONdi.restRequest.PayBillPaymentRequest;
import ritman.wallet.di.JSONdi.restRequest.PrepaidOperatorRequest;
import ritman.wallet.di.JSONdi.restRequest.PrepaidPlanRequest;
import ritman.wallet.di.JSONdi.restRequest.RitmanPaySendRequest;
import ritman.wallet.di.JSONdi.restRequest.TransactionRecieptRequest;
import ritman.wallet.di.JSONdi.restResponse.BillDetailResponse;
import ritman.wallet.di.JSONdi.restResponse.BillPayResponse;
import ritman.wallet.di.JSONdi.restResponse.CaltransferResponse;
import ritman.wallet.di.JSONdi.restResponse.GetTransactionReceiptResponse;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillDetailResponseN;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerFieldsResponseN;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerTypeResponse;
import ritman.wallet.di.JSONdi.restResponse.GetWRCountryListResponseC;
import ritman.wallet.di.JSONdi.restResponse.MobileTopUpResponse;
import ritman.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import ritman.wallet.di.JSONdi.restResponse.PrepaidOperatorResponse;
import ritman.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import ritman.wallet.di.JSONdi.restResponse.RitmanPaySendResponse;
import ritman.wallet.di.JSONdi.retrofit.RestApi;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.di.XMLdi.RequestHelper.BeneficiaryAddRequest;
import ritman.wallet.di.XMLdi.RequestHelper.TootiPayRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.GetBankNetworkListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.WRCountryListResponse;

public class BillPaymentViewModel extends ViewModel {

    public MutableLiveData<CalTransferRequest> convertCurrency = new MutableLiveData<>();
    public GetBeneficiaryListResponse beneficiaryDetails = new GetBeneficiaryListResponse();
    public BeneficiaryAddRequest beneficiaryAddRequest = new BeneficiaryAddRequest();

    public TootiPayRequest request = new TootiPayRequest();
    RestApi restApi = RestClient.getBase();

    AppExecutors appExecutors = new AppExecutors();

    public LiveData<NetworkResource<GetWRBillerTypeResponse>> GetWRBillerType(GetWRBillerTypeRequestNew request, String userName) {
        MutableLiveData<NetworkResource<GetWRBillerTypeResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetWRBillerTypeResponse> call = restApi.GetWRBillerType(RestClient.makeGSONRequestBody(request)
                , userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetWRBillerTypeResponse>() {
            @Override
            public void onResponse(Call<GetWRBillerTypeResponse> call1, Response<GetWRBillerTypeResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetWRBillerTypeResponse model = new GetWRBillerTypeResponse();
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
            public void onFailure(Call<GetWRBillerTypeResponse> call1, Throwable t) {
                GetWRBillerTypeResponse temp = new GetWRBillerTypeResponse();
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

    //CountriList
    public LiveData<NetworkResource<GetWRCountryListResponseC>> GetCountryList(GetWRCountryListRequestC request, String userName) {
        MutableLiveData<NetworkResource<GetWRCountryListResponseC>> data = new MutableLiveData<>(); // receiving
        Call<GetWRCountryListResponseC> call = restApi.GetCountryList(RestClient.makeGSONRequestBody(request)
                , userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetWRCountryListResponseC>() {
            @Override
            public void onResponse(Call<GetWRCountryListResponseC> call1, Response<GetWRCountryListResponseC> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetWRCountryListResponseC model = new GetWRCountryListResponseC();
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
            public void onFailure(Call<GetWRCountryListResponseC> call1, Throwable t) {
                GetWRCountryListResponseC temp = new GetWRCountryListResponseC();
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


    //BillerCategory
    public LiveData<NetworkResource<GetWRBillerCategoryResponseC>> GetWRBillerCategory(GetWRBillerCategoryRequestC request, String userName) {
        MutableLiveData<NetworkResource<GetWRBillerCategoryResponseC>> data = new MutableLiveData<>(); // receiving
        Call<GetWRBillerCategoryResponseC> call = restApi.GetWRBillerCategory(RestClient.makeGSONRequestBody(request)
                , userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetWRBillerCategoryResponseC>() {
            @Override
            public void onResponse(Call<GetWRBillerCategoryResponseC> call1, Response<GetWRBillerCategoryResponseC> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetWRBillerCategoryResponseC model = new GetWRBillerCategoryResponseC();
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
            public void onFailure(Call<GetWRBillerCategoryResponseC> call1, Throwable t) {
                GetWRBillerCategoryResponseC temp = new GetWRBillerCategoryResponseC();
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

    //GetWRBillerNames
    public LiveData<NetworkResource<GetWRBillerNamesResponseC>> GetWRBillerNames(GetWRBillerNamesRequestC request, String userName) {
        MutableLiveData<NetworkResource<GetWRBillerNamesResponseC>> data = new MutableLiveData<>(); // receiving
        Call<GetWRBillerNamesResponseC> call = restApi.GetWRBillerNames(RestClient.makeGSONRequestBody(request)
                , userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetWRBillerNamesResponseC>() {
            @Override
            public void onResponse(Call<GetWRBillerNamesResponseC> call1, Response<GetWRBillerNamesResponseC> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetWRBillerNamesResponseC model = new GetWRBillerNamesResponseC();
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
            public void onFailure(Call<GetWRBillerNamesResponseC> call1, Throwable t) {
                GetWRBillerNamesResponseC temp = new GetWRBillerNamesResponseC();
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

    //GetWRBillerFields
    public LiveData<NetworkResource<GetWRBillerFieldsResponseN>> GetWRBillerFields(GetWRBillerFieldsRequestN request, String userName) {
        MutableLiveData<NetworkResource<GetWRBillerFieldsResponseN>> data = new MutableLiveData<>(); // receiving
        Call<GetWRBillerFieldsResponseN> call = restApi.GetWRBillerFields(RestClient.makeGSONRequestBody(request)
                , userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetWRBillerFieldsResponseN>() {
            @Override
            public void onResponse(Call<GetWRBillerFieldsResponseN> call1, Response<GetWRBillerFieldsResponseN> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");


                        GetWRBillerFieldsResponseN model = new GetWRBillerFieldsResponseN();
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
            public void onFailure(Call<GetWRBillerFieldsResponseN> call1, Throwable t) {
                GetWRBillerFieldsResponseN temp = new GetWRBillerFieldsResponseN();
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


    //GetBillDetails

    public LiveData<NetworkResource<GetWRBillDetailResponseN>> GetWRBillDetail(GetWRBillDetailRequestN request, String userName) {
        MutableLiveData<NetworkResource<GetWRBillDetailResponseN>> data = new MutableLiveData<>(); // receiving
        Call<GetWRBillDetailResponseN> call = restApi.GetWRBillDetail(RestClient.makeGSONRequestBody(request)
                , userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<GetWRBillDetailResponseN>() {
            @Override
            public void onResponse(Call<GetWRBillDetailResponseN> call1, Response<GetWRBillDetailResponseN> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");


                        GetWRBillDetailResponseN model = new GetWRBillDetailResponseN();
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
            public void onFailure(Call<GetWRBillDetailResponseN> call1, Throwable t) {
                GetWRBillDetailResponseN temp = new GetWRBillDetailResponseN();
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


    //BillPay
    public LiveData<NetworkResource<BillPayResponse>> getBillPay(BillPayRequest request, String userName) {
        MutableLiveData<NetworkResource<BillPayResponse>> data = new MutableLiveData<>(); // receiving
        Call<BillPayResponse> call = restApi.getBillPay(RestClient.makeGSONRequestBody(request)
                , userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<BillPayResponse>() {
            @Override
            public void onResponse(Call<BillPayResponse> call1, Response<BillPayResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        BillPayResponse model = new BillPayResponse();
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
            public void onFailure(Call<BillPayResponse> call1, Throwable t) {
                BillPayResponse temp = new BillPayResponse();
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


    public LiveData<NetworkResource<WRCountryListResponse>> getCountryList(Credentials request, String userName) {
        MutableLiveData<NetworkResource<WRCountryListResponse>> data = new MutableLiveData<>(); // receiving
        Call<WRCountryListResponse> call = restApi.getCountryList(RestClient.makeGSONRequestBody(request)
                , userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<WRCountryListResponse>() {
            @Override
            public void onResponse(Call<WRCountryListResponse> call1, Response<WRCountryListResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        WRCountryListResponse model = new WRCountryListResponse();
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
            public void onFailure(Call<WRCountryListResponse> call1, Throwable t) {
                WRCountryListResponse temp = new WRCountryListResponse();
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




    public LiveData<NetworkResource<BillDetailResponse>> getBillDetails(BillDetailRequest request, String userName) {
        MutableLiveData<NetworkResource<BillDetailResponse>> data = new MutableLiveData<>(); // receiving
        Call<BillDetailResponse> call = restApi.getBillDetails(RestClient.makeGSONRequestBody(request)
                , userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<BillDetailResponse>() {
            @Override
            public void onResponse(Call<BillDetailResponse> call1, Response<BillDetailResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        BillDetailResponse model = new BillDetailResponse();
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
            public void onFailure(Call<BillDetailResponse> call1, Throwable t) {
                BillDetailResponse temp = new BillDetailResponse();
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



    public LiveData<NetworkResource<PayBillPaymentResponse>> getPayBill(PayBillPaymentRequest request, String userName) {
        MutableLiveData<NetworkResource<PayBillPaymentResponse>> data = new MutableLiveData<>(); // receiving
        Call<PayBillPaymentResponse> call = restApi.getPayBill(RestClient.makeGSONRequestBody(request)
                , userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<PayBillPaymentResponse>() {
            @Override
            public void onResponse(Call<PayBillPaymentResponse> call1, Response<PayBillPaymentResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        PayBillPaymentResponse model = new PayBillPaymentResponse();
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
            public void onFailure(Call<PayBillPaymentResponse> call1, Throwable t) {
                PayBillPaymentResponse temp = new PayBillPaymentResponse();
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
