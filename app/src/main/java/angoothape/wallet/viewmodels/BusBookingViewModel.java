package angoothape.wallet.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.di.JSONdi.AppExecutors;
import angoothape.wallet.di.JSONdi.NetworkResource;
import angoothape.wallet.di.JSONdi.models.BoardingInfo;
import angoothape.wallet.di.JSONdi.models.RoutePickUp;
import angoothape.wallet.di.JSONdi.restRequest.BusBlockTicketRequest;
import angoothape.wallet.di.JSONdi.restRequest.BusBookingCancelConfirmRequest;
import angoothape.wallet.di.JSONdi.restRequest.BusBookingPreCancelRequest;
import angoothape.wallet.di.JSONdi.restRequest.BusSeatingLayoutRequest;
import angoothape.wallet.di.JSONdi.restRequest.BusTicketConfirmTicketRequest;
import angoothape.wallet.di.JSONdi.restRequest.GetBUSStationsRequest;
import angoothape.wallet.di.JSONdi.restRequest.GetBusAvailableServiceRequest;
import angoothape.wallet.di.JSONdi.restRequest.GetBusDestinationsRequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restResponse.BusBlockTicketResponse;
import angoothape.wallet.di.JSONdi.restResponse.BusBookingCancelResponse;
import angoothape.wallet.di.JSONdi.restResponse.BusBookingPreCancelResponse;
import angoothape.wallet.di.JSONdi.restResponse.BusSeatingLayoutResponse;
import angoothape.wallet.di.JSONdi.restResponse.BusTicketConfirmResponse;
import angoothape.wallet.di.JSONdi.restResponse.GetAvailableServicesResponse;
import angoothape.wallet.di.JSONdi.restResponse.GetBusStationsResponse;
import angoothape.wallet.di.JSONdi.restResponse.Services;
import angoothape.wallet.di.JSONdi.retrofit.RestApi;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusBookingViewModel extends ViewModel {

    RestApi restApi = RestClient.get();
    AppExecutors appExecutors = new AppExecutors();


    //data for blocktickets
    public String isAcSeat = "0";
    public String seatLayoutUniqueId = "";
    public String concessionId = "0";
    public String additionalInfoValue = "";

    public Services selectedServices;
    public BusBlockTicketRequest busBlockTicketRequest;
    public BusSeatingLayoutRequest busSeatingLayoutRequest;
    public List<BoardingInfo> selectedSeats;

    public BusBookingViewModel() {
        this.busSeatingLayoutRequest = new BusSeatingLayoutRequest();
        this.busBlockTicketRequest = new BusBlockTicketRequest();

        selectedSeats = new ArrayList<>();
    }

    public List<RoutePickUp> getBusRoute(List<String> pickUpService) {
        List<RoutePickUp> pickDetails = new ArrayList<>();
        for (String data :
                pickUpService) {
            String[] splitData = data.split("\\^");
            RoutePickUp pickUp = new RoutePickUp();
            pickUp.station = splitData[0];
            pickUp.time = splitData[1];
            pickUp.pointID = splitData[2];
            pickDetails.add(pickUp);
        }
        return pickDetails;
    }

    public List<RoutePickUp> getDropRoute(List<String> pickUpService) {
        List<RoutePickUp> pickDetails = new ArrayList<>();
        for (String data :
                pickUpService) {
            String[] splitData = data.split("\\^");
            RoutePickUp pickUp = new RoutePickUp();
            pickUp.station = splitData[1];
            pickUp.time = splitData[2];
            pickUp.pointID = splitData[0];
            pickDetails.add(pickUp);
        }
        return pickDetails;
    }

    public LiveData<NetworkResource<BusBookingCancelResponse>> busCancelBooking(BusBookingCancelConfirmRequest request, String key) {
        MutableLiveData<NetworkResource<BusBookingCancelResponse>> data = new MutableLiveData<>(); // receiving
        Call<BusBookingCancelResponse> call = restApi.busCancelBooking(RestClient.makeGSONRequestBody(request)
                , key); // rest api declaration

        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<BusBookingCancelResponse>() {
            @Override
            public void onResponse(Call<BusBookingCancelResponse> call1, Response<BusBookingCancelResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        BusBookingCancelResponse model = new BusBookingCancelResponse();
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
            public void onFailure(Call<BusBookingCancelResponse> call1, Throwable t) {
                BusBookingCancelResponse temp = new BusBookingCancelResponse();
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


    public LiveData<NetworkResource<BusBookingPreCancelResponse>> busPreCancellation(BusBookingPreCancelRequest request, String key) {
        MutableLiveData<NetworkResource<BusBookingPreCancelResponse>> data = new MutableLiveData<>(); // receiving
        Call<BusBookingPreCancelResponse> call = restApi.busBookingPreCancellation(RestClient.makeGSONRequestBody(request)
                , key); // rest api declaration

        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<BusBookingPreCancelResponse>() {
            @Override
            public void onResponse(Call<BusBookingPreCancelResponse> call1, Response<BusBookingPreCancelResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        BusBookingPreCancelResponse model = new BusBookingPreCancelResponse();
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
            public void onFailure(Call<BusBookingPreCancelResponse> call1, Throwable t) {
                BusBookingPreCancelResponse temp = new BusBookingPreCancelResponse();
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


    public LiveData<NetworkResource<BusTicketConfirmResponse>> busConfirmTicket(BusTicketConfirmTicketRequest request, String key) {
        MutableLiveData<NetworkResource<BusTicketConfirmResponse>> data = new MutableLiveData<>(); // receiving
        Call<BusTicketConfirmResponse> call = restApi.busConfirmTickets(RestClient.makeGSONRequestBody(request)
                , key); // rest api declaration

        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<BusTicketConfirmResponse>() {
            @Override
            public void onResponse(Call<BusTicketConfirmResponse> call1, Response<BusTicketConfirmResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        BusTicketConfirmResponse model = new BusTicketConfirmResponse();
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
            public void onFailure(Call<BusTicketConfirmResponse> call1, Throwable t) {
                BusTicketConfirmResponse temp = new BusTicketConfirmResponse();
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


    public LiveData<NetworkResource<BusBlockTicketResponse>> busBlockTickets(BusBlockTicketRequest request, String key) {
        MutableLiveData<NetworkResource<BusBlockTicketResponse>> data = new MutableLiveData<>(); // receiving
        Call<BusBlockTicketResponse> call = restApi.busBlockTicket(RestClient.makeGSONRequestBody(request)
                , key); // rest api declaration

        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<BusBlockTicketResponse>() {
            @Override
            public void onResponse(Call<BusBlockTicketResponse> call1, Response<BusBlockTicketResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        BusBlockTicketResponse model = new BusBlockTicketResponse();
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
            public void onFailure(Call<BusBlockTicketResponse> call1, Throwable t) {
                BusBlockTicketResponse temp = new BusBlockTicketResponse();
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

    public LiveData<NetworkResource<BusSeatingLayoutResponse>> getBusSeatingLayoutRequest(BusSeatingLayoutRequest request, String key) {
        MutableLiveData<NetworkResource<BusSeatingLayoutResponse>> data = new MutableLiveData<>(); // receiving
        Call<BusSeatingLayoutResponse> call = restApi.getServiceSeatingLayout(RestClient.makeGSONRequestBody(request)
                , key); // rest api declaration

        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<BusSeatingLayoutResponse>() {
            @Override
            public void onResponse(Call<BusSeatingLayoutResponse> call1, Response<BusSeatingLayoutResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        BusSeatingLayoutResponse model = new BusSeatingLayoutResponse();
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
            public void onFailure(Call<BusSeatingLayoutResponse> call1, Throwable t) {
                BusSeatingLayoutResponse temp = new BusSeatingLayoutResponse();
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

    public LiveData<NetworkResource<GetAvailableServicesResponse>> getAvailableService(GetBusAvailableServiceRequest request, String key) {
        MutableLiveData<NetworkResource<GetAvailableServicesResponse>> data = new MutableLiveData<>(); // receiving
        Call<GetAvailableServicesResponse> call = restApi.getAvailableService(RestClient.makeGSONRequestBody(request)
                , key); // rest api declaration

        appExecutors.networkIO().execute(() -> call.enqueue(new Callback<GetAvailableServicesResponse>() {
            @Override
            public void onResponse(Call<GetAvailableServicesResponse> call1, Response<GetAvailableServicesResponse> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");

                        GetAvailableServicesResponse model = new GetAvailableServicesResponse();
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
            public void onFailure(Call<GetAvailableServicesResponse> call1, Throwable t) {
                GetAvailableServicesResponse temp = new GetAvailableServicesResponse();
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
