package ritman.wallet.Mobile_Top_Up.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;

import retrofit2.Call;
import retrofit2.Response;
import ritman.wallet.R;
import ritman.wallet.di.JSONdi.AppExecutors;
import ritman.wallet.di.JSONdi.NetworkResource;
import ritman.wallet.di.JSONdi.restRequest.BillDetailRequest;
import ritman.wallet.di.JSONdi.restRequest.MobileTopUpRequest;
import ritman.wallet.di.JSONdi.restRequest.PayBillPaymentRequest;
import ritman.wallet.di.JSONdi.restRequest.PlanCategoriesRequest;
import ritman.wallet.di.JSONdi.restRequest.PrepaidOperatorRequest;
import ritman.wallet.di.JSONdi.restRequest.PrepaidPlanRequest;
import ritman.wallet.di.JSONdi.restRequest.RechargePlansRequest;
import ritman.wallet.di.JSONdi.restResponse.BillDetailResponse;
import ritman.wallet.di.JSONdi.restResponse.MobileTopUpResponse;
import ritman.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import ritman.wallet.di.JSONdi.restResponse.PlanCategoriesResponse;
import ritman.wallet.di.JSONdi.restResponse.PrepaidOperatorResponse;
import ritman.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import ritman.wallet.di.JSONdi.restResponse.RechargePlansResponse;
import ritman.wallet.di.JSONdi.retrofit.RestApi;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.di.XMLdi.RequestHelper.WRPrepaidRechargeRequest;
import ritman.wallet.di.generic_response.SimpleResponse;

public class MobileTopUpViewModel extends ViewModel {

    RestApi restApi = RestClient.getBase();
    AppExecutors appExecutors = new AppExecutors();

    public MutableLiveData<WRPrepaidRechargeRequest> rechargeRequestLiveData = new MutableLiveData<>();
    public MutableLiveData<String> operatorCode = new MutableLiveData<>();
    public MutableLiveData<String> circleCode = new MutableLiveData<>();
    public MutableLiveData<String> mobileNo = new MutableLiveData<>();
    public MutableLiveData<String> operatorName = new MutableLiveData<>();



    public LiveData<NetworkResource<PrepaidOperatorResponse>> getOperator (PrepaidOperatorRequest request , String userName){
        MutableLiveData<NetworkResource<PrepaidOperatorResponse>> data = new MutableLiveData<>(); // receiving
        Call<PrepaidOperatorResponse> call = restApi.getOperator(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<PrepaidOperatorResponse>() {
            @Override
            public void onResponse(Call<PrepaidOperatorResponse> call1, Response<PrepaidOperatorResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        PrepaidOperatorResponse model = new PrepaidOperatorResponse();
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
            public void onFailure(Call<PrepaidOperatorResponse> call1, Throwable t) {
                PrepaidOperatorResponse temp = new PrepaidOperatorResponse();
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

    public LiveData<NetworkResource<PrepaidPlanResponse>> getPrepaidPlan (PrepaidPlanRequest request , String userName){//old one
        MutableLiveData<NetworkResource<PrepaidPlanResponse>> data = new MutableLiveData<>(); // receiving
        Call<PrepaidPlanResponse> call = restApi.getPrepaidPlan(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<PrepaidPlanResponse>() {
            @Override
            public void onResponse(Call<PrepaidPlanResponse> call1, Response<PrepaidPlanResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        PrepaidPlanResponse model = new PrepaidPlanResponse();
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
            public void onFailure(Call<PrepaidPlanResponse> call1, Throwable t) {
                PrepaidPlanResponse temp = new PrepaidPlanResponse();
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

    public LiveData<NetworkResource<MobileTopUpResponse>> getMobileTopUp (MobileTopUpRequest request , String userName){
        MutableLiveData<NetworkResource<MobileTopUpResponse>> data = new MutableLiveData<>(); // receiving
        Call<MobileTopUpResponse> call = restApi.getMobileTopUp(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<MobileTopUpResponse>() {
            @Override
            public void onResponse(Call<MobileTopUpResponse> call1, Response<MobileTopUpResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        MobileTopUpResponse model = new MobileTopUpResponse();
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
            public void onFailure(Call<MobileTopUpResponse> call1, Throwable t) {
                MobileTopUpResponse temp = new MobileTopUpResponse();
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


    public LiveData<NetworkResource<SimpleResponse>> mobileRecharge(WRPrepaidRechargeRequest request
            , String userName){
        MutableLiveData<NetworkResource<SimpleResponse>> data = new MutableLiveData<>(); // receiving
        Call<SimpleResponse> call = restApi.prepaidRecharge(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<SimpleResponse>() {
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
    }//old one


    public LiveData<NetworkResource<PlanCategoriesResponse>> getPlanName (PlanCategoriesRequest request , String userName){
        MutableLiveData<NetworkResource<PlanCategoriesResponse>> data = new MutableLiveData<>(); // receiving
        Call<PlanCategoriesResponse> call = restApi.getPlanName(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<PlanCategoriesResponse>() {
            @Override
            public void onResponse(Call<PlanCategoriesResponse> call1, Response<PlanCategoriesResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        PlanCategoriesResponse model = new PlanCategoriesResponse();
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
            public void onFailure(Call<PlanCategoriesResponse> call1, Throwable t) {
                PlanCategoriesResponse temp = new PlanCategoriesResponse();
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


    public LiveData<NetworkResource<RechargePlansResponse>> getRechargePlanName (RechargePlansRequest request , String userName){
        MutableLiveData<NetworkResource<RechargePlansResponse>> data = new MutableLiveData<>(); // receiving
        Call<RechargePlansResponse> call = restApi.getRechargePlanName(RestClient.makeGSONRequestBody(request)
                ,userName); // rest api declaration
        //init
        appExecutors.networkIO().execute(() -> call.enqueue(new retrofit2.Callback<RechargePlansResponse>() {
            @Override
            public void onResponse(Call<RechargePlansResponse> call1, Response<RechargePlansResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        RechargePlansResponse model = new RechargePlansResponse();
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
            public void onFailure(Call<RechargePlansResponse> call1, Throwable t) {
                RechargePlansResponse temp = new RechargePlansResponse();
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
